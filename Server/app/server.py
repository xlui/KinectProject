#!/usr/bin/env python
# coding=utf-8
import socket
import threading
from contextlib import closing
from time import ctime

from app.push import JPush
from app.sqlite import Sqlite


class Server(object):
    def __init__(self, host='', port=21567, buffer_size=1024, timeout=2):
        super(Server, self).__init__()
        self.__host = host
        self.__port = port
        self.__buffer_size = buffer_size
        self.__timeout = timeout
        self.__address = (self.__host, self.__port)
        self.__login = []                       # 服务器维护一个登录用户列表。用户登录后添加用户到该列表，用户登出时移除。

        self.__socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.__socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)     # 使端口在一个连接关闭后可以重新使用
        try:
            self.__socket.bind(self.__address)
            self.__socket.listen(5)
        except Exception:
            raise

    def run(self):
        print("Waiting for connection...")
        while True:
            # 当接收到新的客户端连接，新开一个线程处理
            connection, address = self.__socket.accept()
            thread = threading.Thread(target=self.tcp_link, args=(connection, address))
            thread.start()

    def tcp_link(self, connection, address):
        # `address` 是一个包含客户端地址与端口号的元组(tuple)
        print("Accept new connection from %s:%s!" % address)
        user = 0

        while True:
            data = connection.recv(self.__buffer_size)
            # 当客户端仅仅连接服务器而没有发送数据时，服务器代码会在这里阻塞，直到有数据接收
            # 当客户端与服务器的 socket 连接断开后，data 的值会变为空字符串，利用这一点可以让服务器及时断开连接
            print("Data received(from %s:%s): " % address, data)

            flag, to_send = self.data_process(data)
            # flag 是循环中止标志。当 flag 为 1 时，中止循环；flag 为 0 时，继续循环
            try:
                to_send, user = to_send.split('id:')
                # 尝试从 `data_process` 函数处理后返回的数据中读取用户名，并忽略读取失败的情况
                # 这样设计是为了便于循环中止（用户登出）时及时将用户从登陆用户列表中移除
            except ValueError:
                pass

            if flag:
                break

            to_send = to_send + '\n'
            print("Send data to client: ", to_send)
            connection.send(to_send.encode('utf-8'))

        connection.close()
        try:
            self.__login.remove(user)
        except ValueError:
            pass
        print("Connection from %s:%s closed." % address)

    def data_process(self, data):
        # 数据处理，返回两个变量。其中包括一个循环中止变量，`to_send` 是回复客户端的变量
        # `flag = 1`，停止循环；`flag = 0`，继续循环
        decode_data = data.decode('utf-8').strip('\n')
        # 解码接收到的数据，并去掉换行符。经过处理后， `decode_data` 中全部是有意义的字符
        to_send = 'Get data: {}\n[{}]'.format(data.decode('utf-8'), ctime())
        # 默认的返回值

        if len(data) == 0 or decode_data.lower() == 'exit':
            # `len(data) == 0` 表明客户端连接中断
            # `decode` 是 `exit` 表明客户端主动退出
            return 1, to_send

        if 'id:' in decode_data:
            # 安卓客户端登录模块
            username = decode_data[3:]
            with closing(Sqlite()) as sqlite:
                result = sqlite.query_registration_id(username)
                if result:
                    # 成功在数据库中找到 `username` 对应的 `registration_id`
                    print("Android client's registration id: ", result)
                    if username not in self.__login:
                        to_send = 'success'
                        self.__login.append(username)
                        return 0, to_send + decode_data
                    else:
                        to_send = 'already'
                        return 0, to_send
                else:
                    # 在数据库中没有找到 `username` 对应的 `registration_id`
                    to_send = 'failed'
                    return 0, to_send

        if 'csharp:' in decode_data:
            # C# 客户端登陆模块
            username = decode_data[7:]
            with closing(Sqlite()) as sqlite:
                result = sqlite.query_username(username)
            if result:
                # 成功在数据库中找到用户名
                print("C# client's id: ", username)
                to_send = 'success'
                return 0, to_send
            else:
                # 在数据库中未找到用户名
                to_send = 'failed'
                return 0, to_send

        if 'code:' in decode_data:
            # 发送自 C# 客户端，要转发给安卓客户端的原始代码
            # 原始代码格式是：code:1username:2
            disposed = decode_data.split('code:')[1].split('username:')
            code, username = disposed
            # 对格式化数据进行处理，从中提取出手势代码和发送用户
            with closing(Sqlite()) as sqlite:
                registration_id = sqlite.query_registration_id(username)
            to_push = decode_data.split('username')[0]
            # 截取代码的前半部分用于发送给安卓设备，代码格式：code:1

            jpush = JPush()
            if to_push == 'code:1':
                # 对于特殊的代码，进行特别的响应
                # if username in self.__login:
                    # 确认安卓用户已经登陆。对于登陆用户，同时发送消息和通知栏通知
                print("Preparing to send special code to {}!".format(registration_id))
                jpush.push_message_registration_id(registration_id, to_push)
                jpush.push_notification_registration_id(registration_id, "Hand Open")
            else:
                # 对于正常代码，仅仅发送消息
                jpush.push_message_registration_id(registration_id, to_push)

        if 'register' in decode_data:
            # 安卓客户端注册代码，注册代码格式是：register:2:registrationID:qwerty4ui
            _, username, _, registration_id = decode_data.split(':')
            # 从原始代码中提取出用户名和注册ID
            print("New Android user register: {}[{}]".format(username, registration_id))
            with closing(Sqlite()) as sqlite:
                found = sqlite.query_username(username)
            if found:
                # 在数据库中查询到用户名，表明用户已经注册
                return 0, "already_registered"
            else:
                sqlite = Sqlite()
                try:
                    sqlite.save(username, registration_id)
                except Exception:
                    return 0, "register_failed"
                finally:
                    sqlite.close()
                return 0, "register_success"

        return 0, to_send


if __name__ == "__main__":
    try:
        Server().run()
    except KeyboardInterrupt as e:
        print('Exit!')

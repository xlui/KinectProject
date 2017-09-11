#!/usr/bin/env python
# coding=utf-8
import socket
import threading
from time import ctime
from contextlib import closing
from mysql import MySQL
from push import JPush


class Server(object):
    """Server class"""

    def __init__(self, host='', port=21567, bufsize=1024, timeout=2):
        super(Server, self).__init__()
        self.__host = host
        self.__port = port
        self.__bufsize = bufsize
        self.__timeout = timeout
        self.__address = (self.__host, self.__port)
        self.__login = []

        self.__socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.__socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        # set this to reuse port
        try:
            self.__socket.bind(self.__address)
            self.__socket.listen(5)
        except Exception:
            raise

    def run(self):
        print("Waiting for connection...")
        while True:
            # when connection arrived, open a new thread
            connection, address = self.__socket.accept()
            thread = threading.Thread(
                target=self.tcp_link, args=(connection, address))
            thread.start()

    def tcp_link(self, connection, address):
        # address is a tuple of host and port for client
        print("Accept new connection from %s:%s!" % address)
        user = 0

        while True:
            # wait to receive data from client
            flag = 0
            # flag to continue loop or stop
            data = connection.recv(self.__bufsize)
            # receive data
            print("Data received(from %s:%s): " % address, data)

            flag, to_send = self.data_process(data)
            try:
                to_send, user = to_send.split('id:')
            except ValueError:
                pass

            if flag:
                break

            self.send(connection, to_send + '\n')

        connection.close()
        # try:
        #     self.__login.remove(user)
        # except ValueError:
        #     pass
        print("Connection from %s:%s closed." % address)

    def send(self, connection, to_send):
        print("Send data to client: ", to_send)
        connection.send(to_send.encode('utf-8'))

    def data_process(self, data):
        # data process, return two variables, flag and to_send
        # flag = 1, break receive loop;
        # flag = 0, continue loop
        # to_send is a string which will be sent to client

        decode_data = data.decode('utf-8').lower().strip('\n')
        # get decode data and transform to lowercase
        to_send = 'Get data: {}\n[{}]'.format(data.decode('utf-8'), ctime())
        # set default to_send

        if len(data) == 0 or decode_data == 'exit':
            return 1, to_send

        if 'id:' in decode_data:
            # Android client login
            username = decode_data[3:]
            with closing(MySQL()) as mysql:
                result = mysql.find_registration_id(username)
                if result:
                    # find registration_id in database
                    print("Android client's registration id: ", result)
                    if username not in self.__login:
                        to_send = 'success'
                        self.__login.append(username)
                        return 0, to_send + decode_data
                    else:
                        to_send = 'already'
                        return 0, to_send
                else:
                    # cannot find registration id in database, response 'failed'
                    to_send = 'failed'
                    return 0, to_send

        if 'csharp:' in decode_data:
            # C# client login
            username = decode_data[7:]
            with closing(MySQL()) as mysql:
                result = mysql.find_username(username)
            if result:
                # find username in database
                print("C# client's id: ", username)
                to_send = 'success'
                return 0, to_send
            else:
                to_send = 'failed'
                return 0, to_send

        if 'code:' in decode_data:
            # the format of received data is:  code:1username:2
            # now cut out code and username
            dealed = decode_data.split('code:')[1].split('username:')
            code, username = dealed
            with closing(MySQL()) as mysql:
                registration_id = mysql.find_registration_id(username)
            to_push = decode_data.split('username')[0]

            jpush = JPush()
            if to_push == 'code:1':
                # bug here! 'code:1' is 'code:1' = False
                if username in self.__login:
                    jpush.push_notification_registration_id(registration_id, "Hand Open")
                # todo: to send notification to special device, should detect the login state first
                print('username: ', username, 'login: ', self.__login)
                jpush.push_message_registration_id(registration_id, to_push)
            else:
                jpush.push_message_registration_id(registration_id, to_push)

        if 'register' in decode_data:
            # todo: feature: android client register
            pass


        return 0, to_send


if __name__ == "__main__":
    try:
        Server().run()
    except KeyboardInterrupt as e:
        print('Exit!')

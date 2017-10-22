#!/usr/bin/env python
# coding=utf-8
import socket

# HOST = "111.231.1.210"					# server IP address, used to deploy
HOST = "192.168.1.165"						# local IP address, used to test


class Client(object):
    """Client class"""

    def __init__(self, host=HOST, port=21567, bufsize=1024):
        super(Client, self).__init__()
        self.__host = host
        self.__port = port
        self.__bufsize = bufsize
        self.address = (self.__host, self.__port)
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    def run(self):
        try:
            self.socket.connect(self.address)
        except ConnectionRefusedError as e:
            print("Connection Refused, Server is unreachable!")
            exit(1)

        print('Successfully connect to server %s:%s' % self.address)

        while True:
            data = input('please input data to send to server: ')

            if not data:
                continue
            if data.lower() == 'exit':
                print('Exit!')
                break

            try:
                self.socket.send(data.encode('utf-8'))
            except BrokenPipeError as e:
                print('connection stoped unexpectedly, '
                      'please restart the program!')
                exit(1)

            receive = self.socket.recv(self.__bufsize)
            print(receive.decode('utf-8').strip())

    def stop(self):
        try:
            self.socket.send('exit'.encode('utf-8'))
        except BrokenPipeError as e:
            pass
        self.socket.close()


if __name__ == '__main__':
    client = Client()
    try:
        client.run()
    except KeyboardInterrupt as e:
        print('Exit!')
    except EOFError as e:
        print('Exit!')
    finally:
        client.stop()

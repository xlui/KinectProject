#!/usr/bin/env python
# coding:utf-8

from app.server import Server

try:
    server = Server()
    server.run()
except KeyboardInterrupt as e:
    print('Exit!')

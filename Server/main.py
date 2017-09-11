#!/usr/bin/env python
# coding:utf-8
# main module, to run server project

import server


try:
    server.Server().run()
except KeyboardInterrupt as e:
    print('Exit!')

import os
import platform

basedir = os.path.abspath(os.path.dirname(__file__))
linux_database = '/tmp/dev.sqlite'
windows_database = os.path.join(basedir, 'dev.sqlite')
local_database = windows_database if 'Windows' in platform.platform() else linux_database


class Config:
    SECRET_KEY = "This is secret key to use SCRF, must be hard to guess"
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    SQLALCHEMY_COMMIT_ON_TEARDOWN = True
    SQLALCHEMY_DATABASE_URI = 'sqlite:///' + (windows_database if 'Windows' in platform.platform() else linux_database)

    @staticmethod
    def init_app(app):
        pass

import os
import platform

basedir = os.path.abspath(os.path.dirname(__file__))
linux_database = 'sqlite:////tmp/dev.sqlite'
windows_database = 'sqlite:///' + os.path.join(basedir, 'dev.sqlite')


class Config:
    SECRET_KEY = "This is secret key to use SCRF, must be hard to guess"
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    SQLALCHEMY_COMMIT_ON_TEARDOWN = True
    SQLALCHEMY_DATABASE_URI = windows_database if 'Windows' in platform.platform() else linux_database

    @staticmethod
    def init_app(app):
        pass

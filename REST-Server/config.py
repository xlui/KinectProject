import os

basedir = os.path.abspath(os.path.dirname(__file__))


class Config:
    SECRET_KEY = "This is secret key to use SCRF, must be hard to guess"
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    SQLALCHEMY_COMMIT_ON_TEARDOWN = True
    SQLALCHEMY_DATABASE_URI = 'sqlite:////tmp/dev.sqlite' or 'sqlite:///' + os.path.join(basedir, 'dev.sqlite')

    @staticmethod
    def init_app(app):
        pass

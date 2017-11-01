import os
import platform
from flask_uploads import IMAGES
from functools import reduce

# Environment variables
# project base directory name
_basedir = os.path.abspath(os.path.dirname(__file__))
# platform
_platform = platform.platform()
# database location
_linux_database = '/tmp/dev.sqlite'
_windows_database = os.path.join(_basedir, 'dev.sqlite')
database = _windows_database if 'Windows' in _platform else _linux_database
# upload folder
_linux_uploads = '/tmp/static/uploads'
_windows_uploads = reduce(os.path.join, [_basedir, 'app', 'static', 'uploads'])
uploads = _windows_uploads if 'Windows' in _platform else _linux_uploads


class Config:
    SECRET_KEY = "This is secret key to use SCRF, must be hard to guess"
    # SQLALCHEMY
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    SQLALCHEMY_COMMIT_ON_TEARDOWN = True
    SQLALCHEMY_DATABASE_URI = 'sqlite:///' + database
    # Uploads
    UPLOADED_PHOTOS_DEST = uploads
    UPLOADED_PHOTOS_ALLOW = IMAGES

    @staticmethod
    def init_app(app):
        pass

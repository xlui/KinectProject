from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from config import Config

__version__ = '0.2.0'
db = SQLAlchemy()


def create_app():
    app = Flask(__name__)
    app.config.from_object(Config)

    Config.init_app(app)
    db.init_app(app)

    from .api import api as api_blueprint
    app.register_blueprint(api_blueprint, url_prefix='/api/v{}'.format(__version__))

    return app

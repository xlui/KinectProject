from flask import Flask


def create_app():
    app = Flask(__name__)
    app.config.from_object(None)

    from .api_0_1_0 import api as api_0_1_0_blueprint
    app.register_blueprint(api_0_1_0_blueprint, url_prefix='/api/v0.1.0')

    return app

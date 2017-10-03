from flask import make_response, jsonify

from . import api


@api.app_errorhandler(404)
def page_not_found(error):
    return make_response(jsonify({'error':'Not Found'}), 404)


@api.route('/')
def index():
    return '<div align="center"><h1>Hello World</h1></div>'

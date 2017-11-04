from flask import make_response, jsonify

from . import api


@api.app_errorhandler(400)
def bad_request(error):
    return make_response(jsonify({'error': 'Bad request!'}), 400)


@api.app_errorhandler(404)
def page_not_found(error):
    return make_response(jsonify({'error': 'Not Found'}), 404)


@api.app_errorhandler(500)
def internal_server_error(error):
    return make_response(jsonify({'error': 'The server encountered an internal error and was unable to complete your '
                                           'request. Either the server is overloaded or there is an error in the '
                                           'application.'}), 500)


@api.route('/')
def index():
    return '<div align="center"><h1>Hello World</h1></div>'

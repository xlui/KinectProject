from flask import g, jsonify, make_response
from flask_httpauth import HTTPBasicAuth, HTTPTokenAuth, MultiAuth

from . import api
from ..models import User

basic_auth = HTTPBasicAuth()
token_auth = HTTPTokenAuth(scheme="Dev")
multi_auth = MultiAuth(basic_auth, token_auth)


@basic_auth.verify_password
def verify_password(username, password):
    if username == '':
        return False
    user = User.query.filter_by(username=username).first()
    if not user:
        return False
    g.current_user = user
    g.token_used = False
    return user.verify_password(password)


@token_auth.verify_token
def verify_token(token):
    g.current_user = User.verify_auth_token(token)
    g.token_used = True
    return g.current_user is not None


@basic_auth.error_handler
def basic_auth_error():
    return make_response(jsonify({'error': 'unknown username or password'}), 401)


@token_auth.error_handler
def token_auth_error():
    return make_response(jsonify({'error': 'unknown username or password'}), 401)


@api.before_request
@multi_auth.login_required
def before_request():
    if not g.current_user:
        return jsonify({'error': 'login required!'})


@api.route('/token')
def get_token():
    if g.token_used:
        return make_response(jsonify({'error': 'Invalid credentials'}), 405)
    return jsonify({'token': g.current_user.generate_auth_token(3600).decode('utf-8'), 'expiration': 3600})

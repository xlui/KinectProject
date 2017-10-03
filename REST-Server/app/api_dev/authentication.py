from flask import g, jsonify
from flask_httpauth import HTTPBasicAuth

from ..models import User

auth = HTTPBasicAuth()


@auth.verify_password
def verify_password(username, password):
    if username == '':
        return False
    # if password == '':
    #     g.current_user = User.verify_auth_token(username)
    #     g.token_used = True
    #     return g.current_user is not None
    user = User.query.filter_by(username=username).first()
    if not user:
        return False
    g.current_user = user
    # g.token_used = False
    return user.verify_password(password)


@auth.error_handler
def auth_error():
    return jsonify({'error': 'unknown username or password'})

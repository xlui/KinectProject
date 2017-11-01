# 认证模块
from flask import g, jsonify, make_response
from flask_httpauth import HTTPBasicAuth, HTTPTokenAuth, MultiAuth

from . import api
from ..models import User

basic_auth = HTTPBasicAuth()
# 用户名密码认证
token_auth = HTTPTokenAuth(scheme="Dev")
# 基于 token 的认证
multi_auth = MultiAuth(basic_auth, token_auth)
# multi_auth.login_required = basic_auth_login_required + token_auth.login_required


@basic_auth.verify_password
def verify_password(username, password):
    """用户名密码验证"""
    if username == '':
        return False
    user = User.query.filter_by(username=username).first()
    if not user:
        return False
    g.current_user = user
    # 全局变量记录当前登陆用户
    g.token_used = False
    # 全局变量记录是否使用 token 登陆
    return user.verify_password(password)


@token_auth.verify_token
def verify_token(token):
    """token 登陆验证"""
    g.current_user = User.verify_auth_token(token)
    # 全局变量记录当前登陆用户
    g.token_used = True
    # 全局变量记录是否使用 token 登陆
    return g.current_user is not None


@basic_auth.error_handler
def basic_auth_error():
    """username-password auth Error handler"""
    return make_response(jsonify({'login': 'failed'}), 401)


@token_auth.error_handler
def token_auth_error():
    """token auth error handler"""
    return make_response(jsonify({'login': 'failed'}), 401)


@api.route('/token', methods=['GET'])
@multi_auth.login_required
def get_token():
    """Generate a new token when token not used"""
    if g.token_used:
        return make_response(jsonify({'token': 'Invalid credentials'}), 405)
    expiration = 3600 * 24 * 30 # 3600 = 1 hour, extend token's expiration to one month
    return make_response(jsonify({'token': g.current_user.generate_auth_token(expiration=expiration).decode('utf-8'), 'expiration': expiration}), 200)

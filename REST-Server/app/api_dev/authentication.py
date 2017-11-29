"""
认证模块，对用户身份进行认证
"""

from flask import g, jsonify, make_response, current_app, abort
from flask_httpauth import HTTPBasicAuth, HTTPTokenAuth, MultiAuth
import sqlite3

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
    """用户名密码认证

    :param username: 用户名
    :param password: 密码
    :return: true or false
    """
    if username == '':
        return False
    user = None
    try:
        user = User.query.filter_by(username=username).first()
    except sqlite3.OperationalError as e:
        # no table in database
        current_app.logger.debug(e)
        abort(500)
    if not user:
        return False
    g.current_user = user
    g.user_id = user.username
    # 全局字典变量 g 记录当前登陆用户
    g.token_used = False
    # 全局字典变量 g 记录是否使用 token 登陆
    return user.verify_password(password)


@token_auth.verify_token
def verify_token(token):
    """token 登陆验证"""
    try:
        g.current_user = User.verify_auth_token(token)
        g.user_id = int(g.current_user.username)
        # 全局变量记录当前登陆用户
    except Exception as e:
        current_app.logger.debug(e)
    g.token_used = True
    # 全局变量记录是否使用 token 登陆
    return g.current_user is not None


@basic_auth.error_handler
def basic_auth_error():
    """用户名密码验证错误返回"""
    return make_response(jsonify({'login': 'failed'}), 401)


@token_auth.error_handler
def token_auth_error():
    """token 验证错误返回"""
    return make_response(jsonify({'login': 'failed'}), 401)


@api.route('/token', methods=['GET'])
@multi_auth.login_required
def get_token():
    """生成 token"""
    if g.token_used:
        # 如果使用 token 登录，拒绝申请
        return make_response(jsonify({'token': 'Invalid credentials'}), 405)
    expiration = 3600 * 24 * 30  # 3600 = 1 hour, extend token's expiration to one month
    return jsonify({'token': g.current_user.generate_auth_token(expiration=expiration).decode('utf-8'),
                    'expiration': expiration})

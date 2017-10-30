from flask import jsonify, request, abort, make_response, url_for, redirect
from sqlalchemy import func

from . import api
from .authentication import multi_auth
from .. import db
from ..models import User, State, History


@api.route('/login', methods=['GET'])
@multi_auth.login_required
def login():
    """client login"""
    return make_response(jsonify({'login': 'success'}), 200)


@api.route('/register', methods=['POST'])
def register():
    """user register"""
    if not request.json or not 'username' in request.json:
        abort(400)
    user = User(username=request.json.get('username'),
                password=request.json.get('password'))
    db.session.add(user)
    db.session.commit()
    return make_response(jsonify({'register': 'success'}), 200)


@api.route('/pictures/<name>', methods=['GET'])
@multi_auth.login_required
def picture(name):
    root_url = 'https://nxmup.com'
    return root_url + url_for('static', filename='images/' + name)


@api.route('/latest', methods=['GET'])
@multi_auth.login_required
def index():
    """Will only show the latest hand state"""
    latest = db.session.query(func.max(State.id)).first()[0]
    state = State.query.get(latest)
    return make_response(jsonify({'state': state.get_json()}), 200)


@api.route('/update', methods=['POST'])
@multi_auth.login_required
def update():
    """update latest hand state"""
    if not request.json or not 'state' in request.json:
        abort(400)
    state = State(state=request.json.get('state'))
    db.session.add(state)
    db.session.commit()
    return make_response(jsonify({'state': state.get_json()}), 200)


@api.route('/history', methods=['GET'])
@multi_auth.login_required
def history():
    """show history hand state of user."""
    histories = [_history.get_json() for _history in History.query.all()]
    return make_response(jsonify(histories))

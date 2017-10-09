from flask import jsonify, request, abort, make_response
from sqlalchemy import func

from . import api
from .. import db
from ..models import State


@api.route('/login', methods=['GET'])
def login():
    """client login"""
    return make_response(jsonify({'login': 'success'}), 200)


@api.route('/latest', methods=['GET'])
def index():
    """Will only show the latest hand state"""
    latest = db.session.query(func.max(State.id)).first()[0]
    state = State.query.get(latest)
    return make_response(jsonify({'state': state.get_json()}), 200)


@api.route('/update', methods=['POST'])
def update():
    """update latest hand state"""
    if not request.json or not 'state' in request.json:
        abort(400)
    state = State(state=request.json.get('state'))
    db.session.add(state)
    db.session.commit()
    return make_response(jsonify({'state': state.get_json()}), 200)

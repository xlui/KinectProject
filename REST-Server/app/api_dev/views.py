from flask import jsonify, request, abort
from sqlalchemy import func

from . import api
from .. import db
from ..models import State


@api.route('/', methods=['GET'])
def index():
    """Will only show the latest hand state"""
    latest = db.session.query(func.max(State.id)).first()[0]
    state = State.query.get(latest)
    return jsonify({'state': state.get_json()})


@api.route('/update', methods=['POST'])
def update():
    """update latest hand state"""
    if not request.json or not 'state' in request.json:
        abort(400)
    state = State(state=request.json.get('state'))
    db.session.add(state)
    db.session.commit()
    return jsonify({'state': state.get_json()})

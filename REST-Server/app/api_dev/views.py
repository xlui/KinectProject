from flask import jsonify

from . import api
from .authentication import auth
from ..models import State


@api.route('/')
@auth.login_required
def index():
    """Will only show the latest hand state"""
    state = State.query.order_by(State.timestamp.desc()).first()
    return jsonify({'state': state.get_json()})

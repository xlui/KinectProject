from flask import jsonify
from sqlalchemy import func

from . import api
from .. import db
from ..models import State


@api.route('/')
def index():
    """Will only show the latest hand state"""
    latest = db.session.query(func.max(State.id)).first()[0]
    state = State.query.get(latest)
    return jsonify({'state': state.get_json()})

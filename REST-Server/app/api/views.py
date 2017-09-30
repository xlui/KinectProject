from flask import jsonify

from . import api
from .. import db
from ..models import State


@api.route('/')
def index():
    states = State.query.all()
    return jsonify({'states': [state.get_json() for state in states]})

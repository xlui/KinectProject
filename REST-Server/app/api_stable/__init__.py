from flask import Blueprint

api = Blueprint('api_stable', __name__)

from . import views

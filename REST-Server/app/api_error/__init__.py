from flask import Blueprint

api = Blueprint('api_error', __name__)

from . import views
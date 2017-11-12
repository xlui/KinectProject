from flask import Blueprint

api = Blueprint('api_dev', __name__)

from . import views, authentication, tmp_view

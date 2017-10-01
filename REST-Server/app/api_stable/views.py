from . import api


@api.route('/')
def index():
    return '<h1>Hello World</h1>'

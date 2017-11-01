from flask import jsonify, request, abort, make_response, url_for, g, redirect, render_template
from sqlalchemy import func

from . import api
from .authentication import multi_auth
from .. import db
from ..models import User, State, History, Picture


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


@api.route('/latest', methods=['GET'])
@multi_auth.login_required
def index():
    """Will only show the latest hand state"""
    latest = db.session.query(func.max(State.id)).first()[0]
    state = State.query.get(latest)
    return make_response(jsonify({'state': state.get_json(), 'userId': int(g.current_user.username)}), 200)


@api.route('/update', methods=['POST'])
@multi_auth.login_required
def update():
    """update latest hand state"""
    if not request.json or not 'state' in request.json:
        abort(400)
    state = State(state=request.json.get('state'))
    _history = History(userId=int(g.current_user.username), state=request.json.get('state'))
    db.session.add(state)
    db.session.add(_history)
    db.session.commit()
    return make_response(jsonify({'state': state.get_json()}), 200)


@api.route('/history', methods=['GET'])
@multi_auth.login_required
def history():
    """show history hand state of user."""
    user_id = int(g.current_user.username)
    user_histories = History.query.order_by(History.id.desc()).all()
    histories = [_history.get_json() for _history in user_histories]
    return make_response(jsonify(histories))


@api.route('/picture/<name>', methods=['GET'])
@multi_auth.login_required
def pictures(name):
    """Hand state pictures, get by name"""
    picture_url = url_for('static', filename='images/' + name)
    return render_template('show.html', url=picture_url)


@api.route('/upload', methods=['POST'])
@multi_auth.login_required
def upload():
    from .. import photos
    from datetime import datetime
    saved_name = 'user_' + str(g.current_user.username) + datetime.now().strftime('_%Y_%m_%d_%H_%M.')
    file = request.files.get('file')
    if file:
        filename = photos.save(file, name=saved_name)
        picture = Picture(user=g.current_user, filename=filename)
        db.session.add(picture)
        return jsonify({'upload': 'success', 'imageUrl': photos.url(filename)})
    return jsonify({'upload': 'failed', 'imageUrl': None})


@api.route('/photo/<name>', methods=['GET'])
def show(name):
    from .. import photos
    if name is None:
        abort(404)
    url = photos.url(name)
    print(name)
    print(url)
    return render_template('show.html', url=url, name=name)


@api.route('/current_picture', methods=['GET'])
@multi_auth.login_required
def current_picture():
    from .. import photos
    _picture = Picture.query.order_by(Picture.filename.desc()).first()
    if not _picture:
        abort(404)
    return render_template('show.html', url=photos.url(_picture.filename))

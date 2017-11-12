from flask import jsonify, request, abort, make_response, url_for, g, render_template, current_app
from sqlalchemy import func

from . import api
from .authentication import multi_auth
from .. import db
from ..models import User, State, Picture


@api.route('/login', methods=['GET'])
@multi_auth.login_required
def login():
    """client login: username:password or token"""
    return make_response(jsonify({'login': 'success'}), 200)


@api.route('/register', methods=['POST'])
def register():
    """user register: json format data only"""
    if not request.json or 'username' not in request.json:
        abort(400)
    user = User.query.get(request.json.get('username'))
    if user:
        return jsonify({'register': 'failed', 'reason': 'username already exists!'})
    else:
        user = User(username=request.json.get('username'),
                    password=request.json.get('password'))
        db.session.add(user)
        db.session.commit()
        return jsonify({'register': 'success'})


@api.route('/latest', methods=['GET'])
@multi_auth.login_required
def latest():
    """Will only show the latest hand state"""
    _latest = State.query.filter_by(user_id=g.user_id).order_by(State.id.desc()).first()
    return jsonify({'state': _latest.get_json(), 'user_id': g.user_id})


@api.route('/update', methods=['POST'])
@multi_auth.login_required
def update():
    """update latest gesture state"""
    if not request.json or not 'state' in request.json:
        abort(400)
    state = State(state=request.json.get('state'), danger=request.json.get('danger'), user_id=g.user_id)
    db.session.add(state)
    db.session.commit()
    return jsonify({'state': state.get_json()})


@api.route('/history', methods=['GET'])
@multi_auth.login_required
def history():
    """show history hand state of user."""
    user_histories = State.query.filter_by(user_id=g.user_id).order_by(State.id.desc()).all()
    histories = [_history.get_json() for _history in user_histories]
    return jsonify(histories)


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
    saved_name = 'user_' + str(g.user_id) + datetime.now().strftime('_%Y_%m_%d_%H_%M.')
    file = request.files.get('file')
    if file:
        filename = photos.save(file, name=saved_name)
        picture = Picture(user=g.current_user, filename=filename)
        db.session.add(picture)
        return jsonify({'upload': 'success', 'imageUrl': photos.url(filename)})
    return jsonify({'upload': 'failed', 'imageUrl': None})


@api.route('/photo/<name>', methods=['GET'])
def show(name):
    """Show images"""
    from .. import photos
    if name is None:
        abort(404)
    url = photos.url(name)
    return render_template('show.html', url=url, name=name)


@api.route('/latest_picture', methods=['GET'])
@multi_auth.login_required
def latest_picture():
    """Get the latest picture uploaded through kinect"""
    from .. import photos
    max_id = 0
    try:
        max_id = db.session.query(func.max(Picture.id)).first()[0]
    except Exception as e:
        current_app.logger.debug(e)
        abort(404)
    _picture = Picture.query.get(max_id)
    if not _picture:
        abort(404)
    return jsonify({'url': photos.url(_picture.filename),
                    'date': _picture.date})


@api.route('/pics', methods=['GET'])
def pics():
    _pics = Picture.query.order_by(Picture.id.desc())
    _pics = [_pic.get_json() for _pic in _pics]
    return jsonify(_pics)

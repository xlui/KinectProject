"""
主要 API
"""

from flask import jsonify, request, abort, make_response, g, current_app

from . import api
from .authentication import multi_auth
from .. import db
from ..models import User, State, Picture, Train


@api.route('/login', methods=['GET'])
@multi_auth.login_required
def login():
    """客户端登陆，用户名密码或者 token"""
    return make_response(jsonify({'login': 'success'}), 200)


@api.route('/register', methods=['POST'])
def register():
    """用户注册，只接收 json 格式的数据"""
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
    """获取最新的手势状态"""
    _latest = g.current_user.state.order_by(State.id.desc()).first()
    return jsonify({'state': _latest.get_json(), 'user_id': g.user_id})


@api.route('/update', methods=['POST'])
@multi_auth.login_required
def update():
    """kinect 端更新手势状态"""
    if not request.json or not 'state' in request.json:
        abort(400)
    state = State(state=request.json.get('state'), danger=request.json.get('danger'), user=g.current_user)
    db.session.add(state)
    db.session.commit()
    return jsonify({'state': state.get_json()})


@api.route('/history', methods=['GET'])
@multi_auth.login_required
def history():
    """历史手势"""
    _histories = g.current_user.state.order_by(State.id.desc()).all()
    _json_history = [_history.get_json() for _history in _histories]
    return jsonify(_json_history)


@api.route('/upload', methods=['POST'])
@multi_auth.login_required
def upload():
    """上传图片"""
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


@api.route('/latest_picture', methods=['GET'])
@multi_auth.login_required
def latest_picture():
    """获取最新图片"""
    from .. import photos
    try:
        _pic = g.current_user.picture.order_by(Picture.id.desc()).first()
        if not _pic:
            abort(404)
        return jsonify({'url': photos.url(_pic.filename),
                        'date': _pic.date})
    except Exception as e:
        current_app.logger.debug(e)
        abort(404)


@api.route('/train', methods=['GET', 'POST'])
@multi_auth.login_required
def train():
    """训练结果"""
    if request.method == 'POST':
        if not request.json:
            abort(400)
        else:
            result = request.json.get('result')
            if result:
                train_result = Train(result=result, user=g.current_user)
                db.session.add(train_result)
                db.session.commit()
                return jsonify(train_result.get_json())
            else:
                abort(400)
    _trains = User.query.filter_by(username=g.user_id).first().train.order_by(Train.id.desc()).all()
    _train_results = [_t.get_json() for _t in _trains]
    return jsonify({'train': _train_results})

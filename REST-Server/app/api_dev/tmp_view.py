"""
仅供测试使用的 api
"""

from flask import jsonify, abort, render_template, url_for

from . import api
from ..models import Picture


@api.route('/picture/<name>', methods=['GET'])
def pictures(name):
    """forget the usage of this API"""
    picture_url = url_for('static', filename='images/' + name)
    return render_template('show.html', url=picture_url)


@api.route('/photo/<name>', methods=['GET'])
def show(name):
    """查看特定名字图片"""
    from .. import photos
    if name is None:
        abort(404)
    url = photos.url(name)
    return render_template('show.html', url=url, name=name)


@api.route('/pics', methods=['GET'])
def pics():
    """查看数据库中图片名字列表"""
    _pics = Picture.query.order_by(Picture.id.desc())
    _pics = [_pic.get_json() for _pic in _pics]
    return jsonify(_pics)

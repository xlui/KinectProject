from flask import jsonify, abort, render_template, url_for

from . import api
from ..models import Picture


@api.route('/picture/<name>', methods=['GET'])
def pictures(name):
    """Hand state pictures, get by name"""
    picture_url = url_for('static', filename='images/' + name)
    return render_template('show.html', url=picture_url)


@api.route('/photo/<name>', methods=['GET'])
def show(name):
    """Show images by image name"""
    from .. import photos
    if name is None:
        abort(404)
    url = photos.url(name)
    return render_template('show.html', url=url, name=name)


@api.route('/pics', methods=['GET'])
def pics():
    """Show all pics in database"""
    _pics = Picture.query.order_by(Picture.id.desc())
    _pics = [_pic.get_json() for _pic in _pics]
    return jsonify(_pics)

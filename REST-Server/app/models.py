# 数据库模块
from datetime import datetime
from werkzeug.security import generate_password_hash, check_password_hash
from itsdangerous import TimedJSONWebSignatureSerializer as Serializer
from . import db
from config import Config


class User(db.Model):
    """Table `user`"""
    __tablename__ = 'user'
    id = db.Column(db.Integer, primary_key=True)
    # must have a `primary_key` when creating new table
    username = db.Column(db.Integer, unique=True, index=True)
    password_hash = db.Column(db.String(128))

    state = db.relationship('State', backref='user', lazy='dynamic')
    picture = db.relationship('Picture', backref='user', lazy='dynamic')
    train = db.relationship('Train', backref='user', lazy='dynamic')

    @staticmethod
    def init():
        """provide for command: `python manage.py init`, to init database"""
        default_user = User(username=1, password='dev')
        db.session.add(default_user)
        db.session.commit()

    @property
    def password(self):
        """no getter for `password`"""
        raise AttributeError('password is not a readable attribute')

    @password.setter
    def password(self, password):
        self.password_hash = generate_password_hash(password)

    def verify_password(self, password):
        return check_password_hash(self.password_hash, password)

    def generate_auth_token(self, expiration=3600):
        # default 1 hour: 3600
        serializer = Serializer(Config.SECRET_KEY, expires_in=expiration)
        return serializer.dumps({'username': self.username})

    @staticmethod
    def verify_auth_token(token):
        serializer = Serializer(Config.SECRET_KEY)
        try:
            data = serializer.loads(token)
        except:
            return None
        return User.query.get(data.get('username'))

    def __repr__(self):
        return 'User(username=%r, password=???)' % self.username

    def __str__(self):
        return '<User %r>' % self.username


class State(db.Model):
    """Table `state`: save user's hand gesture"""
    __tablename__ = 'state'
    id = db.Column(db.Integer, primary_key=True)
    date = db.Column(db.String(16), default=datetime.now().strftime("%Y-%m-%d %H:%M"))

    user_id = db.Column(db.Integer, db.ForeignKey('user.username'))

    state = db.Column(db.String(10), index=True, default='')
    danger = db.Column(db.Boolean, default=False)

    @staticmethod
    def init():
        default_data = [
            {'state': 'open_open', 'user_id': 0},
            {'state': 'open_close', 'user_id': 1},
            {'state': 'open_lasso', 'user_id': 0},
        ]
        for data in default_data:
            state = State(state=data.get('state'), user_id=data.get('user_id'))
            db.session.add(state)
        db.session.commit()

    def get_json(self):
        return {
            'id': self.id,
            'user_id': self.user_id,
            'date': self.date,
            'state': self.state,
            'danger': self.danger,
        }

    def __repr__(self):
        return 'State(state={}, danger={})'.format(self.state, self.danger)


class Picture(db.Model):
    """Table picture: save picture name for upload, picture url can get by:
    ```
    photos = UploadSet('PHOTOS', IMAGES)
    photos.url(filename)
    ```

    """
    __tablename__ = 'picture'
    id = db.Column(db.Integer, primary_key=True)
    date = db.Column(db.String(10), default=datetime.now().strftime('%Y-%m-%d %H:%M'))

    user_id = db.Column(db.Integer, db.ForeignKey('user.username'))

    filename = db.Column(db.String(64), index=True)

    @staticmethod
    def init():
        picture1 = Picture(filename='user_1_2017_11_12_16_25.jpg', user_id=0)
        picture2 = Picture(filename='user_1_2017_11_12_16_25.jpg', user_id=1)
        db.session.add(picture1)
        db.session.add(picture2)
        db.session.commit()

    def get_json(self):
        return {
            'id': self.id,
            'user_id': self.user_id,
            'filename': self.filename,
            'date': self.date,
        }

    def __repr__(self):
        return 'Picture(id={}, date={}, filename={}, user_id={})'.format(self.id, self.date, self.filename, self.user_id)


class Train(db.Model):
    """Table: train"""
    __tablename__ = 'train'
    id = db.Column(db.Integer, primary_key=True)
    date = db.Column(db.String(16), default=datetime.now().strftime('%Y-%m-%d %H:%M'))
    user_id = db.Column(db.Integer, db.ForeignKey('user.username'))

    target = db.Column(db.Integer, default=0)
    result = db.Column(db.Integer, default=0)

    @staticmethod
    def init():
        train1 = Train(result=12, target=3, user_id=0)
        train2 = Train(result=11, target=34, user_id=1)
        db.session.add(train1)
        db.session.add(train2)
        db.session.commit()

    def get_json(self):
        return {
            # 'id': self.id,
            'date': self.date,
            'target': self.target,
            'result': self.result,
            'user_id': self.user_id,
        }

    def __repr__(self):
        return 'Train(user_id={}, result={})'.format(self.user_id, self.result)

# 数据库模块
from datetime import datetime
from werkzeug.security import generate_password_hash, check_password_hash
from itsdangerous import TimedJSONWebSignatureSerializer as Serializer
from . import db
from config import Config


class User(db.Model):
    __tablename__ = 'users'
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.Integer, unique=True, index=True)
    # must have a `primary_key` when creating new table
    password_hash = db.Column(db.String(128))
    picture = db.relationship('Picture', backref='user', lazy='dynamic')

    @staticmethod
    def init():
        default_user = User(username=1, password='dev')
        db.session.add(default_user)
        db.session.commit()

    @property
    def password(self):
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
        return '<User %r>' % self.username


class State(db.Model):
    __tablename__ = 'state'
    id = db.Column(db.Integer, primary_key=True)
    state = db.Column(db.String(10), index=True, default='')
    timestamp = db.Column(db.DateTime(), default=datetime.now)
    danger = db.Column(db.Boolean, default=False)

    @staticmethod
    def init():
        default_data = [
            {
                'id': 1,
                'state': 'open_open',
                'time': datetime.utcnow()
            },
            {
                'id': 2,
                'state': 'open_close',
                'time': datetime.now()
            },
            {
                'id': 3,
                'state': 'open_lasso',
                'time': datetime.now()
            },
        ]
        for data in default_data:
            state = State(
                id=data.get('id'),
                state=data.get('state'),
                timestamp=data.get('time'),
            )
            db.session.add(state)
        db.session.commit()

    def get_json(self):
        return {
            'id': self.id,
            'state': self.state,
            'time': self.timestamp,
            'danger': self.danger,
        }

    def __repr__(self):
        return '<State {}>'.format(self.state)


class History(db.Model):
    __tablename__ = 'history'
    id = db.Column(db.Integer, primary_key=True, index=True)
    userId = db.Column(db.Integer, index=True)
    date = db.Column(db.String(10), default=datetime.now().strftime('%Y-%m-%d %H:%M'))
    state = db.Column(db.String(10), index=True, default='')

    @staticmethod
    def init():
        history = History(userId=0, state='init_state')
        db.session.add(history)
        db.session.commit()

    def get_json(self):
        return {
            'id': self.id,
            'userId': self.userId,
            'date': self.date,
            'state': self.state,
        }

    def __repr__(self):
        return '<History {}, user {}, date {}, state {}>'.format(self.id, self.userId, self.date, self.state)


class Picture(db.Model):
    __tablename__ = 'picture'
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('users.id'))
    filename = db.Column(db.String(64), index=True)
    date = db.Column(db.String(10), default=datetime.now().strftime('%Y-%m-%d %H:%M'))

    def get_json(self):
        return {
            'id': self.id,
            'userId': self.user_id,
            'filename': self.filename,
            'date': self.date,
        }

    def __repr__(self):
        return '<Picture {}, user {}, filename {}, date {}>'.format(self.id, self.user, self.filename, self.date)

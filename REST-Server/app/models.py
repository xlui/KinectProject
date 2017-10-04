from datetime import datetime
from werkzeug.security import generate_password_hash, check_password_hash
from itsdangerous import TimedJSONWebSignatureSerializer as Serializer
from . import db
from config import Config


class User(db.Model):
    __tablename__ = 'users'
    username = db.Column(db.Integer, unique=True, index=True, primary_key=True)
    # must have a `primary_key` when creating new table
    password_hash = db.Column(db.String(128))

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

    def generate_auth_token(self, expiration):
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
    timestamp = db.Column(db.DateTime(), default=datetime.utcnow)

    @staticmethod
    def init():
        default_data = [
            {
                'id': 1,
                'state': 'Open',
                'time': datetime.utcnow()
            },
            {
                'id': 2,
                'state': 'Close',
                'time': datetime.now()
            },
            {
                'id': 3,
                'state': 'Lasso',
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
        }

    def __repr__(self):
        return '<State {}>'.format(self.state)

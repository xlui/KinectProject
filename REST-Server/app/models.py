from datetime import datetime
from . import db


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

from flask_script import Manager, Shell

from app import create_app, db
from app.models import State


app = create_app()
manager = Manager(app)


def make_shell_context():
    return dict(app=app, db=db, State=State, )


if __name__ == '__main__':
    manager.add_command('shell', Shell(make_context=make_shell_context))
    manager.run()

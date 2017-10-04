from flask_script import Manager, Shell
from flask_migrate import Migrate, MigrateCommand

from app import create_app, db
from app.models import State, User


app = create_app()
manager = Manager(app)
migrate = Migrate(app, db)


def make_shell_context():
    return dict(app=app, db=db, State=State, User=User, )


if __name__ == '__main__':
    manager.add_command('shell', Shell(make_context=make_shell_context))
    manager.add_command('db', MigrateCommand)
    manager.run()

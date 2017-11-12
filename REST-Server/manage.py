from flask_script import Manager, Shell
from flask_migrate import Migrate, MigrateCommand

from app import create_app, db
from app.models import State, User, Picture, Train


app = create_app()
manager = Manager(app)
migrate = Migrate(app, db)


def make_shell_context():
    return dict(app=app, db=db, State=State, User=User, Picture=Picture, Train=Train, )


if __name__ == '__main__':
    manager.add_command('shell', Shell(make_context=make_shell_context))
    manager.add_command('db', MigrateCommand)

    from command import show

    @manager.command
    def show_all():
        show()

    @manager.command
    def show_user():
        show('user')

    @manager.command
    def show_state():
        show('state')

    @manager.command
    def init():
        from command import init
        init(db)

    @manager.command
    def drop_init():
        from command import init
        init(db, True)

    manager.run()

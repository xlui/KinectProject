from flask_script import Manager, Shell
from flask_migrate import Migrate, MigrateCommand

from app import create_app, db
from app.models import State, User, History


app = create_app()
manager = Manager(app)
migrate = Migrate(app, db)


def make_shell_context():
    return dict(app=app, db=db, State=State, User=User, History=History, )


if __name__ == '__main__':
    manager.add_command('shell', Shell(make_context=make_shell_context))
    manager.add_command('db', MigrateCommand)

    @manager.command
    def show():
        def print_users(results):
            for row in results:
                print('username:', row[0])
                print('password_hash:', row[1])
                print()

        def print_state(results):
            for row in results:
                print('id:', row[0])
                print('state:', row[1])
                print('time:', row[2])
                print()

        def print_history(results):
            for row in results:
                print('id:', row[0])
                print('date:', row[1])
                print('state:', row[2])
                print()

        import sqlite3
        import config
        connect = sqlite3.connect(config.local_database)
        cursor = connect.cursor()

        print('Data in database [state]:')
        results = cursor.execute("SELECT * FROM state")
        print_state(results)

        print('Data in database [users]:')
        results = cursor.execute("SELECT * FROM users")
        print_users(results)

        print('Data in database [history]:')
        results = cursor.execute("SELECT * FROM history")
        print_history(results)

        cursor.close()
        connect.close()

    def _init(drop=False):
        if drop:
            db.drop_all()
            db.session.commit()
        db.create_all()
        User.init()
        State.init()
        History.init()

    @manager.command
    def init():
        _init()

    @manager.command
    def drop_init():
        _init(True)

    manager.run()

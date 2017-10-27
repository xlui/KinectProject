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

    @manager.command
    def show():
        def print_state(results):
            for row in results:
                print('id:', row[0])
                print('state:', row[1])
                print('time:', row[2])
                print()

        def print_users(results):
            for row in results:
                print('username:', row[0])
                print('password_hash:', row[1])

        import sqlite3
        connect = sqlite3.connect("dev.sqlite")
        cursor = connect.cursor()

        print('Data in database [state]:')
        result = cursor.execute("SELECT * FROM state")
        print_state(result)

        print('Data in database [users]:')
        result = cursor.execute("SELECT * FROM users")
        print_users(result)

        cursor.close()
        connect.close()

    @manager.command
    def init(drop=False):
        if drop:
            db.drop_all()
            db.session.commit()
        db.create_all()
        User.init()
        State.init()

    manager.run()

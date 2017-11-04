from app.models import State, User, History, Picture


def init(db, drop=False):
    if drop:
        db.drop_all()
        db.session.commit()
    db.create_all()
    User.init()
    State.init()
    History.init()


def print_users(results):
    for row in results:
        print('id: ', row[0])
        print('username:', row[1])
        print('password_hash:', row[2])
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
        print('userId', row[1])
        print('date:', row[2])
        print('state:', row[3])
        print()


def show(database=''):
    import sqlite3
    import config
    connect = sqlite3.connect(config.database)
    cursor = connect.cursor()

    def _common(db_name):
        print('Data in database [{}]:'.format(db_name))
        results = cursor.execute("SELECT * FROM (SELECT * FROM {} ORDER BY id DESC LIMIT 3) ORDER BY id".format(db_name))
        return results

    if database == 'users':
        _result = _common(database)
        print_users(_result)
    if database == 'state':
        _result = _common(database)
        print_state(_result)
    if database == 'history':
        _result = _common(database)
        print_history(_result)
    if not database:
        users_results = _common('users')
        print_users(users_results)

        state_results = _common('state')
        print_state(state_results)

        history_results = _common('history')
        print_history(history_results)

    cursor.close()
    connect.close()

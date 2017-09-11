# module mysql -- to save data into mysql or query data from database
import contextlib
import pymysql
from conf import host as database_host, username as database_username, password as database_password, database
# todo: use sqlite instead of mysql


class MySQL(object):
    """MySQL class -- database operation class"""
    def __init__(self):
        super(MySQL, self).__init__()
        self.__connect = pymysql.connect(host=database_host, user=database_username, passwd=database_password, db=database)
        print("Open MySQL connection...")

    def execute(self, sql):
        # execute a sql statement
        cursor = self.__connect.cursor()
        try:
            cursor.execute(sql)
            self.__connect.commit()
        except Exception as e:
            print(e)
        else:
            print("Success execute: ", sql)
        result = cursor.fetchall()
        cursor.close()
        if len(result) == 0:
            return None
        else:
            return result

    def find_username(self, username):
        sql = 'select username from account where username={}'.format(username)
        result = self.execute(sql)
        if result:
            return True
        else:
            return False

    def find_registration_id(self, username):
        # find registrationID through username
        sql = 'select registrationID from account where username={}'.format(username)
        result = self.execute(sql)
        if not result:
            # it indicates that cannot find registration_id in database
            return None
        else:
            # simply return the first element of result tuple, do not detect the length of result
            return result[0][0]

    def save(self, username, registration_id):
        # save username and registration_id to database
        result = self.find_username(username)
        # sql = ''
        if result:
            # username is in database, now update table
            sql = 'update account set registrationID="{}" where username={}'.format(registration_id, username)
            # fix bug here: to value to be set as registrationID should be surround with quotation marks
            print("to execute sql statement: ", sql)
        else:
            # username is not in database, now insert
            sql = "insert into account values ({}, '{}')".format(username, registration_id)
            # print(sql)
            # BUG here! in the SQL string, if u want to insert a string into table, you must add ''
            # in the SQL statement
        self.execute(sql)

    def close(self):
        self.__connect.close()
        print('Close MySQL connection...')


if __name__ == '__main__':
    with contextlib.closing(MySQL()) as mysql:
        mysql.save(4, 'asdssaa')

    # mysql = MySQL()

    # result = mysql.find_registration_id(username)
    # if result:
    #     print("username: {}\nregistrationID: {}".format(username, result))
    # else:
    #     print("None!")

    # mysql.save(4, 'asdssaa')
    #
    # mysql.close()

# sqlite 模块，对一些数据库相关的操作进行封装
import sqlite3
from contextlib import closing
from config import config_default


class Sqlite(object):
    """Class sqlite -- 对 sqlite3 相关操作进行封装"""
    def __init__(self, database=config_default.database_abs_path):
        super(Sqlite, self).__init__()
        self.__connect = sqlite3.connect(database)
        self.row_count = 0
        print("Open sqlite connection...")

    def execute(self, sql):
        # 执行一个 sql 语句，并处理过程中可能发生的异常
        with closing(self.__connect.cursor()) as cursor:
            try:
                cursor.execute(sql)
                self.__connect.commit()
            except Exception as e:
                print(e)
            else:
                print("successfully execute: ", sql)
            result = cursor.fetchall()
            self.row_count = cursor.rowcount
        if len(result) == 0:
            return None
        else:
            return result

    def create(self):
        # 创建表
        sql = "CREATE TABLE {} (USERNAME INT PRIMARY KEY, REGISTRATION_ID VARCHAR(20));".format(
            config_default.table_name)
        result = self.execute(sql)
        if not result:
            return True
        else:
            return False

    def drop(self):
        # 删除表，如果表不存在忽略错误
        sql = "DROP TABLE {};".format(config_default.table_name)
        try:
            self.execute(sql)
        except sqlite3.OperationalError as e:
            print('table {} does not exist.'.format(config_default.table_name))

    def all(self):
        # 查询数据库中所有数据
        sql = "SELECT * FROM {};".format(config_default.table_name)
        result = self.execute(sql)
        if result:
            return result
        else:
            return None

    def query_username(self, username):
        """查询用户名
        :param username: 要查询用户名
        :return: 用户名存在于数据库就返回用户名，不存在就返回 None
        """
        sql = "SELECT USERNAME FROM {} WHERE USERNAME={};".format(config_default.table_name, username)
        result = self.execute(sql)
        if result:
            return result[0][0]
        else:
            return None

    def query_registration_id(self, username):
        """查询用户名对应注册ID
        :param username: 用户名
        :return: 如果存在记录，就返回注册ID；如果不存在，返回 None
        """
        sql = "SELECT REGISTRATION_ID FROM {} WHERE USERNAME={};".format(config_default.table_name, username)
        result = self.execute(sql)
        if result:
            return result[0][0]
        else:
            return None

    def save(self, username, registration_id):
        # 保存数据到数据库，对于同用户名数据的情况，更新数据库信息
        result = self.query_username(username)
        if result:
            sql = "UPDATE {} SET REGISTRATION_ID='{}'".format(config_default.table_name, registration_id)
        else:
            sql = "INSERT INTO {} VALUES ({}, '{}')".format(config_default.table_name, username, registration_id)
        print("Save data into database...")
        print("Execute sql statement:", sql)
        self.execute(sql)

    def close(self):
        self.__connect.close()
        print("Close sqlite connection...")


if __name__ == '__main__':
    with closing(Sqlite()) as sqlite:
        sqlite.execute('INSERT INTO {} VALUES (1, "170976fa8abb6683123");'.format(config_default.table_name))
        print('Data in database is:')
        sqlite.execute("SELECT * FROM {};".format(config_default.table_name))
        print(sqlite.all())

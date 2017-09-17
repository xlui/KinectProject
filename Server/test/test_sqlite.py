import unittest
from app.sqlite import Sqlite
from config import config


class MyTestCase(unittest.TestCase):
    # setUp 和 tearDown 是在每个测试函数开始前和结束后都执行,而不是只执行一次
    def setUp(self):
        super().setUp()
        self.config = config.get('testing')
        self.database = self.config.database_abs_path
        self.sqlite = Sqlite(database=self.database)
        self.username = 1
        self.registration_id = "test"

    def tearDown(self):
        super().tearDown()
        self.sqlite.close()

    def test_1create(self):
        self.sqlite.drop()
        self.assertTrue(self.sqlite.create())

    def test_2execute(self):
        sql = 'INSERT INTO {} VALUES ({}, "{}");'.format(self.config.table_name, self.username, self.registration_id)
        result = self.sqlite.execute(sql)
        self.assertEqual(1, self.sqlite.row_count)
        self.assertIsNone(result)

    def test_3all(self):
        # 此处出现 bug，已解决，记录：
        # Python中单元测试的顺序默认是由内置函数 unittest.TestLoader.sortTestMethodsUsing 进行比较排序的，本例中由于使用新数据库
        # 因此 test_all 的顺序要在后边,我们通过改变测试函数名的方法来指定顺序
        result = self.sqlite.all()
        self.assertIsNotNone(result)
        self.assertGreaterEqual(len(result), 1)

    def test_4query_username(self):
        result = self.sqlite.query_username(self.username)
        self.assertEqual(self.username, result)

    def test_5query_registration_id(self):
        result = self.sqlite.query_registration_id(self.username)
        self.assertEqual(self.registration_id, result)

    def test_6save(self):
        username = self.username + 1
        self.sqlite.save(username, self.registration_id)
        self.assertEqual(username, self.sqlite.query_username(username))
        self.assertEqual(self.registration_id, self.sqlite.query_registration_id(username))


if __name__ == '__main__':
    unittest.main()

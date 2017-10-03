import unittest
from app.models import User


class MyTestCase(unittest.TestCase):
    def setUp(self):
        super().setUp()
        self.user = User(username='test', password='cat')

    def test_password_setter(self):
        self.assertTrue(self.user.password_hash is not None)

    def test_no_password_getter(self):
        with self.assertRaises(AttributeError):
            _ = self.user.password

    def test_password_verification(self):
        self.assertTrue(self.user.verify_password('cat'))
        self.assertFalse(self.user.verify_password('cae'))
        self.assertFalse(self.user.verify_password('dog'))

    def test_password_salts_are_random(self):
        user1 = User(password='cat')
        self.assertTrue(self.user.password_hash != user1.password_hash)


if __name__ == '__main__':
    unittest.main()

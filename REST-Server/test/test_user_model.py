import unittest
from app.models import User
from app import db, create_app


class MyTestCase(unittest.TestCase):
    def setUp(self):
        super().setUp()
        self.user = User(username=2, password='cat')

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

    def test_get_auth_token(self):
        token = self.user.generate_auth_token()
        self.assertTrue(token is not None)

    def test_verify_auth_token(self):
        app_context = create_app().app_context()
        app_context.push()

        user = User.query.get(1)
        token = user.generate_auth_token()
        auth_user = User.verify_auth_token(token.decode('utf-8'))
        self.assertTrue(user.username == auth_user.username)
        self.assertFalse(user.password_hash != auth_user.password_hash)

        app_context.pop()


if __name__ == '__main__':
    unittest.main()

#!/usr/bin/env python
# coding:utf-8
# push message to android device
# instruction:
#   push_notification_registration_id(registration_id, notification)
#       will push notification to special (registration id) android device
#   push_message_all(message)
#       will push message to all android platform
import time
import jpush
from conf import app_key, master_secret


class JPush(object):
    """
    jpush class -- push message to android service
    """
    def __init__(self, ):
        super(JPush, self).__init__()
        self.__jpush = jpush.JPush(app_key, master_secret)
        self.__push = self.__jpush.create_push()
        self.__jpush.set_logging("DEBUG")

    def push_notification_all(self, notification=u'empty message!'):
        """
        this function will push notification to all android devices

        :param notification: notification to be sent
        :return: none
        """
        self.__push.audience = jpush.all_
        self.__send_notification(notification)

    def push_notification_registration_id(self, registration_id, notification=u'empty message!'):
        """
        this function will push notification to special registration id android device

        :param registration_id: android device's registration id
        :param notification: notification to be sent
        :return: none
        """
        self.__push.audience = jpush.registration_id(registration_id)
        self.__send_notification(notification)

    def __send_notification(self, notification):
        self.__push.notification = jpush.notification(alert=notification)
        self.__push.platform = jpush.platform("android")
        self.__send()

    def push_message_all(self, message=u'Empty Message!'):
        """
        this function will send message to all android devices

        :param message: message to be sent
        :return: none
        """
        self.__push.audience = jpush.all_
        self.__send_message(message)

    def push_message_registration_id(self, registration_id, message=u'Empty Message!'):
        """
        this function will send message to android device identified by a unique registration id

        :param registration_id: android device's registration id, unique
        :param message: message to be sent
        :return: none
        """
        self.__push.audience = jpush.registration_id(registration_id)
        self.__send_message(message)

    def __send_message(self, message):
        self.__push.message = jpush.message(message)
        self.__push.platform = jpush.platform("android")
        self.__send()

    def __send(self):
        """
        send message or notification to android device, and will deal with exceptions

        :return: none
        """
        try:
            self.__push.send()
        except jpush.common.Unauthorized:
            raise jpush.common.Unauthorized("Unauthorized!")
        except jpush.common.APIConnectionException:
            raise jpush.common.APIConnectionException("conn error!")
        except jpush.common.JPushFailure:
            print("JPushFailure")
        except Exception as e:
            print("Exception occurred: ", e)


if __name__ == '__main__':
    message = 'hello python jpush api {}'.format(time.ctime())
    # message = 'registrationID'
    push = JPush()
    push.push_message_all(message=message)
    # push.push_notification_all(message)

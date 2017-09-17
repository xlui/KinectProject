#!/usr/bin/env python
# coding:utf-8
# 向安卓设备推送通知或者消息
# API 介绍:
#   push_notification_all(notification)
#       向所有安卓设备推送通知，会在通知栏显示
#   push_notification_registration_id(registration_id, notification)
#       通过唯一 registration id 向安卓设备推送通知，会在通知栏显示
#   push_message_all(message)
#       向所有安卓平台推送消息
#   push_message_registration_id(registration_id, message)
#       通过唯一 registration id 向安卓设备推送消息

import time
import jpush
from config import config_default


class JPush(object):
    def __init__(self, ):
        super(JPush, self).__init__()
        self.__jpush = jpush.JPush(config_default.app_key, config_default.master_secret)
        self.__push = self.__jpush.create_push()
        self.__jpush.set_logging("DEBUG")

    def push_notification_all(self, notification=u'empty message!'):
        """向 所有 安卓设备推送通知

        :param notification: 将要发送的通知
        :return: 无
        """
        self.__push.audience = jpush.all_
        # audience 是发送通知的目标人群，可以选择：所有、设备标签、设备别名、 registration id、用户分群
        self.__send_notification(notification)

    def push_notification_registration_id(self, registration_id, notification=u'empty message!'):
        """向 特定registration id 的安卓设备发送通知

        :param registration_id: 安卓设备的 registration id
        :param notification: 将要发送的通知
        :return: 无
        """
        self.__push.audience = jpush.registration_id(registration_id)
        # audience 是发送通知的目标人群，可以选择：所有、设备标签、设备别名、 registration id、用户分群
        self.__send_notification(notification)

    def __send_notification(self, notification):
        self.__push.notification = jpush.notification(alert=notification)
        self.__push.platform = jpush.platform("android")
        # platform 是发送通知的目标平台，可以选择：安卓、IOS、WinPhone
        self.__send()

    def push_message_all(self, message=u'Empty Message!'):
        """向 所有 安卓设备发送消息

        :param message: 将要发送的消息
        :return: 无
        """
        self.__push.audience = jpush.all_
        # audience 是发送通知的目标人群，可以选择：所有、设备标签、设备别名、 registration id、用户分群
        self.__send_message(message)

    def push_message_registration_id(self, registration_id, message=u'Empty Message!'):
        """向 特定registration id 的安卓设备发送消息

        :param registration_id: 安卓设备的 registration id
        :param message: 将要发送的消息
        :return: 无
        """
        self.__push.audience = jpush.registration_id(registration_id)
        # audience 是发送通知的目标人群，可以选择：所有、设备标签、设备别名、 registration id、用户分群
        self.__send_message(message)

    def __send_message(self, message):
        self.__push.message = jpush.message(message)
        self.__push.platform = jpush.platform("android")
        # platform 是发送通知的目标平台，可以选择：安卓、IOS、WinPhone
        self.__send()

    def __send(self):
        # 推送消息或者通知到安卓设备，同时处理过程中可能会产生的异常
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
    msg = 'hello python jpush api, now the time is: {}'.format(time.ctime())
    reg_id = "170976fa8abb6683123"
    push = JPush()
    # 向 所有 安卓设备推送通知
    # push.push_notification_all(notification=msg)
    # 向 特定 registration id 的安卓设备推送通知
    push.push_notification_registration_id(registration_id=reg_id, notification=msg)
    # 向 所有 安卓设备推送消息
    # push.push_message_all(message=msg)
    # 向 特定 registration id 的安卓设备推送消息
    # push.push_message_registration_id(registration_id=reg_id, message=msg)

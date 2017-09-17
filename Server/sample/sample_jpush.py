# 使用极光推送的简单例子
import os
import sys
import time
import jpush
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../..')))
from config import config_default


_jpush = jpush.JPush(config_default.app_key, config_default.master_secret)
_jpush.set_logging("DEBUG")
push = _jpush.create_push()


message = "message from python jpush api {}".format(time.ctime())
# 将被推送的内容
push.audience = jpush.all_
# 目标人群，可以是所有人，也可以是某个设备的 registration id
push.platform = jpush.platform("android")
# 目标平台，可以是 安卓、IOS、WinPhone
push.message = jpush.message(message)
# 消息内容。     消息可以做为特定信号被接收并处理
push.notification = jpush.notification(message)
# 通知内容。     通知会在通知栏显示通知
print(push.payload)
# 打印出推送的内容
push.send()
# 发送推送


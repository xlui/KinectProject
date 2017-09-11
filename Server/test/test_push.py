# test to push message to android device
# successfully push a message to android service
import os
import sys
import time
import jpush
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '../..')))
from conf import app_key, master_secret


message = "message from python jpush api {}".format(time.ctime())
_jpush = jpush.JPush(app_key, master_secret)
push = _jpush.create_push()
_jpush.set_logging("DEBUG")

push.audience = jpush.all_
push.platform = jpush.platform("android")
push.message = jpush.message(message)
print(push.payload)
push.send()

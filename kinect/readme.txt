KinectHandTracking:
    可以识别手的三个状态：打开、关闭、剪刀手。
    已经结合了 csharp_client 的代码，可以将识别的手势发送到服务器端。测试的时候请先确认 Config.cs 中 SERVER_IP 变量的设置是否正确。

KinectFingerTracking:
    可以识别手的轮廓。更进一步要做到手势识别（例如手指1、2、3、4、5等手势）。
    代码来自外国技术博客，原文地址：
        http://pterneas.com/2016/01/24/kinect-finger-tracking/
    文中有相应演示视频。
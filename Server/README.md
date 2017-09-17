## Kinect 服务器端代码

基于多线程开发，用于接收安卓端与 C# 端的数据并作相应处理。

### 使用方法：
```bash
cd KinectProject/Server
pip install -r requirements.txt
python main.py
```
默认会一直监听客户端的连接，本机测试的时候请将客户端配置文件中相应配置文件中的服务器 IP 改为本机 IP （Windows下用`ipconfig`查看，Linux下用`ifconfig`查看），否则客户端将无法链接到服务器。

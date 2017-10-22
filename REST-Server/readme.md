# A RESTful Server

RESTful 服务器端，提供给 C# 端更新数据和安卓端查询数据的 API 接口。

<br>

## API 使用方法

下表列出了支持的 API 以及说明：

API|说明
---|---
http://111.231.1.210/api/dev/login|登录
http://111.231.1.210/api/dev/token|获取 token
http://111.231.1.210/api/dev/latest|获取最新手势
http://111.231.1.210/api/dev/update|更新手势
http://111.231.1.210/api/dev/register|注册

#### 使用说明：

- 默认用户名 1 密码 dev
- 所有的 API 请求都需要验证用户身份，即在 request 中附件 Authorization 信息（用户名密码或者 token 认证）
- API login 接收 GET 方法。返回数据：成功 `{"login": "success"}`、失败 `{"login": "failed"}`
- API token 接收 GET 方法。返回数据：成功 `{"expiration": 3600, "token": TOKEN字符串}`、失败 `{"token": "Invalid credentials"}`
- API latest 接收 GET 方法。返回数据：成功 `{
  "state": {
    "id": 4,
    "state": "TEST",
    "time": "Sun, 08 Oct 2017 14:06:44 GMT"
  }
}`、失败 `{"login": "failed"}`
- API update 接收 POST 方法。以及一个 json 格式的 body（例：`{"state":"TEST"}`）。示例代码已经给出。 
- API register 接收 POST 方法。该 API 不需要认证。body 需要设置为包含 `username` 和 `password` 的 json 字符串（例：`{"password":"dev2","username":2}`）。示例代码也已经给出。

**关于 token 登录的说明[重要！]**

token 是登录方式的一种，建议使用 token 而不是用户名密码进行登陆。

token 的默认有效时间是一个小时。

获取 token 的方法上边已经列出。

使用 token 登陆：

```bash
curl -X GET -H "Authorization:Dev 获取到的token" http://111.231.1.210/api/dev
```

使用 token 登陆后不能再次获取 token。

<br>

#### 代码示例：

C#： [主程序 Program.cs](https://github.com/xlui/KinectProject/blob/master/csharp_client/csharp_client/Program.cs)&nbsp;&nbsp;&nbsp;&nbsp;
[具体实现（需和主程序在同一 namespace）](https://github.com/xlui/KinectProject/blob/master/csharp_client/csharp_client/Client.cs)  
Java:  
[Client.java](https://github.com/xlui/KinectProject/blob/master/JavaClient/src/main/java/com/liuqi/client/Client.java) &nbsp;&nbsp; [Register.java](https://github.com/xlui/KinectProject/blob/master/JavaClient/src/main/java/com/liuqi/client/Register.java)  
需要引用 json 包，在 GitHub 上下载相应的 [JSON-java](https://github.com/stleary/JSON-java) 包，然后作为本地包导入。



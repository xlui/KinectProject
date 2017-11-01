# A RESTful Server

RESTful 服务器端，提供给 C# 端更新数据和安卓端查询数据的 API 接口。

<br>

## API 列表

API|说明
---|---
https://nxmup.com/api/dev/register|注册
https://nxmup.com/api/dev/login|登录
https://nxmup.com/api/dev/token|获取 token，默认 token 有效期是 30 天
https://nxmup.com/api/dev/latest|获取最新手势
https://nxmup.com/api/dev/update|更新手势
https://nxmup.com/api/dev/history|历史记录
https://nxmup.com/api/dev/picture/name|显示手势对应图片，name 是图片名字
https://nxmup.com/api/dev/upload|上传图片

## 使用

默认用户名 1 密码 dev。

所有的 API 请求都需要验证用户身份，即在 request 中附加 Authorization 信息（用户名密码或者 token 认证）。示例：

```java
// java
String username = "1";
String password = "dev";
String authorization = new BASE64Encoder().encode((username + ":" + password).getBytes());

OkHttpClient client = new OkHttpClient();
Request request = new Request.Builder()
    .url(url)
    .addHeader("Authorization", "Basic " + authorization)
    .build();

handleResponse(client, request);
```

token 认证类似：

```java
// 改变上述代码的这一行
.addHeader("Authorization", "Dev " + token)
```

1. 注册（/register），仅支持 POST 方法

Java 示例代码在 [Register.java](https://github.com/xlui/KinectProject/blob/master/Samples/JavaClient/src/main/java/com/liuqi/client/Register.java)

注册 API 仅接收 json 格式的 request body。

成功时返回：

```json
{
  "register": "success"
}
```

如果发送的 request body 不是 json 格式的会返回 `400 Error`。

2. 登录（/login），仅接受 GET 方法

成功返回：

```json
{'login': 'success'}
```

失败返回 401 错误：

```json
{'login': 'failed'}
```

3. 获取 token（/token），仅接受 GET 方法

token 的默认有效期是一个月。

获取成功返回：

```json
{
  "expiration": 2592000,  // 过期时间，按秒计算
  "token": "token字符串"
}

```

token 只能在用户名密码登录情况下获取，token 登录情况下尝试获取 token 会得到 405 错误：

```json
{'token': 'Invalid credentials'}
```

4. 最新手势（/latest），仅接受 GET 方法

获取成功返回：

```json
{
  "state": {
    "id": 4, 
    "state": "close_close", 
    "time": "Mon, 30 Oct 2017 21:12:19 GMT"
  }, 
  "userId": 1
}
```

5. 更新手势（/update），仅接受 POST 方法

更新请求的 request body 必须是 json 格式，如果不是会返回 400 错误。

更新成功返回：

```json
{
  "state": {
    "id": 5, 
    "state": "close_close", 
    "time": "Wed, 01 Nov 2017 16:12:42 GMT"
  }
}
```

6. 历史记录（/history），仅接受 GET 方法

历史记录会自动按照用户区分，即只显示当前用户的历史记录

获取成功返回：

```json
[
  {
    "date": "2017-10-30", 
    "id": 2, 
    "state": "close_close", 
    "userId": 1
  }, 
  {
    "date": "2017-11-01", 
    "id": 3, 
    "state": "close_close", 
    "userId": 1
  }
]
```

7. 手势对应图片（/picture/name），仅接受 GET 方法。

显示 name 对应的手势图片。

url 示例：https://nxmup.com/api/dev/picture/open_open.png

该 API 暂未启用

8. 上传图片（/upload）

version：0.0.1

接受 GET 方法，返回网页，上传在网页进行。

接受经由网页点击提交的 POST 方法，重定向到 /photo/pictureName 显示上传的图片，上传的图片还可以通过 https://nxmup.com/static/uploads/pictureName 获得。

## 示例代码

C#： [主程序 Program.cs](https://github.com/xlui/KinectProject/blob/master/Samples/csharp_client/csharp_client/Program.cs)&nbsp;&nbsp;&nbsp;&nbsp;
[具体实现（需和主程序在同一 namespace）](https://github.com/xlui/KinectProject/blob/master/Samples/csharp_client/csharp_client/Client.cs)  
Java:  
[Client.java](https://github.com/xlui/KinectProject/blob/master/Samples/JavaClient/src/main/java/com/liuqi/client/Client.java) &nbsp;&nbsp; [Register.java](https://github.com/xlui/KinectProject/blob/master/Samples/JavaClient/src/main/java/com/liuqi/client/Register.java) &nbsp;&nbsp;[GetPicture.java](https://github.com/xlui/KinectProject/blob/master/Samples/JavaClient/src/main/java/com/liuqi/client/GetPicture.java) &nbsp;&nbsp; [TokenLogin.java](https://github.com/xlui/KinectProject/blob/master/Samples/JavaClient/src/main/java/com/liuqi/client/TokenLogin.java)  
需要引用 json 包，在 GitHub 上下载相应的 [JSON-java](https://github.com/stleary/JSON-java) 包，然后作为本地包导入。



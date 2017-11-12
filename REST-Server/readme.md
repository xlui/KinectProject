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
https://nxmup.com/api/dev/latest_picture|最新图片

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

#### 1. 注册（/register），仅支持 POST 方法

Java 示例代码在 [Register.java](https://github.com/xlui/KinectProject/blob/master/Samples/JavaClient/src/main/java/com/liuqi/client/Register.java)

注册 API 仅接收 json 格式的 request body。

成功时返回：

```json
{
  "register": "success"
}
```

失败时（用户名已存在）返回：

```json
{
  "reason": "username already exists!", 
  "register": "failed"
}
```

如果发送的 request body 不是 json 格式的会返回 `400 Error`。

#### 2. 登录（/login），仅接受 GET 方法

成功返回：

```json
{
  "login": "success"
}
```

失败返回 401 错误：

```json
{
  "login": "failed"
}
```

#### 3. 获取 token（/token），仅接受 GET 方法

token 的默认有效期是一个月。

获取成功返回：

```json
{
  "expiration": 2592000,
  "token": "token字符串"
}

```

token 只能在用户名密码登录情况下获取，token 登录情况下尝试获取 token 会得到 405 错误：

```json
{"token": "Invalid credentials"}
```

#### 4. 最新手势（/latest），仅接受 GET 方法

获取成功返回：

```json
{
  "state": {
    "danger": false, 
    "date": "2017-11-12 17:14", 
    "id": 2, 
    "state": "open_close", 
    "user_id": 1
  }, 
  "user_id": 1
}
```

#### 5. 更新手势（/update），仅接受 POST 方法

更新请求的 request body 必须是 json 格式，如果不是会返回 400 错误。

更新成功返回：

```json
{
  "state": {
    "danger": false, 
    "date": "2017-11-12 17:19", 
    "id": 5, 
    "state": "lasso_lasso", 
    "user_id": 1
  }
}
```

#### 6. 历史记录（/history），仅接受 GET 方法

历史记录会自动按照用户区分，即只显示当前用户的历史记录

获取成功返回：

```json
[
  {
    "danger": false, 
    "date": "2017-11-12 17:19", 
    "id": 5, 
    "state": "lasso_lasso", 
    "user_id": 1
  }, 
  {
    "danger": false, 
    "date": "2017-11-12 17:14", 
    "id": 2, 
    "state": "open_close", 
    "user_id": 1
  }
]
```

#### 7. 手势对应图片（/picture/name），仅接受 GET 方法。

显示 name 对应的手势图片。

url 示例：https://nxmup.com/api/dev/picture/open_open.png

该 API 暂未启用

#### 8. 上传图片（/upload）

接受 POST 方法，具体看 Java 示例代码 [SendPicture.java](https://github.com/xlui/KinectProject/blob/master/Samples/JavaClient/src/main/java/com/liuqi/client/SendPicture.java)。

封装 RequestBody 时这两行是必须的：

```java
// java
.setType(MultipartBody.FORM)
.addFormDataPart("file", file.getName(), fileBody)
```

`file` 是服务器端从 FORM 中读取文件的标识，如果没有 `file` 服务器端就无从读取文件。

发送文件成功返回：

```json
{
  "imageUrl": "图片的地址", 
  "upload": "success"
}
```

失败返回：

```json
{
  "upload": "failed", 
  "imageUrl": 
}
```

失败情况未测试。

#### 9. 最新图片（/latest_picture）

服务器端保存了从 kinect 上传的最新的图片

成功返回：

```json
{
  "date": "2017-11-05 12:31", 
  "url": "https://nxmup.com/_uploads/PHOTOS/user_1_2017_11_05_12_34.jpg"
}
```

如果图片不存在，即服务器端未有任何上传图片的记录会返回 404 或者 500 错误。

对返回数据的说明：url 是图片链接，通过此链接可以直接下载图片，我在本地使用 Samples/JavaClient 中的 GetPicture 成功进行测试。

## 示例代码

C#： [主程序 Program.cs](https://github.com/xlui/KinectProject/blob/master/Samples/csharp_client/csharp_client/Program.cs)&nbsp;&nbsp;&nbsp;&nbsp;
[具体实现（需和主程序在同一 namespace）](https://github.com/xlui/KinectProject/blob/master/Samples/csharp_client/csharp_client/Client.cs)  
Java:  
[Client.java](https://github.com/xlui/KinectProject/blob/master/Samples/JavaClient/src/main/java/com/liuqi/client/Client.java) &nbsp;&nbsp; [Register.java](https://github.com/xlui/KinectProject/blob/master/Samples/JavaClient/src/main/java/com/liuqi/client/Register.java) &nbsp;&nbsp;[GetPicture.java](https://github.com/xlui/KinectProject/blob/master/Samples/JavaClient/src/main/java/com/liuqi/client/GetPicture.java) &nbsp;&nbsp; [TokenLogin.java](https://github.com/xlui/KinectProject/blob/master/Samples/JavaClient/src/main/java/com/liuqi/client/TokenLogin.java)  
需要引用 json 包，在 GitHub 上下载相应的 [JSON-java](https://github.com/stleary/JSON-java) 包，然后作为本地包导入。

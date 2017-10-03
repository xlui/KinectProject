# A RESTful Server

RESTful 服务器端，提供给 C# 端更新数据和安卓端查询数据的 API 接口。

<br>

## 目前进度

1. 用户名、密码登陆模块
2. token 登陆模块
3. 安卓查询接口

<br>

## 下一步计划

1. 创建 C# 示例程序，尝试从服务器读取数据
2. 提供 POST 接口，供 C# 客户端进行数据新增

<br>

## API 使用方法

服务器端已经部署在我的个人云服务器（111.231.1.210）上，所以可以通过固定 IP 访问 API，在本地不需要任何配置。

下表列出了支持的 API 以及使用说明：

API|说明
---|---
http://111.231.1.210/api/dev/|获取当前手势状态，默认显示最新手势
http://111.231.1.210/api/dev/token|获取 token

使用说明：

两个 API 均以 GET 方法获取，返回数据为 JSON 格式。  
每次访问前都需要登录。  
默认用户名：1，密码：dev。

示例：

> 本示例在 Windowns10 下用 git bash 提供的 curl 命令进行

```
# 获取最新手势
curl -X GET -u 1:dev http://111.231.1.210/api/dev/
```

```
获取 token
curl -X GET -u 1:dev http://111.231.1.210/api/dev/token
```

**关于 token 的说明**

token 是登录方式的一种，建议使用 token 而不是用户名密码进行登陆。

token 的默认有效时间是一个小时。

获取 token 的方法上边已经列出。

使用 token：

```bash
curl -X GET -H "Authorization:Dev 获取到的token" http://111.231.1.210/api/dev
```

使用 token 登陆后不能再次获取 token。

# 服务器端部署指南

## 1. 下载

从 GitHub 上下载最新的项目：

```bash
git clone --depth=1 https://github.com/xlui/KinectProject
cd KinectProject
```

## 2. 安装 Gunicorn

Gunicorn 是一个高效的 Python wsgi Server，通常用它来运行 wsgi application，地位相当于 Java 中的 Tomcat。

使用 Pip 安装：

```bash
pip install gunicorn
```

在项目顶级目录（KinectProject/REST-Server）下运行：

```bash
gunicorn -w 3 -b 127.0.0.1:5000 wsgi:app
```

- -w 后接工作线程数，推荐 (2 * $num_cores) + 1 的数量
- b 后接绑定地址，即本机访问地址

运行后，在本地测试：

```bash
curl localhost:5000/
```

## 3. 安装配置 Supervisor

利用 `yum` 可以直接安装 Supervisor，并且默认生成了配置文件。需要注意的是，不要把 Supervisor 安装到虚拟环境中去，Supervisor 是直接运行在系统中的。并且，Supervisor 只支持 Python2。

```bash
yum install supervisor
```

如果安装失败，可能是没有启用 epel 源：

```bash
yum install -y epel-release
yum update -y
```

然后重新尝试安装即可。

创建配置文件：`/etc/supervisord.d/server.ini`

```conf
[program:server]
directory=/项目目录
command=/虚拟环境目录/bin/gunicorn -w 2 -b 127.0.0.1:5000 wsgi:app
autostart=true
autorestart=true
user=用户
startsecs=3
startretries=5
redirect_stderr=true
stdout_logfile_maxbytes=50MB
stdout_logfile_backups=10
stdout_logfile=/opt/server.log
```

- 将`项目目录`改为 REST-Server 所在的目录（例如 `/home/test/KinectProject/REST-Server）
- 设置`虚拟环境目录`，如果没有，就改为 gunicorn 命令对应的目录
- 将`用户`改为需要运行项目的用户

配置完成后启动：

```bash
supervisord -c /etc/supervisord.conf
```

## 4. Nginx 反向代理

现在我们的项目只能本地访问，我们需要借助 Nginx 让项目可以外部访问：

```nginx
server {
    listen 80 default_server;
    server_name _;
    #index index.html index.htm index.php;
    # 重要的是下面这部分
    location / {
        proxy_pass          http://127.0.0.1:5000/;
        proxy_redirect      off;
        proxy_set_header    Host                $host;
        proxy_set_header    X-Real-IP           $remote_addr;
        proxy_set_header    X-Forwarded-For     $proxy_add_x_forwarded_for;
        proxy_set_header    X-Forwarded-Proto   $scheme;
    }

    location ~ .*\.(gif|jpg|jpeg|png|bmp|swf)$
    {
        expires      30d;
    }

    location ~ .*\.(js|css)?$
    {
        expires      12h;
    }

    location ~ /.well-known {
        allow all;
    }

    location ~ /\.
    {
        deny all;
    }

    access_log  /home/wwwlogs/access.log;
}
```

用 `nginx -t` 检查配置文件无误后重启 Nginx 即可。

## 5. Trouble Shoot

如果部署过程中有问题，欢迎 [Issues](https://github.com/xlui/KinectProject/issues) 或者邮件联系：[i@xlui.me](mailto:i@xlui.me)

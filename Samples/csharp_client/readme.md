# 使用说明

首先需要添加 JSON 支持。如果使用 VS2017，NuGet 默认已经包含，如果使用 VS2015 以及一下版本，需要自己在 [https://www.nuget.org/downloads](https://www.nuget.org/downloads) 下载安装包安装。

打开项目 -> 解决方案 -> 引用 -> 右键 “管理 NuGet 程序包” -> 搜索 Newtonsoft.Json -> 安装。

然后就可以直接运行程序了，我刚进行了一次测试，所以现在能看到的最新手势应该是 Test。

## 集成

只需要把 Client.cs 添加到你的项目中，然后模仿 Program.cs 中写就可以了。

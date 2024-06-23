# QRCode Tool / 二维码生成与扫描工具

## 项目描述
本项目旨在实现一款支持二维码生成与扫描的安卓软件。

## 主要功能
软件实现功能如下：

1. 能够通过文本生成二维码，支持保存至本地相册；可以生成纯二维码图片或中央有一个小图标的二维码图片，类似QQ生成的二维码中央有企鹅图标；

2. 能够扫描二维码，获取信息：

&emsp; 1）如果是文本，则调用Android文本阅读器显示；

&emsp; 2）如果是网页链接，则启动浏览器进行访问；

3. 支持二维码转换为一维码（条形码）。

## 项目细节
1. 项目代码在`qrcode-tool/app/src/main/java/com/example/qrcode`包中，各界面设计的xml文件在`qrcode-tool/app/src/main/res/layout`中，打包的apk在`qrcode-tool/app/release`中。

2. 代码各文件功能

&emsp; 1）`MainActivity.java`包含系统的主类，程序运行的入口，`StatusBar.java`用于隐藏状态栏；

&emsp; 2）`InputActivity.java`为二维码生成界面文本输入等功能的实现，`Generate.java`为二维码生成界面二维码生成、二维码转换一维码、保存至相册等功能的实现，`GenerateUtils.java`为具体生成二维码、一维码的代码；

&emsp; 3）`Scan.java`负责二维码扫描界面各功能，会根据扫描的二维码中内容选择进行文本显示或启动浏览器，`Result.java`负责展示扫描二维码所获得的文本信息，`Capture.java`负责调用摄像头。

<br/>
注：软件生成的二维码默认是不带中央小图标的，若想要添加，可在`Generate.java`中第70行处进行修改。

## 功能展示
### 1. 主界面
<img src="./assets/main.jpg" width="180px">

### 2. 二维码生成
<img src="./assets/generate.jpg" width="180px">

&emsp; 1）二维码生成结果（无中央小图标的版本）

<img src="./assets/gen_res.jpg" width="180px">

&emsp; 2）二维码转条形码

<img src="./assets/bar_code.jpg" width="180px">

### 3. 二维码扫描
<img src="./assets/scan.jpg" width="180px">

&emsp; 1）扫描包含文本信息二维码的结果

<img src="./assets/scan_res.jpg" width="180px">


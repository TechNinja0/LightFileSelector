# LightFileSelector
这事一个轻量级的文件选择器，简单、流畅、好用

可前往简书查看：[https://www.jianshu.com/p/a655ac8ca0f5](https://www.jianshu.com/p/a655ac8ca0f5)

## 如图所示：

![文件选择](https://upload-images.jianshu.io/upload_images/4334983-247e7645f96745e5.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/300/format/webp)

![文件选择](https://upload-images.jianshu.io/upload_images/4334983-8f6c613a87fb7bee.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/300/format/webp)

## 实现方式：
```
 FileSelector.create(this)
                .isChooseFile(true)//设置是选择文件还是文件夹
                .setMaxNum(maxnum)//设置选择最大数量
                .setTitle(title)//设置title
                .setMutilyMode(true)//设置选择模式：true为多选，false为单选
                .setRootPath(rootPath)//设置需要选择的文件路径，默认为根路径
                .setMaxFileSize(100 * 1024 * 1024)//设置可以选择文件大小的最大值
                .setFileFilter(new String[]{"pdf", "doc", "docx","txt","png","mp4"})//设置过滤需要保留的扩展名
                .startForResult(REQUESTCODE);//打开文件选择界面
```
               

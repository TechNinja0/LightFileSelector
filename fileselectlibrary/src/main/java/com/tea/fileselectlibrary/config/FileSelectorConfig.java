package com.tea.fileselectlibrary.config;

import android.os.Environment;
import android.support.annotation.DrawableRes;

import java.io.Serializable;

/**
 * 作者：WQ
 * 时间：2019/5/2 16:57
 */
public class FileSelectorConfig implements Serializable {
    public String title;
    public String titleColor;
    public int maxNum;
    public String backgroundColor;
    public String addText;
    public String[] fileTypes;
    public String[] notSelectStartWith;
    public String rootPath;
    public boolean mutilyMode;//设置选择模式，默认为true,多选；false为单选
    public boolean isChooseFile = true;//true 选择文件，false：选择文件夹
    public long fileSize;
    public long maxFileSize;//可选择的最大文件大小
    public boolean isLargerFileSize;//true是获取大于给定文件尺寸的文件；false：获取小于指定尺寸的文件；

    public static FileSelectorConfig getInstance(){
        return InstanceHolder.INSTANCE;
    }
    public static FileSelectorConfig getCleanInstace(){
        FileSelectorConfig instance = getInstance();
        instance.reSet();
        return instance;
    }

    private void reSet() {
        title = null;
        titleColor = null;
        backgroundColor = null;
        mutilyMode = true;
        addText = null;
        fileTypes = null;
        maxNum = 9;
        isChooseFile = true;
        rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileSize = 0;
        notSelectStartWith = new String[]{"."};
        isLargerFileSize = true;
        maxFileSize = 100*1024*1024;
    }


    private static final class InstanceHolder{
        private static final FileSelectorConfig INSTANCE = new FileSelectorConfig();
    }
    private FileSelectorConfig(){}

}

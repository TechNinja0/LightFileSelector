package com.tea.fileselectlibrary;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;


import com.tea.fileselectlibrary.config.FileSelectorConfig;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.tea.fileselectlibrary.FileSelectorActivity.PATHS;

/**
 * 作者：WQ
 * 时间：2019/5/2 16:57
 */
public class FileSelector {

    private WeakReference<Activity> mActivity;
    private WeakReference<Fragment> mFragment;
    private WeakReference<android.support.v4.app.Fragment> mSupportFragment;
    private FileSelectorConfig paramEntity;

    private FileSelector(Activity activity){
        this(activity,null,null);
    }
    private FileSelector(Fragment fragment){
        this(fragment.getActivity(),fragment,null);
    }
    private FileSelector(android.support.v4.app.Fragment fragment){
        this(fragment.getActivity(),null,fragment);
    }
    private FileSelector(Activity activity, Fragment fragment, android.support.v4.app.Fragment mSupportFragment){
        this.mActivity =  new WeakReference<>(activity);
        this.mFragment =  new WeakReference<>(fragment);
        this.mSupportFragment =  new WeakReference<>(mSupportFragment);
        paramEntity = FileSelectorConfig.getCleanInstace();
    }

    public static FileSelector create(Activity activity){
        return new FileSelector(activity);
    }
    public static FileSelector create(Fragment fragment){
        return new FileSelector(fragment);
    }
    public static FileSelector create(android.support.v4.app.Fragment fragment){
        return new FileSelector(fragment);
    }
    /**
     * 设置主标题
     *
     * @param title 主题文字
     * @return FileSelector
     */
    public FileSelector setTitle(String title) {
        paramEntity.title = title;
        return this;
    }

    /**
     * 设置辩题颜色
     *
     * @param color 主题颜色
     * @return FileSelector
     */
    public FileSelector setTitleColor(String color) {
        paramEntity.titleColor = color;
        return this;
    }


    /**
     * 设置标题背景色
     *
     * @param color 颜色
     * @return FileSelector
     */
    public FileSelector setBackgroundColor(String color) {
        paramEntity.backgroundColor = color;
        return this;
    }

    /**
     * 设置选择模式，默认为true,多选；false为单选
     *
     * @param isMutily true:多选，false：单选
     * @return FileSelector
     */
    public FileSelector setMutilyMode(boolean isMutily) {
        paramEntity.mutilyMode = isMutily;
        return this;
    }

    /**
     * 设置多选时按钮文字
     *
     * @param text 文案
     * @return FileSelector
     */
    public FileSelector setAddText(String text) {
        paramEntity.addText = text;
        return this;
    }

    /**
     * 设置选择的文件格式，如new {pdf,word}，则只筛选设置的文件格式
     * @param arrs 需要的文件格式，如果为空则不过滤；
     * @return FileSelector
     */
    public FileSelector setFileFilter(String[] arrs) {
        paramEntity.fileTypes = arrs;
        return this;
    }



    /**
     * 设置最大选中数量
     * @param num 设置多选时选择的最大数量，默认为9
     * @return FileSelector
     */
    public FileSelector setMaxNum(int num) {
        paramEntity.maxNum = num;
        return this;
    }

    /**
     * 设置初始显示路径
     * @param path 设置要显示根路径
     * @return FileSelector
     */
    public FileSelector setRootPath(String path) {
        paramEntity.rootPath = path;
        return this;
    }

    /**
     * 设置选择模式，true为文件选择模式，false为文件夹选择模式，默认为true
     *
     * @param isChooseFile 选择文件模式
     * @return FileSelector
     */
    public FileSelector isChooseFile(boolean isChooseFile) {
        paramEntity.isChooseFile = isChooseFile;
        return this;
    }

    /**
     * 设置文件大小过滤方式：大于指定大小或者小于指定大小
     *
     * @param isGreater true：大于 ；false：小于，同时包含指定大小在内
     * @return FileSelector
     */
    public FileSelector setIsGreater(boolean isGreater) {
        paramEntity.isLargerFileSize = isGreater;
        return this;
    }

    /**
     * 设置需要过滤的文件夹特征，基于文件夹是否已给定字符串为开头进行过滤；
     * @param strings 给定需要过滤的文件夹名字开始特征
     * @return FileSelector
     */
    public FileSelector setNoSelectStartWith(String[] strings){
        paramEntity.notSelectStartWith = strings;
        return this;
    }
    /**
     * 设置过滤文件大小
     *
     * @param fileSize 过滤的文件大小，单位为byte;
     * @return this
     */
    public FileSelector setFileSize(long fileSize) {
        paramEntity.fileSize = fileSize;
        return this;
    }
    /**
     * 设置可选文件最大值
     *
     * @param maxFileSize 可选文件最大值，单位为byte;
     * @return this
     */
    public FileSelector setMaxFileSize(long maxFileSize) {
        paramEntity.maxFileSize = maxFileSize;
        return this;
    }

    /**
     * 打开文件选择页面
     * @param requstCode 请求code
     */
    public void startForResult(int requstCode) {
        if (mActivity == null && mFragment == null && mSupportFragment == null) {
            throw new RuntimeException("You must pass Activity or Fragment by withActivity or withFragment or withSupportFragment method");
        }
        Intent intent = initIntent();
        if (intent == null) return;
        if (mActivity != null) {
            Activity activity = mActivity.get();
            if (activity != null)
                activity.startActivityForResult(intent, requstCode);
        } else if (mFragment != null) {
            Fragment fragment = mFragment.get();
            if (fragment != null)
                fragment.startActivityForResult(intent, requstCode);
        } else {
            android.support.v4.app.Fragment fragment = mSupportFragment.get();
            if (fragment != null)
                fragment.startActivityForResult(intent, requstCode);
        }
    }


    private Intent initIntent() {
        if (mActivity != null) {
            if (mActivity.get() != null) {
                return new Intent(mActivity.get(), FileSelectorActivity.class);
            }
        } else if (mFragment != null) {
            if (mFragment.get() != null) {
                return new Intent(mFragment.get().getActivity(), FileSelectorActivity.class);
            }
        } else {
            if (mSupportFragment.get() != null) {
                return new Intent(mSupportFragment.get().getActivity(), FileSelectorActivity.class);
            }
        }
        return null;
    }

    public static List<String> obtainSelectorList(Intent data) {
        List<String> selectionFiles;
        if (data != null) {
            selectionFiles = data.getStringArrayListExtra(PATHS);
            return selectionFiles;
        }
        selectionFiles = new ArrayList<>();
        return selectionFiles;
    }
}

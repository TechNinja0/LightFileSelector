package com.tea.fileselectlibrary.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * 作者：WQ
 * 时间：2019/5/215 16:57
 */
public class LightFileFilter implements FileFilter {
    private String[] mSelectorTypes;
    private String[] mNoSelectorDirectors;//以该数组元素开头的文件夹不选

    public LightFileFilter(String[] types, String[] mNoSelectorDirectors) {
        this.mSelectorTypes = types;
        this.mNoSelectorDirectors = mNoSelectorDirectors;
    }

    @Override
    public boolean accept(File file) {
        boolean accept = false;
        if (file.isDirectory()) {
            if (mNoSelectorDirectors!=null && mNoSelectorDirectors.length>0){
                for (String mNoSelectorDirector : mNoSelectorDirectors) {
                    if (file.getName().startsWith(mNoSelectorDirector.toLowerCase()) ||
                            file.getName().startsWith(mNoSelectorDirector.toUpperCase())) {
                        return false;
                    }
                }
            }
            return true;
        }else{
            if (mSelectorTypes != null && mSelectorTypes.length > 0) {
                for (String mSelectorType : mSelectorTypes) {
                    if (file.getName().endsWith(mSelectorType.toLowerCase()) || file.getName().endsWith(mSelectorType.toUpperCase())) {
                        accept =  true;
                        break;
                    }
                }
            }else {
                accept = true;
            }
            if (accept && mNoSelectorDirectors!=null && mNoSelectorDirectors.length>0){
                for (String mNoSelectorDirector : mNoSelectorDirectors) {
                    if (file.getName().startsWith(mNoSelectorDirector.toLowerCase()) ||
                            file.getName().startsWith(mNoSelectorDirector.toUpperCase())) {
                        return false;
                    }
                }
            }
        }

        return accept;
    }
}

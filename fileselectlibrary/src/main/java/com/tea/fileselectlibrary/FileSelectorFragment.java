package com.tea.fileselectlibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tea.fileselectlibrary.adapter.FileAdapter;
import com.tea.fileselectlibrary.config.FileSelectorConfig;
import com.tea.fileselectlibrary.filter.LightFileFilter;
import com.tea.fileselectlibrary.utils.FileUtils;
import com.tea.fileselectlibrary.view.EmptyRecyclerView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：WQ
 * 时间：2019/5/02 16:57
 */
public class FileSelectorFragment extends Fragment {
    public static final String PARAMENTITY = "FileSelectorFragment";
    private EmptyRecyclerView mRecylerView;
    private View mEmptyView;
    private FileAdapter mFileAdapter;
    private List<File> mListFiles = new ArrayList<>();
    private ArrayList<String> selectFiles = new ArrayList<>();//存放选中条目的数据地址
    private FileSelectorConfig mFileSelectorConfig;
    private FileFilter mFilter;
    private FileSelectorActivity activity;
    private int[] nums;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_file_picker, null);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        initListener();
    }

    private void initListener() {
        mFileAdapter.setOnItemClickListener(new FileAdapter.OnItemClickListener() {
            @Override
            public void click(int position) {
                if (getActivity() instanceof FileSelectorActivity) {
                    ((FileSelectorActivity) getActivity()).onItemClick(position);
                }
            }
        });
    }

    private void initData() {
        if (getActivity() != null && getActivity() instanceof FileSelectorActivity) {
            activity = (FileSelectorActivity) getActivity();
            mListFiles = activity.getDirFiles();
            mFileSelectorConfig = activity.getmFileSelectorConfig();
            selectFiles = activity.getSelectFiles();
        }
        nums = new int[mListFiles.size()];
        mFilter = new LightFileFilter(mFileSelectorConfig.fileTypes, mFileSelectorConfig.notSelectStartWith);
        mFileAdapter = new FileAdapter(mListFiles, getContext());
        mFileAdapter.setSelectFiles(selectFiles);
        mRecylerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecylerView.setAdapter(mFileAdapter);
        mRecylerView.setmEmptyView(mEmptyView);
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (int i = 0; i < mListFiles.size(); i++) {
                    File file = mListFiles.get(i);
                    if (file.isDirectory()) {
                        List<File> fileList = FileUtils.getFileList(file.getAbsolutePath(), mFilter, mFileSelectorConfig.isLargerFileSize, mFileSelectorConfig.fileSize);
                        nums[i] = fileList.size();
                    }
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mFileAdapter.setNums(nums);
                        }
                    });

                }

            }
        }.start();
    }


    private void initView(View view) {
        mRecylerView = view.findViewById(R.id.recylerview);
        mEmptyView = view.findViewById(R.id.empty_view);

    }

}

package com.tea.fileselectlibrary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tea.fileselectlibrary.config.FileSelectorConfig;
import com.tea.fileselectlibrary.filter.LightFileFilter;
import com.tea.fileselectlibrary.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 作者：WQ
 * 时间：2019/5/215 16:57
 */
public class FileSelectorActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String PATHS = "file_selector_path";
    private final String TAG = "FilePickerLeon";
    private String mPath;
    private List<File> dirFiles;
    private ArrayList<String> selectFiles = new ArrayList<>();//存放选中条目的数据地址
    private FileSelectorConfig mFileSelectorConfig;
    private LightFileFilter mFilter;
    private RelativeLayout titleLayout;
    private TextView title;
    private TextView rightBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_file_picker);
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_no);
        initView();
        if (!checkSDState()) {
            Toast.makeText(this, R.string.file_NotFoundPath, Toast.LENGTH_SHORT).show();
            return;
        }
        initData();
    }

    private void initData() {
        mFileSelectorConfig = FileSelectorConfig.getInstance();
        initTitle();
        mPath = mFileSelectorConfig.rootPath;
        mFilter = new LightFileFilter(mFileSelectorConfig.fileTypes, mFileSelectorConfig.notSelectStartWith);
        dirFiles = FileUtils.getFileList(mPath, mFilter, mFileSelectorConfig.isLargerFileSize, mFileSelectorConfig.fileSize);
        setTitleText();
        getSupportFragmentManager().beginTransaction().add(R.id.framlayout, new FileSelectorFragment()).commit();
    }


    /**
     * 更新Toolbar展示
     */
    private void initTitle() {
        if (mFileSelectorConfig.titleColor != null) {
            titleLayout.setBackgroundColor(Color.parseColor(mFileSelectorConfig.titleColor)); //设置标题颜色
        }
        if (mFileSelectorConfig.backgroundColor != null) {
            titleLayout.setBackgroundColor(Color.parseColor(mFileSelectorConfig.backgroundColor)); //设置标题背景颜色
        }
    }


    @SuppressLint("StringFormatMatches")
    public void onItemClick(int position) {

        if (mFileSelectorConfig.mutilyMode) {
            if (dirFiles.get(position).isDirectory()) {
                //如果当前是目录，则进入继续查看目录
                enterDirectory(position);
            } else {
                //如果已经选择则取消，否则添加进来
                if (selectFiles.contains(dirFiles.get(position).getAbsolutePath())) {
                    selectFiles.remove(dirFiles.get(position).getAbsolutePath());
                } else {
                    selectFiles.add(dirFiles.get(position).getAbsolutePath());
                }
                updateMenuTitle();
            }
        } else {
            //单选模式直接返回
            if (dirFiles.get(position).isDirectory()) {
                enterDirectory(position);
                return;
            }
            if (mFileSelectorConfig.isChooseFile) {
                //选择文件模式,需要添加文件路径，否则为文件夹模式，直接返回当前路径
                selectFiles.add(dirFiles.get(position).getAbsolutePath());
                doFinish();
            }
        }
    }


    /**
     * 点击进入目录
     * @param position 条目
     */
    private void enterDirectory(int position) {
        mPath = dirFiles.get(position).getAbsolutePath();
        //更新数据源
        dirFiles = FileUtils.getFileList(mPath, mFilter, mFileSelectorConfig.isLargerFileSize, mFileSelectorConfig.fileSize);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out,
                        R.anim.slide_right_in, R.anim.slide_right_out)
                .add(R.id.framlayout, new FileSelectorFragment())
                .addToBackStack("")
                .commit();
        setTitleText();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!this.isFinishing()) {
            String tempPath = new File(mPath).getParent();
            if (tempPath == null) {
                return;
            }
            mPath = tempPath;
            dirFiles = FileUtils.getFileList(mPath, mFilter, mFileSelectorConfig.isLargerFileSize, mFileSelectorConfig.fileSize);
            setTitleText();
        }
    }

    /**
     * 完成提交
     */
    private void doFinish() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(PATHS, selectFiles);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        titleLayout = findViewById(R.id.top_title_layout);
        View leftBT = findViewById(R.id.bt_leftButton);
        leftBT.setOnClickListener(this);
        title = findViewById(R.id.tv_title);
        rightBT = findViewById(R.id.bt_rightButton);
        rightBT.setOnClickListener(this);
    }

    /**
     * 检测SD卡是否可用
     */
    private boolean checkSDState() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 显示顶部地址
     */
    private void setTitleText() {
        String name = TextUtils.isEmpty(mFileSelectorConfig.title) ? "请选择文件" : mFileSelectorConfig.title;
        String nextName = mPath.replace(mFileSelectorConfig.rootPath+"/", "");
        title.setText((nextName.contains(mFileSelectorConfig.rootPath) ? name : nextName));
        updateMenuTitle();
    }


    /**
     * 更新选项菜单文字
     */
    public void updateMenuTitle() {
        rightBT.setEnabled(selectFiles.size() != 0);
        if (selectFiles.size()>0){
            rightBT.setText("确定("+selectFiles.size()+")");
        }else{
            rightBT.setText("确定");
        }
    }

    public FileSelectorConfig getmFileSelectorConfig() {
        return mFileSelectorConfig;
    }

    public List<File> getDirFiles() {
        return dirFiles;
    }

    public ArrayList<String> getSelectFiles() {
        return selectFiles;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_leftButton) {
            onBackPressed();
        } else if (id == R.id.bt_rightButton) {
            if (selectFiles != null && selectFiles.size() == 0){
                return;
            }
            doFinish();
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_no, R.anim.slide_right_out);
    }
}

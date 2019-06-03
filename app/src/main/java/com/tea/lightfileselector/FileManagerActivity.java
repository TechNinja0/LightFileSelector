package com.tea.lightfileselector;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tea.fileselectlibrary.FileSelector;
import com.tea.lightfileselector.tools.OpenFileUtils;

import java.io.File;

public class FileManagerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String FILE_SELECT_MAXNUM = "file_select_max_num";
    private static final int REQUESTCODE = 11;
    private int maxnum;
    private String sdCardPath;
    private RelativeLayout sdMemoryLayout;
    private boolean isGetPermission = true;

    public static void startFileManagerActivityForResult(Activity activity, int maxNum, int requestCode) {
        Intent intent = new Intent(activity, FileManagerActivity.class);
        intent.putExtra(FILE_SELECT_MAXNUM, maxNum);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_file_manager);
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_no);
        initView();
        initData();
    }

    private void initData() {
        maxnum = getIntent().getIntExtra(FILE_SELECT_MAXNUM, 0);
        sdCardPath = getSDCardPath();
        if (new File(sdCardPath).exists()) {
            sdMemoryLayout.setVisibility(View.VISIBLE);
        } else {
            sdMemoryLayout.setVisibility(View.GONE);
        }
        int i = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (i == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},22);
        }
    }

    private void initView() {
        View leftBT = findViewById(R.id.bt_leftButton);
        leftBT.setOnClickListener(this);
        RelativeLayout phoneMemoryLayout = findViewById(R.id.layout_memory_phone);
        phoneMemoryLayout.setOnClickListener(this);
        sdMemoryLayout = findViewById(R.id.layout_memory_sd);
        sdMemoryLayout.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sdCardPath = getSDCardPath();
        if (new File(sdCardPath).exists()) {
            sdMemoryLayout.setVisibility(View.VISIBLE);
        } else {
            sdMemoryLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_no, R.anim.slide_right_out);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_leftButton) {
            onBackPressed();
        } else {
            if (!isGetPermission){
                Toast.makeText(this,"未获取权限",Toast.LENGTH_SHORT).show();
            }
            if (id == R.id.layout_memory_phone) {
                String phonePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                openFile(phonePath,"手机存储");
            } else if (id == R.id.layout_memory_sd) {
                openFile(sdCardPath,"SDCard");
            }

        }
    }

    private void openFile(String rootPath, String title) {
        FileSelector.create(this)
                .isChooseFile(true)//设置是选择文件还是文件夹
                .setMaxNum(maxnum)//设置选择最大数量
                .setTitle(title)//设置title
                .setRootPath(rootPath)//设置需要选择的文件路径，默认为根路径
                .setMaxFileSize(100 * 1024 * 1024)//设置可以选择文件大小的最大值
                .setFileFilter(new String[]{"pdf", "doc", "docx","txt","png","mp4"})//设置过滤需要保留的扩展名
                .startForResult(REQUESTCODE);

    }

    public String getSDCardPath() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return OpenFileUtils.getSDCardPath6();
        } else {
            return OpenFileUtils.getSDCardPath5();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUESTCODE) {
            setResult(RESULT_OK, data);
            this.finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ( requestCode == 22){
            for (int grantResult : grantResults) {
                Log.d("FileManagerActivity","grantResult:"+grantResult);

                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    isGetPermission = false;
//                    break;
                }
            }
            for (String permission : permissions) {
                Log.d("FileManagerActivity","permission:"+permission);
            }

        }
    }
}

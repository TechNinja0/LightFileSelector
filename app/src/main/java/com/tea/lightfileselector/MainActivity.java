package com.tea.lightfileselector;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tea.fileselectlibrary.FileSelector;
import com.tea.lightfileselector.adapter.FilesAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> paths = new ArrayList<>();
    private FilesAdapter filesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.recylerview);
        filesAdapter = new FilesAdapter(this, paths);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(filesAdapter);
    }


    public void selectfile(View view) {
        FileManagerActivity.startFileManagerActivityForResult(this,9,11);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 11){
            List<String> files = FileSelector.obtainSelectorList(data);
            filesAdapter.setpaths(files);
        }
    }
}

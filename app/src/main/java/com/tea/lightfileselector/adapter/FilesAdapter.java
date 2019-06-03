package com.tea.lightfileselector.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tea.lightfileselector.R;

import java.util.ArrayList;
import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.FileSelecterViewHolder> {
    private Context content;
    private LayoutInflater inflater;
    private List<String> paths = new ArrayList<>();


    class FileSelecterViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        FileSelecterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_file_name);
        }
    }

    public FilesAdapter(Context context, List<String> paths) {
        this.content = context;
        this.inflater = LayoutInflater.from(context);
        setpaths(paths);
    }

    /**
     * 设置和更新数据
     *
     * @param path
     */
    public void setpaths(@NonNull List<String> path) {
        this.paths = new ArrayList<>(path);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public FileSelecterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_adapter, viewGroup, false);
        return new FileSelecterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final FileSelecterViewHolder viewHolder, final int position) {
        String filePath = paths.get(position);
        viewHolder.tvName.setText(filePath);
    }



}

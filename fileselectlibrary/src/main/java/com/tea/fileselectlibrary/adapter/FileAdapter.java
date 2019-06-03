package com.tea.fileselectlibrary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tea.fileselectlibrary.R;
import com.tea.fileselectlibrary.config.FileSelectorConfig;
import com.tea.fileselectlibrary.utils.DoubleUtils;
import com.tea.fileselectlibrary.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：King
 * 时间：2019/5/5 16:57
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.PathViewHolder> {
    public interface OnItemClickListener {
        void click(int position);
    }

    private List<File> fileList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private LayoutInflater mIflater;
    private FileSelectorConfig mParamEntity;
    private ArrayList<String> selectFiles = new ArrayList<>();//存放选中条目的数据地址
    private int[] nums;

    public FileAdapter(List<File> fileList, Context mContext) {
        this.fileList = fileList;
        this.mContext = mContext;
        mIflater = LayoutInflater.from(mContext);
        this.mParamEntity = FileSelectorConfig.getInstance();
        this.nums = new int[fileList.size()];
    }

    @NonNull
    @Override
    public PathViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mIflater.inflate(R.layout.item_file, null);
        return new PathViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    @Override
    public void onBindViewHolder(final PathViewHolder holder, final int position) {
        final File file = fileList.get(position);
        if (file.isFile()) {
            updateFileIconStyle(holder.ivType, file);
            holder.fileLyaout.setVisibility(View.VISIBLE);
            holder.dirLayout.setVisibility(View.GONE);
            holder.tvName.setText(file.getName());
            holder.tvDetail.setText(FileUtils.getReadableFileSize(file.length()));
        } else {
            holder.ivType.setImageResource(R.drawable.ic_dir);
            holder.fileLyaout.setVisibility(View.GONE);
            holder.dirLayout.setVisibility(View.VISIBLE);
            holder.tvDirName.setText(file.getName());
            holder.tvDirNum.setText(String.valueOf(nums[position]));
        }
        if (!mParamEntity.mutilyMode) {
            holder.cbChoose.setVisibility(View.GONE);
        }
        holder.layoutRoot.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StringFormatMatches")
            @Override
            public void onClick(View v) {
                if (DoubleUtils.isFastDoubleClickShort()) {
                    return;
                }
                if (file.isFile()) {
                    if (!selectFiles.contains(fileList.get(position).getAbsolutePath()) && selectFiles.size() >= mParamEntity.maxNum) {
                        Toast.makeText(mContext, mContext.getString(R.string.lfile_OutSize, mParamEntity.maxNum), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (fileList.get(position).length() > mParamEntity.maxFileSize) {
                        Toast.makeText(mContext, mContext.getString(R.string.file_Outlength,FileUtils.getReadableFileSize(mParamEntity.maxFileSize)), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    holder.cbChoose.setChecked(!selectFiles.contains(fileList.get(position).getAbsolutePath()));
                }
                if (onItemClickListener != null)
                    onItemClickListener.click(position);
            }
        });
        holder.cbChoose.setClickable(false);
        holder.cbChoose.setOnCheckedChangeListener(null);//先设置一次CheckBox的选中监听器，传入参数null
        holder.cbChoose.setChecked(selectFiles.contains(fileList.get(position).getAbsolutePath()));
    }


    private void updateFileIconStyle(ImageView imageView, File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith("pdf")) {
            imageView.setImageResource(R.drawable.ic_pdf);
        } else if (fileName.endsWith("doc") || fileName.endsWith("docx")) {
            imageView.setImageResource(R.drawable.ic_word);
        } else if (fileName.endsWith("txt")) {
            imageView.setImageResource(R.drawable.ic_txt);
        } else if (fileName.endsWith("pptx") || fileName.endsWith("ppt")) {
            imageView.setImageResource(R.drawable.ic_ppt);
        } else if (fileName.endsWith("jpg") || fileName.endsWith("png") || fileName.endsWith("mp4")){
            imageView.setImageResource(R.drawable.icon_photo);
        } else{
            imageView.setImageResource(R.drawable.ic_other);
        }
    }

    /**
     * 设置监听器
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置数据源
     *
     * @param fileList
     */
    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    public void setNums(int[] nums) {
        this.nums = nums;
        notifyDataSetChanged();
    }

    class PathViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout layoutRoot;
        private ImageView ivType;
        private TextView tvName;
        private TextView tvDetail;
        private CheckBox cbChoose;
        private ImageView ivRight;
        private RelativeLayout fileLyaout;
        private RelativeLayout dirLayout;
        private TextView tvDirName;
        private TextView tvDirNum;

        public PathViewHolder(View itemView) {
            super(itemView);
            ivType = itemView.findViewById(R.id.iv_type);
            layoutRoot = itemView.findViewById(R.id.layout_item_root);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDetail = itemView.findViewById(R.id.tv_detail);
            cbChoose = itemView.findViewById(R.id.cb_choose);
            ivRight = itemView.findViewById(R.id.iv_right);
            fileLyaout = itemView.findViewById(R.id.layout_file);
            dirLayout = itemView.findViewById(R.id.layout_dir);
            tvDirName = itemView.findViewById(R.id.tv_dir_name);
            tvDirNum = itemView.findViewById(R.id.tv_dir_num);
        }
    }

    public void setSelectFiles(ArrayList<String> selectFiles) {
        this.selectFiles = selectFiles;
    }
}



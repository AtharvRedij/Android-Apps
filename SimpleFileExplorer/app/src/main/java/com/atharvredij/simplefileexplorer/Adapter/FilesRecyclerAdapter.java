package com.atharvredij.simplefileexplorer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atharvredij.simplefileexplorer.ClickListener.ItemClickListener;
import com.atharvredij.simplefileexplorer.ClickListener.ItemLongClickListener;
import com.atharvredij.simplefileexplorer.Model.FileModel;
import com.atharvredij.simplefileexplorer.Utils.FileType;
import com.atharvredij.simplefileexplorer.Utils.FileUtils;
import com.atharvredij.simplefileexplorer.UI.FilesListFragment;
import com.atharvredij.simplefileexplorer.UI.MainActivity;
import com.atharvredij.simplefileexplorer.R;

import java.io.File;
import java.util.List;

import static com.atharvredij.simplefileexplorer.Utils.FileUtils.getCurrentPath;
import static com.atharvredij.simplefileexplorer.Utils.FileUtils.getFileModelsFromFiles;
import static com.atharvredij.simplefileexplorer.Utils.FileUtils.getFilesFromPath;

public class FilesRecyclerAdapter extends RecyclerView.Adapter<FilesRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<FileModel> filesList;

    ItemClickListener itemClickListener;

    ItemLongClickListener itemLongClickListener;

    public FilesRecyclerAdapter(Context context, List<FileModel> filesList) {
        this.context = context;
        this.filesList = filesList;
    }


    public void updateData(List<FileModel> fileModelList) {
        filesList = fileModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FilesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_recycler_file, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(rootView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FilesRecyclerAdapter.ViewHolder viewHolder, int position) {
        FileModel fileModel = filesList.get(position);
        viewHolder.nameTextView.setText(fileModel.name);

        // Depending upon fileType change what is displayed
        if (fileModel.fileType == FileType.FOLDER) {
            viewHolder.folderTextView.setVisibility(View.VISIBLE);
            viewHolder.totalSizeTextView.setVisibility(View.GONE);
            viewHolder.folderTextView.setText("(" + fileModel.subFiles +" files)");
        } else {
            viewHolder.folderTextView.setVisibility(View.GONE);
            viewHolder.totalSizeTextView.setVisibility(View.VISIBLE);
            viewHolder.totalSizeTextView.setText(fileModel.sizeInMB + " MB");// = "${String.format("%.2f", fileModel.sizeInMB)} mb"
        }

        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                FileModel fileModel = filesList.get(position);
                if (fileModel.fileType == FileType.FOLDER) {

                    FileUtils.breadcrumbList.add(fileModel);
                    MainActivity.mBreadcrumbRecyclerAdapter.updateData(FileUtils.breadcrumbList);
                    addFileFragment(fileModel);
                    MainActivity.breadcrumbRecyclerView.smoothScrollToPosition(FileUtils.breadcrumbList.size()-1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri myUri = FileProvider.getUriForFile(context,
                            context.getPackageName(),
                            new File(fileModel.path));
                    intent.setData(myUri);
                    context.startActivity(intent);
                }
            }
        });

        viewHolder.setItemLongClickListener(new ItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int pos) {
                showFileOptions(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(filesList == null) {
            return 0;
        } else {
            return filesList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView nameTextView, folderTextView, totalSizeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            folderTextView = itemView.findViewById(R.id.folderTextView);
            totalSizeTextView = itemView.findViewById(R.id.totalSizeTextView);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener ic)
        {
            itemClickListener=ic;
        }

        public void setItemLongClickListener(ItemLongClickListener ic)
        {
            itemLongClickListener=ic;
        }

        @Override
        public boolean onLongClick(View v) {
            itemLongClickListener.onItemLongClick(v,getLayoutPosition());
            return true;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v,getLayoutPosition());
        }
    }

    private void showFileOptions(final int position) {
        final BottomSheetDialog fileOptionsDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_file_options, null);

        TextView copyTextView = view.findViewById(R.id.copyTextView);
        TextView moveTextView = view.findViewById(R.id.moveTextView);
        TextView deleteTextView = view.findViewById(R.id.deleteTextView);
        TextView shareTextView = view.findViewById(R.id.shareTextView);

        // TODO: ISSUE - Can't share a folder Part 1
        // Hide share option if folder
        if(filesList.get(position).fileType == 1) {
            shareTextView.setVisibility(View.GONE);
        }

        copyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.pasteFAB.show();
                fileOptionsDialog.dismiss();
                final String fileName = filesList.get(position).name;
                final File srcFile = new File(getCurrentPath() + "/" + fileName);
                MainActivity.pasteFAB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File dstFile = new File(getCurrentPath() + "/" + fileName);
                        FileUtils.copyFile(srcFile, dstFile);
                        MainActivity.pasteFAB.hide();
                        Toast.makeText(context, "Path " + getCurrentPath(), Toast.LENGTH_SHORT).show();
                        updateData(getFileModelsFromFiles(getFilesFromPath(getCurrentPath())));
                    }
                });
            }
        });

        moveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.pasteFAB.show();
                fileOptionsDialog.dismiss();
                final String fileName = filesList.get(position).name;
                final File srcFile = new File(getCurrentPath() + "/" + fileName);
                MainActivity.pasteFAB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File dstFile = new File(getCurrentPath() + "/" + fileName);
                        FileUtils.moveFile(srcFile, dstFile);
                        MainActivity.pasteFAB.hide();
                        Toast.makeText(context, "Path " + getCurrentPath(), Toast.LENGTH_SHORT).show();
                        updateData(getFileModelsFromFiles(getFilesFromPath(getCurrentPath())));
                    }
                });
            }
        });

        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileModel fileModel = filesList.get(position);
                FileUtils.deleteFile(fileModel.path);
                fileOptionsDialog.dismiss();
                updateData(getFileModelsFromFiles(getFilesFromPath(getCurrentPath())));
            }
        });

        shareTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtils.shareFile(context, filesList.get(position));
                fileOptionsDialog.dismiss();
            }
        });

        fileOptionsDialog.setContentView(view);
        fileOptionsDialog.show();
    }


    // Replaces current Fragment with new one
    private void addFileFragment(FileModel fileModel) {

        Bundle bundle = new Bundle();
        bundle.putString(FilesListFragment.ARG_PATH, fileModel.path);
        FilesListFragment fragment = new FilesListFragment();
        fragment.setArguments(bundle);

        ((FragmentActivity)context).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(Environment.getExternalStorageDirectory().getAbsolutePath())
                .commit();
    }
}

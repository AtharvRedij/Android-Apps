package com.atharvredij.simplefileexplorer.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atharvredij.simplefileexplorer.ClickListener.BreadcrumbItemClickListener;
import com.atharvredij.simplefileexplorer.Model.FileModel;
import com.atharvredij.simplefileexplorer.R;

import java.util.List;

public class BreadcrumbRecyclerAdapter extends RecyclerView.Adapter<BreadcrumbRecyclerAdapter.ViewHolder>  {

    private List<FileModel> files;

    private BreadcrumbItemClickListener listener;

    public BreadcrumbRecyclerAdapter(BreadcrumbItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<FileModel> fileModelList) {
        files = fileModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BreadcrumbRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_breadcrumb,
                viewGroup, false);
        final BreadcrumbRecyclerAdapter.ViewHolder viewHolder = new BreadcrumbRecyclerAdapter.ViewHolder(rootView);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBreadcrumbItemClick(viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BreadcrumbRecyclerAdapter.ViewHolder viewHolder, int position) {
        FileModel file = files.get(position);
        viewHolder.nameTextView.setText(file.name);
    }

    @Override
    public int getItemCount() {
        if(files == null) {
            return 0;
        } else {
            return files.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }
}

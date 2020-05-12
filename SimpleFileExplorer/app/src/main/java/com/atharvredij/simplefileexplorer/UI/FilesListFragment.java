package com.atharvredij.simplefileexplorer.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.atharvredij.simplefileexplorer.Adapter.FilesRecyclerAdapter;
import com.atharvredij.simplefileexplorer.Model.FileModel;
import com.atharvredij.simplefileexplorer.Utils.FileUtils;
import com.atharvredij.simplefileexplorer.R;

import java.util.List;

public class FilesListFragment extends Fragment {

    public static String ARG_PATH = "com.atharvredij.simplefileexplorer.fileslist.path";

    public static FilesRecyclerAdapter mFilesAdapter;
    private String PATH;
    private LinearLayout emptyFolderLayout;
    private RecyclerView filesRecyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_files_list, container, false);
        filesRecyclerView = rootView.findViewById(R.id.filesRecyclerView);
        emptyFolderLayout = rootView.findViewById(R.id.emptyFolderLayout);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // path of folder passed to Fragment in form of bundle arguments
        String filePath = getArguments().getString(ARG_PATH);
        if (filePath == null) {
            Toast.makeText(getContext(), "Path should not be null!", Toast.LENGTH_SHORT).show();
            return;
        }
        PATH = filePath;

        initViews();
    }

    private void initViews() {

        final List<FileModel> files = FileUtils.getFileModelsFromFiles(FileUtils.getFilesFromPath(PATH));

        if (files.isEmpty()) {
            emptyFolderLayout.setVisibility(View.VISIBLE);
        } else {
            emptyFolderLayout.setVisibility(View.INVISIBLE);
        }


        mFilesAdapter = new FilesRecyclerAdapter(getContext(), files);

        filesRecyclerView.setAdapter(mFilesAdapter);
        filesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        filesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

    }



}

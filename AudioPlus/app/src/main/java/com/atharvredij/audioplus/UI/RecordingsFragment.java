package com.atharvredij.audioplus.UI;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atharvredij.audioplus.Adapter.RecordingsListAdapter;
import com.atharvredij.audioplus.R;
import com.atharvredij.audioplus.Utils;

import java.io.File;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordingsFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private RecordingsListAdapter mAdapter;


    public RecordingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recordings, container, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mAdapter = new RecordingsListAdapter(Utils.getListOfNames(), getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        return rootView;
    }
}

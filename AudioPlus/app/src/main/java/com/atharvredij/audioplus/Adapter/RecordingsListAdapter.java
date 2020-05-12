package com.atharvredij.audioplus.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atharvredij.audioplus.R;
import com.atharvredij.audioplus.Utils;

import java.util.List;

public class RecordingsListAdapter extends RecyclerView.Adapter<RecordingsListAdapter.RecordingsViewHolder>  {

    private List<String> names;
    private Context context;

    private MediaPlayer player = new MediaPlayer();
    private Boolean isRecordingPlaying = false;
    private String currentRecordingName = "a";

    public RecordingsListAdapter(List<String> names, Context context) {
        this.names = names;
        this.context = context;
    }

    @NonNull
    @Override
    public RecordingsListAdapter.RecordingsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.recording_list_item,
                viewGroup, false);
        return new RecordingsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordingsListAdapter.RecordingsViewHolder recordingsViewHolder, int i) {
        if(names != null) {
            String name = names.get(i);
            recordingsViewHolder.recordingNameTextView.setText(name);
        }
    }

    @Override
    public int getItemCount() {
        if(names != null) {
            return names.size();
        } else {
            return 0;
        }
    }

    public class RecordingsViewHolder extends RecyclerView.ViewHolder {

        TextView recordingNameTextView;

        public RecordingsViewHolder(@NonNull View itemView) {
            super(itemView);
            recordingNameTextView = itemView.findViewById(R.id.recordingNameTextView);

            recordingNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO : touch to play, second touch pause, touch another item play that recording

                    String name = recordingNameTextView.getText().toString();
                    if(player.isPlaying()) {
                        if(currentRecordingName.equals(name)) {
                            makeToast("1");
                            player.pause();
                        } else {
                            makeToast("2");
                            player.reset();
                            player.release();
                            player = new MediaPlayer();
                            playRecording(name);
                            currentRecordingName = name;
                        }
                    } else {
                        if(currentRecordingName.equals(name)) {
                            makeToast("3");
                            player.start();
                        } else {
                            makeToast("4");
                            player.reset();
                            player.release();
                            player = new MediaPlayer();
                            playRecording(name);
                            currentRecordingName = name;
                        }
                    }
                }
            });
        }


    }

    private void playRecording(String name) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/AudioPlus/" + name;
        try {
            player.setDataSource(path);
            player.prepare();
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


}

package com.example.msp.legaldesire;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import static com.example.msp.legaldesire.OnLoginSuccessful.myBundle;

public class Play_audio extends Fragment implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl{


    private MediaPlayer mediaPlayer;
    private MediaController mediaController;
    private String audioFile;

    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_play_audio, container, false);
        v.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    //do something
                    mediaController.show();
                }
                return true;
            }
        });

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

            TextView name=(TextView)view.findViewById(R.id.name);

            String audio=(String)myBundle.get("law_name_article");
            String namea=(String)myBundle.get("audio_name_article");

            Toast.makeText(getActivity(), namea, Toast.LENGTH_SHORT).show();

            String display_name=namea.substring(0,namea.length()-4);
            //String[] name_audio1=audio_name.split(".");
            name.setText(display_name);

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(this);

            mediaController = new MediaController(getActivity());

            try {
                mediaPlayer.setDataSource(audio);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                Log.e("tag", "Could not open file " + audioFile + " for playback.", e);
            }
        }





    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaController.hide();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

        //--MediaPlayerControl methods----------------------------------------------------
    public void start() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean canPause() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }
    //--------------------------------------------------------------------------------

    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d("tqg", "onPrepared");
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(getActivity().findViewById(R.id.main_audio_view));

        handler.post(new Runnable() {
            public void run() {
                mediaController.setEnabled(true);
                mediaController.show();
            }
        });
    }
}




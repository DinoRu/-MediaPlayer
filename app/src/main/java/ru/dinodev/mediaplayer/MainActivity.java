package ru.dinodev.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextView player_position, player_duration;
    SeekBar seekBar;
    ImageView btnRewind, btnForward, btnPlayer, btnPause, btnStop, btnRepeat;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVew();

        mediaPlayer = MediaPlayer.create(this, R.raw.booba);

        runnable = new Runnable() {
            @Override
            public void run() {
                //Set Progress on seek bar
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                //Handler post delay for 0.5 second
                handler.postDelayed(this, 500);
            }
        };

        //Get duration media player
        int duration = mediaPlayer.getDuration();
        String sDuration  = convertFormat(duration);

        //Set duration on text view
        player_duration.setText(sDuration);

        btnPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide btnPlay
                btnPlayer.setVisibility(View.GONE);
                //Show btnPause
                btnPause.setVisibility(View.VISIBLE);
                //Media start player
                mediaPlayer.start();
                //Set Max on seek bar
                seekBar.setMax(mediaPlayer.getDuration());
                //Set handler
                handler.postDelayed(runnable, 0);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide btnPause
                btnPause.setVisibility(View.GONE);
                //Show btnPlayer
                btnPlayer.setVisibility(View.VISIBLE);
                //Media player pause
                handler.removeCallbacks(runnable);
            }
        });

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get current position of media player
                int currentPosition = mediaPlayer.getCurrentPosition();
                //Get duration of media player
                int duration = mediaPlayer.getDuration();
                //Chek condition
                if (mediaPlayer.isPlaying() && duration != currentPosition){
                    //When media is playing and duration is not equal to current position
                    //Fast forward position on text view
                     currentPosition = currentPosition + 5000;
                    player_position.setText(convertFormat(currentPosition));
                    //Set progrss on seek bar
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });

    btnRewind.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Get current position of media player
            int currentPosition = mediaPlayer.getCurrentPosition();
            //Check condition
            if (mediaPlayer.isPlaying() && currentPosition > 5000){
                currentPosition = currentPosition - 5000;
                player_position.setText(convertFormat(currentPosition));
                //Set progress on seek bar
                mediaPlayer.seekTo(currentPosition);
            }
        }
    });

    btnStop.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //hide button pause
            btnPause.setVisibility(View.GONE);
            //Show button play
            btnPlayer.setVisibility(View.VISIBLE);
            //Stop media current play now
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    });

    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
          //Check condition
          if (fromUser){
              //When drag the seek bar
              //Set progress on seek bar
              mediaPlayer.seekTo(progress);
          }
          //Set current position on text view
          player_position.setText(convertFormat(mediaPlayer.getCurrentPosition()));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });

    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            //Hide pause button
            btnPause.setVisibility(View.GONE);
            //Show play button
            btnPlayer.setVisibility(View.VISIBLE);

            mediaPlayer.seekTo(0);
        }
    });

    btnRepeat.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.reset();
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.booba);
            }
            mediaPlayer.start();
        }
    });

    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration){
        return String.format("%02d:%02d"
                , TimeUnit.MILLISECONDS.toMinutes(duration)
                ,TimeUnit.MILLISECONDS.toSeconds(duration)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }


    private void initVew() {
        player_duration = findViewById(R.id.player_duration);
        player_position = findViewById(R.id.player_position);
        seekBar = findViewById(R.id.seek_bar);
        btnForward = findViewById(R.id.btnForward);
        btnPause = findViewById(R.id.btnPause);
        btnPlayer = findViewById(R.id.btnPlay);
        btnStop = findViewById(R.id.btnStop);
        btnRewind = findViewById(R.id.btnRewind);
        btnRepeat = findViewById(R.id.btnRepeat);
    }
}
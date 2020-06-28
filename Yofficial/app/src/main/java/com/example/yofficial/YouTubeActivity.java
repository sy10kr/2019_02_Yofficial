package com.example.yofficial;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;



//로딩중엔 음성인식 안되게
public class YouTubeActivity extends YouTubeBaseActivity implements SensorEventListener {

    private static final int NOTHING = -1;
    private static final int PLAYING = 0;
    private static final int PAUSED = 1;
    private static final int LOADING = 2;
    private static final int LOADED = 3;
    private static final int ENDED = 4;

    private CheckThread t = null;
    private int savedVState;
    private int flag = 0;
    private static final String TAG = "YouTubeActivity!";

    ArrayList<Integer> start_time;
    ArrayList<Integer> end_time;
    int totalStep;
    String videoId;
    ArrayList<String> startTime;
    ArrayList<String> endTime;
    ArrayList<String> desc;


    int step = 0;
    int videoState = -1;

    TextView tv;




    YouTubePlayerView mYouTubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;
    YouTubePlayer.PlayerStateChangeListener mPlayerStateChangeListener;
    YouTubePlayer.PlaybackEventListener mPlayBackEventListener;
    YouTubePlayer player = null;

    Button btnPlay;
    Button btnPause;
    Button btnNext;
    Button btnPrev;
    Button btnReplay;


    //junhong start

    private SensorManager sensorManager;
    Intent intent;
    final int PERMISSION = 1;
    SpeechRecognizer mRecognizer;
    TextView voiceTv;

    //junhong end




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {




        //junhongstart
        Bundle extras = getIntent().getExtras();
        videoId = extras.getString("url");
        Log.d(TAG, videoId);





        startTime = (ArrayList<String>)extras.get("startTime");
        endTime = (ArrayList<String>)extras.get("endTime");
        desc = (ArrayList<String>) extras.get("stepDesc");

        Log.d(TAG, startTime.get(0));
        Log.d(TAG, endTime.get(0));
        Log.d(TAG, ""+desc.size());

        start_time = new ArrayList<>();
        end_time = new ArrayList<>();
        totalStep = desc.size();
        for(int i = 0; i < totalStep; i++){
            start_time.add(Integer.parseInt(startTime.get(i)));
            end_time.add(Integer.parseInt(endTime.get(i)));
        }
        Log.d(TAG, ""+desc.size());








        //voiceTv = findViewById(R.id.voiceTextView);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximitySensor == null) {
            Toast.makeText(this, "센서를 찾을 수 없음", Toast.LENGTH_SHORT).show();
        }

        if (Build.VERSION.SDK_INT >= 23) {
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        //junhong end



        Log.d(TAG, "onCreate : Starting.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        //find button from layout
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.pause);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnReplay = findViewById(R.id.btnReplay);

        //find youtube view from layout
        mYouTubePlayerView = findViewById(R.id.youtubePlay);

        //find textview from layout
        tv = findViewById(R.id.description);


        //when video is initialized this method will be called.
        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "onClick : Done initializing");
                player = youTubePlayer;
                player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                player.setPlayerStateChangeListener(mPlayerStateChangeListener);
                player.setPlaybackEventListener(mPlayBackEventListener);
                player.loadVideo(videoId);
                videoState = LOADING;
                Log.d(TAG, "VideoStateChanged = " + videoState + " must be " + LOADING);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onClick : Fail to initializing");
            }
        };





        //when play button clicked
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                pause();
            }
        });

        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replay();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prev();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        mPlayerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {
                videoState = LOADING;
                Log.d(TAG, "VideoStateChanged = " + videoState + " must be " + LOADING);
            }

            @Override
            public void onLoaded(String s) {
                videoState = LOADED;
                Log.d(TAG, "VideoStateChanged = " + videoState + " must be " + LOADED);
            }

            @Override
            public void onAdStarted() {

            }

            @Override
            public void onVideoStarted() {
                Log.d(TAG, "Video Started");
                videoState = PLAYING;
                Log.d(TAG, "VideoStateChanged = " + videoState+ " must be " + PLAYING);
                player.seekToMillis(start_time.get(step) * 1000);

            }
            @Override
            public void onVideoEnded() {
                videoState = ENDED;
                Log.d(TAG, "VideoStateChanged = " + videoState + " must be " + ENDED);
            }
            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {

            }
        };

        mPlayBackEventListener = new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {

            }

            @Override
            public void onPaused() {
                //Log.d(TAG, "PAUSE!!");
            }

            @Override
            public void onStopped() {

            }

            @Override
            public void onBuffering(boolean b) {

            }

            @Override
            public void onSeekTo(int i) {

                if(t != null){
                    if(t.isAlive()){
                        Log.d(TAG, "t is alive");
                        t.flag = 1;
                        try {
                            t.join();
                        } catch (InterruptedException e) {
                            Log.d(TAG, e.getStackTrace().toString());
                        }
                    }
                }

                t = new CheckThread();
                t.start();

                player.play();
                videoState = PLAYING;
                Log.d(TAG, "VideoStateChanged = " + videoState + " must be " + PLAYING);
                tv.setPadding(5,0,0,0);
                tv.setText(desc.get(step));



            }
        };

    }


    //junhong start

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] == 0) {

                savedVState = videoState;
                if(videoState == PLAYING)
                    pause();
                // 센서 가까워지면 음성인식 작동
                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
                mRecognizer.setRecognitionListener(listener);
                mRecognizer.startListening(intent);
            }
        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        switch (accuracy) {
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                Toast.makeText(this, "UNRELIABLE", Toast.LENGTH_SHORT).show();
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                Toast.makeText(this, "LOW", Toast.LENGTH_SHORT).show();
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                Toast.makeText(this, "MEDIUM", Toast.LENGTH_SHORT).show();
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                Toast.makeText(this, "HIGH", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(), "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }

            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
            Log.d(TAG, "Enter to On Results");
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            int fail = 0;
            int resultNum = matches.size();

            for(int idx = 0; idx < resultNum; idx++){

                Log.d(TAG, matches.get(idx));
                if(matches.get(idx).contains("다음")){
                    next();
                    break;

                }
                else if(matches.get(idx).contains("이전")){
                    prev();
                    break;
                }

                else if(matches.get(idx).contains("다시")){
                    replay();
                    break;
                }

                else if(matches.get(idx).contains("정지")){
                    pause();
                    break;
                }

                else if(matches.get(idx).contains("재생")){
                    play();
                    break;
                }
                else{
                    fail++;
                }

            }
            if(fail == resultNum){
                if(savedVState == PLAYING){
                    play();
                }
            }




            /*
            for (int i = 0; i < matches.size(); i++) {
                voiceTv.setText(matches.get(i));
            }
             */
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
        }
    };


    void prev() {
        if(videoState == PAUSED || videoState == PLAYING || videoState == ENDED) {
            if (step > 0) {
                step -= 1;
            }

            if(t != null){
                if(t.isAlive()){
                    t.flag = 1;
                }
            }


            player.seekToMillis(start_time.get(step) * 1000);
        }
    }

    void replay() {


        if(videoState == PAUSED || videoState == PLAYING || videoState == ENDED) {
            if(t != null){
                if(t.isAlive()){
                    t.flag = 1;
                }
            }
            player.seekToMillis(start_time.get(step) * 1000);
        }
    }

    void pause() {

        if(videoState == PLAYING) {
            player.pause();
            videoState = PAUSED;
            Log.d(TAG, "VideoStateChanged = " + videoState + " must be " + PAUSED);
        }
    }

    void play() {
        if(videoState == NOTHING){
            Log.d(TAG, "Onclick : Initializing YouTube Player.");
            mYouTubePlayerView.initialize(YouTubeConfig.getApiKey(), mOnInitializedListener);
        }

        if(videoState == PAUSED){
            player.play();
            videoState = PLAYING;
            Log.d(TAG, "VideoStateChanged = " + videoState + " must be " + PLAYING);
        }
    }

    void next() {
        if(videoState == PAUSED || videoState == PLAYING) {
            if (step < totalStep - 1) {
                step += 1;
                if(t != null){
                    if(t.isAlive()){
                        t.flag = 1;
                    }
                }
                player.seekToMillis(start_time.get(step) * 1000);
            }
            else{
                Toast.makeText(getApplicationContext(),"마지막 단계 입니다.", Toast.LENGTH_LONG).show();
            }


        }
    }
    //junhong end



    public void onBackButtonClicked(View v){
        if(videoState != LOADING){
            if(t != null){
                if(t.isAlive()){
                    Log.d(TAG, "t is alive");
                    t.flag = 1;
                    try {
                        Log.d(TAG, "wait for t");
                        t.join();
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


        }

    }

    @Override
    public void onBackPressed() {
        if(videoState != LOADING){
            if(t != null){
                if(t.isAlive()){
                    Log.d(TAG, "t is alive");
                    t.flag = 1;
                    try {
                        Log.d(TAG, "wait for t");
                        t.join();
                        super.onBackPressed();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            player.setFullscreen(true);
        }

    }




    class CheckThread extends Thread{

        int flag;

        CheckThread(){
            flag = 0;
        }


        @Override
        public void run() {
            try {
                while(player.getCurrentTimeMillis() < end_time.get(step) * 1000 && flag != 1 && step != totalStep){
                    Log.d(TAG, this.getId()+" : "+ player.getCurrentTimeMillis());
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d(TAG, "Error Occur in run() of thread");
            }


            try {

                if (flag == 0) {
                    tStop();
                    Log.d(TAG, "Stopped at time : " + player.getCurrentTimeMillis());
                    step += 1;
                    Log.d(TAG, "State = " + step);
                    if (step == totalStep) {
                        fin();
                        Log.d(TAG, "All Video Finished");
                    } else {
                        searchNext();
                    }
                }
            }catch (Exception e){
                Log.d(TAG, e.getStackTrace().toString());

            }

            Log.d(TAG, this.getId() + " : Thread Completed");
        }

        void tStop(){
            pause();
            Log.d(TAG, "VideoStateChanged = " + videoState + " must be " + PAUSED);
        }

        void fin() {

            Log.d(TAG, "EnterFinish");

            //Toast.makeText(getApplicationContext(), "Finished", Toast.LENGTH_LONG).show();
            videoState = ENDED;
            Log.d(TAG, "VideoStateChanged = " + videoState + " must be " + ENDED);
            Log.d(TAG, "Final State = " + videoState);
            Intent intent = new Intent(getApplicationContext(), eval_popup.class);
            startActivity(intent);
        }

        void searchNext(){
            player.seekToMillis(start_time.get(step) * 1000);

        }


    }

}






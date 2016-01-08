package com.example.aida.audiocapture;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MediaRecorder myAudioRecorder;
    private String outputFile=null;
    private Button start,stop,play,choose,stopMusic;
    MediaPlayer md=new MediaPlayer();

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start=(Button) findViewById(R.id.button1);
        stop=(Button) findViewById(R.id.button2);
        play=(Button) findViewById(R.id.button3);
        choose=(Button) findViewById(R.id.button4);
        stopMusic=(Button) findViewById(R.id.button5);

        choose.setEnabled(true);
        stopMusic.setEnabled(true);

        stop.setEnabled(false);
        play.setEnabled(false);
        outputFile=Environment.getExternalStorageDirectory().getAbsolutePath()+"/myrecording.3gp";

        //1.create a mediarecorder object
        myAudioRecorder=new MediaRecorder();

        //set the source,output and encoding format and output file
        //2.this method specifies the source of audio to be recorded
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        //3.this method specifies the audio format in which auioto be stored
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        //4.this method specifies the audio to be used
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
        ;
        //5.choose the file to save audio
        myAudioRecorder.setOutputFile(outputFile);
    }

    //6.methods in the mediaRecorder allow you to start and stop recording
    public void start(View view){
        try{
            //two basic methods prepare and start to start recording the audio
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch(Exception e){
            e.printStackTrace();
        }
        start.setEnabled(false);
        stop.setEnabled(true);
        Toast.makeText(getApplicationContext(),"Recording started",Toast.LENGTH_LONG).show();;
    }
    public void stop(View view){

        //this method stops the recording process
        myAudioRecorder.stop();

        //this method should be called when the recorder instance is needed
        myAudioRecorder.release();
        myAudioRecorder=null;
        stop.setEnabled(false);
        play.setEnabled(true);
        Toast.makeText(getApplicationContext(),"Audio recorded",Toast.LENGTH_LONG).show();


    }
    public void stopMusic(View v){
        md.stop();
    }
    public void chooseMusic(View v){
        i=new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);

    }

    //7.after the recording is done we create a mediaplayer object which gives us the method to play audio
    public void play(View view) throws IllegalArgumentException,SecurityException,IllegalStateException,IOException{
        MediaPlayer m = new MediaPlayer();
        m.setDataSource(outputFile);
        m.prepare();
        m.start();
        Toast.makeText(getApplicationContext(), "Playing audio",
                Toast.LENGTH_LONG).show();




    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==1){
            if(data!=null){
                Uri muri=data.getData();
                String uri=muri.getPath();
                File track=new File(uri);
                if(uri!=null){
                    // Uri urinew=MediaStore.Audio.Media.getContentUriForPath(track.getAbsolutePath());

                    try{
                        md.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();
                            }
                        });
                        md.setDataSource(this, muri);
                        md.prepare();
                        md.start();
                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }
                }
                else{
                    Toast.makeText(this,"NO audio selected",Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}

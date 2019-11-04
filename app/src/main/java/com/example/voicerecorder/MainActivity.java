package com.example.voicerecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    ToggleButton t_record,t_play;
    Button b_arquivo;
    String pathSave = "";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    Chronometer timer;
    Intent i_tela_arquivo;

    final int REQUEST_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!checkPermissionFromDevice())
            requestPermission();

        t_record = findViewById(R.id.t_record);
        t_play = findViewById(R.id.t_play);
        timer = findViewById(R.id.tempo);
        b_arquivo = findViewById(R.id.b_arquivo);


        b_arquivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i_tela_arquivo = new Intent(MainActivity.this,Tela_Arquivo.class);
                startActivity(i_tela_arquivo);
                System.out.println("Arquivo");
            }
        });

        t_record.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (checkPermissionFromDevice()) {
                        pathSave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                                + UUID.randomUUID().toString() + "_audio_file.3gp";
                        setupMediaRecorder();
                        try {
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                            t_play.setEnabled(false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
                    } else {
                        requestPermission();
                    }
                    timer.setBase(SystemClock.elapsedRealtime());
                    timer.start();
                }
                else
                {
                    t_play.setEnabled(true);
                    mediaRecorder.stop();
                    timer.stop();
                }
            }
        });

        t_play.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    mediaPlayer = new MediaPlayer();
                    try {
                        t_record.setEnabled(false);
                        mediaPlayer.setDataSource(pathSave);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this, "Playing...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    t_record.setEnabled(true);
                    if(mediaPlayer != null)
                    {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        setupMediaRecorder();
                    }
                }
            }
        });
        }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        },REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_PERMISSION_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this,"Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
            break;
            default:

        }
    }

    private boolean checkPermissionFromDevice(){
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }
}

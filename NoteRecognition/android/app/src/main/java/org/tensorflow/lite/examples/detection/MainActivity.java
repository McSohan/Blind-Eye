package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    public static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.5f;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts = new TextToSpeech(this, this);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                openMessage();
            }
        }, 2000);

        handler.postDelayed(new Runnable() {
            public void run() {

                startActivity(new Intent(MainActivity.this, DetectorActivity.class));
            }
        }, 2000);
    }

    private void openMessage (){
        tts.speak("Note app open", TextToSpeech.QUEUE_ADD, null, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
            int result = tts.setLanguage(Locale.UK);
            if(result==TextToSpeech.LANG_MISSING_DATA ||
                    result==TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("error", "This Language is not supported");
            }
            else {
                Log.d ("Main Activity", "Language setup success");
            }
        }
        else{
            Log.e("error", "Initilization Failed!");
        }
    }

    //Take back resources from tts when the main activity closes
    @Override
    protected void onPause() {
        super.onPause();
    }
}

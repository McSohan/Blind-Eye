/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package org.tensorflow.lite.examples.detection.tracking;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.env.BorderedText;
import org.tensorflow.lite.examples.detection.env.ImageUtils;
import org.tensorflow.lite.examples.detection.env.Logger;
import org.tensorflow.lite.examples.detection.tflite.Classifier.Recognition;

/** A tracker that handles non-max suppression and matches existing objects to new detections. */
public class MultiBoxTracker {
  private static final float TEXT_SIZE_DIP = 18;

  final List<Pair<Float, RectF>> screenRects = new LinkedList<Pair<Float, RectF>>();
  private final Logger logger = new Logger();

  private final List<TrackedRecognition> trackedObjects = new LinkedList<TrackedRecognition>();

  private Matrix frameToCanvasMatrix;
  private int frameWidth;
  private int frameHeight;
  private int sensorOrientation;
  private TextToSpeech tts;



  public MultiBoxTracker(final Context context) {

    tts = new TextToSpeech(context, new TextToSpeech.OnInitListener(){
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
    });
  }

  public synchronized void setFrameConfiguration(
      final int width, final int height, final int sensorOrientation) {
    frameWidth = width;
    frameHeight = height;
    this.sensorOrientation = sensorOrientation;
  }

  public synchronized void drawDebug(final Canvas canvas) {
    final Paint textPaint = new Paint();
    textPaint.setColor(Color.WHITE);
    textPaint.setTextSize(60.0f);

    final Paint boxPaint = new Paint();
    boxPaint.setColor(Color.RED);
    boxPaint.setAlpha(200);
    boxPaint.setStyle(Style.STROKE);

    for (final Pair<Float, RectF> detection : screenRects) {
      final RectF rect = detection.second;
      canvas.drawRect(rect, boxPaint);
      canvas.drawText("" + detection.first, rect.left, rect.top, textPaint);

    }
  }

  public synchronized void trackResults(final List<Recognition> results, final long timestamp) {
    logger.i("Processing %d results from %d", results.size(), timestamp);
    processResults(results);
  }

  private Matrix getFrameToCanvasMatrix() {
    return frameToCanvasMatrix;
  }

  public synchronized void draw(final Canvas canvas) {
    //absolutely nothing to do here
  }

  private void processResults(final List<Recognition> results) {
    int count = results.size();
    int total = 0;
    //tts to tell the count
    tts.speak("Count "+count, TextToSpeech.QUEUE_FLUSH, null, null);
    //add delay here
    Log.d("Recognition Count", ""+count);
    if (count > 0){
      for (Recognition r: results){
        switch (r.getDetectedClass() / 2){
          case 0:
              total += 100;
              break;
          case 1:
              total += 200;
              break;
          case 2:
              total += 500;
              break;
          case 3:
              total += 50;
              break;
        }
      }
      Log.d("Recognition Amount", ""+total);
      //tts the output
      tts.speak("Amount "+total, TextToSpeech.QUEUE_ADD, null, null);

      //add delay here
    }
  }

  private static class TrackedRecognition {
    RectF location;
    float detectionConfidence;
    int color;
    String title;
  }
}

# Blind Eye
Banknote denomination detection application for the visually challenged.
Incorporates the following use-case-specific features into the standard TensorFlow-lite deployment template- 
1. Periodic audio feedback to guide the user.
2. Light intensity detection to automatically toggle the phone flashlight.
3. Multi-note detection and cumulative denomination output.

Note: A TensorFlow-lite model should be trained and placed in the model location (NoteRecognition/android/app/src/main/assets).
      The sample model for Indian banknotes (denominations- 50,100,200,500) is placed in the same location (labels file: coco.txt).

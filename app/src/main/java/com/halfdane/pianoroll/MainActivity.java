package com.halfdane.pianoroll;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.halfdane.pianoroll.util.OpenCvInit;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String  TAG = "ANOTHERCV";

    private CameraBridgeViewBase mOpenCvCameraView;

    SingleFrameHandler singleFrameHandler = new SingleFrameHandler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Log.d(TAG, "Creating and setting view");
        mOpenCvCameraView = new JavaCameraView(this, -1);
        setContentView(mOpenCvCameraView);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);


        Intent i = new Intent(getApplicationContext(), TestImage.class);
        startActivity(i);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCvInit.initOpenCV(this, new OpenCvInit.AfterInit() {
            @Override
            public void onAfterInit() {
                /* Now enable camera view to start receiving frames */
                mOpenCvCameraView.enableView();
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        Log.d(TAG, "onCameraViewStarted!");
    }

    public void onCameraViewStopped() {
        Log.d(TAG, "onCameraViewStopped!");
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.gray();
        return singleFrameHandler.handleFrame(rgba);
    }

}

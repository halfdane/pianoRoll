package com.halfdane.pianoroll;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.halfdane.pianoroll.util.OpenCvInit;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

public class LiveCameraFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String  TAG = LiveCameraFragment.class.getSimpleName();
    private CameraBridgeViewBase mOpenCvCameraView;
    private SingleFrameHandler singleFrameHandler = new SingleFrameHandler();
    private Camera camera;


    public LiveCameraFragment() {
        // Required empty public constructor
    }

    public static LiveCameraFragment newInstance() {
        LiveCameraFragment fragment = new LiveCameraFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_camera, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mOpenCvCameraView= (CameraBridgeViewBase) getView().findViewById(R.id.mainCameraView);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        OpenCvInit.initOpenCV(getActivity(), new OpenCvInit.AfterInit() {
            @Override
            public void onAfterInit() {
                mOpenCvCameraView.enableView();
            }
        });

        if ( getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) ) {
            camera = Camera.open();
            Camera.Parameters param = camera.getParameters();
            param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(param);
            camera.startPreview();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
        if (camera != null) {
            camera.stopPreview();
            camera.release();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            onStop();
        } else {
            onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onStop();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return singleFrameHandler.handleFrame(inputFrame.rgba());
    }
}

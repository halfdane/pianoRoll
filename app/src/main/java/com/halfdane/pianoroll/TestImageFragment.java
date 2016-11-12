package com.halfdane.pianoroll;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.halfdane.pianoroll.util.OpenCvInit;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;


public class TestImageFragment extends Fragment {

    private static final String ARG_TEST_IMAGE = "testImage";

    private int mTestImage;

    private SingleFrameHandler singleFrameHandler = new SingleFrameHandler();

    public TestImageFragment() {
        // Required empty public constructor
    }

    public static TestImageFragment newInstance(int testImage) {
        TestImageFragment fragment = new TestImageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TEST_IMAGE, testImage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTestImage = getArguments().getInt(ARG_TEST_IMAGE);
        }

        OpenCvInit.initOpenCV(getActivity(), new OpenCvInit.AfterInit() {
            @Override
            public void onAfterInit() {
                try {
                    Mat m = Mat.zeros(100, 400, CvType.CV_8UC3);

                    Mat img = Utils.loadResource(getContext(), mTestImage);
                    Imgproc.cvtColor(img, m, Imgproc.COLOR_RGB2BGRA);

                    displayIn(m, R.id.rawImage);

                    displayIn(singleFrameHandler.handleFrame(m), R.id.resultImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_image, container, false);
    }

    private void displayIn(Mat m, int imageViewId) {
        Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(m, bm);
        ImageView iv = (ImageView) getView().findViewById(imageViewId);
        iv.setImageBitmap(bm);
    }
}

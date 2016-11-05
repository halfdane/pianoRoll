package com.halfdane.pianoroll;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class TestImage extends AppCompatActivity {

    private static final String TAG = TestImage.class.getCanonicalName();
    SingleFrameHandler singleFrameHandler = new SingleFrameHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_image);

        try {
            helloworld();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void helloworld() throws IOException {
        // make a mat and draw something
        Mat m = Mat.zeros(100, 400, CvType.CV_8UC3);

        Mat img = Utils.loadResource(this, R.drawable.testimg);
        Imgproc.cvtColor(img, m, Imgproc.COLOR_RGB2BGRA);

        displayIn(m, R.id.imageView1);

        displayIn(singleFrameHandler.handleFrame(m), R.id.imageView2);
    }

    private void displayIn(Mat m, int imageViewId) {
        Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(m, bm);
        ImageView iv = (ImageView) findViewById(imageViewId);
        iv.setImageBitmap(bm);
    }

}

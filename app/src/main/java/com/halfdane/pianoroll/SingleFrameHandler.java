package com.halfdane.pianoroll;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.R.attr.lines;
import static org.opencv.imgproc.Imgproc.Canny;
import static org.opencv.imgproc.Imgproc.GaussianBlur;
import static org.opencv.imgproc.Imgproc.boundingRect;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.drawContours;
import static org.opencv.imgproc.Imgproc.equalizeHist;
import static org.opencv.imgproc.Imgproc.findContours;
import static org.opencv.imgproc.Imgproc.rectangle;

public class SingleFrameHandler {

    Random random = new Random();

    public Mat handleFrame(Mat inputFrame) {
        Mat output = inputFrame.clone();

        cvtColor(output, output, Imgproc.COLOR_BGR2GRAY);

        equalizeHist(output, output);
        GaussianBlur(output, output, new Size(7, 7), 0, 0);
        Canny(output, output, 100, 200);

        MatOfPoint corners = new MatOfPoint();
        Imgproc.goodFeaturesToTrack(output, corners, 30, 0.01, 100);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        findContours(output, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        output = inputFrame.clone();

        for( int i = 0; i< contours.size(); i++ ) {
            Scalar color = new Scalar(random.nextInt(255), random.nextInt(255), random.nextInt(255));
            drawContours(output, contours, i, color, 2, 8, hierarchy, 0, new Point());
        }
        //displayContours(output, contours);

        return output;
    }

    private void displayContours(Mat output, List<MatOfPoint> contours) {
        for (MatOfPoint contour : contours) {
            Rect rect = boundingRect(contour);
            rectangle(output, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255));
        }
    }
}

package com.halfdane.pianoroll.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
        GaussianBlur(output, output, new Size(5, 5), 0, 0);
        Canny(output, output, 100, 200);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        findContours(output, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        output = inputFrame.clone();

        //for( int i = 0; i< contours.size(); i++ ) {
        //    Scalar color = new Scalar(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        //    drawContours(output, contours, i, color, 5, 8, hierarchy, 0, new Point());
        //}

        ArrayList<Rect> boundingRects = new ArrayList<>();
        for (MatOfPoint contour : contours) {
            boundingRects.add(boundingRect(contour));
            Rect rect = boundingRect(contour);

            //show(output, rect);
        }

        Collections.sort(boundingRects, new Comparator<Rect>() {
            @Override
            public int compare(Rect rect1, Rect rect2) {
                return rect2.height - rect1.height;
            }
        });

        show(output, boundingRects.get(0));
        show(output, boundingRects.get(1));

        return output;
    }

    private void show(Mat output, Rect rect) {
        rectangle(output, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 5);
    }
}

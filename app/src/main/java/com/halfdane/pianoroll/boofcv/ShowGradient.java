package com.halfdane.pianoroll.boofcv;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.ddogleg.struct.FastQueue;

import java.util.List;

import boofcv.abst.feature.detect.line.DetectLineHoughPolar;
import boofcv.abst.filter.derivative.ImageGradient;
import boofcv.alg.feature.detect.line.LineImageOps;
import boofcv.android.VisualizeImageData;
import boofcv.android.gui.VideoImageProcessing;
import boofcv.factory.feature.detect.line.ConfigHoughPolar;
import boofcv.factory.feature.detect.line.FactoryDetectLineAlgs;
import boofcv.factory.filter.derivative.FactoryDerivative;
import boofcv.struct.image.GrayS16;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.ImageType;
import georegression.struct.line.LineParametric2D_F32;
import georegression.struct.line.LineSegment2D_F32;

public class ShowGradient extends VideoImageProcessing<GrayU8> {

    // adjusts edge threshold for identifying pixels belonging to a line
    private static final float edgeThreshold = 25;
    // adjust the maximum number of found lines in the image
    private static final int maxLines = 10;

    // Storage for the gradient
    private GrayS16 derivX = new GrayS16(1, 1);
    private GrayS16 derivY = new GrayS16(1, 1);

    // computes the image gradient
    private ImageGradient<GrayU8, GrayS16> gradient = FactoryDerivative.sobel(GrayU8.class, GrayS16.class);

    protected ShowGradient() {
        super(ImageType.single(GrayU8.class));
    }

    @Override
    protected void declareImages(int width, int height) {
        // You must call the super or else it will crash horribly
        super.declareImages(width, height);

        derivX.reshape(width, height);
        derivY.reshape(width, height);
    }


    FastQueue<LineSegment2D_F32> lines = new FastQueue<LineSegment2D_F32>(LineSegment2D_F32.class,true);

    @Override
    protected void process(GrayU8 gray, Bitmap output, byte[] storage) {
        DetectLineHoughPolar<GrayU8, GrayS16> detector = FactoryDetectLineAlgs.houghPolar(
                new ConfigHoughPolar(3, 30, 2, Math.PI / 180,edgeThreshold, maxLines), GrayU8.class, GrayS16.class);
//		DetectLineHoughFoot<T,D> detector = FactoryDetectLineAlgs.houghFoot(
//				new ConfigHoughFoot(3, 8, 5, edgeThreshold,maxLines), imageType, derivType);
//		DetectLineHoughFootSubimage<T,D> detector = FactoryDetectLineAlgs.houghFootSub(
//				new ConfigHoughFootSubimage(3, 8, 5, edgeThreshold,maxLines, 2, 2), imageType, derivType);


        List<LineParametric2D_F32> found = detector.detect(gray);

        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setColor(Color.CYAN);
        for( LineParametric2D_F32 p : found ) {
            LineSegment2D_F32 ls = LineImageOps.convert(p, gray.width,gray.height);

            canvas.drawLine(ls.a.getX(), ls.a.getY(), ls.b.getX(), ls.b.getY(), paint);
        }


//        gradient.process(gray, derivX, derivY);
//        VisualizeImageData.colorizeGradient(derivX, derivY, -1, output, storage);

    }
}

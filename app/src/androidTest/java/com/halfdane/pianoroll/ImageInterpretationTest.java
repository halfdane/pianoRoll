package com.halfdane.pianoroll;

import android.support.test.runner.AndroidJUnit4;

import com.halfdane.pianoroll.util.OpenCvInit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static com.halfdane.pianoroll.util.OpenCvInit.initOpenCV;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class ImageInterpretationTest {

    @Test
    public void shouldLoadImage() throws Exception {
        final AtomicBoolean hasRun = new AtomicBoolean(false);
        initOpenCV(getTargetContext(), new OpenCvInit.AfterInit() {
            @Override
            public void onAfterInit() {
                hasRun.set(true);
            }
        });

        Thread.sleep(100);
        assertTrue(hasRun.get());

        File testImgFile = new File("./test/java/com.halfdane.pianoroll/testimg.jpg");
        Mat m = Imgcodecs.imread("./test/java/com.halfdane.pianoroll/testimg.jpg");
    }
}

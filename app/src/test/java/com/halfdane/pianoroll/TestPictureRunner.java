package com.halfdane.pianoroll;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import static junit.framework.Assert.fail;
import static org.mockito.Matchers.anyString;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class TestPictureRunner {

    @Test
    public void displayPicture() {
        PowerMockito.mockStatic(Log.class);
        BDDMockito.given(Log.d(anyString(), anyString())).willReturn(3);

        if (!OpenCVLoader.initDebug()) {
            fail("Expected openCvLoader to start");
        } else {
            System.out.println("Success");
        }

        File testImgFile = new File("./test/java/com.halfdane.pianoroll/testimg.jpg");
        Mat m = Imgcodecs.imread("./test/java/com.halfdane.pianoroll/testimg.jpg");
    }
}

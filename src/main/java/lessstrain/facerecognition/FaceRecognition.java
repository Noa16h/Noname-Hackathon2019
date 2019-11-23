package lessstrain.facerecognition;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.bytedeco.javacv.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.indexer.*;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_imgproc.*;
import org.bytedeco.opencv.opencv_calib3d.*;
import org.bytedeco.opencv.opencv_objdetect.*;

import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_calib3d.*;
import static org.bytedeco.opencv.global.opencv_objdetect.*;


public class FaceRecognition {

    private final FrameGrabber grabber;
    private final OpenCVFrameConverter.ToMat converter;
    private final CascadeClassifier classifier;

    public FaceRecognition() throws IOException {
        classifier = new CascadeClassifier(loadClassifier());
        converter = new OpenCVFrameConverter.ToMat();

        grabber = FrameGrabber.createDefault(0);
        grabber.start();
    }

    public String loadClassifier() throws IOException {
        URL url = new URL("https://raw.githubusercontent.com/opencv/opencv/master/data/haarcascades/haarcascade_frontalface_alt.xml");
        File file = Loader.cacheResource(url);
        return file.getAbsolutePath();
    }

    public Frame grabFrame() throws FrameGrabber.Exception {
        return grabber.grab();
    }

    public Mat convertFrame(Frame frame) {
        return converter.convertToMat(frame);
    }

    public static void main(String[] main) throws IOException {
        FaceRecognition faceRecog = new FaceRecognition();

        Frame frame = faceRecog.grabFrame();
        Mat grabbedImage = faceRecog.convertFrame(frame);
        int height = grabbedImage.rows();
        int width = grabbedImage.cols();

        // Objects allocated with `new`, clone(), or a create*() factory method are automatically released
        // by the garbage collector, but may still be explicitly released by calling deallocate().
        // You shall NOT call cvReleaseImage(), cvReleaseMemStorage(), etc. on objects allocated this way.
        Mat grayImage = new Mat(height, width, CV_8UC1);
        Mat rotatedImage = grabbedImage.clone();

        // CanvasFrame is a JFrame containing a Canvas component, which is hardware accelerated.
        // It can also switch into full-screen mode when called with a screenNumber.
        // We should also specify the relative monitor/camera response for proper gamma correction.
        CanvasFrame canvasFrame = new CanvasFrame("Some Title", CanvasFrame.getDefaultGamma() / grabber.getGamma());

        // Let's create some random 3D rotation...
        Mat randomR = new Mat(3, 3, CV_64FC1),
                randomAxis = new Mat(3, 1, CV_64FC1);
        // We can easily and efficiently access the elements of matrices and images
        // through an Indexer object with the set of get() and put() methods.
        DoubleIndexer Ridx = randomR.createIndexer(),
                axisIdx = randomAxis.createIndexer();
        axisIdx.put(0, 0, 0, 0);
        Rodrigues(randomAxis, randomR);
        double f = (width + height) / 2.0;
        Ridx.put(0, 2, Ridx.get(0, 2) * f);
        Ridx.put(1, 2, Ridx.get(1, 2) * f);
        Ridx.put(2, 0, Ridx.get(2, 0) / f);
        Ridx.put(2, 1, Ridx.get(2, 1) / f);
        System.out.println(Ridx);

        // We can allocate native arrays using constructors taking an integer as argument.
        Point hatPoints = new Point(3);

        while (frame.isVisible() && (grabbedImage = converter.convert(grabber.grab())) != null) {
            // Let's try to detect some faces! but we need a grayscale image...
            cvtColor(grabbedImage, grayImage, CV_BGR2GRAY);
            RectVector faces = new RectVector();
            classifier.detectMultiScale(grayImage, faces);
            long total = faces.size();
            for (long i = 0; i < total; i++) {
                Rect r = faces.get(i);
                int x = r.x(), y = r.y(), w = r.width(), h = r.height();
                rectangle(grabbedImage, new Point(x, y), new Point(x + w, y + h), Scalar.RED, 1, CV_AA, 0);
            }

            warpPerspective(grabbedImage, rotatedImage, randomR, rotatedImage.size());

            Frame rotatedFrame = converter.convert(rotatedImage);
            canvasFrame.showImage(rotatedFrame);
        }
        canvasFrame.dispose();

    }

}

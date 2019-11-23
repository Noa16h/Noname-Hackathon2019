package lessstrain.facerecognition;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;


public class FaceRecognition {

    private final OpenCVFrameGrabber grabber;
    private final CascadeClassifier classifier;

    public FaceRecognition() throws FrameGrabber.Exception {
        grabber = new OpenCVFrameGrabber(0);
        grabber.start();
        grabber.setFrameRate(grabber.getFrameRate());

        classifier = new CascadeClassifier();
        classifier.load("face_cascade.xml");
    }

    public Frame grabFrame() throws FrameGrabber.Exception {
        return grabber.grab();
    }

    public void dispose() {
        try {
            grabber.stop();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] main) throws FrameGrabber.Exception {
        try {
            FaceRecognition face = new FaceRecognition();
            Frame frame = face.grabFrame();

            CanvasFrame canvasFrame = new CanvasFrame("Cam");
            canvasFrame.setCanvasSize(frame.imageWidth, frame.imageHeight);

            while (canvasFrame.isVisible()) {
                frame = face.grabFrame();
                Frame grayFrame = null;
                //Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);

                canvasFrame.showImage(frame);
            }
            canvasFrame.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

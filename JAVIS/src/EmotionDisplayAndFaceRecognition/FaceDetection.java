///////////////////////////////////////////////////////////////////
// Author: Taha Emara, Ayodele Owoyele
//
///////////////////////////////////////////////////////////////////
package EmotionDisplayAndFaceRecognition;

import Conversation.SessionManager;
import datastructures.MoodState;
import jarvis.ProjectJarvis;
import jarvis.inpustream.ClickEvent;
import jarvis.utilities.Utilities;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.Timer;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import outputstream.BlackBoard;
import outputstream.MainPanel;
import outputstream.SystemSounds;

/**
 *
 * @author Taha Emara and Ayodele Owoyele
 */
public class FaceDetection extends javax.swing.JFrame {
///

    public static CardLayout flipCards = new CardLayout();  //card layout for different screens
    public static Container contentPane; //just the content pane
    public static boolean SnapShotOver = true; //no snapshot is going on.

    public static boolean MeetSomeoneNew = false; //obtain new training data.
    public static boolean stopLookingAtMe = false;
    public int faceSaveCounter = 0, faceRecogCounter = 0; //numbers of faces to save
    public static boolean TakePicForRecognition = false; //identify person sitting in front of camera
    public static String LastRecognizedUser = ""; //last person identified by recognition algorithm

    public static void takeSnapShot(BufferedImage capture) {
        try {
            ImageIO.write(capture, "bmp", new File(new JFileChooser().getFileSystemView().getDefaultDirectory().toString().replace("Documents", "Pictures") + "\\RoboPhoto" + new Random().nextInt(5000) + ".bmp"));
            SystemSounds flash = new SystemSounds("flash");
            flash.playsound();
        } catch (IOException ex) {
        }
        //SnapShotOver = true; //snapshot is already taken
    }
    private DaemonThread myThread = null;
    int count = 0;
    VideoCapture webSource = null;
    Mat frame = new Mat();
    MatOfByte mem = new MatOfByte();
    CascadeClassifier faceDetector;
    MatOfRect faceDetections = new MatOfRect();
    public int absoluteFaceSize;
    private EmotionScreen emotionScreen;
    ClickEvent textInputListener; //click listener
  
    class DaemonThread implements Runnable {

        protected volatile boolean runnable = false;

        @Override
        public void run() {
            synchronized (this) {
                while (runnable) {
                    if (webSource.grab()) {
                        try {
                            webSource.retrieve(frame);

                            Graphics g = feed.getGraphics();
                            Mat grayframe = new Mat();

                            // convert the frame in gray scale
                            Imgproc.cvtColor(frame, grayframe, Imgproc.COLOR_BGR2GRAY);
                            // equalize the frame histogram to improve the result
                            Imgproc.equalizeHist(grayframe, grayframe);

                            // compute minimum face size (20% of the frame height)
                            if (absoluteFaceSize == 0) {
                                int height = frame.rows();
                                if (Math.round(height * 0.2f) > 0) {
                                    absoluteFaceSize = Math.round(height * 0.2f);
                                }
                            }

                            faceDetector.detectMultiScale(frame, faceDetections, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(
                                    absoluteFaceSize, absoluteFaceSize), new Size());
                            for (Rect rect : faceDetections.toArray()) {
                                // System.out.println("ttt");
                                Core.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                                        new Scalar(0, 255, 0), 6);

                                //meet someone new and the person has a name
                                if (MeetSomeoneNew == true && !MainPanel.LastFacialRecognitionUsername.isEmpty()) {
                                    Mat faceImage = frame.submat(rect);
                                    MatOfByte recogMem = new MatOfByte();
                                    Highgui.imencode(".bmp", faceImage, recogMem);
                                    Image savedImage = ImageIO.read(new ByteArrayInputStream(recogMem.toArray()));
                                    BufferedImage savedBufferImageResized = Utilities.ResizeAndReScaleAndGrayScaleImage((BufferedImage) savedImage);
                                    try {
                                        ImageIO.write(savedBufferImageResized, "png", new File((Utilities.getJarParentDir() + "assets\\faces\\" + MainPanel.LastFacialRecognitionUsername.replace(" ", "_") + String.valueOf(System.currentTimeMillis()) + ".png")));
                                    } catch (IOException ex) {
                                    }
                                    //Highgui.imwrite(new String(Utilities.getJarParentDir()+"assets\\faces\\" +MainPanel.LastFacialRecognitionUsername.replace(" ", "_")+String.valueOf(System.currentTimeMillis()) +".png"), faceImage);
                                    if (faceSaveCounter >= 10) {
                                        MeetSomeoneNew = false;
                                        faceSaveCounter = 0; //resetting counter back to zero
                                    }
                                    faceSaveCounter++; //six training picture required
                                }

                                //TakePicForRecognition will be timer controlled
                                if (TakePicForRecognition == true) {
                                    //provided the camera is not running at this time.

                                    Mat faceImage = frame.submat(rect);
                                    MatOfByte recogMem = new MatOfByte();
                                    Highgui.imencode(".bmp", faceImage, recogMem);
                                    Image savedImage = ImageIO.read(new ByteArrayInputStream(recogMem.toArray()));
                                    BufferedImage savedBufferImageResized = Utilities.ResizeAndReScaleAndGrayScaleImage((BufferedImage) savedImage);
                                    try {
                                        ImageIO.write(savedBufferImageResized, "png", new File(new String(Utilities.getJarParentDir() + "assets\\recog\\" + String.valueOf(faceRecogCounter) + ".png")));
                                    } catch (IOException ex) {
                                    }
                                    //Highgui.imwrite(new String(Utilities.getJarParentDir()+"assets\\faces\\" +MainPanel.LastFacialRecognitionUsername.replace(" ", "_")+String.valueOf(System.currentTimeMillis()) +".png"), faceImage);
                                    if (faceRecogCounter >= 3) {
                                        TakePicForRecognition = false;
                                        faceRecogCounter = 0; //resetting counter back to zero
                                    }
                                    faceRecogCounter++; //six training picture required

                                }

                            }

                            Highgui.imencode(".bmp", frame, mem);
                            Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
                            BufferedImage buff = (BufferedImage) im;
                            if (g.drawImage(buff, 0, 0, getWidth() - 20, getHeight() - 20, 0, 0, buff.getWidth() - 20, buff.getHeight() - 20, null)) {
                                if (runnable == false) {
                                    //System.out.println("Paused ..... ");
                                    this.wait();
                                }
                                if (SnapShotOver == false) {
                                    takeSnapShot(buff);
                                    SnapShotOver = true;
                                }
                            }

                        } catch (Exception ex) {
                            //System.out.println("Error");
                        }
                    }
                }
            }
        }
    }


    
    /**
     * Creates new form FaceDetection
     */
    public FaceDetection() {
        initComponents();
    }

   

    private void initComponents() {

        feed = new javax.swing.JPanel();
        mainCardJPanel = new javax.swing.JPanel();
        contentPane = mainCardJPanel;
        textInputListener = new ClickEvent(); //click handler.

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setOpacity(0.8F);

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        mainCardJPanel.setLayout(flipCards);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(feed);
        feed.setLayout(jPanel1Layout);

        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 680, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 350, Short.MAX_VALUE)
        );

        emotionScreen = new EmotionScreen();
        emotionScreen.setLocation(5, 5);
        emotionScreen.addMouseListener(textInputListener);
        mainCardJPanel.addMouseListener(textInputListener);
        addMouseListener(textInputListener);
        //feed.setBounds(10, 11, 680, 350);
        feed.setBounds(5, 5, getWidth() - 10, getHeight() - 10);

        mainCardJPanel.add(feed, "feed"); //adding panel to form. 
        mainCardJPanel.add(emotionScreen, "emotions"); //this is the main 
        getContentPane().add(mainCardJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 680, 350));

        flipCards.show(contentPane, "feed");
        
        pack();
        setLocation(0, BlackBoard.screenSize.height - getHeight() - 100);
        this.faceDetector = new CascadeClassifier(FaceDetection.class.getResource("haarcascade_frontalface_alt.xml").getFile().substring(6).replace("%20", " ").replace("/JAVIS.jar!/EmotionDisplayAndFaceRecognition", ""));
        this.absoluteFaceSize = 0;
        //feed.setBounds(5, 5, getWidth() - 5, getHeight() - 5);
        feed.setBackground(Color.black);
        //click listener for text input here.
        feed.addMouseListener(textInputListener);

        URL resource = BlackBoard.class.getResource("/jarvis/images/bigme.gif");
        this.setIconImage(new ImageIcon(resource).getImage());
        startFaceDetection(); //start detection face
        this.getContentPane().setBackground(Color.black);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setAlwaysOnTop(true);
       // setLocation(0, BlackBoard.screenSize.height - getHeight() - 200);

    }// </editor-fold>//GEN-END:initComponents
    public static Thread faceDectionThread;

    /**
     * stop face detections.
     */
    public void stopFaceDetection() {
        try {
            myThread.runnable = false;            // stop thread
            webSource.release();  // stop caturing fron 
        } catch (NullPointerException e) {
        }
    }

    public void startFaceDetection() {
        webSource = new VideoCapture(0); // video capture from default cam
        myThread = new DaemonThread(); //create object of thread class
        faceDectionThread = new Thread(myThread);
        faceDectionThread.setDaemon(true);
        myThread.runnable = true;
        faceDectionThread.start();                 //start thrad
    }
    
    public static void CheckFaceDb() {
        if (MeetSomeoneNew) return;
            Timer t = new Timer (10000, (ActionEvent e) -> {
                try {
                Image sampleRecog = ImageIO.read(new File((Utils.getJarParentDir()+"assets\\recog\\1.png")));
                BufferedImage smallImageCaptureFromCamera = (BufferedImage) sampleRecog;
                String fileNameForMatchedImage = ProjectJarvis.faceFinder.findMatchingImage(smallImageCaptureFromCamera);
                String nameOfPersonSeeingByCamera = fileNameForMatchedImage.replaceAll("[0-9]", "").replace("_", " ");
                MainPanel.LastFacialRecognitionUsername = nameOfPersonSeeingByCamera.replace("assets\\faces\\", "").replace(".png", "");

                //if it is a new user great them and start a new session technically.
                if (!LastRecognizedUser.equalsIgnoreCase(MainPanel.LastFacialRecognitionUsername)) {
                    BlackBoard.mainPanel.mouth.sayThisOnce("Hi " + MainPanel.LastFacialRecognitionUsername + ", " + Utilities.loadStrings("dialogs/firstIntro/backagain.txt")[new Random().nextInt(Utilities.loadStrings("dialogs/firstIntro/backagain.txt").length)]);
                    BlackBoard.emotions.setMood(MoodState.WAVING); //change mood to waving
                    SessionManager.session.getHumanJarvis().setHumanmood(MoodState.HAPPY);
                    LastRecognizedUser = MainPanel.LastFacialRecognitionUsername;
                    //setting current session start user name here.
                    SessionManager.session.getStartSessionHuman().setObjectName(MainPanel.LastFacialRecognitionUsername);
                }

            } catch (Exception fe) { }
            });
            if (ProjectJarvis.DisableFaceRecog == false){
            t.start();
            }
            
        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel feed;
    public static javax.swing.JPanel mainCardJPanel;
    // End of variables declaration//GEN-END:variables
}

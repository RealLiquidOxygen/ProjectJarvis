/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jarvis.utilities;

import EmotionDisplayAndFaceRecognition.Utils;
import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import outputstream.BlackBoard;
import outputstream.SystemSounds;

/**
 *
 * @author owoye001
 */
public class Utilities {

    private static final int IMG_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int IMG_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static boolean PNGORNOT = true;

    //instruction counter variables
    public static int Acount = 0;
    public static int Qcount = 0;
    public static int Ucount = 0;
    public static int Ccount = 0;
    public static int Wcount = 0;
    public static int Gcount = 0;
    public static int Mcount = 0;
    public static int Vcount = 0;
    public static int Icount = 0;
    public static int Kcount = 0;
    public static int Tcount = 0;

    static int EIGEN_FACE_WIDTH = 125;
    static int EIGEN_FACE_HEIGHT = 150;

    /**
     * resets the variables that determine what kind of information the input
     * screen should receive.
     */
    public static void resetTextCollectorScreen() {
        BlackBoard.mainPanel.textEntry = false;
        BlackBoard.mainPanel.numberEntry = false;
        BlackBoard.mainPanel.imageEntry = false;
    }

    /**
     *
     * @param temp
     */
    public synchronized void resizeImageAndReSave(File temp) {

        File file = temp;

        //System.out.println(file.exists());
        try {

            BufferedImage originalImage = ImageIO.read(file.getAbsoluteFile());
            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

            BufferedImage resizeImagePng = resizeImage(originalImage, type);
            ImageIO.write(resizeImagePng, PNGORNOT ? "png" : "jpg", file);

        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    /**
     * this method load line by line string from a text file
     *
     * @param textfileURL expecting the url for the text file
     * @return
     */
    public static String[] loadStrings(String textfileURL) {
        Path filePath = new File(textfileURL).toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList;
        try {
            stringList = Files.readAllLines(filePath.toAbsolutePath(), charset);
            return stringList.toArray(new String[]{});
        } catch (IOException ex) {
        }
        return null;
    }

    public static SystemSounds laugh; //sound initialization

    public static void playLaughFile() {

        new Thread() {
            @Override
            public void run() {
                laugh = new SystemSounds("laugh");
                laugh.playsound();
            }
        }.start();

    }

    /**
     * resize rescale and make image gray scale.
     *
     * @param originalImage
     * @return resized gray scale image
     */
    public static BufferedImage ResizeAndReScaleAndGrayScaleImage(BufferedImage originalImage) {

//                eigenFaceWidth = 125;
//                eigenFaceHeight = 150;
        originalImage = Utils.scaleToWindow(originalImage, EIGEN_FACE_WIDTH, EIGEN_FACE_HEIGHT);

        BufferedImage grayscale = Utils.convertToGrayscale(originalImage);

        return grayscale;
    }

    //resize image to screen size here.
    public static BufferedImage resizeImage(BufferedImage originalImage, int type) {
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();

        return resizedImage;
    }

    /**
     *
     * @param date date to be converted
     * @return date in string format
     */
    public static String getCurrentTimeStamp(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z"); // date
        String dateString = dateFormat.format(date); //format today date
        return dateString;
    }

    /**
     *
     * @return todays date
     */
    public Date getDateStamp() {
        return new Date();
    }

    public static boolean IsMorning() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        int m = calendar.get(Calendar.MINUTE);  // Values from 0 - 59
        int h = calendar.get(Calendar.HOUR_OF_DAY);    // Values from 0 - 23
        return h < 12;
    }

    public static boolean IsAfterNoon() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        int m = calendar.get(Calendar.MINUTE);  // Values from 0 - 59
        int h = calendar.get(Calendar.HOUR_OF_DAY);    // Values from 0 - 23
        return (h >= 12 && h <= 16);
    }

    public static boolean IsEvening() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        int m = calendar.get(Calendar.MINUTE);  // Values from 0 - 59
        int h = calendar.get(Calendar.HOUR_OF_DAY);    // Values from 0 - 23
        return (h > 15 && h < 19);
    }

    public static boolean IsNight() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        int m = calendar.get(Calendar.MINUTE);  // Values from 0 - 59
        int h = calendar.get(Calendar.HOUR_OF_DAY);    // Values from 0 - 23
        return (h >= 19);
    }

    public static int IsMorningAfterNoonOrNight() {
        if (IsMorning()) {
            return 1;
        }
        if (IsAfterNoon()) {
            return 2;
        }
        if (IsEvening()) {
            return 3;
        }
        if (IsNight()) {
            return 4;
        }

        return 0;
    }

    /**
     * reset all instruction counters to zero
     */
    public static void resetInstructionCounters() {
        Acount = 0;
        Qcount = 0;
        Ucount = 0;
        Ccount = 0;
        Wcount = 0;
        Gcount = 0;
        Mcount = 0;
        Vcount = 0;
        Icount = 0;
        Kcount = 0;
        Tcount = 0;
    }

    /**
     *
     * @param type information to type in
     */
    public static void TypeThisInRobot(String type) {
        int countOfDigits = type.length(); //length of stuff to type

        String[] digitToType = new String[countOfDigits]; //arrays of number for the robot to push

        try {
            for (int q = 0; q < countOfDigits; q++) {
                digitToType[q] = type.substring(q, q + 1); //obtaining digits
            }
        } catch (NumberFormatException e) {
            //in case of an error 
        }

        boolean LowerCaseCharacter = false; //

        try {
            Robot robot = new Robot(); //CREATING A ROBOT OBJECT
            robot.setAutoDelay(300);
            for (int t = 0; t < countOfDigits; t++) //robot pushes the keys using for and switch statements
            {

                if (digitToType[t].toLowerCase() == digitToType[t]) {
                    LowerCaseCharacter = true;
                    Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, !LowerCaseCharacter);
                } else {
                    LowerCaseCharacter = false;
                    Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, !LowerCaseCharacter);
                }
                
                switch (digitToType[t].toLowerCase()) {

                    case "a":
                        robot.keyPress(KeyEvent.VK_A);  //presses the keys
                        robot.keyRelease(KeyEvent.VK_A);  //releases the key
                        break;
                    case "b":
                        robot.keyPress(KeyEvent.VK_B);  //presses the keys
                        robot.keyRelease(KeyEvent.VK_B);  //releases the key
                        break;
                    case "c":
                        robot.keyPress(KeyEvent.VK_C);  //presses the keys
                        robot.keyRelease(KeyEvent.VK_C);  //releases the key
                        break;
                    case "d":
                        robot.keyPress(KeyEvent.VK_D);  //presses the keys
                        robot.keyRelease(KeyEvent.VK_D);  //releases the key
                        break;
                    case "e":
                        robot.keyPress(KeyEvent.VK_E);  //presses the keys
                        robot.keyRelease(KeyEvent.VK_E);  //releases the key
                        break;
                    case "f":
                        robot.keyPress(KeyEvent.VK_F);  //presses the keys
                        robot.keyRelease(KeyEvent.VK_F);  //releases the key
                        break;
                    case "g":
                        robot.keyPress(KeyEvent.VK_G); //presses the key 
                        robot.keyRelease(KeyEvent.VK_G); //releases the key 
                        break;
                    case "h":
                        robot.keyPress(KeyEvent.VK_H); //presses the key 
                        robot.keyRelease(KeyEvent.VK_H); //releases the key 
                        break;
                    case "i":
                        robot.keyPress(KeyEvent.VK_I); //presses the key 
                        robot.keyRelease(KeyEvent.VK_I); //releases the key
                        break;
                    case "j":
                        robot.keyPress(KeyEvent.VK_J); //same here 
                        robot.keyRelease(KeyEvent.VK_J); //same here 
                        break;
                    case "k":
                        robot.keyPress(KeyEvent.VK_K); //same here 
                        robot.keyRelease(KeyEvent.VK_K); //same here 
                        break;
                    case "l":
                        robot.keyPress(KeyEvent.VK_L); //same here 
                        robot.keyRelease(KeyEvent.VK_L); //same here 
                        break;
                    case "m":
                        robot.keyPress(KeyEvent.VK_M); //same here 
                        robot.keyRelease(KeyEvent.VK_M); //same here 
                        break;
                    case "n":
                        robot.keyPress(KeyEvent.VK_N); //same here 
                        robot.keyRelease(KeyEvent.VK_N); //same here 
                        break;
                    case "o":
                        robot.keyPress(KeyEvent.VK_O); //same here 
                        robot.keyRelease(KeyEvent.VK_O); //same here 
                        break;
                    case "p":
                        robot.keyPress(KeyEvent.VK_P); //same here 
                        robot.keyRelease(KeyEvent.VK_P); //same here 
                        break;
                    case "q":
                        robot.keyPress(KeyEvent.VK_Q); //same here 
                        robot.keyRelease(KeyEvent.VK_Q); //same here 
                        break;
                    case "u":
                        robot.keyPress(KeyEvent.VK_U); //same here 
                        robot.keyRelease(KeyEvent.VK_U); //same here 
                        break;
                    case "r":
                        robot.keyPress(KeyEvent.VK_R); //same here 
                        robot.keyRelease(KeyEvent.VK_R); //same here 
                        break;
                    case "s":
                        robot.keyPress(KeyEvent.VK_S); //same here 
                        robot.keyRelease(KeyEvent.VK_S); //same here 
                        break;
                    case "t":
                        robot.keyPress(KeyEvent.VK_T); //same here 
                        robot.keyRelease(KeyEvent.VK_T); //same here 
                        break;
                    case "v":
                        robot.keyPress(KeyEvent.VK_V); //same here 
                        robot.keyRelease(KeyEvent.VK_V); //same here 
                        break;
                    case "w":
                        robot.keyPress(KeyEvent.VK_W); //same here 
                        robot.keyRelease(KeyEvent.VK_W); //same here 
                        break;
                    case "x":
                        robot.keyPress(KeyEvent.VK_X); //same here 
                        robot.keyRelease(KeyEvent.VK_X); //same here 
                        break;
                    case "y":
                        robot.keyPress(KeyEvent.VK_Y); //same here 
                        robot.keyRelease(KeyEvent.VK_Y); //same here 
                        break;
                    case "z":
                        robot.keyPress(KeyEvent.VK_Z); //same here 
                        robot.keyRelease(KeyEvent.VK_Z); //same here 
                        break;
                    case "1":
                        robot.keyPress(KeyEvent.VK_1);  //presses the keys
                        robot.keyRelease(KeyEvent.VK_1);  //releases the key
                        break;
                    case "2":
                        robot.keyPress(KeyEvent.VK_2); //presses the key
                        robot.keyRelease(KeyEvent.VK_2); //releases the key
                        break;
                    case "3":
                        robot.keyPress(KeyEvent.VK_3); //presses the key
                        robot.keyRelease(KeyEvent.VK_3); //releases the key 
                        break;
                    case "4":
                        robot.keyPress(KeyEvent.VK_4); //presses the key 
                        robot.keyRelease(KeyEvent.VK_4); //releases the key 
                        break;
                    case "5":
                        robot.keyPress(KeyEvent.VK_5); //presses the key 
                        robot.keyRelease(KeyEvent.VK_5); //releases the key 
                        break;
                    case "6":
                        robot.keyPress(KeyEvent.VK_6); //presses the key 
                        robot.keyRelease(KeyEvent.VK_6); //releases the key 
                        break;
                    case "7":
                        robot.keyPress(KeyEvent.VK_7); //presses the key 
                        robot.keyRelease(KeyEvent.VK_7); //releases the key 
                        break;
                    case "8":
                        robot.keyPress(KeyEvent.VK_8); //presses the key 
                        robot.keyRelease(KeyEvent.VK_8); //releases the key 
                        break;
                    case "9":
                        robot.keyPress(KeyEvent.VK_9); //presses the key 
                        robot.keyRelease(KeyEvent.VK_9); //releases the key
                        break;
                    case "0":
                        robot.keyPress(KeyEvent.VK_0); //presses the key 
                        robot.keyRelease(KeyEvent.VK_0); //releases the key
                        break;
                    case "!":
                        robot.keyPress(KeyEvent.VK_SHIFT); //presses the key 
                        robot.keyPress(KeyEvent.VK_1); //releases the key
                        robot.keyRelease(KeyEvent.VK_1); //presses the key 
                        robot.keyRelease(KeyEvent.VK_SHIFT); //releases the key 
                        break;
                    case "@":
                        robot.keyPress(KeyEvent.VK_SHIFT); //presses the key 
                        robot.keyPress(KeyEvent.VK_2); //releases the key
                        robot.keyRelease(KeyEvent.VK_2); //presses the key 
                        robot.keyRelease(KeyEvent.VK_SHIFT); //releases the key  
                        break;
                    case "#":
                        robot.keyPress(KeyEvent.VK_SHIFT); //presses the key 
                        robot.keyPress(KeyEvent.VK_3); //releases the key
                        robot.keyRelease(KeyEvent.VK_3); //presses the key 
                        robot.keyRelease(KeyEvent.VK_SHIFT); //releases the key
                        break;
                    case "$":
                        robot.keyPress(KeyEvent.VK_SHIFT); //presses the key 
                        robot.keyPress(KeyEvent.VK_4); //releases the key
                        robot.keyRelease(KeyEvent.VK_4); //presses the key 
                        robot.keyRelease(KeyEvent.VK_SHIFT); //releases the key
                        break;
                    case "%":
                        robot.keyPress(KeyEvent.VK_SHIFT); //presses the key 
                        robot.keyPress(KeyEvent.VK_5); //releases the key
                        robot.keyRelease(KeyEvent.VK_5); //presses the key 
                        robot.keyRelease(KeyEvent.VK_SHIFT); //releases the key 
                        break;
                    case "^":
                        robot.keyPress(KeyEvent.VK_SHIFT); //presses the key 
                        robot.keyPress(KeyEvent.VK_6); //releases the key
                        robot.keyRelease(KeyEvent.VK_6); //presses the key 
                        robot.keyRelease(KeyEvent.VK_SHIFT); //releases the key 
                        break;
                    case "&":
                        robot.keyPress(KeyEvent.VK_SHIFT); //presses the key 
                        robot.keyPress(KeyEvent.VK_7); //releases the key
                        robot.keyRelease(KeyEvent.VK_7); //presses the key 
                        robot.keyRelease(KeyEvent.VK_SHIFT); //releases the key  
                        break;
                    case "*":
                        robot.keyPress(KeyEvent.VK_SHIFT); //presses the key 
                        robot.keyPress(KeyEvent.VK_8); //releases the key
                        robot.keyRelease(KeyEvent.VK_8); //presses the key 
                        robot.keyRelease(KeyEvent.VK_SHIFT); //releases the key 
                        break;
                    case "(":
                        robot.keyPress(KeyEvent.VK_LEFT_PARENTHESIS); //presses the key 
                        robot.keyRelease(KeyEvent.VK_LEFT_PARENTHESIS); //releases the key 
                        break;
                    case ")":
                        robot.keyPress(KeyEvent.VK_RIGHT_PARENTHESIS); //presses the key 
                        robot.keyRelease(KeyEvent.VK_RIGHT_PARENTHESIS); //releases the key
                        break;
                    case "-":
                        robot.keyPress(KeyEvent.VK_MINUS); //same here 
                        robot.keyRelease(KeyEvent.VK_MINUS); //same here 
                        break;
                    case "_":
                        robot.keyPress(KeyEvent.VK_UNDERSCORE); //same here 
                        robot.keyRelease(KeyEvent.VK_UNDERSCORE); //same here 
                        break;

                    case "+":
                        robot.keyPress(KeyEvent.VK_PLUS); //presses the key 
                        robot.keyRelease(KeyEvent.VK_PLUS); //releases the key 
                        break;
                    case "=":
                        robot.keyPress(KeyEvent.VK_EQUALS); //presses the key 
                        robot.keyRelease(KeyEvent.VK_EQUALS); //releases the key
                        break;
                    case "{":
                        robot.keyPress(KeyEvent.VK_SHIFT); //presses the key 
                        robot.keyPress(KeyEvent.VK_OPEN_BRACKET); //same here 
                        robot.keyRelease(KeyEvent.VK_OPEN_BRACKET); //same here 
                        robot.keyRelease(KeyEvent.VK_SHIFT); //presses the key 
                        break;
                    case "}":
                        robot.keyPress(KeyEvent.VK_SHIFT); //presses the key 
                        robot.keyPress(KeyEvent.VK_CLOSE_BRACKET); //same here 
                        robot.keyRelease(KeyEvent.VK_CLOSE_BRACKET); //same here 
                        robot.keyRelease(KeyEvent.VK_SHIFT); //presses the key 
                        break;

                    case "[":
                        robot.keyPress(KeyEvent.VK_OPEN_BRACKET); //same here 
                        robot.keyRelease(KeyEvent.VK_OPEN_BRACKET); //same here 
                        break;
                    case "]":
                        robot.keyPress(KeyEvent.VK_CLOSE_BRACKET); //same here 
                        robot.keyRelease(KeyEvent.VK_CLOSE_BRACKET); //same here 
                        break;
                    case ";":
                        robot.keyPress(KeyEvent.VK_SEMICOLON); //same here 
                        robot.keyRelease(KeyEvent.VK_SEMICOLON); //same here 
                        break;
                    case ":":
                        robot.keyPress(KeyEvent.VK_COLON); //same here 
                        robot.keyRelease(KeyEvent.VK_COLON); //same here 
                        break;
                    case "\"":
                        robot.keyPress(KeyEvent.VK_QUOTEDBL); //same here 
                        robot.keyRelease(KeyEvent.VK_QUOTEDBL); //same here 
                        break;
                    case "'":
                        robot.keyPress(KeyEvent.VK_QUOTE); //same here 
                        robot.keyRelease(KeyEvent.VK_QUOTE); //same here 
                        break;

                    case "<":
                        robot.keyPress(KeyEvent.VK_LESS); //same here 
                        robot.keyRelease(KeyEvent.VK_LESS); //same here 
                        break;
                    case ">":
                        robot.keyPress(KeyEvent.VK_GREATER); //same here 
                        robot.keyRelease(KeyEvent.VK_GREATER); //same here 
                        break;
                    case ",":
                        robot.keyPress(KeyEvent.VK_COMMA); //same here 
                        robot.keyRelease(KeyEvent.VK_COMMA); //same here 
                        break;
                    case ".":
                        robot.keyPress(KeyEvent.VK_PERIOD); //same here 
                        robot.keyRelease(KeyEvent.VK_PERIOD); //same here 
                        break;
                    case "/":
                        robot.keyPress(KeyEvent.VK_SLASH); //same here 
                        robot.keyRelease(KeyEvent.VK_SLASH); //same here 
                        break;
                    case "?":
                        robot.keyPress(KeyEvent.VK_SHIFT); //same here 
                        robot.keyPress(KeyEvent.VK_SLASH); //same here 
                        robot.keyRelease(KeyEvent.VK_SLASH); //same here 
                        robot.keyRelease(KeyEvent.VK_SHIFT); //same here 
                        break;
                    case "\\":
                        robot.keyPress(KeyEvent.VK_BACK_SLASH); //same here 
                        robot.keyRelease(KeyEvent.VK_BACK_SLASH); //same here 
                        break;
                    case "|":
                        robot.keyPress(KeyEvent.VK_SHIFT); //same here 
                        robot.keyPress(KeyEvent.VK_BACK_SLASH); //same here 
                        robot.keyRelease(KeyEvent.VK_BACK_SLASH); //same here 
                        robot.keyRelease(KeyEvent.VK_SHIFT); //same here 
                        break;
                    case " ":
                        robot.keyPress(KeyEvent.VK_SPACE); //same here 
                        robot.keyRelease(KeyEvent.VK_SPACE); //same here 
                        break;
                }
            } //end of number press

            //PRESS THE ENTER BUTTON - hehehe
            robot.keyPress(KeyEvent.VK_ENTER); //presses the enter key 
            robot.keyRelease(KeyEvent.VK_ENTER); //releases the enter key 

        } catch (AWTException e) {
            e.printStackTrace();
        }

    }

    static int counter = 0;

    /**
     * count the number of command and prepare for session
     *
     * @param numberOfLines total number code lines
     * @param regularLinesStringArray arrays of text lines.
     * @return the number of questions.
     */
    public static int countNumberOfActions(int numberOfLines, String[] regularLinesStringArray) {
        /*
        A stands for excitment
        Q stands for question
        U stands for user answer
        C: computer response
        W stand for wait
        G: stand for carcarousel
        M: music
        V: Video
        I: mouse movement
        K: keyboard typing
        T: terminate
        B: find a common topic
         */
        while (counter < numberOfLines) {
            switch (regularLinesStringArray[counter].substring(0, 1)) {
                case "A": //stands for excitment
                    Utilities.Acount++;
                    break;
                case "Q": //stands for question
                case "B": //stands for find common topic
                    Utilities.Qcount++;
                    break;
                case "U": //stands for user answer
                    Utilities.Ucount++;
                    break;
                case "C": //computer response
                    Utilities.Ccount++;
                    break;
                case "W": //stand for wait for some time 
                    Utilities.Wcount++;
                    break;
                case "G": //stand for carousel seletion 
                    Utilities.Gcount++;
                    break;
                case "M": //music feed 
                    Utilities.Mcount++;
                    break;
                case "V": //video feed 
                    Utilities.Vcount++;
                    break;
                case "I": //input from other devices
                    Utilities.Icount++;
                    break;
                case "T": //terminate
                    Utilities.Tcount++;
                    break;
                case "K": //keyboard typing
                    Utilities.Kcount++;
                    break;
            }
            counter++;
        }
        counter = 0; //resetting counter
        return Utilities.Qcount;
    }

    public static String getJarParentDir() {
        File file = null;

        try {
            file = new File(Utilities.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return file.getParent() + "\\";
    }

}

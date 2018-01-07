/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jarvis;

import Conversation.SessionManager;
import EmotionDisplayAndFaceRecognition.FaceFinder;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import googlespeechapi.GoogleSpeechApi;
import jarvis.inpustream.TextEntry;
import jarvis.utilities.Utilities;
import java.awt.TrayIcon;
import splash.ProgressPanelOverlay;
import outputstream.BlackBoard;
import outputstream.Preferences;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.opencv.core.Core;
import outputstream.DisplayTrayIcon;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author owoye001
 */
public class ProjectJarvis implements NativeKeyListener {

    public static Preferences settings = new Preferences(); //setting load.
    public static String BUILDFOLDER = "Build1"; //build folder for now
    public static final String VERSIONSTRING = "2.0";
    public static JFrame captureScreen; //capture screen picture
    public static Utilities utilities; //class to handle program utilities. 
    public static TextEntry txtEntry; //to record user texts
    public static FaceFinder faceFinder; //face recognition
    public static boolean DisableFaceRecog; //disable face recognition.
    public static String MEMES_LOCATION = "assets\\backstory\\"+ProjectJarvis.BUILDFOLDER+"\\memes\\"; //location of the memes photo
    public static int NUMBER_OF_MEMES; //there are 30 files in the memes folder 
    public static int TIME_SET_FOR_RANDOM_MEMES; //10 minutes//TIME IN MILLISECONDS
    public static boolean SMSMessage;
    public static boolean LockedScreen = false; //screen is locked
    public static String MYACCOUNTPASSWORD;
   
    
    //interstellar variables
    public static int HumourLevel = 100; //remove the 10 later and adjust the preferences settings. 
    public static String WEATHERIDENTIFIER; //this identifies the location
    public static String WEATHERLOCATION;
    public static boolean DEBUGMODE;
    public static String SALUTATION;
    public static boolean speechEngineEnabledByInternet = true; //true by default
    public static boolean batteryStatusUpdate;
    public static String SPEECHENGINE_VOICE;
    public static String SPEECHENGINE_PASSWORD;
    public static String SPEECHENGINE_URL;
    public static String SPEECHENGINE_USERNAME;
    public static int FIRST_FIVE_NEWS;
    public static String MYPHONENUMBER;
    public static String QUERYURLFORNEWMESSAGE;
    public static String BASEWEATHERURLDONOTCHANGE;
    public static String SendSMSMessageLink;
   
    


    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        // System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        //if key pressed is home button, give an actual talk window
         if (e.getKeyCode() == NativeKeyEvent.VC_HOME) {
             try {
                 
                if (!BlackBoard.mainPanel.SystemReadyStatus) {
                    DisplayTrayIcon.trayIcon.displayMessage("Project Jarvis", "System is not ready!", TrayIcon.MessageType.INFO);
                    return;
                }
                 
                 
                 GoogleSpeechApi gsa = new GoogleSpeechApi();
                 BlackBoard.mainPanel.textEntry = true;
                 BlackBoard.mainPanel.StringEntry = gsa.getResult().toLowerCase();
                 SessionManager.mainListenerTextEars.ProcessCommand();
             } catch (Exception je) {
                 //JOptionPane.showMessageDialog(null, "Speech Engine error! Sorry for the inconvenience", "Jarvis", JOptionPane.ERROR_MESSAGE);
             }
         }
        //if key pressed is esc, give me a talk windows
        if (e.getKeyCode() == NativeKeyEvent.VC_BACKQUOTE) {

            try {

                if (!BlackBoard.mainPanel.SystemReadyStatus) {
                    DisplayTrayIcon.trayIcon.displayMessage("Project Jarvis", "System is not ready!", TrayIcon.MessageType.INFO);
                    return;
                }

                Thread newActivity = new Thread() {
                    @Override
                    public void run() {
                        if (ProjectJarvis.txtEntry != null) ProjectJarvis.txtEntry.setVisible(false); //kill existing thread
                        ProjectJarvis.txtEntry = new TextEntry();
                        ProjectJarvis.txtEntry.setVisible(true);
                        SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                        BlackBoard.mainPanel.textEntry = true; //text entry
                        BlackBoard.mainPanel.sessionManager.collectInputValues();
                    }
                };
                newActivity.start();
            } catch (Exception eg) {
                DisplayTrayIcon.trayIcon.displayMessage("Project Jarvis", "Artificial Intelligence System Failure", TrayIcon.MessageType.ERROR);
                try {
                    BlackBoard.exitRoutine();
                    System.exit(0);
                } catch (Exception ef) {
                }
            }

        }

//        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
//            try {
//                GlobalScreen.unregisterNativeHook();
//            } catch (NativeHookException ex) {
//                Logger.getLogger(ProjectJarvis.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        //System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        // System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
    }

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); //picture capture library. 
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new NativeDiscovery().discover();
        NativeInterface.open();
         //disable all console output
        // Get the logger for "org.jnativehook" and set the level to off.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        // Don't forget to disable the parent handlers.
        logger.setUseParentHandlers(false);
        
        //keyboard shortcuts here
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(0);
        }
//        Thread threadCheckLockedState = new Thread() {
//            public void run(){
//                SystemLockState systemLockState = new SystemLockState();
//            }
//        };
        
        
        //Status for all variable - DEBUG USE
        if (DEBUGMODE){
        System.out.println ("INFO: DEBUG MODE - " + (DEBUGMODE ? "Enabled" : "Disabled"));
        System.out.println ("INFO: CURRENT BUILD - " + BUILDFOLDER);
        System.out.println ("INFO: CURRENT VERSION - " + VERSIONSTRING);
        System.out.println ("INFO: NUMBER OF MEMES - " + NUMBER_OF_MEMES);
        System.out.println ("INFO: MEMES FREQUENCY - " + TIME_SET_FOR_RANDOM_MEMES / 60 / 1000 + " minutes");
        System.out.println ("INFO: SMSMESSAGE - " + (SMSMessage ? "Enabled" : "Disabled"));
        System.out.println ("INFO: SAY BATTERY LEFT/FEELINGS - " + (batteryStatusUpdate ? "Enabled" : "Disabled"));
        System.out.println ("INFO: SPEECH SOUND - " + (ProjectJarvis.settings.SpeechSoundActive ? "Enabled" : "Disabled"));
        System.out.println ("INFO: ENVIRONMENT SOUND - " + (ProjectJarvis.settings.RegularSoundActive ? "Enabled" : "Disabled"));
        System.out.println ("INFO: FACIAL RECOGNITION - "  + (!ProjectJarvis.DisableFaceRecog ? "Enabled" : "Disabled"));
        System.out.println ("INFO: WEATHER LOCATION - " + WEATHERLOCATION);
        System.out.println ("INFO: WEATHER LOCATION CODE - " + WEATHERIDENTIFIER);
        System.out.println ("INFO: FORMAL GREETINGS  - " + SALUTATION);
        
        } else {
            System.out.println ("INFO: DEBUG MODE DISABLED!");
            System.out.println ("INFO: TO ENABLED, SET DebugMode PROPERTY in config file to true");
        }
        
        //threadCheckLockedState.start();

        GlobalScreen.addNativeKeyListener(new ProjectJarvis()); 

           SwingUtilities.invokeLater(() -> {
            txtEntry = new TextEntry();

            faceFinder = new FaceFinder();

            DisplayTrayIcon displayTrayIcon = new DisplayTrayIcon();

            //put thread to sleep to give splash more time
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
            }

            //array list declaration for splash overlay and module load
            ArrayList loadListArrayList = new ArrayList<>();
            ArrayList moduleLoadListArrayList = new ArrayList<>();

            //preferences load
            
            utilities = new Utilities(); //picture resize tool load.

            loadUPTestArray(loadListArrayList);

            loadUpProgressOverlayScreenArray(moduleLoadListArrayList);

            //load up progress screen
            java.awt.EventQueue.invokeLater(() -> {
                captureScreen = new ProgressPanelOverlay(moduleLoadListArrayList, loadListArrayList);
            });

            
            //put thread to sleep here 16000 before card swaps to main window
            Timer loadMainProgram = new Timer(18000, (ActionEvent e) -> {

                stopFlashActivity();

                BlackBoard.mainCards.show(BlackBoard.mainProgramComponentHolder, "main"); //Selecting the card to be shown

                BlackBoard.mainPanel.startActivityOnCard();
            });

            //timer only runs once
            loadMainProgram.setRepeats(false);
            loadMainProgram.start();
        });
        
      
        NativeInterface.runEventPump();
        // don't forget to properly close native components
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NativeInterface.close();
        }));

    }

    /**
     * load up progress overlay screen. The big me window
     */
    private static void loadUpProgressOverlayScreenArray(ArrayList moduleLoadListArrayList) {
        //splash over load here
        moduleLoadListArrayList.add("<h1>PROJECT JARVIS</h1>");
        moduleLoadListArrayList.add("<h2>LOADING CAMERA PROGRAM MODULES...</h2>");
        moduleLoadListArrayList.add("<h2>LOADING MATHS MODULES...</h2>");
        moduleLoadListArrayList.add("<h2>LOADING JOKES MODULES...</h2>");
        moduleLoadListArrayList.add("<h2>LOADING SCRIPT READER...</h2>");
        moduleLoadListArrayList.add("<h2>LOADING SEARCH MODULES...</h2>");
        moduleLoadListArrayList.add("<h2>LOADING EMOTION MODULES...</h2>");
        moduleLoadListArrayList.add("<h2>LOADING NETFLIX MODULES...</h2>");
        moduleLoadListArrayList.add("<h2>LOADING MEMES MODULES...</h2>");
        moduleLoadListArrayList.add("<h2>LOADING SPEECH MODULES...</h2>");
        moduleLoadListArrayList.add("<h2>LOADING REMINDERS MODULES...</h2>");
        moduleLoadListArrayList.add("<h2>LOADING LITTLE ME MODULES...</h2>");
        moduleLoadListArrayList.add("<h2>LOADING SMS MESSAGING MODULES...</h2>");
        moduleLoadListArrayList.add("<h2>LOADING DATABASE MODULES...</h2>");
         moduleLoadListArrayList.add("<h2></h2>");
        moduleLoadListArrayList.add("<h2>BIG-ME SOFTWARES</h2>");
        moduleLoadListArrayList.add("<h2>VERSION: "+VERSIONSTRING+"</h2>");
    }

    /**
     *
     * @param loadListArrayList item to scroll at the bag. This can be actual
     * data.
     */
    private static void loadUPTestArray(ArrayList loadListArrayList) {
        for (int i = 0; i < 500; i++) {

            loadListArrayList.add("<h3>        Loading emotion recognition models           |         " + new Date().toString() + "                 |         Loading modules " + new Random(10000000).nextInt() + "</h3>");
            loadListArrayList.add("<h3>        Loading voice recognition modules            |         " + new Date().toString() + "                 |         Loading modules " + new Random(10000000).nextInt() + "</h3>");
            loadListArrayList.add("<h3>        Loading higher neural functions              |         " + new Date().toString() + "                 |         Loading modules " + new Random(10000000).nextInt() + "</h3>");

        }
    }

    /**
     * stop the activity of the progress panel overlay and the progress panel
     */
    private static void stopFlashActivity() {
        //close the splash screen
        ProjectJarvis.captureScreen.setVisible(false);
        //stop the progresspanel timer
        BlackBoard.progressPanel.loadHtmlNicely.stop();
        BlackBoard.progressPanel.programload.currentObject.stop();

    }

}

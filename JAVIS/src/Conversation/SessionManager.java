/*
 * Copyright (C) 2016 owoye001
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package Conversation;

import EmotionDisplayAndFaceRecognition.FaceDetection;
import ErrorMessages.ErrorMessages;
import datastructures.MoodState;
import jarvis.ProjectJarvis;
import jarvis.inpustream.MainListener;
import jarvis.utilities.ScriptReader;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.util.Random;
import javax.swing.Timer;
import outputstream.BlackBoard;
import static outputstream.DisplayTrayIcon.trayIcon;
import outputstream.MainPanel;

/**
 *
 * @author owoye001
 */
public class SessionManager {

    /**
     * main constructor.
     */
    //JARVIS EARS DECLARATION
    //public static Ears mainListenerTextEars; //FOR SPEECH 
    public static MainListener mainListenerTextEars = new MainListener(); //text listener
    
    public static Session session = new Session();

    /**
     * change mood to null in three seconds
     */
    public static void ShowCameraInTime() {
        Timer showCamera = new Timer(5000, (ActionEvent e) -> { //5000
            BlackBoard.emotions.setMood(null);
        });
        showCamera.setRepeats(false);
        showCamera.start();
    }

    /**
     * change emotional expression back to happy
     */
    public static void ChangeMoodBackToHappy() {
        Timer showCamera = new Timer(6000, (ActionEvent e) -> {
            BlackBoard.emotions.setMood(MoodState.HAPPY);
        });
        showCamera.setRepeats(false);
        showCamera.start();
    }
    
   
    public Regular regular = null;

    public SessionManager() {

        InitiateBasicConveration();

    }

    /**
     * initiates the base conversation Overall program logics
     */
    private void InitiateBasicConveration() {
        Thread sessionManager = new Thread() {
            @Override
            public void run() {
                
                //set AI name
                SessionManager.session.getHumanJarvis().setObjectName(mainListenerTextEars.AINAME != null ? mainListenerTextEars.AINAME : "Jarvis");

                FaceDetection.TakePicForRecognition = true; //identify user, will reset variable

                //BASED ON PERSONALITY SELECTION, A SCRIPT WILL LOAD 
                
                //TO START CONVERSATION INITIALIZER.
                
                //load ai from a particular location. 
                
                
                 if (mainListenerTextEars.checkInternetConnectivity()) {
                     BlackBoard.mainPanel.mouth.sayThisOnce("We are online and ready " + ProjectJarvis.SALUTATION + "!" );
                     try {Thread.sleep(4000);}catch (Exception e) {}
                 } else { //speeech engine needs internet connection.
                    BlackBoard.mainPanel.mouth.printToScreen("It does not appear that we have internet connection at the moment");
                    BlackBoard.mainPanel.mouth.printToScreen("Speech Engine Disabled!");
                    ProjectJarvis.speechEngineEnabledByInternet = false; //speech engine disabled
                }

                trayIcon.displayMessage("I am now up and running " + ProjectJarvis.SALUTATION, "At your service", TrayIcon.MessageType.INFO);

//                String[] firstLineOptions = Utilities.loadStrings("dialogs/firstIntro/firstIntro.txt");
//                
//                BlackBoard.mainPanel.mouth.sayThisOnce(firstLineOptions[ new Random().nextInt(firstLineOptions.length)]);
//                
                 //System is official ready at this time for general input
                BlackBoard.mainPanel.SystemReadyStatus = true;
                
                MainListener.scriptReader = 
                        
                        new ScriptReader("assets/backstory/"+ProjectJarvis.BUILDFOLDER+"/mainAILogic/script.txt",
                        
                        "assets/backstory/"+ProjectJarvis.BUILDFOLDER+"/mainAILogic/commonDiscussionTopic.txt");
                
                MainListener.scriptReader.start();

            }

        };
        //sessionManager.setDaemon(true);
        sessionManager.start();
    }

    /**
     * collect input values receives no input stall main thread to receive input
     * joins calling thread
     */
    public void collectInputValues() {
        WaitForInputAndProcessIt();
        waitToJoin();
    }

    /**
     * wait for input collector thread to join main thread
     *
     */
    void waitToJoin() {
        try {
            input.join();
        } catch (InterruptedException e) {
        }
    }

   

    public static Thread input;

    /**
     * This thread waits for the input and process it
     */
    public static void WaitForInputAndProcessIt() {
        //collecting users input here on a new thread.
       
        input = new Thread() {
            //wait until it is not visible.
            @Override
            public void run() {                
                do {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                    }
                    //System.out.println ("I was here.");
                    //no longer visible
                    if (!ProjectJarvis.txtEntry.isVisible()) {
                        //displayInfo.displayH1(ProjectJarvis.txtEntry.textEntered); do not display

                        if (BlackBoard.mainPanel.nameEntry) {
                             MainPanel.LastFacialRecognitionUsername = ProjectJarvis.txtEntry.textEntered.toLowerCase();
                            if (MainListener.NameCollectionForRecognition){
                                 FaceDetection.MeetSomeoneNew = true; //identify user, will reset variable
                            }
                            
                            //if face recognition is disabled.
                            if (ProjectJarvis.DisableFaceRecog == false){
                            //BlackBoard.mainPanel.mouth.sayThisOnce("Hello " +  MainPanel.LastFacialRecognitionUsername + ", It is nice to meet you!");
                            }                            
                            //setting current session start user name here.
                            session.getStartSessionHuman().setObjectName(MainPanel.LastFacialRecognitionUsername);
                            BlackBoard.mainPanel.nameEntry = false;
                        } 
                        
                        if (BlackBoard.mainPanel.textEntry) { //for collecting text inputs

                            if (ProjectJarvis.txtEntry.textEntered.isEmpty()) {
                                //no input was entered.
                                //BlackBoard.mainPanel.mouth.sayThisOnce("You did not enter anything, " +  MainPanel.LastFacialRecognitionUsername);
                            } else {
                                BlackBoard.mainPanel.StringEntry = ProjectJarvis.txtEntry.textEntered;
                                mainListenerTextEars.ProcessCommand(); //process the command

                            }
                            BlackBoard.mainPanel.textEntry = false; //disabling text entry collection 
                        } 
                        
                        if (BlackBoard.mainPanel.numberEntry) { //number entry

                            if (ProjectJarvis.txtEntry.textEntered.isEmpty()) {
                                //no input was entered.
                                BlackBoard.mainPanel.mouth.sayThisOnce("You did not enter anything, I was expecting a number" +  MainPanel.LastFacialRecognitionUsername);
                            } else { //user has entered something
                                try {
                                    BlackBoard.mainPanel.NumberEntry = Integer.parseInt(ProjectJarvis.txtEntry.textEntered);
                                    //JOptionPane.showMessageDialog(null, String.valueOf(BlackBoard.mainPanel.NumberEntry));
                                    //mainListenerTextEars.ProcessCommand(); //process the command

                                } catch (NumberFormatException e) {
                                    ErrorMessages errMessages = new ErrorMessages();
                                    Random random = new Random();
                                    BlackBoard.mainPanel.mouth.sayThisOnce(errMessages.ErrorMessagesBadFormat[random.nextInt(10)] + ", " +  MainPanel.LastFacialRecognitionUsername);
                                }
                            }

                            BlackBoard.mainPanel.numberEntry = false; //disabling number entry collection 
                        } 
                        
                        if (BlackBoard.mainPanel.imageEntry == true) {
                            //WILL DEVELOP THIS INN VERSION 200
                            //YAY YAY YAY YAY YAY 
                            //BlackBoard.mainPanel.imageEntry = false; //disabling image entry
                            BlackBoard.mainPanel.imageEntry = false;
                        }
                        BlackBoard.mainPanel.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR)); //changes the cursor to a wait cursor
                       
                    }

                } while (ProjectJarvis.txtEntry.isVisible());
                
                
            }

            private void ShowCameraInTime() {
                Timer showCamera = new Timer(3000, (ActionEvent e) -> {
                    BlackBoard.emotions.setMood(null);
                });
                showCamera.setRepeats(false);
                showCamera.start();
            }

        };

        input.start();
    }

    public static void changeCommandExecutionStatus() {
        Regular.CommandExecuted = true;
    }

}

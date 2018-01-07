package jarvis.inpustream;

import jarvis.ProjectJarvis;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import outputstream.BlackBoard;
import outputstream.DisplayTrayIcon;
import outputstream.SystemSounds;

/**
 * the programs mouth for speaking
 *
 * @author owoye001
 */
public class Mouth {

    public boolean keepSpeaking = false; //starting talking at the beginning of the program.
    public String whatToSay = ""; //nothing to say
    public SpeechEngineConnector speechEngine;

    /**
     *
     * @param say what to say
     * @param TimeInSecond how long to wait before saying it again. Default is
     * 2000
     */
    public void ReapetStatementEvery(String say, int TimeInSecond) {

        if (ProjectJarvis.SPEECHENGINE_URL.equalsIgnoreCase("nothing")) return;
        
        Thread speak = new Thread() {
            @Override
            public void run() {
                while (keepSpeaking) {

                    //!keepspeaking - stop talking
                    if (!keepSpeaking) {

                    } else {
                        try {
                            // wait timeinsecond second they say what needs to be said.
                            Thread.sleep(TimeInSecond);
                        } catch (InterruptedException ex) {
                        }
                        try {
                            speechEngine.DownloadAudio(say);
                        } catch (IllegalArgumentException | IllegalStateException | IOException ex) {
                        }
                        SystemSounds playthis = new SystemSounds("temp");
                        playthis.currentObject.play();
                    }
                }
            }

        };

        speak.start(); //start the speak threaad 

    }

    public static Thread saythis;

    /**
     *
     * @param say this statement once and display on screen
     */
    public synchronized void sayThisOnce(String say) {

        try {

            BlackBoard.mainPanel.displayInfo.displayInformation(say); //display on the dash
            
            if (ProjectJarvis.SPEECHENGINE_URL.equalsIgnoreCase("nothing")) return;

            if (ProjectJarvis.settings.SpeechSoundActive && ProjectJarvis.speechEngineEnabledByInternet) {

                try {
                    speechEngine.DownloadAudio(say);
                } catch (IllegalArgumentException | IllegalStateException | IOException ex) {
                }
                SystemSounds playthis = new SystemSounds("temp");
                playthis.currentObject.play();

                try {
                    Thread.sleep(4000);
                    saythis = null;
                } catch (InterruptedException ex) {
                }
            } //end of speech sound active

        } catch (Exception e) {
        }

    }

    /**
     *
     * @param say this statement once
     * @param display display not information on screen
     */
    public synchronized void sayThisOnce(String say, boolean display) {

       if (ProjectJarvis.SPEECHENGINE_URL.equalsIgnoreCase("nothing")) return;
        
       try {

            //BlackBoard.mainPanel.displayInfo.displayInformation(say); //display on the dash

            if (ProjectJarvis.settings.SpeechSoundActive && ProjectJarvis.speechEngineEnabledByInternet) {

                try {
                    speechEngine.DownloadAudio(say);
                } catch (IllegalArgumentException | IllegalStateException | IOException ex) {
                }
                SystemSounds playthis = new SystemSounds("temp");
                playthis.currentObject.play();

                try {
                    Thread.sleep(4000);
                    saythis = null;
                } catch (InterruptedException ex) {
                }
            } //end of speech sound active

        } catch (Exception e) {
        }
    }

    /**
     * gets the program to start speaking
     */
    public void startSpeeking() {
        keepSpeaking = true;
    }

    /**
     * stops the program from speaking
     */
    public void stopSpeaking() {
        keepSpeaking = false;

    }

    /**
     * initialize the mouth function
     */
    public Mouth() {

        if (ProjectJarvis.settings.SpeechSoundActive && ProjectJarvis.speechEngineEnabledByInternet) {
            speechEngine = new SpeechEngineConnector();
        }
    }

    /**
     * asks a question and displays it on the screen at the same. the same time
     * it requests for text input from the user.
     *
     * @param say what developer need the application to say.
     */
    public synchronized void askQuestion(String say) {

        sayThisOnce(say, true); //, true
        Thread sendTextMessage = new Thread() {
            @Override
            public void run() {
                sendQuestionMessageIfNecessary(say);
            }
        };

        sendTextMessage.start();
        BlackBoard.mainPanel.displayInfo.displayQuestion(say);
    }
    
    //this method sends text messages to set phone number
    /**
     * @param textMessasge message to be sent
    */
    public void sendMessageToDefaultPhone(String textMessasge){
        
        Thread sendTextMessage = new Thread();
        
        if (!sendTextMessage.isAlive()){
        
        sendTextMessage = new Thread() {
            @Override
            public void run() {
                sendQuestionMessageIfNecessary(textMessasge,true);
            }
        };
        //kk`
        sendTextMessage.start();
        
        BlackBoard.mainPanel.mouth.sayThisOnce(ProjectJarvis.SALUTATION + " , your message has been sent!");
        
        Toolkit.getDefaultToolkit().beep();
        
        } else { //let user know that thread is busy
            BlackBoard.mainPanel.mouth.sayThisOnce("Be patient with me,"
                    + " I am still sending a message right now " + ProjectJarvis.SALUTATION);
        }
    }

    /**
     *
     * @param text this represents the text to be printed to the screen
     */
    public void printToScreen(String text) {
        BlackBoard.mainPanel.displayInfo.displayInformation(text); //just print on screen
    }

    /**
     *
     * @param say
     * @param SpeechAnswer expecting a speech answer
     */
    public void askQuestion(String say, boolean SpeechAnswer) {
        BlackBoard.mainPanel.displayInfo.displayQuestion(say, true);
        sayThisOnce(say, true);

    }

    /**
     * it asks question but does not say it.
     *
     * @param fakeInt does nothing
     * @param say
     */
    public void askQuestion(int fakeInt, String say) {
        BlackBoard.mainPanel.displayInfo.displayQuestion(say);
    }

    private void sendQuestionMessageIfNecessary(String string) {
        if (MainListener.SMSResponse || MainListener.KeepSMSTextingUser) {
            MainListener.SMSResponseText = string.replace(" ", "%20");
            if (MainListener.SMSResponseText != null || MainListener.SMSResponseText != "") { //there is a message to send
                String link = "http://www.bigmesoftwares.com/projectjarvis/jarvis.php?SendMessageTo="
                        + MainListener.MYPHONENUMBER + "&Message=" + MainListener.SMSResponseText;

                HttpURLConnection connection = null;
                try {
                    URL urladdress = new URL(link);
                    String agent = "Applet";
                    String query = "query=" + "S";
                    String type = "application/x-www-form-urlencoded";
                    connection = (HttpURLConnection) urladdress.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("User-Agent", agent);
                    connection.setRequestProperty("Content-Type", type);
                    connection.setRequestProperty("Content-Length", "" + query.length());

                    OutputStream out = connection.getOutputStream();
                    out.write(query.getBytes());
                    int rc = connection.getResponseCode();
                    String rm = connection.getResponseMessage();
                } catch (Exception e) {
                    //e.printStackTrace();
                } finally {
                    connection.disconnect();
                }

                MainListener.SMSResponseText = null;
            }
            MainListener.SMSResponse = false; //turning it off
        } else {
            MainListener.SMSResponseText = null;
        }

    }

    /**
     * this method sends the message immediately. Used for send user text message
     * @param string
     * @param Immediately 
     */
     private void sendQuestionMessageIfNecessary(String string, boolean Immediately) {
        
         
         if (ProjectJarvis.SendSMSMessageLink.equalsIgnoreCase("nothing")){
             DisplayTrayIcon.trayIcon.displayMessage("Unable to send SMS message. No send link present, check config file", "Project Jarvis", TrayIcon.MessageType.INFO);
             return;
         }
         
            MainListener.SMSResponseText = string.replace(" ", "%20");
            if (MainListener.SMSResponseText != null) { //there is a message to send
                String link = ProjectJarvis.SendSMSMessageLink
                        + MainListener.MYPHONENUMBER + "&Message=" + MainListener.SMSResponseText;

                HttpURLConnection connection = null;
                try {
                    URL urladdress = new URL(link);
                    String agent = "Applet";
                    String query = "query=" + "S";
                    String type = "application/x-www-form-urlencoded";
                    connection = (HttpURLConnection) urladdress.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("User-Agent", agent);
                    connection.setRequestProperty("Content-Type", type);
                    connection.setRequestProperty("Content-Length", "" + query.length());

                    OutputStream out = connection.getOutputStream();
                    out.write(query.getBytes());
                    int rc = connection.getResponseCode();
                    String rm = connection.getResponseMessage();
                } catch (Exception e) {
                    //e.printStackTrace();
                } finally {
                    connection.disconnect();
                }

                MainListener.SMSResponseText = null;
            }
     }

    
}

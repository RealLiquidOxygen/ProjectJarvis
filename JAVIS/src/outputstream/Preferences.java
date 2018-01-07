/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outputstream;

import jarvis.ProjectJarvis;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Contain preferences for the ai
 *
 * @author owoye001
 */
public class Preferences {

    public static Properties aiProperties = new Properties();
    public String mainScreenBackground;
    public String splashBackground;
    public boolean RegularSoundActive = true;
    public boolean SpeechSoundActive = true;
    private OutputStream output = null;
    private InputStream input = null;

    /**
     *
     * constructor
     */
    public Preferences() {

        splashBackground = "background/default.PNG";
        
        mainScreenBackground = splashBackground;

        //load from consider file and set variables.
        loadProperties();
    }

    /**
     *
     * @param PAPERENUM number representing the specific wallpaper
     *
     */
    public void SetMainWallPaper(int PAPERENUM) {
        switch (PAPERENUM) {
            case 1:
                mainScreenBackground = "background/firstbackground.PNG";
                break;
            case 2:
                mainScreenBackground = "background/secondBackground.PNG";
                break;
            case 3:
                mainScreenBackground = "background/thirdBackground.PNG";
                break;
            case 4:
                mainScreenBackground = "background/fourthBackground.PNG";
                break;
            case 5:
                mainScreenBackground = "background/default.PNG";
                break;

        }
    }

   
    /**
     *
     * @param status sound status
     */
    public void SetRegularSoundActive(boolean status) {
        RegularSoundActive = status;
    }

    /**
     * @param status For speech sounds
     */
    public void SetSpeechSoundActive(boolean status) {
        SpeechSoundActive = status;
    }

    /**
     * updates the current program settings
     */
    void upDatePreferences() {
        aiProperties.setProperty("SpeechEnginePassword", ProjectJarvis.SPEECHENGINE_PASSWORD);
        aiProperties.setProperty("SpeechEngineURL", ProjectJarvis.SPEECHENGINE_URL);
        aiProperties.setProperty("SpeechEngineUsername", ProjectJarvis.SPEECHENGINE_USERNAME);
        aiProperties.setProperty("Number_of_MSN_News_OnStartup", String.valueOf(ProjectJarvis.FIRST_FIVE_NEWS));
        aiProperties.setProperty("Salutation", ProjectJarvis.SALUTATION);
        aiProperties.setProperty("SpeechEngineVoiceName", ProjectJarvis.SPEECHENGINE_VOICE);
        aiProperties.setProperty("RegularSound", String.valueOf(RegularSoundActive));
        aiProperties.setProperty("SpeechSound", String.valueOf(SpeechSoundActive));
        aiProperties.setProperty("WallPaper", mainScreenBackground);
        aiProperties.setProperty("BuildFolder", ProjectJarvis.BUILDFOLDER);
        aiProperties.setProperty("Number_Of_Memes", String.valueOf(ProjectJarvis.NUMBER_OF_MEMES));
        aiProperties.setProperty("FaceRecognitionDisabled", String.valueOf(ProjectJarvis.DisableFaceRecog));
        aiProperties.setProperty("TimeSetForRandomMemes", String.valueOf(ProjectJarvis.TIME_SET_FOR_RANDOM_MEMES));
        aiProperties.setProperty("SMSListener", String.valueOf(ProjectJarvis.SMSMessage));
        aiProperties.setProperty("BatteryStatusUpdate", String.valueOf(ProjectJarvis.batteryStatusUpdate));
        aiProperties.setProperty("Humor", String.valueOf(ProjectJarvis.HumourLevel));
        aiProperties.setProperty("WeatherIdentifier", ProjectJarvis.WEATHERIDENTIFIER);
        aiProperties.setProperty("WeatherLocation", ProjectJarvis.WEATHERLOCATION);
        aiProperties.setProperty("DebugMode", String.valueOf(ProjectJarvis.DEBUGMODE));
        saveCurrentProgramState(); //save program state
    }

    /**
     * saves the current program state.
     */
    public void saveCurrentProgramState() {
        try {

            output = new FileOutputStream("config.properties");

            aiProperties.store(new FileOutputStream("config.properties"), null);
        } catch (IOException e) {
            System.out.println("FISHY: COULD NOT SAVE PROFILE PROPERTIES");
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    System.out.println("FISHY: ERROR CLOSING OUTPUT STREAM");
                }
            }

        }
    }

    private void loadProperties() {
        
        
        try { 

		input = new FileInputStream("config.properties");

		// load a properties file
		aiProperties.load(input);

                
                //(10*60*1000)
		// get the property value and load it
		SetRegularSoundActive(Boolean.valueOf(aiProperties.getProperty("RegularSound", "true")));
                SetSpeechSoundActive(Boolean.valueOf(aiProperties.getProperty("SpeechSound", "true")));
                mainScreenBackground = aiProperties.getProperty("WallPaper", "background/thirdBackground.PNG");
                ProjectJarvis.SPEECHENGINE_PASSWORD = aiProperties.getProperty("SpeechEnginePassword", "nothing");
                ProjectJarvis.SPEECHENGINE_URL = aiProperties.getProperty("SpeechEngineURL","nothing");
                ProjectJarvis.SPEECHENGINE_USERNAME = aiProperties.getProperty("SpeechEngineUsername", "nothing");
                ProjectJarvis.SPEECHENGINE_VOICE = aiProperties.getProperty("SpeechEngineVoiceName", "nothing");
                ProjectJarvis.BUILDFOLDER = aiProperties.getProperty("BuildFolder", "Build1");
                ProjectJarvis.NUMBER_OF_MEMES = Integer.parseInt(aiProperties.getProperty("Number_Of_Memes", "40"));
                ProjectJarvis.DisableFaceRecog = Boolean.valueOf(aiProperties.getProperty("FaceRecognitionDisabled", "false"));
                ProjectJarvis.TIME_SET_FOR_RANDOM_MEMES = Integer.parseInt(aiProperties.getProperty("TimeSetForRandomMemes", String.valueOf(600000)));
                ProjectJarvis.SMSMessage = Boolean.valueOf(aiProperties.getProperty("SMSListener", "true"));
                ProjectJarvis.batteryStatusUpdate = Boolean.valueOf(aiProperties.getProperty("BatteryStatusUpdate", "true"));
                ProjectJarvis.HumourLevel = Integer.parseInt(aiProperties.getProperty("Humor", "100"));
                ProjectJarvis.WEATHERIDENTIFIER = aiProperties.getProperty("WeatherIdentifier", "w3091293");
                ProjectJarvis.WEATHERLOCATION = aiProperties.getProperty("WeatherLocation", "crookston, minnesota");
                ProjectJarvis.DEBUGMODE = Boolean.valueOf(aiProperties.getProperty("DebugMode", "true"));
                ProjectJarvis.SALUTATION = aiProperties.getProperty("Salutation", "sir");
                ProjectJarvis.FIRST_FIVE_NEWS = Integer.parseInt(aiProperties.getProperty("Number_of_MSN_News_OnStartup","5"));
                ProjectJarvis.MYPHONENUMBER = aiProperties.getProperty("YourPhoneNumber", "Nothing");
                ProjectJarvis.QUERYURLFORNEWMESSAGE = aiProperties.getProperty("QueryURLForNewMessage", "Nothing");
                ProjectJarvis.BASEWEATHERURLDONOTCHANGE = aiProperties.getProperty("BaseWeatherURLDONOTCHANGE", "Nothing");
                ProjectJarvis.SendSMSMessageLink = aiProperties.getProperty("SendSMSMessageLinkGETREQUEST", "Nothing");
                ProjectJarvis.MYACCOUNTPASSWORD = " " + aiProperties.getProperty("WindowsPassword", "Nothing");
	} catch (IOException ex) {
		ex.printStackTrace();
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

        
        
        
    }

}

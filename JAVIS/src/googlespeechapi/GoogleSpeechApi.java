/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googlespeechapi;

import jarvis.ProjectJarvis;
import java.io.File;
import javaFlacEncoder.FLACFileWriter;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import outputstream.BlackBoard;
import speech.microphone.Microphone;
import speech.recognizer.GoogleResponse;
import speech.recognizer.Recognizer;

/**
 *https://github.com/lkuza2/java-speech-api/tree/api-example
 * @author owoye001
 */
public class GoogleSpeechApi {

    private static final String GOOGLE_API_KEY = "AIzaSyCCoGGrpF2RKFdJDmVMBjm0bp47RmlN1U8";
    public String result = null;
    /**
     * constructor
     */
    public GoogleSpeechApi() {
       
    // Mixer.Info[] infoArray = AudioSystem.getMixerInfo();
    // for(Mixer.Info info : infoArray) {
    //    System.out.println("info: " + info.toString());
    // } 
   AudioFileFormat.Type[] typeArray = AudioSystem.getAudioFileTypes();
//    for(AudioFileFormat.Type type : typeArray) {
//       System.out.println("type: " + type.toString());
//    } 

    Microphone mic = new Microphone(FLACFileWriter.FLAC);
    File file = new File ("tmp/testfile2.flac");	//Name your file whatever you want
    try {
      mic.captureAudioToFile (file);
    } catch (Exception ex) {
      //Microphone not available or some other error.
      BlackBoard.mainPanel.mouth.sayThisOnce(ProjectJarvis.SALUTATION + ", Microphone is not availible.");
      //.printStackTrace ();
    }

    /* User records the voice here. Microphone starts a separate thread so do whatever you want
     * in the mean time. Show a recording icon or whatever.
     */
    try {
      BlackBoard.mainPanel.mouth.printToScreen("Recording...");
      Thread.sleep (5000);	//In our case, we'll just wait 5 seconds.
      mic.close ();
    } catch (InterruptedException ex) {
      //ex.printStackTrace ();
    }

    mic.close ();		//Ends recording and frees the resources
    BlackBoard.mainPanel.mouth.printToScreen("Recording stopped.");

    Recognizer recognizer = new Recognizer (Recognizer.Languages.ENGLISH_US, GOOGLE_API_KEY);
    //Although auto-detect is available, it is recommended you select your region for added accuracy.
    try {
      int maxNumOfResponses = 4;
      //System.out.println("Sample rate is: " + (int) mic.getAudioFormat().getSampleRate());
      GoogleResponse response = recognizer.getRecognizedDataForFlac (file, maxNumOfResponses, (int) mic.getAudioFormat().getSampleRate ());
//      BlackBoard.mainPanel.mouth.sayThisOnce(response.getResponse () == null ? 
//              "I don't know what you are trying to say" : response.getResponse());
      if (response.getResponse() == null){
          result = null;
      } else {
          result = response.getResponse();
      }
//System.out.println ("Google is " + Double.parseDouble (response.getConfidence ()) * 100 + "% confident in" + " the reply");
//      System.out.println ("Other Possible responses are: ");
//      for (String s:response.getOtherPossibleResponses ()) {
//	  System.out.println ("\t" + s);
//      }
    }
    catch (Exception ex) {
      // TODO Handle how to respond if Google cannot be contacted
      BlackBoard.mainPanel.mouth.sayThisOnce (ProjectJarvis.SALUTATION + ", google cannot be contacted, so I could not transcribe");
      //ex.printStackTrace ();
    }

    //file.deleteOnExit ();	//Deletes the file as it is no longer necessary.
    }
    
    /**
     * 
     * @return return the result of google query. 
     */
    public String getResult() {
        try { Thread.sleep(2000); } catch (InterruptedException ex) {}
        return result;
    }
    
}

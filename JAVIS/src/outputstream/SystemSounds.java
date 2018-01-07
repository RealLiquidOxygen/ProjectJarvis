/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outputstream;

import jaco.mp3.player.MP3Player;
import jarvis.ProjectJarvis;
import jarvis.utilities.Utilities;
import java.io.File;

/**
 * the mouth of the AI 
 * @author owoye001
 */
public class SystemSounds {

    public MP3Player currentObject = new MP3Player();

    public String resourceURL;

    /**
     * 
     * @param soundname class constructor 
     */
    public SystemSounds(String soundname) {

        this.resourceURL = Utilities.getJarParentDir() + "sounds\\" + soundname + ".mp3";

        currentObject = new MP3Player(new File(this.resourceURL));

    }

    /**
     * play the sound once 
     */
    public void playsound() {
        if (ProjectJarvis.settings.RegularSoundActive) {
        currentObject.play();
        }
    }
    
    /**
     * stop the music
     */
    public void stopsound() {
        currentObject.stop();
    }

    /**
     * 
     * @return MP3 player object of the sound player class 
     */
    public MP3Player getAudio() {
        return currentObject;
    }

    /**
     * play the sound file on a loop 
     */
    public void playonloop() {
       if (ProjectJarvis.settings.RegularSoundActive) {
        currentObject.addToPlayList(new File(this.resourceURL));
        currentObject.setRepeat(true);
        currentObject.play();
       }
    }
}

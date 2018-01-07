/*
 * Copyright (C) 2017 owoye001
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
package outputstream;

import jarvis.ProjectJarvis;
import jarvis.inpustream.ImageSlider;
import jarvis.utilities.Utilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;

/**
 *
 * @author owoye001
 */
public class Memes extends Thread {
    
    public int TIME_SET_FOR_RANDOM_MEMES = ProjectJarvis.TIME_SET_FOR_RANDOM_MEMES;

    public boolean ThreadStillRunning = false;
    
    public Random random = new Random ();
    
    public int NUMBER_OF_MEMES = ProjectJarvis.NUMBER_OF_MEMES; //retrieving number of memes for project jarvis here.
    
    public String MEMES_BASE_LOCATION = ProjectJarvis.MEMES_LOCATION; //location of folder containing the memes
    
    public Memes(boolean activate){
        ThreadStillRunning = true; //ready to activate thread. 
    }

    public Memes() {
        //thread is not running at this time.
    }
    
    @Override
    public void run(){
        
        Timer showMemesEveryTimeSet = new Timer (TIME_SET_FOR_RANDOM_MEMES, (ActionEvent e) -> {
          
            ImageSlider go = new ImageSlider(MEMES_BASE_LOCATION + String.valueOf(random.nextInt(NUMBER_OF_MEMES-1) + 1) + ".jpg", "", 6); 
        
        });
        showMemesEveryTimeSet.setInitialDelay(5000); //first meme view set to 5 seconds. 
        
        showMemesEveryTimeSet.start(); //start the timer 
            
        while (ThreadStillRunning) {}; //busy wait 
        
        showMemesEveryTimeSet.stop(); //stop timer before exiting the thread
    }
}

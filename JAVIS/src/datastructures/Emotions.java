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
package datastructures;

/**
 *
 * @author owoye001
 */
public class Emotions {
    
    private MoodState mood; //this mood has effect on the camera or should I say the eyes.
    private MoodState privateMood; //this mood is to determine thread state.
    
    public Emotions() {
        mood = MoodState.HAPPY;
        privateMood = MoodState.NEUTRAL;
    }

    /**
     * @return the mood
     */
    public MoodState getMood() {
        return mood;
    }

    /**
     * @param mood the mood to set
     */
    public void setMood(MoodState mood) {
        this.mood = mood;
    }
    
    /**
     * 
     * @param mood this mood has nothing to do with the camera
     * this is current thread mood.
     */
    public void setPrivateModd (MoodState mood) {
        this.privateMood = mood;
    }
    
    /**
     * 
     * @return this mood does not control the eyes
     * this mood is the programs mood regardless of the web cam.
     */
    public MoodState getPrivateMood (){
        return privateMood;
    }
    
    
}

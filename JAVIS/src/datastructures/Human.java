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
package datastructures;

import AbstractClasses.JuniorObject;

/**
 *
 * @author owoye001
 */
public class Human extends JuniorObject {
    
    private MoodState humanmood;
    
    public Human(String ObjectName, String ObjectNickNames, String ObjectDescription) {
        super(ObjectName, ObjectNickNames, ObjectDescription);
         humanmood = MoodState.NEUTRAL;
    }

    /**
     * @return the human mood
     */
    public MoodState getHumanmood() {
        return humanmood;
    }

    /**
     * @param humanmood the human mood to set
     */
    public void setHumanmood(MoodState humanmood) {
        this.humanmood = humanmood;
    }
}

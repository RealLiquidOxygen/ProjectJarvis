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
package jarvis.utilities;

import Conversation.SessionManager;

/**
 *
 * @author owoye001
 */
public class PersonalityDrivesBasedOnEmotion extends Thread {

    @Override
    public void run() {
        switch (SessionManager.session.getHumanJarvis().getHumanmood()) {
            case AMAZING:
                //do that to show that you are feeling amazing right now 
                break;
            case LOVE: 
                //do task to show that you are feeling loved right now 
                break;
            case LOW:
                //do task to show that you are feeling not so happy right now 
                break;
            case TIRED:
                //do tasks to show that you are tired right now 
                break;
            case FOODQUESTION:
                //show people that you are hungry right now 
                break;
            case HOPEFUL:
                //be hopeful
                break;
            case EXCITED:
                //say exiting thing s
                break;
            case ANGRY:
                //show that you are angry right now 
                break;
            case BLESSED:
                //show that you are blessed right now 
                break;
            case GUILT:
                //exhibit guilt behaviors right now
                break;
        }
    }
}

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
package ReminderObjects;

import jarvis.ProjectJarvis;
import jarvis.inpustream.MainListener;
import java.awt.event.ActionEvent;
import java.util.Random;
import javax.swing.Timer;
import outputstream.BlackBoard;

/**
 *
 * @author owoye001
 */
public class RemindMe {

    private final String originalTextByUser;
    private String immediateReplyAfterTimerSetup;
    private String alarmListing;
    private String formalReplyAtAlarmTime;
    private int timeInMilliseconds;
    private int timeLeftInMilliseconds; //i ont use this variable yet, too much hassle with this
    private int remindObjectStatus; //2 mean its good, anything else is bad.
    private String timePhrase;

    public RemindMe(String originalTextByUser) {

        this.originalTextByUser = originalTextByUser;
        
        try {
            if (this.originalTextByUser.contains("in") || this.originalTextByUser.contains("at")) {
                //this is a good reminder object sentence
                remindObjectStatus++; //1
                immediateReplyAfterTimerSetup = new Random().nextBoolean() ? "Alright " : "Okay " + ProjectJarvis.SALUTATION + ", I will remind you to " + originalTextByUser.substring("remind me to ".length(), originalTextByUser.length());
                if (this.originalTextByUser.contains("in")) {
                    alarmListing = this.originalTextByUser.substring("remind me to".length() + 1, this.originalTextByUser.indexOf("in ") - 1);
                    //getting time phrase out of theh sentence
                    timePhrase = this.originalTextByUser.substring(this.originalTextByUser.indexOf("in ") + "in ".length());
                } else if (this.originalTextByUser.contains("at")) {
                    alarmListing = this.originalTextByUser.substring("remind me to".length() + 1, this.originalTextByUser.indexOf("at ") - 1);
                    //getting time phrase out of theh sentence
                    timePhrase = this.originalTextByUser.substring(this.originalTextByUser.indexOf("at ") + "at ".length());
                }
            }

            //formal reply at alarm time 
            formalReplyAtAlarmTime = ProjectJarvis.SALUTATION + ", I would suggest that you " + alarmListing;

            //getting time from timephrase
            if (timePhrase.contains("minute")) {
                timeInMilliseconds = Integer.parseInt(timePhrase.substring(0, timePhrase.indexOf("minute") - 1)) * 60 * 1000;
            } else if (timePhrase.contains("minutes")) {
                timeInMilliseconds = Integer.parseInt(timePhrase.substring(0, timePhrase.indexOf("minutes") - 1)) * 60 * 1000;
            } else if (timePhrase.contains("hour")) {
                timeInMilliseconds = Integer.parseInt(timePhrase.substring(0, timePhrase.indexOf("hour") - 1)) * 60 * 60 * 1000;
            } else if (timePhrase.contains("hours")) {
                timeInMilliseconds = Integer.parseInt(timePhrase.substring(0, timePhrase.indexOf("hours") - 1)) * 60 * 60 * 1000;
            } else if (timePhrase.contains("second")) {
                timeInMilliseconds = Integer.parseInt(timePhrase.substring(0, timePhrase.indexOf("second") - 1)) * 1000;
            } else if (timePhrase.contains("seconds")) {
                timeInMilliseconds = Integer.parseInt(timePhrase.substring(0, timePhrase.indexOf("seconds") - 1)) * 1000;
            } else if (timePhrase.contains("day")) {
                timeInMilliseconds = Integer.parseInt(timePhrase.substring(0, timePhrase.indexOf("day") - 1)) * 24 * 60 * 60 * 1000;
            } else if (timePhrase.contains("days")) {
                timeInMilliseconds = Integer.parseInt(timePhrase.substring(0, timePhrase.indexOf("days") - 1)) * 24 * 60 * 60 * 1000;
            }

            remindObjectStatus++; //2
        } catch (Exception e) {
           // e.printStackTrace();
            BlackBoard.mainPanel.mouth.sayThisOnce("I am sorry " + ProjectJarvis.SALUTATION + ", I could not set up the reminder. It had something to do with the time");
        }
    }

    /**
     * @return the originalTextByUser
     */
    public String getOriginalTextByUser() {
        return originalTextByUser;
    }

    /**
     * @return the immediateReplyAfterTimerSetup
     */
    public String getImmediateReplyAfterTimerSetup() {
        return immediateReplyAfterTimerSetup;
    }

    /**
     * @param immediateReplyAfterTimerSetup the immediateReplyAfterTimerSetup to
     * set
     */
    public void setImmediateReplyAfterTimerSetup(String immediateReplyAfterTimerSetup) {
        this.immediateReplyAfterTimerSetup = immediateReplyAfterTimerSetup;
    }

    /**
     * @return the alarmListing
     */
    public String getAlarmListing() {
        return alarmListing;
    }

    /**
     * @param alarmListing the alarmListing to set
     */
    public void setAlarmListing(String alarmListing) {
        this.alarmListing = alarmListing;
    }

    /**
     * @return the formalReplyAtAlarmTime
     */
    public String getFormalReplyAtAlarmTime() {
        return formalReplyAtAlarmTime;
    }

    /**
     * @param formalReplyAtAlarmTime the formalReplyAtAlarmTime to set
     */
    public void setFormalReplyAtAlarmTime(String formalReplyAtAlarmTime) {
        this.formalReplyAtAlarmTime = formalReplyAtAlarmTime;
    }

    /**
     * @return the timeInMilliseconds
     */
    public int getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    /**
     * @param timeInMilliseconds the timeInMilliseconds to set
     */
    public void setTimeInMilliseconds(int timeInMilliseconds) {
        this.timeInMilliseconds = timeInMilliseconds;
    }

    /**
     * @return the timeLeftInMilliseconds
     */
    public int getTimeLeftInMilliseconds() {
        return timeLeftInMilliseconds;
    }

    /**
     * @param timeLeftInMilliseconds the timeLeftInMilliseconds to set
     */
    public void setTimeLeftInMilliseconds(int timeLeftInMilliseconds) {
        this.timeLeftInMilliseconds = timeLeftInMilliseconds;
    }

    /**
     * @return the remindObjectStatus
     */
    public int getRemindObjectStatus() {
        return remindObjectStatus;
    }

    /**
     * @param remindObjectStatus the remindObjectStatus to set
     */
    public void setRemindObjectStatus(int remindObjectStatus) {
        this.remindObjectStatus = remindObjectStatus;
    }
    
    /**
     * this method start the timer for the reminder object.
     */
    public void startReminderImmediately() {
        
            Timer remindMeIn = new Timer(getTimeInMilliseconds(), (ActionEvent e) -> {
                
                //remind user what they needed to be remind of 
                BlackBoard.mainPanel.mouth.sayThisOnce(getFormalReplyAtAlarmTime());
                remindObjectStatus = 0; //hereby rendering it bad.
                
                //remove timer object that has already been used. 
                for (int i=0; i < MainListener.remindMeObjectsList.size(); i++){
                    if (MainListener.remindMeObjectsList.get(i).getRemindObjectStatus() != 2){
                         MainListener.remindMeObjectsList.remove(i);
                    }
                }
               
               
            });
            remindMeIn.setRepeats(false);
            remindMeIn.start(); //start the timer

         
    }

}

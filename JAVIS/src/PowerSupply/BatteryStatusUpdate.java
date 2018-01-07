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
package PowerSupply;

import Conversation.SessionManager;
import jarvis.ProjectJarvis;
import jarvis.inpustream.MainListener;
import jarvis.utilities.Utilities;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.util.Random;
import javax.swing.Timer;
import outputstream.BlackBoard;
import static outputstream.DisplayTrayIcon.trayIcon;

/**
 *
 * @author owoye001
 * this class makes the computer talk sometimes, based on power level
 */
public class BatteryStatusUpdate {
    
    public boolean updateUser = ProjectJarvis.batteryStatusUpdate; //to check if it should update the user
    Timer batteryAlerts,batteryAlertsVariableReset; //timer to monitor battery level
    String[] firstLineOptions; //to load the files required for the computer to speak
    boolean[] alreadySaidOnceBoolean = new boolean[6]; //to check if something is already spoken at a particular battery level
    
    /**
     * constructor 
     * initiate the talking mouth parameters
     * start monitoring the power level 
     */
    public BatteryStatusUpdate() {
        
        
        initializeVariables();
        startPowerLevelMonitor();
        
    }
    
    /**
     * initialize already said variables
     */
    private void initializeVariables() {
        for (int i=0; i < alreadySaidOnceBoolean.length-1; i++){
            alreadySaidOnceBoolean[i] = false; //-1 to prevent it from saying something every ten minutes, when its charging
        }
    }

    /**
     * monitors the power level and makes the AI say stuff
     */
    private void startPowerLevelMonitor() {
     batteryAlerts = new Timer(30000, (ActionEvent e) -> {
         
         if (updateUser){ //see if the user authorizes this!
         PowerStatus.INSTANCE.GetSystemPowerStatus(MainListener.batteryStatus);

                        int batteryPercentLeft = (Integer.valueOf(MainListener.batteryStatus.getBatteryLifePercent().substring(0, MainListener.batteryStatus.getBatteryLifePercent().length() - 1)));

                        switch (batteryPercentLeft / 10 * 10) {
                            case 0:
                            case 10:
                                if (alreadySaidOnceBoolean[0]) return;
                                firstLineOptions = Utilities.loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/emotions/10.txt");
                                sayAppropriateStuffForBatteryLevel(batteryPercentLeft);
                                alreadySaidOnceBoolean[0] = true;
                                break;
                            case 20:
                            case 30:
                                 if (alreadySaidOnceBoolean[1]) return;
                                firstLineOptions = Utilities.loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/emotions/20.txt");
                                sayAppropriateStuffForBatteryLevel(batteryPercentLeft);
                                alreadySaidOnceBoolean[1] = true;
                                break;
                            case 40:
                            case 50:
                                 if (alreadySaidOnceBoolean[2]) return;
                                firstLineOptions = Utilities.loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/emotions/30.txt");
                                sayAppropriateStuffForBatteryLevel(batteryPercentLeft);
                                alreadySaidOnceBoolean[2] = true;
                                break;
                            case 60:
                            case 70:
                                 if (alreadySaidOnceBoolean[3]) return;
                                firstLineOptions = Utilities.loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/emotions/40.txt");
                                sayAppropriateStuffForBatteryLevel(batteryPercentLeft);
                                alreadySaidOnceBoolean[3] = true;
                                break;
                            case 80:
                            case 90:
                                 if (alreadySaidOnceBoolean[4]) return;
                                firstLineOptions = Utilities.loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/emotions/50.txt");
                                sayAppropriateStuffForBatteryLevel(batteryPercentLeft);
                                alreadySaidOnceBoolean[4] = true;
                                break;
                            case 100:
                            default:
                                 if (alreadySaidOnceBoolean[5]) return;
                                firstLineOptions = Utilities.loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/emotions/60.txt");
                                sayAppropriateStuffForBatteryLevel(batteryPercentLeft);
                                alreadySaidOnceBoolean[5] = true;
                                break;
                        }
         } //end of check whether the user authorize this!
     });
     batteryAlerts.setInitialDelay(5 * 60 * 1000); //initial delay at 4 minutes
     batteryAlerts.start();
     
     //check if the device is plugged in every 30 minutes, if it is, reset the variables if is not already reset
     batteryAlertsVariableReset = new Timer(10 * 60 * 1000, (ActionEvent e) -> {
          if (updateUser){ //see if the user authorizes this!
             PowerStatus.INSTANCE.GetSystemPowerStatus(MainListener.batteryStatus);
             if (MainListener.batteryStatus.getBatteryFlagString().contains("Charging")){
                 initializeVariables();
             }
          } //end of if the user authorizes this action 

     });
     batteryAlertsVariableReset.start();
     //initializeVariables();
    }

    /**
     * will say appropriate stuff based on battery level
     * @param batteryPercentLeft the battery left on the computer
     */
    private void sayAppropriateStuffForBatteryLevel(int batteryPercentLeft) {
        if (batteryPercentLeft == 0 || batteryPercentLeft == 10){
            String powerlevelstring = Utilities.loadStrings("assets/backstory/" + ProjectJarvis.BUILDFOLDER + "/emotions/0.txt")[new Random().nextInt(5)];
            BlackBoard.mainPanel.mouth
                    .sayThisOnce(powerlevelstring);
            trayIcon.displayMessage(powerlevelstring, "Feelings", TrayIcon.MessageType.INFO);

        }
        BlackBoard.mainPanel.mouth.sayThisOnce(ProjectJarvis.SALUTATION + ", power level at " + String.valueOf(batteryPercentLeft) +"%");
        trayIcon.displayMessage(ProjectJarvis.SALUTATION + ", power level at " + String.valueOf(batteryPercentLeft) +"%", "Jarvis Power Level Update", TrayIcon.MessageType.INFO);
        try { Thread.sleep(5000); } catch (InterruptedException ex) {}
        if (new Random().nextBoolean()) SessionManager.mainListenerTextEars.sayFeelingAndSetEmotionsVariable(firstLineOptions, new Random());
    }
    
    /**
     * stop monitoring the power level
     */
    public void stopPowerLevelMonitor() {
        batteryAlerts.stop();
        batteryAlertsVariableReset.stop();
    }
}

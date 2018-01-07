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
package TextCommunication;

import Conversation.SessionManager;
import jarvis.ProjectJarvis;
import jarvis.inpustream.MainListener;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import outputstream.BlackBoard;
import outputstream.DisplayTrayIcon;

/**
 *
 * @author owoye001
 */
public class TextCommunication {
    GetLastPostedCommand dbConnection;
    public boolean KeepRunning = true;
    Timer checkTextMessage;
    
    public TextCommunication() {
         checkTextMessage = new Timer (30000, (ActionEvent e) -> {
          
             
          
           dbConnection = new GetLastPostedCommand(MainListener.QUERY);
           String newCommand = dbConnection.getLastestCommand();
           if (newCommand!=null) {
               
               try{
               
               if (MainListener.LastPhoneNumberReceived.equalsIgnoreCase(MainListener.MYPHONENUMBER)){
               //MainListener.scriptReader.ScriptStillRunning = false; //stop current thread.
               if (ProjectJarvis.txtEntry != null){
                   if (ProjectJarvis.txtEntry.isVisible()){
                       ProjectJarvis.txtEntry.textEntered = newCommand.toLowerCase();
                       MainListener.SMSResponse = true; //this is from a text message
                       ProjectJarvis.txtEntry.setVisible(false);
                       return;
                   }
               }
               MainListener.SMSResponse = true; //this is from a text message
               SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
               BlackBoard.mainPanel.textEntry = true; //needed to process command type
               BlackBoard.mainPanel.StringEntry = newCommand.toLowerCase(); //text message 
               
                   new Thread() { 
                      @Override
                       public void run() {
                           SessionManager.mainListenerTextEars.ProcessCommand(); //processes the command.
                       }
                   }.start();
               }
               
               
               } catch (NullPointerException textCommunicationError) {
                   BlackBoard.mainPanel.mouth.sayThisOnce(ProjectJarvis.SALUTATION + ", I am unable to contact the database!");
                   DisplayTrayIcon.trayIcon.displayMessage(ProjectJarvis.SALUTATION + ", I am unable to contact the database!", "Project Jarvis", TrayIcon.MessageType.ERROR);
                   stopTextCommunication();
               }
               
           }
        });
        
        checkTextMessage.start();
      
    }
    
    public void stopTextCommunication(){
        checkTextMessage.stop();
    }
}

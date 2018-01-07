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
package outputstream;

import Conversation.SessionManager;
import jarvis.ProjectJarvis;
import jarvis.inpustream.TextEntry;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author owoye001
 */
public class DisplayTrayIcon {
    
    public static TrayIcon trayIcon;
    
    public DisplayTrayIcon(){
        ShowTrayIcon();
    }
    
    public static void ShowTrayIcon() {
        
        if (!SystemTray.isSupported()){
            //tried my best, but i couldnt
            JOptionPane.showMessageDialog(null, "Your computer does not support task bar icons\n"
                    + "As a result, system tray icon for this program will not load");
            return;
            //program continues normally..
        }
        
        
        
        final PopupMenu popup = new PopupMenu();
        trayIcon = new TrayIcon(CreateIcon("/jarvis/images/bigmesmall.gif", "Tray Icon"));
        trayIcon.setToolTip("Version "+ProjectJarvis.VERSIONSTRING+"\nProject Jarvis");
        final SystemTray tray = SystemTray.getSystemTray();
        
        //add a menu.
        Menu displayMenu = new Menu ("Menu");
        
       
        //add components/menu items
        MenuItem ExecuteAction = new MenuItem ("Talk (`)");
        MenuItem AboutItem = new MenuItem ("About");
        MenuItem ExitItem = new MenuItem("Exit");
        
        
        //populate the pop up menu
        popup.add(ExecuteAction);
        popup.addSeparator();
        popup.add(AboutItem);
        popup.add(ExitItem);
        
        trayIcon.setPopupMenu(popup);
        
        
        ExecuteAction.addActionListener((ActionEvent e) -> {
            
            try{
            
            if (!BlackBoard.mainPanel.SystemReadyStatus) {
                //JOptionPane.showMessageDialog(null,"System is not ready!", "Jarvis",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
           
                Thread newActivity = new Thread() {
                    @Override
                    public void run() {
                        if (ProjectJarvis.txtEntry != null) ProjectJarvis.txtEntry.setVisible(false); //kill existing thread
                        ProjectJarvis.txtEntry = new TextEntry();
                        ProjectJarvis.txtEntry.setVisible(true);
                        SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                        BlackBoard.mainPanel.textEntry = true; //text entry
                        BlackBoard.mainPanel.sessionManager.collectInputValues();
                    }
                };
                newActivity.start();
            } catch(Exception eg) {
                JOptionPane.showMessageDialog(null, "System not ready", "Jarvis", JOptionPane.ERROR_MESSAGE);
//                try{
//                BlackBoard.exitRoutine();
//                System.exit(0);
//                }catch(Exception ef) {}
            }
            
        });
        
        AboutItem.addActionListener((ActionEvent e) -> {
            DisplayTrayIcon.trayIcon.displayMessage("Written by Ayodele Owoyele" + 
                    "\nProject Jarvis\n" + "Version "+ProjectJarvis.VERSIONSTRING
            +"\nBig-Me Softwares 2017", "About Author", TrayIcon.MessageType.INFO);
        });
        
         ExitItem.addActionListener((ActionEvent e) -> {
            tray.remove(trayIcon);
            try{
            BlackBoard.exitRoutine();
            }catch(Exception egg){}
            System.exit(0);
        });
        
        try {
            tray.add(trayIcon);
        } catch (AWTException ex) {
            //ex.printStackTrace();
            //tried my best, but i couldnt
        }
        
       
    }
    
    protected static Image CreateIcon(String path, String desc){
        URL ImageURL = DisplayTrayIcon.class.getResource(path);
        return (new ImageIcon (ImageURL, desc)).getImage();
    }
    
}

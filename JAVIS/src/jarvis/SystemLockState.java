/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jarvis;

import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import javax.swing.Timer;
import outputstream.BlackBoard;
import outputstream.DisplayTrayIcon;

/**
 *
 * @author owoye001
 */
public class SystemLockState {

  
    public SystemLockState() {
        // TODO code application logic here

        Runtime run = Runtime.getRuntime();
        Process checkLockedStateProcess = null;
        try {
             checkLockedStateProcess = run.exec("java -jar " + getJarParentDir() + "LockScreenTool\\LockedScreenSysEvents.jar");
            System.out.println("INFO: CHECK LOCK STATE PROCESS INITIATED!");
     
        final BufferedReader in = new BufferedReader(new InputStreamReader(checkLockedStateProcess.getInputStream()));
        
         Timer CheckScreenLock = new Timer (2500, (ActionEvent ae) -> {
            
            String line;
            
            try {
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                     DisplayTrayIcon.trayIcon.displayMessage(line, "Project Jarvis", TrayIcon.MessageType.INFO);

                    if (line.equalsIgnoreCase("locked")){
                        ProjectJarvis.LockedScreen = true;
                        Toolkit.getDefaultToolkit().beep();
                        BlackBoard.mainPanel.mouth.sayThisOnce("The screen is now locked!");
                        DisplayTrayIcon.trayIcon.displayMessage("Screen Locked", "Project Jarvis", TrayIcon.MessageType.INFO);
                    } else if (line.equalsIgnoreCase("unlocked")){
                        Toolkit.getDefaultToolkit().beep();
                        BlackBoard.mainPanel.mouth.sayThisOnce("The screen is now unlocked!");
                        DisplayTrayIcon.trayIcon.displayMessage("Screen Unlocked", "Project Jarvis", TrayIcon.MessageType.INFO);
                        ProjectJarvis.LockedScreen = false;
                    }
                }
            } catch (IOException ex) {
                //ex.printStackTrace();
            }
            
            
        
        
        });
         
       
        CheckScreenLock.start();
        
        System.out.println("INFO: CHECK LOCK STATE PROCESS RUNNING!");
        
        } catch (IOException ex) {
             System.out.println("INFO: CHECK LOCK STATE PROCESS TERMINATED!");
        }
        
    }

    public static String getJarParentDir() {
        File file = null;

        try {
            file = new File(SystemLockState.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return file.getParent() + "\\";
    }

}

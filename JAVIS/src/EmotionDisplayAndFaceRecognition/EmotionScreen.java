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
package EmotionDisplayAndFaceRecognition;

import Conversation.SessionManager;
import static Conversation.SessionManager.ShowCameraInTime;
import datastructures.MoodState;
import jarvis.ProjectJarvis;
import jarvis.utilities.Utilities;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import outputstream.BlackBoard;

/**
 *
 * @author owoye001
 */
public class EmotionScreen extends javax.swing.JPanel {

    public static Thread emotions;
    public static Thread monitorPeopleChanges;
    public ImageIcon Happy, Sad, Waving, Angry, Laughing, Tired;

    /**
     * Creates new form EmotionScreen
     */
    public EmotionScreen() {
        initComponents();
        Happy = new ImageIcon(EmotionScreen.class.getResource("/emotionGIFs/happy.gif"));
        Sad = new ImageIcon(EmotionScreen.class.getResource("/emotionGIFs/sad.gif"));
        Waving = new ImageIcon(EmotionScreen.class.getResource("/emotionGIFs/waving.gif"));
        Angry = new ImageIcon(EmotionScreen.class.getResource("/emotionGIFs/angry.gif"));
        Laughing = new ImageIcon(EmotionScreen.class.getResource("/emotionGIFs/laughing.gif"));
        Tired = new ImageIcon(EmotionScreen.class.getResource("/emotionGIFs/tired.gif"));

        //monitor mood changes and made appropriate changes.
        //wait a little before staring to monitor mood changes
        
        Timer startMonitoring = new Timer(3000, (ActionEvent e) -> {
            startMonintoringMoodChanges();
            if (ProjectJarvis.DisableFaceRecog = false){
            startMonitoringPeopleChanges();
            }
        });
        startMonitoring.setRepeats(false);
        startMonitoring.start();
        
    }

    /**
     * this method monitor mood changes and swaps gifs
     */
    boolean CameraRunning = true;
    /**
     * i will not use this method, causes entire system to freeze
     * USED IT
     */
    private void startMonitoringPeopleChanges() {
        FaceDetection.CheckFaceDb(); //start checking file for changes.
        monitorPeopleChanges = new Thread() {
            @Override
            public void run() {
                
                
                    Timer t = new Timer (3000, (ActionEvent e) -> {
                        FaceDetection.TakePicForRecognition = true;
                    });
                    t.start();
                    
                   
                
            }
        };
        monitorPeopleChanges.start();
    }
    private void startMonintoringMoodChanges() {
        emotions = new Thread() {
            @Override
            public void run() {
                new Timer (5000, (ActionEvent e) -> { 
                    if (BlackBoard.emotions.getMood() == null) {

                        if (!CameraRunning) {
                            if (!FaceDetection.stopLookingAtMe){
                                CameraRunning = true;
                                jLabelEmotIcon.setIcon(null);
                                FaceDetection.flipCards.show(FaceDetection.mainCardJPanel, "feed");
                                BlackBoard.mainPanel.fd.startFaceDetection();
                                revalidate();
                                repaint();
                            }
                        }

                    } else //stop face dection - MOOD EXIST
                        if (CameraRunning) {
                            BlackBoard.mainPanel.fd.stopFaceDetection();
                            FaceDetection.flipCards.show(FaceDetection.mainCardJPanel, "emotions");
                            jLabelEmotIcon.setIcon(Happy);
                            CameraRunning = false;
                            if (!FaceDetection.stopLookingAtMe){
                                ShowCameraInTime(); //show camera in certain time
                                SessionManager.session.getHumanJarvis().setHumanmood(BlackBoard.emotions.getMood());
                            }
                            revalidate();
                            repaint();
                        }

                    try {
                        if (BlackBoard.emotions.getMood() == MoodState.HAPPY
                                || BlackBoard.emotions.getMood() == MoodState.NEUTRAL
                                || BlackBoard.emotions.getMood() == MoodState.NOTTIRED) {
                            if (!jLabelEmotIcon.getIcon().equals(Happy)) {
                                jLabelEmotIcon.setIcon(Happy);
                            }
                        }

                        if (BlackBoard.emotions.getMood() == MoodState.SAD) {
                            if (!jLabelEmotIcon.getIcon().equals(Sad)) {
                                jLabelEmotIcon.setIcon(Sad);
                            }
                        }

                        if (BlackBoard.emotions.getMood() == MoodState.ANGRY) {
                            if (!jLabelEmotIcon.getIcon().equals(Angry)) {
                                jLabelEmotIcon.setIcon(Angry);
                            }
                        }

                        if (BlackBoard.emotions.getMood() == MoodState.LAUGHING) {
                            if (!jLabelEmotIcon.getIcon().equals(Laughing)) {
                                jLabelEmotIcon.setIcon(Laughing);
                                Utilities.playLaughFile();
                            }
                        }

                        if (BlackBoard.emotions.getMood() == MoodState.TIRED) {
                            if (!jLabelEmotIcon.getIcon().equals(Tired)) {
                                jLabelEmotIcon.setIcon(Tired);
                            }
                        }

                        if (BlackBoard.emotions.getMood() == MoodState.WAVING) {
                            if (!jLabelEmotIcon.getIcon().equals(Waving)) {
                                jLabelEmotIcon.setIcon(Waving);
                                Utilities.playLaughFile();
                            }

                        }

                    } catch (NullPointerException eg) {
                        jLabelEmotIcon.setIcon(Happy);
                    }
                }).start();
            }
        };
        emotions.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelEmotIcon = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setPreferredSize(new java.awt.Dimension(670, 340));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelEmotIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/emotionGIFs/happy.gif"))); // NOI18N
        add(jLabelEmotIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 340, 290));

        getAccessibleContext().setAccessibleName("");
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel jLabelEmotIcon;
    // End of variables declaration//GEN-END:variables
}

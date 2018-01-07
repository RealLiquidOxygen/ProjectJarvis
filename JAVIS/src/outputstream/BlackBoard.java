/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outputstream;

import datastructures.Emotions;
import jarvis.ProgressPanel;
import jarvis.ProjectJarvis;
import jarvis.utilities.RecordTimer;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * this is the main class in the program.
 *
 * @author owoye001
 */
public class BlackBoard extends javax.swing.JFrame {

    public static CardLayout mainCards = new CardLayout();  //card layout for different screens
    public static Container mainProgramComponentHolder; //getcontent pane 
    public static Dimension screenSize = new Dimension(); //screen size
    public static ProgressPanel progressPanel; //load screen jpanel 
    public static MainPanel mainPanel; //load screen jpanel
    public static Emotions emotions; //emotion section of the program.

    public static void exitRoutine() {

        //save the current program state. 
        ProjectJarvis.settings.upDatePreferences();

        //close facedections
        mainPanel.fd.stopFaceDetection();

        mainPanel.fd.dispose();
        
        DisplayTrayIcon.trayIcon.displayMessage("Have a good day, " + 
                (MainPanel.LastFacialRecognitionUsername.isEmpty() ? 
                        ProjectJarvis.SALUTATION : MainPanel.LastFacialRecognitionUsername) 
                + ", Goodbye!", "Project Jarvis", TrayIcon.MessageType.INFO);

    }
    private RecordTimer timer; //recording timer

    public static boolean isRecording = false; //is recording wav from the user

    public static Thread voiceListener; //voiceListener

    /**
     * Creates new form ProgressScreen
     *
     * @param loadListArrayList array list to log
     */
    public BlackBoard(ArrayList<String> loadListArrayList) {
        initComponents();

        //reinitiaze the components to my taste;
        mainProgramComponentHolder = getContentPane();

        //getting screen size
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setUpFrameAndComponents(mainProgramComponentHolder, loadListArrayList);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 102, 0));
        setUndecorated(true);
        setOpacity(0.75F);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(400, 300));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    //setup the JFrame.
    private void setUpFrameAndComponents(Container contentPane, ArrayList loadListArrayList) {

        //get default background location
        File defaultBackgroundFile = null;
        defaultBackgroundFile = new File("rsrc:defaultImage.PNG");

        //initializing mood state
        emotions = new Emotions();

        //adding the panels to the blackboard here. 
        progressPanel = new ProgressPanel(loadListArrayList); //first panel ever
        mainPanel = new MainPanel(); //main panel

        contentPane.setBackground(Color.black); //setting the background color of the content pane here
        contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); //changes the cursor to a wait cursor
        contentPane.setLayout(mainCards);

        add(progressPanel, "load"); //adding panel to form. 
        add(mainPanel, "main"); //this is the main panel

        mainCards.show(contentPane, "load");

        //add an event to happen when user decides to close this program
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {

                BlackBoard.exitRoutine();

            }
        });

        //setting jframe icon here. 
        URL resource = BlackBoard.class.getResource("/jarvis/images/bigme.gif");
        setIconImage(new ImageIcon(resource).getImage());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setTitle("Jarvis Project"); //show current user in title

        setVisible(true);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

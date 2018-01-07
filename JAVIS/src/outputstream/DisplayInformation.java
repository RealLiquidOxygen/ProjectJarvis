/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outputstream;

import jarvis.ProjectJarvis;
import jarvis.inpustream.MainListener;
import jarvis.inpustream.TextEntry;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;

/**
 * helps display information on the main panel
 *
 * @author owoye001
 */
public class DisplayInformation {

    //object instance variables 
    private HTMLDocument blank;
    private HTMLDocument htmlDoc;
    private int entry = 0;
    private Timer loadHtmlNicely;
    SystemSounds beep;

    //redenter the HTML display to the mainDisplayPanel
    void HTMLInformationToDisplay(String html, boolean SoundOn) {

        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            //Logger.getLogger(DisplayInformation.class.getName()).log(Level.SEVERE, null, ex);
        }

        BlackBoard.mainPanel.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        //getting current html doc
        htmlDoc = (HTMLDocument) MainPanel.jEditorPaneDisplayInfo.getDocument();

        try {
            //for speed optimization
            blank = new HTMLDocument();
            MainPanel.jEditorPaneDisplayInfo.setDocument(blank);
            MainPanel.detailPanelKit.read(new StringReader(html), htmlDoc, 1);

            if (SoundOn) {
                beep.playsound();
            }

            MainPanel.jEditorPaneDisplayInfo.setDocument(MainPanel.docMainScreen);
            MainPanel.jEditorPaneDisplayInfo.revalidate();
            MainPanel.jEditorPaneDisplayInfo.repaint();

        } catch (IOException | IndexOutOfBoundsException | BadLocationException ex) {
        }

        BlackBoard.mainPanel.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));

    }

    //display html elements on the mainDisplay JEditorPane
    public void displayH1(String string) {
        HTMLInformationToDisplay("<h1>" + string + "</h1>", true);
    }

    //display html h2
    public void displayH2(String string) {

        HTMLInformationToDisplay("<h2>" + string + "</h2>", true);
    }

    //display html h3
    public void displayH3(String string) {

        HTMLInformationToDisplay("<h3>" + string + "</h3>", true);
    }

    //display html h4
    public void displayH4(String string) {

        HTMLInformationToDisplay("<h4>" + string + "</h4>", true);
    }

    public void displayParagraph(String string) {
        HTMLInformationToDisplay("<p>" + string + "</p>", true);
    }

    /**
     *
     * @param arrayList of things to display on the screen
     */
    void displayArrayListText(ArrayList<String> arrayList) {

        htmlDoc = (HTMLDocument) MainPanel.jEditorPaneDisplayInfo.getDocument();

        loadHtmlNicely = new Timer(500, (ActionEvent e) -> {
            try {
                //for speed optimization
                blank = new HTMLDocument();
                MainPanel.jEditorPaneDisplayInfo.setDocument(blank);

                // detailPanelKit.read(new StringReader("" + new Random().nextInt() +""), htmlDoc, 1);
                MainPanel.detailPanelKit.read(new StringReader(arrayList.get(entry)), htmlDoc, 1);
                entry++;
                if (entry > arrayList.size() - 1) {
                    loadHtmlNicely.stop();
                }

                MainPanel.jEditorPaneDisplayInfo.setDocument(MainPanel.docMainScreen);
                entry = 0;
            } catch (IOException | IndexOutOfBoundsException | BadLocationException ex) {
                // Logger.getLogger(ProgressPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        loadHtmlNicely.start();

        SystemSounds programload = new SystemSounds("longrenderingtask");
        programload.playsound();
    }

    /**
     * constructor
     */
    public DisplayInformation() {
        this.loadHtmlNicely = new Timer(0, null);
        beep = new SystemSounds("beep");
    }

    public void displayQuestion(String string) {
        if (string.isEmpty() || string.equalsIgnoreCase("")) {
            return;
        }

        displayH1(string);

        if (ProjectJarvis.txtEntry != null) {
            ProjectJarvis.txtEntry.setVisible(false); //kill existing thread
        }

        try {
            SwingUtilities.invokeAndWait(() -> {
                ProjectJarvis.txtEntry = new TextEntry();
                ProjectJarvis.txtEntry.textEntered = "";
                ProjectJarvis.txtEntry.jTextFieldInput.setText("");
                ProjectJarvis.txtEntry.setVisible(true);
            });
        
        } catch (InterruptedException | InvocationTargetException e){} 

    }

    /**
     *
     * @param string quesiton to be answered
     * @param SpeechAnswer speech answer is expected for the question in this
     * case.
     */
    public void displayQuestion(String string, boolean SpeechAnswer) {
        // if (string.isEmpty() || string.equalsIgnoreCase("")) return;
        HTMLInformationToDisplay("<h1>" + string + "</h1>", false);
    }

    /**
     *
     * @param string information to be displayed
     */
    public void displayInformation(String string) {

        if (MainListener.SMSResponse || MainListener.KeepSMSTextingUser) {
            MainListener.SMSResponseText = string.replace(" ", "%20");
            if (MainListener.SMSResponseText != null || MainListener.SMSResponseText != "") { //there is a message to send
                String link = "http://www.bigmesoftwares.com/projectjarvis/jarvis.php?SendMessageTo="
                        + MainListener.MYPHONENUMBER + "&Message=" + MainListener.SMSResponseText;

                new Thread() {
                    @Override
                    public void run() {
                        gotoLink(link);
                    }

                    private void gotoLink(String link) {
                        HttpURLConnection conn = null;
                        try {
                            URL url = new URL(link);
                            String agent = "Applet";
                            String query = "query=" + "S";
                            String type = "application/x-www-form-urlencoded";
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.setDoOutput(true);
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("User-Agent", agent);
                            conn.setRequestProperty("Content-Type", type);
                            conn.setRequestProperty("Content-Length", "" + query.length());

                            OutputStream out = conn.getOutputStream();
                            out.write(query.getBytes());
                            int rc = conn.getResponseCode();
                            String rm = conn.getResponseMessage();
                        } catch (Exception e) {
                            //e.printStackTrace();
                        } finally {
                            conn.disconnect();
                        }
                    }
                }.start();

                MainListener.SMSResponseText = null;
            }
            MainListener.SMSResponse = false; //turning it off
        } else {
            MainListener.SMSResponseText = null;
        }
        //if (string.isEmpty() || string.equalsIgnoreCase("")) return;
        //if it is an sms response
        HTMLInformationToDisplay("<h1>" + string + "</h1>", false);

    }

    /**
     * display image on the console
     *
     * @param imageURL the is the image URL retrieved from the internet
     */
    public void displayImage(String imageURL) {
        HTMLInformationToDisplay("<img src=" + imageURL + ">", false);
    }

    /**
     * display image on the console
     *
     * @param imageURL the is the image URL retrieved from files
     */
    public void displayImage(URL imageURL) {
        HTMLInformationToDisplay("<img src=" + imageURL + ">", false);
    }

    public void displayRegularHTML(String html) {
        HTMLInformationToDisplay(html, false);
    }

}

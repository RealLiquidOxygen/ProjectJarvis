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
package NewsPlugIn;
import Conversation.SessionManager;
import jarvis.ProjectJarvis;
import jarvis.inpustream.MainListener;
import jarvis.utilities.Utilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;
import outputstream.BlackBoard;
import outputstream.MainPanel;

/**
 *
 * @author owoye001
 */
public class MSNNews {
    private  final String MSN_URL = "http://rss.msn.com";
    final String CHROMELOCATION = "\"Program Files (x86)\"\\Google\\Chrome\\Application\\chrome.exe";
    final String CHROMEREALLOCATION = System.getProperty("user.dir").substring(0, 3) + CHROMELOCATION;
    static boolean NewsAvailable;
    public static volatile boolean KeepReading;
    ArrayList<String> NewsTitles = new ArrayList<>();
    ArrayList<String> NewsDescription= new ArrayList<>();
    ArrayList<String> NewsLink = new ArrayList<>();
    /**
     * retrieves the news on a daily basis. 
     * @param interesting number of new to read.
     */
    public MSNNews(int interesting) {
        
        readAllNewsTopics(interesting);
        
    }
    
    public MSNNews(boolean b) {
        //displayTheNewCollated();
        readAllNews(-1);
    }
    private boolean RetrieveTheLatestNews() {
        try {
            NewsAvailable = DownloadNews(MSN_URL);
        } catch (IllegalArgumentException |
                IllegalStateException | IOException ex) {
            NewsAvailable = false; 
        }
        return NewsAvailable;
    }
    /**
     * this method is for testing purposes. only
     * print out the news article parsed to the console. 
     */
    private void displayTheNewCollated() {
    
           try {if (DownloadNews(MSN_URL)){
                 System.out.println("News Topics\n");
            for ( int i =0; i < NewsTitles.size(); i ++){
                System.out.println (NewsTitles.get(i));
                System.out.println (NewsLink.get(i));
                System.out.println();
                System.out.println(NewsDescription.get(i));
                System.out.println();
            }
           }
         } catch 
                 (IllegalArgumentException | 
                         IllegalStateException | IOException ex) {
         }
    }
    /**
     * this method is capable of reading the news description. 
     */
    private void readAllNews(int interesting) {
     
        
        
        if (RetrieveTheLatestNews()){
            //BlackBoard.mainPanel.mouth.sayThisOnce(String.format("A total of %d news article was retrieved today", NewsTitles.size()));
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("No news articles retrieved from the news source");
            return;
        }
        
        int counter = 0; KeepReading = true;
        
        BlackBoard.mainPanel.mouth.sayThisOnce("In todays news, we have");
        
        int variableToControlLoop;
        
        if (interesting==-1){
            variableToControlLoop = NewsTitles.size();
        } else {
            variableToControlLoop = interesting;
            
            if (NewsTitles.size() > 10) {
                counter = 5 + new Random().nextInt(5);
                variableToControlLoop = counter + interesting;
            } //interesting news are usually located here. 
        }
        
        
        while (counter < variableToControlLoop && KeepReading){
            
            
            BlackBoard.mainPanel.mouth.sayThisOnce(NewsTitles.get(counter));

            BlackBoard.mainPanel.mouth.sayThisOnce("In more details..");

            BlackBoard.mainPanel.mouth.sayThisOnce(NewsDescription.get(counter));
            
            MainListener.LinkOfCurrentlyReadNews = NewsLink.get(counter);

            if (NewsDescription.get(counter).contains("This news article does not have a description")) {

                BlackBoard.mainPanel.mouth.sayThisOnce("Although, there is a link to that particular article");

                SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();

                BlackBoard.mainPanel.textEntry = true; //enter texts.

                BlackBoard.mainPanel.mouth.askQuestion("Would you like to visit that link " + ProjectJarvis.SALUTATION);

                SessionManager.WaitForInputAndProcessIt();
                try {
                    SessionManager.input.join();
                } catch (InterruptedException ex) {
                }

                if (BlackBoard.mainPanel.StringEntry.equalsIgnoreCase("yes")
                        || BlackBoard.mainPanel.StringEntry.equalsIgnoreCase("ya")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("please")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("yes")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("yeah")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("sure")
                        || BlackBoard.mainPanel.StringEntry.toLowerCase().contains("definitely")) {

                    try {
                        //push website data to front end.
                        Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start " + CHROMEREALLOCATION + " " + NewsLink.get(counter)});
                        BlackBoard.mainPanel.mouth.sayThisOnce("Alright " + MainPanel.LastFacialRecognitionUsername + "!");
                    } catch (Exception ex) {
                        BlackBoard.mainPanel.mouth.sayThisOnce("I am sorry, I am unable to open the link!");
                    }

                    

                } else {
                    //BlackBoard.mainPanel.mouth.sayThisOnce("Alright then!");
                }

            } 
            
            try {Thread.sleep(15000);} catch (InterruptedException ex) { }
            
            counter++;
        } 
        
         KeepReading = false;
         
         MainListener.msnnews = null;

    }
    /**
     * this method will read all the news topics . 
     */
    private void readAllNewsTopics(int interesting) {
        
        //read all news items from msn news.
        if (RetrieveTheLatestNews()){
        //BlackBoard.mainPanel.mouth.sayThisOnce(String.format("A total of %d news article was retrieved today", NewsTitles.size()));
        } else {
            BlackBoard.mainPanel.mouth.sayThisOnce("No news articles retrieved from the news source");
            return;
        }
        
        int counter = 0; KeepReading = true;
        
        BlackBoard.mainPanel.mouth.sayThisOnce("In todays news, we have");
        
        int variableToControlLoop;
        
        if (interesting==-1){
            variableToControlLoop = NewsTitles.size();
        } else {
            variableToControlLoop = interesting;
            
              if (NewsTitles.size() > 10) {
                counter = 5 + new Random().nextInt(5);
                variableToControlLoop = counter + interesting;
            } //interesting news are usually located here. 
        }
        
        while (counter < variableToControlLoop && KeepReading){
        
            BlackBoard.mainPanel.mouth.sayThisOnce(NewsTitles.get(counter));
        
            counter++;
        }
        
        BlackBoard.mainPanel.mouth.sayThisOnce(Utilities.loadStrings("dialogs/smalltalk/morningenthusiam.txt")[new Random().nextInt(Utilities.loadStrings("dialogs/smalltalk/morningenthusiam.txt").length)] + ", " + (new Random().nextBoolean() ? ProjectJarvis.SALUTATION : MainPanel.LastFacialRecognitionUsername));
        
        KeepReading = false;
        
        MainListener.msnnews = null;
        
    }
    public Boolean DownloadNews (String url) throws IllegalArgumentException, IllegalStateException, IOException {
       
        URLConnection conn;
        
        InputStream is;
        
        OutputStream outstream;
        
        NewsTitles = new ArrayList<>();
        
        NewsDescription= new ArrayList<>();
        
        NewsLink = new ArrayList<>();

        boolean newDownloaded = doHttpPost(url);

        return newDownloaded;
    }
    private boolean doHttpPost(String mUrl) {
        
        URL url = null;
     
        try {
            url = new URL(mUrl);
        } catch (MalformedURLException ex) {
        }
        
        
        HttpURLConnection conn = null;
        
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
        }
        
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException ex) {
        }
        
        conn.setRequestProperty("Accept", "application/xml"); //json
        
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
        } catch (IOException ex) {
        }

        String output;
        //the first 6 lines is junk.`
        try {
            for ( int i = 0; i < 6 ; i++){
                output = br.readLine();
            }
        }catch (IOException e) {
            
        }
        
        
        try {
            while ((output = br.readLine()) != null) {
                
                try {
                    
                    if (-1 !=  output.indexOf("<title>")){
                        NewsTitles.add(output.substring(output.indexOf
                        ("<title>") + "<title>".length(), output.indexOf("</title>"))
                                .replace("&#39;", "'"));
                    } else if (-1 !=  output.indexOf("<description>")){
                        
                            output = output.substring(output.indexOf("/>") + "/>".length(), output.indexOf("]]></descriptio"));
                            output = output.replace("&#39;", "'")
                                    .replace("", "")
                                    .replace("&amp;nbsp;", "")
                                    .replace("&quot;", "\"")
                                    .replace("&lt;p&gt;", "");
                        if (output.length() < 2 || output.contains("                   <description><![CDATA[")){
                            output = "This news article does not have a description";
                        }
                       
                        
                        NewsDescription.add(output);
                        
                    } else if (-1 !=  output.indexOf("<link>")){
                        NewsLink.add(output.substring(output.indexOf("<link>")+ "<link>".length(), output.indexOf("</link>")));
                    }
                   
                } catch 
                        (StringIndexOutOfBoundsException ex) {
                    
                } 
            }
            
            if (!NewsDescription.isEmpty()){
              
                    try(PrintWriter out = new PrintWriter("news.txt")  )
                    {
                    
                    for (int i=0; i < NewsDescription.size(); i++){
                        out.println("====================================");
                        out.println(NewsTitles.get(i));
                        out.println(NewsDescription.get(i));
                        out.println(NewsLink.get(i));
                        out.println("====================================\n");
                        
                    }
                   
                    }
                    }
            return !NewsTitles.isEmpty();
        } catch (IOException ex) {
            return false;
        } finally {
        conn.disconnect();
        }
       
    }
}

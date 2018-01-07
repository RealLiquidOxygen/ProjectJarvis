/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternetSearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import outputstream.BlackBoard;

/**
 *
 * @author owoye001
 */
public class GoogleSearch {

    public ArrayList<String> urlForWebPages = new ArrayList<>();
    public ArrayList<String> imageFromWeb = new ArrayList<>();
    public ArrayList<String> articleSnippets = new ArrayList<>();
    public String headline, datePublished, description, articleBody;
    public boolean ArticleFound = false;
    public StringBuilder fullHtmlOutput = new StringBuilder();
    public String snippet = "";

    /**
     * this class search google using custom api to find information.
     *
     * @param args the command line arguments
     */
    public GoogleSearch(String QUERY) {

        URL url = null;
        ArticleFound = false;
        try {
            url = new URL(
                    "https://www.googleapis.com/customsearch/v1?key=AIzaSyC_l7p26-fSdcIE5M5SCUnRigkl4hc1ADI&cx=008131775351800950046:spp_4i1yakw&q=QUERY&alt=json".replace("QUERY", QUERY));
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
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
        } catch (IOException ex) {
        }

        String output;
        //System.out.println("Output from Server .... \n");
        try {
            while ((output = br.readLine()) != null) {

                try {
                    //System.out.println(output);
                    if (-1 != output.indexOf("\"formattedUrl\": \"")) {
                        urlForWebPages.add(output.substring(20, output.length() - 2));
                    }

                    if (-1 != output.indexOf("\"src\": \"")) {
                        imageFromWeb.add(output.substring(14, output.length() - 1));
                    }

                    if (-1 != output.indexOf("\"snippet\": \"") && snippet.isEmpty()) {
                        snippet = (output.substring(15, output.length() - 2)).replace("See more.", "").replace("\\n", "");
                        BlackBoard.mainPanel.mouth.sayThisOnce(snippet);
                    }

                    if (-1 != output.indexOf("\"headline\": \"") && ArticleFound == false) {
                        headline = output.substring(19, output.length() - 2);
                        output = br.readLine();
                        datePublished = output.substring(24, output.length() - 17);
                        output = br.readLine();
                        description = output.substring(22, output.length() - 2);
                        output = br.readLine();
                        articleBody = output.substring(22, output.length() - 1).split("\\.")[0];

                        articleSnippets.add(headline);
                        articleSnippets.add(datePublished);
                        articleSnippets.add(description);
                        articleSnippets.add(articleBody);

                        ArticleFound = true;
                    }
                } catch (StringIndexOutOfBoundsException ex) {
                }

            }
        } catch (IOException ex) {
        }

        conn.disconnect();
        try {
            getContentOfWebPage(urlForWebPages.get(0).toString());
        } catch (Exception e) {
        }
    }

    public Thread waitingForReply = null;

    /**
     * I tried using method. It worked very well. But it is not compatible with
     * my display subroutine HTML 3.2 NOT COMPATIBLE WITH HTML 5. THE END
     *
     * @param urlString
     */
    public void getContentOfWebPage(String urlString) {
        // Make a URL to the web page
        waitingForReply = new Thread() {
            @Override
            public void run() {
                try {

                    URL url = null;
                    try {
                        url = new URL(urlString);
                    } catch (MalformedURLException ex) {
                    }

                    // Get the input stream through URL Connection
                    URLConnection con = null;
                    try {
                        con = url.openConnection();
                    } catch (Exception ex) {
                    }
                    InputStream is = null;
                    try {
                        is = con.getInputStream();
                        // Once you have the Input Stream, it's just plain old Java IO stuff.
                        // For this case, since you are interested in getting plain-text web page
                        // I'll use a reader and output the text content to System.out.
                        // For binary content, it's better to directly read the bytes from stream and write
                        // to the target file.
                    } catch (Exception ex) {
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader(is));

                    String line = null;

                    try {
                        // read each line and write to System.out
                        while ((line = br.readLine()) != null) {
                            //System.out.println(line);
                            fullHtmlOutput.append(line);
                            fullHtmlOutput.append("\n");
                        }
                    } catch (IOException ex) {
                    }
                } catch (Exception e) {
                }
            }
        };
        waitingForReply.start();

    }

}

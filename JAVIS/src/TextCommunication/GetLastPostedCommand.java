package TextCommunication;

import jarvis.ProjectJarvis;
import jarvis.inpustream.MainListener;
import java.io.*;
import java.net.*;

public class GetLastPostedCommand {
    
    public static boolean IgnoreFirstRunCommand = true; //ignore the command, from first getCommand, stale.
    
    public GetLastPostedCommand(String queryStatement) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(QUERYURL);
            String agent = "Applet";
            String query = "query=" + queryStatement;
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
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            //this can only handle one type of QUERY.
             inputLine = in.readLine(); 
                MainListener.LastCommandEnteredOnline = inputLine;
                inputLine = in.readLine(); 
                inputLine = in.readLine(); 
                inputLine = in.readLine(); 
                MainListener.LastPhoneNumberReceived = inputLine;
            
            in.close();
            int rc = conn.getResponseCode();
            //System.out.print("Response Code = "+rc+"\n");
            String rm = conn.getResponseMessage();
            //System.out.print("Response Message = "+rm+"\n");
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }
    
    private static final String QUERYURL = ProjectJarvis.QUERYURLFORNEWMESSAGE;

    public String getLastestCommand(){
        if (IgnoreFirstRunCommand == true){
            if (!MainListener.LastCommandEnteredOnline.equalsIgnoreCase(MainListener.LastCommandProcessed)){
            MainListener.LastCommandProcessed = MainListener.LastCommandEnteredOnline;
            IgnoreFirstRunCommand = false;
            }
            return null;
        }
        if (MainListener.LastCommandProcessed.equalsIgnoreCase(MainListener.LastCommandEnteredOnline)){
            return null; //nothing new happened
        }else{
            MainListener.LastCommandProcessed = MainListener.LastCommandEnteredOnline; //update processed commands
            return MainListener.LastCommandEnteredOnline;
        }
    }
}

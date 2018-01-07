package jarvis.inpustream;

import jarvis.ProjectJarvis;
import jarvis.utilities.Utilities;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

//watson
public class SpeechEngineConnector {

    
    private final String SPEECHENGINE_PASSWORD = ProjectJarvis.SPEECHENGINE_PASSWORD;
    private final String SPEECHENGINE_URL = ProjectJarvis.SPEECHENGINE_URL;
    private final String SPEECHENGINE_USERNAME = ProjectJarvis.SPEECHENGINE_USERNAME;
    public String SPEECHENGINE_VOICE = ProjectJarvis.SPEECHENGINE_VOICE;

    // Constructs the VaaS connector with default account (demo) settings.
    public SpeechEngineConnector() {
    }

    // Speaks given text with given VaaS voice.
    public Boolean DownloadAudio(String text) throws IllegalArgumentException, IllegalStateException, IOException {
        URLConnection conn;
        InputStream is;
        OutputStream outstream;
        
        String url = SPEECHENGINE_URL
                + text.replace(" ", "%20")
                + "&username=" + SPEECHENGINE_USERNAME + "&password=" 
                + SPEECHENGINE_PASSWORD + "&voice=" + SPEECHENGINE_VOICE;

        //System.out.println (url);
        // Download audio
        try {
            conn = new URL(url).openConnection();

            is = conn.getInputStream();
            outstream = new FileOutputStream(new File(Utilities.getJarParentDir() + "sounds\\temp.mp3"));
            byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) > 0) {
                outstream.write(buffer, 0, len);
            }
            outstream.close();
        } catch (Exception e) {
        }

        return true;	

    }
}

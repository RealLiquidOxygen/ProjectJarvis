package EmotionDisplayAndFaceRecognition;


import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 3/10/13
 * Time: 8:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class SysConfig {
    private static final String CONFIG_FILE = "conf.xml";
    //private File file;
    private static SysConfig instance;
    private String dBPath;
    private int eyeDistance;
    private int eigenFaceWidth;
    private int eigenFaceHeight;
    private int eigenVectorsNumber;

    private SysConfig() throws URISyntaxException {
        if (instance != null) {
            throw new Error();
        }
        
        dBPath = "assets\\faces";
        eyeDistance = 63;
        eigenFaceWidth = 125;
        eigenFaceHeight = 150;
        eigenVectorsNumber = 16;
    }

    public String getDBPath() {
//        Document doc = getConfigDoc();
//        return doc.selectSingleNode("//conf/db_path").getText();
        return dBPath;
    }

//    public static double getTreshold() {
//        Document doc = getConfigDoc();
//        return Double.parseDouble(doc.selectSingleNode("//conf/treshold").getText());
//    }

    public int getEyeDistance() {
//        Document doc = getConfigDoc();
//        return Integer.parseInt(doc.selectSingleNode("//conf/eye_distance").getText());
        return eyeDistance;
    }

    public int getEigenFaceWidth() {
//        Document doc = getConfigDoc();
//        return Integer.parseInt(doc.selectSingleNode("//conf/eigen_face_width").getText());
        return eigenFaceWidth;
    }

    public  int getEigenFaceHeight() {
//        Document doc = getConfigDoc();
//        return Integer.parseInt(doc.selectSingleNode("//conf/eigen_face_height").getText());
        return eigenFaceHeight;
    }

    public  int getEigenVectorsNumber() {
//        Document doc = getConfigDoc();
//        return Integer.parseInt(doc.selectSingleNode("//conf/num_of_eigenfaces").getText());
        return eigenVectorsNumber;
    }

    public static synchronized SysConfig getInstance()  {
        if (instance == null)
            try {
                instance = new SysConfig();
            } catch (URISyntaxException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        return instance;
    }
}

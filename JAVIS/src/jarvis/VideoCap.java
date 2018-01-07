/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jarvis;


import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import outputstream.SystemSounds;
import outputstream.BlackBoard;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;        
import org.opencv.highgui.VideoCapture;        

        
public class VideoCap {
    
    
    public VideoCap(String saveLocation){
    	
        
    	VideoCapture camera = new VideoCapture(0);
    	
    	if(!camera.isOpened()){
    		System.out.println("Error");
    	}
    	else {
    		Mat frame = new Mat();
    	    while(true){
    	    	if (camera.read(frame)){
    	    		Highgui.imwrite(saveLocation, frame);
                        SystemSounds flash = new SystemSounds("flash");
                        flash.playsound();
    	    		break;
    	    	}
    	    }	
    	}
    	camera.release();
    }
    
    public static void takeSnapShot() {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage capture = null;
        try {
            capture = new Robot().createScreenCapture(screenRect);
        } catch (AWTException ex) {}
        try {
             ImageIO.write(capture, "bmp", new File(new JFileChooser().getFileSystemView().getDefaultDirectory().toString().replace("Documents", "Pictures") + "\\RoboPhoto" + new Random().nextInt(5000) + ".bmp"));
             SystemSounds flash = new SystemSounds("flash");
                        flash.playsound();
        } catch (IOException ex) {}
    }
    
  private static void loadLibrary() {
    try {
        InputStream in = null;
        File fileOut = null;
        String osName = System.getProperty("os.name");
        System.out.println(osName);
        if(osName.startsWith("Windows")){
            int bitness = Integer.parseInt(System.getProperty("sun.arch.data.model"));
            if(bitness == 32){
                System.out.println("32 bit detected");
                in = BlackBoard.class.getResourceAsStream("/opencv/x86/opencv_java249.dll");
                fileOut = File.createTempFile("lib", ".dll");
            }
            else if (bitness == 64){
                System.out.println("64 bit detected");
                in = BlackBoard.class.getResourceAsStream("/opencv/x64/opencv_java249.dll");
                fileOut = File.createTempFile("lib", ".dll");
            }
            else{
                System.out.println("Unknown bit detected - trying with 32 bit");
                in = BlackBoard.class.getResourceAsStream("/opencv/x86/opencv_java249.dll");
                fileOut = File.createTempFile("lib", ".dll");
            }
        }
        else if(osName.equals("Mac OS X")){
            in = BlackBoard.class.getResourceAsStream("/opencv/mac/libopencv_java249.dylib");
            fileOut = File.createTempFile("lib", ".dylib");
        }


        OutputStream out = FileUtils.openOutputStream(fileOut);
        IOUtils.copy(in, out);
        in.close();
        out.close();
        System.load(fileOut.toString());
    } catch (Exception e) {
        throw new RuntimeException("Failed to load opencv native library", e);
    }

}
  
  private static void loadLib(String name, String path) {
  name = System.mapLibraryName(name); // extends name with .dll, .so or .dylib
  try {
        InputStream in = BlackBoard.class.getResourceAsStream("/"+path + name);
        File fileOut = new File(System.getProperty("java.library.path"));
        OutputStream out = FileUtils.openOutputStream(fileOut);
        IOUtils.copy(in, out);
        in.close();
        out.close();
        System.load(fileOut.toString());//loading goes here
   } catch (Exception e) {
               //handle
   }
}
  
}

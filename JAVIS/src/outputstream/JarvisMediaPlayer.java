/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outputstream;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

/**
 *
 * @author owoye001
 */
public class JarvisMediaPlayer {

    private final JFrame frame;
    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
    public boolean isRunning = false; //video is currently playing.
    
    public JarvisMediaPlayer(String videoLocation) {
        frame = new JFrame("Jarvis - Media Player");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
            }
        });
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
            }
            
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                mediaPlayerComponent.release();
                frame.setVisible(false);
                isRunning = false;
            }
            
            @Override
            public void error(MediaPlayer mediaPlayer) {
            }
        });
        frame.setContentPane(mediaPlayerComponent);
        frame.setVisible(true);
        mediaPlayerComponent.getMediaPlayer().playMedia(videoLocation);
        mediaPlayerComponent.getMediaPlayer().setFullScreen(true);
        isRunning = true;
    }

    public JarvisMediaPlayer() {
         frame = null;
         mediaPlayerComponent = null;
    }
    
    public void closePlayerAndReleaseResources(){
        mediaPlayerComponent.getMediaPlayer().stop();
        mediaPlayerComponent.release();
        frame.setVisible(false);
        isRunning = false;
    }
    
   
}

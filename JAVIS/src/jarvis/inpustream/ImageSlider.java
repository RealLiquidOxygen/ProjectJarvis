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
package jarvis.inpustream;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import jaco.mp3.player.MP3Player;
import jarvis.utilities.ScriptReader;
import jarvis.utilities.Utilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import outputstream.BlackBoard;

/**
 *
 * @author owoye001
 */
public class ImageSlider extends JPanel implements ActionListener, MouseListener {

    private JLabel titleLabel;
    private static final Font sans = new Font("SansSerif", Font.BOLD, 22);
    private static final Border border
            = BorderFactory.createLineBorder(Color.green);
    private List<String> list = new ArrayList<String>();
    private List<ImageIcon> cache = new ArrayList<ImageIcon>();
    private List<MP3Player> cacheMP3 = new ArrayList<MP3Player>();
    private JLabel imageLabel = new JLabel();
    private JButton prevButton = new JButton();
    private JButton nextButton = new JButton();
    private JComboBox favorites;
    private String[] itemNames;
    private  int CarouselTypeOnGUI = 1; //Image Carousel
    private MP3Player music;
    private JWebBrowser webBrowser; //web browser.

    public ImageSlider(String dataLocation, String nameLocation, int CarouselType, boolean Empty) {
        this.setLayout(new BorderLayout());
        //names of object load
        if (!nameLocation.contentEquals("")){
        itemNames = Utilities.loadStrings(nameLocation);
         }

        CarouselTypeOnGUI = CarouselType; //type of carousel to be created.

        switch (CarouselTypeOnGUI) {
            case 1: //Image carousel
                LoadImageSliderGUI(dataLocation);
                break;
            case 2: //Music carousel
                LoadMusicSliderGUI(dataLocation);
                break;
            case 3: //Video carousel
                LoadVideoSliderGUI(dataLocation);
                break;
            case 4: //the middle finger
            case 5: //load beating heart
                LoadBigEmotionScreen();
                break;
            case 6: //dank memes
            case 7: //emergency image display
                ShowAPictureToUser(dataLocation);
                break;

        }

        favorites = new JComboBox(list.toArray(new String[list.size()]));
        favorites.setActionCommand("favs");
        favorites.addActionListener(this);

        prevButton.setText("\u22b2Prev");
        prevButton.setFont(sans);
        prevButton.setActionCommand("prev");
        prevButton.addActionListener(this);

        nextButton.setText("Next\u22b3");
        nextButton.setFont(sans);
        nextButton.setActionCommand("next");
        nextButton.addActionListener(this);

        //optional button
         JButton select = new JButton("Choose This");
        if (CarouselTypeOnGUI > 1 && CarouselTypeOnGUI < 4) {
           
            select.setFont(sans);
            select.addActionListener((ActionEvent e) -> {
                ScriptReader.updateLikedPictureObject(titleLabel.getText());
                try{
                    music.stop();
                }catch(NullPointerException efe) {}
                if (CarouselTypeOnGUI == 3){
                    webBrowser.navigate("http://www.google.com");
                }
                CarouselFrame.setVisible(false); //hide this screen
                CarouselFrame = null; //stop all action by this variable 
            });
        }
        
         JPanel controlPanel = new JPanel();
       
        controlPanel.add(prevButton);
        //controlPanel.add(favorites);
        controlPanel.add(nextButton);
        
        if (CarouselTypeOnGUI > 1 && CarouselTypeOnGUI < 4){
            controlPanel.add(select);
        }
        if (CarouselTypeOnGUI > 0 && CarouselTypeOnGUI < 4) { //middle finger and beating heart
        controlPanel.setBorder(border);
        controlPanel.setBackground(Color.black);
        this.add(controlPanel, BorderLayout.SOUTH);
        }
    }

    private void LoadImageSliderGUI(String dataLocation) {

        //car image loads
        for (int i = 1; i <= itemNames.length; i++) {
            list.add(dataLocation + i + ".jpg");
        }

        for (int i = 0; i < list.size(); i++) {
            cache.add(i, null);
        }

        titleLabel = new JLabel();
        titleLabel.setBackground(Color.black);
        titleLabel.setForeground(Color.red);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setBorder(border);
        this.add(titleLabel, BorderLayout.NORTH);

        imageLabel.setIcon((ImageIcon) getObject(0));
        titleLabel.setText(itemNames[0]);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBackground(Color.black);
        imageLabel.setForeground(Color.green);
        imageLabel.setBorder(border);
        imageLabel.addMouseListener(this);
        this.add(imageLabel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();
        switch (CarouselTypeOnGUI) {
            case 1: //image carousel
                if ("favs".equals(cmd)) {
                    int index = favorites.getSelectedIndex();
                    ImageIcon image = (ImageIcon) getObject(index);
                    imageLabel.setIcon(image);
                    if (image != null) {
                        imageLabel.setText("");
                    } else {
                        imageLabel.setText("Image not available.");
                    }
                }
                if ("prev".equals(cmd)) {
                    int index = favorites.getSelectedIndex() - 1;
                    if (index < 0) {
                        index = list.size() - 1;
                    }
                    favorites.setSelectedIndex(index);
                    titleLabel.setText(itemNames[index]);
                }
                if ("next".equals(cmd)) {
                    int index = favorites.getSelectedIndex() + 1;
                    if (index > list.size() - 1) {
                        index = 0;
                    }
                    favorites.setSelectedIndex(index);
                    titleLabel.setText(itemNames[index]);
                }
                break;
            case 2: //music object.
                if ("favs".equals(cmd)) {
                    int index = favorites.getSelectedIndex();
                    music.stop();
                    music = (MP3Player) getObject(index);
                    music.play();
                    if (music == null) {
                        BlackBoard.mainPanel.mouth.sayThisOnce("I cannot find that music file!");
                    }
                }
                if ("prev".equals(cmd)) {
                    int index = favorites.getSelectedIndex() - 1;
                    if (index < 0) {
                        index = list.size() - 1;
                    }
                    favorites.setSelectedIndex(index);
                    titleLabel.setText(itemNames[index]);
                }
                if ("next".equals(cmd)) {
                    int index = favorites.getSelectedIndex() + 1;
                    if (index > list.size() - 1) {
                        index = 0;
                    }
                    favorites.setSelectedIndex(index);
                    titleLabel.setText(itemNames[index]);
                }
                break;
            case 3: //video object
                // webBrowser.navigate(String.valueOf(getObject(0)));
                if ("favs".equals(cmd)) {
                    int index = favorites.getSelectedIndex();
                    webBrowser.navigate(String.valueOf(getObject(index)));
                }
                if ("prev".equals(cmd)) {
                    int index = favorites.getSelectedIndex() - 1;
                    if (index < 0) {
                        index = list.size() - 1;
                    }
                    favorites.setSelectedIndex(index);
                    titleLabel.setText(itemNames[index]);
                }
                if ("next".equals(cmd)) {
                    int index = favorites.getSelectedIndex() + 1;
                    if (index > list.size() - 1) {
                        index = 0;
                    }
                    favorites.setSelectedIndex(index);
                    titleLabel.setText(itemNames[index]);
                }
                break;
            default:
                break;
        }

    }

    public JButton getDefault() {
        return nextButton;
    }

    // Return the (possibly cached) image having the given index.
    private Object getObject(int index) {
        switch (CarouselTypeOnGUI) {
            case 1:
                ImageIcon image = cache.get(index);
                if (image != null) {
                    return image;
                }
                String name = list.get(index);
                //URL url = ImageSlider.class.getResource(name);
                //if (url != null) {
                if (name != null) {
                    //image = new ImageIcon(url);
                    image = new ImageIcon(new String(Utilities.getJarParentDir() + name));
                }
                cache.set(index, image);
                return image;
            case 2:
                MP3Player musicCheck = cacheMP3.get(index);
                if (musicCheck != null) {
                    return musicCheck;
                }
                String musicURL = list.get(index);
                //URL url = ImageSlider.class.getResource(name);
                //if (url != null) {
                if (musicURL != null) {
                    //image = new ImageIcon(url);
                    music = new MP3Player(new File(new String(Utilities.getJarParentDir() + musicURL)));
                }
                cacheMP3.set(index, music);
                return music;
            case 3: //video player
                String websiteurl = list.get(index);
                return websiteurl;

            default:
                return null;
        }

    }
    public static JFrame CarouselFrame;

    /**
     *
     * @param location location of the objects
     * @param nameLocation location of the names of the text files of the object
     * @param CarouselType type of carousel that should be loaded.
     */
    public ImageSlider(String location, String nameLocation, int CarouselType) {
        Thread videoPlayer = new Thread() {
            @Override
            public void run() {
            CarouselFrame = new JFrame();
            CarouselFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ImageSlider go = new ImageSlider(location, nameLocation, CarouselType, true); //call first constructor
            CarouselFrame.add(go);
            CarouselFrame.setTitle("Jarvis - Carousel");
            CarouselFrame.setSize(1000, 800);
            CarouselFrame.setBackground(Color.black);
            CarouselFrame.setLocationRelativeTo(null);
            CarouselFrame.setUndecorated(true);
            CarouselFrame.setOpacity(0.85F);
            //frame.setAlwaysOnTop(true);
            CarouselFrame.setVisible(true);
            }
        };
        if (CarouselType == 3){
            try {
                SwingUtilities.invokeAndWait(videoPlayer);//
            } catch (InterruptedException | InvocationTargetException ex) {
            }
        
        } else {
            CarouselFrame = new JFrame();
            CarouselFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ImageSlider go = new ImageSlider(location, nameLocation, CarouselType, true); //call first constructor
            CarouselFrame.add(go);
            CarouselFrame.setTitle("Jarvis - Carousel");
            CarouselFrame.setSize(1000, 800);
            CarouselFrame.setBackground(Color.black);
            CarouselFrame.setLocationRelativeTo(null);
            CarouselFrame.setUndecorated(true);
            CarouselFrame.setOpacity(0.8F);
            //frame.setAlwaysOnTop(true);
            CarouselFrame.setVisible(true);
        }

         
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getClickCount() == 1 && (CarouselTypeOnGUI == 1)) {
            ScriptReader.updateLikedPictureObject(titleLabel.getText());
            CarouselFrame.setVisible(false); //hide this screen 
            CarouselFrame = null; //stop all action by this variable 
           
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void LoadMusicSliderGUI(String dataLocation) {

        //music loads
        for (int i = 1; i <= itemNames.length; i++) {
            list.add(dataLocation + i + ".mp3");
        }

        for (int i = 0; i < list.size(); i++) {
            cacheMP3.add(i, null);
        }

        titleLabel = new JLabel();
        titleLabel.setBackground(Color.black);
        titleLabel.setForeground(Color.red);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setBorder(border);
        this.add(titleLabel, BorderLayout.NORTH);

        music = (MP3Player)getObject(0);
        music.play();
        JPanel PlayPauseJPanel = new JPanel();
        PlayPauseJPanel.setBackground(Color.black);
        PlayPauseJPanel.setLayout(new GridBagLayout());
        JButton play = new JButton("Play");
        play.setFont(sans);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               music.play();
            }
            
        });
        PlayPauseJPanel.add(play);
        JButton pause = new JButton("Pause");
        pause.setFont(sans);
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                music.pause();
            }
            
        });
        PlayPauseJPanel.add(pause);
        JButton stop = new JButton("Stop");
        stop.setFont(sans);
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            music.stop();
            }
            
        });
        PlayPauseJPanel.add(stop);

        titleLabel.setText(itemNames[0]);
        PlayPauseJPanel.setOpaque(false);
        PlayPauseJPanel.setBorder(border);
        this.add(PlayPauseJPanel, BorderLayout.CENTER);
    }

    private void LoadVideoSliderGUI(String dataLocation) {
        //video loads
        String[] urls = Utilities.loadStrings(dataLocation);

        //add the full screen option
        for (int k = 0; k < urls.length; k++) {
            urls[k] = urls[k] + "?fs=1";
        }

        list.addAll(Arrays.asList(urls));

        //for (int i = 0; i < list.size(); i++) cache.add(i, null);
        titleLabel = new JLabel();
        titleLabel.setBackground(Color.black);
        titleLabel.setForeground(Color.red);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setBorder(border);
        this.add(titleLabel, BorderLayout.NORTH);

        //imageLabel.setIcon(getImage(0));
        JPanel webBrowserPanel = new JPanel(new BorderLayout());
        webBrowser = new JWebBrowser();
        webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
        webBrowser.setBarsVisible(false);
        webBrowser.navigate((String) (getObject(0)));

        titleLabel.setText(itemNames[0]);
        webBrowserPanel.setOpaque(false);
        webBrowserPanel.setBorder(border);
        webBrowserPanel.addMouseListener(this);
        this.add(webBrowserPanel, BorderLayout.CENTER);
    }

    private void LoadBigEmotionScreen() {
        titleLabel = new JLabel();
        titleLabel.setBackground(Color.black);
        titleLabel.setForeground(Color.red);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setBorder(border);
        this.add(titleLabel, BorderLayout.NORTH);

        if (CarouselTypeOnGUI == 4)
        {
             titleLabel.setText("The Bird");
        imageLabel.setIcon(new ImageIcon(Utilities.getJarParentDir() + "images\\bird.jpg")); //change image
        } else if (CarouselTypeOnGUI == 5){
             titleLabel.setText("Beating Heart!");
        imageLabel.setIcon(new ImageIcon(Utilities.getJarParentDir() + "images\\beatingHeart.gif")); //change image    
        }
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBackground(Color.black);
        imageLabel.setForeground(Color.green);
        imageLabel.setBorder(border);
        imageLabel.addMouseListener(this);
        this.add(imageLabel, BorderLayout.CENTER);
        
        Timer hideBird = new Timer(3000, (ActionEvent e) -> {
            CarouselFrame.setVisible(false); //hide this screen
            CarouselFrame = null; //stop all action by this variable 
            CarouselTypeOnGUI = 1;
        });
        hideBird.setRepeats(false);
        hideBird.start();
    }

    private void ShowAPictureToUser(String dataLocation) {
        titleLabel = new JLabel();
        titleLabel.setBackground(Color.black);
        titleLabel.setForeground(Color.red);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setBorder(border);
        this.add(titleLabel, BorderLayout.NORTH);

        if (CarouselTypeOnGUI == 6)
        {
             titleLabel.setText("Dank Memes");
            imageLabel.setIcon(new ImageIcon(Utilities.getJarParentDir() + dataLocation)); //change image 
        } 
        
        //emergency image show.
        if (CarouselTypeOnGUI == 7) {
            imageLabel.setIcon(new ImageIcon(Utilities.getJarParentDir() + dataLocation)); //change image 
        }
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBackground(Color.black);
        imageLabel.setForeground(Color.green);
        imageLabel.setBorder(border);
        imageLabel.addMouseListener(this);
        this.add(imageLabel, BorderLayout.CENTER);
        
        Timer hidePicture = new Timer(8000, (ActionEvent e) -> {
            CarouselFrame.setVisible(false); //hide this screen
            CarouselFrame = null; //stop all action by this variable 
            CarouselTypeOnGUI = 1;
        });
        hidePicture.setRepeats(false);
        hidePicture.start();
    }
}

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Author: JJB
 * CSCI 3302 Seciton 001
 * 
 * 
 * This class does a simple image display where keyboard
 * inputs are used to maninpulate the images in various ways.
 * 
 * For example, rotate the image 90 degree, filter the image using masks,
 * invert the colors, grays scale, etc.
 * 
 */
public class ImageGUI extends JFrame implements KeyListener, ActionListener {

    // current image is the image current displayed
    private BufferedImage currentImage;
    // componenet (label) holding the image on the JFrame
    private JLabel imageHolder;
    // images that can be displayed
    private static final String[] IMAGES = new String[] { "buzz.png", "billy.png", "gru.png" };
    // index of current image being display
    private int currentImageIndex = 0;
    

    private ImageManipulator imgManipulator;
    GridBagConstraints c = new GridBagConstraints();

    JButton imageSwitch = new JButton("switch image");
    JButton imageRotate = new JButton("rotate image");
    JButton imageFlipV = new JButton("flip image vertically");
    JButton imageFlipH = new JButton("flip image horizontally");
    
    Icon buzz = new ImageIcon("buzz.png");
    Icon billy = new ImageIcon("billy.png");
    Icon gru = new ImageIcon("gru.png");

    JButton buzzB = new JButton(buzz);
    JButton billyB = new JButton(billy);
    JButton gruB = new JButton(gru);


    /*************************MAIN**********************
     * Simply create the object to display.
     * @param avg - not used
     * @throws IOException
     */
    public static void main(String avg[]) throws IOException {
        new ImageGUI();
    }

    /*****************************************************
     * This construct sets up and create the Image Display
     * with the first image.
     * @throws IOException - file not found
     * Post: an image is shown on the display
     */
    public ImageGUI() throws IOException {
        
        // note super class (JFrame) constructor is auto called
        this.setLayout(new GridBagLayout());
        this.addKeyListener(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // read in the default (first image)
        String imagePath = ImageGUI.IMAGES[this.currentImageIndex];
        File imageFile = new File(imagePath);
        this.currentImage = ImageIO.read(imageFile);

        // load the pixels
        imgManipulator = new ImageManipulator();
        imgManipulator.setImage(this.currentImage);

        c.fill = GridBagConstraints.HORIZONTAL;

        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        this.add(imageSwitch, c);
        imageSwitch.addActionListener(this);

        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        this.add(imageRotate, c);
        imageRotate.addActionListener(this);

        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 2;
        this.add(imageFlipV, c);
        imageFlipV.addActionListener(this);

        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 3;
        this.add(imageFlipH, c);
        imageFlipH.addActionListener(this);

        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 0;
        this.add(buzzB, c);
        buzzB.addActionListener(this);

        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 1;
        this.add(billyB, c);
        billyB.addActionListener(this);

        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 2;
        this.add(gruB, c);
        gruB.addActionListener(this);

        
        // display the image
        this.displayImage(this.currentImage);

    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == imageSwitch){
            switchImage();
        }
        int[][] newPixels = new int[0][0];
        if(e.getSource() == imageRotate){
            newPixels = imgManipulator.rotate();
        }
        if(e.getSource() == imageFlipV){
            newPixels = imgManipulator.flipVertical();
        }
        if(e.getSource() == imageFlipH){
            newPixels = imgManipulator.flipHorizontal();
        }

        if(e.getSource() == buzzB){
            switchImage(0);
        }
        if(e.getSource() == billyB){
            switchImage(1);
        }
        if(e.getSource() == gruB){
            switchImage(2);
        }
        
        if (newPixels.length > 0){
            this.updateDisplayedImage(newPixels);
        }
        
    }
    
    /*****************************************
     * This method switches between the images available.
     * Pre : none
     * Post: the available images (IMAGES array) are rotated through in
     * a circular queue fashion.
     */
    public void switchImage() {
        // move the circular queue position to next index
        this.currentImageIndex = (this.currentImageIndex + 1) % 3;
        String imagePath = ImageGUI.IMAGES[this.currentImageIndex];
        File imageFile = new File(imagePath);
        // get the new file, if there is an error, switch to the default
        // (first image) and try again
        try {
            this.currentImage = ImageIO.read(imageFile);
        } catch (IOException e) {
            this.currentImageIndex = 0;
            this.switchImage();
        }
        this.displayImage(this.currentImage);
    }

    public void switchImage(int index) {
        // move the circular queue position to next index
        this.currentImageIndex = index;
        String imagePath = ImageGUI.IMAGES[this.currentImageIndex];
        File imageFile = new File(imagePath);
        // get the new file, if there is an error, switch to the default
        // (first image) and try again
        try {
            this.currentImage = ImageIO.read(imageFile);
        } catch (IOException e) {
            this.currentImageIndex = 0;
            this.switchImage();
        }
        this.displayImage(this.currentImage);
    }

    /**********************************
     * This method displays the given image on the JFrame.
     * 
     * @param newImage - non-null buffered image is given
     * Post: the given image is display on the JFrame
     */
    private void displayImage(BufferedImage newImage) {
        this.setVisible(false);
        // if there is a current image, then remove it
        if (imageHolder != null)
            this.remove(imageHolder);
        // set the size of the frame with 50 pixels of padding
        //this.setSize(newImage.getWidth() + 50, newImage.getHeight() + 50);
        this.setSize(500, 500);
        // add the image to the JFrame
        imageHolder = new JLabel(new ImageIcon(newImage));
        
        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 4;
        
        this.add(imageHolder, c);
        this.setVisible(true); // show the JFrame with image
    }

    /**
     * This method updates the image displayed
     * using the given pixels.
     * @param pixels - the color info for the new pixel
     */
    private void updateDisplayedImage(int[][] pixels) {
        // get the width and height of the image
        int w = pixels.length;
        int h = pixels[0].length;

        // create a new image with the pixel info
        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int color = pixels[x][y];
                newImage.setRGB(x, y, color);
            }

        // display the newly created image
        this.displayImage(newImage);
    }

    /****************************************************
     * This method handles the event of a key being pressed
     * and changes the displayed image in accordance
     * with the key pressed.
     * Pre : given a key event holding the keycode and char
     * of the key pressed
     * Post: if the key is tied to an action, a new image will
     * be display. if not, nothing happends
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int[][] newPixels = new int[0][0];
        switch (e.getKeyCode()) {
            case KeyEvent.VK_C:
                newPixels = imgManipulator.rotate();
                break;
            case KeyEvent.VK_R:
                newPixels = imgManipulator.filter(0x00FF0000);
                break;
            case KeyEvent.VK_G:
                newPixels = imgManipulator.filter(0x0000FF00);
                break;
            case KeyEvent.VK_B:
                newPixels = imgManipulator.filter(0x000000FF);
                break;
            case KeyEvent.VK_H:
                newPixels = imgManipulator.flipHorizontal();
                break;
            case KeyEvent.VK_V:
                newPixels = imgManipulator.flipVertical();
                break;
            case KeyEvent.VK_M:
                newPixels = imgManipulator.filter(0x00FF00FF);
                break;
            case KeyEvent.VK_Y:
                newPixels = imgManipulator.filter(0x00FFFF00);
                break;
            case KeyEvent.VK_T:
                newPixels = imgManipulator.filter(0x0000FFFF);
                break;
            case KeyEvent.VK_I:
                newPixels = imgManipulator.invert();
                break;
            case KeyEvent.VK_W:
                newPixels = imgManipulator.grayScale();
                break;
            case KeyEvent.VK_S:
                this.switchImage();
                break;
            default:
                System.out.println("The key " + e.getKeyChar() + " is not tied to an action.");
                break;
        } // end switch

        if (newPixels.length > 0)
            this.updateDisplayedImage(newPixels);
    } // end keyPressed

    @Override
    public void keyReleased(KeyEvent e) {
        // not used
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not used
    }

} // end class
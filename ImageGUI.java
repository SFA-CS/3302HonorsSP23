import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Dimension;;

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
    private String[] ImagesAdded = new String[] { "buzz", "billy", "gru" };
    private String[] picsNonUsed = new String[] {};
    private String[] picsUsed = new String[] {"all images added"};

    // index of current image being display
    private int currentImageIndex = 0;
    
    private ImageManipulator imgManipulator;
    GridBagConstraints c = new GridBagConstraints();

    JLabel menu = new JLabel("Menu");
    JButton add = new JButton("add");
    JButton remove = new JButton("remove all");
    JButton save = new JButton("save");
    JButton exit = new JButton("exit");

    JButton imageRotate = new JButton("rotate image");
    JButton imageFlipV = new JButton("flip image vertically");
    JButton imageFlipH = new JButton("flip image horizontally");
    JButton imageInvert = new JButton("invert image");
    JButton imageGrey = new JButton("greyscale");
    JButton imageRed = new JButton("filter red");
    JButton imageGreen = new JButton("filter green");
    JButton imageBlue = new JButton("filter blue");
    
    JComboBox picList = new JComboBox(picsUsed);

    ImageIcon buzz = new ImageIcon("buzz.png");
    ImageIcon billy = new ImageIcon("billy.png");
    ImageIcon gru = new ImageIcon("gru.png");

    JButton buzzB = new JButton(resize(buzz, 100, 100));
    JButton billyB = new JButton(resize(billy, 100, 100));
    JButton gruB = new JButton(resize(gru, 100, 100));


    /*************************MAIN**********************
     * Simply create the object to display.
     * @param avg - not used
     * @throws IOException
     */
    public static void main(String avg[]) throws IOException {
        new ImageGUI();
    }

    public static ImageIcon resize(ImageIcon image, int width, int height){
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image.getImage(), 0, 0, width, height, null);
        g2d.dispose();
        return new ImageIcon(bi);
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

        c.fill = GridBagConstraints.BOTH;

        //menu
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        this.add(menu, c);

        //combobox
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        this.add(picList, c);
        add.addActionListener(this);

        //add
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 2;
        c.gridy = 0;
        this.add(add, c);
        add.addActionListener(this);

        //remove
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 3;
        c.gridy = 0;
        this.add(remove, c);
        remove.addActionListener(this);

        //save
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 4;
        c.gridy = 0;
        this.add(save, c);
        save.addActionListener(this);

        //exit
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 5;
        c.gridy = 0;
        this.add(exit, c);
        exit.addActionListener(this);

        //rotate button
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        this.add(imageRotate, c);
        imageRotate.addActionListener(this);

        //flip vertically
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 2;
        this.add(imageFlipV, c);
        imageFlipV.addActionListener(this);

        //flip horizontal
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 3;
        this.add(imageFlipH, c);
        imageFlipH.addActionListener(this);

        //invert image
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 4;
        this.add(imageInvert, c);
        imageInvert.addActionListener(this);

        //greyscale
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 5;
        this.add(imageGrey, c);
        imageGrey.addActionListener(this);

        //filter red
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 6;
        this.add(imageRed, c);
        imageRed.addActionListener(this);

        //filter green
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 7;
        this.add(imageGreen, c);
        imageGreen.addActionListener(this);

        //filter blue
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 8;
        this.add(imageBlue, c);
        imageBlue.addActionListener(this);

        if(ImagesAdded.length > 0){
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridheight = 2;
            c.gridx = 5;
            c.gridy = 1;
            for(int i = 0; i < ImagesAdded.length; i++){
                if(ImagesAdded[i].equals("buzz")){
                    this.add(buzzB, c);
                    buzzB.addActionListener(this);
                }
                else if(ImagesAdded[i].equals("billy")){
                    this.add(billyB, c);
                    billyB.addActionListener(this);
                }
                else if(ImagesAdded[i].equals("gru")){
                    this.add(gruB, c);
                    gruB.addActionListener(this);
                }
                c.gridy = c.gridy + 2;
            }
        }
        
        // display the image
        this.displayImage(this.currentImage);

    }

    public void actionPerformed(ActionEvent e){
        //image manipulation buttons:
        int[][] newPixels = new int[0][0];
        if(e.getSource() == imageRotate){
            imgManipulator.setImage(currentImage);
            newPixels = imgManipulator.rotate();
        }
        if(e.getSource() == imageFlipV){
            imgManipulator.setImage(currentImage);
            newPixels = imgManipulator.flipVertical();
        }
        if(e.getSource() == imageFlipH){
            imgManipulator.setImage(currentImage);
            newPixels = imgManipulator.flipHorizontal();
        }
        if(e.getSource() == imageInvert){
            imgManipulator.setImage(currentImage);
            newPixels = imgManipulator.invert();
        }
        if(e.getSource() == imageGrey){
            imgManipulator.setImage(currentImage);
            newPixels = imgManipulator.grayScale();
        }
        if(e.getSource() == imageRed){
            imgManipulator.setImage(currentImage);
            newPixels = imgManipulator.filter(0x00FF0000);
        }
        if(e.getSource() == imageGreen){
            imgManipulator.setImage(currentImage);
            newPixels = imgManipulator.filter(0x0000FF00);
        }
        if(e.getSource() == imageBlue){
            imgManipulator.setImage(currentImage);
            newPixels = imgManipulator.filter(0x000000FF);
        }

        //button icons:
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

        //menu buttons:
        if(e.getSource() == exit){
            super.dispose();
        }
        if(e.getSource() == save){
            
        }
        if(e.getSource() == remove){
            picList.removeAllItems();
            picList.addItem("buzz");
            picList.addItem("billy");
            picList.addItem("gru");

            String[] newArr = new String[] {};
            ImagesAdded = newArr;
            
            String[] otherArr = new String[] {"buzz", "billy", "gru"};
            picsNonUsed = otherArr;
        }
        if(e.getSource() == add){
            String selected = (String)picList.getSelectedItem();
            if(!selected.equals("all images added")){
                
                //update list of images displayed
                String[] newArr = new String[ImagesAdded.length+1];
                for(int i = 0; i < ImagesAdded.length; i++){
                    newArr[i] = ImagesAdded[i];
                }
                newArr[ImagesAdded.length+1] = selected;
                ImagesAdded = newArr;
                
                //update list of images not displayed
                String[] otherArr = new String[picsNonUsed.length-1];
                int index = 0;
                for(int i = 0; i < picsNonUsed.length; i++){
                    if(!picsNonUsed[i].equals(selected)){
                        otherArr[index] = picsNonUsed[i];
                    }
                }
                picsNonUsed = otherArr;
                
                //update combobox - NOT WORKING
                picList.removeAllItems();
                for(int i = 0; i < picsNonUsed.length; i++){
                    picList.addItem(picsNonUsed[i]);
                }

                //if there is nothing left in the combobox...
                if(picList.getItemCount() == 0){
                    picList.addItem("all images added");
                }
                
            }
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
        this.setSize(700, 520);
        // add the image to the JFrame
        imageHolder = new JLabel(new ImageIcon(newImage));
        
        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 8;
        c.gridwidth = 4;
        
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
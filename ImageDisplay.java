import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageDisplay {
    static JPanel imagePanel; // holds list of images on the right side in a scrollable panel
    static JLabel centerLabel; // holds current image in the center
    static ImageList imageList; // a linked list of all images
    static JFrame frame; // the frame for the application
    static ImageManipulator imageManipulator = new ImageManipulator(); // used to alter image pixels
    static BufferedImage currentImage; // currrent image being displayed
    static int currentImageIndex = 0; // index for the current image in the list being used

    // buttons names on the left along with corresponding method calls
    static String[] leftButtonNames = new String[] { "rotate", "flip vertically", "flip horizontally", "invert colors",
            "greyscale", "filter red", "filer green", "filter blue", "filter magenta", "filter yellow",
            "filter teal", "filter by color" };
    static Runnable[] leftButtonMethods = new Runnable[] {
            () -> rotate(),
            () -> flipVertical(),
            () -> flipHorizontal(),
            () -> invert(),
            () -> greyscale(),
            () -> filterRed(),
            () -> filterGreen(),
            () -> filterBlue(),
            () -> filterMagenta(),
            () -> filterYellow(),
            () -> filterTeal(),
            () -> filterColor()
    };
    // buttons name for the top panel and the corresponding method calls
    static String[] topButtonNames = new String[] { "add", "remove", "remove all", "exit" };
    static Runnable[] topButtonMethods = new Runnable[] { () -> addImage(), () -> removeImage(),
            () -> removeAllImages(), () -> exit() };

    // ==================MAIN=======================
    public static void main(String[] args) throws IOException {
        // add default image
        imageList = new ImageList();
        imageList.add(0, ImageIO.read(new File("gru.png")));
        imageList.add(1, ImageIO.read(new File("buzz.png")));
        imageList.add(2, ImageIO.read(new File("billy.png")));

        // makes app thread safe and starts the app
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    // ---------------------getImageIcon----------------------------
    // Helper method to turned a buffered image into a scaled icon
    private static ImageIcon getImageIcon(BufferedImage image, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(image);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Image Manipulator with Linked List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // add buttons on the left side
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(leftButtonNames.length, 1));
        for (int i = 0; i < leftButtonNames.length; i++) {
            JButton button = new JButton(leftButtonNames[i]);
            Runnable methodToCall = leftButtonMethods[i];
            button.addActionListener(e -> methodToCall.run());
            leftPanel.add(button);
        }

        // add buttons buttons to top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // label just for spacing at the top
        JLabel topLeftLabel = new JLabel("         ", SwingConstants.CENTER);
        topLeftLabel.setPreferredSize(new Dimension(120, 30));
        topPanel.add(topLeftLabel, BorderLayout.WEST);

        // add buttons at the top
        JPanel topButtonsPanel = new JPanel();
        topButtonsPanel.setLayout(new GridLayout(1, topButtonNames.length));
        for (int i = 0; i < topButtonNames.length; i++) {
            JButton button = new JButton(topButtonNames[i]);
            Runnable methodToCall = topButtonMethods[i];
            button.addActionListener(e -> methodToCall.run());
            topButtonsPanel.add(button);
        }
        topPanel.add(topButtonsPanel, BorderLayout.CENTER);

        // Scrollable panel with image icons on the right
        imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(0, 1));
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        for (int i = 0; i < imageList.size(); i++) {
            BufferedImage currentImage = imageList.get(i);
            ImageIcon icon = getImageIcon(currentImage, 150, 150);
            JButton button = new JButton(icon);
            button.putClientProperty("location", i);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setPreferredSize(new Dimension(150, 150));
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    displayImage((Integer) ((JButton) e.getSource()).getClientProperty("location"));
                }
            });
            imagePanel.add(button);
        }

        // JLabel with an first image icon in the center
        ImageIcon centerIcon = new ImageIcon(imageList.get(0));
        centerLabel = new JLabel(centerIcon);
        currentImage = imageList.get(0);
        currentImageIndex = 0;

        // add all componenets to the main display
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.EAST);
        mainPanel.add(centerLabel, BorderLayout.CENTER);

        // show the frame
        frame.getContentPane().add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void callMethod() {
        System.out.println("Call Method");
    }

    // -----------------------exit---------------------
    // called by exit button to close the app
    private static void exit() {
        frame.dispose();
    }

    // -------------------displayImage-------------------
    // This method display the image clicked on the right scroll panel
    // in the middle for manipulation.
    private static void displayImage(int index) {
        // get the image clicked on from the list
        BufferedImage newImage = imageList.get(index);
        displayImage(newImage);
        currentImageIndex = index;
    }

    // displays the given image in the center of the frame
    private static void displayImage(BufferedImage newImage) {
        centerLabel.setVisible(false);

        // change the image icon and show the image
        centerLabel.setIcon(new ImageIcon(newImage));
        centerLabel.setVisible(true);

        currentImage = newImage;
    }

    // ------------------addImage-------------------
    // This method opens a dialogue box for the user to select an image
    // and adds it to the end at the panel on the left.
    private static void addImage() {
        // TODO: 1) Open dialogue box for user to select image 
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        final File file = chooser.getSelectedFile();
        //String filename = file.getAbsolutePath();
        
        BufferedImage newImage = null;
        try{
            newImage = (ImageIO.read(file));
            newImage = imageResize(newImage);

            // 2) image should be added to end of imageList
            imageList.add(imageList.size(), newImage);
        }
        catch (IOException ex) {}

        // TODO: Icon correspondonging to image should be added to the end of
        // imagePanel. See code about line 108 on how to add icon.
        if(newImage != null){
        ImageIcon newIcon = getImageIcon(newImage, 150, 150);
        JButton button = new JButton(newIcon);
        button.putClientProperty("location", imageList.size()-1);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 150));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayImage((Integer) ((JButton) e.getSource()).getClientProperty("location"));
            }
        });
        imagePanel.add(button);
        imagePanel.updateUI(); //updates icons
        }
        else{
            // TODO: Add exception handling for selecting a non-valid file which display a
            // JOptionPane dialogue box. See errorMessage() for exampleerrorMessage();
            JOptionPane.showMessageDialog(frame, "File is not valid.", "File Note Valid", JOptionPane.ERROR_MESSAGE);
        }
        displayImage(imageList.size()-1); //sets display to new image
    }

    public static BufferedImage imageResize(BufferedImage img){
        int w;
        int h;
        if(img.getWidth() >= img.getHeight()){
            w = 450;
            h = img.getHeight()/(img.getWidth()/w);
        }
        else{
            h = 450;
            w = img.getWidth()/(img.getHeight()/h);
        }
        Image tmp = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage newImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = newImg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return newImg;
    }

    // -----------------removeImage------------------
    // This method removes the currently displayed image from the imageList
    // and the scrollable pane.
    private static void removeImage() {
        // TODO: 1) Removes the currently displayed image form the imageList and the
        // icon from the imagePanel 2) show the first image in the image list on the
        // imagePanel
        // TODO: 3) if no images are in the list, just display a message dialgoue
        // notificying the user
        if(imageList.isEmpty()){
            errorMessage();
        }
        else if(imageList.size() == 1){
            removeAllImages();
        }
        else{
            imageList.remove(currentImageIndex);
            imagePanel.remove(currentImageIndex);
            imagePanel.revalidate();
            imagePanel.repaint();
            
            displayImage(0); //displays image first in imageList

            imagePanel.updateUI(); //updates icons
        }
        
    }

    // ------------------removeAll-------------------
    // This method removes all image from the image list and the image panel.
    private static void removeAllImages() {
        // TODO: 1) Remove all images from the image list 2) image all icons from the
        // imagePanel

        imageList.removeAll(); //removes all images from imageList
        imagePanel.removeAll(); //removes all icons from imagePanel

        currentImage = null;
        currentImageIndex = -1;

        imagePanel.updateUI(); //updates icons
        centerLabel.setVisible(false); //gets rid of center image
    }

    private static void updateDisplayedImage(int[][] pixels) {
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
        displayImage(newImage);
    }

    //----------------------errorMessage----------------
    // notify the user that no images are currently in use
    private static void errorMessage() {
        JOptionPane.showMessageDialog(frame, "No image currently in use.", "Image Not Presenent",    JOptionPane.ERROR_MESSAGE);
    }
    
    // ----------------------rotate-----------------
    // rotates the current image
    private static void rotate() {
        if (currentImage == null)
            errorMessage();
        else {
            imageManipulator.setImage(currentImage);
            int[][] nexPixels = imageManipulator.rotate();
            updateDisplayedImage(nexPixels);
        }
    }

    // ----------------------flipsVertical-----------------
    // flips the image vertically
    private static void flipVertical() {
        if (currentImage == null)
            errorMessage();
        else {
            imageManipulator.setImage(currentImage);
            int[][] nexPixels = imageManipulator.flipVertical();
            updateDisplayedImage(nexPixels);
        }
    }

    // ----------------------flipsHorizontal-----------------
    // flips the image horizontally
    private static void flipHorizontal() {
        //TODO: implement, similar to flipVertical
        if (currentImage == null)
            errorMessage();
        else {
            imageManipulator.setImage(currentImage);
            int[][] nexPixels = imageManipulator.flipHorizontal();
            updateDisplayedImage(nexPixels);
        }
    }

    private static void invert() {
        //TODO: implement, similar to flipVertical
        if (currentImage == null)
            errorMessage();
        else {
            imageManipulator.setImage(currentImage);
            int[][] nexPixels = imageManipulator.invert();
            updateDisplayedImage(nexPixels);
        }
    }

    private static void greyscale() {
        //TODO: implement, similar to flipVertical
        if (currentImage == null)
            errorMessage();
        else {
            imageManipulator.setImage(currentImage);
            int[][] nexPixels = imageManipulator.grayScale();
            updateDisplayedImage(nexPixels);
        }
    }

    private static void filterRed() {
        if (currentImage == null)
            errorMessage();
        else {
            imageManipulator.setImage(currentImage);
            int[][] newPixels = imageManipulator.filterRed();
            updateDisplayedImage(newPixels);
        }
    }

    private static void filterGreen() {
        //TODO: implement, similar to filterRed
        if (currentImage == null)
            errorMessage();
        else {
            imageManipulator.setImage(currentImage);
            int[][] newPixels = imageManipulator.filterGreen();
            updateDisplayedImage(newPixels);
        }
    }

    private static void filterBlue() {
        //TODO: implement, similar to filterRed
        if (currentImage == null)
            errorMessage();
        else {
            imageManipulator.setImage(currentImage);
            int[][] newPixels = imageManipulator.filterBlue();
            updateDisplayedImage(newPixels);
        }
    }
    
    private static void filterMagenta() {
        //TODO: implement, similar to filterRed
        if (currentImage == null)
            errorMessage();
        else {
            imageManipulator.setImage(currentImage);
            int[][] newPixels = imageManipulator.filterMagenta();
            updateDisplayedImage(newPixels);
        }
    }

    private static void filterYellow() {
        //TODO: implement, similar to filterRed
        if (currentImage == null)
            errorMessage();
        else {
            imageManipulator.setImage(currentImage);
            int[][] newPixels = imageManipulator.filterYellow();
            updateDisplayedImage(newPixels);
        }
    }

    private static void filterTeal() {
        //TODO: implement, similar to filterRed
        if (currentImage == null)
            errorMessage();
        else {
            imageManipulator.setImage(currentImage);
            int[][] newPixels = imageManipulator.filterTeal();
            updateDisplayedImage(newPixels);
        }
    }

    private static void filterColor() {
        //TODO: This one is a little tricky. Here is the idea.
        /* 1) Open a color choice dialogue box. Let's just do a simple RGB one. 
           https://www.geeksforgeeks.org/java-swing-jcolorchooser-class/#
           2) Get the selected color from the panel
           3) Convert the color to a mask and call the filter method with
           mask parameter from the image manipulator class
        */
        Color initialColor = Color.RED;
        Color color = JColorChooser.showDialog(centerLabel, "Select a color", initialColor);
        
        int mask = color.getRGB();

        imageManipulator.setImage(currentImage);
            int[][] newPixels = imageManipulator.filter(mask);
            updateDisplayedImage(newPixels);
    }
    
}

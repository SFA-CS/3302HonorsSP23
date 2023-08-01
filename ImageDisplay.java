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
    //TODO (Assignment 2): Toggle the comments on the next two lines
    //static ImageList imageList = new ImageList(); // a linked list of all iimages
    static GenericList<BufferedImage> imageList = new GenericList<BufferedImage>();
    static JFrame frame; // the frame for the application
    static ImageManipulator imageManipulator = new ImageManipulator(); // used to alter image pixels
    static BufferedImage currentImage; // currrent image being displayed
    static int currentImageIndex = 0; // index for the current image in the list being used
    static ColorChooser colorChooser;

    // buttons names on the left along with corresponding method calls
    static String[] leftButtonNames = new String[] { "rotate", "flip vertically", "flip horizontally", "invert colors",
            "grayscale", "filter red", "filter green", "filter blue", "filter magenta", "filter yellow",
            "filter teal", "filter by color" };
    static Runnable[] leftButtonMethods = new Runnable[] {
            () -> rotate(),
            () -> flipVertical(),
            () -> flipHorizontal(),
            () -> invert(),
            () -> grayscale(),
            () -> filter(0x00FF0000), // red
            () -> filter(0x0000FF00), // green
            () -> filter(0x000000FF), // blue
            () -> filter(0x00FF00FF), // magenta
            () -> filter(0x00FFFF00), // yellow
            () -> filter(0x0000FFFF), // teal
            () -> filterColor()
    };
    // buttons name for the top panel and the corresponding method calls
    static String[] topButtonNames = new String[] { "add", "remove", "remove all", "exit" };
    static Runnable[] topButtonMethods = new Runnable[] { () -> addImage(), () -> removeImage(),
            () -> removeAllImages(), () -> exit() };

    // ==================MAIN=======================
    public static void main(String[] args) throws IOException {
        // add default image
        //imageList = new ImageList();
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
        addImagesToPanel();

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

    private static void addImagesToPanel() {
        imagePanel.removeAll();
        imagePanel.setVisible(false);
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
        imagePanel.setVisible(true);
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
        // open the file selector and get the selected file
        ImageFileSelector fileSelector = new ImageFileSelector(frame);
        File fileSelected = fileSelector.getSelectedFile();

        // check to see if a file was choosen
        if (fileSelected != null) {
            try {
                // add the file to the fornt of the list
                imageList.add(0, ImageIO.read(fileSelected));
                // add the new image to top of panel
                addImagesToPanel();
                // display the new image on center
                displayImage(0);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }     
        
    }

    // -----------------removeImage------------------
    // This method removes the currently displayed image from the imageList
    // and the scrollable pane.
    private static void removeImage() {
        if (!imageList.isEmpty()) {
            imageList.remove(currentImageIndex);
            
            // remove image from panel
            addImagesToPanel();
            // display the first image on left in the center
            if (imageList.isEmpty())
                removeAllImages();
            else
                displayImage(0);
            
        } else {
            JOptionPane.showMessageDialog(frame, "No images currently in use.", "Cannot remove image",
                JOptionPane.ERROR_MESSAGE);
        }
        
    }

    // ------------------removeAll-------------------
    // This method removes all image from the image list and the image panel.
    private static void removeAllImages() {
        imageList.removeAll();
        addImagesToPanel();
        centerLabel.setVisible(false);
        currentImage = null;
        currentImageIndex = -1;
    }

    /**
     * Update the currently displayed image with the new pixels.
     * 
     * @param pixels
     */
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

    // ----------------------errorMessage----------------
    // notify the user that no images are currently in use
    private static void errorMessage() {
        JOptionPane.showMessageDialog(frame, "No image currently in use.", "Image Not Present",
                JOptionPane.ERROR_MESSAGE);
    }

    // ----------------------rotate-----------------
    // rotates the current image 90 degrees clockwise
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
        if (currentImage == null)
            errorMessage();
        else {
            imageManipulator.setImage(currentImage);
            int[][] nexPixels = imageManipulator.flipHorizontal();
            updateDisplayedImage(nexPixels);
        }
    }

    /**
     * Invert the pixels in the current image.
     * i.e. flip the 1's and 0's in the color bits
     */
    private static void invert() {
        if (currentImage == null)
            errorMessage();
        else {
            imageManipulator.setImage(currentImage);
            int[][] nexPixels = imageManipulator.invert();
            updateDisplayedImage(nexPixels);
        }
    }

    /**
     * Take the current image and convert to gray scale.
     */
    private static void grayscale() {
        if (currentImage == null)
            errorMessage();
        else {
            imageManipulator.setImage(currentImage);
            int[][] nexPixels = imageManipulator.grayScale();
            updateDisplayedImage(nexPixels);
        }
    }

    /**
     * Filter the image RGB color according to the mask
     * and display the result.
     * 
     * @param mask - bit mask for color
     */
    private static void filter(int mask) {
        if (currentImage == null)
            errorMessage();
        else {
            imageManipulator.setImage(currentImage);
            int[][] newPixels = imageManipulator.filter(mask);
            updateDisplayedImage(newPixels);
        }
    }

    /**
     * Open a color chooser and have it call
     * filter by color when done.
     */
    private static void filterColor() {
        // when closed the color choose will call filter by color
        colorChooser = new ColorChooser(() -> filterByColor());
        colorChooser.createAndShowGUI();
    }

    /**
     * Filter the image by the color choosen by the
     * coolor chooser.
     */
    private static void filterByColor() {
        filter(colorChooser.getColor());
    }

}

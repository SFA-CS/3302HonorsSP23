import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestLayout {
    static JPanel imagePanel;   // holds list of images on the right side in a scrollable panel
    static JLabel centerLabel;  // holds current image in the center
    static ImageList imageList; // a linked list of all iimages
    static JFrame frame;        // the frame for the application
    
    // buttons names on the left along with corresponding method calls
    static String[] leftButtonNames = new String[]{"rotate", "flip vertically", "flip horizontally", "invert colors", "greyscale", "filter red", "filer green", "filter blue", "filter magenta", "filter yellow", "filter magenta"};
    
    // buttons name for the top panel and the corresponding method calls
    static String[] topButtonNames = new String[]{"add", "remove", "remove all", "exit"};
    static Runnable[] topButtonMethods = new Runnable[]{() -> addImage(), ()-> removeImage(), () -> removeAllImages(), () -> exit()};
    
    //==================MAIN=======================
    public static void main(String[] args) throws IOException {
        // add default image
        imageList = new ImageList();
        imageList.add(0, ImageIO.read(new File("gru.png")));
        imageList.add(1, ImageIO.read(new File("buzz.png")));
        imageList.add(2, ImageIO.read(new File("billy.png")));
        
        // makes app thread safe and starts the app
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }


    private static ImageIcon getImageIcon(BufferedImage image, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(image);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
    

    private static void createAndShowGUI() {
        frame = new JFrame("Button Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // add buttons on the left side
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(leftButtonNames.length, 1));
        for (int i = 0; i < leftButtonNames.length; i++) {
            JButton button = new JButton(leftButtonNames[i]);
            leftPanel.add(button);
        }

        // add buttons buttons to top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // label just for spacing at the top
        JLabel topLeftLabel = new JLabel("         ", SwingConstants.CENTER);
        topLeftLabel.setPreferredSize(new Dimension(100, 30));
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
                   displayImage((Integer)((JButton)e.getSource()).getClientProperty("location"));
                }
             });
            imagePanel.add(button);
        }

        // JLabel with an image icon in the center
        ImageIcon centerIcon = new ImageIcon("gru.png");
        centerLabel = new JLabel(centerIcon);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.EAST);
        mainPanel.add(centerLabel, BorderLayout.CENTER);

        frame.getContentPane().add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void callMethod() {
        System.out.println("Call Method");
    }

    
    //-----------------------exit---------------------
    // called by exit button to close the app
    private static void exit() {
        frame.dispose();
    } 

    //-------------------displayImage-------------------
    // This method display the image clicked on the right scroll panel
    // in the middle for manipulation.
    private static void displayImage(int index) {
        // get the image clicked on from the list
        BufferedImage newImage = imageList.get(index);
        centerLabel.setVisible(false);

        // change the image icon and show the image
        centerLabel.setIcon(new ImageIcon(newImage));
        centerLabel.setVisible(true); 
    }

    //------------------addImage-------------------
    // This method opens a dialogue box for the user to select an image
    // and adds it to the end at the panel on the left.
    private static void addImage() {
        //TODO: 1) Open dialogue box for user to select image 2) image should be added to end of imageList 3) icon should be added to the end of imagePanel.
        //TODO: Add exception handling. See code about line 90 on how to add icon 
    }

    //-----------------removeImage------------------
    // This method removes the currently displayed image from the imageList
    // and the scrollable pane.
    private static void removeImage() {
        //TODO: 1) Removes the currently displayed image form the imageList and the icon from the imagePanel 2) show the first image in the image list on the imagePanel
        //TODO: 3) if no images are in the list, just display a message dialgoue notificying the user
    }

    //------------------removeAll-------------------
    // This method removes all image from the image list and the image panel.
    private static void removeAllImages() {
        //TODO: 1) Remove all images from the image list 2) image all icons from the imagePanel
    }

}

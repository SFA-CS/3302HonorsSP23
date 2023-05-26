/**
 * This class shows a color selecltion panel.
 * The class tracks the currently selected color.
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.*;

public class ColorChooser extends JPanel implements ChangeListener {

    private JColorChooser colorChooser; // color chooser which display panel for selection  
    private JLabel label;               // label showing the currently selected color
    private Runnable onCloseMethod;     // method to call when panel is closed
    private Color color;                // currently selected color

    //===============Constructors====================

    /**
     * Constructor the color selection panel
     * @param onCloseMethod - method to call when panel is closed
     */
    public ColorChooser(Runnable onCloseMethod) {
        this();
        this.onCloseMethod = onCloseMethod;
    }

    private ColorChooser() {
        super(new BorderLayout());
        // Set up the Label at the top of the window
        label = new JLabel("Pick a Color!", JLabel.CENTER);

        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.add(label, BorderLayout.CENTER);
        bannerPanel.setBorder(BorderFactory.createTitledBorder("Image Filter"));

        colorChooser = new JColorChooser(label.getForeground());
        this.color = colorChooser.getColor();
        
        // remove all panels but the RGB panel
        AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
        for (AbstractColorChooserPanel p : panels) {
            String displayName = p.getDisplayName();
            if (displayName.equals("HSV") || displayName.equals("HSL") || displayName.equals("CMYK") || displayName.equals("Swatches"))
                colorChooser.removeChooserPanel(p);
        }

        colorChooser.getSelectionModel().addChangeListener(this);
        colorChooser.setBorder(BorderFactory.createTitledBorder("Choose Text Color"));

        add(bannerPanel, BorderLayout.CENTER);
        add(colorChooser, BorderLayout.PAGE_END);
    }

    /**
     * Access method to get the color choosen
     * @return - the last color selected by the panel (as an integer - RGB mode)
     */
    public int getColor() {
        //System.out.println("getColor: " + this.color.getRGB());
        return this.color.getRGB();
    }

    /**
     * Called when state of the panel is changed, i.e.
     * a new color is selected. The slected color is
     * recorded and the lable is changed to refeclt the color selection.
     */
    public void stateChanged(ChangeEvent e) {
        this.color = colorChooser.getColor();
        label.setForeground(color);
    }

    // Create the GUI and show it. For thread safety,
    // this method should be invoked from the
    // event-dispatching thread.
    public void createAndShowGUI() {

        // Create and set up the window.
        JFrame frame = new JFrame("ColorChooserDemo");

        // set default close operation of the window.
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // call the appropriate method when the selector is colosed
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {

                onCloseMethod.run();
            }
        });
        
        // display the selector
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    }

}

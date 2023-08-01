/**
 * This is a class to allow the user to selct an image file
 * using a java file chooser.
 */
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

public class ImageFileSelector {
    private File file; // file selected

    /**
     * This constructor creates an image selctor
     * which is only allowed to select images.
     * 
     * @param parentFrame - main frame for the program
     */
    public ImageFileSelector(JFrame parentFrame) {
        this.createWindow(parentFrame);
    }

    private void createWindow(JFrame frame) {
        String userDirLocation = System.getProperty("user.dir");
        File userDir = new File(userDirLocation);
        JFileChooser fileChooser = new JFileChooser(userDir);
        fileChooser.addChoosableFileFilter(new ImageFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);
        int option = fileChooser.showOpenDialog(frame);

        // check to see if user approved the choice
        if (option == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        } else {
            file = null;
        }
    }

    /**
     * Simple getter that returns the latest selected file
     * or null if operation was canceled
     * @return - file is image file selected or null
     * if operation is canceled
     */
    public File getSelectedFile() {
        return this.file;
    }

}

/**
 * Filter so the above only selects images
 */
class ImageFilter extends FileFilter {
    public final static String JPEG = "jpeg";
    public final static String JPG = "jpg";
    public final static String GIF = "gif";
    public final static String TIFF = "tiff";
    public final static String TIF = "tif";
    public final static String PNG = "png";

    /**
     * Accepts any directory and
     * file that is an image.
     */
    @Override
    public boolean accept(File f) {
        // directories are a-okay
        if (f.isDirectory()) {
            return true;
        }

        // see if the file extension is acceptable
        String extension = getExtension(f);
        if (extension != null) {
            return (extension.equals(TIFF) ||
                    extension.equals(TIF) ||
                    extension.equals(GIF) ||
                    extension.equals(JPEG) ||
                    extension.equals(JPG) ||
                    extension.equals(PNG));
        } else
            return false;
    }

    @Override
    public String getDescription() {
        return "Image Only";
    }

    /**
     * Takes a file and returns a string for the file extension.
     * 
     * @param f - file selected
     * @return - extension of the file as a String
     */
    String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}

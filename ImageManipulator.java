import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Author: JJB
 * CSCI 3302 Seciton 001
 * This class is used to perform mainpulations of
 * a double array of integers. The integers represent
 * pixels of a image.
 */
public class ImageManipulator {
    // pixels making up an image
    private int[][] pixels;

    public ImageManipulator() {
        // nothing to do but what the super constructor does
    }

    /**
     * This method sets the pixels given the image
     * file.
     * @param image - given a image
     * The pixels contain the color information for the given.
     */
    public void setImage(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        this.pixels = new int[w][h];

        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                this.pixels[x][y] = image.getRGB(x, y);
    }

    
    /**********************************************
     * This method rotates the currently image used by 90 degree
     * in a clockwise fashion.
     * Pre : given an implicit image display (this)
     * Post: the image is rotated 90 degree clockwise
     */
    public int[][] rotate() {
        // first row becomes last column
        // second row becomes second to last column
        int w = pixels.length;
        int h = pixels[0].length;

        int[][] newPixels = new int[h][w];
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int color = this.pixels[x][y];
                newPixels[h - y - 1][x] = color;
            }
            
        
        return newPixels;
    }

    /************************************
     * This method takes the current image used and finds the grayscale equivalent
     * image.
     * Pre : the implicit image display is given
     * Post: a gray scale version of the current image is displayed
     */
    public int[][] grayScale() {
        int w = pixels.length;
        int h = pixels[0].length;

        int[][] newPixels = new int[w][h];
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                // average the red, blue, and green values
                // of the pixel and set the corresponding
                // grayscale pixel to this average value
                Color color = new Color( this.pixels[x][y]);

                int red = color.getRed();
                int blue = color.getBlue();
                int green = color.getGreen();
                int gray = (red + blue + green) / 3;
                
                newPixels[x][y] = new Color(gray,gray,gray).getRGB();
            }

        return newPixels;
    }

    /******************************************
     * This method inverts the color of the currently displayed image.
     * That is, we take the current RGB value of the color for the pixel
     * and form a new image where the valuee is 255-R, 255-G, 255-B.
     * Pre : given the implicit image display
     * Post: an image with the inverted colors of the current image is displayed
     */
    public int[][] invert() {
        int w = pixels.length;
        int h = pixels[0].length;

        int[][] newPixels = new int[w][h];
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int color = this.pixels[x][y];
                // subtracting the RGB value from 255 (= 0b11111111)
                // is the same as flipping the bits!
                newPixels[x][y] = ~color;
            }

        return newPixels;
    }

    /***************************************
     * This method displays a version of the current image which
     * is flipped horizontally. That is, the first column pixels are
     * swapped with the last column, the 2nd column pixels are swapped
     * with the second to last columns, etc.
     * Pre : given the implicit ImageDisplay
     * Post: a version of the current image that is flipped horizontally is
     * displayed
     */
    public int[][] flipHorizontal() {
        int w = pixels.length;
        int h = pixels[0].length;

        int[][] newPixels = new int[w][h];

        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int color = this.pixels[x][y];
                newPixels[w - x - 1][y] = color;
            }
        return newPixels;
    }

    /***************************************
     * This method displays a version of the current image which
     * is flipped vertically. That is, the first row pixels are
     * swapped with the last row, the 2nd row pixels are swapped
     * with the second to last row, etc.
     * Pre : given the implicit ImageDisplay
     * Post: a version of the current image that is flipped vertically is displayed
     */
    public int[][] flipVertical() {
        int w = pixels.length;
        int h = pixels[0].length;

        int[][] newPixels = new int[w][h];

        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int color = this.pixels[x][y];
                newPixels[x][h - y - 1] = color;
            }
        return newPixels;
    }

    /***************************************
     * This method displays a filtered version of the current image.
     * That is, a mask is applied to the pixels to highlight specific
     * colors.
     * 
     * Pre : given the mask as a positive integer
     * Post: a version of the current image that is filtered is displayed
     */
    public int[][] filter(int mask) {
        int w = pixels.length;
        int h = pixels[0].length;

        int[][] newPixels = new int[w][h];
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int color = this.pixels[x][y];
                newPixels[x][y] = mask & color;
            }

        return newPixels;
    }

    public int[][] filterRed() {
        return this.filter(0x00FF0000);
    }

    public int[][] filterGreen() {
        return this.filter(0x0000FF00);
    }

    public int[][] filterBlue() {
        return this.filter(0x000000FF);
    }

    public int[][] filterMagenta() {
        return this.filter(0x00FF00FF);
    }            

    public int[][] filterYellow() {
        return this.filter(0x00FFFF00);
    }

    public int[][] filterTeal() {
        return this.filter(0x0000FFFF);
    }
            

} // end class
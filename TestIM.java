/**
 * This is used to test the image manipulator file.
 * Author: JJB
 * Date: 5/27/2023
 */
public class TestIM {
    public static void main(String[] args) {
        int[][] pixels = {{1,2,3,4}, {5,6,7,8}, {9,10,11,12}};
        ImageManipulator im = new ImageManipulator(pixels);
        System.out.println("Starting Image: ");
        displayResults(pixels, false);
        
        System.out.println("After Rotation:");
        displayResults(im.rotate(), false);
                
        System.out.println("After Flip Horizontal:");
        displayResults(im.flipHorizontal(), false);

        System.out.println("After Flip Vertical:");
        displayResults(im.flipVertical(), false);

        System.out.println("After inversion:");
        displayResults(im.invert(), true);
        
    }

    private static void displayResults(int[][] array, boolean hex) {
        String s = "";
        for (int y = 0; y < array[0].length; y++) {
            for (int x = 0; x < array.length; x++) {
                String val = hex ? Integer.toHexString(array[x][y]) : "" + array[x][y];
                s = s + val + "\t";
            }
            s = s + "\n";
        }
        System.out.println(s);
    }
}

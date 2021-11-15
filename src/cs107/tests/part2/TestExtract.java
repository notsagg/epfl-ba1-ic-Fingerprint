import cs107.Fingerprint;
import cs107.Helper;
import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

public final class TestExtract {
    public static void main(String[] args) {
        // 1. simple skeleton test case
            // a. construct the test case
        boolean[][] sk1 = Helper.readBinary("../../../resources/test_inputs/skeletonTest.png");
        List<int[]> m1 = Fingerprint.extract(sk1);
        List<int[]> expt1 = new ArrayList<int[]>();
        expt1.add(new int[] { 39, 21, 264 });
        expt1.add(new int[] { 53, 33, 270 });

            // b. assert the equality between the expected and extracted minutia
        assertEquals("test case 1 - number of identified minutia differ", m1.size(), expt1.size());
        assertEquals("test case 1 - minutia 1 row differ", expt1.get(0)[0], m1.get(0)[0]);
        assertEquals("test case 1 - minutia 1 column differ", expt1.get(0)[1], m1.get(0)[1]);
        assertEquals("test case 1 - minutia 1 orientation differ", expt1.get(0)[2], m1.get(0)[2]);
        assertEquals("test case 1 - minutia 2 row differ", expt1.get(1)[0], m1.get(1)[0]);
        assertEquals("test case 1 - minutia 2 column differ", expt1.get(1)[1], m1.get(1)[1]);
        assertEquals("test case 1 - minutia 2 orientation differ", expt1.get(1)[2], m1.get(1)[2]);

        // 2. whole fingerprint extraction (1 to 16)
        System.out.print("Extracting ");

        for (int i = 1; i <= 16; ++i) {
            for (int j = 1; j <= 8; ++j) {
                // a. construct the test case
                String name = i + "_" + j;
                boolean[][] image = Helper.readBinary("../../../resources/fingerprints/" + name + ".png");
                boolean[][] skeleton = Fingerprint.thin(image);
                List<int[]> minutia = Fingerprint.extract(skeleton);
                int[][] colorImageSkeleton = Helper.fromBinary(skeleton);
                Helper.drawMinutia(colorImageSkeleton, minutia);

                // b. write the extracted minutia as png images to the disk
                System.out.print(name + ", ");
                Helper.writeARGB("minutiae_" + name + ".png", colorImageSkeleton);
            }
        }
        System.out.println("");

        // 3. summarize the testing process
        System.out.println(" => TestExtract - All Tests Passed");
    }
}

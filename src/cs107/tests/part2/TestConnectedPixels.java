import cs107.Fingerprint;
import cs107.Helper;
import java.util.Arrays;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

public final class TestConnectedPixels {
    public static void main(String[] args) {
        // 1. simple path test case (limited distance)
            // a. construct the test case
        boolean[][] im1 = {
            { true, false, false, true, true },
            { false, false, true, true, false },
            { false, true, true, false, false },
            { false, false, false, false, false }
        };
        boolean[][] expt1 = {
            { false, false, false, true, false },
            { false, false, true, true, false },
            { false, true, true, false, false },
            { false, false, false, false, false }
        };
        boolean[][] c1 = Fingerprint.connectedPixels(im1, 2, 1, 2);

            // b. assert the identical content of both array of connected pixels
        assertArrayEquals("test case 1 - images differ", Helper.convertImage(expt1), Helper.convertImage(c1));

        // 2. simple path test case (out of bound distance)
            // a. construct the test case
        boolean[][] im2 = {
            { true, false, false, true },
            { false, false, true, true },
            { false, true, true, false },
            { false, false, false, false }
        };
        boolean[][] expt2 = {
            { false, false, false, true },
            { false, false, true, true },
            { false, true, true, false },
            { false, false, false, false }
        };
        boolean[][] c2 = Fingerprint.connectedPixels(im2, 2, 1, 10);

            // b. assert the identical content of both array of connected pixels
        assertArrayEquals("test case 2 - images differ", Helper.convertImage(expt2), Helper.convertImage(c2));

        // 3. simple path test case (limited distance)
            // a. construct the test case
        boolean[][] im3 = {
            { true, false, false, true },
            { false, false, true, true },
            { false, true, true, false },
            { false, false, false, false }
        };
        boolean[][] expt3 = {
            { false, false, false, false },
            { false, false, true,  false },
            { false, true,  true,  false },
            { false, false, false, false }
        };
        boolean[][] c3 = Fingerprint.connectedPixels(im3, 2, 1, 1);

            // b. assert the identical content of both array of connected pixels
        assertArrayEquals("test case 3 - images differ", Helper.convertImage(expt3), Helper.convertImage(c3));

        // 4. diverging path test case
            // a. construct the test case
        boolean[][] im4 = {
            { true,  false, false, true,  true },
            { true,  false, true,  true,  false },
            { true,  true,  false, false, false },
            { false, true,  false, true,  false }
        };
        boolean[][] expt4 = {
            { true,  false, false, true,  false },
            { true,  false, true,  true,  false },
            { true,  true,  false, false, false },
            { false, true,  false, false, false }
        };
        boolean[][] c4 = Fingerprint.connectedPixels(im4, 2, 1, 2);

            // b. assert the identical content of the two output arrays
        assertArrayEquals("test case 4 - images differ", Helper.convertImage(expt4), Helper.convertImage(c4));

        // 5. summarize the testing process
        System.out.println(" => TestConnectedPixels - All Tests Passed");
    }
}

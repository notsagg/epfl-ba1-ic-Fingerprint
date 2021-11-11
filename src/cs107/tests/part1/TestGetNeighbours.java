import cs107.Fingerprint;
import java.util.Arrays;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public final class TestGetNeighbours {
    public static void main(String[] args) {
        // 1. no neighbours test case
            // a. construct the test case
        boolean[][] im1 = {{ true }};
        boolean[] n1 = Fingerprint.getNeighbours(im1, 0, 0);
        boolean[] expt1 = { false, false, false, false, false, false, false, false };

            // b. assert the identical lengths of the two output arrays
        assertSame("test case 1: expecting same array length -", n1.length, expt1.length);

            // c. assert the identical content of the two output arrays
        assertTrue(Arrays.equals(n1, expt1));

        // 2. single neighbour test case
            // a. construct the test case
        boolean[][] im2 = {{ true, true }};
        boolean[] n2 = Fingerprint.getNeighbours(im2, 0, 0);
        boolean[] expt2 = { false, false, true, false, false, false, false, false };

            // b. assert the identical lengths of the two output arrays
        assertSame("test case 2: expecting same array length -", n2.length, expt2.length);

            // c. assert the identical content of the two output arrays
        assertTrue(Arrays.equals(n2, expt2));

        // 3. no image test undefined test cases (1)
            // a. construct the test case
        boolean[][] im3 = null;

            // b. assert that the method throws as expected
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.getNeighbours(im3, 0, 0));

        // 4. no image test undefined test cases (2)
            // a. construct the test case
        boolean[][] im4 = { };

            // b. assert that the method throws as expected
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.getNeighbours(im4, 0, 0));

        // 5. out of bound row/col test case
            // a. construct the test case
        boolean[][] im5 = {{ true }};

            // b. assert that the method throws as expected
        assertNull("test case 5: should be null", Fingerprint.getNeighbours(im5, 10, 10));

        // 6. summarize the testing process
        System.out.println(" => All Tests Passed");
    }
}

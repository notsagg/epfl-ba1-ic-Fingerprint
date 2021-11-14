import cs107.Fingerprint;
import java.text.DecimalFormat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public final class TestComputeSlope {
    public static void main(String[] args) {
        DecimalFormat f = new DecimalFormat("##.00"); // 2 decimal place rounding

        // 1. simple slope test case
            // a. construct the test case
        boolean[][] cp1 = {
            { false, false, false, true, false },
            { false, false, true, true, false },
            { false, true, true, false, false },
            { false, false, false, false, false }
        };
        double expt1 = 0.7;
        double sl1 = Fingerprint.computeSlope(cp1, 2, 1);

            // b. assert the equality between the expected slope and the actual result
        assertTrue("test case 1 - slopes differ", expt1 == sl1);

        // 2. 1st quadrant angle test case
            // a. construct the test case
        boolean[][] im2 = {
            { false, false, false, false, false, false, false, false, false, true, true },
            { false, false, false, false, false, false, false, true, true, false, false },
            { false, false, false, false, true, true, true, false, false, false, false },
            { true, true, true, true, false, false, false, false, false, false, false }
        };
        double expt2 = 0.26;
        boolean[][] c2 = Fingerprint.connectedPixels(im2, 3, 0, 11);
        double s2 = Fingerprint.computeSlope(c2, 3, 0);

            // b. assert the slope equality
        assertEquals("test case 2 - slopes differ", f.format(s2), f.format(expt2));

        // 3. 2nd quadrant angle test case
            // a. construct the test case
        boolean[][] im3 = {
            { true, true, false, false, false, false, false, false, false, false, false },
            { false, false, true, true, false, false, false, false, false, false, false },
            { false, false, false, false, true, true, true, false, false, false, false },
            { false, false, false, false, false, false, false, true, true, true, true }
        };
        double expt3 = -0.26;
        boolean[][] c3 = Fingerprint.connectedPixels(im3, 3, 10, 11);
        double s3 = Fingerprint.computeSlope(c3, 3, 10);

            // b. assert the slope equality
        assertEquals("test case 3 - slopes differ", f.format(s3), f.format(expt3));

        // 4. 3rd quadrant angle test case
            // a. construct the test case
        boolean[][] im4 = {
            { false, false, false, false, false, false, false, true, true, true, true },
            { false, false, false, false, true, true, true, false, false, false, false },
            { false, false, true, true, false, false, false, false, false, false, false },
            { true, true, false, false, false, false, false, false, false, false, false }
        };
        double expt4 = 0.26;
        boolean[][] c4 = Fingerprint.connectedPixels(im4, 0, 10, 11);
        double s4 = Fingerprint.computeSlope(c4, 0, 10);

            // b. assert the slope equality
        assertEquals("test case 4 - slopes differ", f.format(s4), f.format(expt4));

        // 5. 4th quadrant angle test case
            // a. construct the test case
        boolean[][] im5 = {
            { true, true, true, true, false, false, false, false, false, false, false },
            { false, false, false, false, true, true, true, false, false, false, false },
            { false, false, false, false, false, false, false, true, true, false, false },
            { false, false, false, false, false, false, false, false, false, true, true }
        };
        double expt5 = -0.26;
        boolean[][] c5 = Fingerprint.connectedPixels(im5, 0, 0, 11);
        double s5 = Fingerprint.computeSlope(c5, 0, 0);

            // b. assert the slope equality
        assertEquals("test case 5 - slopes differ", f.format(s5), f.format(expt5));

        // 6. summarize the testing process
        System.out.println(" => TestComputeSlope - All Tests Passed");
    }
}

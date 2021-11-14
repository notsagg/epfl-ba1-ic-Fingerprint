import cs107.Fingerprint;
import java.text.DecimalFormat;
import static org.junit.Assert.assertEquals;

public final class TestComputeAngle {
    public static void main(String[] args) {
        DecimalFormat f = new DecimalFormat("##.00"); // 2 decimal place rounding

        // 1. 1st quadrant angle test case (./)
            // a. construct the test case
        boolean[][] im1 = {
            { false, false, false, false, false, false, false, false, false, true, true },
            { false, false, false, false, false, false, false, true, true, false, false },
            { false, false, false, false, true, true, true, false, false, false, false },
            { true, true, true, true, false, false, false, false, false, false, false }
        };
        double expt1 = 0.26;
        boolean[][] c1 = Fingerprint.connectedPixels(im1, 3, 0, 11);
        double s1 = Fingerprint.computeSlope(c1, 3, 0);
        double a1 = Fingerprint.computeAngle(c1, 3, 0, s1);

            // b. assert the slope equality
        assertEquals("test case 1 - angles differ", f.format(expt1), f.format(a1));

        // 2. 2nd quadrant angle test case (\.)
            // a. construct the test case
        boolean[][] im2 = {
            { true, true, false, false, false, false, false, false, false, false, false },
            { false, false, true, true, false, false, false, false, false, false, false },
            { false, false, false, false, true, true, true, false, false, false, false },
            { false, false, false, false, false, false, false, true, true, true, true }
        };
        double expt2 = -0.34;
        boolean[][] c2 = Fingerprint.connectedPixels(im2, 0, 0, 11);
        double s2 = Fingerprint.computeSlope(c2, 0, 0);
        double a2 = Fingerprint.computeAngle(c2, 0, 0, s2);

            // b. assert the slope equality
        assertEquals("test case 2 - angles differ", f.format(expt2), f.format(a2));

        // 3. 3rd quadrant angle test case (/.)
            // a. construct the test case
        boolean[][] im3 = {
            { false, false, false, false, false, false, false, true, true, true, true },
            { false, false, false, false, true, true, true, false, false, false, false },
            { false, false, true, true, false, false, false, false, false, false, false },
            { true, true, false, false, false, false, false, false, false, false, false }
        };
        double expt3 = 3.40;
        boolean[][] c3 = Fingerprint.connectedPixels(im3, 0, 10, 11);
        double s3 = Fingerprint.computeSlope(c3, 0, 10);
        double a3 = Fingerprint.computeAngle(c3, 0, 10, s3);

            // b. assert the slope equality
        assertEquals("test case 3 - angles differ", f.format(expt3), f.format(a3));

        // 4. 4th quadrant angle test case (.\)
            // a. construct the test case
        boolean[][] im4 = {
            { true, true, true, true, false, false, false, false, false, false, false },
            { false, false, false, false, true, true, true, false, false, false, false },
            { false, false, false, false, false, false, false, true, true, false, false },
            { false, false, false, false, false, false, false, false, false, true, true }
        };
        double expt4 = -0.26;
        boolean[][] c4 = Fingerprint.connectedPixels(im4, 0, 0, 11);
        double s4 = Fingerprint.computeSlope(c4, 0, 0);
        double a4 = Fingerprint.computeAngle(c4, 0, 0, s4);

            // b. assert the slope equality
        assertEquals("test case 4 - angles differ", f.format(expt4), f.format(a4));

        // 5. summarize the testing process
        System.out.println(" => TestComputeAngle - All Tests Passed");
    }
}

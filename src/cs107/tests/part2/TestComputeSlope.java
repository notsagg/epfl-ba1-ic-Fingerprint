import cs107.Fingerprint;
import static org.junit.Assert.assertTrue;

public final class TestComputeSlope {
    public static void main(String[] args) {
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

        // 2. summarize the testing process
        System.out.println(" => TestComputeSlope - All Tests Passed");
    }
}

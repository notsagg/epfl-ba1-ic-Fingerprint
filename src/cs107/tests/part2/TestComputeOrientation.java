import cs107.Fingerprint;
import static org.junit.Assert.assertSame;

public final class TestComputeOrientation {
    public static void main(String[] args) {
        // 1. simple orientation test case
            // a. construct the test case
        boolean[][] im1 = {
            { true, false, false, true },
            { false, false, true, true },
            { false, true, true, false },
            { false, false, false, false }
        };
        int expt1 = 35;
        int a1 = Fingerprint.computeOrientation(im1, 2, 1, 3);

            // b. assert the equality in orientation
        assertSame("test case 1 - orientation differ", a1, expt1);

        // 2. summarize the testing process
        System.out.println(" => TestComputeOrientation - All Tests Passed");
    }
}

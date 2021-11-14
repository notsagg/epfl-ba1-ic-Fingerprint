import cs107.Fingerprint;
import static org.junit.Assert.assertArrayEquals;

public final class TestApplyTranslation {
    public static void main(String[] args) {
        // 1. test case (1)
            // a. construct the test case
        int[] m1 = { 1, 3, 10 };
        int[] expt1 = { 1, 3, 10 };
        int[] r1 = Fingerprint.applyTranslation(m1, 0, 0);

            // b. assert the minutia equality
        assertArrayEquals("test case 1 - minutia parameters differ", expt1, r1);

        // 2. test case (2)
            // a. construct the test case
        int[] m2 = { 1, 3, 10 };
        int[] expt2 = { -9, -2, 10 };
        int[] r2 = Fingerprint.applyTranslation(m2, 10, 5);

            // b. assert the minutia equality
        assertArrayEquals("test case 2 - minutia parameters differ", expt2, r2);

        // 3. summarize the testing process
        System.out.println(" => TestApplyTranslation - All Tests Passed");
    }
}

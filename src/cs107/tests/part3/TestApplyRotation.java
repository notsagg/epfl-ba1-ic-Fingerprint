import cs107.Fingerprint;
import static org.junit.Assert.assertArrayEquals;

public final class TestApplyRotation {
    public static void main(String[] args) {
        int[] m123 = { 1, 3, 10 };

        // 1. test case (1)
            // a. construct the test case
        int[] expt1 = { 1, 3, 10 };
        int[] r1 = Fingerprint.applyRotation(m123, 0, 0, 0);

            // b. assert the minutia equality
        assertArrayEquals("test case 1 - minutia parameters differ", expt1, r1);

        // 2. test case (2)
            // a. construct the test case
        int[] expt2 = { 1, 3, 10 };
        int[] r2 = Fingerprint.applyRotation(m123, 10, 5, 0);

            // b. assert the minutia equality
        assertArrayEquals("test case 2 - minutia parameters differ", expt2, r2);

        // 3. test case (3)
            // a. construct the test case
        int[] expt3 = { -3, 1, 100 };
        int[] r3 = Fingerprint.applyRotation(m123, 0, 0, 90);

            // b. assert the minutia equality
        assertArrayEquals("test case 3 - minutia parameters differ", expt3, r3);

        // 4. test case (4)
            // a. construct the test case
        int[] m4 = { 0, 3, 10 };
        int[] expt4 = { -3, 0, 100 };
        int[] r4 = Fingerprint.applyRotation(m4, 0, 0, 90);

            // b. assert the minutia equality
        assertArrayEquals("test case 4 - minutia parameters differ", expt4, r4);

        // 5. test case (5)
            // a. construct the test case
        int[] m5 = { 3, 0, 10 };
        int[] expt5 = { 0, 3, 100 };
        int[] r5 = Fingerprint.applyRotation(m5, 0, 0, 90);

            // b. assert the minutia equality
        assertArrayEquals("test case 5 - minutia parameters differ", expt5, r5);

        // 6. summarize the testing process
        System.out.println(" => TestApplyRotation - All Tests Passed");
    }
}

import cs107.Fingerprint;
import cs107.Helper;
import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.assertSame;

public final class TestExtract {
    public static void main(String[] args) {
        // 1. simple skeleton test case
            // a. construct the test case
        boolean[][] sk1 = Helper.readBinary("../../../resources/test_inputs/skeletonTest.png");
        List<int[]> m1 = Fingerprint.extract(sk1);
        List<int[]> expt1 = new ArrayList<int[]>();
        expt1.add(new int[] { 39, 21, 265 });
        expt1.add(new int[] { 53, 33, 94 });

            // b. assert the equality between the expected and extracted minutia
        assertSame("test case 1 - number of identified minutia differ", m1.size(), expt1.size());
        assertSame("test case 1 - minutia 1 row differ", expt1.get(0)[0], m1.get(0)[0]);
        assertSame("test case 1 - minutia 1 column differ", expt1.get(0)[1], m1.get(0)[1]);
        assertSame("test case 1 - minutia 1 orientation differ", expt1.get(0)[2], m1.get(0)[2]);
        assertSame("test case 1 - minutia 2 row differ", expt1.get(1)[0], m1.get(1)[0]);
        assertSame("test case 1 - minutia 2 column differ", expt1.get(1)[1], m1.get(1)[1]);
        assertSame("test case 1 - minutia 2 orientation differ", expt1.get(1)[2], m1.get(1)[2]);

        // 2. summarize the testing process
        System.out.println(" => TestExtract - All Tests Passed");
    }
}

import cs107.Fingerprint;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

public final class TestTransitions {
    public static void main(String[] args) {
        // 1. null neighbour array
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.transitions(null));

        // 2. incomplete neighbour array
        boolean[] incomplete = { false, true }; // only two neighbours (missing six)
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.transitions(incomplete));

        // 3. random neighbour set
            // a. construct the test case
        boolean[] n3 = { false, true, true, false, false, false, true, false };
        int t3 = Fingerprint.transitions(n3);

            // b. assert the identical black neighbour count
        assertEquals("test case 3: transition count differ", t3, 2);

        // 4. summarize the testing process
        System.out.println(" => TestTransitions - All Tests Passed");
    }
}

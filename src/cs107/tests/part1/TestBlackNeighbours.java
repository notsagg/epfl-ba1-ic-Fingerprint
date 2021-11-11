import cs107.Fingerprint;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

public final class TestBlackNeighbours {
    public static void main(String[] args) {
        // 1. null neighbour array
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.blackNeighbours(null));

        // 2. incomplete neighbour array
        boolean[] incomplete = { false, true }; // only two neighbours (missing six)
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.blackNeighbours(incomplete));

        // 3. random neighbour set
            // a. construct the test case
        boolean[] n3 = { false, true, true, false, false, false, true, false };
        int b3 = Fingerprint.blackNeighbours(n3);

            // b. assert the identical black neighbour count
        assertEquals("test case 3: black neighbour count differ", b3, 3);

        // 4. summarize the testing process
        System.out.println(" => TestBlackNeighbours - All Tests Passed");
    }
}

import cs107.Fingerprint;
import static org.junit.Assert.assertThrows;

public final class TestBlackNeighbours {
    public static void main(String[] args) {
        // 1. null neighbour array
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.blackNeighbours(null));

        // 2. incomplete neighbour array
        boolean[] incomplete = { false, true }; // only two neighbours (missing six)
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.blackNeighbours(incomplete));

        // 3. summarize the testing process
        System.out.println(" => TestBlackNeighbours - All Tests Passed");
    }
}

import cs107.Fingerprint;
import static org.junit.Assert.assertThrows;

public final class TestTransitions {
    public static void main(String[] args) {
        // 1. null neighbour array
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.transitions(null));

        // 2. incomplete neighbour array
        boolean[] incomplete = { false, true }; // only two neighbours (missing six)
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.transitions(incomplete));

        // 3. summarize the testing process
        System.out.println(" => TestTransitions - All Tests Passed");
    }
}

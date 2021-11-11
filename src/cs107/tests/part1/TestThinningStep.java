import cs107.Fingerprint;
import static org.junit.Assert.assertThrows;

public final class TestThinningStep {
    public static void main(String[] args) {
        // 1. no image undefined test case
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.thinningStep(null, 0));

        // 2. empty image undefined test case
        boolean[][] im2 = {};
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.thinningStep(im2, 0));

        // 3. undefined step test case
        boolean[][] im3 = {{}};
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.thinningStep(im3, 3));

        // 4. summarize the testing process
        System.out.println(" => TestThinningStep - All Tests Passed");
    }
}

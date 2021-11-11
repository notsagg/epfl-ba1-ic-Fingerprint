import cs107.Fingerprint;
import static org.junit.Assert.assertThrows;

public final class TestIdentical {
    public static void main(String[] args) {
        // 1. no image test undefined test cases (1)
        boolean[][] im1 = {};
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.identical(im1, null));

        // 2. no image test undefined test cases (2)
        boolean[][] im2 = {};
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.identical(null, im2));

        // 3. no image test undefined test cases (3)
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.identical(null, null));

        // 4. empty images test case
        boolean[][] im4a = {}, im4b = {};
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.identical(im4a, im4b));

        // 5. different row size test case
        boolean[][] im5a = {{ false }}, im5b = { { false }, {false} };
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.identical(im5a, im5b));

        // 6. different col size test case
        boolean[][] im6a = {{ false }}, im6b = { { false, false } };
        assertThrows(IllegalArgumentException.class, () -> Fingerprint.identical(im6a, im6b));

        // 7. summarize the testing process
        System.out.println(" => TestIdentical - All Tests Passed");
    }
}

import cs107.Fingerprint;
import cs107.Helper;
import static org.junit.Assert.assertTrue;

public final class TestThin {
    public static void main(String[] args) {
        // 1. small image thinning test case
            // a. construct the test case
        boolean[][] im1 = Helper.readBinary("../../../resources/test_inputs/1_1_small.png");
        boolean[][] expt1 = Helper.readBinary("../../../resources/test_outputs/skeleton_1_1_small.png");

            // b. thin the corresponding image
        boolean[][] r1 = Fingerprint.thin(im1);
        // Helper.writeBinary("skeleton_1_1_small.png", r1);

            // c. compare the output image with the expected output
        assertTrue(Fingerprint.identical(r1, expt1));

        // 2. actual fingerprint thinning test case (1)
            // a. construct the test case
        boolean[][] im2 = Helper.readBinary("../../../resources/fingerprints/1_1.png");
        boolean[][] expt2 = Helper.readBinary("../../../resources/test_outputs/skeleton_1_1.png");

            // b. thin the corresponding image
        boolean[][] r2 = Fingerprint.thin(im2);
        // Helper.writeBinary("skeleton_1_1.png", r2);

            // c. compare the output image with the expected output
        assertTrue(Fingerprint.identical(r2, expt2));

        // 3. actual fingerprint thinning test case (2)
            // a. construct the test case
        boolean[][] im3 = Helper.readBinary("../../../resources/fingerprints/1_2.png");
        boolean[][] expt3 = Helper.readBinary("../../../resources/test_outputs/skeleton_1_2.png");

            // b. thin the corresponding image
        boolean[][] r3 = Fingerprint.thin(im3);
        // Helper.writeBinary("skeleton_1_2.png", r3);

            // c. compare the output image with the expected output
        assertTrue(Fingerprint.identical(r3, expt3));

        // 4. actual fingerprint thinning test case (3)
            // a. construct the test case
        boolean[][] im4 = Helper.readBinary("../../../resources/fingerprints/2_1.png");
        boolean[][] expt4 = Helper.readBinary("../../../resources/test_outputs/skeleton_2_1.png");

            // b. thin the corresponding image
        boolean[][] r4 = Fingerprint.thin(im4);
        // Helper.writeBinary("skeleton_2_1.png", r4);

            // c. compare the output image with the expected output
        assertTrue(Fingerprint.identical(r4, expt4));

        // 5. summarize the testing process
        System.out.println(" => TestThin - All Tests Passed");
    }
}

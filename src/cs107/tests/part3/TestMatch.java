import cs107.Fingerprint;
import cs107.Helper;
import java.util.List;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public final class TestMatch {
    public static void main(String[] args) {
        // 1. fingerprint 1 two version equality test case
            // a. construct the test case
        boolean[][] f1im1 = Helper.readBinary("../../../resources/fingerprints/1_1.png");
        boolean[][] f1im2 = Helper.readBinary("../../../resources/fingerprints/1_2.png");
        boolean[][] f1sk1 = Fingerprint.thin(f1im1);
        boolean[][] f1sk2 = Fingerprint.thin(f1im2);
        List<int[]> f1m1 = Fingerprint.extract(f1sk1);
        List<int[]> f1m2 = Fingerprint.extract(f1sk2);

            // b. assert the minutia equality
        boolean f1match = Fingerprint.match(f1m1, f1m2);
        assertTrue("test case 1 - fingerprints differ", f1match);

        // 2. fingerprint 2 two version equality test case
            // a. construct the test case
        boolean[][] f2im1 = Helper.readBinary("../../../resources/fingerprints/2_1.png");
        boolean[][] f2im2 = Helper.readBinary("../../../resources/fingerprints/2_2.png");
        boolean[][] f2sk1 = Fingerprint.thin(f2im1);
        boolean[][] f2sk2 = Fingerprint.thin(f2im2);
        List<int[]> f2m1 = Fingerprint.extract(f2sk1);
        List<int[]> f2m2 = Fingerprint.extract(f2sk2);

            // b. assert the minutia equality
        boolean f2match = Fingerprint.match(f2m1, f2m2);
        assertTrue("test case 2 - fingerprints differ", f2match);

        // 3. fingerprint 1 to 16 all versions equality test case
        System.out.print("Testing ");

        for (int i = 1; i <= 16; ++i) {
            String refName = i + "_1";

            for (int j = 1; j <= 8; ++j) {
                // a. print out the name of the fingerprints about to be tested
                String name = i + "_" + j;
                System.out.print(refName + " and " + name + ", ");

                // b. construct the test case
                boolean[][] fim1 = Helper.readBinary("../../../resources/fingerprints/" + refName + ".png");
                boolean[][] fim2 = Helper.readBinary("../../../resources/fingerprints/" + name + ".png");
                boolean[][] fsk1 = Fingerprint.thin(fim1);
                boolean[][] fsk2 = Fingerprint.thin(fim2);
                List<int[]> fm1 = Fingerprint.extract(fsk1);
                List<int[]> fm2 = Fingerprint.extract(fsk2);

                // c. assert the minutia equality
                boolean fmatch = Fingerprint.match(fm1, fm2);
                assertTrue("\ntest case 3 - fingerprint " + refName + " and " + name + " differ", fmatch);
            }
        }
        System.out.println("");

        // 4. fingerprint 1 to 16 all versions non-equality test case
        System.out.print("Testing ");

        for (int i = 1; i <= 16; ++i) {
            String refName = i + "_1";

            for (int j = 1; j <= 16; ++j) {
                // a. skip cases were both fingerprint ought to be equal
                if (i == j) continue;

                // b. print out the name of the fingerprints about to be tested
                String name = j + "_1";
                System.out.print(refName + " and " + name + ", ");

                // c. construct the test case
                boolean[][] fim1 = Helper.readBinary("../../../resources/fingerprints/" + refName + ".png");
                boolean[][] fim2 = Helper.readBinary("../../../resources/fingerprints/" + name + ".png");
                boolean[][] fsk1 = Fingerprint.thin(fim1);
                boolean[][] fsk2 = Fingerprint.thin(fim2);
                List<int[]> fm1 = Fingerprint.extract(fsk1);
                List<int[]> fm2 = Fingerprint.extract(fsk2);

                // d. assert the minutia equality
                boolean fmatch = Fingerprint.match(fm1, fm2);
                assertFalse("\ntest case 4 - fingerprint " + refName + " and " + name + " were matched", fmatch);
            }
        }
        System.out.println("");

        // 5. summarize the testing process
        System.out.println(" => TestMatch - All Tests Passed");
    }
}

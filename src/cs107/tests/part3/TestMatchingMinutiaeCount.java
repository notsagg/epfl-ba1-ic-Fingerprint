import cs107.Fingerprint;
import cs107.Helper;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class TestMatchingMinutiaeCount {
    public static void main(String[] args) {
        // 1. fingerprint 1 to 16 all versions equality test case
        int[][] expt1 = {
            { 66, 6, 10, 14, 16, 9, 11, 4 },
            { 76, 19, 23, 20, 19, 9, 19, 17 },
            { 121, 17, 30, 2, 4, 21, 2, 7 },
            { 118, 24, 17, 19, 25, 19, 21, 22 },
            { 186, 14, 9, 12, 18, 8, 19, 7 },
            { 71, 4, 6, 7, 4, 3, 10, 9 },
            { 240, 35, 14, 18, 45, 44, 21, 14 },
            { 127, 25, 11, 15, 6, 19, 20, 50 },
            { 135, 15, 19, 25, 23, 12, 26, 23 },
            { 165, 20, 12, 17, 21, 12, 16, 26 },
            { 85, 18, 13, 23, 2, 22, 7, 12 },
            { 109, 42, 21, 22, 23, 20, 25, 23 },
            { 87, 18, 1, 3, 3, 0, 7, 4 },
            { 111, 28, 32, 45, 36, 28, 24, 37 },
            { 101, 33, 15, 14, 13, 15, 19, 13 },
            { 209, 22, 8, 21, 19, 19, 16, 20 }
        };
        System.out.print("Counting ");

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

                // c. assert the minutiae count equality
                int count = Fingerprint.matchingMinutiaeCount(fm1, fm2, Fingerprint.DISTANCE_THRESHOLD, Fingerprint.ORIENTATION_THRESHOLD);
                assertEquals("\ntest case 1 - minutiae count between " + refName + " and " + name + " differs", expt1[i-1][j-1], count);
            }
        }
        System.out.println("");

        // 2. fingerprint 1 to 16 all versions non-equality test case
        int expt2 = Fingerprint.FOUND_THRESHOLD; // expecting no more than FOUND_THRESHOLD matches
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
                int count = Fingerprint.matchingMinutiaeCount(fm1, fm2, Fingerprint.DISTANCE_THRESHOLD, Fingerprint.ORIENTATION_THRESHOLD);
                assertTrue("\ntest case 2 - minutiae count between " + refName + " and " + name + " goes behond threshold", count < expt2);
            }
        }
        System.out.println("");

        // 6. summarize the testing process
        System.out.println(" => TestMatchingMinutiaeCount - All Tests Passed");
    }
}

package cs107;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* Provides tools to compare fingerprint.
*/
public class Fingerprint {
    // MARK: - Public Attributes
    /// The number of pixels to consider in each direction when doing the linear regression to compute the orientation
    public static final int ORIENTATION_DISTANCE = 16;

    /// The maximum distance between two minutiae to be considered matching
    public static final int DISTANCE_THRESHOLD = 5;

    /// The number of matching minutiae needed for two fingerprints to be considered identical
    public static final int FOUND_THRESHOLD = 20;

    /// The distance between two angle to be considered identical
    public static final int ORIENTATION_THRESHOLD = 20;

    /// The offset in each direction for the rotation to test when doing the matching
    public static final int MATCH_ANGLE_OFFSET = 2;

    // MARK: - Private Attributes
    /// The position of neighbour P0 to P7 relative to (row, col) in an image
    private static final int[][] NEIGHBOUR_MAPPING = { {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1} };

    /// The number of neighbours that exist around a given pixel
    private static final int NEIGHBOUR_COUNT = 8;

    /// Neighbours that ought to be white for a pixel to be considered redundant (sibblings for step 1 and step 2)
    private static final int[][] THINNING_WHITES = { { 0, 2, 4, 2, 4, 6 }, { 0, 2, 6, 0, 4, 6 } };

    /// Lower/Upper Coordinates of an Operating Field
    private static int fieldLowerRow; // operating field row lower bound
    private static int fieldUpperRow; // operating field row upper bound
    private static int fieldLowerCol; // operating field colunn lower bound
    private static int fieldUpperCol; // operating field colunn upper bound

    // MARK: - Public Methods
    /**
    * Returns an array containing the value of the 8 neighbours of the pixel at
    * coordinates <code>(row, col)</code>.
    * <p>
    * The pixels are returned such that their indices corresponds to the following
    * diagram:<br>
    * ------------- <br>
    * | 7 | 0 | 1 | <br>
    * ------------- <br>
    * | 6 | _ | 2 | <br>
    * ------------- <br>
    * | 5 | 4 | 3 | <br>
    * ------------- <br>
    * <p>
    * If a neighbours is out of bounds of the image, it is considered white.
    * <p>
    * If the <code>row</code> or the <code>col</code> is out of bounds of the
    * image, the returned value should be <code>null</code>.
    *
    * @param image array containing each pixel's boolean value.
    * @param row   the row of the pixel of interest, must be between
    *              <code>0</code>(included) and
    *              <code>image.length</code>(excluded).
    * @param col   the column of the pixel of interest, must be between
    *              <code>0</code>(included) and
    *              <code>image[row].length</code>(excluded).
    * @return An array containing each neighbours' value.
    */
    public static boolean[] getNeighbours(boolean[][] image, int row, int col) {
        // 1. ensure the existence of the image
        if (image == null || image.length == 0) {
            throw new IllegalArgumentException("error: image is expected to contain at least one pixel");
        }

        // 2. ensure that row and col are in bound
        if (row >= image.length || col >= image[0].length) return null;

        // 3. initialize working variable
        boolean[] neighbours = new boolean[NEIGHBOUR_COUNT];

        // 4. mark the position of our neighbours if they exist
        for (int i = 0; i < NEIGHBOUR_COUNT; ++i) {
            // a. get the corresponding position
            int longitude = row + NEIGHBOUR_MAPPING[i][0], latitude = col + NEIGHBOUR_MAPPING[i][1];

            // b. handle the corner case (upper-left, upper-right, lower-left, and lower-right)
            if (longitude < 0 || latitude < 0 || longitude >= image.length || latitude >= image[0].length) {
                neighbours[i] = false;

            // c. mark the neighbour
            } else {
                neighbours[i] = image[longitude][latitude];
            }
        }

        // 5. return an array of neighbours
        return neighbours;
    }

    /**
    * Computes the number of black (<code>true</code>) pixels among the neighbours
    * of a pixel.
    *
    * @param neighbours array containing each pixel value. The array must respect
    *                   the convention described in
    *                   {@link #getNeighbours(boolean[][], int, int)}.
    * @return the number of black neighbours.
    */
    public static int blackNeighbours(boolean[] neighbours) {
        // 1. ensure the existance of the neighbours array
        if (neighbours == null) {
            throw new IllegalArgumentException("error: neigbours is null");
        }

        // 2. ensure that neighbours is an array of neigbours
        if (neighbours.length != NEIGHBOUR_COUNT) {
            throw new IllegalArgumentException("error: neighbours is incomplete");
        }

        // 3. initialize working variables
        int encounter = 0; // number of black pixels encountered

        // 4. count the number of encountered black pixels
        for (int i = 0; i < NEIGHBOUR_COUNT; ++i) {
            if (neighbours[i]) encounter++;
        }

        // 5. return the total count
        return encounter;
    }

    /**
    * Computes the number of white to black transitions among the neighbours of
    * pixel.
    *
    * @param neighbours array containing each pixel value. The array must respect
    *                   the convention described in
    *                   {@link #getNeighbours(boolean[][], int, int)}.
    * @return the number of white to black transitions.
    */
    public static int transitions(boolean[] neighbours) {
        // 1. ensure the existance of the neighbours array
        if (neighbours == null) {
            throw new IllegalArgumentException("error: neigbours is null");
        }

        // 2. ensure that neighbours is an array of neigbours
        if (neighbours.length != NEIGHBOUR_COUNT) {
            throw new IllegalArgumentException("error: neighbours is incomplete");
        }

        // 3. initialize working variables
        int transitions = 0; // number of transitions from white to black made

        // 4. count the number of essential transitions made (white to black)
        for (int i = 1; i <= NEIGHBOUR_COUNT; ++i) {
            // a. check if there were a notable transition
            if (neighbours[i%NEIGHBOUR_COUNT] && !neighbours[i-1]) transitions++;
        }

        // 5. return the total count
        return transitions;
    }

    /**
    * Returns <code>true</code> if the images are identical and false otherwise.
    *
    * @param image1 array containing each pixel's boolean value.
    * @param image2 array containing each pixel's boolean value.
    * @return <code>True</code> if they are identical, <code>false</code>
    *         otherwise.
    */
    public static boolean identical(boolean[][] image1, boolean[][] image2) {
        // 1. ensure the existence of both images to compare
        if (image1 == null || image2 == null) {
            throw new IllegalArgumentException("error: an image is null");
        }

        // 2. ensure that both images aren't empty
        if (image1.length == 0 || image2.length == 0) {
            throw new IllegalArgumentException("error: images are empty");
        }

        // 3. check that the two images are of identical sizes
        if ((image1.length != image2.length) || (image1[0].length != image2[0].length)) {
            throw new IllegalArgumentException("error: images differ in size");
        }

        // 4. if a single pixel differs then the two images are different
        for (int i = 0; i < image1.length; ++i) {
            for (int j = 0; j < image1[0].length; ++j) {
                if (image1[i][j] != image2[i][j]) return false;
            }
        }

        // 5. otherwise the images are identical
        return true;
    }

    /**
    * Internal method used by {@link #thin(boolean[][])}.
    *
    * @param image array containing each pixel's boolean value.
    * @param step  the step to apply, Step 0 or Step 1.
    * @return A new array containing each pixel's value after the step.
    */
    public static boolean[][] thinningStep(boolean[][] image, int step) {
        // 1. ensure the existence of the input image
        if (image == null) throw new IllegalArgumentException("error: image is null");

        // 2. ensure that the input image is not empty
        if (image.length == 0) throw new IllegalArgumentException("error: image is empty");

        // 3. ensure the validity of our inputs
        if (step != 0 && step != 1) {
            throw new IllegalArgumentException("error: step is neither 0 nor 1");
        }

        // 4. initialize working variables
        boolean[][] thin = new boolean[image.length][image[0].length];

        // 5. perform a deep-copy of the image to thin-out
        for (int i = 0; i < image.length; ++i) {
            thin[i] = Arrays.copyOf(image[i], image[i].length);
        }

        // 6. recursively discard redundant pixels
        for (int i = 0; i < image.length; ++i) {
            for (int j = 0; j < image[0].length; ++j) {
                // a. check that (i, j) is a pixel
                if (!image[i][j]) continue;

                // b. get the number of surrounding pixels (black neighbours)
                boolean[] adjacent = getNeighbours(image, i, j);
                int blacks = blackNeighbours(adjacent);

                // c. check that there are more than 2 but less than 6 black black neighbours
                if (blacks < 2 || blacks > 6) continue;

                // d. check that there is only one transition from (i, j) to the black neighbour
                if (transitions(adjacent) != 1) continue;

                // e. get the indexes where to expect white neighbours
                int[] whites = THINNING_WHITES[step];

                // f. check that P0, P2, or P4/P6 are white neighbours
                if (adjacent[whites[0]] && adjacent[whites[1]] && adjacent[whites[2]]) continue;

                // g. check that P2/P0, P4, or P6 are white neighbours
                if (adjacent[whites[3]] && adjacent[whites[4]] && adjacent[whites[5]]) continue;

                // h. thin-out the pixel at (i, j)
                thin[i][j] = false;
            }
        }

        // 7. return the thinned-out image
        return thin;
    }

    /**
    * Compute the skeleton of a boolean image.
    *
    * @param image array containing each pixel's boolean value.
    * @return array containing the boolean value of each pixel of the image after
    *         applying the thinning algorithm.
    */
    public static boolean[][] thin(boolean[][] image) {
        boolean[][] thin1, thin2 = new boolean[image.length][image[0].length];

        // 1. perform a deep-copy of the input image to thin2
        for (int i = 0; i < image.length; ++i) {
            thin2[i] = Arrays.copyOf(image[i], image[i].length);
        }

        // 1. thin-out the image as long as the line is not 1 pixel wide
        do {
            // a. execute step 1
            thin1 = thinningStep(thin2, 0);

            // b. execute step 2
            thin2 = thinningStep(thin1, 1);

        } while (!identical(thin1, thin2));

        // 3. return the 1 pixel thinned-out image
        return thin2;
    }

    private static boolean[][] connectedPixels(boolean[][] image, boolean[][] minutia, int row, int col, int distance) {
        // 1. mark the pixel at (row, col)
        minutia[row][col] = true;

        // 2. get the surrounding neighbours of the considered pixel
        boolean[] neighbours = getNeighbours(image, row, col);

        // 3. iterate through each neighbour
        for (int i = 0; i < NEIGHBOUR_COUNT; ++i) {
            // a. compute the coordinates of the neighbour
            int neighbourRow = row + NEIGHBOUR_MAPPING[i][0];
            int neighbourCol = col + NEIGHBOUR_MAPPING[i][1];

            // b. check that the neighbour row is in boundary
            if (neighbourRow < Fingerprint.fieldLowerRow || neighbourRow > Fingerprint.fieldUpperRow) continue;

            // c. check that the neighbour column is in boundary
            if (neighbourCol < Fingerprint.fieldLowerCol || neighbourCol > Fingerprint.fieldUpperCol) continue;

            // d. check that neighbour is a pixel
            if (!image[neighbourRow][neighbourCol]) continue;

            // e. check that the considered pixel is not already marked
            if (minutia[neighbourRow][neighbourCol]) continue;

            // f. continue with a recursive pixel search
            minutia = connectedPixels(image, minutia, neighbourRow, neighbourCol, distance);
        }

        // 4. if all neighbours have been marked then exit
        return minutia;
    }

    /**
    * Computes all pixels that are connected to the pixel at coordinate
    * <code>(row, col)</code> and within the given distance of the pixel.
    *
    * @param image    array containing each pixel's boolean value.
    * @param row      the first coordinate of the pixel of interest.
    * @param col      the second coordinate of the pixel of interest.
    * @param distance the maximum distance at which a pixel is considered.
    * @return An array where <code>true</code> means that the pixel is within
    *         <code>distance</code> and connected to the pixel at
    *         <code>(row, col)</code>.
    */
    public static boolean[][] connectedPixels(boolean[][] image, int row, int col, int distance) {
        // 1. check the validy of the input parameters
            // a. ensure the existance of the input image
        if (image == null) throw new IllegalArgumentException("error: image is null");

            // b. check that there is an actual minutia at the given row and column
        if (!image[row][col]) {
            System.out.println("row: " + row + " and col: " + col);
            throw new IllegalArgumentException("error: expecting a pixel at (row, col)");
        }

            // c. ensure that row, col, and distance are positive integer
        if (row < 0 || col < 0 || distance < 0) {
            throw new IllegalArgumentException("error: a paramater has negative value");
        }

            // d. check that row, and col are contained in the boundaries of the image
        if (row >= image.length || col >= image[0].length) {
            throw new IllegalArgumentException("error: a parameter is out of boundary");
        }

        // 2. compute the coordinates of the operating field
        Fingerprint.fieldLowerCol = (col - distance) < 0 ? 0 : (col - distance);
        Fingerprint.fieldLowerRow = (row - distance) < 0 ? 0 : (row - distance);
        Fingerprint.fieldUpperCol = (col + distance) >= image[0].length ? image[0].length-1 : (col + distance);
        Fingerprint.fieldUpperRow = (row + distance) >= image.length ? image.length-1 : (row + distance);

        // 3. initialize the connected pixels array
        boolean[][] minutia = new boolean[image.length][image[0].length];

        // 4. return the computed array of connected pixels
        return connectedPixels(image, minutia, row, col, distance);
    }

    /**
    * Computes the slope of a minutia using linear regression.
    *
    * @param connectedPixels the result of
    *                        {@link #connectedPixels(boolean[][], int, int, int)}.
    * @param row             the row of the minutia.
    * @param col             the col of the minutia.
    * @return the slope.
    */
    public static double computeSlope(boolean[][] connectedPixels, int row, int col) {
        // 1. check the validy of the input parameters
            // a. ensure the existence of the input array of connected pixels
        if (connectedPixels == null) throw new IllegalArgumentException("error: connectedPixels is null");

            // b. ensure that the input array of connectedPixels is not empty
        if (connectedPixels.length == 0) throw new IllegalArgumentException("error: connectedPixels is empty");

            // c. check that col and row are positive integers
        if (row < 0 || col < 0) throw new IllegalArgumentException("error: a paramater has negative value");

        // 2. initialize working variables
        int xy = 0, x2 = 0, y2 = 0; // x*y, x^2, and y^2

        // 3. recursively compute the three components of a linear regression
        for (int y = 0; y < connectedPixels.length; ++y) {
            for (int x = 0; x < connectedPixels[0].length; ++x) {
                // a. check that the current coordinates represent a pixel
                if (!connectedPixels[y][x]) continue;

                // b. compute the product of the coordinates (col x row)
                xy += ((x-col) * (row-y));

                // c. compute the square of the row coordinate
                y2 += Math.pow(row-y, 2);

                // d. compute the square of the col coordinate
                x2 += Math.pow(x-col, 2);
            }
        }

        // 4. handle the vertical slope case
        if (x2 == 0) return Double.POSITIVE_INFINITY;

        // 5. compute the general slope
        if (x2 >= y2) return (double)(xy)/x2;
        else return (double)(y2)/xy;
    }

    /**
    * Computes the orientation of a minutia in radians.
    *
    * @param connectedPixels the result of
    *                        {@link #connectedPixels(boolean[][], int, int, int)}.
    * @param row             the row of the minutia.
    * @param col             the col of the minutia.
    * @param slope           the slope as returned by
    *                        {@link #computeSlope(boolean[][], int, int)}.
    * @return the orientation of the minutia in radians.
    */
    public static double computeAngle(boolean[][] connectedPixels, int row, int col, double slope) {
        // 1. check the validy of the input parameters
            // a. ensure the existence of the input array of connected pixels
        if (connectedPixels == null) throw new IllegalArgumentException("error: connectedPixels is null");

            // b. ensure that the input array of connectedPixels is not empty
        if (connectedPixels.length == 0) throw new IllegalArgumentException("error: connectedPixels is empty");

            // c. check that col and row are positive integers
        if (row < 0 || col < 0) throw new IllegalArgumentException("error: a paramater has negative value");

        // 2. compute the orthonal slope
        double orthogonal = -1 * Math.pow(slope, -1);

        // 3. count the number of pixels to the left and right side of the minutia
        int lower = 0, upper = 0; // lower/upper pixel count

        for (int y = 0; y < connectedPixels.length; ++y) {
            for (int x = 0; x < connectedPixels[0].length; ++x) {
                // a. check that the pixel at (i, j) is part of the minutia
                if (!connectedPixels[y][x]) continue;

                // b. increment accordingly (pixel is above or belove the orthogonal line)
                if ((row-y) >= orthogonal*(x-col)) upper++;
                else lower++;
            }
        }

        // 4. handle the undefined cases
        if (slope == Double.POSITIVE_INFINITY) {
            return (upper >= lower) ? Math.PI/2 : -1/2 * Math.PI;
        }

        // 5. compute the angle between the x-axis and the slope using the arctan function
        double angle = Math.atan(slope);

        // 6. return the properly oriented angle
            // a. 3rd quadrant and 2nd quadrant
        if ((angle >= 0 && lower > upper) || (angle < 0 && lower <= upper)) {
            return angle + Math.PI;

            // b. 1st quadrant and 4th quadrant
        } else {
            return angle;
        }
    }

    /**
    * Computes the orientation of the minutia that the coordinate <code>(row,
    * col)</code>.
    *
    * @param image    array containing each pixel's boolean value.
    * @param row      the first coordinate of the pixel of interest.
    * @param col      the second coordinate of the pixel of interest.
    * @param distance the distance to be considered in each direction to compute
    *                 the orientation.
    * @return The orientation in degrees.
    */
    public static int computeOrientation(boolean[][] image, int row, int col, int distance) {
        // 1. check the validy of the input parameters
            // a. ensure the existence of the input image
        if (image == null) throw new IllegalArgumentException("error: image is null");

            // b. ensure that the input image is not empty
        if (image.length == 0) throw new IllegalArgumentException("error: image is empty");

            // c. check that col and row are positive integers
        if (row < 0 || col < 0 || distance < 0) {
            throw new IllegalArgumentException("error: a paramater has negative value");
        }

        // 2. get the connection pixels of (row, col) in image
        boolean[][] connectedPixels = connectedPixels(image, row, col, distance);

        // 3. compute the slope of this minutia represented by the connected pixels
        double slope = computeSlope(connectedPixels, row, col);

        // 4. compute the angle in radians between the x-axis and the slope
        double angle = computeAngle(connectedPixels, row, col, slope);

        // 5. convert the computed angle from radian to degrees
        float degrees = (float)Math.toDegrees(angle);

        // 6. return the orientation of the minutia in degrees
        return (degrees < 0) ? Math.round(degrees + 360) : Math.round(degrees);
    }

    /**
    * Extracts the minutiae from a thinned image.
    *
    * @param image array containing each pixel's boolean value.
    * @return The list of all minutiae. A minutia is represented by an array where
    *         the first element is the row, the second is column, and the third is
    *         the angle in degrees.
    * @see #thin(boolean[][])
    */
    public static List<int[]> extract(boolean[][] image) {
        // 1. check the validy of the input parameters
            // a. ensure the existence of the input image
        if (image == null) throw new IllegalArgumentException("error: image is null");

            // b. ensure that the input image is not empty
        if (image.length == 0) throw new IllegalArgumentException("error: image is empty");

        // 2. create a new array list to hold the extracted minutias (row, col and orientation)
        ArrayList<int[]> list = new ArrayList<int[]>();

        // 3. extract the minutias from the input image
        for (int i = 1; i < image.length-1; ++i) {
            for (int j = 1; j < image[0].length-1; ++j) {
                // a. ensure that (i, j) is a pixel
                if (!image[i][j]) continue;

                // b. get the number of transitions around the pixel at (i, j)
                boolean[] neighbours = getNeighbours(image, i, j);
                int transitions = transitions(neighbours);

                // c. handle exclusively terminations (1) and bifurcations (3)
                if (transitions != 1 && transitions != 3) continue;

                // d. get the ortienttion of the minutia
                int orientation = computeOrientation(image, i, j, image.length);
                int[] minutia = { i, j, orientation };

                // e. apend the minutia to our list
                list.add(minutia);
            }
        }

        // 4. reutrn the list of extracted minutia
        return list;
    }

    /**
    * Applies the specified rotation to the minutia.
    *
    * @param minutia   the original minutia.
    * @param centerRow the row of the center of rotation.
    * @param centerCol the col of the center of rotation.
    * @param rotation  the rotation in degrees.
    * @return the minutia rotated around the given center.
    */
    public static int[] applyRotation(int[] minutia, int centerRow, int centerCol, int rotation) {
        // 1. check the validy of the input parameters
            // a. ensure the existence of the input minutia
        if (minutia == null) throw new IllegalArgumentException("error: minutia is null");

            // b. ensure that the input minutia has three parameters
        if (minutia.length != 3) throw new IllegalArgumentException("error: minutia has wrong parameters");

            // c. check that centerRow and centerCol are positive integers
        if (centerRow < 0 || centerCol < 0) {
            throw new IllegalArgumentException("error: a paramater has negative value");
        }

        // 2. initialize working variables
        int[] orientedMinutia = new int[3]; // row, col, and orientation

        // 3. compute the new coordinates of our oriented minutia
            // a. compute the current cartesian coordinate system
        int y = centerRow - minutia[0]; // tantamount to rows
        int x = minutia[1] - centerCol; // tantamount to cols

            // b. conver the rotation parameter from degrees to radians
        double rad = Math.toRadians(rotation);

            // c. compute the new cartesian coordinate system
        int orientedY = (int)Math.round((x * Math.sin(rad)) + (y * Math.cos(rad))); // tantamount to rows
        int orientedX = (int)Math.round((x * Math.cos(rad)) - (y * Math.sin(rad))); // tantamount to cols

            // d. write the coordinates of our minutia in the new coordinate system
        orientedMinutia[0] = centerRow - orientedY;
        orientedMinutia[1] = orientedX + centerCol;

        // 4. compute the orientation in the new coordinate system
        orientedMinutia[2] = (minutia[2] + rotation) % 360;

        // 5. return the parameters of our oriented minutia
        return orientedMinutia;
    }

    /**
    * Applies the specified translation to the minutia.
    *
    * @param minutia        the original minutia.
    * @param rowTranslation the translation along the rows.
    * @param colTranslation the translation along the columns.
    * @return the translated minutia.
    */
    public static int[] applyTranslation(int[] minutia, int rowTranslation, int colTranslation) {
        // 1. check the validy of the input parameters
            // a. ensure the existence of the input minutia
        if (minutia == null) throw new IllegalArgumentException("error: minutia is null");

            // b. ensure that the input minutia has three parameters
        if (minutia.length != 3) throw new IllegalArgumentException("error: minutia has wrong parameters");

        // 2. initialize working variables
        int[] translatedMinutia = new int[3]; // row, col, and orientation

        // 3. update the coordinates of our translated minutia
        translatedMinutia[0] = minutia[0] - rowTranslation;
        translatedMinutia[1] = minutia[1] - colTranslation;

        // 4. keep the orientation of our minutia as is
        translatedMinutia[2] = minutia[2];

        // 5. return the parameters of our translated minutia
        return translatedMinutia;
    }

    /**
    * Computes the row, column, and angle after applying a transformation
    * (translation and rotation).
    *
    * @param minutia        the original minutia.
    * @param centerCol      the column around which the point is rotated.
    * @param centerRow      the row around which the point is rotated.
    * @param rowTranslation the vertical translation.
    * @param colTranslation the horizontal translation.
    * @param rotation       the rotation.
    * @return the transformed minutia.
    */
    public static int[] applyTransformation(int[] minutia, int centerRow, int centerCol, int rowTranslation,
    int colTranslation, int rotation) {
        // 1. apply a rotation to our minutia
        int[] transformedMinutia = applyRotation(minutia, centerRow, centerCol, rotation);

        // 2. apply a translation to our minutia
        return applyTranslation(transformedMinutia, rowTranslation, colTranslation);
    }

    /**
    * Computes the row, column, and angle after applying a transformation
    * (translation and rotation) for each minutia in the given list.
    *
    * @param minutiae       the list of minutiae.
    * @param centerCol      the column around which the point is rotated.
    * @param centerRow      the row around which the point is rotated.
    * @param rowTranslation the vertical translation.
    * @param colTranslation the horizontal translation.
    * @param rotation       the rotation.
    * @return the list of transformed minutiae.
    */
    public static List<int[]> applyTransformation(List<int[]> minutiae, int centerRow, int centerCol, int rowTranslation,
    int colTranslation, int rotation) {
        // 1. for each item of minutiae appply a transformation
        for (int i = 0; i < minutiae.size(); ++i) {
            // a. apply the transformation
            int[] minutia = applyTransformation(minutiae.get(i), centerRow, centerCol, rowTranslation, colTranslation, rotation);

            // b. update our list of minutiae with the transformed minutia
            minutiae.set(i, minutia);
        }

        // 2. return the updated list of minutiae
        return minutiae;
    }
    /**
    * Counts the number of overlapping minutiae.
    *
    * @param minutiae1      the first set of minutiae.
    * @param minutiae2      the second set of minutiae.
    * @param maxDistance    the maximum distance between two minutiae to consider
    *                       them as overlapping.
    * @param maxOrientation the maximum difference of orientation between two
    *                       minutiae to consider them as overlapping.
    * @return the number of overlapping minutiae.
    */
    public static int matchingMinutiaeCount(List<int[]> minutiae1, List<int[]> minutiae2, int maxDistance,
    int maxOrientation) {
        int matched = 0; // number of matching minutiae

        // 1. compare all minutiae from minutiae1 to all minutiae of minutia2
        for (int i = 0; i < minutiae1.size(); ++i) {
            for (int j = 0; j < minutiae2.size(); ++j) {
                // a. compute the difference for all parameters between the two minutiae
                int rowDiff = minutiae1.get(i)[0] - minutiae2.get(j)[0]; // difference in rows
                int colDiff = minutiae1.get(i)[1] - minutiae2.get(j)[1]; // difference in columns
                int angleDiff = minutiae1.get(i)[2] - minutiae2.get(j)[2]; // difference in orientation

                // b. compute the distance between the two minutiae
                int distance = (int)Math.sqrt(Math.pow(rowDiff, 2) + Math.pow(colDiff, 2));

                // c. compare the distance separating the two minutiae
                boolean distanceMatch = distance <= maxDistance;

                // d. compare the orientation betweem the two minutiae
                boolean angleMatch = angleDiff <= (maxOrientation + MATCH_ANGLE_OFFSET);

                // e. increment the match count if all three parameters match with tolerance
                if (distanceMatch && angleMatch) matched++;
            }
        }

        // 2. returned the number of matching minutiae
        return matched;
    }

    /**
    * Compares the minutiae from two fingerprints.
    *
    * @param minutiae1 the list of minutiae of the first fingerprint.
    * @param minutiae2 the list of minutiae of the second fingerprint.
    * @return Returns <code>true</code> if they match and <code>false</code>
    *         otherwise.
    */
    public static boolean match(List<int[]> minutiae1, List<int[]> minutiae2) {
        // 1. superpose every minutiae from minutia2 over the minutia at i from minutiae1
        for (int i = 0; i < minutiae1.size(); ++i) {
            for (int j = 0; j < minutiae2.size(); ++j) {
                // a. compute the transformation in row/col to apply (translation)
                int rowDiff = minutiae2.get(j)[0] - minutiae1.get(i)[0];
                int colDiff = minutiae2.get(j)[1] - minutiae1.get(i)[1];

                // b. compute the transformation in orientation to apply (rotation)
                int rotDiff = minutiae2.get(j)[2] - minutiae1.get(i)[2];

                // c. superspose minutiae j over minutia i by applying the computed transformation
                List<int[]> transformed = applyTransformation(minutiae2, minutiae1.get(i)[0], minutiae1.get(i)[1], rowDiff, colDiff, rotDiff);

                // d. count the number of matching minutiae with a certain tolerance
                int found = matchingMinutiaeCount(minutiae1, transformed, DISTANCE_THRESHOLD, ORIENTATION_THRESHOLD);

                // e. exit if there are enough matching minutiae
                if (found >= FOUND_THRESHOLD) return true;
            }
        }

        // 2. return false otherwise
        return false;
    }
}

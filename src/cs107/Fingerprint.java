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
    private static final int[][] neighbourMapping = { {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1} };

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
        boolean[] neighbours = new boolean[8];

        // 1. ensure the existence of the image
        if (image.length == 0) {
            throw new IllegalArgumentException("parameter image is expected to contain at least one pixel");
        }

        // 2. mark the position of our neighbours if they exist
        for (int i = 0; i < neighbours.length; ++i) {
            // a. get the corresponding position
            int longitude = row + neighbourMapping[i][0], latitude = col + neighbourMapping[i][1];

            // b. handle the corner case (upper-left, upper-right, lower-left, and lower-right)
            if (longitude < 0 || latitude < 0 || longitude >= image.length || latitude >= image[0].length) {
                neighbours[i] = false;

            // c. mark the neighbour
            } else {
                neighbours[i] = image[longitude][latitude];
            }
        }

        // 3. return an array of neighbours
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
        int encounter = 0; // number of black pixels encountered

        // 1. count the number of encountered black pixels
        for (int i = 0; i < neighbours.length; ++i) {
            if (neighbours[i]) encounter++;
        }

        // 2. return the total count
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
        int transitions = 0; // number of transitions from white to black made

        // 1. count the number of essential transitions made (white to black)
        for (int i = 0; i < neighbours.length; ++i) {
            // 1. compute a rotating previous element index
            int previous = (i-1 < 0) ? (i-1 + neighbours.length) : (i-1);

            // 2. check if there were a notable transition
            if (neighbours[i] && !neighbours[previous]) transitions++;
        }

        // 2. return the total count
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
        // 1. check that the two images are of identical sizes
        if ((image1.length != image2.length) || (image1[0].length != image2[0].length)) {
            return false;
        }

        // 2. if a single pixel differs then the two images are different
        for (int i = 0; i < image1.length; ++i) {
            for (int j = 0; j < image1[0].length; ++j) {
                if (image1[i][j] != image2[i][j]) return false;
            }
        }

        // 3. otherwise the images are identical
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
        // 1. ensure the validity of our inputs
        if (step != 0 && step != 1) {
            throw new IllegalArgumentException("parameter step is expected to be either 0 or 1");
        }

        // 2. recursively discard redundant pixels
        for (int i = 0; i < image.length; ++i) {
            for (int j = 0; j < image[0].length; ++j) {
                // a. check that the pixel at (i, j) is black
                if (!image[i][j]) continue;

                // b. get the number of surrounding black neighbours
                boolean[] neighbours = getNeighbours(image, i, j);
                int neighbourCount = blackNeighbours(neighbours);

                // c. check that the pixel at (i, j) has more than 2 but less than 6 black neighbours
                if (neighbourCount < 2 || neighbourCount > 6) continue;

                // d. check that specific neighbours are white (different from step 0 and 1)
                int[][] indexes = { { 0, 2, 4, 2, 4, 6 }, { 0, 2, 6, 0, 4, 6 } }; // indexes where to expect white neighbours

                    // i. check that P0, P2, or P4/P6 are white neighbours
                if (neighbours[indexes[step][0]] && neighbours[indexes[step][1]] && neighbours[indexes[step][2]]) continue;

                    // ii. check that P2/P0, P4, or P6 are white neighbours
                if (neighbours[indexes[step][3]] && neighbours[indexes[step][4]] && neighbours[indexes[step][5]]) continue;

                // e. thin-out the pixel at (i, j)
                image[i][j] = false;
            }
        }

        // 3. return the thinned-out image
        return image;
    }

    /**
    * Compute the skeleton of a boolean image.
    *
    * @param image array containing each pixel's boolean value.
    * @return array containing the boolean value of each pixel of the image after
    *         applying the thinning algorithm.
    */
    public static boolean[][] thin(boolean[][] image) {
        boolean[][] thin1, thin2;

        // 1. thin-out the image as long as the line is not 1 pixel wide
        do {
            // a. execute step 1
            thin1 = thinningStep(image, 0);

            // b. execute step 2
            thin2 = thinningStep(image, 1);

        } while (!identical(thin1, thin2));

        // 3. return the 1 pixel thinned-out image
        return image;
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
        int searchRow = row, searchCol = col; // coordinates following a minutia path

        // 1. check that there is an actual minutia at the given row and column
        if (!image[row][col]) throw new IllegalArgumentException("expecting a set pixel at (row, col)");

        // 2. create a blank image to write the extracted minutia
        boolean[][] minutia = new boolean[image.length][image[0].length]; // blank image to work on

        // 3. compute the maximum/minimum coordinates of the operating field
        int minCol = (col - distance) < 0 ? 0 : (col - distance);
        int minRow = (row - distance) < 0 ? 0 : (row - distance);
        int maxCol = (col + distance) >= image[0].length ? image[0].length-1 : (col + distance);
        int maxRow = (row + distance) >= image.length ? image.length-1 : (row + distance);

        // 4. while there are pixels to mark, mark them
        while (true) {
            // a. mark the pixel at (search row, search col) if contained in the maximum distance
            if (searchRow <= maxRow && searchRow >= minRow) {
                if (searchCol <= maxCol && searchCol >= minCol) {
                    minutia[searchRow][searchCol] = true;
                }
            }

            // b. get the surrounding neighbour
            boolean[] neighbours = getNeighbours(image, searchRow, searchCol);

            // c. if all neigbours of the origin point are marked then exit
            if (searchRow == row && searchCol == col) {
                int marked = 0; // number of marked neighbours

                // i. count the number of marked neighbours
                for (int i = 0; i < neighbours.length; ++i) {
                    if (neighbours[i] && minutia[row+neighbourMapping[i][0]][col+neighbourMapping[i][1]]) {
                        marked++;
                    }
                }

                // ii. if all neighbours are marked then exit
                if (blackNeighbours(neighbours) == marked) break;
            }

            // c. find the coordinate of the first pixel transition (from white to black)
            int upcomingRow = 0, upcomingCol = 0; // potential path to be taken

            for (int i = 0; i < neighbours.length; ++i) {
                // i. compute a rotating previous element index
                int previous = (i-1 < 0) ? (i-1 + neighbours.length) : (i-1);

                // ii. memorize the coordinates if there is a notable transition
                if (neighbours[i] && !neighbours[previous]) {
                    upcomingRow = searchRow + neighbourMapping[i][0];
                    upcomingCol = searchCol + neighbourMapping[i][1];

                    if (!minutia[upcomingRow][upcomingCol]) {
                        if (searchRow <= maxRow && searchRow >= minRow) {
                            if (searchCol <= maxCol && searchCol >= minCol) {
                                searchRow = upcomingRow;
                                searchCol = upcomingCol;
                                break;
                            }
                        }
                    }
                }

                // iii. take the pass anyway if on the last iteration
                if (i == neighbours.length-1) {
                    searchRow = upcomingRow;
                    searchCol = upcomingCol;
                }
            }
        }

        // 5. return a new image of connected pixels
        return minutia;
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
        int xy = 0, x2 = 0, y2 = 0; // working variables

        // 1. recursively compute the three components of a linear regression
        for (int y = 0; y < connectedPixels.length; ++y) {
            for (int x = 0; x < connectedPixels[0].length; ++x) {
                // a. check that the current coordinates represent a pixel
                if (!connectedPixels[y][x]) continue;

                // b. compute the product of the coordinates (col x row)
                xy += ((x-col) * (y-row));

                // c. compute the square of the row coordinate
                y2 += Math.pow(x-col, 2);

                // d. compute the square of the col coordinate
                x2 += Math.pow(y-row, 2);
            }
        }

        // 2. handle the vertical slope case
        if (x2 == 0) return Double.POSITIVE_INFINITY;

        // 3. compute the general slope
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
        //TODO implement
        return 0;
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
        //TODO implement
        return 0;
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
        //TODO implement
        return null;
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
        //TODO implement
        return null;
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
        //TODO implement
        return null;
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
        //TODO implement
        return null;
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
        //TODO implement
        return null;
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
        //TODO implement
        return 0;
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
        //TODO implement
        return false;
    }
}

package seedu.address.model.book;

import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Arrays;
//@@author 592363789
/**
 * Represents a book's rating.
 * Guarantees: immutable.
 */
public class Rating implements Comparable<Rating> {

    public static final String MESSAGE_RATING_CONSTRAINTS = "Rating must be between -1 and 5 (both inclusive).";
    public static final int UNRATED_RATING = -1;
    public static final int MIN_RATING = 0;
    public static final int MAX_RATING = 5;
    public static final int DEFAULT_RATING = UNRATED_RATING;

    private static final char STAR = '\u2B50';
    private static final String DISPLAY_TEXT_UNRATED = "\u2B50 Unrated";
    private static final String STYLE_CLASS_UNRATED = "rating-unrated";
    private static final String STYLE_CLASS_RATED = "rating-rated";

    public final int rating;

    /**
     * Contructs a {@code Rating} with the default rating.
     */
    public Rating() {
        this.rating = DEFAULT_RATING;
    }

    /**
     * Constructs a {@code Rating}.
     *
     * @param rating A book rating.
     */
    public Rating(int rating) {
        checkArgument(isValidRating(rating), MESSAGE_RATING_CONSTRAINTS);
        this.rating = rating;
    }

    /**
     * Returns true if the given integer is a valid rating.
     */
    public static boolean isValidRating(int rating) {
        return rating == UNRATED_RATING || (rating >= MIN_RATING && rating <= MAX_RATING);
    }

    public String getDisplayText() {
        if (rating == UNRATED_RATING) {
            return DISPLAY_TEXT_UNRATED;
        }

        // returns a string with number of stars equal to rating
        char[] repeat = new char[rating];
        Arrays.fill(repeat, STAR);
        return new String(repeat);
    }

    public String getStyleClass() {
        if (rating == UNRATED_RATING) {
            return STYLE_CLASS_UNRATED;
        }
        return STYLE_CLASS_RATED;
    }

    @Override
    public int compareTo(Rating other) {
        return Integer.compare(rating, other.rating);
    }

    @Override
    public String toString() {
        return Integer.toString(rating);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Rating // instanceof handles nulls
                && this.rating == ((Rating) other).rating); // state check
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(rating);
    }

}

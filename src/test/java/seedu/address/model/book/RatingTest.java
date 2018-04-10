package seedu.address.model.book;
//@@author 592363789
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class RatingTest {

    @Test
    public void constructor_validRating_success() {
        assertEquals(-1, new Rating(-1).rating);
        assertEquals(5, new Rating(5).rating);
    }

    @Test
    public void constructor_invalidRating_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new Rating(-2));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Rating(6));
    }

    @Test
    public void hashCode_sameContent_returnsSameValue() {
        assertEquals(new Rating(-1).hashCode(), new Rating(-1).hashCode());
        assertEquals(new Rating(0).hashCode(), new Rating(0).hashCode());
    }

    @Test
    public void isValidRating() {
        assertTrue(Rating.isValidRating(-1));
        assertTrue(Rating.isValidRating(5));
        assertFalse(Rating.isValidRating(-2));
        assertFalse(Rating.isValidRating(6));
    }
}

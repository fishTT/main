package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_BOOK;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.book.Author;
import seedu.address.model.book.Category;
import seedu.address.model.book.Description;
import seedu.address.model.book.Title;
import seedu.address.testutil.Assert;

public class ParserUtilTest {

    private static final String VALID_TITLE = "Valid Title";
    private static final String VALID_DESCRIPTION = "Valid Description";
    private static final String VALID_AUTHOR_1 = "Author A";
    private static final String VALID_AUTHOR_2 = "Author B";
    private static final String VALID_CATEGORY_1 = "Category A";
    private static final String VALID_CATEGORY_2 = "Category B";

    private static final String WHITESPACE = " \t\r\n";

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseIndex_invalidInput_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        ParserUtil.parseIndex("10 a");
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(MESSAGE_INVALID_INDEX);
        ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_BOOK, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_BOOK, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseTitle_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseTitle((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseTitle((Optional<String>) null));
    }

    @Test
    public void parseTitle_optionalEmpty_returnsOptionalEmpty() {
        assertFalse(ParserUtil.parseTitle(Optional.empty()).isPresent());
    }

    @Test
    public void parseTitle_validValueWithoutWhitespace_returnsTitle() {
        Title expectedTitle = new Title(VALID_TITLE);
        assertEquals(expectedTitle, ParserUtil.parseTitle(VALID_TITLE));
        assertEquals(Optional.of(expectedTitle), ParserUtil.parseTitle(Optional.of(VALID_TITLE)));
    }

    @Test
    public void parseTitle_validValueWithWhitespace_returnsTrimmedTitle() {
        String titleWithWhitespace = WHITESPACE + VALID_TITLE + WHITESPACE;
        Title expectedTitle = new Title(VALID_TITLE);
        assertEquals(expectedTitle, ParserUtil.parseTitle(titleWithWhitespace));
        assertEquals(Optional.of(expectedTitle), ParserUtil.parseTitle(Optional.of(titleWithWhitespace)));
    }

    @Test
    public void parseDescription_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseDescription((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseDescription((Optional<String>) null));
    }

    @Test
    public void parseDescription_optionalEmpty_returnsOptionalEmpty() {
        assertFalse(ParserUtil.parseDescription(Optional.empty()).isPresent());
    }

    @Test
    public void parseDescription_validValueWithoutWhitespace_returnsDescription() {
        Description expectedDescription = new Description(VALID_DESCRIPTION);
        assertEquals(expectedDescription, ParserUtil.parseDescription(VALID_DESCRIPTION));
        assertEquals(Optional.of(expectedDescription), ParserUtil.parseDescription(Optional.of(VALID_DESCRIPTION)));
    }

    @Test
    public void parseDescription_validValueWithWhitespace_returnsTrimmedDescription() {
        String descWithWhitespace = WHITESPACE + VALID_DESCRIPTION + WHITESPACE;
        Description expectedDescription = new Description(VALID_DESCRIPTION);
        assertEquals(expectedDescription, ParserUtil.parseDescription(descWithWhitespace));
        assertEquals(Optional.of(expectedDescription), ParserUtil.parseDescription(Optional.of(descWithWhitespace)));
    }

    @Test
    public void parseAuthor_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseAuthor(null);
    }

    @Test
    public void parseAuthor_validValueWithoutWhitespace_returnsAuthor() {
        Author expectedAuthor = new Author(VALID_AUTHOR_1);
        assertEquals(expectedAuthor, ParserUtil.parseAuthor(VALID_AUTHOR_1));
    }

    @Test
    public void parseAuthors_collectionWithValidAuthors_returnsAuthorSet() {
        Set<Author> actualAuthorSet = ParserUtil.parseAuthors(Arrays.asList(VALID_AUTHOR_1, VALID_AUTHOR_2));
        Set<Author> expectedAuthorSet =
                new HashSet<>(Arrays.asList(new Author(VALID_AUTHOR_1), new Author(VALID_AUTHOR_2)));

        assertEquals(expectedAuthorSet, actualAuthorSet);
    }

    @Test
    public void parseCategory_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseCategory(null);
    }

    @Test
    public void parseCategory_validValueWithoutWhitespace_returnsCategory() {
        Category expectedCategory = new Category(VALID_CATEGORY_1);
        assertEquals(expectedCategory, ParserUtil.parseCategory(VALID_CATEGORY_1));
    }

    @Test
    public void parseCategorys_collectionWithValidCategorys_returnsCategorySet() {
        Set<Category> actualCategorySet = ParserUtil.parseCategories(Arrays.asList(VALID_CATEGORY_1, VALID_CATEGORY_2));
        Set<Category> expectedCategorySet =
                new HashSet<>(Arrays.asList(new Category(VALID_CATEGORY_1), new Category(VALID_CATEGORY_2)));

        assertEquals(expectedCategorySet, actualCategorySet);
    }

    @Test
    public void parseRating_validRating_success() throws ParseException {
        assertEquals(-1, ParserUtil.parseRating("-1").rating);
        assertEquals(0, ParserUtil.parseRating("0").rating);
        assertEquals(5, ParserUtil.parseRating("5").rating);
    }

    @Test
    public void parseRating_invalidRating_throwsParseException() throws ParseException {
        thrown.expect(ParseException.class);
        ParserUtil.parseRating("6");
    }
}

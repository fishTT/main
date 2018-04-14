package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.book.Author;
import seedu.address.model.book.Category;
import seedu.address.model.book.Description;
import seedu.address.model.book.Priority;
import seedu.address.model.book.Rating;
import seedu.address.model.book.Status;
import seedu.address.model.book.Title;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 * {@code ParserUtil} contains methods that take in {@code Optional} as parameters. However, it goes against Java's
 * convention (see https://stackoverflow.com/a/39005452) as {@code Optional} should only be used a return type.
 * Justification: The methods in concern receive {@code Optional} return values from other methods as parameters and
 * return {@code Optional} values based on whether the parameters were present. Therefore, it is redundant to unwrap the
 * initial {@code Optional} before passing to {@code ParserUtil} as a parameter and then re-wrap it into an
 * {@code Optional} return value inside {@code ParserUtil} methods.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws IllegalValueException if the specified index is invalid (not non-zero unsigned integer).
     */
    protected static Index parseIndex(String oneBasedIndex) throws IllegalValueException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new IllegalValueException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String title} into a {@code Title}.
     * Leading and trailing whitespaces will be trimmed.
     */
    protected static Title parseTitle(String title) {
        requireNonNull(title);
        String trimmedTitle = title.trim();
        return new Title(trimmedTitle);
    }

    /**
     * Parses a {@code Optional<String> title} into an {@code Optional<Title>} if {@code title} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    protected static Optional<Title> parseTitle(Optional<String> title) {
        requireNonNull(title);
        return title.isPresent() ? Optional.of(parseTitle(title.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String author} into a {@code Author}.
     * Leading and trailing whitespaces will be trimmed.
     */
    protected static Author parseAuthor(String author) {
        requireNonNull(author);
        String trimmedAuthor = author.trim();
        return new Author(trimmedAuthor);
    }

    /**
     * Parses {@code Collection<String> authors} into a {@code Set<Author>}.
     */
    protected static Set<Author> parseAuthors(Collection<String> authors) {
        requireNonNull(authors);
        final Set<Author> authorSet = new HashSet<>();
        for (String author : authors) {
            authorSet.add(parseAuthor(author));
        }
        return authorSet;
    }

    /**
     * Parses a {@code String category} into a {@code Category}.
     * Leading and trailing whitespaces will be trimmed.
     */
    protected static Category parseCategory(String category) {
        requireNonNull(category);
        String trimmedCategory = category.trim();
        return new Category(trimmedCategory);
    }

    /**
     * Parses {@code Collection<String> categories} into a {@code Set<Category>}.
     */
    protected static Set<Category> parseCategories(Collection<String> categories) {
        requireNonNull(categories);
        final Set<Category> categorySet = new HashSet<>();
        for (String category : categories) {
            categorySet.add(parseCategory(category));
        }
        return categorySet;
    }

    /**
     * Parses a {@code String description} into a {@code Description}.
     * Leading and trailing whitespaces will be trimmed.
     */
    protected static Description parseDescription(String description) {
        requireNonNull(description);
        String trimmedDescription = description.trim();
        return new Description(trimmedDescription);
    }

    /**
     * Parses a {@code Optional<String> description} into an {@code Optional<Description>}
     * if {@code description} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    protected static Optional<Description> parseDescription(Optional<String> description) {
        requireNonNull(description);
        return description.isPresent() ? Optional.of(parseDescription(description.get())) : Optional.empty();
    }

    /**
     * Parses the given {@code statusString} and returns a {@code Status}.
     *
     * @throws ParseException if the string does not represent a valid status.
     */
    protected static Status parseStatus(String statusString) throws ParseException {
        Status status = Status.findStatus(statusString);
        if (status == null) {
            throw new ParseException(Messages.MESSAGE_INVALID_STATUS);
        }
        return status;
    }

    /**
     * Parses the given {@code priorityString} and returns a {@code Priority}.
     *
     * @throws ParseException if the string does not represent a valid priority.
     */
    protected static Priority parsePriority(String priorityString) throws ParseException {
        Priority priority = Priority.findPriority(priorityString);
        if (priority == null) {
            throw new ParseException(Messages.MESSAGE_INVALID_PRIORITY);
        }
        return priority;
    }

    /**
     * Parses the given {@code ratingString} and returns a {@code Rating}.
     *
     * @throws ParseException if the string does not represent a valid rating.
     */
    protected static Rating parseRating(String ratingString) throws ParseException {
        try {
            return new Rating(Integer.parseInt(ratingString));
        } catch (IllegalArgumentException e) {
            throw new ParseException(Messages.MESSAGE_INVALID_RATING);
        }
    }
}

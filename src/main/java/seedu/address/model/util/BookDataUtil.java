package seedu.address.model.util;

import java.util.ArrayList;
import java.util.List;

import seedu.address.model.book.Author;
import seedu.address.model.book.Category;

/**
 * Contains utility methods for manipulating book data.
 */
public class BookDataUtil {

    /**
     * Returns an author list containing the list of strings given.
     */
    public static List<Author> getAuthorList(String... strings) {
        ArrayList<Author> authors = new ArrayList<>();
        for (String s : strings) {
            authors.add(new Author(s));
        }

        return authors;
    }

    /**
     * Returns a category list containing the list of strings given.
     */
    public static List<Category> getCategoryList(String... strings) {
        ArrayList<Category> categories = new ArrayList<>();
        for (String s : strings) {
            categories.add(new Category(s));
        }

        return categories;
    }

}

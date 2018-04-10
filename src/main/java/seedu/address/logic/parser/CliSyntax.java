package seedu.address.logic.parser;

import java.util.Arrays;
import java.util.List;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {
    /* Prefix String Definitions */
    public static final String PREFIX_EMPTY_STRING = "";
    public static final String PREFIX_AUTHOR_STRING = "a/";
    public static final String PREFIX_CATEGORY_STRING = "c/";
    public static final String PREFIX_DESCRIPTION_STRING = "d/";
    public static final String PREFIX_ISBN_STRING = "i/";
    public static final String PREFIX_TITLE_STRING = "t/";
    public static final String PREFIX_STATUS_STRING = "s/";
    public static final String PREFIX_PRIORITY_STRING = "p/";
    public static final String PREFIX_RATING_STRING = "r/";
    public static final String PREFIX_REMARK_STRING = "re/";
    public static final String PREFIX_SORT_BY_STRING = "by/";

    /* Prefix definitions */
    public static final Prefix PREFIX_EMPTY = new Prefix(PREFIX_EMPTY_STRING);
    public static final Prefix PREFIX_AUTHOR = new Prefix(PREFIX_AUTHOR_STRING);
    public static final Prefix PREFIX_CATEGORY = new Prefix(PREFIX_CATEGORY_STRING);
    public static final Prefix PREFIX_DESCRIPTION = new Prefix(PREFIX_DESCRIPTION_STRING);
    public static final Prefix PREFIX_ISBN = new Prefix(PREFIX_ISBN_STRING);
    public static final Prefix PREFIX_TITLE = new Prefix(PREFIX_TITLE_STRING);
    public static final Prefix PREFIX_STATUS = new Prefix(PREFIX_STATUS_STRING);
    public static final Prefix PREFIX_PRIORITY = new Prefix(PREFIX_PRIORITY_STRING);
    public static final Prefix PREFIX_RATING = new Prefix(PREFIX_RATING_STRING);
    public static final Prefix PREFIX_REMARK = new Prefix(PREFIX_REMARK_STRING);
    public static final Prefix PREFIX_SORT_BY = new Prefix(PREFIX_SORT_BY_STRING);


    public static final List<Prefix> LIST_OF_PREFIXES =
            Arrays.asList(
                    PREFIX_EMPTY, PREFIX_AUTHOR, PREFIX_CATEGORY, PREFIX_DESCRIPTION, PREFIX_ISBN,
                    PREFIX_TITLE, PREFIX_STATUS, PREFIX_PRIORITY, PREFIX_RATING, PREFIX_SORT_BY);
}

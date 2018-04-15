package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    // Prefix String Definitions
    protected static final String PREFIX_AUTHOR_STRING = "a/";
    protected static final String PREFIX_CATEGORY_STRING = "c/";
    protected static final String PREFIX_ISBN_STRING = "i/";
    protected static final String PREFIX_TITLE_STRING = "t/";
    protected static final String PREFIX_STATUS_STRING = "s/";
    protected static final String PREFIX_PRIORITY_STRING = "p/";
    protected static final String PREFIX_RATING_STRING = "r/";
    protected static final String PREFIX_SORT_BY_STRING = "by/";
    protected static final String PREFIX_OLD_STRING = "old/";
    protected static final String PREFIX_NEW_STRING = "new/";
    protected static final String PREFIX_COMMAND_STRING = "cmd/";

    // Prefix definitions
    public static final Prefix PREFIX_AUTHOR = new Prefix(PREFIX_AUTHOR_STRING);
    public static final Prefix PREFIX_CATEGORY = new Prefix(PREFIX_CATEGORY_STRING);
    public static final Prefix PREFIX_ISBN = new Prefix(PREFIX_ISBN_STRING);
    public static final Prefix PREFIX_TITLE = new Prefix(PREFIX_TITLE_STRING);
    public static final Prefix PREFIX_STATUS = new Prefix(PREFIX_STATUS_STRING);
    public static final Prefix PREFIX_PRIORITY = new Prefix(PREFIX_PRIORITY_STRING);
    public static final Prefix PREFIX_RATING = new Prefix(PREFIX_RATING_STRING);
    public static final Prefix PREFIX_SORT_BY = new Prefix(PREFIX_SORT_BY_STRING);
    public static final Prefix PREFIX_OLD = new Prefix(PREFIX_OLD_STRING);
    public static final Prefix PREFIX_NEW = new Prefix(PREFIX_NEW_STRING);
    public static final Prefix PREFIX_COMMAND = new Prefix(PREFIX_COMMAND_STRING);
}

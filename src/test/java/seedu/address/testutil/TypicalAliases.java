package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.alias.Alias;
import seedu.address.model.alias.UniqueAliasList;

/**
 * A utility class containing a list of {@code Alias} objects to be used in tests.
 */
public class TypicalAliases {

    public static final Alias REMOVE = new Alias("rm", "delete", "");
    public static final Alias SEARCH = new Alias("s", "search", "");
    public static final Alias UNREAD = new Alias("urd", "list", "s/unread");

    private TypicalAliases() {}

    public static UniqueAliasList getTypicalAliasList() {
        UniqueAliasList aliasList = new UniqueAliasList();
        getTypicalAliases().forEach(aliasList::add);
        return aliasList;
    }

    public static List<Alias> getTypicalAliases() {
        return new ArrayList<>(Arrays.asList(REMOVE, SEARCH, UNREAD));
    }
}

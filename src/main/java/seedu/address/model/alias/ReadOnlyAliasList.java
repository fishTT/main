package seedu.address.model.alias;

import java.util.Optional;

import javafx.collections.ObservableList;

/**
 * Unmodifiable view of an alias list.
 */
public interface ReadOnlyAliasList {

    /**
     * Returns an {@code Optional} containing the alias that matches the specified {@code name}.
     * Returns an empty {@code Optional} if no matching alias was found.
     */
    Optional<Alias> getAliasByName(String name);

    /**
     * Returns an unmodifiable view of the alias list.
     */
    ObservableList<Alias> getAliasList();

    /**
     * Returns the number of aliases contained in this alias list.
     */
    int size();
}

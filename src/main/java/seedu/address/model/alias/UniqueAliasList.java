package seedu.address.model.alias;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import javafx.collections.ObservableList;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.UniqueList;

//@@author takuyakanbr
/**
 * Represents a unique collection of aliases. Does not allow nulls.
 *
 * Supports a minimal set of list operations.
 */
public class UniqueAliasList extends UniqueList<Alias> implements ReadOnlyAliasList {

    /**
     * Creates an empty {@code UniqueAliasList}.
     */
    public UniqueAliasList() {
        super();
    }

    /**
     * Creates a {@code UniqueAliasList} using the aliases in the {@code toBeCopied}.
     */
    public UniqueAliasList(ReadOnlyAliasList toBeCopied) {
        super();
        requireNonNull(toBeCopied);
        toBeCopied.getAliasList().forEach(this::add);
    }

    /**
     * Adds an alias to this list. If there is an existing alias
     * with the same name, that alias will be replaced.
     */
    @Override
    public void add(Alias toAdd) {
        requireNonNull(toAdd);
        getAliasByName(toAdd.getName()).ifPresent(internalList::remove);
        internalList.add(toAdd);
        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Removes the alias with the specified {@code name}.
     * Does nothing if no matching alias was found.
     */
    public void remove(String name) {
        requireNonNull(name);
        getAliasByName(name).ifPresent(internalList::remove);
    }

    @Override
    public Optional<Alias> getAliasByName(String name) {
        requireNonNull(name);
        return internalList.stream()
                .filter(alias -> name.trim().equalsIgnoreCase(alias.getName()))
                .findFirst();
    }

    @Override
    public ObservableList<Alias> getAliasList() {
        return asObservableList();
    }

    @Override
    public int size() {
        return internalList.size();
    }
}

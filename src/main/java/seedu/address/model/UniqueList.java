package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DuplicateDataException;
import seedu.address.commons.util.CollectionUtil;

/**
 * A list of items that enforces no nulls and uniqueness between its elements.
 *
 * Supports minimal set of list operations for the app's features.
 */
public class UniqueList<T> implements Iterable<T> {

    private static final Logger logger = LogsCenter.getLogger(UniqueList.class);

    protected final ObservableList<T> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty UniqueList.
     */
    public UniqueList() {}

    /**
     * Returns all items in this list as a list.
     * This list is mutable and change-insulated against the internal list.
     */
    public List<T> toList() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return new ArrayList<>(internalList);
    }

    /**
     * Replaces the items in this list with those in the given collection.
     */
    public void setItems(Collection<T> items) throws DuplicateDataException {
        requireAllNonNull(items);
        internalList.clear();
        for (T toAdd : items) {
            add(toAdd);
        }
        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Ensures every item in the argument list exists in this object.
     */
    public void mergeFrom(UniqueList<T> from) {
        final List<T> alreadyInside = this.toList();
        from.internalList.stream()
                .filter(tag -> !alreadyInside.contains(tag))
                .forEach(internalList::add);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Returns true if the list contains an equivalent item as the given argument.
     */
    public boolean contains(T toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds an item to the list.
     *
     * @throws DuplicateItemException if the item to add is a duplicate of an existing item in the list.
     */
    public void add(T toAdd) throws DuplicateDataException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateItemException();
        }
        internalList.add(toAdd);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Adds all items in the given collection into this list.
     * Any duplicates found will be ignored.
     */
    public void addAllIgnoresDuplicates(Collection<T> items) {
        requireAllNonNull(items);
        for (T toAdd : items) {
            try {
                add(toAdd);
            } catch (DuplicateDataException e) {
                logger.fine("Duplicate data: " + toAdd);
            }
        }
        assert CollectionUtil.elementsAreUnique(internalList);
    }

    @Override
    public Iterator<T> iterator() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return internalList.iterator();
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<T> asObservableList() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public boolean equals(Object other) {
        assert CollectionUtil.elementsAreUnique(internalList);
        return other == this // short circuit if same object
                || (other instanceof UniqueList // instanceof handles nulls
                && this.internalList.equals(((UniqueList) other).internalList));
    }

    /**
     * Returns true if the element in this list is equal to the elements in {@code other}.
     * The elements do not have to be in the same order.
     */
    public boolean equalsOrderInsensitive(UniqueList<T> other) {
        assert CollectionUtil.elementsAreUnique(internalList);
        assert CollectionUtil.elementsAreUnique(other.internalList);
        return this == other || new HashSet<>(this.internalList).equals(new HashSet<>(other.internalList));
    }

    @Override
    public int hashCode() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return internalList.hashCode();
    }

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class DuplicateItemException extends DuplicateDataException {
        protected DuplicateItemException() {
            super("Operation would result in duplicate items");
        }
    }

}

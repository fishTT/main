package seedu.address.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.alias.ReadOnlyAliasList;

/**
 * Represents a storage for {@link seedu.address.model.alias.UniqueAliasList}.
 */
public interface AliasListStorage {

    /**
     * Returns alias list data as a {@link ReadOnlyAliasList}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyAliasList> readAliasList() throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyAliasList} to the storage.
     *
     * @param aliasList cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveAliasList(ReadOnlyAliasList aliasList) throws IOException;
}

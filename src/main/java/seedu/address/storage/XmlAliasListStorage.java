package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.alias.ReadOnlyAliasList;

/**
 * Accesses and stores alias list data as an xml file on the hard disk.
 */
public class XmlAliasListStorage implements AliasListStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlAliasListStorage.class);

    private final String filePath;

    public XmlAliasListStorage(String filePath) {
        requireNonNull(filePath);
        this.filePath = filePath;
    }

    @Override
    public Optional<ReadOnlyAliasList> readAliasList() throws DataConversionException, IOException {
        return readAliasList(filePath);
    }

    /**
     * @see #readAliasList()
     */
    public Optional<ReadOnlyAliasList> readAliasList(String filePath) throws DataConversionException, IOException {
        File aliasListFile = new File(filePath);

        if (!aliasListFile.exists()) {
            logger.info("Alias list file "  + aliasListFile + " not found");
            return Optional.empty();
        }

        XmlSerializableAliasList xmlAliasList = XmlFileStorage.loadAliasListDataFromFile(new File(filePath));
        try {
            return Optional.of(xmlAliasList.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + aliasListFile + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveAliasList(ReadOnlyAliasList aliasList) throws IOException {
        saveAliasList(aliasList, filePath);
    }

    /**
     * @see #saveAliasList(ReadOnlyAliasList)
     */
    public void saveAliasList(ReadOnlyAliasList aliasList, String filePath) throws IOException {
        requireNonNull(aliasList);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveAliasListDataToFile(file, new XmlSerializableAliasList(aliasList));
    }
}

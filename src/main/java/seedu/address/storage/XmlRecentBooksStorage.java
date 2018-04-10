package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyBookShelf;

//@@author qiu-siqi
/**
 * A class to access recently selected books data stored as an xml file on the hard disk.
 */
public class XmlRecentBooksStorage implements RecentBooksStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlRecentBooksStorage.class);

    private String filePath;

    public XmlRecentBooksStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getRecentBooksFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyBookShelf> readRecentBooksList() throws DataConversionException, IOException {
        return readRecentBooksList(filePath);
    }

    /**
     * Similar to {@link #readRecentBooksList()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyBookShelf> readRecentBooksList(String filePath) throws DataConversionException,
            FileNotFoundException {
        requireNonNull(filePath);

        File file = new File(filePath);

        if (!file.exists()) {
            logger.info("Recent books file "  + file + " not found");
            return Optional.empty();
        }

        XmlSerializableBookShelf xmlRecentBooksList = XmlFileStorage.loadBookShelfDataFromFile(new File(filePath));
        try {
            return Optional.of(xmlRecentBooksList.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + file + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveRecentBooksList(ReadOnlyBookShelf recentBooksList) throws IOException {
        saveRecentBooksList(recentBooksList, filePath);
    }

    /**
     * Similar to {@link #saveRecentBooksList(ReadOnlyBookShelf)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveRecentBooksList(ReadOnlyBookShelf recentBooksList, String filePath) throws IOException {
        requireNonNull(recentBooksList);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveBookShelfDataToFile(file, new XmlSerializableBookShelf(recentBooksList));
    }
}

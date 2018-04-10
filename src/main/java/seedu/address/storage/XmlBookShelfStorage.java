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

/**
 * A class to access BookShelf data stored as an xml file on the hard disk.
 */
public class XmlBookShelfStorage implements BookShelfStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlBookShelfStorage.class);

    private String filePath;

    public XmlBookShelfStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getBookShelfFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyBookShelf> readBookShelf() throws DataConversionException, IOException {
        return readBookShelf(filePath);
    }

    /**
     * Similar to {@link #readBookShelf()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyBookShelf> readBookShelf(String filePath) throws DataConversionException,
                                                                             FileNotFoundException {
        requireNonNull(filePath);

        File bookShelfFile = new File(filePath);

        if (!bookShelfFile.exists()) {
            logger.info("BookShelf file "  + bookShelfFile + " not found");
            return Optional.empty();
        }

        XmlSerializableBookShelf xmlBookShelf = XmlFileStorage.loadBookShelfDataFromFile(new File(filePath));
        try {
            return Optional.of(xmlBookShelf.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + bookShelfFile + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveBookShelf(ReadOnlyBookShelf bookShelf) throws IOException {
        saveBookShelf(bookShelf, filePath);
    }

    /**
     * Similar to {@link #saveBookShelf(ReadOnlyBookShelf)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveBookShelf(ReadOnlyBookShelf bookShelf, String filePath) throws IOException {
        requireNonNull(bookShelf);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveBookShelfDataToFile(file, new XmlSerializableBookShelf(bookShelf));
    }

}

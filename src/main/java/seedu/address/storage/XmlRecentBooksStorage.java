package seedu.address.storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyBookShelf;

//@@author qiu-siqi
/**
 * A class to access recently selected books data stored as an xml file on the hard disk.
 */
public class XmlRecentBooksStorage implements RecentBooksStorage {

    private XmlBookShelfStorage xmlBookShelfStorage;

    public XmlRecentBooksStorage(String filePath) {
        xmlBookShelfStorage = new XmlBookShelfStorage(filePath);
    }

    public String getRecentBooksFilePath() {
        return xmlBookShelfStorage.getBookShelfFilePath();
    }

    @Override
    public Optional<ReadOnlyBookShelf> readRecentBooksList() throws DataConversionException, IOException {
        return xmlBookShelfStorage.readBookShelf();
    }

    /**
     * Similar to {@link #readRecentBooksList()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyBookShelf> readRecentBooksList(String filePath) throws DataConversionException,
            FileNotFoundException {
        return xmlBookShelfStorage.readBookShelf(filePath);
    }

    @Override
    public void saveRecentBooksList(ReadOnlyBookShelf recentBooksList) throws IOException {
        xmlBookShelfStorage.saveBookShelf(recentBooksList);
    }

    /**
     * Similar to {@link #saveRecentBooksList(ReadOnlyBookShelf)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveRecentBooksList(ReadOnlyBookShelf recentBooksList, String filePath) throws IOException {
        xmlBookShelfStorage.saveBookShelf(recentBooksList, filePath);
    }
}

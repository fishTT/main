package seedu.address.storage;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.XmlUtil;

/**
 * Stores bookshelf and alias list data in XML files.
 */
public class XmlFileStorage {

    /**
     * Saves the given bookshelf data to the specified file.
     */
    public static void saveBookShelfDataToFile(File file, XmlSerializableBookShelf bookShelf)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, bookShelf);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage());
        }
    }

    /**
     * Returns book shelf in the file or an empty book shelf.
     */
    public static XmlSerializableBookShelf loadBookShelfDataFromFile(File file) throws DataConversionException,
            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableBookShelf.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

    /**
     * Saves the given alias list data to the specified file.
     */
    public static void saveAliasListDataToFile(File file, XmlSerializableAliasList aliasList)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, aliasList);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage());
        }
    }

    /**
     * Returns alias list in the file or an empty alias list.
     */
    public static XmlSerializableAliasList loadAliasListDataFromFile(File file) throws DataConversionException,
            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableAliasList.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}

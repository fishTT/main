package seedu.address.network.api.google;

import java.util.logging.Logger;
import java.util.stream.Stream;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.BookShelf;
import seedu.address.model.ReadOnlyBookShelf;
import seedu.address.model.book.Book;
import seedu.address.model.book.Description;
import seedu.address.model.book.Gid;
import seedu.address.model.book.Isbn;
import seedu.address.model.book.PublicationDate;
import seedu.address.model.book.Publisher;
import seedu.address.model.book.Title;
import seedu.address.model.book.exceptions.DuplicateBookException;
import seedu.address.model.book.exceptions.InvalidBookException;
import seedu.address.model.util.BookDataUtil;

//@@author takuyakanbr
/**
 * A temporary data holder used for deserialization of the JSON response
 * from the book searching endpoint of Google Books API.
 */
public class JsonSearchResults {

    private static final Logger logger = LogsCenter.getLogger(JsonSearchResults.class);

    private int error = 0;
    private int totalItems = 0;
    private JsonVolume[] items = new JsonVolume[0];

    // This should fail if the API returns an error, due to incompatible types.
    public void setError(int error) {
        this.error = error;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public void setItems(JsonVolume[] items) {
        this.items = items;
    }

    /**
     * Converts this data holder object into the model's BookShelf object.
     */
    public ReadOnlyBookShelf toModelType() {
        BookShelf bookShelf = new BookShelf();

        for (JsonVolume volume : items) {
            try {
                Book book = convertToBook(volume);
                bookShelf.addBook(book);
            } catch (InvalidBookException | DuplicateBookException e) {
                logger.warning(e.getMessage());
            }
        }
        return bookShelf;
    }

    /**
     * Converts a JsonVolume into a Book.
     */
    private static Book convertToBook(JsonVolume volume) throws InvalidBookException {
        JsonVolumeInfo volumeInfo = volume.volumeInfo;
        Isbn isbn = getIsbnFromIndustryIdentifiers(volumeInfo.industryIdentifiers);

        if (isbn == null) {
            throw new InvalidBookException("Found book without ISBN");
        }

        return new Book(new Gid(volume.id), isbn,
                BookDataUtil.getAuthorList(volumeInfo.authors), new Title(volumeInfo.title),
                BookDataUtil.getCategoryList(volumeInfo.categories), new Description(volumeInfo.description),
                new Publisher(volumeInfo.publisher), new PublicationDate(volumeInfo.publishedDate));
    }

    private static Isbn getIsbnFromIndustryIdentifiers(JsonIndustryIdentifiers[] iiArray) {
        return Stream.of(iiArray)
                .filter(ii -> ii.type.equals("ISBN_13"))
                .findFirst()
                .map(ii -> new Isbn(ii.identifier))
                .orElse(null);
    }


    /** Temporary data holder used for deserialization. */
    private static class JsonVolume {
        private String id = "";
        private JsonVolumeInfo volumeInfo = new JsonVolumeInfo();

        //@@author
        public void setId(String id) {
            this.id = id;
        }

        public void setVolumeInfo(JsonVolumeInfo volumeInfo) {
            this.volumeInfo = volumeInfo;
        }
    }

    //@@author takuyakanbr
    /** Temporary data holder used for deserialization. */
    private static class JsonVolumeInfo {
        private String title = "";
        private String[] authors = new String[0];
        private String publisher = "";
        private String publishedDate = "";
        private String description = "";
        private JsonIndustryIdentifiers[] industryIdentifiers = new JsonIndustryIdentifiers[0];
        private String[] categories = new String[0];

        //@@author
        public void setIndustryIdentifiers(JsonIndustryIdentifiers[] industryIdentifiers) {
            this.industryIdentifiers = industryIdentifiers;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setAuthors(String[] authors) {
            this.authors = authors;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public void setPublishedDate(String publishedDate) {
            this.publishedDate = publishedDate;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setCategories(String[] categories) {
            this.categories = categories;
        }
    }

    /** Temporary data holder used for deserialization. */
    private static class JsonIndustryIdentifiers {
        private String type;
        private String identifier;

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

package seedu.address.network.api.google;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.model.book.Book;
import seedu.address.model.book.Category;
import seedu.address.model.book.Description;
import seedu.address.model.book.Gid;
import seedu.address.model.book.Isbn;
import seedu.address.model.book.PublicationDate;
import seedu.address.model.book.Publisher;
import seedu.address.model.book.Title;
import seedu.address.model.book.exceptions.InvalidBookException;
import seedu.address.model.util.BookDataUtil;

//@@author takuyakanbr
/**
 * A temporary data holder used for deserialization of the JSON response
 * from the book details endpoint of Google Books API.
 */
public class JsonBookDetails {
    private int error = 0;
    private String id = "";
    private JsonVolumeInfo volumeInfo = new JsonVolumeInfo();

    // This should fail if the API returns an error, due to incompatible types.
    public void setError(int error) {
        this.error = error;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVolumeInfo(JsonVolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    /**
     * Converts this data holder object into the model's Book object.
     */
    public Book toModelType() throws InvalidBookException {
        Isbn isbn = getIsbnFromIndustryIdentifiers(volumeInfo.industryIdentifiers);

        if (isbn == null) {
            throw new InvalidBookException("No ISBN is found for the book.");
        }

        return new Book(new Gid(id), isbn,
                BookDataUtil.getAuthorList(volumeInfo.authors), new Title(volumeInfo.title),
                getCategoryList(volumeInfo.categories), new Description(volumeInfo.description),
                new Publisher(volumeInfo.publisher),
                new PublicationDate(volumeInfo.publishedDate));
    }

    private static Isbn getIsbnFromIndustryIdentifiers(JsonIndustryIdentifiers[] iiArray) {
        return Stream.of(iiArray)
                .filter(ii -> ii.type.equals("ISBN_13"))
                .findFirst()
                .map(ii -> new Isbn(ii.identifier))
                .orElse(null);
    }

    private static List<Category> getCategoryList(String[] categories) {
        return Stream.of(categories)
                .flatMap(category -> Stream.of(category.split("/")))
                .map(token -> new Category(token.trim()))
                .collect(Collectors.toList());
    }

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

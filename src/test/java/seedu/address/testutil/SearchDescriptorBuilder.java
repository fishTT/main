package seedu.address.testutil;

import seedu.address.logic.commands.SearchCommand.SearchDescriptor;

/**
 * A utility class to help with building SearchDescriptor objects.
 */
public class SearchDescriptorBuilder {
    private SearchDescriptor descriptor;

    public SearchDescriptorBuilder() {
        descriptor = new SearchDescriptor();
    }

    public SearchDescriptorBuilder(SearchDescriptor descriptor) {
        this.descriptor = new SearchDescriptor(descriptor);
    }

    /**
     * Sets the {@code keyWords} of the {@code SearchDescriptor} that we are building.
     */
    public SearchDescriptorBuilder withKeyWords(String keyWords) {
        descriptor.setKeyWords(keyWords);
        return this;
    }

    /**
     * Sets the {@code isbn} of the {@code SearchDescriptor} that we are building.
     */
    public SearchDescriptorBuilder withIsbn(String isbn) {
        descriptor.setIsbn(isbn);
        return this;
    }

    /**
     * Sets the {@code title} of the {@code SearchDescriptor} that we are building.
     */
    public SearchDescriptorBuilder withTitle(String title) {
        descriptor.setTitle(title);
        return this;
    }

    /**
     * Sets the {@code author} of the {@code SearchDescriptor} that we are building.
     */
    public SearchDescriptorBuilder withAuthor(String author) {
        descriptor.setAuthor(author);
        return this;
    }

    /**
     * Sets the {@code category} of the {@code SearchDescriptor} that we are building.
     */
    public SearchDescriptorBuilder withCategory(String category) {
        descriptor.setCategory(category);
        return this;
    }

    public SearchDescriptor build() {
        return descriptor;
    }
}

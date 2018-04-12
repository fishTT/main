package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.logic.commands.SearchCommand.SearchDescriptor;
import seedu.address.testutil.SearchDescriptorBuilder;

public class SearchDescriptorTest {

    @Test
    public void isAnyFieldEdited() {
        // has at least 1 modified field -> returns true
        SearchDescriptor descriptor = new SearchDescriptorBuilder().withAuthor("12345").build();
        assertTrue(descriptor.isValid());
        descriptor = new SearchDescriptorBuilder().withCategory("12345").build();
        assertTrue(descriptor.isValid());
        descriptor = new SearchDescriptorBuilder().withIsbn("12345").build();
        assertTrue(descriptor.isValid());
        descriptor = new SearchDescriptorBuilder().withKeyWords("12345").build();
        assertTrue(descriptor.isValid());
        descriptor = new SearchDescriptorBuilder().withTitle("12345").build();
        assertTrue(descriptor.isValid());

        // has no modified field -> returns false
        descriptor = new SearchDescriptorBuilder().build();
        assertFalse(descriptor.isValid());
    }

    @Test
    public void toString_equalsToSearchString() {
        SearchDescriptor descriptor =
                new SearchDescriptorBuilder().withAuthor("author1").withIsbn("12345").build();
        assertEquals(descriptor.toSearchString(), descriptor.toString());
        descriptor = new SearchDescriptorBuilder().withAuthor("12345").build();
        assertEquals(descriptor.toSearchString(), descriptor.toString());
        descriptor = new SearchDescriptorBuilder().build();
        assertEquals(descriptor.toSearchString(), descriptor.toString());
    }

    @Test
    public void equals() {
        SearchDescriptor descriptorA =
                new SearchDescriptorBuilder().withAuthor("author1").withIsbn("12345").build();

        // same values -> returns true
        SearchDescriptor descriptorWithSameValues =
                new SearchDescriptorBuilder().withAuthor("author1").withIsbn("12345").build();
        assertTrue(descriptorA.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(descriptorA.equals(descriptorA));

        // null -> returns false
        assertFalse(descriptorA.equals(null));

        // different types -> returns false
        assertFalse(descriptorA.equals(5));

        // different author -> returns false
        SearchDescriptor editedDescriptor =
                new SearchDescriptorBuilder().withAuthor("author2").withIsbn("12345").build();
        assertFalse(descriptorA.equals(editedDescriptor));

        // different category -> returns false
        editedDescriptor = new SearchDescriptorBuilder()
                .withAuthor("author1").withCategory("cat1").withIsbn("12345").build();
        assertFalse(descriptorA.equals(editedDescriptor));

        // different isbn -> returns false
        editedDescriptor = new SearchDescriptorBuilder()
                .withAuthor("author1").withIsbn("234567").build();
        assertFalse(descriptorA.equals(editedDescriptor));

        // different title -> returns false
        editedDescriptor = new SearchDescriptorBuilder()
                .withAuthor("author1").withIsbn("12345").withTitle("title2").build();
        assertFalse(descriptorA.equals(editedDescriptor));

        // different key word -> returns false
        editedDescriptor = new SearchDescriptorBuilder()
                .withAuthor("author1").withIsbn("12345").withKeyWords("search").build();
        assertFalse(descriptorA.equals(editedDescriptor));
    }
}

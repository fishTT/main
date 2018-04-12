package seedu.address.model.book;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.testutil.TypicalBooks;

//@@author qiu-siqi
public class UniqueBookCircularListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void addToFront_null_failure() throws Exception {
        UniqueBookCircularList uniqueBookCircularList = new UniqueBookCircularList(5);
        thrown.expect(NullPointerException.class);
        uniqueBookCircularList.addToFront(null);
    }

    @Test
    public void addToFront_validBook_success() throws Exception {
        UniqueBookCircularList uniqueBookCircularList = new UniqueBookCircularList();
        uniqueBookCircularList.addToFront(TypicalBooks.ARTEMIS);
        assertEquals(true, uniqueBookCircularList.asObservableList().contains(TypicalBooks.ARTEMIS));
    }

    @Test
    public void addToFront_repeatedBook_success() throws Exception {
        UniqueBookCircularList uniqueBookCircularList = new UniqueBookCircularList(5);
        uniqueBookCircularList.addToFront(TypicalBooks.ARTEMIS);
        uniqueBookCircularList.addToFront(TypicalBooks.ARTEMIS);

        ObservableList<Book> list = uniqueBookCircularList.asObservableList();
        assertEquals(true, list.contains(TypicalBooks.ARTEMIS));
        assertEquals(1, list.size());
    }

    @Test
    public void addToFront_latestBookAtFront_success() throws Exception {
        UniqueBookCircularList uniqueBookCircularList = new UniqueBookCircularList(5);
        uniqueBookCircularList.addToFront(TypicalBooks.ARTEMIS);
        uniqueBookCircularList.addToFront(TypicalBooks.BABYLON_ASHES);

        ObservableList<Book> list = uniqueBookCircularList.asObservableList();
        assertEquals(TypicalBooks.BABYLON_ASHES, list.get(0));
    }

    @Test
    public void addToFront_repeatedBookBroughtToFront_success() throws Exception {
        UniqueBookCircularList uniqueBookCircularList = new UniqueBookCircularList(5);
        uniqueBookCircularList.addToFront(TypicalBooks.ARTEMIS);
        uniqueBookCircularList.addToFront(TypicalBooks.BABYLON_ASHES);
        uniqueBookCircularList.addToFront(TypicalBooks.ARTEMIS);

        ObservableList<Book> list = uniqueBookCircularList.asObservableList();
        assertEquals(2, list.size());
        assertEquals(TypicalBooks.ARTEMIS, list.get(0));
        assertEquals(TypicalBooks.BABYLON_ASHES, list.get(1));
    }

    @Test
    public void addToFront_tooManyBooks_success() throws Exception {
        UniqueBookCircularList uniqueBookCircularList = new UniqueBookCircularList(2);
        uniqueBookCircularList.addToFront(TypicalBooks.ARTEMIS);
        uniqueBookCircularList.addToFront(TypicalBooks.BABYLON_ASHES);
        uniqueBookCircularList.addToFront(TypicalBooks.COLLAPSING_EMPIRE);

        ObservableList<Book> list = uniqueBookCircularList.asObservableList();
        assertEquals(2, list.size()); // max size 2
        assertEquals(false, list.contains(TypicalBooks.ARTEMIS)); // replaced
        assertEquals(true, list.contains(TypicalBooks.BABYLON_ASHES));
        assertEquals(true, list.contains(TypicalBooks.COLLAPSING_EMPIRE));
    }

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueBookCircularList uniqueBookCircularList = new UniqueBookCircularList(5);
        thrown.expect(UnsupportedOperationException.class);
        uniqueBookCircularList.asObservableList().remove(0);
    }

    @Test
    public void equals_sameObject_returnsTrue() throws Exception {
        UniqueBookCircularList uniqueBookCircularList = new UniqueBookCircularList(5);
        uniqueBookCircularList.addToFront(TypicalBooks.ARTEMIS);
        uniqueBookCircularList.addToFront(TypicalBooks.BABYLON_ASHES);
        assertEquals(true, uniqueBookCircularList.equals(uniqueBookCircularList));
    }

    @Test
    public void equals_sameContentSameOrder_returnsTrue() throws Exception {
        UniqueBookCircularList uniqueBookCircularList = new UniqueBookCircularList(5);
        uniqueBookCircularList.addToFront(TypicalBooks.ARTEMIS);
        uniqueBookCircularList.addToFront(TypicalBooks.BABYLON_ASHES);
        UniqueBookCircularList uniqueBookCircularList2 = new UniqueBookCircularList(5);
        uniqueBookCircularList2.addToFront(TypicalBooks.ARTEMIS);
        uniqueBookCircularList2.addToFront(TypicalBooks.BABYLON_ASHES);
        assertEquals(true, uniqueBookCircularList.equals(uniqueBookCircularList2));
    }

    @Test
    public void equals_sameContentDifferentOrder_returnsFalse() throws Exception {
        UniqueBookCircularList uniqueBookCircularList = new UniqueBookCircularList(5);
        uniqueBookCircularList.addToFront(TypicalBooks.ARTEMIS);
        uniqueBookCircularList.addToFront(TypicalBooks.BABYLON_ASHES);
        UniqueBookCircularList uniqueBookCircularList2 = new UniqueBookCircularList(5);
        uniqueBookCircularList2.addToFront(TypicalBooks.BABYLON_ASHES);
        uniqueBookCircularList2.addToFront(TypicalBooks.ARTEMIS);
        assertEquals(false, uniqueBookCircularList.equals(uniqueBookCircularList2));
    }

}

package seedu.address.model.book;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.book.exceptions.BookNotFoundException;
import seedu.address.model.book.exceptions.DuplicateBookException;
import seedu.address.testutil.TypicalBooks;

public class UniqueBookListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void setBook_validTargetAndReplacement_success() throws Exception {
        UniqueBookList uniqueBookList = new UniqueBookList();
        uniqueBookList.add(TypicalBooks.ARTEMIS);
        uniqueBookList.setBook(TypicalBooks.ARTEMIS, TypicalBooks.BABYLON_ASHES);
        assertEquals(false, uniqueBookList.contains(TypicalBooks.ARTEMIS));
        assertEquals(true, uniqueBookList.contains(TypicalBooks.BABYLON_ASHES));
    }

    @Test
    public void setBook_nonMatchingTarget_throwsBookNotFoundException() throws Exception {
        UniqueBookList uniqueBookList = new UniqueBookList();
        uniqueBookList.add(TypicalBooks.ARTEMIS);
        thrown.expect(BookNotFoundException.class);
        uniqueBookList.setBook(TypicalBooks.BABYLON_ASHES, TypicalBooks.WAKING_GODS);
    }

    @Test
    public void setBook_nullTarget_throwsBookNotFoundException() throws Exception {
        UniqueBookList uniqueBookList = new UniqueBookList();
        uniqueBookList.add(TypicalBooks.ARTEMIS);
        thrown.expect(BookNotFoundException.class);
        uniqueBookList.setBook(null, TypicalBooks.WAKING_GODS);
    }

    @Test
    public void setBook_nullReplacement_throwsNullPointerException() throws Exception {
        UniqueBookList uniqueBookList = new UniqueBookList();
        uniqueBookList.add(TypicalBooks.ARTEMIS);
        thrown.expect(NullPointerException.class);
        uniqueBookList.setBook(TypicalBooks.ARTEMIS, null);
    }

    @Test
    public void setBook_duplicateReplacement_throwsDuplicateBookException() throws Exception {
        UniqueBookList uniqueBookList = new UniqueBookList();
        uniqueBookList.add(TypicalBooks.ARTEMIS);
        uniqueBookList.add(TypicalBooks.BABYLON_ASHES);
        thrown.expect(DuplicateBookException.class);
        uniqueBookList.setBook(TypicalBooks.BABYLON_ASHES, TypicalBooks.ARTEMIS);
    }

    @Test
    public void remove_validBook_success() throws Exception {
        UniqueBookList uniqueBookList = new UniqueBookList();
        uniqueBookList.add(TypicalBooks.ARTEMIS);
        uniqueBookList.remove(TypicalBooks.ARTEMIS);
        assertEquals(0, uniqueBookList.toList().size());
    }

    @Test
    public void remove_nonMatchingBook_throwsBookNotFoundException() throws Exception {
        UniqueBookList uniqueBookList = new UniqueBookList();
        uniqueBookList.add(TypicalBooks.ARTEMIS);
        thrown.expect(BookNotFoundException.class);
        uniqueBookList.remove(TypicalBooks.BABYLON_ASHES);
    }

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueBookList uniqueBookList = new UniqueBookList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueBookList.asObservableList().remove(0);
    }

    @Test
    public void equals_sameContent_returnsTrue() throws Exception {
        UniqueBookList uniqueBookList = new UniqueBookList();
        uniqueBookList.add(TypicalBooks.ARTEMIS);
        uniqueBookList.add(TypicalBooks.BABYLON_ASHES);
        UniqueBookList uniqueBookList2 = new UniqueBookList();
        uniqueBookList2.add(TypicalBooks.ARTEMIS);
        uniqueBookList2.add(TypicalBooks.BABYLON_ASHES);
        assertEquals(true, uniqueBookList.equals(uniqueBookList2));
    }

}

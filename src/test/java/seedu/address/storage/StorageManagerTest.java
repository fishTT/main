package seedu.address.storage;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalAliases.getTypicalAliasList;
import static seedu.address.testutil.TypicalBooks.getTypicalBookShelf;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.events.model.AliasListChangedEvent;
import seedu.address.commons.events.model.BookShelfChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.model.BookShelf;
import seedu.address.model.ReadOnlyBookShelf;
import seedu.address.model.UserPrefs;
import seedu.address.model.alias.ReadOnlyAliasList;
import seedu.address.model.alias.UniqueAliasList;
import seedu.address.ui.testutil.EventsCollectorRule;

public class StorageManagerTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private StorageManager storageManager;

    @Before
    public void setUp() {
        XmlBookShelfStorage bookShelfStorage = new XmlBookShelfStorage(getTempFilePath("biblio"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        XmlRecentBooksStorage recentBooksStorage = new XmlRecentBooksStorage(getTempFilePath("recent"));
        XmlAliasListStorage aliasListStorage = new XmlAliasListStorage(getTempFilePath("alias"));
        storageManager = new StorageManager(bookShelfStorage, userPrefsStorage, recentBooksStorage, aliasListStorage);
    }

    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void bookShelfReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link XmlBookShelfStorage} class.
         * More extensive testing of BookShelf saving/reading is done in {@link XmlBookShelfStorageTest} class.
         */
        BookShelf original = getTypicalBookShelf();
        storageManager.saveBookShelf(original);
        ReadOnlyBookShelf retrieved = storageManager.readBookShelf().get();
        assertEquals(original, new BookShelf(retrieved));
    }

    @Test
    public void getBookShelfFilePath() {
        assertNotNull(storageManager.getBookShelfFilePath());
    }

    @Test
    public void handleBookShelfChangedEvent_exceptionThrown_eventRaised() {
        // Create a StorageManager while injecting a stub that  throws an exception when the save method is called
        Storage storage = new StorageManager(
                new XmlBookShelfStorageExceptionThrowingStub("dummy"), new JsonUserPrefsStorage("dummy"),
                new XmlRecentBooksStorage("dummy"), new XmlAliasListStorage("dummy"));
        storage.handleBookShelfChangedEvent(new BookShelfChangedEvent(new BookShelf()));
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof DataSavingExceptionEvent);
    }

    @Test
    public void recentBooksReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link XmlRecentBooksStorage} class.
         * More extensive testing of recent books saving/reading is done in
         * {@link XmlRecentBooksStorageTest} class.
         */
        BookShelf original = getTypicalBookShelf();
        storageManager.saveRecentBooksList(original);
        ReadOnlyBookShelf retrieved = storageManager.readRecentBooksList().get();
        assertEquals(original, new BookShelf(retrieved));
    }

    @Test
    public void aliasListReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * XmlAliasListStorage class.
         * More extensive testing of alias list saving/reading is done in XmlAliasListStorageTest class.
         */
        UniqueAliasList original = getTypicalAliasList();
        storageManager.saveAliasList(original);
        ReadOnlyAliasList retrieved = storageManager.readAliasList().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void getRecentBooksFilePath() {
        assertNotNull(storageManager.getRecentBooksFilePath());
    }

    @Test
    public void handleAliasListChangedEvent_exceptionThrown_eventRaised() {
        // create a StorageManager while injecting a stub that throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlBookShelfStorage("dummy"), new JsonUserPrefsStorage("dummy"),
                new XmlRecentBooksStorage("dummy"), new XmlAliasListStorageExceptionThrowingStub("dummy"));
        storage.handleAliasListChangedEvent(new AliasListChangedEvent(new UniqueAliasList()));
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof DataSavingExceptionEvent);
    }

    /**
     * A Stub class to throw an exception when the save method is called.
     */
    private static class XmlBookShelfStorageExceptionThrowingStub extends XmlBookShelfStorage {

        private XmlBookShelfStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveBookShelf(ReadOnlyBookShelf bookShelf, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }

    /**
     * A Stub class to throw an exception when the save method is called.
     */
    private static class XmlAliasListStorageExceptionThrowingStub extends XmlAliasListStorage {

        private XmlAliasListStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveAliasList(ReadOnlyAliasList aliasList) throws IOException {
            throw new IOException("dummy exception");
        }
    }

}

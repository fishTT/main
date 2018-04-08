package seedu.address;

import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

import javafx.stage.Screen;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.WindowSettings;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.BookShelf;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyBookShelf;
import seedu.address.model.UserPrefs;
import seedu.address.storage.UserPrefsStorage;
import seedu.address.storage.XmlSerializableBookShelf;
import seedu.address.testutil.TestUtil;
import systemtests.ModelHelper;

/**
 * This class is meant to override some properties of MainApp so that it will be suited for
 * testing
 */
public class TestApp extends MainApp {

    public static final String APP_TITLE = "Test App";
    protected static final String BOOK_SHELF_NAME = "Test";

    private String saveFileLocation;
    private final String recentBooksFileLocation;
    private final String aliasListFileLocation;
    private final String prefFileLocation;

    public TestApp(Supplier<ReadOnlyBookShelf> initialDataSupplier, String saveFileLocation) {
        super();
        this.saveFileLocation = saveFileLocation;
        this.recentBooksFileLocation = TestUtil.getFilePathInSandboxFolder("recentbooks.xml");
        this.aliasListFileLocation = TestUtil.getFilePathInSandboxFolder("aliaslist.xml");
        this.prefFileLocation = TestUtil.getFilePathInSandboxFolder("pref_testing.json");

        // If some initial local data has been provided, write those to the file
        if (initialDataSupplier.get() != null) {
            createDataFileWithData(new XmlSerializableBookShelf(initialDataSupplier.get()),
                    this.saveFileLocation);
        }
    }

    @Override
    protected Config initConfig(String configFilePath) {
        Config config = super.initConfig(configFilePath);
        config.setAppTitle(APP_TITLE);
        config.setUserPrefsFilePath(prefFileLocation);
        config.setRecentBooksFilePath(recentBooksFileLocation);
        return config;
    }

    @Override
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        UserPrefs userPrefs = super.initPrefs(storage);
        double x = Screen.getPrimary().getVisualBounds().getMinX();
        double y = Screen.getPrimary().getVisualBounds().getMinY();
        userPrefs.updateLastUsedGuiSetting(new WindowSettings(800.0, 800.0, (int) x, (int) y));
        userPrefs.setBookShelfFilePath(saveFileLocation);
        userPrefs.setAliasListFilePath(aliasListFileLocation);
        userPrefs.setBookShelfName(BOOK_SHELF_NAME);
        return userPrefs;
    }

    /**
     * Returns a defensive copy of the book shelf data stored inside the storage file.
     */
    public BookShelf readStorageBookShelf() {
        try {
            return new BookShelf(storage.readBookShelf().get());
        } catch (DataConversionException dce) {
            throw new AssertionError("Data is not in the BookShelf format.");
        } catch (IOException ioe) {
            throw new AssertionError("Storage file cannot be found.");
        }
    }

    /**
     * Returns the file path of the storage file.
     */
    public String getStorageSaveLocation() {
        return storage.getBookShelfFilePath();
    }

    /**
     * Returns a defensive copy of the model.
     */
    public Model getModel() {
        Model copy = new ModelManager(model.getBookShelf(), new UserPrefs(),
                model.getRecentBooksListAsBookShelf(), model.getAliasList());
        copy.updateBookListFilter(model.getBookListFilter());
        copy.updateBookListSorter(model.getBookListSorter());
        ModelHelper.setSearchResults(copy, model.getSearchResultsList());
        ModelHelper.setRecentBooks(copy, model.getRecentBooksList());
        return copy;
    }

    @Override
    public void start(Stage primaryStage) {
        ui.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Creates an XML file at the {@code filePath} with the {@code data}.
     */
    private <T> void createDataFileWithData(T data, String filePath) {
        try {
            File saveFileForTesting = new File(filePath);
            FileUtil.createIfMissing(saveFileForTesting);
            XmlUtil.saveDataToFile(saveFileForTesting, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

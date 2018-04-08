package seedu.address.testutil;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.Model;
import seedu.address.model.book.Book;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    private static final String SANDBOX_FOLDER = FileUtil.getPath("./build/tmp/sandbox/");
    private static final int RANDOM_BYTE_LENGTH = 9;
    private static final String PREFIX_SEPARATOR = "_";

    /**
     * Appends {@code fileName} to the sandbox folder path and returns the resulting string.
     * Creates the sandbox folder if it doesn't exist.
     */
    public static String getFilePathInSandboxFolder(String fileName) {
        try {
            FileUtil.createDirs(new File(SANDBOX_FOLDER));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String randomizedFileName = generateRandomPrefix() + PREFIX_SEPARATOR + fileName;
        return SANDBOX_FOLDER + randomizedFileName;
    }

    /**
     * Returns the middle index of the book in the {@code model}'s display book list.
     */
    public static Index getMidIndex(Model model) {
        return Index.fromOneBased(model.getDisplayBookList().size() / 2);
    }

    /**
     * Returns the last index of the book in the {@code model}'s display book list.
     */
    public static Index getLastIndex(Model model) {
        return Index.fromOneBased(model.getDisplayBookList().size());
    }

    /**
     * Returns the book in the {@code model}'s display book list at {@code index}.
     */
    public static Book getBook(Model model, Index index) {
        return model.getDisplayBookList().get(index.getZeroBased());
    }

    /**
     * Returns a random 8 character string to be used as a prefix to a filename.
     */
    private static String generateRandomPrefix() {
        byte[] randomBytes = new byte[RANDOM_BYTE_LENGTH];
        new Random().nextBytes(randomBytes);
        byte[] encodedBytes = Base64.getEncoder().encode(randomBytes);
        return new String(encodedBytes).replace("/", "-");
    }

}

package seedu.address.network.library;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.model.book.Book;

//@@author qiu-siqi
/**
 * Provides utilities to manage result from NLB catalogue search.
 */
public class NlbResultHelper {

    protected static final String URL_FULL_DISPLAY = "https://catalogue.nlb.gov.sg/cgi-bin/spydus.exe/"
            + "ENQ/EXPNOS/BIBENQ?ENTRY=%s&ENTRY_NAME=BS&ENTRY_TYPE=K&GQ=%s&SORTS=SQL_REL_TITLE";
    protected static final String NO_RESULTS_FOUND = "No results found.";
    private static final String FULL_DISPLAY_STRING = "<span>Full Display</span>";
    private static final String SEARCH_STRING = "/cgi-bin/spydus.exe/FULL/EXPNOS/BIBENQ/";
    private static final String URL_PREFIX = "https://catalogue.nlb.gov.sg";

    /**
     * Obtains a URL linking to the display of a single book, if search only found that single book.
     * Obtains the URL of the top search result, if {@code result} contains a list of search results.
     * Returns a custom String if the list is empty.
     *
     * @param result result from querying NLB catalogue.
     * @param book book that was queried for.
     * @return String containing the single book page as HTML content.
     */
    protected static String getUrl(String result, Book book) {
        requireAllNonNull(result, book);

        if (!isFullDisplay(result)) {
            return getTopResultUrl(result);
        }
        String searchTerms = book.getTitle().toString() + " " + book.getAuthorsAsString();
        return String.format(URL_FULL_DISPLAY, searchTerms, searchTerms);
    }

    /**
     * Checks whether {@code result} is the full display result page of a single book.
     */
    private static boolean isFullDisplay(String result) {
        return result.contains(FULL_DISPLAY_STRING);
    }

    /**
     * Assumes: {@code result} is not the full display result page.
     * Obtains the URL of the top search result, if any.
     */
    private static String getTopResultUrl(String result) {
        assert !isFullDisplay(result);

        int index = result.indexOf(SEARCH_STRING);
        if (index == -1) {
            return NO_RESULTS_FOUND;
        }
        return getUrlFromIndex(result, index);
    }

    /**
     * Assumes: {@code index} is valid.
     * Obtains the URL which is given starting at {@code index} in {@code result}.
     */
    private static String getUrlFromIndex(String result, int index) {
        int len = result.length();

        assert index >= 0;
        assert index < len;

        StringBuilder builder = new StringBuilder();
        builder.append(URL_PREFIX);

        for (int i = index;; i++) {
            if (i >= len || result.charAt(i) == '\"') {
                break;
            }
            builder.append(result.charAt(i));
        }

        return builder.toString();
    }
}

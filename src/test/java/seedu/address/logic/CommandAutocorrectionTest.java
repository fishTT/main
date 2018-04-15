package seedu.address.logic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.BookShelf;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.alias.Alias;
import seedu.address.network.NetworkManager;

//@@author qiu-siqi
public class CommandAutocorrectionTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Logic logic;
    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(new BookShelf(), new UserPrefs());
        logic = new LogicManager(model, mock(NetworkManager.class));
    }

    @Test
    public void attemptCommandAutoCorrection_add() throws Exception {
        assertEquals("list by/title", test("lst by/title"));
        assertEquals("add 1", test("dd 1"));
        assertEquals("deletealias r", test("deletealia r"));
    }

    @Test
    public void attemptCommandAutoCorrection_remove() throws Exception {
        assertEquals("edit 1 s/r", test("eedit 1 s/r"));
        assertEquals("delete 1", test("deletee 1"));
        assertEquals("aliases", test("alliases"));
    }

    @Test
    public void attemptCommandAutoCorrection_replace() throws Exception {
        assertEquals("edit 1 s/r", test("adit 1 s/r"));
        assertEquals("recent", test("resent"));
        assertEquals("addalias a cmd/add", test("addaliae a cmd/add"));
    }

    @Test
    public void attemptCommandAutoCorrection_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        test("addbook 1");
    }

    @Test
    public void attemptCommandAutoCorrection_tooDifferent_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        test("eidt 1 s/r");
    }

    @Test
    public void attemptCommandAutoCorrection_longText_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        test("thisismetryingtomakeyoursystemcrashbygivingareallylongstring 1");
    }

    @Test
    public void attemptCommandAutoCorrection_alias() throws Exception {
        model.addAlias(new Alias("rm", "delete", ""));
        assertEquals("rm 1", test("r 1"));
    }

    private String test(String commandText) throws ParseException {
        return CommandAutocorrection.attemptCommandAutoCorrection(logic, commandText);
    }
}

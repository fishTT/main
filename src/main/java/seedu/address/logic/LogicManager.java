package seedu.address.logic;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.events.ui.BookListSelectionChangedEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.BookShelfParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ActiveListType;
import seedu.address.model.Model;
import seedu.address.model.alias.Alias;
import seedu.address.model.book.Book;
import seedu.address.network.Network;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Network network;
    private final CommandHistory history;
    private final BookShelfParser bookShelfParser;
    private final UndoStack undoStack;

    private String correctedCommand;

    public LogicManager(Model model, Network network) {
        this.model = model;
        this.network = network;
        history = new CommandHistory();
        bookShelfParser = new BookShelfParser(model.getAliasList());
        undoStack = new UndoStack();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        String processedText = bookShelfParser.applyCommandAlias(commandText);
        logger.info("----------------[USER COMMAND][" + processedText + "]");

        try {
            Command command = getCommand(processedText);
            command.setData(model, network, history, undoStack);
            CommandResult result = command.execute();
            undoStack.push(command);
            return result;
        } catch (ParseException e) {
            return attemptCommandAutoCorrection(processedText, e);
        } finally {
            history.add(commandText);
        }
    }

    /**
     * Attempts command auto-correction if {@code e} is a {@code ParseException} due to the user input
     * being unable to be matched to any valid command word.
     * @param processedText The command as entered by the user, after accounting for aliases.
     * @param e The exception thrown by {@link BookShelfParser}.
     * @return message asking user whether he meant the corrected command.
     * @throws ParseException If auto correction failed to find any closely related command.
     */
    private CommandResult attemptCommandAutoCorrection(String processedText, ParseException e) throws ParseException {
        if (!e.getMessage().equals(Messages.MESSAGE_UNKNOWN_COMMAND)) {
            throw e;
        }
        correctedCommand = bookShelfParser.attemptCommandAutoCorrection(processedText);
        return new CommandResult(String.format(Messages.MESSAGE_CORRECTED_COMMAND, correctedCommand));
    }

    /**
     * Obtains the command represented by {@code processedText}. If user enters "y" following a
     * command correction, that corrected command will be returned.
     * @param processedText The command as entered by the user, after accounting for aliases.
     * @return the command obtained.
     * @throws ParseException If {@code processedText} cannot be parsed.
     */
    private Command getCommand(String processedText) throws ParseException {
        Command command;
        if (correctedCommand != null && processedText.equalsIgnoreCase("y")) {
            command = bookShelfParser.parseCommand(correctedCommand);
        } else {
            command = bookShelfParser.parseCommand(processedText);
        }
        correctedCommand = null;
        return command;
    }

    @Override
    public ObservableList<Book> getActiveList() {
        return model.getActiveList();
    }

    @Override
    public ObservableList<Book> getDisplayBookList() {
        return model.getDisplayBookList();
    }

    @Override
    public ObservableList<Book> getSearchResultsList() {
        return model.getSearchResultsList();
    }

    @Override
    public ObservableList<Book> getRecentBooksList() {
        return model.getRecentBooksList();
    }

    @Override
    public ObservableList<Alias> getDisplayAliasList() {
        return model.getDisplayAliasList();
    }

    @Override
    public ListElementPointer getHistorySnapshot() {
        return new ListElementPointer(history.getHistory());
    }

    @Subscribe
    private void handleBookListSelectionChangedEvent(BookListSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if (model.getActiveListType() != ActiveListType.RECENT_BOOKS) {
            model.addRecentBook(event.getNewSelection());
        }
    }
}

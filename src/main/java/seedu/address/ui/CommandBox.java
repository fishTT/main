package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.DisableCommandBoxRequestEvent;
import seedu.address.commons.events.ui.EnableCommandBoxRequestEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.util.TextUtil;
import seedu.address.logic.ListElementPointer;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String DISABLED_STYLE_CLASS = "command-box-disabled";
    private static final String FXML = "CommandBox.fxml";

    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private final Logic logic;
    private ListElementPointer historySnapshot;

    @FXML
    private TextField commandTextField;
    @FXML
    private StackPane commandBox;
    @FXML
    private HBox commandBoxItems;

    private final CommandBoxHints commandBoxHints;

    public CommandBox(Logic logic) {
        super(FXML);
        this.logic = logic;
        registerAsAnEventHandler(this);

        commandBox.widthProperty().addListener((ob, o, n) -> resizeCommandTextField());
        commandTextField.textProperty().addListener((ob, o, n) -> resizeCommandTextField());

        commandBoxHints = new CommandBoxHints(logic, commandTextField);
        commandBoxItems.getChildren().add(commandBoxHints.getRoot());
        HBox.setHgrow(commandBoxHints.getRoot(), Priority.ALWAYS);

        commandTextField.textProperty().addListener((observable, oldInput, newInput) ->
                setStyleByValidityOfInput(newInput));
        commandTextField.textProperty().addListener((observable, oldInput, newInput) ->
                commandBoxHints.updateHint(newInput));
        historySnapshot = logic.getHistorySnapshot();
    }

    /**
     * Handles the key press event, {@code keyEvent}.
     */
    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        if (!commandTextField.isEditable()) {
            return;
        }
        switch (keyEvent.getCode()) {
        case UP:
            // As up and down buttons will alter the position of the caret,
            // consuming it causes the caret's position to remain unchanged
            keyEvent.consume();

            navigateToPreviousInput();
            break;
        case DOWN:
            keyEvent.consume();
            navigateToNextInput();
            break;
        default:
            // let JavaFx handle the keypress
        }
    }

    /**
     * Updates the text field with the previous input in {@code historySnapshot},
     * if there exists a previous input in {@code historySnapshot}
     */
    private void navigateToPreviousInput() {
        assert historySnapshot != null;
        if (!historySnapshot.hasPrevious()) {
            return;
        }

        replaceText(historySnapshot.previous());
    }

    /**
     * Updates the text field with the next input in {@code historySnapshot},
     * if there exists a next input in {@code historySnapshot}
     */
    private void navigateToNextInput() {
        assert historySnapshot != null;
        if (!historySnapshot.hasNext()) {
            return;
        }

        replaceText(historySnapshot.next());
    }

    /** Resizes the command text field based on the width of its containing text. */
    private void resizeCommandTextField() {
        double width = TextUtil.computeTextWidth(commandTextField,
                commandTextField.getText(), 0.0D) + 3;
        double halfWindowWidth = (getRoot().getParent() == null)
                ? 250 : ((Region) getRoot().getParent()).getWidth() / 2;
        width = Math.max(1, width);
        width = (width > halfWindowWidth) ? halfWindowWidth : width;
        commandTextField.setPrefWidth(width);
        commandTextField.setMinWidth(width);
    }

    /**
     * Sets {@code CommandBox}'s text field with {@code text} and
     * positions the caret to the end of the {@code text}.
     */
    private void replaceText(String text) {
        commandTextField.setText(text);
        commandTextField.positionCaret(commandTextField.getText().length());
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandInputChanged() {
        if (!commandTextField.isEditable()) {
            return;
        }
        try {
            CommandResult commandResult = logic.execute(commandTextField.getText());
            if (CommandResult.isEmptyResult(commandResult)) {
                return;
            }

            initHistory();
            historySnapshot.next();
            // process result of the command
            commandTextField.setText("");
            logger.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser));

        } catch (CommandException | ParseException e) {
            initHistory();
            // handle command failure
            setStyleToIndicateCommandFailure();
            logger.info("Invalid command: " + commandTextField.getText());
            raise(new NewResultAvailableEvent(e.getMessage()));
        }
    }

    /**
     * Initializes the history snapshot.
     */
    private void initHistory() {
        historySnapshot = logic.getHistorySnapshot();
        // add an empty string to represent the most-recent end of historySnapshot, to be shown to
        // the user if she tries to navigate past the most-recent end of the historySnapshot.
        historySnapshot.add("");
    }

    /**
     * Sets the command box style to match validity of the input. (valid -> default, invalid -> failed)
     */
    private void setStyleByValidityOfInput(String input) {
        if (input.equals("")) {
            return;
        }

        if (!logic.isValidCommand(input)) {
            setStyleToIndicateCommandFailure();
            return;
        }

        setStyleToDefault();
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    @Subscribe
    private void handleDisableCommandBoxRequestEvent(DisableCommandBoxRequestEvent event) {
        commandTextField.setEditable(false);
        commandTextField.setFocusTraversable(false);
        commandBoxHints.hide();
        commandBox.getStyleClass().add(DISABLED_STYLE_CLASS);
    }

    @Subscribe
    private void handleEnableCommandBoxRequestEvent(EnableCommandBoxRequestEvent event) {
        commandTextField.setEditable(true);
        commandTextField.setFocusTraversable(true);
        commandBoxHints.show();
        commandBox.getStyleClass().remove(DISABLED_STYLE_CLASS);
    }
}

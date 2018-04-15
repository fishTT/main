package seedu.address.logic;

import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.Optional;

import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author qiu-siqi
/**
 * Class that is responsible for giving command correction suggestion based on user's invalid command.
 */
public class CommandAutocorrection {

    private static final int MAX_COMMAND_WORD_LENGTH = 12;

    /**
     * Assumes: {@code commandText} represents an invalid command.
     * Checks: command word in {@code commandText} is within the length of possible commands.
     * Attempts to find a closely matching command that the user might have meant to type.
     * @param logic for checking validity of command.
     * @param commandText Text as entered by the user.
     * @return String representation of the closely matching command.
     * @throws ParseException If auto correction failed to find any closely matching command.
     */
    public static String attemptCommandAutoCorrection(Logic logic, String commandText) throws ParseException {
        String[] command = logic.parse(commandText);
        String commandWord = command[0];
        String arguments = command[1];

        if (commandWord.length() > MAX_COMMAND_WORD_LENGTH) {
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
        return attemptCommandAutoCorrection(logic, commandWord, arguments);
    }

    /**
     * Attempts to find a closely matching command that the user might have meant to type.
     * @param logic for checking validity of command.
     * @param commandWord command word.
     * @param arguments other parameters for the command.
     * @return String representation of the closely matching command.
     * @throws ParseException If auto correction failed to find any closely matching command.
     */
    private static String attemptCommandAutoCorrection(Logic logic, String commandWord, String arguments)
            throws ParseException {
        Optional<String> result = testAddToFront(logic, commandWord, arguments);
        result = result.isPresent() ? result : testAtAllIndexes(logic, commandWord, arguments);

        if (result.isPresent()) {
            return result.get();
        }
        throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Tests all possible single point mutations to {@code commandWord} and checks whether any of them
     * gives a valid command.
     * @return corrected command, if any.
     */
    private static Optional<String> testAtAllIndexes(Logic logic, String commandWord, String arguments) {
        for (int i = 0; i < commandWord.length(); i++) {
            Optional<String> result = testAtIndex(logic, commandWord, arguments, i);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    /**
     * Tests all possible mutations at {@code index} and checks whether any of them gives a valid command.
     * @return corrected command, if any.
     */
    private static Optional<String> testAtIndex(Logic logic, String commandWord, String arguments, int index) {
        char[] alphabets = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        Optional<String> result;
        for (char character : alphabets) {
            result = testAtIndexWithChar(logic, commandWord, arguments, index, character);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    /**
     * Tests all possibilities given an index {@code index} and {@code character}: removing at index {@code index},
     * replacing with {@code character} at index {@code index}, and adding {@code character} after index {@code index}.
     * Checks whether any of them give a valid command.
     * @return valid command, if any.
     */
    private static Optional<String> testAtIndexWithChar(Logic logic, String commandWord, String arguments,
                                                 int index, char character) {
        Optional<String> result = testCommand(logic, StringUtil.removeAt(commandWord, index), arguments);
        if (result.isPresent()) {
            return result;
        }

        result = testCommand(logic, StringUtil.replace(commandWord, character, index), arguments);
        if (result.isPresent()) {
            return result;
        }

        result = testCommand(logic, StringUtil.addAfter(commandWord, character, index), arguments);
        return result;
    }

    /**
     * Tests whether adding any character at the front of {@code commandWord} gives a valid command.
     * @return valid command, if any.
     */
    private static Optional<String> testAddToFront(Logic logic, String commandWord, String arguments) {
        char[] alphabets = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        for (char character : alphabets) {
            Optional<String> result = testCommand(logic, StringUtil.addAfter(commandWord, character, -1), arguments);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    /**
     * Tests whether {@code commandWord} and {@code arguments} form a valid command.
     * @return the command if it is valid.
     */
    private static Optional<String> testCommand(Logic logic, String commandWord, String arguments) {
        String command = (commandWord.trim() + " " + arguments.trim()).trim();
        return logic.isValidCommand(command) ? Optional.of(command) : Optional.empty();
    }
}

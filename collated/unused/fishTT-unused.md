# fishTT-unused
###### \AliasCommand.java
``` java
/**
 * Creates an alias for other commands.
 */
public class AliasCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "alias";
    public static final String MESSAGE_ADD_SUCCESS = "The alias \"%1$s\" now points to \"%2$s\".";
    public static final String MESSAGE_LIST_SUCCESS = "Aliases:\n%1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Creates an alias for other commands."
            + "Parameters: [ALIAS COMMAND]\n"
            + "Example: " + COMMAND_WORD + " create add\n"
            + "Example: " + COMMAND_WORD + " friends find t/friends\n";

    private final String alias;
    private final String command;

    public AliasCommand() {
        this.alias = null;
        this.command = null;
    }

    public AliasCommand(String alias, String command) {
        this.alias = alias;
        this.command = command;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        Aliases aliases = UserPrefs.getInstance().getAliases();

        if (alias == null && command == null) {
            StringBuilder output = new StringBuilder();

            for (String alias : aliases.getAllAliases()) {
                String command = aliases.getCommand(alias);
                output.append(String.format("%1$s=%2$s\n", alias, command));
            }
            return new CommandResult(String.format(MESSAGE_LIST_SUCCESS, output));
        }

        aliases.addAlias(alias, command);

        return new CommandResult(String.format(MESSAGE_ADD_SUCCESS, alias, command));
    }

    @Override
    protected String undo() {
        return null;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AliasCommand // instanceof handles nulls
                && (this.alias == null || this.alias.equals(((AliasCommand) other).alias)) // state check
                && (this.command == null || this.command.equals(((AliasCommand) other).command))); // state check
    }

}
```
###### \AliasCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AliasCommand object
 */
public class AliasCommandParser implements Parser<AliasCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AliasCommand
     * and returns an AliasCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AliasCommand parse(String arguments) throws ParseException {

        if (arguments.length() == 0) {
            return new AliasCommand();
        }

        int delimiterPosition = arguments.trim().indexOf(' ');

        if (delimiterPosition == -1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AliasCommand.MESSAGE_USAGE));
        }

        final String alias = arguments.trim().substring(0, delimiterPosition).trim();
        final String command = arguments.trim().substring(delimiterPosition + 1).trim();
        return new AliasCommand(alias, command);

    }

}

```
###### \Aliases.java
``` java
/**
 * Contains a mapping of aliases to their commands, which is referred to when parsing commands.
 * When using the Alias and Unalias commands, key-value pairs are added and removed respectively.
 */
public class Aliases {

    private final Map<String, String> map = new HashMap<>();

    /*
     * We initialise the map with aliases for frequently used commands. Users can add other aliases themselves.
     */
    public Aliases() {
        map.put("new", AddCommand.COMMAND_WORD);
        map.put("create", AddCommand.COMMAND_WORD);
        map.put("remove", DeleteCommand.COMMAND_WORD);
        map.put("change", EditCommand.COMMAND_WORD);
        map.put("quit", ExitCommand.COMMAND_WORD);
        map.put("man", HelpCommand.COMMAND_WORD);
        map.put("ls", ListCommand.COMMAND_WORD);
        map.put("show", ListCommand.COMMAND_WORD);
        map.put("choose", SelectCommand.COMMAND_WORD);
        map.put("pick", SelectCommand.COMMAND_WORD);
    }

    /**
     * Returns a set of all aliases
     */
    public Set<String> getAllAliases() {
        return map.keySet();
    }

    /**
     * Returns the command linked to a specific alias, or null otherwise.
     */
    public String getCommand(String alias) {
        return map.get(alias);
    }

    /**
     * Adds or updates an alias to the map.
     */
    public void addAlias(String alias, String command) {
        map.put(alias, command);
    }

    /**
     * Removes an alias from the map.
     *
     * @throws NoSuchElementException if no such alias exists
     */
    public boolean removeAlias(String alias) throws NoSuchElementException {
        if (map.remove(alias) == null) {
            throw new NoSuchElementException();
        }
        return true;
    }

    @Override
    public String toString() {
        return map.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Aliases // instanceof handles nulls
                && this.map.equals(((Aliases) other).map));
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }
}
```

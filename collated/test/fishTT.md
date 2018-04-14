# fishTT
###### \java\seedu\address\logic\parser\BookShelfParserTest.java
``` java
    @Test
    public void parseCommand_mixedCaseCommandWord_success() throws Exception {
        /* Case: first character is uppercase */
        char[] commandWord = ListCommand.COMMAND_WORD.toCharArray();
        commandWord[0] = Character.toUpperCase(commandWord[0]);
        String firstCharUppercaseCommand = String.copyValueOf(commandWord);
        assertTrue(parser.parseCommand(firstCharUppercaseCommand) instanceof ListCommand);

        /* Case: last character is uppercase */
        commandWord[commandWord.length - 1] = Character.toUpperCase(commandWord[commandWord.length - 1]);
        String lastCharUppercaseCommand = String.copyValueOf(commandWord);
        assertTrue(parser.parseCommand(lastCharUppercaseCommand) instanceof ListCommand);

        /* Case: middle character is uppercase */
        commandWord[commandWord.length / 2] = Character.toUpperCase(commandWord[commandWord.length / 2]);
        String middleCharUppercaseCommand = String.copyValueOf(commandWord);
        assertTrue(parser.parseCommand(middleCharUppercaseCommand) instanceof ListCommand);

        /* Case: all character is uppercase */
        String allCharUppercaseCommand = ListCommand.COMMAND_WORD.toUpperCase();
        assertTrue(parser.parseCommand(allCharUppercaseCommand) instanceof ListCommand);
    }
```

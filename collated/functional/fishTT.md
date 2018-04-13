# fishTT
###### \java\seedu\address\ui\WelcomePanel.java
``` java
    private String getRandomQuote() throws IOException {
        List<String> lines = Resources.readLines(QUOTES_FILE, Charsets.UTF_8);

        // choose a random one from the list
        Random r = new Random();
        String randomQuote = lines.get(r.nextInt(lines.size()));
        return "\"" + randomQuote + "\"";
    }

```

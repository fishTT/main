package seedu.address.model.alias;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Comparator;

/**
 * Contains data about a single alias.
 * Guarantees: immutable.
 */
public class Alias {
    /**
     * A {@code Comparator} for aliases that sorts by name, in alphabetical order.
     */
    public static final Comparator<Alias> ALIAS_NAME_COMPARATOR = Comparator.comparing(Alias::getName);

    private final String name;
    private final String prefix;
    private final String namedArgs;

    /**
     * Creates an {@code Alias} with the specified {@code name}, {@code prefix}, and {@code namedArgs}.
     * All fields must be non-null.
     */
    public Alias(String name, String prefix, String namedArgs) {
        requireAllNonNull(name, prefix, namedArgs);
        this.name = name.trim().toLowerCase();
        this.prefix = prefix.trim();
        this.namedArgs = namedArgs.trim();
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getNamedArgs() {
        return namedArgs;
    }

    @Override
    public String toString() {
        return name + " - " + prefix + " " + namedArgs;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Alias // instanceof handles nulls
                && this.name.equals(((Alias) other).name)
                && this.prefix.equals(((Alias) other).prefix)
                && this.namedArgs.equals(((Alias) other).namedArgs)); // state check
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

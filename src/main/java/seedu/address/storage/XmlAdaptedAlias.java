package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.alias.Alias;

/**
 * JAXB-friendly version of the {@code Alias}.
 */
public class XmlAdaptedAlias {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Alias' %s field is missing!";
    protected static final String NAME_FIELD = "name";
    protected static final String PREFIX_FIELD = "prefix";
    protected static final String NAMED_ARGS_FIELD = "namedArgs";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String prefix;
    @XmlElement(required = true)
    private String namedArgs;

    /**
     * Constructs an {@code XmlAdaptedAlias}.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedAlias() {}

    /**
     * Constructs an {@code XmlAdaptedAlias} with the given alias details.
     */
    public XmlAdaptedAlias(String name, String prefix, String namedArgs) {
        this.name = name;
        this.prefix = prefix;
        this.namedArgs = namedArgs;
    }

    /**
     * Converts a given {@code Alias} into an {@code XmlAdaptedAlias} for JAXB use.
     */
    public XmlAdaptedAlias(Alias alias) {
        requireNonNull(alias);
        this.name = alias.getName();
        this.prefix = alias.getPrefix();
        this.namedArgs = alias.getNamedArgs();
    }

    /**
     * Converts this jaxb-friendly adapted alias object into the model's {@code Alias} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted {@code Alias}.
     */
    public Alias toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, NAME_FIELD));
        }
        if (prefix == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, PREFIX_FIELD));
        }
        if (namedArgs == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, NAMED_ARGS_FIELD));
        }

        return new Alias(name, prefix, namedArgs);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedAlias)) {
            return false;
        }

        XmlAdaptedAlias otherAlias = (XmlAdaptedAlias) other;
        return Objects.equals(name, otherAlias.name)
                && Objects.equals(prefix, otherAlias.prefix)
                && Objects.equals(namedArgs, otherAlias.namedArgs);
    }
}

package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.alias.ReadOnlyAliasList;
import seedu.address.model.alias.UniqueAliasList;

/**
 * An immutable {@code UniqueAliasList} that is serializable to XML format
 */
@XmlRootElement(name = "aliaslist")
public class XmlSerializableAliasList {

    @XmlElement
    private List<XmlAdaptedAlias> aliases;

    /**
     * Creates an empty {@code XmlSerializableAliasList}.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableAliasList() {
        aliases = new ArrayList<>();
    }

    /**
     * Converts a given {@code ReadOnlyAliasList} into this class for JAXB use.
     */
    public XmlSerializableAliasList(ReadOnlyAliasList src) {
        this();
        requireNonNull(src);
        aliases.addAll(src.getAliasList().stream().map(XmlAdaptedAlias::new).collect(Collectors.toList()));
    }

    /**
     * Converts this alias list into the model's {@code ReadOnlyAliasList} object.
     *
     * @throws IllegalValueException if there were any data constraints violated
     * in the {@code XmlAdaptedAlias}.
     */
    public ReadOnlyAliasList toModelType() throws IllegalValueException {
        UniqueAliasList uniqueAliasList = new UniqueAliasList();
        for (XmlAdaptedAlias a : aliases) {
            uniqueAliasList.add(a.toModelType());
        }
        return uniqueAliasList;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableAliasList)) {
            return false;
        }

        XmlSerializableAliasList otherAliasList = (XmlSerializableAliasList) other;
        return aliases.equals(otherAliasList.aliases);
    }
}

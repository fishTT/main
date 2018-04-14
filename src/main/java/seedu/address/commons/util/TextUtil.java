package seedu.address.commons.util;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * Helper functions for handling text in JavaFX TextFields.
 */
//@@author fishTT
public class TextUtil {

    private static final Text HELPER;

    /**
     * Return's Text Width based on {@code TextField textField, String text}.
     */
    public static double computeTextWidth(TextField textField, String text, double maxWidth) {
        HELPER.setText(text);
        HELPER.setFont(textField.getFont());
        HELPER.setStyle(textField.getStyle());

        HELPER.setWrappingWidth(0.0D);
        HELPER.setLineSpacing(0.0D);
        double d = Math.min(HELPER.prefWidth(-1.0D), maxWidth);
        HELPER.setWrappingWidth((int) Math.ceil(d));
        return Math.ceil(HELPER.getLayoutBounds().getWidth());
    }

    static {
        HELPER = new Text();
    }
}

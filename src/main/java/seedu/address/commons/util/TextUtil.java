package seedu.address.commons.util;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * Helper functions for handling text in JavaFX TextFields
 */

//@@author fishTT
public class TextUtil {

    private static final Text HELPER;
    private static final double DEFAULT_WRAPPING_WIDTH;
    private static final double DEFAULT_LINE_SPACING;

    /**
     * Return's Text Width based on {@code TextField textField, String text}
     */
    public static double computeTextWidth(TextField textField, String text, double help0) {
        HELPER.setText(text);
        HELPER.setFont(textField.getFont());
        HELPER.setStyle(textField.getStyle());

        HELPER.setWrappingWidth(0.0D);
        HELPER.setLineSpacing(0.0D);
        double d = Math.min(HELPER.prefWidth(-1.0D), help0);
        HELPER.setWrappingWidth((int) Math.ceil(d));
        d = Math.ceil(HELPER.getLayoutBounds().getWidth());

        HELPER.setWrappingWidth(DEFAULT_WRAPPING_WIDTH);
        HELPER.setLineSpacing(DEFAULT_LINE_SPACING);
        return d;
    }

    static {
        HELPER = new Text();
        DEFAULT_WRAPPING_WIDTH = HELPER.getWrappingWidth();
        DEFAULT_LINE_SPACING = HELPER.getLineSpacing();
    }
}


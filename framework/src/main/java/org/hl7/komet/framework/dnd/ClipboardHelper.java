package org.hl7.komet.framework.dnd;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ClipboardHelper {

    public static void copyToClipboard(CharSequence charSequence) {
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(charSequence.toString());
        Clipboard.getSystemClipboard().setContent(clipboardContent);
    }


}

package com.shreyaslad.ed1t.Components;

import javax.swing.*;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class LineNumberingTextArea extends JTextArea {

    private JEditorPane textArea;

    public LineNumberingTextArea(JEditorPane textArea) {
        this.textArea = textArea;
        setBackground(new Color(105, 105, 105));
        setForeground(new Color(239, 239, 239));

        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        setEditable(false);
    }

    public void updateLineNumbers() {
        String lineNumbersText = getLineNumbersText();
        setText(lineNumbersText);
    }

    private String getLineNumbersText() {
        int caretPosition = textArea.getDocument().getLength();
        Element root = textArea.getDocument().getDefaultRootElement();
        StringBuilder lineNumbersTextBuilder = new StringBuilder();
        lineNumbersTextBuilder.append("~\n~\n").append("1").append(System.lineSeparator());

        for (int elementIndex = 2; elementIndex < root.getElementIndex(caretPosition) - 1; elementIndex++) {
            lineNumbersTextBuilder.append(elementIndex).append(System.lineSeparator());
        }

        return lineNumbersTextBuilder.toString();
    }

}

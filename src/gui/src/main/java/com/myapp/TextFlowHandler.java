package com.myapp;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class TextFlowHandler {

    public String textFlowToString(TextFlow tf) {
        StringBuilder result = new StringBuilder();

        for (Node node : tf.getChildren()) {
            if (node instanceof Text) {
                Text textNode = (Text) node;
                String textString = textNode.getText();
                if (textNode.getTranslateY() < 0) {
                    textString = String.join("", "^(", textString, ")");
                } else if (textNode.getTranslateY() > 0) {
                    textString = String.join("", "[", textString, "]");
                }
                result.append(textString);
            }
        }
        return result.toString();
    }
    
}

package org.hl7.komet.search;

import javafx.scene.control.TreeCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.hl7.tinkar.coordinate.stamp.calculator.LatestVersionSearchResult;

import static org.hl7.komet.framework.StyleClasses.SEARCH_MATCH;

public class SearchResultCell extends TreeCell<Object> {

    public SearchResultCell() {
        this.setMinHeight(USE_COMPUTED_SIZE);
        this.setPrefHeight(USE_COMPUTED_SIZE);
        this.setMaxHeight(USE_COMPUTED_SIZE);
    }

    @Override
    public void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        setText("");
        setGraphic(null);
        if (item != null) {
            setGraphic(null);
            if (item instanceof LatestVersionSearchResult latestVersionSearchResult) {
                TextFlow textFlow = new TextFlow();
                String matchedText = latestVersionSearchResult.highlightedString();
                String startTokenToMatch = "<B>";
                String endTokenToMatch = "</B>";

                int startMatchIndex = matchedText.indexOf(startTokenToMatch);
                while (startMatchIndex != -1) {
                    if (startMatchIndex != 0) {
                        String noHighlightText = matchedText.substring(0, startMatchIndex);
                        Text t = new Text(noHighlightText);
                        textFlow.getChildren().add(t);
                    }
                    int endMatchIndex = matchedText.indexOf(endTokenToMatch);
                    if (endMatchIndex == -1) {
                        // Malformed. Highlight to the end...
                        String highlightText = matchedText.substring(startMatchIndex + startTokenToMatch.length());
                        Text t = new Text(highlightText);
                        t.getStyleClass().add(SEARCH_MATCH.toString());
                        textFlow.getChildren().add(t);
                        matchedText = "";
                        startMatchIndex = -1;
                    } else {
                        String highlightText = matchedText.substring(startMatchIndex + startTokenToMatch.length(), endMatchIndex);
                        Text t = new Text(highlightText);
                        t.getStyleClass().add(SEARCH_MATCH.toString());
                        textFlow.getChildren().add(t);
                        matchedText = matchedText.substring(endMatchIndex + endTokenToMatch.length());
                        startMatchIndex = matchedText.indexOf(startTokenToMatch);
                    }
                }
                if (!matchedText.isBlank()) {
                    Text t = new Text(matchedText);
                    textFlow.getChildren().add(t);
                }
                textFlow.setMinHeight(USE_COMPUTED_SIZE);
                textFlow.setPrefHeight(USE_COMPUTED_SIZE);
                textFlow.setMaxHeight(USE_COMPUTED_SIZE);

                textFlow.setMaxWidth(250 - 10);
                HBox hBox = new HBox(textFlow);
                setGraphic(hBox);
            } else if (item instanceof String itemString) {
                setText(itemString);
            } else if (item instanceof SearchPanelController.NidTextRecord nidTextRecord) {
                setText(nidTextRecord.text());
            } else {
                setText(item.toString());
            }
        }
    }
}

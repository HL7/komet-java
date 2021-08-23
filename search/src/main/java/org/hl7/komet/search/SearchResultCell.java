package org.hl7.komet.search;

import javafx.scene.control.ListCell;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.hl7.tinkar.coordinate.stamp.calculator.LatestVersionSearchResult;

public class SearchResultCell extends ListCell<LatestVersionSearchResult> {
    @Override
    public void updateItem(LatestVersionSearchResult item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null) {
            setText("");
            setGraphic(null);
        } else {
            TextFlow textFlow = new TextFlow();
            item.latestVersion().ifPresent(semanticEntityVersion -> {
                for (Object fieldValue : semanticEntityVersion.fields()) {
                    if (fieldValue instanceof String text) {
                        Text t = new Text(text);
                        //t.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
                        // TODO: add highlight of search terms... Split text into before and after search term...
                        textFlow.getChildren().addAll(t);
                    }
                }
            });

            setGraphic(textFlow);
        }
    }
}

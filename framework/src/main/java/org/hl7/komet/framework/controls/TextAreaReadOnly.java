package org.hl7.komet.framework.controls;

import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author kec
 */
public class TextAreaReadOnly extends TextArea {

    TextAreaSkinNoScroller textAreaSkinNoScroller;

    public TextAreaReadOnly() {
        setSkin(new TextAreaSkinNoScroller(this));
        this.textAreaSkinNoScroller = (TextAreaSkinNoScroller) this.getSkin();
        this.textAreaSkinNoScroller.getContentView().heightProperty().addListener((observable, oldValue, newValue) -> {
            setTheHeight(newValue.doubleValue());
        });
        setEditable(false);
        setWrapText(true);
        setPrefRowCount(4);

        this.textProperty().addListener((observable, oldValue, newValue) -> {
            double height = textAreaSkinNoScroller.computePrefHeight(getWidth());
            setTheHeight(height);
        });

        this.widthProperty().addListener((observable, oldValue, newValue) -> {
            double height = textAreaSkinNoScroller.computePrefHeight(newValue.doubleValue());
            setTheHeight(height);
        });

        focusedProperty().addListener((observable, oldValue, newValue) -> {
            selectRange(0,0);
        });

        addEventFilter(
                MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() > 2)
                            System.out.println("ClickCount: " + event.getClickCount());
                    }
                }
        );
    }

    private void setTheHeight(Double height) {
        setPrefHeight(height);
        setMaxHeight(height);
        setHeight(height);
    }


}

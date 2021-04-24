package org.hl7.komet.framework;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;

public class ScreenInfo {
    private static Robot robot;
    private static void validateState() {
        if (Platform.isFxApplicationThread()) {
            if (robot == null) {
                robot = new Robot();
            }
        } else {
            throw new IllegalStateException("Trying to access mouse info without using the applicaton thread");
        }
    }

    public static double getMouseX() {
        validateState();
        return robot.getMouseX();
    }

    public static double getMouseY() {
        validateState();
        return robot.getMouseY();
    }

    public static Point2D getMousePosition() {
        validateState();
        return robot.getMousePosition();
    }

    public static Color getPixelColor(double x, double y) {
        validateState();
        return robot.getPixelColor(x, y);
    }

    public static Color getPixelColor(Point2D location) {
        validateState();
        return robot.getPixelColor(location);
    }

    public static WritableImage getScreenCapture(WritableImage image, double x, double y, double width, double height, boolean scaleToFit) {
        validateState();
        return robot.getScreenCapture(image, x, y, width, height, scaleToFit);
    }

    public static WritableImage getScreenCapture(WritableImage image, double x, double y, double width, double height) {
        validateState();
        return robot.getScreenCapture(image, x, y, width, height);
    }

    public static WritableImage getScreenCapture(WritableImage image, Rectangle2D region) {
        validateState();
        return robot.getScreenCapture(image, region);
    }

    public static WritableImage getScreenCapture(WritableImage image, Rectangle2D region, boolean scaleToFit) {
        validateState();
        return robot.getScreenCapture(image, region, scaleToFit);
    }
}

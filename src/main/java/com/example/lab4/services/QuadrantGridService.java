package com.example.lab4.services;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This service provides functionality for drawing a Cartesian coordinate system,
 * determining the quadrant of a point, scaling the grid based on point coordinates,
 * and rendering the grid lines and numbers. It works with a JavaFX `Canvas`.
 */
public class QuadrantGridService {

    private final Canvas canvas;

    private final double GRID_OFFSET_X;
    private final double GRID_OFFSET_Y;
    private final double CANVAS_X_SIZE;
    private final double CANVAS_Y_SIZE;

    /**
     * Constructor initializes the service with the given canvas and calculates offsets and canvas size.
     *
     * @param canvas The JavaFX Canvas where the coordinate system and point will be drawn.
     */
    public QuadrantGridService(Canvas canvas) {
        this.canvas = canvas;
        GRID_OFFSET_X = canvas.getWidth() / 2;
        GRID_OFFSET_Y = canvas.getHeight() / 2;
        CANVAS_X_SIZE = canvas.getWidth();
        CANVAS_Y_SIZE = canvas.getHeight();
    }

    /**
     * Determines which quadrant a given point (x, y) is in the Cartesian coordinate system.
     *
     * @param x The X-coordinate of the point.
     * @param y The Y-coordinate of the point.
     * @return A string describing the quadrant or axis the point lies on.
     */
    public static String determineQuadrant(double x, double y) {
        if (x > 0 && y > 0) {
            return "the first quadrant";
        } else if (x < 0 && y > 0) {
            return "the second quadrant";
        } else if (x < 0 && y < 0) {
            return "the third quadrant";
        } else if (x > 0 && y < 0) {
            return "the fourth quadrant";
        } else if (x == 0 && y != 0) {
            return "on the Y-axis";
        } else if (y == 0 && x != 0) {
            return "on the X-axis";
        } else {
            return "at the origin";
        }
    }

    /**
     * Returns the maximum absolute value of the given coordinates, used to scale the grid accordingly.
     *
     * @param x The X-coordinate of the point.
     * @param y The Y-coordinate of the point.
     * @return The maximum of the absolute values of x and y.
     */
    public static double getMaxAbsValue(double x, double y) {
        return Math.max(Math.abs(x), Math.abs(y));
    }

    /**
     * Determines the scaling factor based on the coordinates to ensure the point fits within the grid.
     * If the coordinates exceed the initial grid size, the grid is scaled by a factor of 2.
     *
     * @param x The X-coordinate of the point.
     * @param y The Y-coordinate of the point.
     * @return The scale factor for the grid.
     */
    public static double getScaleFactor(double x, double y) {
        double maxCord = getMaxAbsValue(x, y);
        double initialGridSize = 10;

        while(initialGridSize < maxCord) {
            initialGridSize *= 2;
        }

        return initialGridSize;
    }

    /**
     * Draws the Cartesian coordinate system (X and Y axes) on the canvas with grid lines.
     */
    public void drawCoordSystem() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setStroke(Color.BLACK);
        gc.strokeLine(0, GRID_OFFSET_Y, CANVAS_X_SIZE, GRID_OFFSET_Y);
        gc.strokeLine(GRID_OFFSET_X, 0, GRID_OFFSET_X, CANVAS_Y_SIZE);
        gc.save();

        gc.setFill(Color.GRAY);
        gc.setStroke(Color.LIGHTGRAY);

        int numberOfDivisions = 10;
        double step = GRID_OFFSET_X / numberOfDivisions;

        for (int i = 1; i <= numberOfDivisions; i++) {
            gc.strokeLine(GRID_OFFSET_X + i * step, 0, GRID_OFFSET_X + i * step, CANVAS_Y_SIZE);
            gc.strokeLine(GRID_OFFSET_X - i * step, 0, GRID_OFFSET_X - i * step, CANVAS_Y_SIZE);

            gc.save();

            gc.strokeLine(0, GRID_OFFSET_Y + i * step, CANVAS_X_SIZE, GRID_OFFSET_Y + i * step);
            gc.strokeLine(0, GRID_OFFSET_Y - i * step, CANVAS_X_SIZE, GRID_OFFSET_Y - i * step);
        }
    }

    /**
     * Draws a point on the coordinate system at the given coordinates (x, y).
     *
     * @param x The X-coordinate of the point.
     * @param y The Y-coordinate of the point.
     */
    public void drawPoint(double x, double y) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0, CANVAS_X_SIZE, CANVAS_Y_SIZE);
        drawCoordSystem();
        drawGridNumbers(x, y);

        double scaleFactorX = GRID_OFFSET_X / getScaleFactor(x, y);
        double scaleFactorY = GRID_OFFSET_Y / getScaleFactor(x, y);

        gc.setFill(Color.RED);
        double dotX = GRID_OFFSET_X + x * scaleFactorX;
        double dotY = GRID_OFFSET_Y - y * scaleFactorY;
        gc.fillOval(dotX - 5, dotY - 5, 10, 10);
    }

    /**
     * Draws the grid numbers at regular intervals along the X and Y axes.
     *
     * @param x The X-coordinate of the point (used to adjust the scale).
     * @param y The Y-coordinate of the point (used to adjust the scale).
     */
    public void drawGridNumbers(double x, double y) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.GRAY);
        gc.setStroke(Color.LIGHTGRAY);

        int numberOfDivisions = 10;
        double step = getScaleFactor(x,y) / numberOfDivisions;

        for (int i = 1; i <= numberOfDivisions; i++) {
            double positiveOffset = i * GRID_OFFSET_X / numberOfDivisions;

            gc.save();

            gc.translate(GRID_OFFSET_X + positiveOffset - 10, GRID_OFFSET_X + 15);

            gc.rotate(45);

            gc.fillText(String.format("%.1f", i * step), 0, 0);

            gc.restore();

            gc.save();
            gc.translate(GRID_OFFSET_X - positiveOffset - 10, GRID_OFFSET_X + 15);
            gc.rotate(45);
            gc.fillText(String.format("%.1f", -i * step), 0, 0);
            gc.restore();

            gc.fillText(String.format("%.1f", -i * step), GRID_OFFSET_X + 5, GRID_OFFSET_X + positiveOffset);
            gc.fillText(String.format("%.1f", i * step), GRID_OFFSET_X + 5, GRID_OFFSET_X - positiveOffset);
        }
    }
}

package com.example.lab4;

import com.example.lab4.services.QuadrantGridService;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {

    private QuadrantGridService quadrantGridService;
    private Canvas canvas;
    private final int CANVAS_SIZE = 400;
    private final int AXIS_OFFSET = CANVAS_SIZE / 2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Lab-4 made by Vitaliy Verzun, coordinate graph");

        Label xLabel = new Label("X:");
        TextField xField = new TextField();
        Label yLabel = new Label("Y:");
        TextField yField = new TextField();
        Button calculateButton = new Button("Find Quadrant");

        Label resultLabel = new Label();
        canvas = new Canvas(CANVAS_SIZE, CANVAS_SIZE);
        quadrantGridService = new QuadrantGridService(canvas);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(xLabel, 0, 0);
        gridPane.add(xField, 1, 0);
        gridPane.add(yLabel, 0, 1);
        gridPane.add(yField, 1, 1);
        gridPane.add(calculateButton, 0, 2, 2, 1);
        gridPane.add(resultLabel, 0, 3, 2, 1);
        gridPane.add(canvas, 0, 4, 2, 1);

        calculateButton.setOnAction(e -> {
            try {
                double x = Double.parseDouble(xField.getText());
                double y = Double.parseDouble(yField.getText());
                String quadrant = quadrantGridService.determineQuadrant(x, y);
                resultLabel.setText("The point is in " + quadrant);
                quadrantGridService.drawPoint(x, y);
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "The input should be numeric !").showAndWait();
            }
        });

        quadrantGridService.drawCoordSystem();
        Scene scene = new Scene(gridPane, 450, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.*;

/**
 * Componente de visualização que desenha o estado atual do World
 * em um Canvas JavaFX.
 */
public class CanvasView extends Canvas {
    private final World world;
    private final double roadLength;
    private final double laneWidth = 20.0; 
    private final double totalRoadWidth = 2 * laneWidth; 
    private final double vehicleSize = 10.0;
    private final double trafficLightRadius = 8.0;

    private final double canvasWidth = 800;
    private final double canvasHeight = 700;
    private final double intersectionCenterY = canvasHeight / 2;
    private final double intersectionCenterX = canvasWidth / 2;

    private final double roadY_R1 = intersectionCenterY - laneWidth / 2;
    private final double roadY_R2 = intersectionCenterY + laneWidth / 2;
    private final double roadX_R3 = intersectionCenterX - laneWidth / 2;
    private final double roadX_R4 = intersectionCenterX + laneWidth / 2;

    public CanvasView(World world, double roadLength) {
        super(800, 700);
        this.world = world;
        this.roadLength = roadLength;
    }

    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.web("#F0F0F0"));
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        drawRoads(gc);
        drawIntersections(gc);
        drawVehicles(gc);
    }

    private void drawRoads(GraphicsContext gc) {
        gc.setFill(Color.web("#404040"));
        gc.fillRect(0, intersectionCenterY - totalRoadWidth / 2, canvasWidth, totalRoadWidth);
        gc.fillRect(intersectionCenterX - totalRoadWidth / 2, 0, totalRoadWidth, canvasHeight);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2.0);
        gc.setLineDashes(10, 10);
        gc.strokeLine(0, intersectionCenterY, canvasWidth, intersectionCenterY);
        gc.strokeLine(intersectionCenterX, 0, intersectionCenterX, canvasHeight);

        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(4.0);
        gc.setLineDashes(0);

        double stopLineOffset = totalRoadWidth / 2 + 5;
        gc.strokeLine(intersectionCenterX - stopLineOffset, roadY_R1 - laneWidth / 2, intersectionCenterX - stopLineOffset, roadY_R1 + laneWidth / 2);
        gc.strokeLine(intersectionCenterX + stopLineOffset, roadY_R2 - laneWidth / 2, intersectionCenterX + stopLineOffset, roadY_R2 + laneWidth / 2);
        gc.strokeLine(roadX_R3 - laneWidth / 2, intersectionCenterY - stopLineOffset, roadX_R3 + laneWidth / 2, intersectionCenterY - stopLineOffset);
        gc.strokeLine(roadX_R4 - laneWidth / 2, intersectionCenterY + stopLineOffset, roadX_R4 + laneWidth / 2, intersectionCenterY + stopLineOffset);
    }

    private void drawIntersections(GraphicsContext gc) {
        if (world.getIntersections().isEmpty()) return;
        Intersection intersection = world.getIntersections().get(0);
        
        Road road1 = world.getRoads().get(0);
        Road road2 = world.getRoads().get(1);
        Road road3 = world.getRoads().get(2);
        Road road4 = world.getRoads().get(3);

        double stopLineX = intersectionCenterX - totalRoadWidth / 2 - 5;
        double stopLineY = intersectionCenterY - totalRoadWidth / 2 - 5;

        drawLight(gc, intersection.getTrafficLight(road1), stopLineX - trafficLightRadius - 5, roadY_R1 - laneWidth / 2, -20, 5);
        drawLight(gc, intersection.getTrafficLight(road2), intersectionCenterX + totalRoadWidth / 2 + 5 + trafficLightRadius + 5, roadY_R2 + laneWidth / 2, 20, 5);
        drawLight(gc, intersection.getTrafficLight(road3), roadX_R3 - laneWidth / 2, stopLineY - trafficLightRadius - 5, -20, -10);
        drawLight(gc, intersection.getTrafficLight(road4), roadX_R4 + laneWidth / 2, intersectionCenterY + totalRoadWidth / 2 + 5 + trafficLightRadius + 5, 20, 10);
    }

    private void drawLight(GraphicsContext gc, TrafficLight light, double x, double y, double tx, double ty) {
        if (light == null) return;
        gc.setFill(Color.BLACK);
        gc.fillOval(x - trafficLightRadius - 1, y - trafficLightRadius - 1, 2 * trafficLightRadius + 2, 2 * trafficLightRadius + 2);

        switch (light.getState()) {
            case RED -> gc.setFill(Color.RED);
            case YELLOW -> gc.setFill(Color.YELLOW);
            case GREEN -> gc.setFill(Color.GREEN);
        }
        gc.fillOval(x - trafficLightRadius, y - trafficLightRadius, 2 * trafficLightRadius, 2 * trafficLightRadius);

        int timeRemaining = light.getCurrentState().getDuration() - light.getTimeInState();
        gc.setFill(Color.BLACK);
        gc.fillText(String.valueOf(Math.max(0, timeRemaining)), x + tx, y + ty);
    }

    private void drawVehicles(GraphicsContext gc) {
        for (Vehicle vehicle : world.getVehicles()) {
            Road currentRoad = vehicle.getCurrentRoad();
            double normalizedPosition = vehicle.getPosition() / roadLength;
            double x = 0, y = 0;

            if (vehicle instanceof EmergencyVehicle) {
                gc.setFill(Color.BLUE); // Veículos de emergência são azuis
            } else {
                gc.setFill(Color.hsb((vehicle.getId() * 50) % 360, 0.8, 0.9));
            }

            if (currentRoad.getId() == 1) {
                x = normalizedPosition * intersectionCenterX;
                y = roadY_R1 - vehicleSize / 2;
            } else if (currentRoad.getId() == 2) {
                x = canvasWidth - (normalizedPosition * intersectionCenterX);
                y = roadY_R2 - vehicleSize / 2;
            } else if (currentRoad.getId() == 3) {
                x = roadX_R3 - vehicleSize / 2;
                y = normalizedPosition * intersectionCenterY;
            } else if (currentRoad.getId() == 4) {
                x = roadX_R4 - vehicleSize / 2;
                y = canvasHeight - (normalizedPosition * intersectionCenterY);
            }

            gc.fillRect(x, y, vehicleSize, vehicleSize);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x, y, vehicleSize, vehicleSize);
        }
    }
}

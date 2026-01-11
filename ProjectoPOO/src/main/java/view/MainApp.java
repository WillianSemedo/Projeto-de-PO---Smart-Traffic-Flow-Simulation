package view;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.World;
import controller.*;
import util.CsvExporter;

public class MainApp extends Application {
    private World world;
    private CanvasView canvasView;
    private ControlPanel controlPanel;
    private SimulationTimer timer;

    private long lastUpdate = 0;
    private double timeScale = 1.0;

    private class SimulationTimer extends AnimationTimer {
        @Override
        public void handle(long now) {
            if (lastUpdate == 0) {
                lastUpdate = now;
                return;
            }
            long elapsedNanos = now - lastUpdate;
            lastUpdate = now;
            
            double elapsedSeconds = elapsedNanos / 1_000_000_000.0;
            double simulationTime = elapsedSeconds * timeScale;
            
            // Processar ticks (1 tick por segundo base)
            int ticksToProcess = (int) (simulationTime / 1.0);
            if (ticksToProcess > 0) {
                for (int i = 0; i < ticksToProcess; i++) world.tick();
            } else if (simulationTime > 0 && Math.random() < simulationTime) {
                // Para escalas menores que 1.0, processar probabilisticamente ou acumular
                world.tick();
            }

            canvasView.draw();
            controlPanel.updateStats(world.getMetrics().getAverageWaitingTime(), world.getMetrics().getTotalVehiclesServed());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        world = new World();
        world.setupSimpleScenario();

        controlPanel = new ControlPanel();
        canvasView = new CanvasView(world, world.getRoads().get(0).getLength());
        timer = new SimulationTimer();

        setupControls();

        BorderPane root = new BorderPane();
        root.setCenter(canvasView);
        root.setRight(controlPanel);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Simulação de Tráfego Inteligente");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupControls() {
        controlPanel.getStartButton().setOnAction(e -> {
            world.start();
            timer.start();
            lastUpdate = 0;
            controlPanel.getStartButton().setDisable(true);
            controlPanel.getStopButton().setDisable(false);
        });

        controlPanel.getStopButton().setOnAction(e -> {
            world.stop();
            timer.stop();
            controlPanel.getStartButton().setDisable(false);
            controlPanel.getStopButton().setDisable(true);
        });

        controlPanel.getRestartButton().setOnAction(e -> {
            timer.stop();
            world.reset();
            canvasView.draw();
            controlPanel.getStartButton().setDisable(false);
            controlPanel.getStopButton().setDisable(true);
        });

        controlPanel.getSpeedSlider().valueProperty().addListener((obs, old, val) -> timeScale = val.doubleValue());
        controlPanel.getGenerationSlider().valueProperty().addListener((obs, old, val) -> world.setVehicleGenerationInterval(val.intValue()));
        controlPanel.getStrategyComboBox().valueProperty().addListener((obs, old, val) -> {
            if ("AdaptiveCycle".equals(val)) world.setTrafficStrategy(new AdaptiveCycle());
            else world.setTrafficStrategy(new FixedCycle());
        });

        controlPanel.getExportButton().setOnAction(e -> {
            CsvExporter.exportMetrics(world.getMetrics(), "simulation_metrics.csv");
        });
    }

    public static void main(String[] args) { launch(args); }
}

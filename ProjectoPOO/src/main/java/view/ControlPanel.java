package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.Separator;

public class ControlPanel extends VBox {
    private final Button startButton = new Button("Iniciar");
    private final Button stopButton = new Button("Parar");
    private final Button restartButton = new Button("Reiniciar");
    private final Slider speedSlider = new Slider(0.1, 5.0, 1.0);
    private final Label speedLabel = new Label("Velocidade: 1.0x");
    private final ComboBox<String> strategyComboBox = new ComboBox<>();
    private final Slider generationSlider = new Slider(1, 20, 5);
    private final Label generationLabel = new Label("Intervalo de Geração: 5 ticks");
    private final Label statsLabel = new Label("Estatísticas:\n- Espera Média: 0.0\n- Veículos: 0");
    private final Button exportButton = new Button("Exportar CSV");

    public ControlPanel() {
        setPadding(new Insets(10));
        setSpacing(10);
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: #f4f4f4;");

        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> 
            speedLabel.setText(String.format("Velocidade: %.1fx", newVal.doubleValue())));

        generationSlider.setShowTickLabels(true);
        generationSlider.setShowTickMarks(true);
        generationSlider.setSnapToTicks(true);
        generationSlider.valueProperty().addListener((obs, oldVal, newVal) -> 
            generationLabel.setText(String.format("Intervalo de Geração: %d ticks", newVal.intValue())));

        startButton.setMaxWidth(Double.MAX_VALUE);
        stopButton.setMaxWidth(Double.MAX_VALUE);
        stopButton.setDisable(true);
        restartButton.setMaxWidth(Double.MAX_VALUE);

        strategyComboBox.getItems().addAll("FixedCycle", "AdaptiveCycle");
        strategyComboBox.setValue("FixedCycle");

        getChildren().addAll(
            startButton, stopButton, restartButton,
            new Label("Estratégia:"), strategyComboBox,
            new Label("Velocidade:"), speedSlider, speedLabel,
            new Label("Geração (ticks):"), generationSlider, generationLabel,
            new Separator(),
            statsLabel,
            exportButton
        );
    }

    public Button getStartButton() { return startButton; }
    public Button getStopButton() { return stopButton; }
    public Button getRestartButton() { return restartButton; }
    public Slider getSpeedSlider() { return speedSlider; }
    public ComboBox<String> getStrategyComboBox() { return strategyComboBox; }
    public Slider getGenerationSlider() { return generationSlider; }

    public void updateStats(double avgWait, int totalServed) {
        statsLabel.setText(String.format("Estatísticas:\n- Espera Média: %.2f\n- Veículos: %d", avgWait, totalServed));
    }

    public Button getExportButton() { return exportButton; }
}

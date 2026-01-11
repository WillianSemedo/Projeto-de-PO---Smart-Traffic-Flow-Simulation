package model;

import model.state.GreenState;
import model.state.LightState;
import model.state.RedState;

/**
 * Representa um semáforo com estados e temporização, usando o padrão State.
 */
public class TrafficLight {
    private LightState currentState;
    private int timeInState;
    private final int baseGreenDuration; // Duração base do verde (para estratégia adaptativa)
    private final int yellowDuration;
    private final int redDuration;

    /**
     * Construtor para TrafficLight.
     * @param initialState O estado inicial do semáforo.
     * @param baseGreenDuration Duração base do estado verde em ticks.
     * @param yellowDuration Duração do estado amarelo em ticks.
     * @param redDuration Duração do estado vermelho em ticks.
     */
    public TrafficLight(TrafficLightState initialState, int baseGreenDuration, int yellowDuration, int redDuration) {
        this.baseGreenDuration = baseGreenDuration;
        this.yellowDuration = yellowDuration;
        this.redDuration = redDuration;
        this.timeInState = 0;

        // Inicializa o estado usando o padrão State
        if (initialState == TrafficLightState.GREEN) {
            this.currentState = new GreenState(baseGreenDuration);
        } else {
            this.currentState = new RedState(redDuration);
        }
    }

    /**
     * Construtor com valores padrão sugeridos (Green: 10, Yellow: 2, Red: 5).
     */
    public TrafficLight() {
        this(TrafficLightState.RED, 10, 2, 5);
    }

    /**
     * Atualiza o estado do semáforo com base no tempo decorrido, delegando ao estado atual.
     */
    public void tick() {
        currentState.handleTick(this);
    }

    /**
     * Altera o estado atual do semáforo.
     * @param newState O novo estado.
     */
    public void changeState(LightState newState) {
        this.currentState = newState;
        this.timeInState = 0;
    }

    /**
     * Incrementa o tempo no estado atual.
     */
    public void incrementTimeInState() {
        this.timeInState++;
    }

    // Getters
    public TrafficLightState getState() {
        return currentState.getColor();
    }

    public LightState getCurrentState() {
        return currentState;
    }

    public int getTimeInState() {
        return timeInState;
    }

    public int getGreenDuration() {
        return baseGreenDuration;
    }

    public int getYellowDuration() {
        return yellowDuration;
    }

    public int getRedDuration() {
        return redDuration;
    }

    // Método para testes
    public void resetTimeInState() {
        this.timeInState = 0;
    }
}

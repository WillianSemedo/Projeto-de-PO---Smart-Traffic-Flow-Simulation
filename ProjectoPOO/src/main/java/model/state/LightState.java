package model.state;

import model.TrafficLight;
import model.TrafficLightState;

/**
 * Interface State para o padrão State. Define o comportamento de um estado do semáforo.
 */
public interface LightState {

    /**
     * Obtém o estado de cor associado a este estado.
     * @return O TrafficLightState (RED, GREEN, YELLOW).
     */
    TrafficLightState getColor();

    /**
     * Executa a lógica de um tick para este estado.
     * @param light O semáforo (contexto) a ser atualizado.
     */
    void handleTick(TrafficLight light);

    /**
     * Obtém a duração padrão deste estado em ticks.
     * @return A duração em ticks.
     */
    int getDuration();
}

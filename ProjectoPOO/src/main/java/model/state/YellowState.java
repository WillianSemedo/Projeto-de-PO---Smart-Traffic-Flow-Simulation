package model.state;

import model.TrafficLight;
import model.TrafficLightState;

/**
 * Implementação do estado Amarelo (Yellow) do semáforo.
 */
public class YellowState implements LightState {
    private final int duration;

    public YellowState(int duration) {
        this.duration = duration;
    }

    @Override
    public TrafficLightState getColor() {
        return TrafficLightState.YELLOW;
    }

    @Override
    public void handleTick(TrafficLight light) {
        light.incrementTimeInState();
        if (light.getTimeInState() >= duration) {
            // Transição para o próximo estado (Red)
            // A duração do Red é fixa e passada para o construtor do RedState
            light.changeState(new RedState(light.getRedDuration()));
        }
    }

    @Override
    public int getDuration() {
        return duration;
    }
}

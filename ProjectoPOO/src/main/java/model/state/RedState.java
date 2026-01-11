package model.state;

import model.TrafficLight;
import model.TrafficLightState;

/**
 * Implementação do estado Vermelho (Red) do semáforo.
 */
public class RedState implements LightState {
    private final int duration;

    public RedState(int duration) {
        this.duration = duration;
    }

    @Override
    public TrafficLightState getColor() {
        return TrafficLightState.RED;
    }

    @Override
    public void handleTick(TrafficLight light) {
        light.incrementTimeInState();
        if (light.getTimeInState() >= duration) {
            // Transição para o próximo estado (Green)
            light.changeState(new GreenState(light.getGreenDuration()));
        }
    }

    @Override
    public int getDuration() {
        return duration;
    }
}

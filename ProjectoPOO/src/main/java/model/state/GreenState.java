package model.state;

import model.TrafficLight;
import model.TrafficLightState;

/**
 * Implementação do estado Verde (Green) do semáforo.
 */
public class GreenState implements LightState {
    private final int duration;

    public GreenState(int duration) {
        this.duration = duration;
    }

    @Override
    public TrafficLightState getColor() {
        return TrafficLightState.GREEN;
    }

    @Override
    public void handleTick(TrafficLight light) {
        light.incrementTimeInState();
        if (light.getTimeInState() >= duration) {
            // Transição para o próximo estado (Yellow)
            light.changeState(new YellowState(light.getYellowDuration()));
        }
    }

    @Override
    public int getDuration() {
        return duration;
    }
}

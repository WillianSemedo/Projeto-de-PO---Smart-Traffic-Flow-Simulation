package controller;

import model.Intersection;
import model.Road;
import model.TrafficLight;
import model.TrafficLightState;
import model.state.GreenState;
import model.state.RedState;

import java.util.Map;

/**
 * Implementação da estratégia de ciclo adaptativo (Adaptive Cycle).
 * Ajusta a duração do estado GREEN com base no número de veículos parados.
 */
public class AdaptiveCycle implements Strategy {
    private static final int MIN_GREEN = 6;
    private static final int MAX_GREEN = 20;
    private static final int INCREMENT_PER_VEHICLE = 1;

    @Override
    public void applyStrategy(Intersection intersection) {
        // A intersecção tem 4 estradas: R1(W->E), R2(E->W), R3(N->S), R4(S->N).
        // R1 e R3 são os semáforos principais que controlam a fase.
        
        TrafficLight light1 = intersection.getTrafficLight(intersection.getRoads().get(0)); // R1
        TrafficLight light3 = intersection.getTrafficLight(intersection.getRoads().get(2)); // R3
        
        // 1. Lógica de Ajuste para R1 (Horizontal)
        adjustGreenDuration(intersection.getRoads().get(0), light1);
        
        // 2. Lógica de Ajuste para R3 (Vertical)
        adjustGreenDuration(intersection.getRoads().get(2), light3);

        // 3. Executa o tick normal nos semáforos principais
        light1.tick();
        light3.tick();

        // 4. Sincroniza os semáforos opostos (R2 com R1, R4 com R3)
        TrafficLight light2 = intersection.getTrafficLight(intersection.getRoads().get(1)); // R2
        TrafficLight light4 = intersection.getTrafficLight(intersection.getRoads().get(3)); // R4

        // Sincroniza R2 com R1
        if (light2.getState() != light1.getState()) {
            light2.changeState(light1.getCurrentState());
        }
        // Sincroniza R4 com R3
        if (light4.getState() != light3.getState()) {
            light4.changeState(light3.getCurrentState());
        }
        
        // Garante que o tempo no estado é sincronizado
        light2.resetTimeInState();
        light4.resetTimeInState();
    }

    /**
     * Ajusta a duração do estado GREEN com base na fila de veículos.
     */
    private void adjustGreenDuration(Road road, TrafficLight light) {
        // Lógica de Ajuste (apenas no início do ciclo GREEN)
        if (light.getCurrentState() instanceof GreenState && light.getTimeInState() == 0) {
            // Conta o número de veículos parados (ou na fila)
            long vehiclesInQueue = road.getVehiclesInQueue();

            // Calcula a nova duração do GREEN
            int baseGreen = light.getGreenDuration(); // Duração base (10s)
            int extraTime = (int) (vehiclesInQueue * INCREMENT_PER_VEHICLE);
            int newGreenDuration = Math.min(MAX_GREEN, baseGreen + extraTime);

            // Atualiza o estado GREEN com a nova duração
            light.changeState(new GreenState(newGreenDuration));
        }
    }

    @Override
    public String getName() {
        return "AdaptiveCycle";
    }
}

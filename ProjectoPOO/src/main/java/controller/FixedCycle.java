package controller;

import model.Intersection;
import model.TrafficLight;

/**
 * Implementação da estratégia de ciclo fixo (Fixed Cycle).
 * Simplesmente chama o tick() em cada semáforo, que usa as durações fixas.
 */
public class FixedCycle implements Strategy {

    @Override
    public void applyStrategy(Intersection intersection) {
        // A lógica de ciclo fixo é tratada inteiramente pelo padrão State dentro do TrafficLight.
        // Para a intersecção em cruz, garantimos que os semáforos opostos (Horizontal/Vertical)
        // estejam sempre sincronizados.
        
        // A intersecção tem 4 estradas: R1(W->E), R2(E->W), R3(N->S), R4(S->N).
        // R1 e R2 (Horizontal) devem estar sincronizados.
        // R3 e R4 (Vertical) devem estar sincronizados.
        
        // Simplificação: Assumimos que o World.setupSimpleScenario() garante que R1/R2 e R3/R4
        // são os pares de estradas.

        TrafficLight light1 = intersection.getTrafficLight(intersection.getRoads().get(0)); // R1
        TrafficLight light3 = intersection.getTrafficLight(intersection.getRoads().get(2)); // R3

        // Apenas chamamos o tick nos semáforos principais (R1 e R3).
        // Os semáforos R2 e R4 devem ser mantidos em sincronia com R1 e R3, respetivamente.
        
        // O padrão State garante a transição de R1 e R3.
        light1.tick();
        light3.tick();

        // Sincroniza os semáforos opostos
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
        
        // Garante que o tempo no estado é sincronizado (necessário para o AdaptiveCycle)
        light2.resetTimeInState();
        light4.resetTimeInState();
    }

    @Override
    public String getName() {
        return "FixedCycle";
    }
}

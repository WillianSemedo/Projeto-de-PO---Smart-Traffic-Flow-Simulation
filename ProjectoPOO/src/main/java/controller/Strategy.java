package controller;

import model.Intersection;
import model.Road;
import model.TrafficLight;

/**
 * Interface Strategy para o padrão Strategy. Define a estratégia de controle de tráfego.
 */
public interface Strategy {

    /**
     * Aplica a estratégia de controle de tráfego a uma intersecção.
     * @param intersection A intersecção a ser controlada.
     */
    void applyStrategy(Intersection intersection);

    /**
     * Obtém o nome da estratégia.
     * @return O nome da estratégia.
     */
    String getName();
}

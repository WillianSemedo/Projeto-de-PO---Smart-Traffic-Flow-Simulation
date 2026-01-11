package model;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Representa uma intersecção que pode conter semáforos.
 */
public class Intersection {
    private final int id;
    // Mapeia a estrada de entrada para o semáforo que a controla
    private final Map<Road, TrafficLight> trafficLights;

    /**
     * Construtor para Intersection.
     * @param id Identificador único da intersecção.
     */
    public Intersection(int id) {
        this.id = id;
        this.trafficLights = new HashMap<>();
    }

    /**
     * Adiciona um semáforo para controlar o tráfego de uma estrada de entrada.
     * @param road A estrada de entrada.
     * @param light O semáforo que controla essa estrada.
     */
    public void addTrafficLight(Road road, TrafficLight light) {
        trafficLights.put(road, light);
        road.setEndIntersection(this);
    }

    /**
     * Atualiza todos os semáforos nesta intersecção.
     */
    public void tick() {
        for (TrafficLight light : trafficLights.values()) {
            light.tick();
        }
    }

    /**
     * Obtém o semáforo para uma estrada específica.
     * @param road A estrada de entrada.
     * @return O semáforo correspondente, ou null se não houver.
     */
    public TrafficLight getTrafficLight(Road road) {
        return trafficLights.get(road);
    }

    // Getters
    public int getId() {
        return id;
    }

    public Map<Road, TrafficLight> getTrafficLights() {
        return trafficLights;
    }

    /**
     * Obtém a lista de estradas que entram nesta intersecção.
     * @return A lista de estradas.
     */
    public List<Road> getRoads() {
        return new java.util.ArrayList<>(trafficLights.keySet());
    }
}

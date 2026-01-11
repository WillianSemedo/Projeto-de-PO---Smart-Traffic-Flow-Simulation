package model;

/**
 * Representa um veículo na simulação.
 */
public class Vehicle {
    private final int id;
    private double position; // Posição na estrada (0 a Road.length)
    private double speed; // Velocidade em unidades/tick (sugerido 10)
    private Road currentRoad;
    private boolean stoppedByTrafficLight;
    private int waitingTime = 0;

    private static final double MIN_DISTANCE = 2.0; // Distância mínima para evitar colisão

    /**
     * Construtor para Vehicle.
     * @param id Identificador único do veículo.
     * @param initialRoad A estrada onde o veículo começa.
     * @param initialPosition A posição inicial na estrada.
     * @param speed A velocidade do veículo.
     */
    public Vehicle(int id, Road initialRoad, double initialPosition, double speed) {
        this.id = id;
        this.currentRoad = initialRoad;
        this.position = initialPosition;
        this.speed = speed;
        this.stoppedByTrafficLight = false;
    }

    /**
     * Construtor com velocidade padrão sugerida (10 unidades/tick).
     */
    public Vehicle(int id, Road initialRoad, double initialPosition) {
        this(id, initialRoad, initialPosition, 10.0);
    }

    /**
     * Tenta mover o veículo no próximo tick.
     * @param nextVehicle O próximo veículo na estrada (ou null se for o último).
     * @param trafficLight O semáforo no final da estrada (ou null se não houver).
     */
    public void move(Vehicle nextVehicle, TrafficLight trafficLight) {
        double distanceToMove = speed;
        stoppedByTrafficLight = false;

        // 1. Verificar colisão com o veículo da frente
        if (nextVehicle != null) {
            double distanceToNext = nextVehicle.getPosition() - this.position;
            if (distanceToNext < speed + MIN_DISTANCE) {
                // Reduz a velocidade para manter a distância mínima, garantindo que não ultrapasse o veículo da frente
                distanceToMove = Math.min(distanceToMove, Math.max(0, distanceToNext - MIN_DISTANCE));
            }
        }

        // 2. Verificar semáforo
        if (trafficLight != null) {
            double distanceToIntersection = currentRoad.getLength() - this.position;
            if (distanceToIntersection <= distanceToMove + MIN_DISTANCE) {
                if (trafficLight.getState() == TrafficLightState.RED) {
                    // Semáforo vermelho, para antes da intersecção
                    distanceToMove = Math.min(distanceToMove, Math.max(0, distanceToIntersection - MIN_DISTANCE));
                    stoppedByTrafficLight = true;
                } else if (trafficLight.getState() == TrafficLightState.YELLOW) {
                    // Semáforo amarelo, para antes da intersecção se não conseguir passar
                    if (distanceToIntersection > MIN_DISTANCE && distanceToIntersection < speed) {
                        // Tenta passar (mantém distanceToMove)
                    } else if (distanceToIntersection > MIN_DISTANCE) {
                        // Para antes
                        distanceToMove = Math.min(distanceToMove, Math.max(0, distanceToIntersection - MIN_DISTANCE));
                        stoppedByTrafficLight = true;
                    }
                }
            }
        }

        // 3. Mover
        if (distanceToMove < 0.1) {
            waitingTime++;
        }
        this.position += distanceToMove;

        // 4. Verificar se chegou ao fim da estrada (para ser tratado pelo World)
        if (this.position >= currentRoad.getLength()) {
            this.position = currentRoad.getLength(); // Garante que não ultrapassa
        }
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Road getCurrentRoad() {
        return currentRoad;
    }

    public void setCurrentRoad(Road currentRoad) {
        this.currentRoad = currentRoad;
        this.position = 0; // Assume que começa no início da nova estrada
    }

    public boolean isStoppedByTrafficLight() {
        return stoppedByTrafficLight;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public static double getMinDistance() {
        return MIN_DISTANCE;
    }
}

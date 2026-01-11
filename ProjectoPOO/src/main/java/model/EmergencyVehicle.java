package model;

/**
 * Representa um veículo de emergência com prioridade.
 */
public class EmergencyVehicle extends Vehicle {
    public EmergencyVehicle(int id, Road initialRoad, double initialPosition) {
        super(id, initialRoad, initialPosition, 15.0); // Mais rápido que o normal
    }

    @Override
    public void move(Vehicle nextVehicle, TrafficLight trafficLight) {
        // Veículos de emergência ignoram semáforos vermelhos (com cautela simulada)
        // Mas ainda respeitam a distância para o veículo da frente
        
        double distanceToMove = getSpeed();
        
        if (nextVehicle != null) {
            double distanceToNext = nextVehicle.getPosition() - getPosition();
            if (distanceToNext < getSpeed() + getMinDistance()) {
                distanceToMove = Math.min(distanceToMove, Math.max(0, distanceToNext - getMinDistance()));
            }
        }

        // Ignora a lógica de paragem do semáforo que existe na classe base
        setPosition(getPosition() + distanceToMove);

        if (getPosition() >= getCurrentRoad().getLength()) {
            setPosition(getCurrentRoad().getLength());
        }
    }
}

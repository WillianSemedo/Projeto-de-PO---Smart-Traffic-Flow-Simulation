package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma estrada na simulação.
 */
public class Road {
    private final int id;
    private final double length;
    private final List<Vehicle> vehicles;
    private Intersection endIntersection; // Interseção no final da estrada

    /**
     * Construtor para Road.
     * @param id Identificador único da estrada.
     * @param length O comprimento da estrada.
     */
    public Road(int id, double length) {
        this.id = id;
        this.length = length;
        this.vehicles = new ArrayList<>();
    }

    /**
     * Adiciona um veículo à estrada.
     * @param vehicle O veículo a ser adicionado.
     */
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        // Ordena os veículos pela posição (do mais próximo ao mais distante do início)
        vehicles.sort((v1, v2) -> Double.compare(v1.getPosition(), v2.getPosition()));
    }

    /**
     * Remove um veículo da estrada.
     * @param vehicle O veículo a ser removido.
     */
    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
    }

    /**
     * Atualiza a posição de todos os veículos na estrada.
     * @param trafficLight O semáforo no final da estrada.
     */
    public void updateVehicles(TrafficLight trafficLight) {
        // Ordena os veículos para garantir que a lógica de colisão funcione corretamente
        // (do mais distante para o mais próximo do final da estrada)
        vehicles.sort((v1, v2) -> Double.compare(v2.getPosition(), v1.getPosition()));

        Vehicle nextVehicle = null;
        for (Vehicle vehicle : vehicles) {
            vehicle.move(nextVehicle, trafficLight);
            nextVehicle = vehicle;
        }

        // Reordena para o padrão (do mais próximo ao mais distante do início)
        vehicles.sort((v1, v2) -> Double.compare(v1.getPosition(), v2.getPosition()));
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public double getLength() {
        return length;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public Intersection getEndIntersection() {
        return endIntersection;
    }

    public void setEndIntersection(Intersection endIntersection) {
        this.endIntersection = endIntersection;
    }

    /**
     * Obtém o número de veículos parados ou em fila na estrada.
     * (Simplificação: veículos na primeira metade da estrada)
     * @return O número de veículos.
     */
    public long getVehiclesInQueue() {
        return vehicles.stream()
                .filter(v -> v.getPosition() < this.length / 2)
                .count();
    }

    /**
     * Verifica se há espaço suficiente no início da estrada para adicionar um novo veículo.
     * @return true se houver espaço, false caso contrário.
     */
    public boolean canAddVehicle() {
        if (vehicles.isEmpty()) {
            return true;
        }
        // O primeiro veículo (o mais próximo do início) está na posição 0
        Vehicle firstVehicle = vehicles.stream()
                .min((v1, v2) -> Double.compare(v1.getPosition(), v2.getPosition()))
                .orElse(null);

        // Se o primeiro veículo estiver a uma distância maior que a distância mínima, pode adicionar
        return firstVehicle == null || firstVehicle.getPosition() > Vehicle.getMinDistance() * 2;
    }

}

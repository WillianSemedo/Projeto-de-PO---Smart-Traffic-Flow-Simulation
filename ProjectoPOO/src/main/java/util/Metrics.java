package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe para recolha e cálculo de métricas da simulação.
 */
public class Metrics {
    private int totalVehiclesServed = 0;
    private long totalWaitingTime = 0;
    private final List<Integer> queueLengths = new ArrayList<>();

    public void recordVehicleServed(int waitingTime) {
        totalVehiclesServed++;
        totalWaitingTime += waitingTime;
    }

    public void recordQueueLength(int length) {
        queueLengths.add(length);
    }

    public double getAverageWaitingTime() {
        return totalVehiclesServed == 0 ? 0 : (double) totalWaitingTime / totalVehiclesServed;
    }

    public int getTotalVehiclesServed() {
        return totalVehiclesServed;
    }

    public double getAverageQueueLength() {
        return queueLengths.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    public void reset() {
        totalVehiclesServed = 0;
        totalWaitingTime = 0;
        queueLengths.clear();
    }
}

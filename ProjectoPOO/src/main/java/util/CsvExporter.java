package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Utilitário para exportar métricas para CSV.
 */
public class CsvExporter {
    public static void exportMetrics(Metrics metrics, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Metric,Value");
            writer.println("Total Vehicles Served," + metrics.getTotalVehiclesServed());
            writer.println("Average Waiting Time," + String.format("%.2f", metrics.getAverageWaitingTime()));
            writer.println("Average Queue Length," + String.format("%.2f", metrics.getAverageQueueLength()));
            System.out.println("Estatísticas exportadas para " + filename);
        } catch (IOException e) {
            System.err.println("Erro ao exportar CSV: " + e.getMessage());
        }
    }
}

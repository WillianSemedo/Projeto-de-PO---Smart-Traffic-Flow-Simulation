package model;

import controller.Strategy;
import controller.FixedCycle;
import util.Metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representa o mundo da simulação, contendo todas as estradas, intersecções e veículos.
 */
public class World {
    private final List<Road> roads;
    private final List<Intersection> intersections;
    private final List<Vehicle> vehicles;
    private int simulationTime;
    private boolean isRunning;
    private int nextVehicleId = 1; // Contador para IDs de veículos
    private int vehicleGenerationInterval = 5; // Intervalo de ticks para gerar um novo veículo
    private int lastGenerationTime = 0; // Tempo do último tick de geração
    private Strategy trafficStrategy; // Padrão Strategy
    private final Metrics metrics = new Metrics();

    /**
     * Construtor para World.
     */
    public World() {
        this.roads = new ArrayList<>();
        this.intersections = new ArrayList<>();
        this.vehicles = new ArrayList<>();
        this.simulationTime = 0;
        this.isRunning = false; // Adicionado controle de estado
        this.trafficStrategy = new FixedCycle(); // Estratégia padrão
        this.nextVehicleId = 1;
        this.lastGenerationTime = 0;
    }

    /**
     * Define a estratégia de controle de tráfego.
     * @param strategy A estratégia a ser usada.
     */
    public void setTrafficStrategy(Strategy strategy) {
        this.trafficStrategy = strategy;
    }

    /**
     * Adiciona uma estrada ao mundo.
     * @param road A estrada a ser adicionada.
     */
    public void addRoad(Road road) {
        roads.add(road);
    }

    /**
     * Adiciona uma intersecção ao mundo.
     * @param intersection A intersecção a ser adicionada.
     */
    public void addIntersection(Intersection intersection) {
        intersections.add(intersection);
    }

    /**
     * Adiciona um veículo ao mundo e à sua estrada inicial.
     * @param vehicle O veículo a ser adicionado.
     */
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicle.getCurrentRoad().addVehicle(vehicle);
        nextVehicleId++; // Incrementa o ID para o próximo veículo
    }

    /**
     * Executa um passo da simulação (um "tick").
     */
    public void tick() {
        if (!isRunning) {
            return; // Não executa se não estiver rodando
        }
        simulationTime++;

        // 1. Geração de veículos
        generateVehicles();

        // 2. Atualiza semáforos usando a estratégia
        for (Intersection intersection : intersections) {
            trafficStrategy.applyStrategy(intersection);
        }

        // 2. Atualiza veículos e trata a transição de estradas
        List<Vehicle> vehiclesToRemove = new ArrayList<>();
        List<Vehicle> vehiclesToMove = new ArrayList<>();

        for (Road road : roads) {
            // Obtém o semáforo no final da estrada, se houver
            TrafficLight light = (road.getEndIntersection() != null)
                    ? road.getEndIntersection().getTrafficLight(road)
                    : null;

            // Atualiza a posição dos veículos na estrada
            road.updateVehicles(light);

            // Verifica veículos que chegaram ao final da estrada
            for (Vehicle vehicle : road.getVehicles()) {
                if (vehicle.getPosition() >= road.getLength()) {
                    vehiclesToRemove.add(vehicle);
                    vehiclesToMove.add(vehicle);
                }
            }
        }

        // Remove veículos que chegaram ao final da estrada
        for (Vehicle vehicle : vehiclesToRemove) {
            metrics.recordVehicleServed(vehicle.getWaitingTime());
            vehicle.getCurrentRoad().removeVehicle(vehicle);
        }
        
        // Registar comprimentos de fila
        for (Road road : roads) {
            metrics.recordQueueLength((int) road.getVehiclesInQueue());
        }

        // Trata a transição de estradas (lógica simplificada: remove do mundo)
        // Em uma simulação real, a lógica de intersecção determinaria a próxima estrada.
        // Por enquanto, apenas removemos os veículos que chegam ao fim.
        vehicles.removeAll(vehiclesToMove);
    }

    // Getters
    public List<Road> getRoads() {
        return roads;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public int getSimulationTime() {
        return simulationTime;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public int getVehicleGenerationInterval() {
        return vehicleGenerationInterval;
    }

    public void setVehicleGenerationInterval(int interval) {
        this.vehicleGenerationInterval = interval;
    }

    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Inicia a simulação.
     */
    public void start() {
        this.isRunning = true;
    }

    /**
     * Para a simulação.
     */
    public void stop() {
        this.isRunning = false;
    }

    /**
     * Reinicia o mundo para o estado inicial.
     */
    public void reset() {
        // Lógica de reset: recria o mundo
        this.roads.clear();
        this.intersections.clear();
        this.vehicles.clear();
        this.simulationTime = 0;
        this.isRunning = false;
        this.nextVehicleId = 1; // Reseta o contador de IDs
        this.lastGenerationTime = 0; // Reseta o tempo de geração
        setupSimpleScenario(); // Recarrega o cenário inicial
    }

    /**
     * Método de inicialização para criar um cenário de teste simples.
     */
    public void setupSimpleScenario() {
        // Cria 4 estradas para a intersecção em cruz (todas com 100 unidades de comprimento)
        
        // Remove a criação inicial de veículos para que a geração automática funcione
        // (As estradas serão criadas, mas os veículos serão gerados pelo tick)
        
        // Cria 4 estradas para a intersecção em cruz (todas com 100 unidades de comprimento)
        // R1: Oeste -> Leste (Horizontal)
        Road road1 = new Road(1, 100.0);
        // R2: Leste -> Oeste (Horizontal)
        Road road2 = new Road(2, 100.0);
        // R3: Norte -> Sul (Vertical)
        Road road3 = new Road(3, 100.0);
        // R4: Sul -> Norte (Vertical)
        Road road4 = new Road(4, 100.0);

        addRoad(road1);
        addRoad(road2);
        addRoad(road3);
        addRoad(road4);

        // Cria intersecção e semáforos
        Intersection intersection1 = new Intersection(1);
        
        // Semáforos (durações base: Green=10s, Yellow=2s, Red=5s)
        // Fase 1: Horizontal (R1 e R2) GREEN, Vertical (R3 e R4) RED
        TrafficLight light1 = new TrafficLight(TrafficLightState.GREEN, 10, 2, 5); // R1: Horizontal (W->E)
        TrafficLight light2 = new TrafficLight(TrafficLightState.GREEN, 10, 2, 5); // R2: Horizontal (E->W)
        TrafficLight light3 = new TrafficLight(TrafficLightState.RED, 10, 2, 5);   // R3: Vertical (N->S)
        TrafficLight light4 = new TrafficLight(TrafficLightState.RED, 10, 2, 5);   // R4: Vertical (S->N)

        intersection1.addTrafficLight(road1, light1);
        intersection1.addTrafficLight(road2, light2);
        intersection1.addTrafficLight(road3, light3);
        intersection1.addTrafficLight(road4, light4);
        addIntersection(intersection1);

        // Remove a criação inicial de veículos para que a geração automática funcione
        // (Os veículos serão gerados pelo tick)
    }

    /**
     * Tenta gerar um novo veículo em uma estrada de entrada aleatória.
     */
    private void generateVehicles() {
        if (simulationTime - lastGenerationTime >= vehicleGenerationInterval) {
            // Lista de estradas de onde os veículos podem entrar (todas as estradas neste cenário)
            List<Road> entryRoads = roads;
            
            // Tenta gerar um veículo em cada estrada de entrada
            for (Road road : entryRoads) {
                if (road.canAddVehicle()) {
                    if (Math.random() < 0.1) { // 10% de chance de ser emergência
                        addVehicle(new EmergencyVehicle(nextVehicleId, road, 0.0));
                    } else {
                        addVehicle(new Vehicle(nextVehicleId, road, 0.0));
                    }
                }
            }
            lastGenerationTime = simulationTime;
        }
    }



    /**
     * Método principal para demonstrar a simulação.
     */
    public static void main(String[] args) {
        World world = new World();
        world.setupSimpleScenario();

        System.out.println("Início da Simulação de Tráfego");
        System.out.println("--------------------------------");

        for (int i = 0; i < 20; i++) {
            world.tick();
            System.out.println("Tempo de Simulação: " + world.getSimulationTime());

            // Exibe o estado dos semáforos
            Intersection intersection = world.getIntersections().get(0);
            Road road1 = world.getRoads().get(0);
            Road road2 = world.getRoads().get(1);
            TrafficLight tl1 = intersection.getTrafficLight(road1);
            TrafficLight tl2 = intersection.getTrafficLight(road2);

            System.out.printf("  Semáforo 1 (Road %d): %s (Tempo: %d)\n", road1.getId(), tl1.getState(), tl1.getTimeInState());
            System.out.printf("  Semáforo 2 (Road %d): %s (Tempo: %d)\n", road2.getId(), tl2.getState(), tl2.getTimeInState());

            // Exibe a posição dos veículos
            for (Vehicle v : world.getVehicles()) {
                System.out.printf("  Veículo %d (Road %d): Posição %.2f%s\n",
                        v.getId(), v.getCurrentRoad().getId(), v.getPosition(),
                        v.isStoppedByTrafficLight() ? " (PARADO)" : "");
            }
            System.out.println("--------------------------------");
        }

        System.out.println("Fim da Simulação. Veículos restantes: " + world.getVehicles().size());
    }
}

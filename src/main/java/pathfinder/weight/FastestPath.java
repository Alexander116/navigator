package pathfinder.weight;

import api.graph.Edge;

/**
 * Вес ребра для быстрейшего пути
 */
public class FastestPath implements Weight {
    @Override
    public double getWeight(Edge edge) {
        return edge.getDistance() / edge.getSpeed();
    }
}

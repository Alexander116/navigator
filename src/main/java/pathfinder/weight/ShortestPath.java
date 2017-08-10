package pathfinder.weight;

import api.graph.Edge;

/**
 * Вес ребра для кратчайшего пути
 */
public class ShortestPath implements Weight {
    @Override
    public double getWeight(Edge edge) {
        return edge.getDistance();
    }
}

package pathfinder.weight;

import api.graph.Edge;

/**
 * Определяет вес ребра
 */
public interface    Weight {

    /**
     * Возвращает вес ребра
     * @param edge
     * @return
     */
    double getWeight(Edge edge);
}

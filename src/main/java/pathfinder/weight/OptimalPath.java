package pathfinder.weight;

import api.settings.Settings;
import api.graph.Edge;
import api.graph.Graph;
import pathfinder.BlockEdgeFinder;


/**
 * Вес ребра для оптимального пути
 */
public class OptimalPath implements Weight {

    private static final double OPTIMAL_RATIO = 17;
    private static final int THIRTY_MINUTES = 3600;
    private double customCoefficient;
    private BlockEdgeFinder blockEdgeFinder;


    public OptimalPath(Settings settings, Graph graph) {
        this.customCoefficient = settings.getOptimalCoefficient();
        blockEdgeFinder = new BlockEdgeFinder(settings,graph);
    }

    @Override
    public double getWeight(Edge edge) {
        double time = edge.getDistance() / edge.getSpeed();
        double distSpeedRatio = edge.getSpeed() / edge.getDistance();
        double weight = time + Math.abs(distSpeedRatio - OPTIMAL_RATIO) * customCoefficient;

        if(blockEdgeFinder.isBlockEdge(edge)) {
            weight += THIRTY_MINUTES;
        }
        return weight;
    }

}

package api.path;

import api.graph.Node;

import java.util.List;

/**
 * Маршрут от точки A до B
 */
public class Route {


    private double dist;



    private double time;
    private List<Node> nodes;

    public Route(List<Node> nodes, double dist, double time){
        this.nodes = nodes;
        this.dist = dist;
        this.time = time;
    }
    public double getTime() {
        return time;
    }
    public double getDist() {
        return dist;
    }

    public List<Node> getNodes() {
        return nodes;
    }
}

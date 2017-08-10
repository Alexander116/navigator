package pathfinder;

import api.*;
import api.graph.Coordinates;
import api.graph.Edge;
import api.graph.Graph;
import api.graph.Node;
import api.path.PathFinder;
import api.path.Route;
import pathfinder.quadtree.EdgeQuadTree;
import pathfinder.weight.Weight;

import java.util.*;

/**
 * Находит кратчайший путь до точки по алгоритму Дейкстры
 */
public class BellmanFordFinder implements PathFinder {

    private EdgeQuadTree edgesTree;
    private double[] weightTo;
    private boolean[] onQueue;
    private Edge[] resEdges;
    private Queue<Node> queue = new LinkedList<>();
    private Edge from;
    private Edge to;
    private Weight weighting;

    public BellmanFordFinder(Weight weighting, Graph graph, Coordinates from, Coordinates to) {
        int nodesTreeSize = graph.getNodesTree().size();
        edgesTree = graph.getEdgesTree();
        this.from = edgesTree.getClosestObject(from);
        this.to = edgesTree.getClosestObject(to);
        this.weighting = weighting;
        weightTo = new double[nodesTreeSize];
        onQueue = new boolean[nodesTreeSize];
        resEdges = new Edge[nodesTreeSize];
        for (int i = 0; i < weightTo.length; i++) {
            weightTo[i] = Double.POSITIVE_INFINITY;
            onQueue[i] = false;
        }
        int fromId = (int)this.from.getFirstNode().getId();
        weightTo[fromId] = 0;
        onQueue[fromId] = true;
    }

    @Override
    public Route getRoute() {

        findPaths(from.getFirstNode());
        PathCreator pathCreator = new PathCreator(resEdges, from.getFirstNode(),to.getFirstNode());
        return pathCreator.getPathNodes();
    }

    private void findPaths(Node nextNode) {
        queue.add(nextNode);
        while(!queue.isEmpty()){
            Node n = queue.remove();
            onQueue[(int)n.getId()] = false;
            relaxEdges(n);
        }
    }

    private void relaxEdges(Node node) {
        List<Edge> neighbours = edgesTree.getNeighborEdge(node);
        for(Edge edge : neighbours){
            relax(edge);
        }
    }

    private void relax(Edge edge) {
        int next = (int)edge.getSecondNode().getId();
        int id= (int) edge.getFirstNode().getId();

        double weight = weighting.getWeight(edge);

        if (weightTo[next] > (weightTo[id] + weight)) {
            weightTo[next] = weightTo[id] + weight;
            resEdges[next] = edge;
            if(!onQueue[next]){
                queue.add(edge.getSecondNode());
                onQueue[next] = true;
            }
        }
    }

}

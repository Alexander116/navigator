package api.graph;

import java.util.List;

/**
 * ребро графа
 */
public class Edge {
    private Node firstNode;
    private Node secondNode;
    private double distance;

    private double speed;
    private List<Node> intermediateNodes;
    private boolean isReverse;

    public Edge(Node firstNode, Node secondNode, double distance, double speed, List<Node> intermediateNodes, boolean isReverse) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.isReverse = isReverse;
        this.speed = speed;
        this.distance = distance;
        this.intermediateNodes = intermediateNodes;
    }

    public Edge(Node firstNode, Node secondNode, double distance, double speed, List<Node> intermediateNodes) {
        this( firstNode,  secondNode, distance, speed, intermediateNodes, false);
    }

    public Edge(Node firstNode, Node secondNode, double distance, int speed) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.speed = speed;
        this.distance = distance;
    }

    public double getDistance(){
        return distance;
    }

    /**
     * Возвращает первую вершину
     * @return
     */
    public Node getFirstNode(){
        return firstNode;
    }
    
    public double getSpeed(){
        return speed;
    }

    /**
     * Возвращает вторую вершину
     * @return
     */
    public Node getSecondNode(){
        return secondNode;
    }

    /**
     * Возвращает промежуточные точки между вершинами
     * @return
     */
    public List<Node> getIntermediateNodes(){
        return intermediateNodes;
    }

    /**
     * По умолчанию false, если true значит intermediateNodes расположены в обратном порядке
     * @return
     */
    public boolean isReverse(){
        return isReverse;
    }

}

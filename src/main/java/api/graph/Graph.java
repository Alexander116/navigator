package api.graph;

import pathfinder.quadtree.EdgeQuadTree;
import pathfinder.quadtree.NodeQuadTree;

/**
 * дорожный граф
 */
public class Graph {

    private EdgeQuadTree edgesTree;
    private NodeQuadTree nodesTree;

    public Graph(EdgeQuadTree edges){
        this.edgesTree = edges;
        this.nodesTree = new NodeQuadTree(new Coordinates(55.631,52.26),new Coordinates(55.81,52.62),0);
    }

    public Graph(EdgeQuadTree edges, NodeQuadTree nodes){
        this.edgesTree = edges;
        this.nodesTree = nodes;
    }
    
    public int getEdgeCount(){
        return edgesTree.size();
    }

    public EdgeQuadTree getEdgesTree(){
        return edgesTree;
    }
    public NodeQuadTree getNodesTree(){
        return nodesTree;
    }
}

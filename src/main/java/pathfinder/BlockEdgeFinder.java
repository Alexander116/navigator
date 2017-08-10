package pathfinder;

import api.settings.Settings;
import api.graph.Coordinates;
import api.graph.Edge;
import api.graph.Graph;
import api.graph.Node;
import pathfinder.quadtree.NodeQuadTree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * определяет заблокировано ли ребро
 */
public class BlockEdgeFinder {
    private Map<Long, Boolean> bpMap = new HashMap<>();
    private NodeQuadTree nodesTree;

    public BlockEdgeFinder(Settings settings, Graph graph) {
        this.nodesTree = graph.getNodesTree();
        initBlockPointsMap(settings);
    }

    /**
     * Записывает блокирующие точки в мапу
     */
    private void initBlockPointsMap(Settings settings){
        List<Coordinates> blockPoints = settings.getBlockPoints();
        for(Coordinates bp : blockPoints){
            List<Node> blockNodes = nodesTree.getClosestObjects(bp);
            for(Node blockNode : blockNodes){
                bpMap.put(blockNode.getId(),true);
            }
        }
    }

    /**
     * Проверяет является ли ребро блокированным
     * @param edge
     * @return
     */
    public boolean isBlockEdge(Edge edge){
        return isBlockNodes(edge) || isBlockIntermediateNodes(edge);
    }

    private boolean isBlockIntermediateNodes(Edge edge){
        if(edge.getIntermediateNodes() != null){
            for (Node childNode : edge.getIntermediateNodes()){
                if(bpMap.get(childNode.getId()) != null){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isBlockNodes(Edge edge){
        Long firstNodeId = edge.getFirstNode().getId();
        Long secondNodeId = edge.getSecondNode().getId();
        return bpMap.get(firstNodeId) != null || bpMap.get(secondNodeId) != null;
    }
}

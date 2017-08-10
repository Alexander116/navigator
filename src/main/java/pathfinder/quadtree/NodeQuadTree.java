package pathfinder.quadtree;

import api.graph.Coordinates;
import api.graph.Node;

import java.util.ArrayList;
import java.util.List;

import static util.MathDistance.getDistance;

/**
 * Квадро -дерево на основе Node с реализацией поиска по радиусу
 */
public class NodeQuadTree extends AbstractQuadTree<Node>{

    private static final double TWENTY_METRES = 0.0003;
    private static final int SEARCH_RADIUS = 20;

    public NodeQuadTree(Coordinates min, Coordinates max, int level) {
        super(min, max, level,null);
    }

    private NodeQuadTree(Coordinates min, Coordinates max, int level, AbstractQuadTree<Node> parentTree) {
        super(min, max, level, parentTree);
    }

    /**
     * Поиск точек в радиусе
     * @param point
     * @return
     */
    public List<Node> getClosestObjects(Coordinates point){
        circleLine = new CircleLine(point.getLng(),point.getLat());
        search = point;
        return findClosest(this);
    }

    private List<Node> findClosest(NodeQuadTree nodeQuadTree) {
        List<Node> res = new ArrayList<>();
        AbstractQuadTree<Node> last = getLastQuad(nodeQuadTree);
        Node radiusNode = new Node(0, new Coordinates(search.getLat(), search.getLng() + TWENTY_METRES));
        last = upWhileOut(last, radiusNode);
        searchInRadius(last, radiusNode,res);
        return res;
    }

    private List<Node> searchInRadius(AbstractQuadTree<Node> tree, Node radiusNode, List<Node> res) {
        if(tree.level != MAX_LEVEL){
            double diameter = getDistance(search, radiusNode.getCoords()) * 2;
            for(AbstractQuadTree<Node> quad : tree.quads){
                if(isQuadInRadius(quad,radiusNode,diameter)){
                    searchInRadius(quad, radiusNode,res);
                }
            }
        }else{
            res.addAll(searchClosests(tree,search));
        }
        return res;
    }

    private List<Node> searchClosests(AbstractQuadTree<Node> tree, Coordinates search) {
        List<Node>  res = new ArrayList<>();
        for (Node node : tree.objects) {
            double dist = getDistance(search,node.getCoords());
            if(dist < SEARCH_RADIUS){
                res.add(node);
            }
        }
        return res;
    }

    @Override
    AbstractQuadTree<Node> initMe(int nextLevel, Coordinates min, Coordinates max, AbstractQuadTree<Node> parent) {
        return new NodeQuadTree(min,max,nextLevel,parent);
    }
}

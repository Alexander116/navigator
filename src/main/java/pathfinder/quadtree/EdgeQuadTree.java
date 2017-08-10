package pathfinder.quadtree;

import api.graph.Coordinates;
import api.graph.Edge;
import api.graph.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Квадро-дерево на основе Edge с реализацией поиска смежных ребер
 */
public class EdgeQuadTree extends AbstractQuadTree<Edge> {
    private Node searchNode;

    public EdgeQuadTree(Coordinates min, Coordinates max, int level) {
        super(min, max, level,null);
    }

    private EdgeQuadTree(Coordinates min, Coordinates max, int level, AbstractQuadTree<Edge> parentTree) {
        super(min, max, level, parentTree);
    }

    @Override
    EdgeQuadTree initMe(int nextLevel, Coordinates min, Coordinates max, AbstractQuadTree<Edge> parent) {
        return new EdgeQuadTree(min,max,nextLevel,parent);
    }

    /**
     * Возвращает смежные ребра
     * @param search
     * @return
     */
    public List<Edge> getNeighborEdge(Node search) {
        this.search = search.getCoords();
        this.searchNode = search;
        return simpleSearch(this);
    }

    private List<Edge> simpleSearch(AbstractQuadTree<Edge> tree){
        AbstractQuadTree<Edge> last = getLastQuad(tree);
        return searchNeighbors(last);
    }

    private List<Edge> searchNeighbors(AbstractQuadTree<Edge> tree) {
        List<Edge> res = new ArrayList<>();
        for (Edge coord : tree.objects) {
            if(coord.getFirstNode().getId() == searchNode.getId()){
                res.add(coord);
            }
        }
        return res;
    }
}

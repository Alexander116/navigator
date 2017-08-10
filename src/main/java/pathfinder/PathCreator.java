package pathfinder;

import api.graph.Edge;
import api.graph.Node;
import api.path.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Создает путь из планарного графа
 */
class PathCreator {

    private Edge[] resEdges;
    private Node from;
    private Node to;

    PathCreator(Edge[] resEdges, Node from, Node to) {
        this.resEdges = resEdges;
        this.from = from;
        this.to = to;
    }

    /**
     * Возвращает список точек пути сформированный по ребрам
     * @return route
     */
    Route getPathNodes(){
        List<Node> route = new ArrayList<>();
        double dist = 0;
        double time = 0;
        int next =  (int)to.getId();
        while (resEdges[next] != null) {
            route.add(resEdges[next].getSecondNode());
            time += resEdges[next].getDistance() / resEdges[next].getSpeed();
            dist += resEdges[next].getDistance();
            addChildNodes(route, next);
            next = (int) resEdges[next].getFirstNode().getId();
            if (next ==  (int)from.getId()) {
                route.add(from);
                break;
            }
        }
        return new Route(route, dist, time);
    }

    private void addChildNodes(List<Node> edgeRoute, int next) {
        if (resEdges[next].getIntermediateNodes() != null) {
            if(resEdges[next].isReverse()){
                addChildNodesFromFirtstToLast(edgeRoute, next);
            }else {
                addChildNodesFromLastToFirst(edgeRoute, next);
            }
        }
    }


    private void addChildNodesFromLastToFirst(List<Node> edgeRoute, int next) {
        List<Node> all = resEdges[next].getIntermediateNodes();
        for (int j = all.size() - 1; j >= 0; j--) {
            edgeRoute.add(all.get(j));
        }
    }

    private void addChildNodesFromFirtstToLast(List<Node> edgeRoute, int next) {
        List<Node> all = resEdges[next].getIntermediateNodes();
        all.forEach(edgeRoute::add);
    }
}

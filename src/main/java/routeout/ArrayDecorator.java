package routeout;

import api.graph.Node;
import api.path.Route;

/**
 * Выводит маршрут в виде массива точек
 */
public class ArrayDecorator extends Decorator {

    public ArrayDecorator(RoutePrint routePrint) {
        super(routePrint);
    }

    @Override
    public void print(Route r){
        super.print(r);
        StringBuilder coords = new StringBuilder();
        for(Node node : r.getNodes()){
            coords.append("[")
                    .append(node.getCoords().getLng())
                    .append(",")
                    .append( node.getCoords().getLat())
                    .append("],");
        }

        logger.info(coords.substring(0,coords.length()-1));
    }
}

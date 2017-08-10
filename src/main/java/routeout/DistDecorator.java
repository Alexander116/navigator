package routeout;

import api.path.Route;

/**
 * Выводит дистанцию маршрута
 */
public class DistDecorator extends Decorator {

    public DistDecorator(RoutePrint routePrint) {
        super(routePrint);
    }

    @Override
    public void print(Route r){
        super.print(r);
        logger.info(String.format("Дистанция: %.2f м",r.getDist()));
    }
}

package routeout;

import api.path.Route;

/**
 * Выводит время маршрута
 */
public class TimeDecorator extends Decorator {
    public TimeDecorator(RoutePrint routePrint) {
        super(routePrint);
    }

    @Override
    public void print(Route r){
        super.print(r);
        logger.info(String.format("Время: %.2f сек.",r.getTime()));
    }
}

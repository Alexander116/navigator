package routeout;

import api.path.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Decorator implements RoutePrint{
    RoutePrint routePrint;
    Logger logger = LoggerFactory.getLogger(Decorator.class);

    public Decorator(RoutePrint routePrint){
        this.routePrint = routePrint;
    }

    @Override
    public void print(Route route) {
        routePrint.print(route);
    }
}

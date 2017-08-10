package routeout;

import api.settings.Settings;

/**
 * Фабрика вывода маршрута
 */
public class Factory {
    public static RoutePrint create(Settings settings){
        String type = settings.getRouteOutType();
        if(Settings.ROUTE_OUT_TYPE_ARRAY.equals(type)){
            return new Decorator(new ArrayDecorator(new TimeDecorator(new DistDecorator(new MainComponent()))));
        }
        return  new Decorator(new TimeDecorator(new DistDecorator(new MainComponent())));
    }
}

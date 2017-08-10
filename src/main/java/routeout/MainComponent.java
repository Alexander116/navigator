package routeout;

import api.path.Route;

/**
 * замыкающий класс для декоратора ничего не выводит
 */
public class MainComponent implements RoutePrint {
    @Override
    public void print(Route route) {

    }
}

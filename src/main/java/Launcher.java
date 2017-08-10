import api.*;
import api.graph.Coordinates;
import api.graph.Graph;
import api.path.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routeout.Factory;
import routeout.RoutePrint;
import api.settings.Settings;

import java.util.List;
import java.util.Scanner;

/**
 * Инициализирует настройки и запускает приложение
 */
public class Launcher {

    private static Logger logger = LoggerFactory.getLogger(Launcher.class);

    private static final String BEFORE_READ_TEXT = "write two coordinates(latitude longitude;latitude1 longitude1)";
    private static Facade facade;
    private static Settings settings = new Settings();
    private static Scanner scan = new Scanner(System.in);
    private static RoutePrint printer = Factory.create(settings);


    public static void main(String[] args) throws GraphException {

        facade = new Facade(settings);
        Graph graph = facade.getGraph();
        String read = readValues();

        while(!"q".equals(read)){
            findPath(read, graph);
            read = readValues();
        }
        System.exit(1);
    }

    private static String readValues(){
        logger.info(BEFORE_READ_TEXT);
        return scan.nextLine();
    }

    private static void findPath(String coordinates, Graph graph){
        try{
            String[] ar = coordinates.split(";");
            Coordinates firstPoint = Coordinates.parseCoordinate(ar[0]);
            Coordinates secondPoint = Coordinates.parseCoordinate(ar[1]);

            if(Settings.PATH_OPTIMAL.equalsIgnoreCase(settings.getPathType())){
                List<Route> routes = facade.getThreePath(graph,firstPoint,secondPoint);
                outRoutes(routes);
            }else{
                Route r = facade.getOnePath(graph,firstPoint,secondPoint);
                outRoute(r);
            }
        }catch (Exception e){
            logger.warn("path can`t be found",e);
        }
    }

    private static void outRoutes(List<Route> routes){
        routes.forEach(Launcher::outRoute);
    }

    private static void outRoute(Route r){
        if(r.getNodes() != null && !r.getNodes().isEmpty()){
            printer.print(r);
        }else{
            logger.info("Пути до точки не существует.");
        }
    }
}
//55.7714 52.4276;55.7308 52.3991
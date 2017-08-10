package api;

import api.graph.Coordinates;
import api.graph.Graph;
import api.path.PathFinder;
import api.path.Route;
import mapparser.GraphCreator;
import org.w3c.dom.Document;
import pathfinder.BellmanFordFinder;
import pathfinder.weight.Factory;
import pathfinder.weight.Weight;
import api.settings.Settings;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Facade {

    private Settings settings;

    public Facade(Settings settings){
        this.settings = settings;
    }

    /**
     * Создает граф по карте и находит путь,
     * тип которого указан в настройках
     * @param firstPoint
     * @param secondPoint
     * @return
     * @throws GraphException
     */
    public Route getOnePath(Graph graph, Coordinates firstPoint, Coordinates secondPoint) throws GraphException {
        Weight weight = Factory.create(settings,graph);
        PathFinder finder = new BellmanFordFinder(weight,graph,firstPoint, secondPoint);
        return finder.getRoute();
    }

    /**
     * Возвращает список кратчайшего, быстрейшего и оптимального маршрутов
     * @param firstPoint точка начала пути
     * @param secondPoint точка конца пути
     * @return
     * @throws GraphException
     */
    public List<Route> getThreePath(Graph graph, Coordinates firstPoint, Coordinates secondPoint) throws GraphException {
        List<Route> routes = new ArrayList<>();

        routes.add(getRoute(Settings.PATH_SHORTEST, graph,firstPoint, secondPoint));
        routes.add(getRoute(Settings.PATH_FASTEST, graph,firstPoint, secondPoint));
        routes.add(getRoute(Settings.PATH_OPTIMAL, graph,firstPoint, secondPoint));

        return routes;
    }

    private Route getRoute( String type, Graph graph, Coordinates firstPoint, Coordinates secondPoint) throws GraphException {
        settings.setPathType(type);
        return getOnePath(graph,firstPoint,secondPoint);
    }

    /**
     * Возвращает граф построенный на основе конфигураций
     * @return
     * @throws GraphException
     */
    public Graph getGraph() throws GraphException {
        GraphCreator graphCreator = new GraphCreator();
        Document doc = getDoc(settings.getMapPath());
        return graphCreator.getGraph(settings,doc);
    }

    public static Document getDoc(String osmPath) throws GraphException {
        try{
            File inputFile = new File(osmPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            return doc;
        }catch(Exception e){
            throw new GraphException("get doc error",e);
        }
    }

}

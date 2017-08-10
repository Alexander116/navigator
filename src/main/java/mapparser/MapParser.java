package mapparser;

import api.graph.Coordinates;
import api.GraphException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;


/**
 *  Переводит карту какого-либо формата в граф
 */
public interface MapParser {

    /**
     * Принимает списки Way, Node и заполняет их данными из osm
     * @throws GraphException
     */
    void convertMap(Executor executor, List<OsmWay> ways, Map<Long, OsmNode> nodes) throws GraphException;

    /**
     * Координаты минимальной точки
     * @return
     */
    Coordinates getMinPoint();

    /**
     * Координаты максимальной точки
     * @return
     */
    Coordinates getMaxPoint();

    /**
     * количество точек
     * @return
     */
    int nodesCount();

    /**
     * количество путей
     * @return
     */
    int waysCount();

}

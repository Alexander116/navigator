package mapparser;

import api.*;
import static util.MathDistance.getDistance;

import api.graph.Edge;
import api.graph.Graph;
import api.graph.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import pathfinder.quadtree.EdgeQuadTree;
import pathfinder.quadtree.NodeQuadTree;
import api.settings.Settings;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Конвертирует объекты OsmWay и Nodes в дорожный граф
 */
public class GraphCreator {
    private static final double METRES_IN_SECONDS = 0.277777778;

    private Logger logger = LoggerFactory.getLogger(OsmParser.class);
    private List<Edge> edges = Collections.synchronizedList(new ArrayList<>());
    private Map<Long, OsmNode> nodes;
    private List<OsmWay> ways;
    private volatile AtomicInteger newLocalId = new AtomicInteger(0);
    private EdgeQuadTree quadTreeEdges;
    private NodeQuadTree quadTreeNodes;
    private static final int THREADS_COUNT = Runtime.getRuntime().availableProcessors() - 1;
    private final Executor executor = Executors.newFixedThreadPool(THREADS_COUNT);

    private volatile AtomicInteger objectsParsed = new AtomicInteger(0);

    private Settings settings;

    public Graph getGraph(Settings settings, Document doc) throws GraphException {
        try{
            this.settings = settings;
            MapParser parser = new OsmParser(doc);
            nodes = Collections.synchronizedMap(new HashMap<>(parser.nodesCount()));
            ways = Collections.synchronizedList(new ArrayList<>(parser.waysCount()));
            parser.convertMap(executor, ways, nodes);
            quadTreeEdges = new EdgeQuadTree(parser.getMinPoint(),parser.getMaxPoint(),0);
            quadTreeNodes = new NodeQuadTree(parser.getMinPoint(),parser.getMaxPoint(),0);
            return createGraph();
        }catch(Exception e){
            throw new GraphException("Graph create exception",e);
        }
    }

    private Graph createGraph() {

        ways.forEach(way -> executor.execute(run(way)));
        blockMethodForThread();
        removePilledAndSetResult();
        return new Graph(quadTreeEdges,quadTreeNodes);
    }

    private void removePilledAndSetResult() {
        List<Long> ids = nodes.entrySet().stream()
                .filter(entry -> entry.getValue().getUses().get() < 2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        ids.forEach(nodes::remove);

        nodes.entrySet().
                forEach(this::osmNodeToNode);
        edges.forEach(quadTreeEdges::add);
    }

    private Runnable run(OsmWay way) {
        return () -> splitWayToEdge(way);
    }

    /**
     * разбивает Way на графы по Tower Nodе и считает дистанцию между ними
     *
     * @param way
     */
    private void splitWayToEdge(OsmWay way) {
        int speed = settings.getSpeedFromMap(way.getHighwayType());
        List<OsmNode> nodesElements = way.getNodes();
        if (!nodesElements.isEmpty()) {
            splitWay(nodesElements,speed);
        }
        objectsParsed.getAndIncrement();
    }

    private void splitWay(List<OsmNode> nodesElements, int speed){
        Double distance = 0.0;
        Node begin = nodesElements.get(0);
        List<Node> wayPoints = new ArrayList<>();
        for (int j = 1; j < nodesElements.size(); j++) {
            OsmNode tmpNode = nodesElements.get(j);
            distance += getDistance(nodesElements.get(j - 1).getCoords(), tmpNode.getCoords());
            if (tmpNode.isTower()) {
                Node end = nodesElements.get(j);
                double speedInMetres = speed * METRES_IN_SECONDS;
                Edge first = new Edge(begin, end, distance, speedInMetres, wayPoints,false);
                Edge second = new Edge(end, begin, distance, speedInMetres, wayPoints,true);
                edges.add(first);
                edges.add(second);
                distance = 0.0;
                begin = end;
                wayPoints = new ArrayList<>();
            } else {
                wayPoints.add(tmpNode);
            }
        }
    }

    private void blockMethodForThread() {
        int waysSize = ways.size();
        while (objectsParsed.get() < waysSize) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.warn("create graph Thread.sleep error", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private void osmNodeToNode(Map.Entry entry) {
        OsmNode node = (OsmNode) entry.getValue();
        node.setId(getNextId());
        quadTreeNodes.add(node);
    }

    private int getNextId() {
        return newLocalId.getAndIncrement();
    }

}

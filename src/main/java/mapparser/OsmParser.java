package mapparser;

import api.graph.Coordinates;
import api.GraphException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.w3c.dom.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Парсит .osm карту (забирает из .osm карты ways,nodes)
 */
class OsmParser implements MapParser{


    private Logger logger= LoggerFactory.getLogger(OsmParser.class);

    private static final String ROOT = "osm";
    private static final String NODE = "node";
    private static final String WAY = "way";
    private static final String WAY_NODE = "nd";
    private static final String WAY_NODE_ATRIBUTE_ID = "ref";

    private Document doc;
    private Executor executor;
    private Map<Long, OsmNode> nodes;
    private List<OsmWay> ways ;
    private volatile AtomicInteger objectsParsed = new AtomicInteger(0);

    OsmParser(Document doc) {
        this.doc = doc;
    }

    static int getMapSize(Document doc){
        NodeList nList = doc.getElementsByTagName("osm").item(0).getChildNodes();
        return nList.getLength();
    }

    @Override
    public void convertMap(Executor executor, List<OsmWay> ways, Map<Long, OsmNode> nodes) throws GraphException {
        try{
            this.executor = executor;
            this.ways = ways;
            this.nodes = nodes;
            parseXml(doc);
            removeUnusedNodes();
        }catch( Exception e){
            throw new GraphException("Document parse error",e);
        }
    }

    @Override
    public Coordinates getMinPoint() {
        return getCoordinatesFromElement(
                (Element) doc.getElementsByTagName("bounds").item(0),
                "minlat",
                "minlon"
        );
    }

    @Override
    public Coordinates getMaxPoint() {
        return getCoordinatesFromElement(
                (Element) doc.getElementsByTagName("bounds").item(0),
                "maxlat",
                "maxlon"
        );
    }

    @Override
    public int nodesCount() {
        return doc.getElementsByTagName(NODE).getLength();
    }

    @Override
    public int waysCount() {
        return doc.getElementsByTagName(WAY).getLength();
    }

    private Coordinates getCoordinatesFromElement(Element eElement, String strLat, String strLng){
        NamedNodeMap attrs = eElement.getAttributes();
        double lat = Double.parseDouble(attrs.getNamedItem(strLat).getTextContent());
        double lng = Double.parseDouble(attrs.getNamedItem(strLng).getTextContent());
        return new Coordinates(lat,lng);
    }

    private void removeUnusedNodes(){
        List<Long> ids= nodes.entrySet().stream()
                .filter(entry -> entry.getValue().getUses().get() == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        ids.forEach(nodes::remove);
    }

    private void parseXml(Document doc) {
        NodeList nList = doc.getElementsByTagName(ROOT).item(0).getChildNodes();
        int nodesCount = nList.getLength();
        for (int i = 0; i < nodesCount; i++) {
            executor.execute(
                    processItem(nList.item(i))
            );
        }

        blockMethodForThread(nodesCount);
    }

    private void blockMethodForThread(int nodesCount){
        while (objectsParsed.get() < nodesCount) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.warn("Thread sleep error",e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private Runnable processItem(org.w3c.dom.Node item) {
        return () -> {

            switch (item.getNodeName()) {
                case NODE:
                    putNodeToMap(item);
                    break;
                case WAY:
                    createWayWithNodes(item);
                    break;
                default:
                    break;
            }
            objectsParsed.getAndIncrement();
        };
    }

    private void putNodeToMap(Node item) {
        Element eElement = (Element) item;
        NamedNodeMap attrs = eElement.getAttributes();
        long id = Long.parseLong(attrs.getNamedItem("id").getTextContent());
        Coordinates coords = getCoordinatesFromElement(eElement,"lat","lon");
        nodes.put(id, new OsmNode( coords));
    }

    private void createWayWithNodes(Node item) {
        Element eElement = (Element) item;
        NodeList nList = eElement.getElementsByTagName("tag");

        for (int i = 0; i < nList.getLength(); i++) {
            String k = nList.item(i).getAttributes().getNamedItem("k").getTextContent();
            String v = nList.item(i).getAttributes().getNamedItem("v").getTextContent();
            if(isHighway(k,v)){
                addWay(eElement, v);
                break;
            }
        }
    }

    private boolean isHighway(String key, String value) {
        return "highway".equals(key) && !"footway".equals(value);
    }

    private List<OsmNode> getWayNodes(NodeList nodesRefs){
        List<OsmNode> waysNodes = new ArrayList<>();
        for (int i = 0; i < nodesRefs.getLength(); i++) {
            OsmNode node = nodes.get(Long.parseLong(((Element) nodesRefs.item(i)).getAttribute(WAY_NODE_ATRIBUTE_ID)));
            if(node != null){
                node.iterateUses();
                if(isTowerNode(i, nodesRefs.getLength())){
                    node.iterateUses();
                }
                waysNodes.add(node);
            }
        }
        return waysNodes;
    }

    private boolean isTowerNode(int position, int nodesSize){
        return position == 0 || position == nodesSize-1;
    }

    private void addWay(Element element, String highwayType){
        NodeList nodesRefs = element.getElementsByTagName(WAY_NODE);
        List<OsmNode> waysNodes = getWayNodes(nodesRefs);
        ways.add(new OsmWay(waysNodes,highwayType));
    }

}

package util;

import api.settings.Settings;
import api.graph.Coordinates;
import api.graph.Edge;
import api.graph.Graph;
import api.graph.Node;
import junit.framework.TestCase;
import pathfinder.BlockEdgeFinder;
import pathfinder.quadtree.NodeQuadTree;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тестирование нахождения блокирующих точек
 */
public class BlockEdgeFinderTest extends TestCase {
    private NodeQuadTree tree;
    private Settings settings;
    private Edge edge;
    public void testBlockPoints(){

        prepareData();

        Graph graph = new Graph(null,tree);
        BlockEdgeFinder blocker = new BlockEdgeFinder(settings,graph);

        assertEquals(blocker.isBlockEdge(edge), true);
    }

    private void prepareData(){
        Coordinates first = new Coordinates(1,1);
        Coordinates second = new Coordinates(2,2);

        List<Coordinates> list = new ArrayList<>();
        list.add(first);

        Node firstNode = new Node(1,first);
        Node secondNode = new Node(2, second);
        List<Node> listNodes = new ArrayList<>();
        listNodes.add(firstNode);
        edge = new Edge(firstNode, secondNode,4,60);
        tree = mock(NodeQuadTree.class);
        when(tree.getClosestObjects(first)).thenReturn(listNodes);

        tree.add(firstNode);
        settings = mock(Settings.class);
        when(settings.getBlockPoints()).thenReturn(list);
    }

}

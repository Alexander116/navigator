package util;

import api.graph.Coordinates;
import junit.framework.TestCase;
import util.BlockPointParser;

import java.io.IOException;
import java.util.List;

/**
 * Тестирование парсера блокируемых точек
 */
public class BlockPointParserTest extends TestCase {

    private static final String FILE_NAME = "src/test/resources/points";
    public void testReadFile() throws IOException {
        List<Coordinates> coords = BlockPointParser.getBP(FILE_NAME);
        Coordinates firstPoint = coords.get(0);

        assertEquals(coords.size(),3);
        assertEquals(firstPoint.getLat(), 5.0);
    }
}

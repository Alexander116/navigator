package api;

import api.graph.Coordinates;
import junit.framework.TestCase;


public class CoordinatesTest extends TestCase {

    public void testParse(){
        String strCoordinate = "12.34 11.12";
        Coordinates coordinate = Coordinates.parseCoordinate(strCoordinate);
        assertEquals(coordinate.getLat(), 12.34);
        assertEquals(coordinate.getLng(), 11.12);
    }
}

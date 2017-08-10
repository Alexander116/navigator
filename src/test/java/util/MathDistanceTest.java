package util;

import api.graph.Coordinates;
import util.MathDistance;
import junit.framework.TestCase;


public class MathDistanceTest extends TestCase{


    /**
     * проверка рассчета расстояния по 2 точкам
     */
    public void testDistance(){

        Coordinates first = new Coordinates(55.721,52.26);
        Coordinates second = new Coordinates(55.839,49.068);
        double dist = MathDistance.getDistance(first,second);
        assertEquals(200074.343, dist);
    }

}

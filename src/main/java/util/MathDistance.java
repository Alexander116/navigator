package util;

import api.graph.Coordinates;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * нахождение расстояния между двумя точками
 */
public class MathDistance {

    private static final double EARTH_RADIUS = 6372795;

    private static final double DEGREE_TO_RADIAN = Math.PI / 180;

    /**
     * Рассчитывает растояние между двумя координатами
     * @param firstPoint первая координата
     * @param secondPoint вторая координата
     */
    public static double getDistance(Coordinates firstPoint, Coordinates secondPoint){

        double lat1 = toRadians(firstPoint.getLat());
        double lat2 = toRadians(secondPoint.getLat());
        double long1 =toRadians(firstPoint.getLng());
        double long2 =toRadians(secondPoint.getLng());

        double cl1 = Math.cos(lat1);
        double cl2 = Math.cos(lat2);
        double sl1 = Math.sin(lat1);
        double sl2 = Math.sin(lat2);
        double delta = long2 - long1;
        double cdelta = Math.cos(delta);
        double sdelta = Math.sin(delta);

        double y = Math.sqrt(Math.pow(cl2*sdelta,2)+Math.pow(cl1*sl2-sl1*cl2*cdelta,2));
        double x = sl1*sl2+cl1*cl2*cdelta;
        double ad = Math.atan2(y,x);

        return BigDecimal.valueOf( ad * EARTH_RADIUS).setScale(3, RoundingMode.HALF_UP).doubleValue();
    }

    private static double toRadians(double val){
        return val * DEGREE_TO_RADIAN;
    }
}

package pathfinder.quadtree;


import api.graph.Coordinates;

/**
 * Проверяет пересекает ли отрезок окружность
 */
public class CircleLine {
    private double centerX;
    private double centerY;

    public CircleLine(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * определяет есть ли пересечение отрезка и круга
     * @param segmentA первая точка отрезка
     * @param segmentB вторая точка отрезка
     * @param pointOnCircle точка лежащая на окружности
     * @return
     */
    public boolean circleBySegment(Coordinates segmentA, Coordinates segmentB, Coordinates pointOnCircle){
        double rQuad = Math.pow(pointOnCircle.getLng() - centerX, 2) + Math.pow(pointOnCircle.getLat()-centerY, 2);
        double segmentAX = segmentA.getLng();
        double segmentAY = segmentA.getLat();
        double segmentBX = segmentB.getLng();
        double segmentBY = segmentB.getLat();

        double circle = centerX * centerX  + centerY * centerY - rQuad;

        double lngDifference = segmentBX - segmentAX;
        double latDifference = segmentBY - segmentAY;

        double center = centerX * segmentAX + centerY * segmentAY;

        double a = Math.pow(lngDifference, 2) + Math.pow(latDifference, 2);
        double b = 2 * ((lngDifference) * (segmentAX - centerX) + (latDifference) * (segmentAY - centerY));
        double c = circle + Math.pow(segmentAX,2) + Math.pow(segmentAY,2) - 2 * (center) ;

        return getResult(a,b,c);
    }

    /**
     * определяет пересекается ли точка по параметрам
     * квадратного уровнения, с помощью теоремы Виетта
     * @param a
     * @param b
     * @param c
     * @return
     */
    private boolean getResult(double a, double b, double c){

        if ( - b < 0){
            return c < 0;
        }
        if ( - b < (2 * a)){
            return 4 * a * c - b * b < 0;
        }
        return a + b + c < 0;
    }
}


package api.graph;


public class Coordinates {
    private double lng;
    private double lat;

    public Coordinates(double lat, double lng){

        this.lat = lat;
        this.lng = lng;
    }

    public double getLat(){
        return lat;
    }

    public double getLng(){
        return lng;
    }

    public static Coordinates parseCoordinate(String str){
        String[] strCoords = str.split(" ");
        double lat = Double.parseDouble(strCoords[0]);
        double lng = Double.parseDouble(strCoords[1]);
        return new Coordinates(lat,lng);
    }
}

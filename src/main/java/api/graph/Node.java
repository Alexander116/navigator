package api.graph;

/**
 * вершина графа
 */
public class Node{
    private long id;
    private Coordinates coords;

    public Node(long id, Coordinates coords) {
        this.id = id;
        this.coords = coords;
    }

    public long getId(){
        return id;
    }

    public Coordinates getCoords() {
        return coords;
    }


    public void setId(long id) {
        this.id = id;
    }

}

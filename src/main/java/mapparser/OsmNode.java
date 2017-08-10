package mapparser;

import api.graph.Coordinates;
import api.graph.Node;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Предварительно обработанная точка на карте
 */
class OsmNode extends Node {
    /**
     * количество исп точки в осм
     */
    private AtomicInteger uses = new AtomicInteger(0);

    OsmNode( Coordinates coords) {
        super(0, coords);
    }

    /**
     * Есть ли у точки пересечения
     * @return
     */
    boolean isTower(){
        return uses.get() > 1;
    }

    void iterateUses(){
        uses.incrementAndGet();
    }

    AtomicInteger getUses() {
        return uses;
    }
    
}

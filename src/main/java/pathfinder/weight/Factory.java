package pathfinder.weight;

import api.settings.Settings;
import api.graph.Graph;

/**
 * Created by denisov_ae on 03.03.17.
 */
public class Factory {
    public static Weight create(Settings settings, Graph graph){
        String type = settings.getPathType();

        if(Settings.PATH_FASTEST.equals(type)){
            return new FastestPath();
        }else if(Settings.PATH_OPTIMAL.equals(type)){
            return new OptimalPath(settings, graph);
        }else{
            return new ShortestPath();
        }
    }
}

package mapparser;


import java.util.List;

/**
 * осм Путь,
 * промежуточный класс хранящий в себе информацию
 * для дальнейшего разбиения на ребра графа
 */
class OsmWay {
    private List<OsmNode> osmNodes;
    private String highwayType;
    OsmWay(List<OsmNode> osmNodes, String highwayType){
        this.osmNodes = osmNodes;
        this.highwayType = highwayType;
    }

    List<OsmNode> getNodes(){
        return osmNodes;
    }

    String getHighwayType() {
        return highwayType;
    }
}

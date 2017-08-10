package pathfinder.quadtree;

import api.graph.Coordinates;
import api.graph.Edge;
import api.graph.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static util.MathDistance.getDistance;

/**
 * Дерево квадрантов
 */
public abstract class AbstractQuadTree<T> {

    static final int MAX_LEVEL = 10;
    Coordinates search;
    CircleLine circleLine;

    private AbstractQuadTree<T> parentTree;
    private final Coordinates minCorner;
    private final Coordinates maxCorner;
    final int level;
    private int size = 0;

    List<T> objects = Collections.synchronizedList(new ArrayList<>());
    List<AbstractQuadTree<T>> quads = new ArrayList<>(4);

    AbstractQuadTree(Coordinates min, Coordinates max, int level, AbstractQuadTree<T> parentTree) {
        this.parentTree = parentTree;
        minCorner = min;
        maxCorner = max;
        this.level = level;
        buildTree();
    }

    /**
     * Возвращает объекты квадрата
     * @return
     */
    public List<T> getObjects() {
        return objects;
    }

    /**
     * Возвращает координаты левого нижнего угла квадрата
     * @return
     */
    public Coordinates getMinCorner() {
        return minCorner;
    }

    /**
     * количество элементов в дереве
     * @return
     */
    public int size(){
        return size;
    }

    /**
     * Возвращает координаты правого верхнего угла квадрата
     * @return
     */
    public Coordinates getMaxCorner() {
        return maxCorner;
    }

    /**
     * Возвращает дочерний квадрат дерева по его индексу
     * @param index
     * @return
     */
    public AbstractQuadTree<T> getQuad(int index){
        return quads.get(index);
    }

    private void buildTree(){

        if(level < MAX_LEVEL){
            double midHeight = (maxCorner.getLat() - minCorner.getLat()) / 2;
            double midWeight = (maxCorner.getLng() - minCorner.getLng()) / 2;

            double maxCornerHeight = maxCorner.getLat() - midHeight;
            double newMidWeight = maxCorner.getLng() - midWeight;
            int nextLevel = level + 1;

            Coordinates firstMin = new Coordinates(minCorner.getLat(), minCorner.getLng());
            Coordinates firstMax = new Coordinates(maxCornerHeight, newMidWeight);
            Coordinates secondMin = new Coordinates(maxCornerHeight, minCorner.getLng());
            Coordinates secondMax = new Coordinates(maxCorner.getLat(), newMidWeight);
            Coordinates thirdMin = new Coordinates(maxCornerHeight, newMidWeight);
            Coordinates thirdMax = new Coordinates(maxCorner.getLat(), maxCorner.getLng());
            Coordinates fourthMin = new Coordinates(minCorner.getLat(), newMidWeight);
            Coordinates fourthMax = new Coordinates(maxCornerHeight, maxCorner.getLng());

            AbstractQuadTree<T>  fist = initMe(nextLevel, firstMin, firstMax, this);
            AbstractQuadTree<T>  second =initMe(nextLevel, secondMin, secondMax, this);
            AbstractQuadTree<T>  third =initMe(nextLevel, thirdMin, thirdMax, this);
            AbstractQuadTree<T>  fourth = initMe(nextLevel, fourthMin, fourthMax, this);

            quads.add(fist);
            quads.add(second);
            quads.add(third);
            quads.add(fourth);
        }
    }

    /**
     * Добавление элемента в дерево
     * @param object
     */
    public void add(T object) {
        size++;
        int index = getQuadIndex(object);
        if(index != -1){
            quads.get(index).add(object);
        }else{
            objects.add(object);
        }
    }

    /**
     * Находит ближайший элемент
     * @param point
     * @return
     */
    public T getClosestObject(Coordinates point) {
        circleLine = new CircleLine(point.getLng(),point.getLat());
        search = point;
        return treeSearchCoordinate(this);
    }

    private int getQuadIndex(T object) {
        for(int i=0; i< quads.size(); i++){
            if(quads.get(i).isInQuad(getCoordinatesFromObject(object))){
                return i;
            }
        }
        return -1;
    }

    /**
     * Находит ближайшую точку
     *
     * @param tree
     * @return
     */
    private T treeSearchCoordinate(AbstractQuadTree<T> tree) {
        T res;
        AbstractQuadTree<T> last = getLastQuad(tree);
        res = searchClosest(last.objects ,search,9999);
        if(res == null){
            res = searchAny(last);
        }
        AbstractQuadTree<T> lastOutTree = upWhileOut(last, res);
        return searchNodeInRadius(lastOutTree, res);
    }

    /**
     * Возвращает последний квадрат в который попадает координата поиска
     * @param tree
     * @return
     */
    AbstractQuadTree<T> getLastQuad(AbstractQuadTree<T> tree) {
        int count = 0;
        while(tree.level != MAX_LEVEL && count < MAX_LEVEL){
            count++;
            for(AbstractQuadTree<T> quad : tree.quads){
                if(quad.isInQuad(search)){
                    tree = quad;
                    break;
                }
            }
        }
        return tree;
    }

    private T searchClosest(List<T> objects, Coordinates search, double beginFault) {
        T res = null;
        double minFault = beginFault;
        for (T node : objects) {
            double fault = getFault(getCoordinatesFromObject(node), search);
            if (minFault > fault) {
                res = node;
                minFault = fault;
            }
        }
        return res;
    }


    private T searchAny(AbstractQuadTree<T> last) {
        while(last.size == 0){
            last = last.parentTree;
        }
        while(last.level != MAX_LEVEL){
            for (AbstractQuadTree<T> quad: last.quads){
                if(quad.size > 0){
                    last = quad;
                    break;
                }
            }
        }
        return last.objects.get(0);
    }

    private boolean isInQuad(Coordinates point) {
        boolean isLngInRange = point.getLng() >= minCorner.getLng() && point.getLng() <= maxCorner.getLng();
        boolean isLatInRange = point.getLat() >= minCorner.getLat() && point.getLat() <= maxCorner.getLat();
        return isLngInRange && isLatInRange;
    }

    private double getFault(Coordinates first, Coordinates second) {
        double xFault = Math.abs(first.getLng() - second.getLng());
        double yFault = Math.abs(first.getLat() - second.getLat());
        return xFault + yFault;
    }

    AbstractQuadTree<T> upWhileOut(AbstractQuadTree<T> last, T node) {
        double diameter = getDistance(search, getCoordinatesFromObject(node)) * 2;
        while(last.parentTree != null && isQuadInRadius(last,node, diameter)){
            last = last.parentTree;
        }
        return last;
    }

    private T searchNodeInRadius(AbstractQuadTree<T> tree , T lastRes) {
        if(tree.level != MAX_LEVEL){
            for(AbstractQuadTree<T> quad : tree.quads){
                lastRes = searchNodeInRadius(quad, lastRes);
            }
        }else{
            double minFault = getFault(search, getCoordinatesFromObject(lastRes));
            T res = searchClosest(tree.objects, search, minFault);
            lastRes = (res != null)? res : lastRes;
        }
        return lastRes;
    }

    /**
     * пересекает ли окружность квадрат
     * @param tree квадро-дерево
     * @param res точка на окружности для вычисления радиуса
     * @return
     */
    private boolean isSegmentIntersectQuad(AbstractQuadTree<T> tree, T res) {

        Coordinates leftDown = tree.getMinCorner();
        Coordinates rightUp = tree.getMaxCorner();
        Coordinates leftUp = new Coordinates(rightUp.getLat(), leftDown.getLng());
        Coordinates rightDown = new Coordinates(leftDown.getLat(), rightUp.getLng());
        Coordinates objCoord = getCoordinatesFromObject(res);

        boolean left = circleLine.circleBySegment(leftDown, leftUp, objCoord);
        boolean top = circleLine.circleBySegment(leftUp, rightUp, objCoord);
        boolean bottom = circleLine.circleBySegment(leftDown, rightDown, objCoord);
        boolean right = circleLine.circleBySegment(rightDown, rightUp, objCoord);

        return left || top || bottom || right;
    }

    abstract AbstractQuadTree<T> initMe(int nextLevel, Coordinates coordinates, Coordinates coordinates1, AbstractQuadTree<T> parentTree);

    /**
     * возвращает true если окружность внутри квадрата или пересекает его
     * @param last квадро-дерево
     * @param node точка на окружности для вычисления радиуса
     * @param diameter диаметр окружности
     * @return
     */
    boolean isQuadInRadius(AbstractQuadTree<T> last, T node, double diameter){
        Coordinates quadPoint = new Coordinates(last.getMinCorner().getLat(),last.getMaxCorner().getLng());
        double length = getDistance(last.getMinCorner(), quadPoint);
        boolean isInCircle = isSegmentIntersectQuad(last, node);
        return diameter >= length || isInCircle ;
    }

    private Coordinates getCoordinatesFromObject(Object object) {
        String s = object.getClass().getName();
        if (s.contains("Edge")) {
            return ((Edge) object).getFirstNode().getCoords();
        } else {
            return ((Node) object).getCoords();
        }
    }
}

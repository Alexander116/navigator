package api.path;

/**
 * отвечает за поиск различных путей по графу
 */
public interface PathFinder {
    /**
     * @return возвращает маршрут от точки до точки
     */
    Route getRoute( );

}

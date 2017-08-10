package api.settings;

import api.GraphException;
import api.graph.Coordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.BlockPointParser;

import java.io.FileInputStream;
import java.util.*;

import static api.settings.ConfigParam.*;

/**
 * Хранит клиентские настройки
 */
public class Settings {

    private static final int DEFAULT_SPEED = 60;
    private static final double DEFAULT_COEFFICIENT = 0.0;
    private static final String DEFAULT_MAP_PATH = "map.osm";
    private static final String DEFAULT_BLOCK_POINTS = "points";
    private static final String DEFAULT_SPEED_MAP = "speed";

    public static final String PATH_OPTIMAL ="Optimal";
    public static final String PATH_SHORTEST ="Shortest";
    public static final String PATH_FASTEST ="Fastest";

    private static final String CONFIG_NAME = "MapParser.properties";

    /**
     * Тип вывода маршрута с дистанцией и временем
     */
    public static final String ROUTE_OUT_TYPE_SIMPLE = "simple";

    /**
     * Тип вывода маршрута с дистанцией, временем и массивом точек
     */
    public static final String ROUTE_OUT_TYPE_ARRAY = "array";

    private static Logger logger = LoggerFactory.getLogger(Settings.class);
    private SafeProperties speedMap;
    private SafeProperties config;

    /**
     * Устанавливает настройки по умолчанию
     */
    public Settings() {
        setConfig();
        setSpeedMap();
    }

    public List<Coordinates> getBlockPoints(){
        return BlockPointParser.getBP(config.getString(BLOCK_POINT_PATH.toString(), DEFAULT_BLOCK_POINTS));
    }

    /**
     * Возвращает скорость по ключу
     * @param key
     * @return
     */
    public int getSpeedFromMap(String key){
        return speedMap.getInt( key, DEFAULT_SPEED);
    }

    public void setPathType(String type){
        config.put(PATH_TYPE.toString(), type);
    }

    /**
     * коэфициент для оптимального пути
     * @return
     */
    public double getOptimalCoefficient(){
        return config.getDouble(OPTIMAL_COEFFICIENT.toString(), DEFAULT_COEFFICIENT);
    }

    /**
     * Возвращает тип искомого пути
     * @return
     */
    public String getPathType(){
        return config.getString(PATH_TYPE.toString(), PATH_SHORTEST);
    }

    /**
     * Возвращает тип вывода маршрута
     * @return
     */
    public String getRouteOutType(){
        return config.getString(ROUTE_OUT_TYPE.toString(), ROUTE_OUT_TYPE_SIMPLE);
    }

    /**
     * путь к карте
     * @return
     */
    public String getMapPath(){
        return config.getString(MAP_PATH.toString(), DEFAULT_MAP_PATH);
    }

    /**
     * Ищет конфиг и записывает его настройки
     */
    private void setConfig() {
        try {
            String path = findConfigPath();
            config = new SafeProperties(getProperties(path));
        } catch (Exception e) {
            logger.warn("set api.settings from config error.", e);
        }
    }

    private String findConfigPath() {
        Map<String, String> env = System.getenv();
        String settingsFilePath = env.get("MAP_PARSER_CONFIG");
        if (settingsFilePath == null) {
            settingsFilePath = env.get("HOME") +"/"+ CONFIG_NAME;
        }
        return settingsFilePath;
    }

    private void setSpeedMap() {
        try{
            Properties prop = getProperties(config.getString(SPEED_MAP_PATH.toString(), DEFAULT_SPEED_MAP));
            speedMap = new SafeProperties(prop);
        }catch(Exception e){
            logger.warn("speed map don`t read.",e);
        }
    }

    private Properties getProperties(String filePath) throws GraphException {
        try (
                FileInputStream fileStream = new FileInputStream(filePath);
        ) {
            Properties properties = new Properties();
            properties.load(fileStream);
            return properties;
        } catch (Exception e) {
            throw new GraphException("get properties error.", e);
        }
    }
}

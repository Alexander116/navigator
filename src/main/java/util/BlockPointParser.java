package util;

import api.graph.Coordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * парсинг точек из файла в список координат
 */
public class BlockPointParser {
    private static Logger logger = LoggerFactory.getLogger(BlockPointParser.class);

    public static List<Coordinates> getBP(String filePath){
        try {
            return getFileLines(filePath)
                    .map(Coordinates::parseCoordinate)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.warn("Block points parse error.\n",e);
        }

        return new ArrayList<>();
    }

    private static Stream<String> getFileLines(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath), StandardCharsets.UTF_8);
    }
}

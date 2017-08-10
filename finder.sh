MAP_DIR='map.pbf'
JAR_PATH='IdeaProjects/GraphParser/target/GraphParser-1.0-SNAPSHOT.jar'
POLY='work/maps/chelny_n.poly'
GRAPH_CONFIG=$HOME'/MapParser.properties'
CONFIG=./params.conf
if [ -f ${CONFIG} ];
then		 
	. ${CONFIG}
	if [ ! -z $map ];
	then
		MAP_DIR=${map}
	fi

	if [ ! -z ${jar_path} ];
	then
		JAR_PATH=${jar_path}
	fi

	if [ ! -z ${poly} ];
	then
		POLY=${poly}
	fi
	if [ ! -z ${graph_config} ];
	then
		GRAPH_CONFIG=${graph_config}
	fi
fi

run() {
	osmconvert ${MAP_DIR} -B=${POLY} -o=${MAP_DIR}".osm"
	MAP_PARSER_CONFIG=$GRAPH_CONFIG java -cp ${JAR_PATH} Launcher
}

download() {
	wget -O ${MAP_DIR} "http://data.gis-lab.info/osm_dump/dump/latest/RU-TA.osm.pbf"
}
read_params() {
	shift 1
	while getopts ":m:j:p:c:d" opt; do
	  case $opt in
		m)
		  	if [ ! -z $OPTARG ]; then
				MAP_DIR=$OPTARG
			fi
		  ;;
		j)
		  	if [ ! -z $OPTARG ]; then
				JAR_PATH=$OPTARG
			fi
		  ;;
		p)
		  	if [ ! -z $OPTARG ]; then
				POLY=$OPTARG
			fi
		  ;;
		c)
			if [ ! -z $OPTARG ]; then
		        GRAPH_CONFIG=$OPTARG
	        fi
		;;
		d)
	       download
	    ;;

		*)
		  echo "wrong flag"
		  ;;
	  esac
	done
}

usage() {
	echo "Usage: $(basename "$0") {run} [params]"
	echo "  -d если указан данный параметр перед запуском будет скачана карта"
	echo "  -m путь для сохранения карты (по умолч. текущая дериктория), сохраняется с названием map.pbf конвертируется в map.osm"
	echo "  -j путь к .jar файлу GraphParser"
	echo "  -p Путь к .poly файлу"
	echo "  -c Путь к конфиг файлу GraphParser"
	echo "Для заполнения данных параметров можно также использовать конфиг файл(params.conf)."
	echo "Параметры конфигурационного файла:"
	echo "  map - путь для сохранения карты (по умолч. текущая дериктория)"
	echo "  jar_path - путь к .jar файлу GraphParser"
	echo "  poly - путь к .poly файлу"
	echo "  graph_config - путь к конфиг файлу GraphParser(по умолчанию $HOME/MapParser.properties)"
}

CMD=$1
case $CMD in

	"run")
        read_params $@;
		run
		;;
	*)
		usage
		;;
esac
exit 0
#!/bin/sh
NAME="graph-parser"
GH_HOME=$(readlink -e "$(dirname "$0")"/..)
CONF=$GH_HOME/conf/MapParser.properties
JAR="$GH_HOME/$NAME.jar"
JAVA_CMD=$(which java)

MAP_PARSER_CONFIG=$CONF $JAVA_CMD -cp $JAR Launcher

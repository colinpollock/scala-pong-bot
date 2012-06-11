#!/bin/bash
echo "" > .classpath.txt
for file in `ls target`;
        do echo -n 'target/' >> .classpath.txt;
        echo -n $file >> .classpath.txt;
        echo -n ':' >> .classpath.txt;
done
for file in `ls target/lib`;
	do echo -n 'target/lib/' >> .classpath.txt;
	echo -n $file >> .classpath.txt;
	echo -n ':' >> .classpath.txt;
done
export CLASSPATH=$(cat .classpath.txt)

export JAVA_CONFIG_OPTIONS="-Xms1000m -Xmx4g -XX:NewSize=256m -XX:MaxNewSize=256m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:PermSize=256m -XX:MaxPermSize=128m"

export JAVA_OPTS="$JAVA_OPTS $JAVA_CONFIG_OPTIONS"

scala -cp $CLASSPATH -DJAVA_OPTS="$JAVA_OPTS" -DloggerPath=conf/log4j.properties "$@"

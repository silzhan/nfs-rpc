java -Xms2g -Xmx2g -Xmn500m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:gc.log -Djava.ext.dirs=$1 $2 [listenport] [maxthreads] [responsesize] > $3 2>&1 &

# Package #

  * download source code from svn;
  * execute mvn clean install assembly:assembly;
  * then u can find nfs-rpc-**.tar.gz & nfs-rpc-**.zip in target directory.

# Server #

  * u need change servercommon.sh(u can find it in nfs-rpc-**/bin/server/),to set listenPort,maxThreads & responseSize;
  * then u just need to execute minaserver.sh | nettyserver.sh | grizzlyserver.sh;**

# Client #

  * u need change clientcommon.sh(u can find it in nfs-rpc-
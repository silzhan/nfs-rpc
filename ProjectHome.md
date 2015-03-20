# Background #
Java app always need to call another java app,now we can use rmi/hessian etc. to realize,but they're not fast enough,so many of us select mina/netty or other high performance network framework to realize,every time when we change io framework,we need realize the rpc framework again,it's so boring....
# Features #
## Current ##
This project abstract the high performance rpc framework,provide some basic implemenation:
  * client proxy,so u can call remote just like call local: UserService.getById(id);
  * server reflection,so u can intergrate the rpc framework with difference server implemenation,such as spring;
  * server direct call,u can implement a specify interface to provide for client call;
  * single or multi connections,in most case,single connection is enough,but in some cases,u'll need multi connections;
  * connection reuse,so u don't need block user thread to wait for connection;
  * synchronize call,rpc always need synchronize call;
  * timeout,u cann't wait for server return response forever,so u need timeout policy,especially for online app;
  * multi or custom serialize protocol(java/hessian/protobuf/kyro);
  * custom your protocol.
With above features,u can implement your high performance rpc framework quickly with your selected network framework.
And the projects also provide mina/netty/grizzly implemenation,so u can directly use it to build your business,u can [see here](http://code.google.com/p/nfs-rpc/wiki/UserGuide) to find how to use it.
## Future ##
    1. intergrate more network frameworks,such as thrift/avro;
    1. intergrate with aio;
    1. test coroutine version,maybe scala or kilim;
    1. server can use different threadpools to handle requests;
    1. support async call & group calls;
    1. add connection protect,avoid create connection again and again;
# QuickStart #
## For Users ##
  * if u just want to use current mina/netty/grizzly/aio framework,pls [see here](http://code.google.com/p/nfs-rpc/wiki/UserGuide).
## For Developers ##
  * if u want to custom serialize/deserialize,custom protocol,implement with other io framework or beat the benchmark record,pls [see here](http://code.google.com/p/nfs-rpc/wiki/DeveloperGuide);
# Benchmark #
**Current benchmark record (single connection,100 concurrents,100 byte request,100 byte response) created by**<h1>grizzly-rpc with protobuf</h1>tps is <h1>168k</h1>**pls [see here](http://code.google.com/p/nfs-rpc/wiki/HowToRunBenchmark) to know how to run the benchmark.
## let's beat the rpc benchmark record together! ##
we hope someone can help us to improve performance or implement your own rpc framework with your favorite io framework to beat the performance record.
Benchmark Machine:
    * CPU: E5620 (8 core HT 16 core)
    * Memory: 24G
    * Network: 1000Mb
Current benchmark results:
    * 100 concurrents,Single connection,Direct Call
> > ![![](http://bluedavy.me/projects/nfs-rpc/benchmark-dc-vs.png)](http://bluedavy.me/projects/nfs-rpc/benchmark-dc-vs.png)
    * 100 concurrents,Single connection,Reflection Call
> > ![![](http://bluedavy.me/projects/nfs-rpc/benchmark-rc-vs.png)](http://bluedavy.me/projects/nfs-rpc/benchmark-rc-vs.png)
    * Different Concurrents,Direct Call
> > ![![](http://bluedavy.me/projects/nfs-rpc/benchmark-concurrents-dc.png)](http://bluedavy.me/projects/nfs-rpc/benchmark-concurrents-dc.png)
    * Different Concurrents,Reflection Call
> > ![![](http://bluedavy.me/projects/nfs-rpc/benchmark-concurrents-rc.png)](http://bluedavy.me/projects/nfs-rpc/benchmark-concurrents-rc.png)
    * Different connections
> > ![![](http://bluedavy.me/projects/nfs-rpc/benchmark-connections.png)](http://bluedavy.me/projects/nfs-rpc/benchmark-connections.png)
> >**<br>More benchmark details pls download from <a href='http://bluedavy.me/projects/nfs-rpc/benchmark-1.xslx'>here</a>.
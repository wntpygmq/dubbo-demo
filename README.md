dubbo使用及源码解析
用户文档


#1 配置

##1.1 配置
provider:application,registry ,protocol,bean,dubbo:service
consumer:application,registry,dubbo:reference

##1.2 配置优先级，不同粒度配置的覆盖关系

以 timeout 为例，下图显示了配置的查找顺序，其它 retries, loadbalance, actives 等类似：
方法级优先，接口级次之，全局配置再次之。
如果级别一样，则消费方优先，提供方次之。
其中，服务提供方配置，通过 URL 经由注册中心传递给消费方。


越细越强，消费最强。


##1.3 属性配置

dubbo可以自动加载classpath下的dubbo.properties
例如：
dubbo.application.name=foo
dubbo.registry.address=10.20.153.10:9090

读取配置优先级：

注意：如果在classpath下有超过一个dubbo.properties文件，比如，两个jar包都各自包含了dubbo.properties，dubbo将随机选择一个加载，并且打印错误日志。

##1.4 JavaApi配置

##1.5 properties配置



#2 示例

##2.1 check
关闭单个服务检查

关闭所有服务检查
<dubbo:consumer check="false" />
关闭注册中心检查
<dubbo:registry check="false" />

##2.2 容错
<dubbo:service interface="com.alibaba.dubbo.demo.DemoService" ref="demoService" delay="-1" cluster="failover" retries="2"/>
失败重试，不含第一次，配置2那么就请求3次。
<dubbo:service retries="2" />

FailoverClusterInvoker的doInvoke()方法中实现


##2.3 负载均衡

Random ：权重随机
RoundRobin：权重轮询
LeastActive：最少活跃数，请求一次活动数+1，请求完成活跃数-1，一段时间后，服务活跃数越少，说明该服务处理请求效率最高。
ConsistentHash：减少服务实例的变化导致的请求的变化，增加虚拟节点可以使请求分布更均匀。
四种设置方式：
<dubbo:service interface="..." loadbalance="roundrobin" />
<dubbo:reference interface="..." loadbalance="roundrobin" />
<dubbo:service interface="...">
    <dubbo:method name="..." loadbalance="roundrobin"/>
</dubbo:service>
<dubbo:reference interface="...">
    <dubbo:method name="..." loadbalance="roundrobin"/>
</dubbo:reference>


##2.4 线程模型

<dubbo:protocol name="dubbo" dispatcher="all" threadpool="fixed" threads="100" />

事件类型：请求，响应，连接事件，断开事件，心跳等。
线程模型：IO线程、线程池

原理是通过spi加载不同的线程派发策略，不同的线程派发策略创建不同的handler处理链。

线程派发器Dispatcher：

all:所有事件都派发到线程池，AllChannelHandler
direct:所有线程都在io线程上执行,没有添加handler
message:只有请求响应消息派发到线程池，MessageOnlyChannelHandler
execution:只有请求消息派发到线程池,ExecutionChannelHandler
connection:在 IO 线程上，创建一个只有一个线程的线程池，将连接断开事件放入队列，有序逐个执行，其它消息派发到线程池,ConnectionOrderedChannelHandler


spi加载dispatcher






线程池：fixed,cached,limited,eager,通过spi加载
fixed:固定大小线程池，启动时建立线程，不关闭，一直持有。(缺省)
cached:缓存线程池，空闲一分钟自动删除，需要时重建。
limited:可伸缩线程池，但池中的线程数只会增长不会收缩。只增长不收缩的目的是为了避免收缩时突然来了大流量引起的性能问题。
eager:优先创建Worker线程池。在任务数量大于corePoolSize但是小于maximumPoolSize时，优先创建Worker来处理任务。当任务数量大于maximumPoolSize时，将任务放入阻塞队列中。阻塞队列充满时抛出RejectedExecutionException。(相比于cached:cached在任务数量超过maximumPoolSize时直接抛出异常而不是将任务放入阻塞队列)

<dubbo:protocol name="dubbo" dispatcher="all" threadpool="fixed" threads="100" />

##2.5 直连提供者

<dubbo:reference id="xxxService" interface="com.alibaba.xxx.XxxService" url="dubbo://localhost:20890" />


##2.6 只订阅，不注册

ServiceConfigdoExportUrls()中
List<URL> registryURLs = loadRegistries(true);//加载注册中心链接

用于开发阶段，在一个注册中心下，开发的服务暂不能提供服务，但需要依赖别的服务,所以需要禁用注册。
<dubbo:registry address="10.20.153.10:9090" register="false" />
or
<dubbo:registry address="10.20.153.10:9090?register=false" />

##2.7 不订阅，只注册
不订阅和不注册，服务都是可以调用的。

##2.8 静态服务
不由注册中心来控制服务的上线和断开，需要手动剔除
<dubbo:registry address="10.20.141.150:9090" dynamic="false" />
or
<dubbo:registry address="10.20.141.150:9090?dynamic=false" />

可以通过Registry将第三方服务手动写入注册中心

##2.9 多协议
不同服务不同协议，不同服务在性能上适用不同协议进行传输，比如大数据用短连接协议，小数据大并发用长连接协议
多协议暴露服务，一个服务使用多种协议暴露，需要与 http 客户端互操作

##2.10 多注册中心
一个服务向多个注册中心注册。
不同服务使用不同的注册中心。
一个消费者向多个注册中心订阅服务。

##2.11 服务分组
当一个接口有多种实现时，可以用 group 区分,任意用*。group="*"

##2.12 多版本

兼容新老版本，可以用version区分，任意用*。version="*"

##2.13 分组聚合

将一个接口的多个实现结果聚合到一起返回。只能聚合集合类型的返回结果。
接口实现类1的结果[aa,bb]和接口实现类2返回的结果[11,22]组合到一起[aa,bb,11,22]。


##2.14 结果缓存
<dubbo:reference interface="com.foo.BarService" cache="lru" />
在XXClusterInvoker到DubboInvoker.invoker()方法的filter链中有个CacheFilter.invoke()

默认是lru

三种cache

lru的实现是LinkedHashMap



##2.15 范化引用和范化实现

通过GenericImplFilter.invoe()实现。

1.范化引用
客户端可以没有依赖api的情况下，通过GenericService来获取代理实例，$invoker()传入方法名，参数类型和参数值。
pojo类型的参数可以通过map进行组装，返回pojo结果也会自动转成map。

<dubbo:reference generic="true">



##2.16范化实现
服务提供端没有api依赖的情况下，通过编写GenericService的实现类。
public class MyGenericService implements GenericService {
 
    public Object $invoke(String methodName, String[] parameterTypes, Object[] args) throws GenericException {
        if ("sayHello".equals(methodName)) {
            return "Welcome " + args[0];
        }
    }
}



##2.17 上下文信息
注意：一个服务同时配置成消息提供者和消息服务者，dubbo:application、dubbo:monitor等配置只能有一个

上下文中存放的是当前调用过程中所需的环境信息。所有配置信息都将转换为 URL 的参数。


RpcContext 是一个 ThreadLocal 的临时状态记录器，当接收到 RPC 请求，或发起 RPC 请求时，RpcContext 的状态都会变化。比如：A 调 B，B 再调 C，则 B 机器上，在 B 调 C 之前，RpcContext 记录的是 A 调 B 的信息，在 B 调 C 之后，RpcContext 记录的是 B 调 C 的信息。




##2.18 隐式参数
在完成下面一次远程调用会被清空，即多次远程调用要多次设置。
消费方发送
RpcContext.getContext().setAttachment("index", "1")
提供方获取
RpcContext.getContext().getAttachment("index"); 


##2.19 consumer异步调用


##2.20 provider异步执行
1.Provider端异步执行将阻塞的业务从Dubbo内部线程池切换到业务自定义线程，避免Dubbo线程池的过度占用，有助于避免不同服务间的互相影响。异步执行无益于节省资源或提升RPC响应性能，因为如果业务执行需要阻塞，则始终还是要有线程来负责执行。
2.CompletableFuture或AsyncContext
3.provider和consumer的异步是独立的。

##2.21 本地调用
如果Consumer与Provider部署在一台主机上，共用一个JVM，那么当Consumer调用Provider时就没有必要经过网络栈，直接调用即可。
本地调用使用了 injvm 协议，是一个伪协议，它不开启端口，不发起远程调用，只在 JVM 内直接关联，但执行 Dubbo 的 Filter 链。
<dubbo:protocol name="injvm" /> 可以不设置，自动本地调用，可设置强制本地和强制远程。

##2.22 参数回调
Dubbo 将基于长连接生成反向代理，Consumer端临时允当Provider端，Provider端调用的接口，真正的逻辑将在Consumer端执行。本来是消费端调用服务端，现在变成服务端推送给消费端了。

客户端通过方法参数形式向服务端传递listener实例，服务提供端调用该listener的方法执行客户端逻辑。


##2.23 事件通知
callback 与 async 功能正交分解，async=true 表示结果是否马上返回，onreturn 表示是否需要回调。
异步回调模式：async=true onreturn="xxx"，立即返回null，有结果触发回调方法
同步回调模式：async=false onreturn="xxx"，立即返回结果，触发回调方法
异步无回调 ：async=true
同步无回调 ：async=false

##2.24 本地存根
消费端使用服务返回结果后，不仅可以拿到接口，还可以通过本地代理，拿到服务端实例，访问ThreadLocal等。

##2.25 延迟暴露
<dubbo:service delay="5000" />

##2.26 并发控制
限制服务端或客户端每个方法或者指定方法的并发执行数量。

##2.27 连接控制
限制服务接收连接数或客户端使用连接数



#3 源码





##spi

什么是spi:是一种将服务接口与服务实现分离以达到解耦、大大提升了程序可扩展性的机制，通过配置文件加载具体的实现类。
比如数据库驱动加载接口实现类的加载，JDBC加载不同类型数据库的驱动。spi破坏了双亲委派机制。


dubbo spi做了哪些改进：
1.对象缓存，缓存加载器对象、class对象和各种实例，性能更好。
2.支持IOC，和静态代理实现AOP。
3.可以通过名称指定具体的实现类，或者spi注解指定具体实现类,java spi只能遍历所有的实现类。
4.自适应扩展机制，可以延迟加载指定的实现类，通过运行时参数指定类加载。
通过注解生成自适应代码，自适应代码中包含url解析和spi加载，然后使用javassist编译成自适应的代理对象。在调用代理对象的方法时，通过url参数和spi机制加载具体实现类，这样就将实现类的加载延迟到了方法调用的时候。



##服务导出

JavassistProxyFactory创建代理对象
ZookeeperFactory创建zkClient启动，并根据url创建节点
DubboProtocol创建nettyServer,启动绑定host和port


##服务引入

RegisterProtocol会创建一个服务字典RegistryDirectory,服务字典获取注册中心的节点，然后通过dubboProtocol维护服务到本地。
并且该类还实现了NotifyListener,如果服务端更新了，会触发刷新服务列表。



##服务字典




##集群容错

FailoverClusterInvoker：失效转移，invoker失败会重试
FailbackClusterInvoker：自动恢复，调用失败直接返回空结果，然后记录下来，定时任务调用。返回结果不可用，用于消息通知。
FailfastClusterInvoker：快速失败，只会进行一次调用，失败后立即抛出异常,常用于幂等性操作
FailsafeClusterInvoker：失败安全，只记录日志，不抛出异常
ForkingClusterInvoker：同时开启多个线程（默认为2）调用多个服务，只要有一个返回结果，完成调用。常用与实时性比较高的读操作，比较	 耗资源
BroadcastClusterInvoker：逐个调用所有服务提供者，如果有一个异常，则最后会抛出异常，通常用于通知所有提供者更新缓存或日志等本地资源信息


##负载均衡




##调用过程

发送请求
proxy0#sayHello(String)
  —> InvokerInvocationHandler#invoke(Object, Method, Object[])
    —> MockClusterInvoker#invoke(Invocation)
      —> AbstractClusterInvoker#invoke(Invocation)
        —> FailoverClusterInvoker#doInvoke(Invocation, List<Invoker<T>>, LoadBalance)
          —> Filter#invoke(Invoker, Invocation)  // 包含多个 Filter 调用
            —> ListenerInvokerWrapper#invoke(Invocation) 
              —> AbstractInvoker#invoke(Invocation) 
                —> DubboInvoker#doInvoke(Invocation)
                  —> ReferenceCountExchangeClient#request(Object, int)
                    —> HeaderExchangeClient#request(Object, int)
                      —> HeaderExchangeChannel#request(Object, int)
                        —> AbstractPeer#send(Object)
                          —> AbstractClient#send(Object, boolean)
                            —> NettyChannel#send(Object, boolean)
                              —> NioClientSocketChannel#write(Object)


调用服务
NettyServerHandler#channelRead(ChannelHandlerContext, Msg)
  —> AbstractPeer#received(Channel, Object)
    —> MultiMessageHandler#received(Channel, Object)
      —> HeartbeatHandler#received(Channel, Object)
        —> AllChannelHandler#received(Channel, Object)
          —> ExecutorService#execute(Runnable)    // 由线程池执行后续的调用逻辑

调用方法
ChannelEventRunnable#run()
  —> DecodeHandler#received(Channel, Object)
    —> HeaderExchangeHandler#received(Channel, Object)
      —> HeaderExchangeHandler#handleRequest(ExchangeChannel, Request)
        —> DubboProtocol.requestHandler#reply(ExchangeChannel, Object)
          —> Filter#invoke(Invoker, Invocation)
            —> AbstractProxyInvoker#invoke(Invocation)
              —> Wrapper0#invokeMethod(Object, String, Class[], Object[])
                —> DemoServiceImpl#sayHello(String)




##Filter链

创建filter链

调用filter链

在协议（除RegistryProtocol）的export()或者refer()返回的invoker实例后，会经过协议包装类ProtocolFilterWrapper的buildInvokerChain方法，
该方法通过spi加载filter列表，返回带有filter链的invoker。在调用invoker的invoke方法时，会触发filter链中各个filter的invoke方法，进行处理。
包括缓存CacheFilter,范化GenericImplFilter等。

echo=com.alibaba.dubbo.rpc.filter.EchoFilter
generic=com.alibaba.dubbo.rpc.filter.GenericFilter
genericimpl=com.alibaba.dubbo.rpc.filter.GenericImplFilter
token=com.alibaba.dubbo.rpc.filter.TokenFilter
accesslog=com.alibaba.dubbo.rpc.filter.AccessLogFilter
activelimit=com.alibaba.dubbo.rpc.filter.ActiveLimitFilter
classloader=com.alibaba.dubbo.rpc.filter.ClassLoaderFilter
context=com.alibaba.dubbo.rpc.filter.ContextFilter
consumercontext=com.alibaba.dubbo.rpc.filter.ConsumerContextFilter
exception=com.alibaba.dubbo.rpc.filter.ExceptionFilter
executelimit=com.alibaba.dubbo.rpc.filter.ExecuteLimitFilter
deprecated=com.alibaba.dubbo.rpc.filter.DeprecatedFilter
compatible=com.alibaba.dubbo.rpc.filter.CompatibleFilter
timeout=com.alibaba.dubbo.rpc.filter.TimeoutFilter



##Tips
1.范化服务接口，泛接口调用方式主要用于客户端没有API接口及模型类元的情况，参数及返回值中的所有POJO均用Map表示。
2.zk的java客户端有zkClient,Curator,dubbo使用的是curator
3.netty的handler处理









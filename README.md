# webServer
这个是学习手写tomcat创建的一个小仓库
 
# 读取web.xml步骤:
   (1)导入依赖
       <dependency>
           <groupId>org.dom4j</groupId>
           <artifactId>dom4j</artifactId>
           <version>2.1.1</version>
       </dependency>
   
   (2)调用方法
    //实例化SAXReader对象
    SAXReader reader = new SAXReader();
    //获取文档对象
    Document doc = reader.read("./config/web.xml");
    //获取根元素，也是根标签
    Element rootElement = doc.getRootElement();
    //根据根标签获取元素
    List<Element> elements = rootElement.elements("mime-mapping");
    //解析元素
    e.elementText("extension");


#总结

一开始，我们通过scoket监听获取到浏览器请求的内容，
然后，对请求内容进行解析，解析过程中，xml的读取用到dom4j
将请求行，请求头，请求体解析

#请求行解析
通过socket的 getInputStream 获取输入流，并且将输入流里面的东西解析输出

#请求头解析
通过socket的 getInputStream 获取输入流，并且将输入流里面的东西解析输出,将解析的东西
通过map保存，暴露一个getHeader方法，通过key获取value

#请求体解析
是否有请求体看 content-length, 如果是post请求，content-type是"application/x-www-form-urlencoded"
通过socket的 getInputStream 获取输入流，并且将输入流里面的东西解析输出,将解析的东西

#响应解析
通过socket.getOutputStream(); 然后调用write方法回写信息给浏览器
#响应行
在响应对象里面，有响应 HTTP/1.1 200 OK 协议 状态码 状态
一般设置默认200,ok, 设置响应码状态码、状态getter setter方法
#响应头
响应头解析包括content-length和 content-type(这个通过响应的文件内容获取后缀，
然后，通过dom4j读取xml文件，放在map里面（key为文件类型，value就是content-type),暴露getType方法，获取)
#相应体
通过file响应给浏览器,获取目录下面的file，通过 socket.getOutputStream() 回写给浏览器
例如:html,jsp等东西

#对于普通接口，比如登录，注册接口,
1.将包路径classpath还有 请求路径name配置到xml中，通过dom4j读取xml,然后
,对读取到的classpath通过反射获取实例对象，key为请求路径，value为实例对象，
保存到map集合里面
2.经过上面操作，我们就可以通过key获取value了，也就是通过路径，得到处理它的业务对象
3.一般定义一个HttpSerlvet抽象类,里面提供一个共同处理业务的方法，参数就是request类
还有response类，这些类里面的属性都被我们封装好了，通过这样就能处理获取请求内容并且
响应内容给浏览器了
4.对于不同路径对于不同的实例对象，这样我们就可以处理不同的业务了，这个也是javaweb的原理




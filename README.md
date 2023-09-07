# insecticide
全链路监控：查分布式服务的bug，专用insecticide（杀虫剂）

对应web平台dashboard：https://github.com/useraddZYB/insecticide-web

```
一，链路查看及admin控制台：  

http://127.0.0.1:8089/insect/admin/systemindex  

登录（修改及删除权限需要，别的权限默认都有）：  
http://....../insect/admin/login?user=&password=  
```

```
二，集成（java项目集成按如下4步走）：  

1, pom.xml中新建依赖：   

jar包已上传到github package中：
https://github.com/useraddZYB?tab=packages  
https://github.com/useraddZYB/insecticide/packages/1938853

<!-- insecticide -->  
        <dependency>  
            <groupId>com.programmerartist.insecticide</groupId>  
            <artifactId>insecticide</artifactId>  
            <version>1.0-SNAPSHOT</version>  
        </dependency>
        
        
2, spring.xml 中新增如下aop配置

<!-- insecticide config -->  
    <bean id="aspectAdvice" class="com.programmerartist.insecticide.advisor.Around">  
        <constructor-arg name="traceName" value="dataapp" />  
        <constructor-arg name="didName" value="did" />  
        <!-- 可选配置举例  
        <property name="debugName" value="debug"/>  
        <property name="debugValue" value="true"/>  
        -->  
    </bean>  
    <aop:config>  
        <aop:aspect id="businessAspect" ref="aspectAdvice">  
            <aop:pointcut id="point_cut" expression="execution(public * com.xx.dataapp.recsys.recommender.impl.*.*(..)) and !execution(public * com.xx.dataapp.recsys.recommender.impl.TestImpl.addUserHistorys(..))" />  
            <aop:around method="doAround" pointcut-ref="point_cut"/>  
        </aop:aspect>  
    </aop:config>  


3, classpath下新增文件：insecticide.properties  

insect.dns=127.0.0.1:8089  
#local pull web server's config (120s is default, advice to 600)    
insect.heart.period=120  

       
4,补充说明（setInsectTrace）：  
a, 方法参数里如果含有多个did属性，则不支持（避免取错字段），会打印错误日志如下，不影响原方法运行：  
所以，请如上面所示，去掉掉相关方法的aop  
java.lang.RuntimeException: [3100] UnSupport_Doubt: we find doubt code, so throw exception to protect logic; detail info is = ( did is not only one )  
b, 方法参数中含有did字段的参数，需要加上insectTrace属性（链路id），且有getter setter，toString里可以不加此属性；  
如果rpc服务链路的节点间调用时，传输的不是起点节点传输过来的参数，则需要手动setInsectTrace(param.getiInsectTrace())
 ```

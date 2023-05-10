# spring-ioc
自己写的一个简易的spring-ioc
#### 首先先对这个小demo做一个说明。首先这个demo是很简易的，里面有一些可以优化的复杂点我设置了TODO，如果你有兴趣的话，可以自己去完善一下，写这个demo就是为了让自己对IOC的一个流程更加熟悉，对于刚开始学习Spring IOC的小伙伴来说学一下还是好的，可以帮助我们理解流程，为以后看源码做铺垫。编写是参考了Spring的源码中的流程。



大概的流程是这样的：

​	自定义了两个注解，@Be、@Di注解，其中@Be注解的作用和spring中的@Component、@Repository、@Service、@Controller类似，就是将当前这个类定义为Bean,对这个对象进行一个实例化。参考spring的源码，spring源码中，实例化的对象放到了一个map集合中，这个map的结构是这样的.所以根据这个我也使用了一个map来存放实例化后的对象。我自定的map结构是map<class,Object>，key是它对应的类，value是这个对象。于此对应的我们可以通过class来得到object。

对@Be标注的类进行实例化的流程大致如下：

​	1、我们定义一个构造方法，这个构造方法中传过来一个对应的包的全类名。类似于spring中的ClassPathXmlApplicationContext一样。

​	2、通过传过来的这个全类名，我们直到去哪个包以及这个包下面的所有子包中扫描所有的.class文件，然后将标注了@Be的.class文件进行实例化，当然里面还有很多注意的地方，下面会细说。

​	3、在Java中全类名的书写格式是这样的“com.lyq”，以"."分割的，因为我们要去操作系统的文件中去获取对应的文件，所以这个格式肯定是不对的，所以我们需要对这个格式进行一个转义，将“.”替换成"\\"，这个过程要注意转义。

​	4、然后通过当前线程可以获得这个包对应的绝对路径，因为我们一个项目中的前面的路径都是一样的，所以我用了一个全局变量rootPath来存放前面公共的部分。然后我们通过文件流去操作这个对应的绝对路径。

​	5、如果对应的这个文件是不是一个文件夹的话，其实是不合法的，说明传过来的包就错了， 所以不处理。当这个文件是一个文件夹的话进行接下来的处理。

​	6、去遍历这个文件夹下的所有的文件，因为我们要扫描这个包以及下面的所有子包中标注了@Be的类。然后对于它所有的子文件，如果这个子文件是一个文件夹的话我们就去递归扫描执行。然后是一个文件的话，我们还要判断是不是一个.class文件，因为我们操作的是一个类，所以必须是.class文件。

​	7、如果这个文件是一个.class文件了，我们要去实例化的时候我们又要通过反射，所以这里我们又要将这个“com\lyq”这种格式的路径转成"com.lyq"这种格式，然后得到后面的全类名。这里就是有点繁琐了。然后得到全类名了就好操作了。下面就是通过反射来实例化了。**所以，一定一定要对反射很熟悉，学习框架必不可少**

​	8、通过这个全类名我们可以得到对应的类class,然后我们要判断这个类是不是接口，因为接口是不能实例化的，所以如果是一个接口的话，我们是不处理的，我们只处理是一个类。

​	9、如果是一个类的话，我们要判断这个类上是不是有@Be注解，如果没有不处理，有的话，通过反射得到一个实例。

​	10、在最后放到map中之前还有一个细节，那就是判断这个类是不是又接口，如果这个类有接口的话，我们的这个map中应该放的是对应的接口。类似于我们通过这个实例得到它的接口类。为了多态。这也是spring中byType进行注入的时候如果一个接口有多个实现类的时候报错的原因。

​	

对@Di标注的属性进行注入的流程大致如下：（可以将@Di类比于@Autowired和@Resource）

​	1、因为我们的实例化的所有对象都放到了map中，所以我们需要遍历这个map

​	2、遍历map可以得到每一个object，根据反射可以得到这个object对应的类class

​	3、借助这个class，通过反射可以得到它所有的属性。然后遍历所有的属性。判断是不是有@Di注解，我们只处理有@Di标注的属性。

​	4、如果有@Di标注，我们就对当前这个object的这个field进行注入值。

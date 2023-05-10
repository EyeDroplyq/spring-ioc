package com.lyq.springioc.bean;

import com.lyq.springioc.anno.Be;
import com.lyq.springioc.anno.Di;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: lyq
 * @createDate: 10/5/2023
 * @version: 1.0
 */
public class AnnotationApplicationContext implements ApplicationContext{
    //因为最后要根据我们传过来的class进行返回，所以我们仿照spring原生的map来写一个map
    //将最后实例化好的对象放到这个map中存储起来，最后根据key来取出对应的对象就可以了
    private Map<Class,Object> beanFactory=new HashMap<>();
    private static String rootPath;
    @Override
    public Object getBean(Class clazz) {
        return beanFactory.get(clazz);
    }


    //根据给定的包的路径，我们去这个包中扫描标注了@Be的类，然后将对象的对象进行实例化
    public AnnotationApplicationContext(String packageBase) {
        try {
            //因为给定的包的名字是com.lyq这种格式的，但是在操作系统中文件是com\lyq这种格式的，所以我们需要对包的名字中的.进行替换成\
            String packagePath = packageBase.replaceAll("\\.", "\\\\");
            //根据这个包的路径来得到对应的绝对路径
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packagePath);
            while(urls.hasMoreElements()){
                URL url = urls.nextElement();
                //因为这个url中我们包的文件夹路径中的“\”会进行转义，所以我们需要转过来
                String dirs = URLDecoder.decode(url.getFile(), "utf-8");
                //我们将绝对路径中除了包路径之前的部分截取出来，放到rootPath中
                //比如我们的绝对路径为 E:\javaWorkSpace\permission\spring6\spring-ioc\src\main\java\com\lyq
                //我们将E:\javaWorkSpace\permission\spring6\spring-ioc\src\main\java截取出来作为rootPath，因为所有的包都是在这个文件夹下
                rootPath=dirs.substring(0,dirs.length()-packagePath.length());
                //进行包扫描
                loadBean(new File(dirs));
                //进行属性注入功能
                loadDi();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void loadBean(File file) throws Exception {
        //判断当前文件是不是一个文件夹，只有是文件夹才进行处理，不是文件夹直接返回
        if(file.isDirectory()){
            //遍历这个文件夹下面的所有文件
            File[] childrenFile = file.listFiles();
            if(childrenFile.length==0||childrenFile==null){
                return;
            }
            //遍历所有的子文件，如果是文件夹递归处理
            for (File child : childrenFile) {
                if(child.isDirectory()){
                    loadBean(child);
                }else{
                    //如果这个子文件不是文件夹，就看是不是.class文件了
                    String absolutePath = child.getAbsolutePath();
                    //对这个文件的绝对路径进行截取，前面的rootPath部门我们不用，所以只要后面的一部分
                    String pathWithClass = absolutePath.substring(rootPath.length() - 1);
                    if(pathWithClass.contains(".class")){
                        //TODO:如果让代码更优雅，可以将格式转化提出来
                        //如果这个文件是一个.class文件，我们就要进行处理了，如果不是.class文件我们不用管
                        //最后的路径是\这种格式，我们要转成.这个格式,最后的.class就不要了
                        String packageAllName = pathWithClass.replaceAll("\\\\", ".").replace(".class", "");
                        //这样这个packageAllName就是类对应的全类名了，然后通过反射来进行实例化了
                        //通过全类名得到对应的class
                        Class<?> clazz = Class.forName(packageAllName);
                        //但是还要判断这个类是不是一个接口，如果是一个接口的话，我们是不会进行实例化的,不是接口才会进行实例化
                        if(!clazz.isInterface()){
                          //判断这个类上是不是有@Be注解
                            Be beAnnotation = clazz.getAnnotation(Be.class);
                            if(beAnnotation!=null){
                                //如果有@Be注解就是我们要进行实例化的对象了
                                Object instance = clazz.getDeclaredConstructor().newInstance();
                                //但是我们实例化之后，还需要判断当前这个类有没有接口，如果有接口我们最后的map中应该存放对应的接口类
                                if(clazz.getInterfaces().length>0){
                                    //TODO,这里可以优化，一个类可以有多个接口，所以这里可以细化到去使用哪个接口，我这里简化的使用了第一个接口
                                    beanFactory.put(clazz.getInterfaces()[0],instance);
                                }else{
                                    beanFactory.put(clazz,instance);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private void loadDi() throws Exception {
        //我们所有的对象实例都放在了map中，所以我们应该遍历整个map
        for (Map.Entry<Class, Object> entry : beanFactory.entrySet()) {
            //获取每一个对象
            Object obj = entry.getValue();
            //获得每个对象的属性
            Class<?> clazz= obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            //如果每个对象的属性不为空，就遍历每一个属性
            if(fields.length!=0&&fields!=null){
                for (Field field : fields) {
                    //判断这个属性上有没有@Di注解
                    Di diAnnotation = field.getAnnotation(Di.class);
                    if(diAnnotation!=null){
                        //如果有这个注解，就需要进行注入
                        field.setAccessible(true);
                        //给当前这个对象设置属性，设置的属性应该是map中这个属性对应的类型的value值
                        field.set(obj,beanFactory.get(field.getType()));
                    }
                }
            }
        }
    }
}

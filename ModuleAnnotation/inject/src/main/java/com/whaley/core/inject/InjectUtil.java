package com.whaley.core.inject;

import android.support.v4.util.LruCache;

import com.whaley.core.inject.annotation.Presenter;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Router;
import com.whaley.core.utils.StrUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangZhi on 2017/7/17 22:27.
 */

public class InjectUtil {

    private static volatile InjectUtil INSTANCE;

    public static InjectUtil getInstance(){
        if(INSTANCE == null){
            synchronized( InjectUtil.class ){
                if(INSTANCE == null){
                    INSTANCE = new InjectUtil();
                }
            }
        }
        return INSTANCE;
    }
    
    private InjectUtil(){
    }
    
    


    public static final String PAGE_VIEW_CLASS_NAME = "com.whaley.core.uiframe.view.PageView";

    public static final String MVP_FRAGMENT_CLASS_NAME = "com.whaley.core.uiframe.MVPFragment";

    public static final String MVP_ACTIVITY_CLASS_NAME = "com.whaley.core.uiframe.MVPActivity";

    public static final String PAGE_PRESENTER_CLASS_NAME = "com.whaley.core.uiframe.presenter.PagePresenter";

    private static final String REPOSITORY_MANAGER_CLASS_NAME = "com.whaley.core.repository.RepositoryManager";

    private static final String IREPOSITORY_MANAGER_CLASS_NAME = "com.whaley.core.repository.IRepositoryManager";

    private static final String IREPOSITORY_CLASS_NAME = "com.whaley.core.repository.IRepository";

    private static final String USECASE_CLASS_NAME="com.whaley.core.interactor.UseCase";

    private static final String GET_REPOSITORY_MANAGER_METHOD_NAME = "getRepositoryManager";



    private static final String JAVA_SCHEDULERS_CLASS_NAME = "io.reactivex.schedulers.Schedulers";

    private static final String ANDROID_SCHEDULERS_CLASS_NAME = "io.reactivex.android.schedulers.AndroidSchedulers";

    private static final String SCHEDULER_CLASS_NAME = "io.reactivex.Scheduler";

    private static final String SCHEDULER_METHOD_IO = "io";

    private static final String SCHEDULER_METHOD_COMPUTATION = "computation";

    private static final String SCHEDULER_METHOD_NEW_THREAD = "newThread";

    private static final String SCHEDULER_METHOD_SINGLE = "single";

    private static final String SCHEDULER_METHOD_TRAMPOLINE = "trampoline";

    private static final String SCHEDULER_METHOD_MAIN_THREAD = "mainThread";

    private static final String USECACHE_MANAGER_ADD_METHOD_NAME = "addUseCase";

    private static final LruCache<String,Class<?>> CLASS_CACHE = new LruCache<>(50);

    public void inject(Object object){
        Class<?> clazz = object.getClass();
        inject(object,clazz);
    }

    private void inject(Object object,Class clazz){
        if(!checkClass(clazz))
            return;
        Field[] fields = clazz.getDeclaredFields();
        Map<Integer,Object> repositoryCache = new HashMap<>();
        List<Field> usecaseFields = null;
        for (Field field : fields){
            if (checkStaticAndFinalField(field)) {
                continue;
            }

            if(!checkHasAnnotationField(field)){
                continue;
            }


            Object injectPresenter = checkPresenter(object,field);

            if(injectFieldValue(object,field,injectPresenter)){
                continue;
            }

            RepositoryMeta injectRepository = checkRepository(field);

            if(injectRepository != null && injectFieldValue(object,field,injectRepository.repositoryInstance)){
                repositoryCache.put(injectRepository.type,injectRepository.repositoryInstance);
                continue;
            }

            if(!field.isAnnotationPresent(UseCase.class)) {
                continue;
            }
            if(usecaseFields == null){
                usecaseFields = new ArrayList<>();
            }
            usecaseFields.add(field);
        }

        if(usecaseFields!=null) {
            for (Field field : usecaseFields) {
                Object injectUseCase = checkUseCase(object,field, repositoryCache.get(Repository.LOCAL), repositoryCache.get(Repository.REMOTE), repositoryCache.get(Repository.MEMORY));
                if(!injectFieldValue(object, field, injectUseCase)){
                    continue;
                }
                Class injectClazz = object.getClass();
                if(!getClassForName(PAGE_PRESENTER_CLASS_NAME).isAssignableFrom(injectClazz)){
                    continue;
                }
                Class useCaseClass = getClassForName(USECASE_CLASS_NAME);
                if(useCaseClass == null) {
                    continue;
                }
                try {
                    Method method = injectClazz.getMethod(USECACHE_MANAGER_ADD_METHOD_NAME,useCaseClass);
                    method.invoke(object,injectUseCase);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        clazz = clazz.getSuperclass();
        inject(object, clazz);
    }

    private boolean checkClass(Class clazz){
        return clazz.getName().startsWith("com.whaley");
    }

    private boolean checkStaticAndFinalField(Field field){
        int mod = field.getModifiers();
        return Modifier.isStatic(mod) || Modifier.isFinal(mod);
    }

    private boolean checkHasAnnotationField(Field field){
        return field.getAnnotations().length>0;
    }

    private Object checkPresenter(Object object,Field field){
        if(!field.isAnnotationPresent(Presenter.class)) {
            return null;
        }
        Class injectObjClazz = object.getClass();
        if(!checkInstanceOfPageView(injectObjClazz)){
            return null;
        }
        Object pageViewObj = object;

        Class presenterClass = getPresenterClass(injectObjClazz);

        if(isAbstract(presenterClass)){
            return null;
        }
        try {
            Constructor constructor = presenterClass.getConstructor(getPageViewClass(presenterClass));
            return constructor.newInstance(pageViewObj);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }


    private Class getPageViewClass(Class presenterClazz){
        Type genericSuperClazz = presenterClazz.getGenericSuperclass();
        if(genericSuperClazz instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType)genericSuperClazz;
            Type[] types = parameterizedType.getActualTypeArguments();
            Class pageViewClazz = (Class) types[0];
            return pageViewClazz;
        }else if(genericSuperClazz instanceof Class){
            Class superClazz = (Class)genericSuperClazz;
            Class pagePresenterClass = getClassForName(PAGE_PRESENTER_CLASS_NAME);
            if(pagePresenterClass.isAssignableFrom(superClazz)){
                return getPageViewClass(superClazz);
            }
        }

        return null;
    }


    private Class getPresenterClass(Class injectObjClazz){
        Type genericSuperClazz = injectObjClazz.getGenericSuperclass();
        if(genericSuperClazz instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType)genericSuperClazz;
//            Class superClass = (Class)parameterizedType.getRawType();
//            if(superClass != getClassForName(MVP_ACTIVITY_CLASS_NAME) && superClass != getClassForName(MVP_FRAGMENT_CLASS_NAME))
//                return getPresenterClass(superClass,field);
            Type[] types = parameterizedType.getActualTypeArguments();
            Class presenterClazz = (Class) types[0];
            return presenterClazz;
        }else if(genericSuperClazz instanceof Class){
            Class superClazz = (Class)genericSuperClazz;
            Class pageViewClass = getClassForName(PAGE_VIEW_CLASS_NAME);
            if(pageViewClass.isAssignableFrom(superClazz))
            return getPresenterClass(superClazz);
        }
        return null;
    }



    /**
     * 检测 Repository 注解 并获得注入实例
     * @param field
     * @return
     */
    private RepositoryMeta checkRepository(Field field){
        if(!field.isAnnotationPresent(Repository.class)) {
            return null;
        }
        RepositoryMeta meta = new RepositoryMeta();
        Repository injectAnnotation = field.getAnnotation(Repository.class);
        meta.type = injectAnnotation.type();
        meta.name = injectAnnotation.name();
        field.setAccessible(true);
        Class<?> clazz = field.getType();
        if(isAbstract(clazz)){
            meta.repositoryInstance = router(meta.name);
            return meta;
        }
        try {
            meta.repositoryInstance = clazz.newInstance();
            return meta;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 检测 UseCase 注解 并获得注入实例
     * @param field
     * @param localRepository
     * @param remoteRepository
     * @param memoryRepository
     * @return
     */
    private Object checkUseCase(Object object,Field field, Object localRepository, Object remoteRepository, Object memoryRepository){
        if(!field.isAnnotationPresent(UseCase.class)) {
            return null;
        }
        UseCase injectAnnotation = field.getAnnotation(UseCase.class);
        Class injectClass = object.getClass();
        Object repositoryManager;
        if( getClassForName(USECASE_CLASS_NAME).isAssignableFrom(injectClass) ){
            repositoryManager = getRepositoryManager(object);
        }else {
            localRepository = getInjectRepository(injectAnnotation.localClassName(), localRepository);
            remoteRepository = getInjectRepository(injectAnnotation.remoteClassName(), remoteRepository);
            memoryRepository = getInjectRepository(injectAnnotation.remoteClassName(), memoryRepository);
            repositoryManager = createRepositoryManager(localRepository, remoteRepository, memoryRepository);
        }
        Object executeThread = getScheduler(injectAnnotation.executeThread());
        Object postExecutionThread = getScheduler(injectAnnotation.postExecutionThread());

        field.setAccessible(true);
        Class<?> clazz = field.getType();
        if(isAbstract(clazz)){
//            String name = injectAnnotation.name();
//            return router(name);
            return null;
        }

        Class iRepositoryClass = getClassForName(IREPOSITORY_MANAGER_CLASS_NAME);

        Class schedulerClass = getClassForName(SCHEDULER_CLASS_NAME);

        if(iRepositoryClass == null || schedulerClass ==null){
            return null;
        }

        try {
            Constructor constructor = clazz.getConstructor(iRepositoryClass,schedulerClass,schedulerClass);
            return constructor.newInstance(repositoryManager,executeThread,postExecutionThread);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 通过类型获得 Scheduler
     * @param scheduler
     * @return
     */
    private Object getScheduler(int scheduler){
        Object schedulerInstance = null;
        switch (scheduler){
            case UseCase.IO:
                schedulerInstance = getScheduler(JAVA_SCHEDULERS_CLASS_NAME,SCHEDULER_METHOD_IO);
                break;
            case UseCase.COMPUTATION:
                schedulerInstance = getScheduler(JAVA_SCHEDULERS_CLASS_NAME,SCHEDULER_METHOD_COMPUTATION);
                break;
            case UseCase.NEW_THREAD:
                schedulerInstance = getScheduler(JAVA_SCHEDULERS_CLASS_NAME,SCHEDULER_METHOD_NEW_THREAD);
                break;
            case UseCase.SINGLE:
                schedulerInstance = getScheduler(JAVA_SCHEDULERS_CLASS_NAME,SCHEDULER_METHOD_SINGLE);
                break;
            case UseCase.TRAMPOLINE:
                schedulerInstance = getScheduler(JAVA_SCHEDULERS_CLASS_NAME,SCHEDULER_METHOD_TRAMPOLINE);
                break;
            case UseCase.MAIN_THREAD:
                schedulerInstance = getScheduler(ANDROID_SCHEDULERS_CLASS_NAME,SCHEDULER_METHOD_MAIN_THREAD);
                break;
            default:
                break;
        }
        return schedulerInstance;
    }

    /**
     * 通过 类名和 静态方法名获得 Scheduler
     * @param schedulersName
     * @param methodName
     * @return
     */
    private Object getScheduler(String schedulersName,String methodName){
        Class clazz = getClassForName(schedulersName);
        if(clazz == null)
            return null;
        try {
            Method method = clazz.getDeclaredMethod(methodName);
            Object object = method.invoke(null);
            return object;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获得 Repository 注入实例 （如果所传的 className 不为空则反射获取实例 否则 返回传递来的 respository 实例）
     * @param className
     * @param repository
     * @return
     */
    private Object getInjectRepository(String className, Object repository){
        if(StrUtil.isEmpty(className)){
            return repository;
        }
        Class clazz = getClassForName(className);
        if(clazz == null)
            return repository;
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return repository;
    }


    /**
     * 创建 RepositoryManager 实例
     * @param localRepository
     * @param remoteRepository
     * @param memoryRepository
     * @return
     */
    private Object createRepositoryManager(Object localRepository, Object remoteRepository, Object memoryRepository){
        Class clazz=getClassForName(REPOSITORY_MANAGER_CLASS_NAME);
        Class iRepositoryClazz = getClassForName(IREPOSITORY_CLASS_NAME);
        if(clazz == null || iRepositoryClazz ==null){
            return null;
        }
        try {
            Constructor constructor = clazz.getDeclaredConstructor(iRepositoryClazz,iRepositoryClazz,iRepositoryClazz);
            constructor.setAccessible(true);
            return constructor.newInstance(remoteRepository,localRepository,memoryRepository);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从 UseCase 实例中获取 RepositoryManager 对象
     * @param useCaseInstance
     * @return
     */
    private Object getRepositoryManager(Object useCaseInstance){
        Class clazz = useCaseInstance.getClass();
        try {
            Method method = clazz.getMethod(GET_REPOSITORY_MANAGER_METHOD_NAME);
            Object repositoryManager = method.invoke(useCaseInstance);
            return repositoryManager;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 检查是不是 PageView 的实现类
     * @param clazz
     * @return
     */
    private boolean checkInstanceOfPageView(Class clazz){
        return getClassForName(PAGE_VIEW_CLASS_NAME).isAssignableFrom(clazz);
    }

    private Object getInstanceForClassName(String className){
        Class<?> clazz = getClassForName(className);
        if(clazz == null) {
            return null;
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class<?> getClassForName(String className){
        Class<?> clazz = CLASS_CACHE.get(className);
        if(clazz != null)
            return clazz;
        try {
            clazz = Class.forName(className);
            CLASS_CACHE.put(className,clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }


    /**
     * 判断是否为抽象类或接口
     * @param clazz
     * @return
     */
    public boolean isAbstract(Class clazz){
        return Modifier.isAbstract(clazz.getModifiers())||Modifier.isInterface(clazz.getModifiers());
    }

    /**
     * 注入属性值
     * @param object
     * @param field
     * @param injectValue
     * @return
     */
    private boolean injectFieldValue(Object object,Field field,Object injectValue){
        if(injectValue != null){
            try {
                field.setAccessible(true);
                field.set(object,injectValue);
                inject(injectValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /**
     * 通过路由获取实例对象
     * @param name
     * @return
     */
    private Object router(String name){
        if(StrUtil.isEmpty(name))
            return null;
        return Router.getInstance().buildObj(name).getObj();
    }

    private static class RepositoryMeta{
        Object repositoryInstance;
        int type;
        String name;
    }
}

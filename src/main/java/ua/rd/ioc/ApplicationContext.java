package ua.rd.ioc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class ApplicationContext implements Context {

    private final Config config;
    private final Map<String, Object> context = new HashMap<>();

    public ApplicationContext() {
        this(null);
    }

    public ApplicationContext(Config config) {
        this.config = config;
        if (config != null) {
            initApplicationContext();
        }
    }

    private void initApplicationContext() {
        BeanDefinition[] beanDefinitions = config.getBeanDefinitions();
        for (BeanDefinition bd : beanDefinitions) {
            String beanName = bd.getBeanName();
            createBean(beanName);
        }
    }

    private void createBean(String beanName) {
        if (context.containsKey(beanName)) {
            return;
        }

        BeanDefinition bd = getBeanDefinition(beanName);
        Class<?> beanType = bd.getType();
        Constructor<?> beanConstructor = beanType.getConstructors()[0];
        Object bean = null;
        if (beanConstructor.getParameterCount() == 0) {
            bean = createBeanWithNoArgConstructor(beanType);
        } else {
            //bean = createBeanWithArgConstructor(beanName);
        }
        callInitMethod(bean);
        bean = createBenchmarkProxy(bean);

        context.put(beanName, bean);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        if (config == null) {
            return new String[]{};
        }
        return Arrays.stream(config.getBeanDefinitions())
                .map(BeanDefinition::getBeanName)
                .toArray(String[]::new);
    }

    @Override
    public <T> T getBean(String beanName) {
        //if (beanDefenition is ptototype) createBeen
        T bean = (T) context.get(beanName);
        return bean;
    }

    private <T> void callInitMethod(T bean) throws RuntimeException {
        Class<?> beanClass = bean.getClass();
        try {
            Method initMethod = beanClass.getMethod("init");
            try {
                initMethod.invoke(bean);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } catch (NoSuchMethodException ex) {
        }
    }

    private <T> T createBeanWithNoArgConstructor(Class<?> beanType) {

        Function<Class<?>, ?> newInstance = cl -> {
            try {
                return cl.newInstance();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };

        return (T) newInstance.apply(beanType);
    }

    private BeanDefinition getBeanDefinition(String beanName) {
        return Arrays.stream(config.getBeanDefinitions())
                .filter(bd -> bd.getBeanName().equals(beanName))
                .findAny().orElseThrow(() -> new RuntimeException("BD not found"));
    }

    private Object createBenchmarkProxy(Object bean) {
        Class<?> beanType = bean.getClass();
        Method[] methods = beanType.getMethods();
        boolean isBenchmarking = Stream.of(methods)
                .anyMatch(m -> m.isAnnotationPresent(Benchmark.class));

        Object proxyBean = bean;
        if (isBenchmarking) {
            proxyBean = Proxy.newProxyInstance(
                    ClassLoader.getSystemClassLoader(),
                    bean.getClass().getInterfaces(),
                    (proxy, method, args) -> {
                        if (method.isAnnotationPresent(Benchmark.class)) {
                            System.out.println("Benchmarking " + method.getName());
                        }
                        return method.invoke(bean, args);
                    });
        }
        return proxyBean;
    }

}

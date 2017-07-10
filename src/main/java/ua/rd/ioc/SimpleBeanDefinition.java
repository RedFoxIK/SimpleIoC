package ua.rd.ioc;

public class SimpleBeanDefinition implements BeanDefinition {

    private final String beanName;
    private final Class<?> type;
    private final boolean isPrototype;

    public SimpleBeanDefinition(String beanName, Class<?> type) {
        this(beanName, type, false);
    }

    public SimpleBeanDefinition(String beanName, Class<?> type, boolean isPrototype) {
        this.beanName = beanName;
        this.type = type;
        this.isPrototype = isPrototype;
    }

    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public <T> Class<T> getType() {
        return (Class<T>) type;
    }

    @Override
    public boolean isPrototype() {
        return isPrototype;
    }

}

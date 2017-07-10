package ua.rd.ioc;

import java.util.Map;

public class JavaConfig implements Config {

    private final BeanDefinition[] beanDefinitions;
    
    public JavaConfig() {
        beanDefinitions = new BeanDefinition[]{};
    }
     

    public JavaConfig(Map<String, Class<?>> beanDescriptions) {
        beanDefinitions = beanDescriptions.entrySet().stream()
                .map(e -> new SimpleBeanDefinition(e.getKey(), e.getValue()))
                .toArray(BeanDefinition[]::new);
    }

    @Override
    public BeanDefinition[] getBeanDefinitions() {
        return beanDefinitions;
    }

}

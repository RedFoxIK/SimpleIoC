/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.rd.ioc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import ua.rd.domain.Tweet;
import ua.rd.repository.InMemTweetRepository;
import ua.rd.repository.TweetRepository;

/**
 *
 * @author Andrii_Rodionov
 */
public class ApplicationContextTest {

    public ApplicationContextTest() {
    }

    @Test
    public void testInitContextWithoutJavaConfig() {
        Context context = new ApplicationContext();
        assertNotNull(context);
    }

    @Test
    public void testInitContextWithEmptyJavaConfig() {
        Config javaConfig = new JavaConfig();
        Context context = new ApplicationContext(javaConfig);
        assertNotNull(context);
    }

    @Test
    public void testBeanDefenitionsWithEmptyJavaConfig() {
        Config javaConfig = new JavaConfig();
        BeanDefinition[] beanDefinitions = javaConfig.getBeanDefinitions();
        assertEquals(beanDefinitions.length, 0);
    }

    @Test
    public void testBeanDefenitionsWithJavaConfig() {
        Map<String, Class<?>> beanDescriptions
                = createTestBeanDescriptions();
        Config javaConfig = new JavaConfig(beanDescriptions);
        BeanDefinition[] beanDefinitions = javaConfig.getBeanDefinitions();
        assertEquals(beanDefinitions.length, beanDescriptions.size());
    }

    @Test
    public void testBeanDefenitionNamesWithoutJavaConfig() {
        Context context = new ApplicationContext();
        String[] beanDefenitionNames = context.getBeanDefinitionNames();
        assertEquals(beanDefenitionNames.length, 0);
    }

    @Test
    public void testBeanDefenitionNamesWithJavaConfig() {
        Map<String, Class<?>> beanDescriptions = createTestBeanDescriptions();
        Context context = new ApplicationContext(new JavaConfig(beanDescriptions));
        String[] beanDefenitionNames = context.getBeanDefinitionNames();
        assertTrue(Arrays.asList(beanDefenitionNames).contains("tweet"));
    }

    @Test
    public void testGetBean() {
        Map<String, Class<?>> beanDescriptions = createTestBeanDescriptions();
        Context context = new ApplicationContext(new JavaConfig(beanDescriptions));
        assertNotNull(context.getBean("tweet"));
    }

//    @Test
//    public void testGetTwoSameBeansEq() {
//                       
//        Context context = new ApplicationContext(new JavaConfig(beanDescriptions));
//        Object bean1 = context.getBean("tweet");
//        Object bean2 = context.getBean("tweet");
//        assertSame(bean1, bean2);
//    }

    @Test

    public void testGetTwoSameBeansAsPrototypeNeq() {
        Map<String, Class<?>> beanDescriptions = createTestBeanDescriptions();
        Context context = new ApplicationContext(new JavaConfig(beanDescriptions));
        Object bean1 = context.getBean("tweet");
        Object bean2 = context.getBean("tweet");
        assertSame(bean1, bean2);
    }

    @Test
    public void testInitMethodCalled() {
        Map<String, Class<?>> beanDescriptions = createTestBeanDescriptions();
        Context context = new ApplicationContext(new JavaConfig(beanDescriptions));
        TweetRepository tr = context.getBean("tweetRepository");
        assertNotNull(tr.getAllTweets());
    }

    private Map<String, Class<?>> createTestBeanDescriptions() {
        Map<String, Class<?>> beanDescriptions
                = new HashMap<String, Class<?>>() {
            {
                put("tweet", Tweet.class);
                put("tweetRepository", InMemTweetRepository.class);
            }
        };
        return beanDescriptions;
    }

}

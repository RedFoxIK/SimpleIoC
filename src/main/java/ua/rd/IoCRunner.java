package ua.rd;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import ua.rd.domain.Tweet;
import ua.rd.ioc.ApplicationContext;
import ua.rd.ioc.Config;
import ua.rd.ioc.Context;
import ua.rd.ioc.JavaConfig;
import ua.rd.repository.InMemTweetRepository;
import ua.rd.repository.TweetRepository;
import ua.rd.service.SimpleTweetService;
import ua.rd.service.TweetService;

public class IoCRunner {

    public static void main(String[] args) {
        Map<String, Class<?>> beanDescriptions
                = new HashMap<String, Class<?>>() {
            {
                put("tweetRepository", InMemTweetRepository.class);
                put("tweetService", SimpleTweetService.class);
                //put("tweet2", Tweet.class);
            }
        };
        Config config = new JavaConfig(beanDescriptions);
        Context context = new ApplicationContext(config);
        System.out.println(Arrays.toString(context.getBeanDefinitionNames()));

        TweetRepository tr = context.getBean("tweetRepository");
        System.out.println(tr.getAllTweets());
        System.out.println(tr.getClass());

        TweetService ts = context.getBean("tweetService");
        System.out.println(ts.getAllTweets());
    }
}

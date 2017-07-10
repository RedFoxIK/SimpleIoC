package ua.rd.repository;

import java.util.Arrays;
import java.util.List;
import ua.rd.domain.Tweet;
import ua.rd.ioc.Benchmark;

public class InMemTweetRepository implements TweetRepository {

    private List<Tweet> tweets;

    public void init() {
        tweets = Arrays.asList(
                new Tweet("Andrii", "First tweet"),
                new Tweet("Serg", "Second tweet")
        );
    }

    @Override
    @Benchmark
    public Iterable<Tweet> getAllTweets() {
        return tweets;
    }

}

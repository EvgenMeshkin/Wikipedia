package by.evgen.android.apiclient.twitter;

/**
 * Created by evgen on 06.03.2015.
 */
public class Tweet {

    String tweetBy;
    String tweet;

    public Tweet(String tweetBy, String tweet) {
        this.tweetBy = tweetBy;
        this.tweet = tweet;
    }

    public String getTweetBy() {
        return tweetBy;
    }

    public void setTweetBy(String tweetBy) {
        this.tweetBy = tweetBy;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

}

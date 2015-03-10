package by.evgen.android.apiclient.twitter;

import android.app.ProgressDialog;
import android.os.AsyncTask;


import java.util.ArrayList;
import java.util.List;


import by.evgen.android.apiclient.utils.Log;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by evgen on 06.03.2015.
 */
public class SearchOnTwitter extends AsyncTask<String, Void, Integer> {

    private final String TWIT_CONS_KEY = "7UlcR38AeujmhJ8d0ZPzLz5Ld";
    private final String TWIT_CONS_SEC_KEY = "me5KS2RJCocKHKZJf2raQgfz907HquomtvC9c3zss33OEiBYec";

    ArrayList<Tweet> tweets;
    final int SUCCESS = 0;
    final int FAILURE = SUCCESS + 1;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
   }

    @Override
    protected Integer doInBackground(String... params) {
        try {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setUseSSL(true);
            builder.setApplicationOnlyAuthEnabled(true);
            builder.setOAuthConsumerKey(TWIT_CONS_KEY);
            builder.setOAuthConsumerSecret(TWIT_CONS_SEC_KEY);

            OAuth2Token token = new TwitterFactory(builder.build()).getInstance().getOAuth2Token();

            builder = new ConfigurationBuilder();
            builder.setUseSSL(true);
            builder.setApplicationOnlyAuthEnabled(true);
            builder.setOAuthConsumerKey(TWIT_CONS_KEY);
            builder.setOAuthConsumerSecret(TWIT_CONS_SEC_KEY);
            builder.setOAuth2TokenType(token.getTokenType());
            builder.setOAuth2AccessToken(token.getAccessToken());

            Twitter twitter = new TwitterFactory(builder.build()).getInstance();

            Query query = new Query(params[0]);
            // YOu can set the count of maximum records here
            query.setCount(50);
            QueryResult result;
            result = twitter.search(query);
            List<twitter4j.Status> tweets = result.getTweets();
            StringBuilder str = new StringBuilder();
            if (tweets != null) {
                this.tweets = new ArrayList<Tweet>();
                for (twitter4j.Status tweet : tweets) {
                    str.append("@" + tweet.getUser().getScreenName() + " - " + tweet.getText() + "\n");
                    System.out.println(str);
                    this.tweets.add(new Tweet("@" + tweet.getUser().getScreenName(), tweet.getText()));
                }
                return SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return FAILURE;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        Log.d(SearchOnTwitter.class, "************" + result.toString());
//        if (result == SUCCESS) {
//            list.setAdapter(new TweetAdapter(MainActivity.this, tweets));
//        } else {
//            Toast.makeText(MainActivity.this, getString(R.string.error), Toast.LENGTH_LONG).show();
//        }
    }
}

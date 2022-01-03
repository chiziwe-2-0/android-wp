package app.wordpress.test.wordpress.api.providers;

import java.util.ArrayList;

import app.wordpress.test.wordpress.PostItem;
import app.wordpress.test.wordpress.api.WordpressGetTaskInfo;


/**
 * This is an interface for Wordpress API Providers.
 */
public interface WordpressProvider {

    String getRecentPosts(WordpressGetTaskInfo info);

    String getTagPosts(WordpressGetTaskInfo info, String tag);

    String getCategoryPosts(WordpressGetTaskInfo info, String category);

    String getSearchPosts(WordpressGetTaskInfo info, String query);

    ArrayList<PostItem> parsePostsFromUrl(WordpressGetTaskInfo info, String url);

}

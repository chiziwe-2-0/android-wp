package app.wordpress.test.comments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import app.wordpress.test.Config;
import app.wordpress.test.R;
import app.wordpress.test.util.Helper;
import app.wordpress.test.util.Log;
import app.wordpress.test.web.WebviewActivity;


public class CommentsActivity extends AppCompatActivity {

	ArrayList<Comment> comments;
	ArrayAdapter<Comment> commentsAdapter;
	int type;
	String id;

	private Toolbar mToolbar;

	public static String DATA_PARSEABLE = "parseable";
	public static String DATA_TYPE = "type";
	public static String DATA_ID = "id";

	public static int WORDPRESS_JETPACK = 4;
	public static int WORDPRESS_JSON = 5;
	public static int WORDPRESS_REST = 6;
	public static int DISQUS = 7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(getResources().getString(R.string.comments));

		Helper.admobLoader(this,getResources(), findViewById(R.id.adView));

		Bundle extras = getIntent().getExtras();
		String parseableString = extras.getString(DATA_PARSEABLE);
		type = extras.getInt(DATA_TYPE);
		id = extras.getString(DATA_ID);

		comments = new ArrayList<>();
		commentsAdapter = new CommentsAdapter(this, comments, type);

		ListView lvComments = (ListView) findViewById(R.id.listView);
		lvComments.setAdapter(commentsAdapter);
		lvComments.setEmptyView(findViewById(R.id.empty));

		commentsAdapter.notifyDataSetChanged();

		// Fetch other comments
		fetchComments(parseableString);
	}

	private void fetchComments(final String parseableString) {
		if (type == WORDPRESS_JSON) {

			((TextView) findViewById(R.id.empty)).setText(getResources()
					.getString(R.string.loading));
			new Thread(new Runnable() {
				@Override
				public void run() {

					try {

						JSONArray dataJsonArray = new JSONArray(parseableString);

						ArrayList<Comment> toBeAddedLater = new ArrayList<>();

						for (int i = 0; i < dataJsonArray.length(); i++) {
							JSONObject commentJson = dataJsonArray
									.getJSONObject(i);
							Comment comment = new Comment();
							comment.text = commentJson.getString("content")
									.trim().replace("<p>", "")
									.replace("</p>", "");
							comment.username = commentJson.getString("name");
							comment.id = commentJson.getInt("id");
							comment.parentId = commentJson.getInt("parent");
							comment.linesCount = 0;
							if (comment.parentId == 0) {
								comments.add(comment);
							} else {
								toBeAddedLater.add(comment);
							}
						}

						Log.v("INFO", "Added: " + comments.size() + " to be added: " + toBeAddedLater.size());

						Collections.reverse(toBeAddedLater);

						do {
							for (int i = 0; i < toBeAddedLater.size(); i++) {

								int index = checkIfContains(toBeAddedLater.get(i).parentId);
								if (index >= 0) {
									toBeAddedLater.get(i).linesCount = comments.get(index).linesCount + 1;
									comments.add(index + 1, toBeAddedLater.get(i));
									toBeAddedLater.remove(i);
								}
							}
						} while (toBeAddedLater.size() > 0);

					} catch (JSONException | NullPointerException e) {
						Log.printStackTrace(e);
					}

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							commentsAdapter.notifyDataSetChanged();
							((TextView) findViewById(R.id.empty))
									.setText(getResources().getString(
											R.string.no_results));
						}
					});
				}
			}).start();
        } else if (type == WORDPRESS_REST) {
            ((TextView) findViewById(R.id.empty)).setText(getResources()
                    .getString(R.string.loading));
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        JSONArray dataJsonArray = Helper.getJSONArrayFromUrl(parseableString);

                        ArrayList<Comment> toBeAddedLater = new ArrayList<>();

                        for (int i = 0; i < dataJsonArray.length(); i++) {
                            JSONObject commentJson = dataJsonArray
                                    .getJSONObject(i);
                            Comment comment = new Comment();
                            comment.text = commentJson.getJSONObject("content").getString("rendered")
                                    .trim().replace("<p>", "")
                                    .replace("</p>", "");
							comment.username = commentJson.getString("author_name");
                            comment.id = commentJson.getInt("id");
                            comment.profileUrl = commentJson.getJSONObject("author_avatar_urls").getString("96");
                            comment.parentId = commentJson.getInt("parent");
                            comment.linesCount = 0;
                            if (comment.parentId == 0) {
                                comments.add(comment);
                            } else {
                                toBeAddedLater.add(comment);
                            }
                        }

                        Log.v("INFO", "Added: " + comments.size() + " to be added: " + toBeAddedLater.size());
                        Collections.reverse(toBeAddedLater);

                        do {
                            for (int i = 0; i < toBeAddedLater.size(); i++) {
                                int index = checkIfContains(toBeAddedLater.get(i).parentId);
                                if (index >= 0) {
                                    toBeAddedLater.get(i).linesCount = comments.get(index).linesCount + 1;
                                    comments.add(index + 1, toBeAddedLater.get(i));
                                    toBeAddedLater.remove(i);
                                }
                            }
                        } while (toBeAddedLater.size() > 0);

                    } catch (JSONException | NullPointerException e) {
                        Log.printStackTrace(e);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commentsAdapter.notifyDataSetChanged();
                            ((TextView) findViewById(R.id.empty))
                                    .setText(getResources().getString(
                                            R.string.no_results));
                        }
                    });
                }
            }).start();
		} else if (type == WORDPRESS_JETPACK){

			((TextView) findViewById(R.id.empty)).setText(getResources()
					.getString(R.string.loading));

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						JSONObject response = Helper.getJSONObjectFromUrl(parseableString);
						JSONArray dataJsonArray = response
								.getJSONArray("comments");

                        ArrayList<Comment> toBeAddedLater = new ArrayList<>();

                        for (int i = 0; i < dataJsonArray.length(); i++) {
                            JSONObject commentJson = dataJsonArray
                                    .getJSONObject(i);
                            Comment comment = new Comment();
                            comment.text = commentJson.getString("content")
                                    .trim().replace("<p>", "")
                                    .replace("</p>", "");
							comment.username = commentJson.getJSONObject("author").getString("login");
                            comment.profileUrl = commentJson.getJSONObject("author").getString("avatar_URL");
                            comment.id = commentJson.getInt("ID");

                            JSONObject parentObj = commentJson.optJSONObject("parent");

                            if (parentObj != null) {
                                comment.parentId = parentObj.getInt("ID");
                            } else {
                                comment.parentId = 0;
                            }

                            comment.linesCount = 0;
                            if (comment.parentId == 0) {
                                comments.add(comment);
                            } else {
                                toBeAddedLater.add(comment);
                            }
                        }

                        //Log.v("INFO", "Added: " + comments.size() + " to be added: " + toBeAddedLater.size());
                        Collections.reverse(toBeAddedLater);

                        do {
                            for (int i = 0; i < toBeAddedLater.size(); i++) {

                                int index = checkIfContains(toBeAddedLater.get(i).parentId);
                                if (index >= 0) {
                                    toBeAddedLater.get(i).linesCount = comments.get(index).linesCount + 1;
                                    comments.add(index + 1, toBeAddedLater.get(i));
                                    toBeAddedLater.remove(i);
                                }
                            }
                        } while (toBeAddedLater.size() > 0);

					} catch (JSONException|NullPointerException e) {
						Log.printStackTrace(e);
					}

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							commentsAdapter.notifyDataSetChanged();
							((TextView) findViewById(R.id.empty))
									.setText(getResources().getString(
											R.string.no_results));
						}
					});

				}
			}).start();

		} else if (type == DISQUS){
			//Split the disqus parseable into: disqus url; identifier patter; shortname.
			String[] components = parseableString.split(";");

			//Insert the ID in the Disqus identifier pattern
			String disqusIdentifier = components[2].replace("%d", id);

			//Generate html to load in the webView based on the identifier and shortname
			String htmlComments = getHtmlComment(disqusIdentifier, components[1]);

			//Start a new WebView wit the given data and  disqus (base) url
			Intent mIntent = new Intent(this, WebviewActivity.class);
			mIntent.putExtra(WebviewActivity.OPEN_EXTERNAL, Config.OPEN_INLINE_EXTERNAL);
			mIntent.putExtra(WebviewActivity.URL, components[0]);
			mIntent.putExtra(WebviewActivity.HIDE_NAVIGATION, true);
			mIntent.putExtra(WebviewActivity.LOAD_DATA, htmlComments);
			startActivity(mIntent);

			//We won't be proceeding in this activity
			finish();
		}
		commentsAdapter.notifyDataSetChanged();
	}

	private int checkIfContains(int parentId) {
		for (int a = 0; a < comments.size(); a++) {
			if (comments.get(a).id == parentId){
				return a;
			}
		}
		return -1;
	}

	//Get html to load in webview for id and shortname
	public String getHtmlComment(String idPost, String shortName) {

		return "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head><body><div id='disqus_thread'></div></body>"
				+ "<script type='text/javascript'>"
				+ "var disqus_identifier = '"
				+ idPost
				+ "';"
				+ "var disqus_shortname = '"
				+ shortName
				+ "';"
				+ " (function() { var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;"
				+ "dsq.src = '/embed.js';"
				+ "(document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq); })();"
				+ "</script></html>";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}

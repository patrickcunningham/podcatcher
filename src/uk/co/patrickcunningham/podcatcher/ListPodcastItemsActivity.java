package uk.co.patrickcunningham.podcatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.patrickcunningham.podcatcher.dao.PodcastDAO;
import uk.co.patrickcunningham.podcatcher.rss.Podcast;
import uk.co.patrickcunningham.podcatcher.rss.RSSFeed;
import uk.co.patrickcunningham.podcatcher.rss.RSSItem;
import uk.co.patrickcunningham.podcatcher.rss.RSSParser;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * ListPodcastItemsActivity.java
 * 
 * List all available podcast episodes available for a specific podcast feed
 * 
 * Retrieves RSS feed from the internet and puts contents into a ListView
 * 
 * @author Patrick Cunningham <patrick.cunningham@bbc.co.uk>
 */
public class ListPodcastItemsActivity extends ListActivity {

	// Progress Dialog
	private ProgressDialog pDialog;

	// Array list for list view
	ArrayList<HashMap<String, String>> rssItemList = new ArrayList<HashMap<String, String>>();

	RSSParser rssParser = new RSSParser();

	static MediaPlayer mPlayer;

	// button add new website
	ImageButton addPodcastButton;

	List<RSSItem> rssItems = new ArrayList<RSSItem>();

	RSSFeed rssFeed;

	private static String TAG_TITLE = "title";
	private static String TAG_LINK = "link";
	private static String TAG_AUDIO = "audio";
	private static String TAG_DESRIPTION = "description";
	private static String TAG_PUB_DATE = "pubDate";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rss_item_list);

		addPodcastButton = (ImageButton) findViewById(R.id.addPodcastButton);
		// hiding plus button
		addPodcastButton.setVisibility(View.GONE);

		// get in data
		final Intent i = getIntent();

		// SQLite Row id
		Integer site_id = Integer.parseInt(i.getStringExtra("id"));

		// Getting Single website from SQLite
		PodcastDAO rssDB = new PodcastDAO(getApplicationContext());

		Podcast podcast = rssDB.getPodcast(site_id);
		String rss_link = podcast.getRssLink();

		/**
		 * Load all episodes
		 * 
		 * */
		new loadRSSFeedItems().execute(rss_link);

		// selecting single ListView item
		final ListView lv = getListView();

		// Launching new screen on Selecting Single ListItem
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent in = new Intent(getApplicationContext(),
						PlayerActivity.class);
				// Set podcast details to pass to Player Activity for playing
				in.putExtra("contentTitle", R.string.app_name);
				in.putExtra(
						"title",
						getString(R.string.now_playing)
								+ ((TextView) view.findViewById(R.id.title))
										.getText().toString());
				in.putExtra("description", ((TextView) view
						.findViewById(R.id.link)).getText().toString());
				in.putExtra("audioUrl", ((TextView) view
						.findViewById(R.id.audio_url)).getText().toString());
				startActivity(in);

			}
		});
	}

	/**
	 * Background Async Task to get RSS Feed Items data from URL
	 * */
	class loadRSSFeedItems extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ListPodcastItemsActivity.this);
			pDialog.setMessage(getString(R.string.loading_recent));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting all recent articles and showing them in listview
		 * */
		@Override
		protected String doInBackground(String... args) {
			// rss link url
			String rss_url = args[0];

			// list of rss items
			rssItems = rssParser.getRSSFeedItems(rss_url);

			// looping through each item
			for (RSSItem item : rssItems) {
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				// adding each child node to HashMap key => value
				map.put(TAG_TITLE, item.getTitle());
				map.put(TAG_LINK, item.getLink());
				map.put(TAG_AUDIO, item.getAudioUrl());
				map.put(TAG_PUB_DATE, item.getPubdate()); // If you want parse
															// the date
				String description = item.getDescription();
				// taking only 200 chars from description
				if (description.length() > 100) {
					description = description.substring(0, 97) + "..";
				}
				map.put(TAG_DESRIPTION, description);

				// adding HashList to ArrayList
				rssItemList.add(map);
			}

			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed items into listview
					 * */
					ListAdapter adapter = new SimpleAdapter(
							ListPodcastItemsActivity.this, rssItemList,
							R.layout.rss_item_list_row, new String[] {
									TAG_LINK, TAG_TITLE, TAG_PUB_DATE,
									TAG_DESRIPTION, TAG_AUDIO }, new int[] {
									R.id.page_url, R.id.title, R.id.pub_date,
									R.id.link, R.id.audio_url });

					// updating listview
					setListAdapter(adapter);
				}
			});
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String args) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
		}
	}
}

package uk.co.patrickcunningham.podcatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.patrickcunningham.podcatcher.dao.PodcastDAO;
import uk.co.patrickcunningham.podcatcher.hardware.ShakeEventListener;
import uk.co.patrickcunningham.podcatcher.rss.Podcast;
import uk.co.patrickcunningham.podcatcher.rss.RSSFeed;
import uk.co.patrickcunningham.podcatcher.rss.RSSParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * PodcatcherActivity.java
 * 
 * Main activity of the application
 * 
 * Retrieves saved podcasts from local database adn creates a list view so that
 * podcast episodes can be parsed and played
 * 
 * Also provides onShakeListener so taht you can shake device to add new
 * podcasts
 * 
 * @author Patrick Cunningham <patrick.cunningham@bbc.co.uk>
 */
public class PodcatcherActivity extends Activity {

	private ProgressDialog pDialog;

	ArrayList<HashMap<String, String>> rssFeedList;

	RSSParser rssParser = new RSSParser();

	RSSFeed rssFeed;

	ImageButton addPodcastButton;

	// array to trace sqlite ids
	String[] sqliteIds;

	public static String TAG_ID = "id";
	public static String TAG_TITLE = "title";
	public static String TAG_LINK = "link";

	// List view
	ListView lv;

	private SensorManager mSensorManager;

	private ShakeEventListener mSensorListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.podcast_list);

		// Set up sensor to react to shake activity to add new podcast
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensorListener = new ShakeEventListener();

		mSensorListener
				.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

					public void onShake() {
						Toast.makeText(PodcatcherActivity.this, "Shake!",
								Toast.LENGTH_SHORT).show();
						Intent i = new Intent(getApplicationContext(),
								AddPodcastActivity.class);
						startActivityForResult(i, 100);
					}
				});

		addPodcastButton = (ImageButton) findViewById(R.id.addPodcastButton);

		// Hashmap for ListView
		rssFeedList = new ArrayList<HashMap<String, String>>();

		/**
		 * Background thread to load all POdcasts stored in local SQLite db
		 * */
		new loadStoreSites().execute();

		// selecting single ListView item
		lv = (ListView) findViewById(R.id.list);

		// Launching new screen on Selecting Single ListItem
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String sqlite_id = ((TextView) view
						.findViewById(R.id.sqlite_id)).getText().toString();
				// Create new intent to pass to Listing Activity
				Intent in = new Intent(getApplicationContext(),
						ListPodcastItemsActivity.class);
				// passing sqlite row id
				in.putExtra(TAG_ID, sqlite_id);
				startActivity(in);
			}
		});

		/**
		 * Add new podcast button and listener
		 * */
		addPodcastButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						AddPodcastActivity.class);
				startActivityForResult(i, 100);
			}
		});
	}

	/**
	 * Response from AddPodcastActivity.java if response is 100 means new site
	 * is added to sqlite adn therefore to listview
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if result code 100
		if (resultCode == 100) {
			// reload this screen again
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
	}

	/**
	 * Building a context menu for listview Long press to delete
	 * */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.list) {
			menu.setHeaderTitle("Delete");
			menu.add(Menu.NONE, 0, 0, "Delete Feed");
		}
	}

	/**
	 * Responding to context menu selected option
	 * */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		// check for selected option
		if (menuItemIndex == 0) {
			// user selected delete
			// delete the feed
			PodcastDAO rssDb = new PodcastDAO(getApplicationContext());
			Podcast podcast = new Podcast();
			podcast.setId(Integer.parseInt(sqliteIds[info.position]));
			rssDb.deletePodcast(podcast);
			// reloading same activity again
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}

		return true;
	}

	/**
	 * Background Task to get Podcast data from Url and progress dialog
	 * */
	class loadStoreSites extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PodcatcherActivity.this);
			pDialog.setMessage("Loading websites ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		// getting all stored podcasts from SQLite
		@Override
		protected String doInBackground(String... args) {
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					PodcastDAO rssDb = new PodcastDAO(getApplicationContext());

					// listing all podcasts from SQLite
					List<Podcast> podList = rssDb.getAllSites();

					sqliteIds = new String[podList.size()];

					// loop through each website
					for (int i = 0; i < podList.size(); i++) {

						Podcast s = podList.get(i);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_ID, s.getId().toString());
						map.put(TAG_TITLE, s.getTitle());
						map.put(TAG_LINK, s.getLink());

						// adding HashList to ArrayList
						rssFeedList.add(map);

						// add sqlite id to array
						// used when deleting a website from sqlite
						sqliteIds[i] = s.getId().toString();
					}
					/**
					 * Updating list view with websites
					 * */
					ListAdapter adapter = new SimpleAdapter(
							PodcatcherActivity.this, rssFeedList,
							R.layout.site_list_row, new String[] { TAG_ID,
									TAG_TITLE, TAG_LINK }, new int[] {
									R.id.sqlite_id, R.id.title, R.id.link });
					// updating listview
					lv.setAdapter(adapter);
					registerForContextMenu(lv);
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

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(mSensorListener);
		super.onPause();
	}

}

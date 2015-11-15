package uk.co.patrickcunningham.podcatcher;

import uk.co.patrickcunningham.podcatcher.dao.PodcastDAO;
import uk.co.patrickcunningham.podcatcher.rss.Podcast;
import uk.co.patrickcunningham.podcatcher.rss.RSSFeed;
import uk.co.patrickcunningham.podcatcher.rss.RSSParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * AddPodcastActivity.java
 * 
 * Activity for adding new podcast RSS feeds to the Podcatcher application
 * database
 * 
 * @author Patrick Cunningham <patrick.cunningham@bbc.co.uk>
 */
public class AddPodcastActivity extends Activity {

	Button btnSubmit;
	Button btnCancel;
	EditText txtUrl;
	TextView lblMessage;

	RSSParser rssParser = new RSSParser();

	RSSFeed rssFeed;

	// Progress Dialog
	private ProgressDialog pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.podcast_add);

		// buttons
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		txtUrl = (EditText) findViewById(R.id.txtUrl);
		lblMessage = (TextView) findViewById(R.id.lblMessage);

		// Submit button click event
		btnSubmit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String url = txtUrl.getText().toString();

				// Validation url
				Log.d("URL Length", "" + url.length());
				// check if user entered any data in EditText
				if (url.length() > 0) {
					lblMessage.setText("");
					String urlPattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
					if (url.matches(urlPattern)) {
						// valid url
						new loadRSSFeed().execute(url);
					} else {
						// URL not valid
						lblMessage.setText(R.string.valid_url);
					}
				} else {
					// Please enter url
					lblMessage.setText(R.string.enter_valid_url);
				}

			}
		});

		// Cancel button click event
		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * Background Async Task to get RSS data from URL
	 * */
	class loadRSSFeed extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AddPodcastActivity.this);
			pDialog.setMessage(getString(R.string.fetching_podcast));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Inbox JSON
		 * */
		@Override
		protected String doInBackground(String... args) {
			String url = args[0];
			rssFeed = rssParser.getRSSFeed(url);
			if (rssFeed != null) {
				PodcastDAO rssDb = new PodcastDAO(getApplicationContext());
				Podcast podcast = new Podcast(rssFeed.getTitle(),
						rssFeed.getLink(), rssFeed.getRssLink(),
						rssFeed.getDescription());
				rssDb.addSite(podcast);
				Intent i = getIntent();
				// send result code 100 to notify about product update
				setResult(100, i);
				finish();
			} else {
				// updating UI from Background Thread
				runOnUiThread(new Runnable() {
					public void run() {
						lblMessage.setText(R.string.url_not_found);
					}
				});
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String args) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					if (rssFeed != null) {

					}

				}
			});

		}

	}

}

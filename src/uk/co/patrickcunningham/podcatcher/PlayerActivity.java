package uk.co.patrickcunningham.podcatcher;

import uk.co.patrickcunningham.podcatcher.service.AudioPlayerService;
import uk.co.patrickcunningham.podcatcher.service.AudioPlayerService.LocalBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * PlayerActivity.java
 * 
 * Activity to bind to and load MediaPlayerService with details passed from ListPodcastItemsActivity
 * 
 * @author Patrick Cunningham <patrick.cunningham@bbc.co.uk>
 */
public class PlayerActivity extends Activity {

	private AudioPlayerService audioService;

	//bind to service
	private ServiceConnection audioServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder binder) {
			audioService = ((LocalBinder) binder).getService();

		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			audioService = null;
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_player);
		startService(new Intent(this, AudioPlayerService.class));
		Intent connectionIntent = new Intent(this, AudioPlayerService.class);

		final Intent podcastIntent = getIntent();
		// bind service and set contents of textViews
		bindService(connectionIntent, audioServiceConnection,
				Context.BIND_AUTO_CREATE);
		TextView titleTextView = (TextView) findViewById(R.id.textView1);
		titleTextView.setText(podcastIntent.getStringExtra("title"));
		TextView descriptionTextView = (TextView) findViewById(R.id.textView2);
		descriptionTextView
				.setText(podcastIntent.getStringExtra("description"));

		
		// play button and clickListener
		final ImageButton play_button = (ImageButton) findViewById(R.id.imageButton1);
		play_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				audioService.playSong(getBaseContext(),
						podcastIntent.getStringExtra("title"),
						podcastIntent.getStringExtra("description"),
						podcastIntent.getStringExtra("audioUrl"));
			}
		});
		
		// pause button and clickListener 
		final ImageButton pause_button = (ImageButton) findViewById(R.id.imageButton2);
		pause_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				audioService.pauseSong(getBaseContext());
			}
		});
		
		// stop button and listener
		final ImageButton stop_button = (ImageButton) findViewById(R.id.imageButton3);
		stop_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				audioService.stopSong(getBaseContext());
			}
		});
	}

	@Override
	protected void onDestroy() {
		unbindService(this.audioServiceConnection);
		super.onDestroy();
	}

}
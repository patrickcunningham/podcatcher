package uk.co.patrickcunningham.podcatcher.service;

import java.io.IOException;

import uk.co.patrickcunningham.podcatcher.PlayerActivity;
import uk.co.patrickcunningham.podcatcher.R;
import uk.co.patrickcunningham.podcatcher.R.drawable;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class AudioPlayerService extends Service {
	public final IBinder localBinder = new LocalBinder();
	private MediaPlayer mplayer = null;
	private boolean created = false;

	@Override
	public IBinder onBind(Intent intent) {
		return localBinder;
	}

	@Override
	public void onCreate() {

	}

	public class LocalBinder extends Binder {
		public AudioPlayerService getService() {
			return AudioPlayerService.this;
		}
	}

	public void playSong(Context c, String title, String description, String url) {
		if (this.mplayer != null) {
			this.mplayer.release();
		}
		this.mplayer = new MediaPlayer(); // change this for your file
		this.mplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			this.mplayer.setDataSource(url);

		} catch (IllegalArgumentException e) {
			Toast.makeText(getApplicationContext(),
					"You might not set the URI correctly!", Toast.LENGTH_LONG)
					.show();
		} catch (SecurityException e) {
			Toast.makeText(getApplicationContext(),
					"You might not set the URI correctly!", Toast.LENGTH_LONG)
					.show();
		} catch (IllegalStateException e) {
			Toast.makeText(getApplicationContext(),
					"You might not set the URI correctly!", Toast.LENGTH_LONG)
					.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			this.mplayer.prepareAsync();
			// this.mplayer.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

		created = true;
		// testing this segment
		this.mplayer
				.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
					public void onPrepared(MediaPlayer player) {
						player.start();
					}
				});
		// this.mplayer.start();

		// notification
		Intent notIntent = new Intent(this, PlayerActivity.class);
		notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendInt = PendingIntent
				.getActivity(this, 0, notIntent, 0);

		Notification.Builder builder = new Notification.Builder(this);

		builder.setContentIntent(pendInt).setSmallIcon(R.drawable.play)
				.setOngoing(true).setContentTitle(title)
				.setContentText(description);
		Notification not = builder.build();
		startForeground(001, not);

	}

	public void pauseSong(Context c) {
		this.mplayer.pause();
	}

	public void stopSong(Context c) {
		// TODO:
		if (this.mplayer != null && this.mplayer.isPlaying()) {
			this.mplayer.stop();
			this.mplayer.release();
		}
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(001);
	}

}
package uk.co.patrickcunningham.podcatcher.service;

import java.io.IOException;

import uk.co.patrickcunningham.podcatcher.ListPodcastItemsActivity;
import uk.co.patrickcunningham.podcatcher.R;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MediaPlayerService extends Service {

	private MediaPlayer mediaPlayer = null;
	private boolean      isPlaying = false;

	private static int classID = 579; // just a number
	
	public static String START_PLAY = "START_PLAY";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent.getBooleanExtra(START_PLAY, false)) {
			//play();	
			if (!isPlaying) {			
				isPlaying = true;			
				
				Intent in = new Intent(this, ListPodcastItemsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
								Intent.FLAG_ACTIVITY_SINGLE_TOP);

				PendingIntent pi = PendingIntent.getActivity(this, 0, in, 0);
				Log.d("MP Intents", intent.getExtras().toString());
				Notification notification = new Notification.Builder(getApplicationContext())
	         	.setContentTitle(intent.getStringExtra("contentTitle"))
	         	.setContentText(intent.getStringExtra("contentText"))
	         	.setSmallIcon(R.drawable.ic_launcher)
	         	.setContentIntent(pi)
	         	.build();
				
				if (mediaPlayer != null) mediaPlayer.release();
				mediaPlayer = new MediaPlayer(); // change this for your file
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);	
				try {
					//String pod_url = ((TextView) view.findViewById(R.id.audio_url)).getText().toString();
					//Log.d("pod URL: ", pod_url);
					//mediaPlayer.setDataSource("http://feedproxy.google.com/~r/Secondcaptainsitcom/~5/RQbRNBkcTEU/165230283-secondcaptains-it-com-second-captains-2908-david-baddiel-250th-show-special.mp3");
					mediaPlayer.setDataSource(intent.getStringExtra("audioUrl"));
				} catch (IllegalArgumentException e) {
					Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
				} catch (SecurityException e) {
					Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
				} catch (IllegalStateException e) {
					Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					mediaPlayer.prepare();
				} catch (IllegalStateException e) {
					Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
				}
				// mediaPlayer.setLooping(true); // this will make it loop forever
				mediaPlayer.start();
				
				startForeground(classID, notification);
			}
		}
		return Service.START_STICKY;	
	}

	private void play() {
		
	}

	@Override
	public void onDestroy() {
		stop();
	}	
	
	private void stop() {
		if (isPlaying) {
			isPlaying = false;
			if (mediaPlayer != null) {
				mediaPlayer.release();
				mediaPlayer = null;
			}
			stopForeground(true);
		}
	}
	
}

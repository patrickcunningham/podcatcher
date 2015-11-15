package uk.co.patrickcunningham.podcatcher.dao;

import java.util.ArrayList;
import java.util.List;

import uk.co.patrickcunningham.podcatcher.rss.Podcast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * PodcastDAO.java
 * 
 * Podcast Data Access Object for CRUD actions to database
 * 
 * @author Patrick Cunningham <patrick.cunningham@bbc.co.uk>
 */
public class PodcastDAO extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "podcatcher";

	// Contacts table name
	private static final String TABLE_NAME = "podcasts";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_LINK = "link";
	private static final String KEY_RSS_LINK = "rss_link";
	private static final String KEY_DESCRIPTION = "description";

	public PodcastDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_RSS_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," + KEY_LINK
				+ " TEXT," + KEY_RSS_LINK + " TEXT," + KEY_DESCRIPTION
				+ " TEXT" + ")";
		db.execSQL(CREATE_RSS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Adding a new website in websites table Function will check if a site
	 * already existed in database. If existed will update the old one else
	 * creates a new row
	 * */
	public void addSite(Podcast podcast) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, podcast.getTitle()); // podcast title
		values.put(KEY_LINK, podcast.getLink()); // podcast url
		values.put(KEY_RSS_LINK, podcast.getRssLink()); // rss link url
		values.put(KEY_DESCRIPTION, podcast.getDescription()); // podcast
																// description

		// Check if row already existed in database
		if (!ifPodcastExists(db, podcast.getRssLink())) {
			// podcast not existed, create a new row
			db.insert(TABLE_NAME, null, values);
			db.close();
		} else {
			// podcast already existed update the row
			updateSite(podcast);
			db.close();
		}
	}

	/**
	 * Reading all rows from database
	 * */
	public List<Podcast> getAllSites() {
		List<Podcast> podcastList = new ArrayList<Podcast>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NAME
				+ " ORDER BY id DESC";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Podcast podcast = new Podcast();
				podcast.setId(Integer.parseInt(cursor.getString(0)));
				podcast.setTitle(cursor.getString(1));
				podcast.setLink(cursor.getString(2));
				podcast.setRssLink(cursor.getString(3));
				podcast.setDescription(cursor.getString(4));
				// Adding contact to list
				podcastList.add(podcast);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		// return contact list
		return podcastList;
	}

	/**
	 * Updating a single row row will be identified by rss link
	 * */
	public int updateSite(Podcast podcast) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, podcast.getTitle());
		values.put(KEY_LINK, podcast.getLink());
		values.put(KEY_RSS_LINK, podcast.getRssLink());
		values.put(KEY_DESCRIPTION, podcast.getDescription());

		// updating row return
		int update = db.update(TABLE_NAME, values, KEY_RSS_LINK + " = ?",
				new String[] { String.valueOf(podcast.getRssLink()) });
		db.close();
		return update;

	}

	/**
	 * Reading a row (website) row is identified by row id
	 * */
	public Podcast getPodcast(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_TITLE,
				KEY_LINK, KEY_RSS_LINK, KEY_DESCRIPTION }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Podcast podcast = new Podcast(cursor.getString(1), cursor.getString(2),
				cursor.getString(3), cursor.getString(4));

		podcast.setId(Integer.parseInt(cursor.getString(0)));
		podcast.setTitle(cursor.getString(1));
		podcast.setLink(cursor.getString(2));
		podcast.setRssLink(cursor.getString(3));
		podcast.setDescription(cursor.getString(4));
		cursor.close();
		db.close();
		return podcast;
	}

	/**
	 * Deleting single row
	 * */
	public void deletePodcast(Podcast podcast) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, KEY_ID + " = ?",
				new String[] { String.valueOf(podcast.getId()) });
		db.close();
	}

	/**
	 * Checking whether a site is already existed check is done by matching rss
	 * link
	 * */
	public boolean ifPodcastExists(SQLiteDatabase db, String rssLink) {

		Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_NAME
				+ " WHERE rss_link = '" + rssLink + "'", new String[] {});
		boolean exists = (cursor.getCount() > 0);
		return exists;
	}

}

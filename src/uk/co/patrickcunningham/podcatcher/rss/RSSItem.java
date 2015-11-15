package uk.co.patrickcunningham.podcatcher.rss;

/**
 * RSSItem.java
 * 
 * RSSItem pojo for mapping Podcast episodes to ListView items
 * 
 * @author Patrick Cunningham <patrick.cunningham@bbc.co.uk>
 */
public class RSSItem {

	// All <item> node name
	String title;
	String link;
	String audioUrl;
	String description;
	String pubdate;
	String guid;

	// constructor
	public RSSItem() {

	}

	// constructor with parameters
	public RSSItem(String title, String link, String audioUrl,
			String description, String pubdate, String guid) {
		this.title = title;
		this.link = link;
		this.audioUrl = audioUrl;
		this.description = description;
		this.pubdate = pubdate;
		this.guid = guid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getAudioUrl() {
		return audioUrl;
	}

	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

}

package uk.co.patrickcunningham.podcatcher.rss;

/**
 * Podcast.java
 * 
 * Podcast pojo for storing in SQLite db
 * 
 * @author Patrick Cunningham <patrick.cunningham@bbc.co.uk>
 */
public class Podcast {

	Integer id;
	String title;
	String link;
	String rssLink;
	String description;

	public Podcast() {
	}

	public Podcast(String title, String link, String rssLink, String description) {
		this.title = title;
		this.link = link;
		this.rssLink = rssLink;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getRssLink() {
		return rssLink;
	}

	public void setRssLink(String rssLink) {
		this.rssLink = rssLink;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

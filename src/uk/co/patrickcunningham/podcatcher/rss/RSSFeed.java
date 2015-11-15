package uk.co.patrickcunningham.podcatcher.rss;

import java.util.List;

/**
 * RSSFeed.java
 * 
 * RSSFeed pojo used in parsing Podcast RSS Feed data
 * 
 * @author Patrick Cunningham <patrick.cunningham@bbc.co.uk>
 */
public class RSSFeed {

	String title;
	String description;
	String link;
	String rssLink;
	String language;
	List<RSSItem> items;

	public RSSFeed(String title, String description, String link,
			String rssLink, String language) {
		this.title = title;
		this.description = description;
		this.link = link;
		this.rssLink = rssLink;
		this.language = language;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<RSSItem> getItems() {
		return items;
	}

	public void setItems(List<RSSItem> items) {
		this.items = items;
	}

}

package com.bridgelabz.notemicroservice.model;

import org.jsoup.nodes.Element;

public class ScrapLinkDTO {

	private String title;
	private String link;
	private Element imageLink;
	
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
	public Element getImageLink() {
		return imageLink;
	}
	public void setImageLink(Element imageLink) {
		this.imageLink = imageLink;
	}
	
}

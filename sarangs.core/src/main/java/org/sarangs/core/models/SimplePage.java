package org.sarangs.core.models;


public class SimplePage{
	public String title;
	public String path;
	public String author;
	public String date;
	public String content;
	public String pTitle;
	public int seq;
	public String seqOdd;
	public String subTitle;
	public String thum;
	public String extraUrl;
	
	
	public String getTitle() {
		return title;
	}
	
	public String getPtitle() {
		return pTitle;
	}
	
	public String getSubTitle() {
		return subTitle;
	}
	
	public String getExtraUrl() {
		return extraUrl;
	}
	
	
	
	public String getThum() {
		return thum;
	}
	
	
	public String getPath() {
		return path;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setSubTitle(String subtitle) {
		this.subTitle = subtitle;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	public void setThum(String thum) {
		this.thum = thum;
	}
	
	public void setPtitle(String pTitle) {
		this.pTitle = pTitle;
	}
	
	public void setSeqOdd(String seqOdd) {
		this.seqOdd = seqOdd;
	}
	
	public void setextraUrl(String extraUrl) {
		this.extraUrl = extraUrl;
	}
	
}
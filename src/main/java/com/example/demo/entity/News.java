package com.example.demo.entity;

import com.example.demo.config.CustomJsonDateDeserializer;
import java.util.Arrays;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class News {
	private String desc;
	
	private String pvnum;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	private Date createdate;
	
	private String scover;
	
	private String setname;
	
	private String cover;
	
	private String[] pics;
	
	private String clientcover1;
	
	private String replynum;
	
	private String topicname;
	
	private Long setid;
	
	private String seturl;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	private Date datetime;
	
	private String clientcover;
	
	private String imgsum;
	
	private String tcover;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPvnum() {
		return pvnum;
	}

	public void setPvnum(String pvnum) {
		this.pvnum = pvnum;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getScover() {
		return scover;
	}

	public void setScover(String scover) {
		this.scover = scover;
	}

	public String getSetname() {
		return setname;
	}

	public void setSetname(String setname) {
		this.setname = setname;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String[] getPics() {
		return pics;
	}

	public void setPics(String[] pics) {
		this.pics = pics;
	}

	public String getClientcover1() {
		return clientcover1;
	}

	public void setClientcover1(String clientcover1) {
		this.clientcover1 = clientcover1;
	}

	public String getReplynum() {
		return replynum;
	}

	public void setReplynum(String replynum) {
		this.replynum = replynum;
	}

	public String getTopicname() {
		return topicname;
	}

	public void setTopicname(String topicname) {
		this.topicname = topicname;
	}

	public Long getSetid() {
		return setid;
	}

	public void setSetid(Long setid) {
		this.setid = setid;
	}

	public String getSeturl() {
		return seturl;
	}

	public void setSeturl(String seturl) {
		this.seturl = seturl;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getClientcover() {
		return clientcover;
	}

	public void setClientcover(String clientcover) {
		this.clientcover = clientcover;
	}

	public String getImgsum() {
		return imgsum;
	}

	public void setImgsum(String imgsum) {
		this.imgsum = imgsum;
	}

	public String getTcover() {
		return tcover;
	}

	public void setTcover(String tcover) {
		this.tcover = tcover;
	}

	@Override
	public String toString() {
		return "News [desc=" + desc + ", pvnum=" + pvnum + ", createdate=" + createdate + ", scover=" + scover
				+ ", setname=" + setname + ", cover=" + cover + ", pics=" + Arrays.toString(pics) + ", clientcover1="
				+ clientcover1 + ", replynum=" + replynum + ", topicname=" + topicname + ", setid=" + setid
				+ ", seturl=" + seturl + ", datetime=" + datetime + ", clientcover=" + clientcover + ", imgsum="
				+ imgsum + ", tcover=" + tcover + "]";
	}

	
}

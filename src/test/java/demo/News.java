package demo;


import java.util.Arrays;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class News {
	private String desc;
	
	private String pvnum;

	private Date createDate;
	
	private String scover;
	
	private String setname;
	
	private String cover;
	
	private String[] pics;
	
	private String clientCover1;
	
	private String replyNum;
	
	private String topicName;
	
	private Long setId;
	
	private String setUrl;
	
	private Date dateTime;
	
	private String clientCover;
	
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public String getClientCover1() {
		return clientCover1;
	}

	public void setClientCover1(String clientCover1) {
		this.clientCover1 = clientCover1;
	}

	public String getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(String replyNum) {
		this.replyNum = replyNum;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public Long getSetId() {
		return setId;
	}

	public void setSetId(Long setId) {
		this.setId = setId;
	}

	public String getSetUrl() {
		return setUrl;
	}

	public void setSetUrl(String setUrl) {
		this.setUrl = setUrl;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getClientCover() {
		return clientCover;
	}

	public void setClientCover(String clientCover) {
		this.clientCover = clientCover;
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
		return "News [desc=" + desc + ", pvnum=" + pvnum + ", createDate=" + createDate + ", scover=" + scover
				+ ", setname=" + setname + ", cover=" + cover + ", pics=" + Arrays.toString(pics) + ", clientCover1="
				+ clientCover1 + ", replyNum=" + replyNum + ", topicName=" + topicName + ", setId=" + setId
				+ ", setUrl=" + setUrl + ", dateTime=" + dateTime + ", clientCover=" + clientCover + ", imgsum="
				+ imgsum + ", tcover=" + tcover + "]";
	}
	
}

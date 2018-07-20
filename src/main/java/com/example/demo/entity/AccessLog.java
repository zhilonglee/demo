package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tbl_access_log")
@Entity
public class AccessLog {

	@Id
	@GeneratedValue
	@Column(columnDefinition = "bigint comment 'primary key'")
	private Long id;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy MM dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy MM dd HH:mm:ss")
	@Column(columnDefinition = "datetime comment 'create date'")
	private Date createDate;

	@Column(columnDefinition = "varchar(255) comment 'ip address'")
	private String ipaddress;

	@Column(columnDefinition = "varchar(255) comment 'access url'")
	private String url;

	public AccessLog() {
		this.createDate = new Date();
	}

	public String formatDataReport() {
		return this.id + "," + this.createDate.toString() + "," + this.ipaddress + "," + this.url;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public Long getId() {
		return this.id;
	}

	public String getIpaddress() {
		return this.ipaddress;
	}

	public String getUrl() {
		return this.url;
	}

	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setIpaddress(final String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "AccessLog [id=" + this.id + ", createDate=" + this.createDate + ", ipaddress=" + this.ipaddress
				+ ", url=" + this.url + "]";
	}

}

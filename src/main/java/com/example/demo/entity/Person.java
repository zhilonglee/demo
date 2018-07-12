package com.example.demo.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Table(name = "tbl_person")
@Entity
@NamedQueries({
		@NamedQuery(name = "Person.withNameAndAddressNamedQuery", query = "select p from Person p where p.name=?1 and p.address=?2") })
@DynamicUpdate
public class Person implements Serializable {
	@Id
	@GeneratedValue
	@Column(columnDefinition = "bigint comment 'person primary key'")
	private Long id;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy MM dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy MM dd HH:mm:ss")
	@Column(columnDefinition = "datetime comment 'create date'")
	private Date createDate;

	@Column(columnDefinition = "varchar(50) comment 'person name'")
	private String name;

	@Column(columnDefinition = "int comment 'person age'")
	private Integer age;

	@Column(columnDefinition = "varchar(255) comment 'person address'")
	private String address;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy MM dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy MM dd HH:mm:ss")
	@Column(columnDefinition = "datetime comment 'person birth day'")
	private Date birthDay;

	@Transient
	private String sessionCSRF;

	public Person() {
		this.createDate = new Date();
	}

	public Person(final Long id, final String name, final Integer age, final String address) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.address = address;
	}

	public Person addBirthDay(final Date birthDay) {
		this.setBirthDay(birthDay);
		return this;
	}

	public String formatDataReport() {
		return this.id + "," + this.createDate + "," + this.name + "," + this.age + "," + this.address + ","
				+ this.birthDay;
	}

	public String getAddress() {
		return this.address;
	}

	public Integer getAge() {
		return this.age;
	}

	public Date getBirthDay() {
		return this.birthDay;
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getSessionCSRF() {
		return this.sessionCSRF;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public void setAge(final Integer age) {
		this.age = age;
	}

	public void setBirthDay(final Date birthDay) {
		this.birthDay = birthDay;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setSessionCSRF(final String sessionCSRF) {
		this.sessionCSRF = sessionCSRF;
	}

	@Override
	public String toString() {
		return "Person [id=" + this.id + ", createDate=" + this.createDate + ", name=" + this.name + ", age=" + this.age
				+ ", address=" + this.address + ", birthDay=" + this.birthDay + ", sessionCSRF=" + this.sessionCSRF
				+ "]";
	}

}

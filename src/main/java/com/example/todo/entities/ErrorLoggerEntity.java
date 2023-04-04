package com.example.todo.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class ErrorLoggerEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ErrorLoggerEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ErrorLoggerEntity(long id, String message, String url, MethodEnum method, String host, String body,
			String token) {
		super();
		this.id = id;
		this.message = message;
		this.url = url;
		this.method = method;
		this.host = host;
		this.body = body;
		this.token = token;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "message")
	private String message;

	@Column(name = "url")
	private String url;

	@Column(name = "method")
	@Enumerated(EnumType.STRING)
	private MethodEnum method;

	@Column(name = "host")
	private String host;

	@Column(name = "body")
	private String body;

	@Column(name = "token")
	private String token;

	@Column(name = "created_At")
	@CreationTimestamp
	private Date createdAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public MethodEnum getMethod() {
		return method;
	}

	public void setMethod(MethodEnum method) {
		this.method = method;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "ErrorLoggerEntity [id=" + id + ", message=" + message + ", url=" + url + ", method=" + method
				+ ", host=" + host + ", body=" + body + ", token=" + token + "]";
	}

}

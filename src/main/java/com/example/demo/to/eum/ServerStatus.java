package com.example.demo.to.eum;

public enum ServerStatus {
	OK(200),
	NOCONTENT(204),
	PARTIALCONTENT(206),
	REDIRECT(302),
	BADREQUEST(400),
	UNAUTHORIZED(401),
	FORBIDDEN(403),
	INTERNALSERVERERROR(500);
	
	private int code;
	ServerStatus(int code){
		this.code = code;
		
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

}

package com.example.demo.to;


import com.example.demo.to.eum.ServerStatus;

public class ToResult {
	
	public final static String REQUESTOK = "Request successfully!";
	public final static String BEDREQUEST = "Wrong request!";
	public final static String REQUESTREDIRECT = "Redirect.";
	public final static String CONSTRAINTERROR = "constraint error!";
	String message;
	int  code;
	String detail;
	Object data;
	
	private static ToResult result = null;
	
	private ToResult(String message, int code){
		this.message = message;
		this.code = code;
		
	}
	
	public static ToResult buildToResult(){
		if(result == null){
			result = new ToResult(REQUESTOK, ServerStatus.OK.getCode());
		return result;
		}else{
		return result.removeExtraAttr().setToResult(REQUESTOK, ServerStatus.OK.getCode());
		}
		
	}
	
	private ToResult setToResult(String message, int code) {
		result.setMessage(message);
		result.setCode(code);
		return result;
	}

	public static ToResult buildToResult(String message,ServerStatus status){
		if(result == null){
			result = new ToResult(message, status.getCode());
		return result;
		}else{
		return result.removeExtraAttr().setToResult(message, status.getCode());
		}
		
	}
	
	public static ToResult buildBadRequestToResult(){
		if(result == null){
			result = new ToResult(BEDREQUEST, ServerStatus.BADREQUEST.getCode());
		return result;
		}else{
		return result.removeExtraAttr().setToResult(BEDREQUEST, ServerStatus.BADREQUEST.getCode());
		}
		
	}

	public ToResult addDetail(String detail){
		result.setDetail(detail);
		return result;
	}
	
	public ToResult addData(Object data){
		result.setData(data);
		return result;
	}
	private ToResult removeExtraAttr(){
		result.setDetail(null);
		result.setData(null);
		return result;
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}


	
	

}

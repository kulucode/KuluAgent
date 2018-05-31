package com.tpson.kuluagent.VO;

public class ResultVO {
	private boolean success;
	private String message;
	private int code;
	private Object data;
	
	public ResultVO(boolean success, String message, int code, Object data) {
		this.success = success;
		this.message = message;
		this.code = code;
		this.data = data;
	}
	
	public static ResultVO successResult() {
		return new ResultVO(true, "成功", 0, null);
	}
	
	public static ResultVO successResult(Object data) {
		return new ResultVO(true, "成功", 0, data);
	}
	
	public static ResultVO failResult(String message) {
		return new ResultVO(false, message, -1, null);
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}

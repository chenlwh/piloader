package com.ygsoft.matcloud.piloader.util;

public class MessageInfo {
	private boolean success;
	private String message;
	private String msg;
	private Object data;

	public MessageInfo() {
		success = false;
		message = "";
		msg = "";
	}

	public MessageInfo(boolean success, String msg, Object data) {
		this.success = false;
		message = "";
		this.msg = "";
		this.success = success;
		message = msg;
		this.msg = msg;
		this.data = data;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String msg) {
		this.msg = msg;
		message = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object object) {
		data = object;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
		message = msg;
	}


}

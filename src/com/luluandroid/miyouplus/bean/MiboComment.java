package com.luluandroid.miyouplus.bean;

import cn.bmob.v3.BmobObject;

public class MiboComment extends BmobObject {

	private Mibos miboParent;
	private String content;
	private String UserName;
	
	public MiboComment() {
		super();
	}

	public MiboComment(Mibos miboParent, String content, String userName) {
		super();
		this.miboParent = miboParent;
		this.content = content;
		UserName = userName;
	}
	
	public Mibos getMiboParent() {
		return miboParent;
	}
	public void setMiboParent(Mibos miboParent) {
		this.miboParent = miboParent;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	
}

package com.luluandroid.miyouplus.bean;

import cn.bmob.v3.BmobObject;

public class MiboComment extends BmobObject {

	private Mibos miboParent;
	private String content;
//	private String toUserName;//接受者姓名
	private String fromUserName;//发送者姓名
	private String fromUserId;//发送者Id
	
	public MiboComment() {
		super();
	}
	
	public MiboComment(Mibos miboParent, String content, String fromUserName,
			String fromUserId) {
		super();
		this.miboParent = miboParent;
		this.content = content;
		this.fromUserName = fromUserName;
		this.fromUserId = fromUserId;
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
	/*public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}*/

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	
}

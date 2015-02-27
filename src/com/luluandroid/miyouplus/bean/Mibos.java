package com.luluandroid.miyouplus.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.luluandroid.miyouplus.R;
import com.luluandroid.miyouplus.config.BmobConstants;

import android.graphics.Bitmap;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class Mibos extends BmobObject implements Serializable {

	/**
	 * 秘博类的SerializableID 必须的
	 */
	private static final long serialVersionUID = 1400147433532249266L;
	private User myUser;//关联的秘博用户
	private String headUserName="";//*用户名
	private String content = "";//*秘博的内容
	private Integer favorCount = 0;//*秘博的赞数
	private Integer CommentCount = 0;//*秘博的评论数
	private boolean isOpentoAll = true;//判断秘博是否对所有用户可见
	private boolean friendCommentOnly = true;//判断秘博是否只让朋友阅读
	private MessageType type = MessageType.TEXT;//判断秘博是否包含图片, 默认不包含图片
	private int parentId = -1;//秘博的父ObjectId（Bmob）-1 代表为根贴
	private BmobFile pic = null;//秘博的网络图片对象
	private String localPicName = null;//如果秘博有图片，则为该秘博的图片名称，否则为空
	private BmobRelation Comments;
	private List<String> zanMan = new ArrayList<String>();//点赞的人的ObjectId
	private int PicResourceId = R.drawable.l_2;//默认用户图片使用app的ID为l_2图片，与localPicName不共存，则该ID还是存在，否则为-1

	public enum MessageType {
		TEXT, TEXTANDPICTURE;
	}

	public Mibos() {
	}

	// 默认的用的构造
	public Mibos(String headUserName,String content, Integer favorCount) {
		super();
		this.headUserName = headUserName;
		this.content = content;
		this.favorCount = favorCount;
		//设置bmob服务器上的表格名称
	}
	
	public User getMyUser() {
		return myUser;
	}

	public void setMyUser(User myUser) {
		this.myUser = myUser;
	}

	/**
	 * @return the favorCount
	 */
	public Integer getFavorCount() {
		return favorCount;
	}

	/**
	 * @param favorCount
	 *            the favorCount to set
	 */
	public void setFavorCount(Integer favorCount) {
		this.favorCount = favorCount;
	}

	public Integer getCommentCount() {
		return CommentCount;
	}

	public void setCommentCount(Integer commentCount) {
		CommentCount = commentCount;
	}

	/**
	 * @return the parentId
	 */
	public int getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the zanCount
	 */
	public Integer getZanCount() {
		return favorCount;
	}

	/**
	 * @param zanCount
	 *            the zanCount to set
	 */
	public void setZanCount(Integer zanCount) {
		this.favorCount = zanCount;
	}

	/**
	 * @return the isOpentoAll
	 */
	public boolean isOpentoAll() {
		return isOpentoAll;
	}

	/**
	 * @param isOpentoAll
	 *            the isOpentoAll to set
	 */
	public void setOpentoAll(boolean isOpentoAll) {
		this.isOpentoAll = isOpentoAll;
	}

	/**
	 * @return the friendCommentOnly
	 */
	public boolean isFriendCommentOnly() {
		return friendCommentOnly;
	}

	/**
	 * @param friendCommentOnly
	 *            the friendCommentOnly to set
	 */
	public void setFriendCommentOnly(boolean friendCommentOnly) {
		this.friendCommentOnly = friendCommentOnly;
	}

	/**
	 * @return the pic
	 */
	public BmobFile getPic() {
		return pic;
	}

	/**
	 * @param pic
	 *            the pic to set
	 */
	public void setPic(BmobFile pic) {
		this.pic = pic;
	}

	/**
	 * @return the type
	 */
	public MessageType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(MessageType type) {
		this.type = type;
	}

	/**
	 * @return the headUserName
	 */
	public String getHeadUserName() {
		return headUserName;
	}

	/**
	 * @param headUserName the headUserName to set
	 */
	public void setHeadUserName(String headUserName) {
		this.headUserName = headUserName;
	}

	public int getPicResourceId() {
		return this.PicResourceId;
	}

	public void setPicResourceId(int picResourceId) {
		this.PicResourceId = picResourceId;
		if(this.PicResourceId != -1){
			setLocalPicName(null);
			setType(MessageType.TEXT);
		}
	}

	public String getLocalPicName() {
		return localPicName;
	}

	public void setLocalPicName(String localPicName) {
		this.localPicName = localPicName;
		if(this.localPicName != null){
			setPicResourceId(-1);
			setType(MessageType.TEXTANDPICTURE);
		}
	}

	public BmobRelation getComments() {
		return Comments;
	}

	public void setComments(BmobRelation comments) {
		Comments = comments;
	}
	
	public List<String> getZanMan() {
		return zanMan;
	}

	public void setZanMan(List<String> zanMan) {
		this.zanMan = zanMan;
	}

	public void addZanMan(String ObjectId){
		zanMan.add(ObjectId);
	}
	
	public void deleteZanMan(String ObjectId){
		for(int i = 0;i<zanMan.size();i++){
			if(ObjectId.equals(zanMan.get(i))){
				zanMan.remove(i);
			}
		}
	}
}

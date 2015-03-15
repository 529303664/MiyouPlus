package com.luluandroid.miyouplus.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

public class Mibos extends BmobObject implements Serializable {

	/**
	 * 秘博类的SerializableID 必须的
	 */
	private static final long serialVersionUID = 1400147433532249266L;
	private String content;//*秘博的内容
	private Integer favorCount;//*秘博的赞数
	private String fromUserId;//*发送秘博的用户ObjectId
	private Integer CommentCount;//*秘博的评论数
	private Integer reportCount;//*秘博的举报数 
	private String tag;//*标签
	private boolean isOpentoAll;//判断秘博是否对可以聊天
	private boolean isCommentOk;//判断秘博是否可以评论
	private BmobFile pic;//秘博的网络图片对象
	private String localPicName;//如果秘博有图片，则为该秘博的图片名称，否则为空
	private BmobRelation Comments;
	private BmobGeoPoint location;//秘博发帖的位置
	private List<String> zanMan;//点赞的人的ObjectId
	private int PicResourceId;//默认用户图片使用app的ID为l_2图片，与localPicName不共存，则该ID还是存在，否则为-1

	public enum MessageType {
		TEXT, TEXTANDPICTURE;
	}

	public Mibos() {
	}

	// 默认的用的构造
	public Mibos(String content, Integer favorCount,String fromUserId,String tag) {
		super();
		this.content = content;
		this.favorCount = favorCount;
		this.fromUserId = fromUserId;
		this.tag = tag;
		this.CommentCount = 0;
		this.reportCount = 0;
		this.isOpentoAll = true;
		this.isCommentOk = true;
		this.pic = null;
		this.localPicName = null;
		this.zanMan = new ArrayList<String>();
		this.PicResourceId = 1;
		location = null;
		//设置bmob服务器上的表格名称
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
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

	public Integer getReportCount() {
		return reportCount;
	}

	public void setReportCount(Integer reportCount) {
		this.reportCount = reportCount;
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
	public boolean isCommentOk() {
		return isCommentOk;
	}

	/**
	 * @param friendCommentOnly
	 *            the friendCommentOnly to set
	 */
	public void setCommentOk(boolean friendCommentOnly) {
		this.isCommentOk = friendCommentOnly;
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

	public int getPicResourceId() {
		return this.PicResourceId;
	}

	public void setPicResourceId(int picResourceId) {
		this.PicResourceId = picResourceId;
		if(this.PicResourceId != -1){
			setLocalPicName(null);
		}
	}

	public String getLocalPicName() {
		return localPicName;
	}

	public void setLocalPicName(String localPicName) {
		this.localPicName = localPicName;
		if(this.localPicName != null){
			setPicResourceId(-1);
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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public BmobGeoPoint getLocation() {
		return location;
	}

	public void setLocation(BmobGeoPoint location) {
		this.location = location;
	}
	
}

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
	 * �ز����SerializableID �����
	 */
	private static final long serialVersionUID = 1400147433532249266L;
	private String content;//*�ز�������
	private Integer favorCount;//*�ز�������
	private String fromUserId;//*�����ز����û�ObjectId
	private Integer CommentCount;//*�ز���������
	private Integer reportCount;//*�ز��ľٱ��� 
	private String tag;//*��ǩ
	private boolean isOpentoAll;//�ж��ز��Ƿ�Կ�������
	private boolean isCommentOk;//�ж��ز��Ƿ��������
	private BmobFile pic;//�ز�������ͼƬ����
	private String localPicName;//����ز���ͼƬ����Ϊ���ز���ͼƬ���ƣ�����Ϊ��
	private BmobRelation Comments;
	private BmobGeoPoint location;//�ز�������λ��
	private List<String> zanMan;//���޵��˵�ObjectId
	private int PicResourceId;//Ĭ���û�ͼƬʹ��app��IDΪl_2ͼƬ����localPicName�����棬���ID���Ǵ��ڣ�����Ϊ-1

	public enum MessageType {
		TEXT, TEXTANDPICTURE;
	}

	public Mibos() {
	}

	// Ĭ�ϵ��õĹ���
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
		//����bmob�������ϵı������
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

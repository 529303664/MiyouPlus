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
	 * �ز����SerializableID �����
	 */
	private static final long serialVersionUID = 1400147433532249266L;
	private User myUser;//�������ز��û�
	private String headUserName="";//*�û���
	private String content = "";//*�ز�������
	private Integer favorCount = 0;//*�ز�������
	private Integer CommentCount = 0;//*�ز���������
	private boolean isOpentoAll = true;//�ж��ز��Ƿ�������û��ɼ�
	private boolean friendCommentOnly = true;//�ж��ز��Ƿ�ֻ�������Ķ�
	private MessageType type = MessageType.TEXT;//�ж��ز��Ƿ����ͼƬ, Ĭ�ϲ�����ͼƬ
	private int parentId = -1;//�ز��ĸ�ObjectId��Bmob��-1 ����Ϊ����
	private BmobFile pic = null;//�ز�������ͼƬ����
	private String localPicName = null;//����ز���ͼƬ����Ϊ���ز���ͼƬ���ƣ�����Ϊ��
	private BmobRelation Comments;
	private List<String> zanMan = new ArrayList<String>();//���޵��˵�ObjectId
	private int PicResourceId = R.drawable.l_2;//Ĭ���û�ͼƬʹ��app��IDΪl_2ͼƬ����localPicName�����棬���ID���Ǵ��ڣ�����Ϊ-1

	public enum MessageType {
		TEXT, TEXTANDPICTURE;
	}

	public Mibos() {
	}

	// Ĭ�ϵ��õĹ���
	public Mibos(String headUserName,String content, Integer favorCount) {
		super();
		this.headUserName = headUserName;
		this.content = content;
		this.favorCount = favorCount;
		//����bmob�������ϵı������
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

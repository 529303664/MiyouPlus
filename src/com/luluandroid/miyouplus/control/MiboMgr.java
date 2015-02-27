package com.luluandroid.miyouplus.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.luluandroid.miyouplus.bean.MiboComment;
import com.luluandroid.miyouplus.bean.Mibos;
import com.luluandroid.miyouplus.bean.User;
import com.luluandroid.miyouplus.extra.ShowToast;
import com.luluandroid.miyouplus.util.TimeUtil;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class MiboMgr {
	private Context context;
	private MiboComment comment;
	private Mibos mibo;
	public MiboMgr(Context context) {
		super();
		this.context = context;
	}
	
	
	public void updateMibo(Mibos mibo,final UpdateMiboListener updateMiboListener){
		if(TextUtils.isEmpty(mibo.getObjectId())){
			ShowToast.showShortToast(context, "当前秘博ObjectId为空,不能更新");
			return;
		}
		mibo.update(context, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				updateMiboListener.onSucess();
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				updateMiboListener.onFailure(code, error);
			}
		});
	}
	
	public void deleteMibo(final Mibos mibo,final DeleteMiboListener deleteMiboListener){
		if(TextUtils.isEmpty(mibo.getObjectId())){
			ShowToast.showShortToast(context, "当前秘博ObjectId为空,不能删除");
			return;
		}
		mibo.delete(context, new DeleteListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				removeMiboToUser(mibo,deleteMiboListener);
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				deleteMiboListener.onFailure(code, error);
			}
		});
	}
	
	public void deleteselectMibo(List<Mibos> mibos,final DeleteMiboListener deleteMiboListener){
		List<BmobObject> deleteObjects = new ArrayList<BmobObject>();
		for(Mibos mibo :mibos){
			Mibos temp = new Mibos();
			temp.setObjectId(mibo.getObjectId());
			deleteObjects.add(temp);
		}
		new BmobObject().deleteBatch(context, deleteObjects, new DeleteListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				deleteMiboListener.onSucess();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				deleteMiboListener.onFailure(arg0, arg1);
			}
		});
	}
	
	public void removeMiboToUser(Mibos mibo,final DeleteMiboListener deleteMiboListener){
		if(TextUtils.isEmpty(mibo.getObjectId())
	||TextUtils.isEmpty(((User)User.getCurrentUser(context)).getObjectId())){
			ShowToast.showShortToast(context, "当前秘博或用户ObjectId为空,不能删除");
			return;
		}
		BmobRelation miboRelation = new BmobRelation();
		miboRelation.remove(mibo);
		((User)User.getCurrentUser(context)).setMiboRelation(miboRelation);
		((User)User.getCurrentUser(context)).update(context, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				deleteMiboListener.onSucess();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				deleteMiboListener.onFailure(arg0, arg1);
			}
		});
	}
	
	
	public void UpdateZanOrCommentCount(Mibos mibo,final boolean ZanOrComment,int count,final UpdateZanListener updateZanListener){
		if(TextUtils.isEmpty(mibo.getObjectId())){
			ShowToast.showShortToast(context, "当前秘博为空,不能更改赞数或评论数!");
			return;
		}
		if(ZanOrComment){
		mibo.increment("favorCount", count);
		}
		else{
			mibo.increment("CommentCount", count);
		}
		mibo.update(context, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if(ZanOrComment){
					ShowToast.showShortToast(context, "赞数更新成功!");
				}else{
					ShowToast.showShortToast(context, "评论数更新成功!");
				}
				updateZanListener.onSucess();
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				ShowToast.showShortToast(context, "赞数或评论数更新失败!"+
				"\n"+"code:"+code+"error:"+error);
				updateZanListener.onFailure(code, error);
			}
		});
	}
	
	/**
	 * @param content
	 * @return -1 代表内容为空
	 */
	public int saveComment(String content,final Mibos miboParent,final SaveCommentAndMiboListener saveCommentAndMiboListener){
		if(TextUtils.isEmpty(content)){
			return -1;
		}
		comment = new MiboComment(miboParent,content,miboParent.getHeadUserName());
		comment.save(context, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				addCommentToMibo(miboParent,saveCommentAndMiboListener);
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				saveCommentAndMiboListener.onFailure(code, error);
			}
		});
		return 1;
	}
	
	private void addCommentToMibo(Mibos mibo,final SaveCommentAndMiboListener saveCommentAndMiboListener){
		this.mibo = mibo;
		if(TextUtils.isEmpty(mibo.getObjectId())||
				TextUtils.isEmpty(comment.getObjectId())){
			ShowToast.showShortToast(context, "当前秘博或评论的objectId为空!");
			return;
		}
		BmobRelation Comments = new BmobRelation();
		Comments.add(comment);
		this.mibo.setComments(Comments);
		this.mibo.increment("CommentCount", 1);
		this.mibo.update(context, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ShowToast.showShortToast(context, "成功发表评论!!");
				saveCommentAndMiboListener.onSuccess(comment);
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				saveCommentAndMiboListener.onFailure(code, error);
			}
		});
	}
	public void removeCommentFromMibo(MiboComment comment,Mibos mibo){
		if(TextUtils.isEmpty(mibo.getObjectId())||
				TextUtils.isEmpty(comment.getObjectId())){
			ShowToast.showShortToast(context, "当前秘博或者当前秘博对象的objectId为空!");
			return;
		}
		this.mibo = mibo;
		this.comment = comment;
		BmobRelation Comments = new BmobRelation();
		Comments.remove(comment);
		this.mibo.setComments(Comments);
		this.mibo.update(context, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ShowToast.showShortToast(context, "删除评论成功");
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				ShowToast.showShortToast(context, "删除评论失败:"+"code:"+code+"\n"+"原因："+error);

			}
		});
	}
	
	public void deleteSelectComment(List<MiboComment>CommentList,final DeleteMiboListener deleteMiboListener){

		List<BmobObject> deleteObjects = new ArrayList<BmobObject>();
		for(MiboComment mibocomment :CommentList){
			MiboComment temp = new MiboComment();
			temp.setObjectId(mibocomment.getObjectId());
			deleteObjects.add(temp);
		}
		new BmobObject().deleteBatch(context, deleteObjects, new DeleteListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				deleteMiboListener.onSucess();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				deleteMiboListener.onFailure(arg0, arg1);
			}
		});
	
	}
	
	/**
	 * 用于打开app进行秘博初始化查询
	 * @param findMiboListener
	 */
	public void findinitialMibo(final FindMiboListener findMiboListener){
		BmobQuery<Mibos>query = new BmobQuery<Mibos>();
		//测试
		query.setLimit(2);
		query.order("-createdAt");
		query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
		query.setMaxCacheAge(1000L);
		query.findObjects(context, new FindListener<Mibos>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				findMiboListener.onFailure(arg0, arg1);
			}

			@Override
			public void onSuccess(List<Mibos> arg0) {
				// TODO Auto-generated method stub
				
				findMiboListener.onSuccess(arg0);
			}
		});
	}
	
	/**
	 * 选择缓存或者网络查询进行秘博查询
	 * @param CacheOrNetWork
	 * @param NeworOld
	 * @param time
	 * @param findMiboListener
	 */
	public void findCacheOrNetWorkMibo(boolean CacheOrNetWork,boolean NeworOld,final FindMiboListener findMiboListener){
		BmobQuery<Mibos>query = new BmobQuery<Mibos>();
		query.setLimit(30);
		if(NeworOld){
			query.order("-createdAt");
		}else{
			query.order("-createdAt");
		}
		
		
		if(CacheOrNetWork){
			if(query.hasCachedResult(context)){
				query.setCachePolicy(CachePolicy.CACHE_ONLY);
				query.findObjects(context, new FindListener<Mibos>() {

					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						findMiboListener.onFailure(arg0, arg1);
					}

					@Override
					public void onSuccess(List<Mibos> mibosList) {
						// TODO Auto-generated method stub
						findMiboListener.onSuccess(mibosList);
					}
				});
			}else{
				ShowToast.showShortToast(context, "缓存没有秘博");
			}
		}else{
			query.setCachePolicy(CachePolicy.NETWORK_ONLY);
			query.findObjects(context, new FindListener<Mibos>() {

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					findMiboListener.onFailure(arg0, arg1);
				}

				@Override
				public void onSuccess(List<Mibos> mibosList) {
					// TODO Auto-generated method stub
					findMiboListener.onSuccess(mibosList);
				}
			});
		}
		
	}
	
	public void findMyMibo(final FindMiboListener findMiboListener){
		BmobQuery<Mibos> query = new BmobQuery<Mibos>();
		query.addWhereEqualTo("headUserName", User.getCurrentUser(context).getUsername());
		query.setLimit(100);
		query.order("-createdAt");
		query.setCachePolicy(CachePolicy.IGNORE_CACHE);
		query.findObjects(context, new FindListener<Mibos>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				findMiboListener.onFailure(arg0, arg1);
			}

			@Override
			public void onSuccess(List<Mibos> mibosList) {
				// TODO Auto-generated method stub
				findMiboListener.onSuccess(mibosList);
			}
		});
	}
	
	public void findMiboUser(Mibos mibo,final FindMiboAndUserListener findMiboAndUserListener){
		BmobQuery<Mibos>query = new BmobQuery<Mibos>();
		query.include("myUser");
		query.getObject(context, mibo.getObjectId(), new GetListener<Mibos>() {

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				findMiboAndUserListener.onFailure(arg0, arg1);
			}

			@Override
			public void onSuccess(Mibos arg0) {
				// TODO Auto-generated method stub
				findMiboAndUserListener.onSuccess(arg0);
			}
		});
	}
	
	public void findZanCount(Mibos mibo,final MiboCountListener miboCountListener){
		if(TextUtils.isEmpty(mibo.getObjectId())||
				TextUtils.isEmpty(comment.getObjectId())){
			ShowToast.showShortToast(context, "当前秘博或者当前秘博对象的objectId为空!");
			return;
		}
		BmobQuery<Mibos> query = new BmobQuery<Mibos>();
		query.addWhereEqualTo("objectId", mibo.getObjectId());
		query.setCachePolicy(CachePolicy.NETWORK_ONLY);
		query.count(context, Mibos.class, new CountListener() {
			
			@Override
			public void onSuccess(int arg0) {
				// TODO Auto-generated method stub
				miboCountListener.onSucess(arg0);
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				miboCountListener.onFailure(code, error);
			}
		});
	}
	
	public void findCommentCount(Mibos mibo,final MiboCountListener miboCountListener){
		if(TextUtils.isEmpty(mibo.getObjectId())||
				TextUtils.isEmpty(comment.getObjectId())){
			ShowToast.showShortToast(context, "当前秘博或者当前秘博对象的objectId为空!");
			return;
		}
		BmobQuery<MiboComment>query  = new BmobQuery<MiboComment>();
		query.addWhereRelatedTo("Comments", new BmobPointer(mibo));
		query.setCachePolicy(CachePolicy.NETWORK_ONLY);
		query.count(context, MiboComment.class, new CountListener() {
			
			@Override
			public void onSuccess(int arg0) {
				// TODO Auto-generated method stub
				miboCountListener.onSucess(arg0);
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				miboCountListener.onFailure(arg0, arg1);
			}
		});
	}
	
	public void findALlComment(Mibos mibo,final FindAllCommentListener finalAllCommentListener){
		if(TextUtils.isEmpty(mibo.getObjectId())){
			ShowToast.showShortToast(context, "当前秘博或者当前秘博对象的objectId为空!");
			return;
		}
		BmobQuery<MiboComment> comments = new BmobQuery<MiboComment>();
		comments.setLimit(10);
		comments.order("createdAt");
		comments.addWhereRelatedTo("Comments", new BmobPointer(mibo));
		comments.findObjects(context, new FindListener<MiboComment>() {
			
			@Override
			public void onSuccess(List<MiboComment> comments) {
				// TODO Auto-generated method stub
				finalAllCommentListener.onSuccess(comments);
			}
			
			@Override
			public void onError(int code, String error) {
				// TODO Auto-generated method stub
				finalAllCommentListener.onFailure(code, error);
			}
		});
	}
	
	public void findCommentAndMibo(MiboComment miboComment,FindCommentAndMiboListener findCommentAndMiboListener){
		BmobQuery<MiboComment> query = new BmobQuery<MiboComment>();
		query.include("user");
		query.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
		this.comment = miboComment;
		query.getObject(context, comment.getObjectId(), new GetListener<MiboComment>() {
			
			
			@Override
			public void onSuccess(MiboComment arg0) {
				// TODO Auto-generated method stub
				ShowToast.showShortToast(context, "评论："+arg0.getContent()+"\n"
						+"秘博用户名："+arg0.getMiboParent().getHeadUserName());
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public void findMyComments(final FindAllCommentListener finalAllCommentListener){
		BmobQuery<MiboComment> comments = new BmobQuery<MiboComment>();
		comments.setLimit(100);
		comments.order("createdAt");
		comments.setCachePolicy(CachePolicy.IGNORE_CACHE);
		comments.addWhereEqualTo("UserName", BmobUser.getCurrentUser(context));
		comments.findObjects(context, new FindListener<MiboComment>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				finalAllCommentListener.onFailure(arg0, arg1);
			}

			@Override
			public void onSuccess(List<MiboComment> arg0) {
				// TODO Auto-generated method stub
				finalAllCommentListener.onSuccess(arg0);
			}
		});
	}
	
	public interface FindAllCommentListener{
		public void onSuccess(List<MiboComment> comments);
		public void onFailure(int code,String error);
	}
	
	public interface FindCommentAndMiboListener{
		public void onSuccess(MiboComment comments);
		public void onFailure(int code,String error);
	}
	
	public interface FindMiboAndUserListener{
		public void onSuccess(Mibos mibos);
		public void onFailure(int code,String error);
	}
	
	public interface FindMiboListener{
		public void onSuccess(List<Mibos> mibosList);
		public void onFailure(int code,String error);
	}
	
	public interface SaveCommentAndMiboListener{
		public void onSuccess(MiboComment comments);
		public void onFailure(int code,String error);
	}
	
	public interface MiboCountListener{
		public void onSucess(int count);
		public void onFailure(int code,String error);
	}
	
	public interface UpdateZanListener{
		public void onSucess();
		public void onFailure(int code,String error);
	}
	
	public interface UpdateMiboListener{
		public void onSucess();
		public void onFailure(int code,String error);
	}
	
	public interface DeleteMiboListener{
		public void onSucess();
		public void onFailure(int code,String error);
	}
}

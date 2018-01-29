package com.kkard.seoulroad.Recycler;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SuGeun on 2017-10-03.
 */

public class Data implements Serializable {

    private int viewType;

    private String mTextTile; // 제목
    private List<String> mTextContent; // 메인의 내용
    private String mTextSingle; // 서브의 내용
    private List<ListImageItem> listImageItemList;


    public List<List<String>> getvImageList() {
        return vImageList;
    }

    public void setvImageList(List<List<String>> vImageList) {
        this.vImageList = vImageList;
    }

    private List<List<String>> vImageList;

    public List<List<String>> getPagerImageList() {
        return pagerImageList;
    }

    public void setPagerImageList(List<List<String>> pagerImageList) {
        this.pagerImageList = pagerImageList;
    }

    private List<List<String>> pagerImageList;
    private List<String> mPostContent; // 아이디, 이미지, 좋아요, 날짜, 코멘트 순서
    private List<String> mCourseContent; // 사진, 제목, 내용 순서
    private int mImageId; // 코스에 넣을 이미지 id
    /*
     * SETTER
     */
    public void setmImageId(int mImageId) {        this.mImageId = mImageId;    }
    public void setmCourseContent(List<String> mCourseContent) {this.mCourseContent = mCourseContent;}
    public void setmPostContent(List<String> mPostContent) {this.mPostContent = mPostContent;}
    public void setListImageItemList(List<ListImageItem> listImageItemList) {
        this.listImageItemList = listImageItemList;
    }

    public void setTextList(String textTitle, List<String> textContent) {
        this.mTextTile = textTitle;
        this.mTextContent = textContent;
    }
    public void setTextList(String textTitle, String textSingle) {
        this.mTextTile = textTitle;
        this.mTextSingle = textSingle;
    }
    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    /*
     * GETTER
     */
    public int getmImageId() {        return mImageId;    }
    public List<String> getmCourseContent() {return mCourseContent;}
    public List<String> getmPostContent() {return mPostContent;}
    public List<ListImageItem> getListImageItemList() {
        return listImageItemList;
    }
    public String getTextTile() {
        return mTextTile;
    }
    public List<String> getTextContent(){return mTextContent;}
    public String getTextSingle(){return mTextSingle;}
    public int getViewType() {
        return viewType;
    }
}

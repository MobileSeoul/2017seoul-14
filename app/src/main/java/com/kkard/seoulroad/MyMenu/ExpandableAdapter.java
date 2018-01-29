package com.kkard.seoulroad.MyMenu;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.kkard.seoulroad.R;

import java.util.ArrayList;

/**
 * Created by SuGeun on 2017-09-04.
 */

public class ExpandableAdapter extends BaseExpandableListAdapter {
    public static final int TYPE_NOTICE = 78;
    public static final int TYPE_QNA = 79;
    ArrayList<NoticeParentData> noticeParentDatas;
    ArrayList<NoticeChildData> noticeChildDatas;
    LayoutInflater layoutInflater;

    public ExpandableAdapter(Context c, ArrayList<NoticeParentData> noticeParentDatas,
                             ArrayList<NoticeChildData> noticeChildDatas ) {
        this.noticeChildDatas = noticeChildDatas;
        this.noticeParentDatas = noticeParentDatas;
        layoutInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return noticeParentDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1; // 각 부모당 자식은 1개
    }

    @Override
    public NoticeParentData getGroup(int groupPosition) {
        return noticeParentDatas.get(groupPosition);
    }

    @Override
    public NoticeChildData getChild(int groupPosition, int childPosition) {
        return noticeChildDatas.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.layout_listview_parent,null);
        }
        TextView type_parent_tv = (TextView)convertView.findViewById(R.id.type_parent);
        TextView content_parent_tv = (TextView)convertView.findViewById(R.id.content_parent);
        switch (noticeParentDatas.get(groupPosition).getType()){
            case TYPE_NOTICE : //공지인 경우
                type_parent_tv.setText("공지");
                type_parent_tv.setBackgroundResource(R.drawable.pink_circle_back);
                type_parent_tv.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case TYPE_QNA : // 질문일 경우
                type_parent_tv.setText("Q.");
                type_parent_tv.setBackgroundColor(Color.TRANSPARENT);
                type_parent_tv.setTextColor(Color.parseColor("#00A24C"));
                break;
        }
        content_parent_tv.setText(noticeParentDatas.get(groupPosition).getTitle());
        return convertView;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.layout_listview_child,null);
        }
        TextView content_child_tv = (TextView)convertView.findViewById(R.id.content_child);

        content_child_tv.setText(noticeChildDatas.get(groupPosition).getContent());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {return false;}
}

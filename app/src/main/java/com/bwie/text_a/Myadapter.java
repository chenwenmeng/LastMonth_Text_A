package com.bwie.text_a;

import android.content.Context;


import android.util.Log;
import android.view.View;


import android.view.ViewGroup;
import android.widget.BaseAdapter;



import android.widget.BaseExpandableListAdapter;

import android.widget.CheckBox;

import android.widget.CompoundButton;
import android.widget.TextView;


import android.widget.Toast;


import com.bwie.bean.Bean;

import java.util.List;

/**
 * 类描述:
 * 作者：陈文梦
 * 时间:2017/2/4 14:23
 * 邮箱:18310832074@163.com
 */

public
class
Myadapter extends BaseExpandableListAdapter {


    private Context context;
    private List<Bean.DataBean> dataBeen;
    private CheckInterface checkInterface;

    public Myadapter(Context context, List<Bean.DataBean> dataBeen) {
        this.context = context;
        this.dataBeen = dataBeen;
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;

    }

    @Override
    public int getGroupCount() {
        return dataBeen.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return dataBeen.get(groupPosition).getDatas().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dataBeen.get(groupPosition).getTitle();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dataBeen.get(groupPosition).getDatas().get(childPosition).getType_name();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //GroupViewHolder groupViewHolder=null;
        //if(convertView==null){
         //   groupViewHolder=new GroupViewHolder();
            convertView=View.inflate(context,R.layout.group_item,null);
            TextView group_title= (TextView) convertView.findViewById(R.id.group_title);
            TextView group_title_id= (TextView) convertView.findViewById(R.id.group_title_id);
            CheckBox group_check= (CheckBox) convertView.findViewById(R.id.group_check);
       //     convertView.setTag(groupViewHolder);
       // }else {
       // groupViewHolder= (GroupViewHolder) convertView.getTag();
       // }
        group_title.setText(dataBeen.get(groupPosition).getTitle());
        group_title_id.setText(dataBeen.get(groupPosition).getTitle_id());
        group_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkInterface!=null){
                    dataBeen.get(groupPosition).setGroupCheck(isChecked);
                checkInterface.groupCheck(groupPosition,isChecked);
                }
            }
        });
        group_check.setChecked(dataBeen.get(groupPosition).isGroupCheck());
        return convertView;
    }

    class GroupViewHolder{

        TextView group_title,group_title_id;
        CheckBox group_check;

    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

       // ChildViewHolder childViewHolder=null;
       // if(convertView==null){
        //    childViewHolder=new ChildViewHolder();
            convertView=View.inflate(context,R.layout.child_item,null);
        CheckBox child_check= (CheckBox) convertView.findViewById(R.id.child_check);
        TextView miaoshu= (TextView) convertView.findViewById(R.id.miaoshu);
        TextView time= (TextView) convertView.findViewById(R.id.time);
        TextView child_title= (TextView) convertView.findViewById(R.id.child_title);
        TextView price= (TextView) convertView.findViewById(R.id.price);
      //
      //
        //convertView.setTag(childViewHolder);
       // }else {

        //   childViewHolder= (ChildViewHolder) convertView.getTag();
       // }
        child_title.setText(dataBeen.get(groupPosition).getDatas().get(childPosition).getType_name());
        miaoshu.setText(dataBeen.get(groupPosition).getDatas().get(childPosition).getMsg());
        time.setText(dataBeen.get(groupPosition).getDatas().get(childPosition).getAdd_time());
        price.setText("￥"+dataBeen.get(groupPosition).getDatas().get(childPosition).getPrice());
        child_check.setChecked(dataBeen.get(groupPosition).getDatas().get(childPosition).isChildcheckBox());
        child_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkInterface!=null){

                checkInterface.childCheck(groupPosition,childPosition,isChecked);
                }
            }
        });
        return convertView;
    }

    class ChildViewHolder{

        TextView child_title,miaoshu,time,price;
        CheckBox child_check;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    //复选框接口
    public interface CheckInterface{

         void groupCheck(int groupPosition, boolean isChecked);
         void childCheck(int groupPosition, int childPosition, boolean isChecked);

    }

}

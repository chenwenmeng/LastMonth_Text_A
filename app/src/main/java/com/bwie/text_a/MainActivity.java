package com.bwie.text_a;

import android.app.ExpandableListActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.util.Log;


import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import android.widget.TextView;
import android.widget.Toast;


import com.bwie.bean.Bean;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;


import com.handmark.pulltorefresh.library.PullToRefreshListView;


import com.lidroid.xutils.HttpUtils;


import com.lidroid.xutils.exception.HttpException;


import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;


import java.util.ArrayList;

import java.util.List;

public
class
MainActivity extends ExpandableListActivity implements PullToRefreshBase.OnRefreshListener2, Myadapter.CheckInterface {

    private PullToRefreshExpandableListView mExpandList;
    private List<String> stringList = new ArrayList<>();
    private List<Bean.DataBean> dataBeen = new ArrayList<Bean.DataBean>();


    private TextView count;
    private
    Myadapter myadapter;

    private TextView count_price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mExpandList = (PullToRefreshExpandableListView) findViewById(R.id.expand_list);
        count = (TextView) findViewById(R.id.count);
        count_price = (TextView) findViewById(R.id.count_price);
        Button huishou = (Button) findViewById(R.id.huishou);
        //可以刷新加载
        mExpandList.setMode(PullToRefreshBase.Mode.BOTH);
        mExpandList.setOnRefreshListener(this);
        //初始化数据
        getdate();
        //回收按钮监听
        huishou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Bean.DataBean.DatasBean> datasList = new ArrayList<Bean.DataBean.DatasBean>();
                List<Bean.DataBean> dataBeanList = new ArrayList<Bean.DataBean>();
                //遍历集合所有数据
                for (int i = 0; i < dataBeen.size(); i++) {
                    //判断一级复选框是否选中
                    if (dataBeen.get(i).isGroupCheck()) {
                        //选中则存进集合
                        dataBeanList.add(dataBeen.get(i));
                    } else {
                        //如果一级复选框不是选中状态则遍历每个一级列表中的二级条目
                        List<Bean.DataBean.DatasBean> datas = dataBeen.get(i).getDatas();
                        for (int j = 0; j < datas.size(); j++) {
                            //判断每个二级条目是否选中
                            if (datas.get(j).isChildcheckBox()) {
                                //选中则添加进集合
                                datasList.add(dataBeen.get(i).getDatas().get(j));
                            }
                            //删除二级条目集合
                            dataBeen.get(i).getDatas().removeAll(datasList);
                        }
                    }
                }
                //删除数据
                dataBeen.removeAll(dataBeanList);
                //刷新适配器
                myadapter.notifyDataSetChanged();
                //计算价格
                setprice();
            }
        });
    }

    //请求数据
    public void getdate() {

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, "http://mock.eoapi.cn/success/9ghtceuvzUWfvffRK6pts8zaEjGYWrkK", new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Bean bean = new Gson().fromJson(result, Bean.class);

                dataBeen.addAll(bean.getData());
                //Toast.makeText(MainActivity.this,"数据请求成功"+dataBeen.size(),Toast.LENGTH_SHORT).show();
                myadapter = new Myadapter(MainActivity.this, dataBeen);
                myadapter.setCheckInterface(MainActivity.this);
                //setListAdapter(myadapter);
                MainActivity.this.setListAdapter(myadapter);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(MainActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        //getdate();
        /*if(myadapter!=null){
            Toast.makeText(this,"下拉刷新数据",Toast.LENGTH_SHORT).show();
            myadapter.notifyDataSetChanged();
            mExpandList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mExpandList.onRefreshComplete();
                }
            }, 1000);
        }*/
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        //getdate();
        /*if(myadapter!=null){
            Toast.makeText(this,"上拉加载数据",Toast.LENGTH_SHORT).show();
            myadapter.notifyDataSetChanged();
            mExpandList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mExpandList.onRefreshComplete();
                }
            }, 1000);
        }*/
    }

    //一级条目复选框监听
    @Override
    public void groupCheck(int groupPosition, boolean isChecked) {
        //判断是否选中
        if (isChecked) {
            //则该一级条目中所有子条目选中
            List<Bean.DataBean.DatasBean> datas = dataBeen.get(groupPosition).getDatas();
            for (int i = 0; i < datas.size(); i++) {
                datas.get(i).setChildcheckBox(isChecked);
            }

        } else {
            //否则全不选中
            List<Bean.DataBean.DatasBean> datas = dataBeen.get(groupPosition).getDatas();
            for (int i = 0; i < datas.size(); i++) {
                datas.get(i).setChildcheckBox(isChecked);
            }
        }
            //刷新，计价
            myadapter.notifyDataSetChanged();
            setprice();
    }
    //二级条目复选框监听
    @Override
    public void childCheck(int groupPosition, int childPosition, boolean isChecked) {
        //获取该一级条目中子元素的集合
        List<Bean.DataBean.DatasBean> datas = dataBeen.get(groupPosition).getDatas();
        //判断该条目是否选中
        if (isChecked) {
            dataBeen.get(groupPosition).getDatas().get(childPosition).setChildcheckBox(isChecked);
        } else {
            dataBeen.get(groupPosition).getDatas().get(childPosition).setChildcheckBox(isChecked);
        }
        //如果该组元素下所有子元素全部选中则组元素选中
        int flag = 0;
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).isChildcheckBox()) {
                flag++;
            } else {
                flag--;
            }
        }
        if (flag == datas.size()) {
            dataBeen.get(groupPosition).setGroupCheck(true);
        } else {
            dataBeen.get(groupPosition).setGroupCheck(false);
        }
        myadapter.notifyDataSetChanged();
        setprice();
    }
    private int num = 0;
    private int price = 0;
    //计价
    public void setprice() {
        //每次重新计价
        num = 0;
        price = 0;
        for (int i = 0; i < dataBeen.size(); i++) {
            List<Bean.DataBean.DatasBean> datas = dataBeen.get(i).getDatas();
            for (int j = 0; j < datas.size(); j++) {
                Bean.DataBean.DatasBean datasBean = datas.get(j);
                if (datasBean.childcheckBox) {
                    num++;
                    price += datasBean.getPrice();
                }
            }
        }
        count.setText(num + "台旧机");
        count_price.setText("合计：￥" + price);
    }

}

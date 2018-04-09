package com.example.j.personal_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wgs on 2018/4/8.
 */

//用来展示学生信息的ListView需要我们自定义Adapter将数据库中的数据填充到ListView。
//ListView需要自定义Adapter来显示视图，自定义Adapter扩展自BaseAdapter，覆写其中四个方法即可，
// 其中getView()方法用来控制没个ListView item的具体显示。
public class StudentAdapter extends BaseAdapter {
    private List<Student> students;
    private Context context;
    //初始化适配器
    public StudentAdapter(Context context, List<Student> students) {
        super();
        this.students=students;
        this.context=context;
    }
    // 适配器中数据集中数据的个数
    @Override
    public int getCount() {
        return students.size();
    }

    // 获取数据集中与指定索引对应的数据项
    @Override
    public Object getItem(int i) {
        return students.get(i);
    }

    // 获取指定行对应的ID
    @Override
    public long getItemId(int i) {
        return i;
    }

    // 获取每一个Item显示的内容
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
        }
        //分别获取ImageView TextView的实例，
        ImageView imageView= (ImageView) view.findViewById(R.id.image);
        TextView tvName= (TextView) view.findViewById(R.id.name);
        TextView tvGrade= (TextView) view.findViewById(R.id.grade);

        //随机为学生匹配头像
        if(students.get(i).getId()%2==0)
        {
            imageView.setImageResource(R.mipmap.girl1);
        }else{
            imageView.setImageResource(R.mipmap.boy2);
        }
        tvName.setText("姓名  "+students.get(i).getName());
        //students.get(i)返回数据集中第i个数据对象，在调用其getName()方法
        tvGrade.setText("分数  "+students.get(i).getGrade());
        return view;
    }
}
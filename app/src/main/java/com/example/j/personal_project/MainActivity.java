package com.example.j.personal_project;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener{

    private ListView students;
    private StudentAdapter adapter;
    private Button btnAdd,btnSearch;
    private DatabaseHandler dbHandler;
    private List<Student> studentList;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        students= (ListView) findViewById(R.id.stduent_list);
        btnAdd= (Button) findViewById(R.id.btn_add);
        btnSearch= (Button) findViewById(R.id.btn_search);


        btnSearch.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        dbHandler=new DatabaseHandler(this);

        //获取全部学生信息存入Student类的对象的容器studentList中
        studentList=dbHandler.getALllStudent();

        //由于数组中的数据是无法直接传递给ListView，我们需要借助适配器来完成。接下来我们只需调用ListView的setAdapeter()方法，
        // 将构建好的适配器对象传递进去。这样ListView和数据之间的关联就建立完成了
        adapter=new StudentAdapter(this,studentList);
        students.setAdapter(adapter);

        //点击ListView item跳转到详细界面
        students.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //打开学生信息界面，并传入数据用于显示
                Intent intent=new Intent(MainActivity.this,StudentActivity.class);
                //Intent中提供了一系列putExtra()方法重载，可以把我们想要传递的数据暂时存在intent中，
                //putExtra(),接受两个参数，用于后面从intent中取值，第二个参数才是真正要传递的数据
                //request是为了区分是通过什么跳转到详细界面的

                intent.putExtra("request","Look");
                intent.putExtra("id",studentList.get(i).getId());
                intent.putExtra("name",studentList.get(i).getName());
                intent.putExtra("grade",studentList.get(i).getGrade());
                //studentList.get(i).getId(),表示找到容器studentList的第i个数据，因为他是Student这个类的对象，
                //调用getId（）方法获取这个对象的id属性值
                startActivityForResult(intent, 0);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                Intent i=new Intent(MainActivity.this,StudentActivity.class);
                i.putExtra("request","Add");
                startActivityForResult(i, 1);
                break;
            case R.id.btn_search:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);

                //自定义View的Dialog
                final LinearLayout searchView= (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_search,null);
                builder.setView(searchView);
                final AlertDialog dialog=builder.create();
                dialog.show();

                //为自定义View的Dialog的控件添加事件监听。
                final EditText searchName= (EditText) searchView.findViewById(R.id.search_name);
                Button btnDialogSearch= (Button) searchView.findViewById(R.id.btn_search_dialog);
                btnDialogSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchName.setVisibility(View.GONE);
                        ListView list = (ListView) searchView.findViewById(R.id.search_result);
                        List<Student> resultList = new ArrayList<Student>();
                        //通过dbhandler根据输入框中的信息查询是否存在这个对象
                        final Student searchStudent = dbHandler.getStudent(searchName.getText().toString());
                        if (searchStudent != null) {
                            resultList.add(searchStudent);
                            StudentAdapter resultAdapter = new StudentAdapter(getApplicationContext(), resultList);
                            list.setAdapter(resultAdapter);
                            list.setVisibility(View.VISIBLE);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(MainActivity.this, StudentActivity.class);
                                    intent.putExtra("request", "Look");
                                    intent.putExtra("id", searchStudent.getId());
                                    intent.putExtra("name", searchStudent.getName());
                                    intent.putExtra("grade", searchStudent.getGrade());
                                    startActivityForResult(intent, 0);
                                }
                            });
                        } else {
                            //关闭Dialog
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "无此学生", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //由于我们是通过startActivityForResult()方法来启动一个活动，在这个活动被销毁时会回调上一个活动的onActitityResult（）方法，
        // 为了获取从被销毁的活动的intent中传来的数据，我们需要在当前活动中重写这个方法
        // 根据返回的requestCode判断数据是通过哪个活动返回的，resultCode用来判断处理结果是否成功，最后从data中取值并打印出来
        //本案例中，进入添加页面，输入信息后，返回数据给主页面
        switch (requestCode){
            case 0:
                if (resultCode==2)
                    Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
                if (resultCode==3)
                    Toast.makeText(this,"已删除",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                if (resultCode==RESULT_OK)
                    Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                break;
        }
        /**
         * 如果这里仅仅使用adapter.notifyDataSetChanged()是不会刷新界面ListView的，
         * 因为此时adapter中传入的studentList并没有给刷新，即adapter也没有被刷新，所以你可以
         * 重新获取studentList后再改变adapter，我这里通过调用onCreate()重新刷新了整个界面
         */

        //        studentList=dbHandler.getALllStudent();
        //        adapter=new StudentAdapter(this,studentList);
        //        students.setAdapter(adapter);
        onCreate(null);
    }
}
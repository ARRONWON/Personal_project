package com.example.j.personal_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wgs on 2018/4/8.
 */

public class DatabaseHandler extends SQLiteOpenHelper{

    private static final String DATABASE_NAME="Test";
    private static final String TABLE_NAME="student";
    private static final int VERSION=1;
    private static final String KEY_ID="id";
    private static final String KEY_NAME="name";
    private static final String KEY_GRADE="grade";

    //建表语句
    private static final String CREATE_TABLE="create table "+TABLE_NAME+"("+KEY_ID+
            " integer primary key autoincrement,"+KEY_NAME+" text not null,"+
            KEY_GRADE+" text not null);";
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    //重载 SQLiteOpenHelper 的onCreat()函数,在数据库第一次被建立时调用，用来创建数据库中的表，并完成初始化工作
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }
    //重载 SQLiteOpenHelper 的onUpgrade()函数，用于对数据库进行升级
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addStudent(Student student){
        //Sqliteopenhelper类的getWritableDatabase（）函数，用来建立或打开可读写的数据库实例，不使用时调用close()关闭
        SQLiteDatabase db=this.getWritableDatabase();

        //使用ContentValues添加数据，先定义一个ContentVlaues实例，然后调用其put()方法，将属性值写入ContentVlaues实例，
        // 最后使用SqliteDatabase实例的insert()方法，将ContentVlaues实例中的数据写入指定的数据表中
        ContentValues values=new ContentValues();
        values.put(KEY_NAME,student.getName());
        values.put(KEY_GRADE,student.getGrade());
        db.insert(TABLE_NAME, null, values);

        //关闭
        db.close();
    }


    //通过name获取student
    public Student getStudent(String name){
        SQLiteDatabase db=this.getWritableDatabase();

        //Cursor对象返回查询结果，但注意查询结果不是数据集合的完整拷贝，而是返回数据集的指针，这个指针就是cursor类
        Cursor cursor=db.query(TABLE_NAME,new String[]{KEY_ID,KEY_NAME,KEY_GRADE},
                KEY_NAME+"=?",new String[]{name},null,null,null,null);
        //分配一个空间
        Student student=null;
        //注意返回结果有可能为空
        if(cursor.moveToFirst()){

            student=new Student(cursor.getInt(0),cursor.getString(1), cursor.getString(2));
        }
        return student;
    }

    //获取学生的数目
    public int getStudentCounts(){
        String selectQuery="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);
        cursor.close();

        //获取集合的数据数量
        return cursor.getCount();
    }

    //查找所有student
    public List<Student> getALllStudent(){
        //new ArrayList<Student>();构造一个数组，数组存储的数据类型为 Student这个类的对象
        //List<Student> studentList,我理解为创造一个容器studentLise来盛放Student的对象
        List<Student> studentList=new ArrayList<Student>();

        String selectQuery="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);
        //由于数据的查询结果的返回值并不是数据集合的完整拷贝，而是返回数据集的指针 ，
        // 所以我们的任务是通过cursor这个指针来将查询结果保存到Student的对象中，
        //因此，我们定义一个Student类的对象student，利用类的方法给student赋值，并将这个对象添加到容器studentLise，
        //通过cursor.moveToFirst()方法控制指针的移动，一个个读取数据放入容器。
        if(cursor.moveToFirst()){
            do{
                Student student=new Student();
                student.setId(Integer.parseInt(cursor.getString(0)));
                student.setName(cursor.getString(1));
                student.setGrade(cursor.getString(2));
                studentList.add(student);
            }while(cursor.moveToNext());
        }
        return studentList;
    }

    //更新student
    public int updateStudent(Student student){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_NAME,student.getName());
        values.put(KEY_GRADE,student.getGrade());

        return db.update(TABLE_NAME,values,KEY_ID+"=?",new String[]{String.valueOf(student.getId())});
    }
    public void deleteStudent(Student student){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,KEY_ID+"=?",new String[]{String.valueOf(student.getId())});
        db.close();
    }
}


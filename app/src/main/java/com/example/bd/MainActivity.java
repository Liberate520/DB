package com.example.bd;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText name, value; //Создаю ссылки на объекты на экране
    private Button add, print;
    private ListView list;
    private LinearLayout title;

    private final static String DATABASE_NAME = "Test";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "Person";

    private final static String COLUMN_ID = "_id";
    private final static String COLUMN_NAME = "name";
    private final static String COLUMN_VALUE = "value";

    private final static int NUM_COLUMN_ID = 0; //номера столбцов для использования курсора
    private final static int NUM_COLUMN_NAME = 1;
    private final static int NUM_COLUMN_VALUE = 2;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name); //Сохраняю объекты в ссылки
        value = findViewById(R.id.value);
        add = findViewById(R.id.add);
        print = findViewById(R.id.print);
        list = findViewById(R.id.list);
        title = findViewById(R.id.title);

        OpenHelper openHelper = new OpenHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        database = openHelper.getWritableDatabase(); //Получить базу данных для записи

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAll();
            }
        });
    }

    public void insert(){
        if (!String.valueOf(name.getText()).equals("") && !String.valueOf(value.getText()).equals("")) {
            ContentValues cv = new ContentValues(); //позволяет добавлять данные для записи
            cv.put(COLUMN_NAME, name.getText().toString());
            cv.put(COLUMN_VALUE, Long.valueOf(value.getText().toString()));
            long count = database.insert(TABLE_NAME, null, cv);
            Toast.makeText(this, String.valueOf(count), Toast.LENGTH_SHORT).show();
        }
    }

    public void selectAll(){
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Person> people = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(NUM_COLUMN_ID);
                String name = cursor.getString(NUM_COLUMN_NAME);
                long value = cursor.getLong(NUM_COLUMN_VALUE);
                Person person = new Person(id, name, value);
                people.add(person);
            }while (cursor.moveToNext());
            if (people.size() > 0){
                title.setVisibility(View.VISIBLE);
            }
            MyListAdapter adapter = new MyListAdapter(this, people);
            list.setAdapter(adapter);
        }
    }

    class OpenHelper extends SQLiteOpenHelper{

        public OpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) { //Создаю единственную таблицу
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_VALUE + " INTEGER NOT NULL);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // Если новая версия, то удаляем старую и создаем новую
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }


    }
}
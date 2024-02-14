package com.AI.lichlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.bson.Document;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class MainActivity extends AppCompatActivity {
    public MongoClient mongoClient;
    public MongoDatabase mongoDatabase;
    public MongoCollection<Document> mongoCollection;
    AtomicReference<User> users = new AtomicReference<User>();
    public User user;
    App app;
    protected Credentials credentials;
    DB DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DB = new DB(this);

//Installation DB Mongodb
        Realm.init(this);
        lichLibrary p = new lichLibrary();
        app = new App(new AppConfiguration.Builder(p.appid)
                .appName("My App")
                .requestTimeout(30, TimeUnit.SECONDS)
                .build());
        credentials = Credentials.anonymous();

        app.loginAsync(credentials, it -> {
            if (it.isSuccess()) {
                Log.v("AUTH", "Successfully authenticated anonymously.");
                users.set(app.currentUser());
            } else {
                Log.e("AUTH", it.getError().toString());
            }
        });
        user = app.currentUser();

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                DB.inLog("","");

                String usr = DB.cek();
                Log.v("cek",usr);
                if (usr.equals("")){
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }else {
                    mongoClient = user.getMongoClient("mongodb-atlas");
                    mongoDatabase = mongoClient.getDatabase("LichLibrary");
                    MongoCollection users_col = mongoDatabase.getCollection("user");
                    Document d = new Document();
                    d.put("username",usr);
                    users_col.find(d).iterator().getAsync(val ->{
                        if (val.isSuccess()){
                            MongoCursor<Document> value = (MongoCursor) val.get();
                            if (value.hasNext()){
                                Intent intent = new Intent(MainActivity.this, Home.class);
                                startActivity(intent);
                                finish();
                            }else {
                                DB.delLog(usr);
                                Intent intent = new Intent(MainActivity.this, Login.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }


            }
        },3000);
    }
}
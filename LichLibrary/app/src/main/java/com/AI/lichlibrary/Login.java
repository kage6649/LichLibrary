package com.AI.lichlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.bson.Document;

import java.security.InvalidKeyException;
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

public class Login extends AppCompatActivity {
    public MongoClient mongoClient;
    public MongoDatabase mongoDatabase;
    public MongoCollection<Document> mongoCollection;
    AtomicReference<User> users = new AtomicReference<User>();
    public User user;
    App app;
    protected Credentials credentials;
    EditText usr,pass;
    DB DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usr = findViewById(R.id.username);
        pass = findViewById(R.id.password);
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
//                Log.v("AUTH", "Successfully authenticated anonymously.");
                users.set(app.currentUser());
            } else {
//                Log.e("AUTH", it.getError().toString());
            }
        });
        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("LichLibrary");
    }
    public void login(View view) throws InvalidKeyException {
//        MainActivity mc = new MainActivity();
        if (usr.getText().toString().equals("")&&pass.getText().toString().equals("")){
            Toast.makeText(this, "Isi Semua Kolom dengan Benar!", Toast.LENGTH_SHORT).show();
        }else {
            ED ed = new ED();
            String us = usr.getText().toString();
            String pas = ed.encryptionKey(pass.getText().toString());
            Document d = new Document();
            d.put("username",us);
            d.put("password",pas);
            d.put("level","user");

            MongoCollection log = mongoDatabase.getCollection("user");
//            MongoCursor<Document> cek = (MongoCursor<Document>) log.find(d).iterator();
            log.find(d).iterator().getAsync(result -> {
                if (result.isSuccess()) {
                    MongoCursor p = (MongoCursor) result.get();
//                        BsonObjectId insertedId = result.get().getInsertedId().asObjectId();
//                        Log.v("adding", "result");
                    int x =0;
                    while (p.hasNext()) {
//                    Log.v("EXAMPLE", p.next().toString());
                        DB.delLog_All();
                        DB.inLog(us,"loged");
                        String cek = DB.cekReg();
                        DB.delReg(cek);
                        Toast.makeText(this, "Login Success!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Login.this, Home.class);
                        startActivity(intent);
                        finish();
                        x=1;
                        break;
                    }
                    if (x==0){
                        Toast.makeText(this, "Account does not exist!", Toast.LENGTH_SHORT).show();
                    }
//                Toast.makeText(this, "Account does not exist!", Toast.LENGTH_SHORT).show();
                } else {
//                        Log.v("adding", "result failed" + result.getError().toString());
//                Toast.makeText(this, "Error " + result.getError(), Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void ToSign(View view) {
        Intent intent = new Intent(Login.this, Sign.class);
        startActivity(intent);
        finish();
    }
}
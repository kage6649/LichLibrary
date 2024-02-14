package com.AI.lichlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.bson.Document;

import java.util.Arrays;
import java.util.Date;
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

public class Sign extends AppCompatActivity {
    public MongoClient mongoClient;
    public MongoDatabase mongoDatabase;
    public MongoCollection<Document> mongoCollection;
    AtomicReference<User> users = new AtomicReference<User>();
    public User user;
    App app;
    protected Credentials credentials;
    EditText usr,pass,repass,name,mail,alamat;
    DB DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        usr = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        repass = findViewById(R.id.repass);
        name = findViewById(R.id.nameku);
        mail = findViewById(R.id.mail);
        alamat = findViewById(R.id.alamat);
        DB = new DB(this);

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
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("LichLibrary");
    }
    public void Sign(View view) {
        if (usr.getText().toString().equals("")||pass.getText().toString().equals("")||repass.getText().toString().equals("")||name.getText().toString().equals("")||mail.getText().toString().equals("")||alamat.getText().toString().equals("")){
            Toast.makeText(this, "Isi Semua Kolom dengan Benar!", Toast.LENGTH_SHORT).show();
        }else {
            String email = mail.getText().toString().trim();
            if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
//                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            } else {
                if (pass.getText().toString().equals(repass.getText().toString())) {
                    ED ed = new ED();
                    String us = usr.getText().toString();
                    String pas = ed.encryptionKey(pass.getText().toString());
                    String nm = name.getText().toString();
//                String email = mail.getText().toString();
                    String alm = alamat.getText().toString();

                    Document d = new Document();
                    d.put("username", us);
                    d.put("password", pas);
                    d.put("NamaLengkap", nm);
                    d.put("email", email);
                    d.put("Alamat", alm);
                    d.put("level","regist");

                    MongoCollection log = mongoDatabase.getCollection("user");
                    //to query or with 3 request
                    Document cek = new Document("$or", Arrays.asList(
                            new Document("username", us),
                            new Document("email", email)));

                    log.find(cek).iterator().getAsync(result -> {
                        if (result.isSuccess()) {
                            MongoCursor p = (MongoCursor) result.get();
                            if (p.hasNext()) {
                                Log.v("RESULT", p.next().toString());
                                Toast.makeText(this, "Username,Email or Number Already exists!", Toast.LENGTH_LONG).show();
                            } else {
                                log.insertOne(d).getAsync(rs -> {
                                    if (rs.isSuccess()) {
                                        Toast.makeText(this, "Regist Success!\nWait for Admin Confirmation!", Toast.LENGTH_SHORT).show();
                                        DB.delReg_All();
                                        DB.inReg(us,"regist");
                                        Intent intent = new Intent(Sign.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(this, "Regist Failed!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } else {
//                        Log.v("adding", "result failed" + result.getError().toString());
//                Toast.makeText(this, "Error " + result.getError(), Toast.LENGTH_LONG).show();
                            Toast.makeText(this, "Regist Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "Password tidak cocok!", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
    public void ToLogin(View view) {
        Intent intent = new Intent(Sign.this, Login.class);
        startActivity(intent);
        finish();
    }
    public void notif(View view) {
        String reg = DB.cekReg();
        MongoCollection log = mongoDatabase.getCollection("user");
        Document cek = new Document();
        cek.put("username",reg);
        cek.put("level","regist");
        Log.v("reg",reg);
        if (reg.equals("")){
            Toast.makeText(this, "No notifications", Toast.LENGTH_SHORT).show();
        }else {
            log.find(cek).iterator().getAsync(val -> {
                MongoCursor v = (MongoCursor) val.get();
                if (v.hasNext()){
//                    Log.v("reg",v.next().toString());
                    v.next();
                    Toast.makeText(this, "Regist Success!\nWait for Staff Confirmation!", Toast.LENGTH_SHORT).show();
                }else {
                    Document cek2 = new Document();
                    cek2.put("username",reg);
                    cek2.put("level","user");
                    log.find(cek2).iterator().getAsync(val2 -> {
                        MongoCursor v2 = (MongoCursor) val2.get();
                        if (v2.hasNext()){
                            v2.next();
                            Toast.makeText(this, "Your account has been accepted\n please login!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(this, "Your account has been rejected, please regist again!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

    }
}
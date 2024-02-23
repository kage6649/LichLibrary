package com.AI.lichlibrary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

public class ListViewKol extends AppCompatActivity implements RecyleKol.ItemClickListener {
    public MongoClient mongoClient;
    public MongoDatabase mongoDatabase;
    public MongoCollection<Document> mongoCollection;
    AtomicReference<User> users = new AtomicReference<User>();
    public User user;
    App app;
    protected Credentials credentials;
    DB DB;
    RecyleKol adapterBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kol);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.lich_logo_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DB = new DB(this);

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0087FF"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

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

        try {
            ValueBook();
        }catch (Exception e){
            e.printStackTrace();
            Log.v("oke","GALAT_2");
        }

    }
    public void ValueBook(){
        Intent intent = getIntent();

        ArrayList<String> data1 = intent.getStringArrayListExtra("valKode");
        ArrayList<String> data2 = intent.getStringArrayListExtra("valJudul");
        ArrayList<String> data3 = intent.getStringArrayListExtra("valKategori");
        ArrayList<String> data4 = intent.getStringArrayListExtra("valPenulis");

        RecyclerView recyclerBook = findViewById(R.id.listKoleksi);
        recyclerBook.setLayoutManager(new LinearLayoutManager(this));
        adapterBook = new RecyleKol(this, data1,data2,data3,data4);
        adapterBook.setClickListener(this);
        recyclerBook.setAdapter(adapterBook);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater MenuI = getMenuInflater();
        MenuI.inflate(R.menu.menu_item,menu);
        return true;
    }
    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(this, "You clicked " + adapterBook.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        alert(adapterBook.getItem(position));
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId==R.id.card){
//            Toast.makeText(this, "card", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ListViewKol.this,UserCard.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.boking) {
//            Toast.makeText(this, "booking", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,Booking.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.pinjam) {
//            Toast.makeText(this, "pinjam", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ListViewKol.this, Pinjam.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.kembali) {
//            Toast.makeText(this, "kembali", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ListViewKol.this, Kembali.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.logot) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            String cek = DB.cek();
            DB.delLog(cek);
            Intent intent = new Intent(ListViewKol.this, Login.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.home) {
            Intent intent = new Intent(ListViewKol.this, Home.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void alert(String id){
        new AlertDialog.Builder(ListViewKol.this)
                .setTitle("Delete Wishlist!")
                .setMessage("Delete Wishlist Book!\nAre you sure?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        MongoCollection koleksipribadi = mongoDatabase.getCollection("koleksipribadi");

                        Document del = new Document("_id", new ObjectId(id));
                        koleksipribadi.deleteOne(del).getAsync(val -> {
                            if (val.isSuccess()){
                                Toast.makeText(ListViewKol.this, "Wishlist Deleted!", Toast.LENGTH_SHORT).show();
                                ValueKoleksi();
                            } else {
                                Log.v("reject",val.getError().toString());
                            }
                        });
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                }).show();
    }
    public void ValueKoleksi(){
        String usr = DB.cek();
        MongoCollection kol = mongoDatabase.getCollection("koleksipribadi");

        //query to join 2 tables
        Document fJ  = new Document();
        fJ.put("from","buku");
        fJ.put("localField","_id_buku");
        fJ.put("foreignField","_id");
        fJ.put("as","data");
        List<Document> Join = Arrays.asList(
                new Document("$match",new Document("username",usr)),
                new Document("$lookup",fJ),
                new Document("$unwind","$data"),
                new Document("$sort", new Document("_id",-1)),
                new Document("$project", new Document("data.judul",1)
                        .append("data.kategori",1).append("data.penulis",1))
        );
        ArrayList<String> data1 = new ArrayList<>();
        ArrayList<String> data2 = new ArrayList<>();
        ArrayList<String> data3 = new ArrayList<>();
        ArrayList<String> data4 = new ArrayList<>();

        kol.aggregate(Join).iterator().getAsync(val -> {
            MongoCursor<Document> cursor = (MongoCursor) val.get();
//            Log.v("oke","nd");
            int x = 0;
            while (cursor.hasNext()){
//                Log.v("oke","rt");
                cursor.forEachRemaining(v -> {
                    String id = v.getObjectId("_id").toString();
                    Document data = (Document) v.get("data");
                    String jd = data.getString("judul");
                    String kat = data.getString("kategori");
                    String pen = data.getString("penulis");

                    data1.add(id);
                    data2.add(jd);
                    data3.add(kat);
                    data4.add(pen);
                });
                x++;
            }
            if (x==0){
//                Log.v("oke","kosong");
                Toast.makeText(this, "You haven't Wishlist Book!", Toast.LENGTH_SHORT).show();
                try {
                    RecyclerView recyclerBook = findViewById(R.id.listKoleksi);
                    recyclerBook.setLayoutManager(new LinearLayoutManager(this));
                    adapterBook = new RecyleKol(this, data1,data2,data3,data4);
                    adapterBook.setClickListener(this);
                    recyclerBook.setAdapter(adapterBook);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.v("oke","GALAT");
                }
                x=0;
            }
            else {
                Log.v("oke","Ada");
//                Log.v("oke",data1+"\n"+data2+"\n"+data3+"\n"+data4+"\n"+x);

                try {
                    RecyclerView recyclerBook = findViewById(R.id.listKoleksi);
                    recyclerBook.setLayoutManager(new LinearLayoutManager(this));
                    adapterBook = new RecyleKol(this, data1,data2,data3,data4);
                    adapterBook.setClickListener(this);
                    recyclerBook.setAdapter(adapterBook);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.v("oke","GALAT");
                }
                x=0;
            }
        });
//        Log.v("oke","st");
    }

    public void fresh(View view) {
        ValueKoleksi();
    }
}
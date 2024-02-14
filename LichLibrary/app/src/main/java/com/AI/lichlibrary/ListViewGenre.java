package com.AI.lichlibrary;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.bson.Document;

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

public class ListViewGenre extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{
    public MongoClient mongoClient;
    public MongoDatabase mongoDatabase;
    public MongoCollection<Document> mongoCollection;
    AtomicReference<User> users = new AtomicReference<User>();
    public User user;
    App app;
    protected Credentials credentials;
    DB DB;
    MyRecyclerViewAdapter adapter;
    MyRecyclerViewAdapter adapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_genre);
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

        GenValue();
    }
    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        MongoCollection book = mongoDatabase.getCollection("buku");
        ArrayList<String> data1 = new ArrayList<>();
        ArrayList<String> data2 = new ArrayList<>();
        ArrayList<String> data3 = new ArrayList<>();
        ArrayList<String> data4 = new ArrayList<>();
        String gen = adapter.getItem(position).toString();
//        Toast.makeText(this, gen, Toast.LENGTH_SHORT).show();
        Document fi = new Document();
        fi.put("kategori",gen);

        book.find(fi).sort(new Document("_id",-1)).iterator().getAsync(val -> {
            if (val.isSuccess()){
                MongoCursor<Document> value = (MongoCursor) val.get();
                int x = 0;
                while(value.hasNext()){
                    value.forEachRemaining(v -> {
                        String KD = v.getObjectId("_id").toString();
                        String TL = v.getString("judul");
                        String GN = v.getString("kategori");
                        String PN = v.getString("penulis");

                        data1.add(KD);
                        data2.add(TL);
                        data3.add(GN);
                        data4.add(PN);

                    });
                    x++;
                }
                if (x==0){
                    Toast.makeText(this, "Category Haven't Data Book!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(ListViewGenre.this, ListViewBook.class);
                    intent.putStringArrayListExtra("valKode",data1);
                    intent.putStringArrayListExtra("valJudul",data2);
                    intent.putStringArrayListExtra("valKategori",data3);
                    intent.putStringArrayListExtra("valPenulis",data4);

                    startActivity(intent);
                }
                x=0;
            }else {
                Toast.makeText(this, "Connection Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void onItemClick2(View view, int position) {
//        Toast.makeText(this, "You clicked " + adapter2.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        MongoCollection book = mongoDatabase.getCollection("buku");
        ArrayList<String> data1 = new ArrayList<>();
        ArrayList<String> data2 = new ArrayList<>();
        ArrayList<String> data3 = new ArrayList<>();
        ArrayList<String> data4 = new ArrayList<>();
        String gen = adapter2.getItem(position).toString();
//        Toast.makeText(this, gen, Toast.LENGTH_SHORT).show();
        Document fi = new Document();
        fi.put("kategori",gen);

        book.find(fi).iterator().getAsync(val -> {
            if (val.isSuccess()){
                MongoCursor<Document> value = (MongoCursor) val.get();
                int x =0;
                while(value.hasNext()){
                    value.forEachRemaining(v -> {
                        String KD = v.getObjectId("_id").toString();
                        String TL = v.getString("judul");
                        String GN = v.getString("kategori");
                        String PN = v.getString("penulis");

                        data1.add(KD);
                        data2.add(TL);
                        data3.add(GN);
                        data4.add(PN);

                    });
                    x++;
                }
                if (x==0){
                    Toast.makeText(this, "Category Haven't Data Book!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(ListViewGenre.this, ListViewBook.class);
                    intent.putStringArrayListExtra("valKode",data1);
                    intent.putStringArrayListExtra("valJudul",data2);
                    intent.putStringArrayListExtra("valKategori",data3);
                    intent.putStringArrayListExtra("valPenulis",data4);

                    startActivity(intent);
                }
                x=0;
            }else {
                Toast.makeText(this, "Connection Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //to make menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater MenuI = getMenuInflater();
        MenuI.inflate(R.menu.menu_item,menu);
        return true;
    }
    //to make func in menu item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId==R.id.card){
//            Toast.makeText(this, "card", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ListViewGenre.this,UserCard.class);
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
            Intent intent = new Intent(ListViewGenre.this, Pinjam.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.kembali) {
//            Toast.makeText(this, "kembali", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ListViewGenre.this, Kembali.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.logot) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            String cek = DB.cek();
            DB.delLog(cek);
            Intent intent = new Intent(ListViewGenre.this, Login.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.koleksi) {
            ValueKoleksi();
            return true;
        } else if (itemId==R.id.home) {
            Intent intent = new Intent(ListViewGenre.this, Home.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    public void GenValue(){
        MongoCollection kategoribuku = mongoDatabase.getCollection("kategoribuku");
        //Genre Values
        ArrayList<String> GenVal1 = new ArrayList<>();
        ArrayList<String> GenVal2 = new ArrayList<>();
        kategoribuku.find().sort(new Document("_id",-1)).iterator().getAsync(v2 -> {
            MongoCursor<Document> value = (MongoCursor) v2.get();
            int sz = 1;
            while (value.hasNext()){
//                Log.v("Genre",value.next().getString("genre"));
                String vv = value.next().getString("NamaKategori");
//                if (sz<=6){
                if (sz % 2 != 0){
                    GenVal1.add(vv);
                } else if (sz % 2 == 0) {
                    GenVal2.add(vv);
                }
                sz++;
//                }

            }

            RecyclerView recyclerView = findViewById(R.id.listView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new MyRecyclerViewAdapter(this, GenVal1);
            adapter.setClickListener(this::onItemClick);
            recyclerView.setAdapter(adapter);

            // set up the RecyclerView
            RecyclerView recyclerView2 = findViewById(R.id.listView2);
            recyclerView2.setLayoutManager(new LinearLayoutManager(this));
            adapter2 = new MyRecyclerViewAdapter(this, GenVal2);
            adapter2.setClickListener(this::onItemClick2);
            recyclerView2.setAdapter(adapter2);

            sz=0;
        });

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
                new Document("$sort", new Document("_id",-1))
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
                x=0;
            }
            else {
//                Log.v("oke","Ada");
//                Log.v("oke",data1+"\n"+data2+"\n"+data3+"\n"+data4+"\n"+x);

                try {
                    Intent intent = new Intent(ListViewGenre.this, ListViewKol.class);
                    intent.putStringArrayListExtra("valKode", data1);
                    intent.putStringArrayListExtra("valJudul", data2);
                    intent.putStringArrayListExtra("valKategori", data3);
                    intent.putStringArrayListExtra("valPenulis", data4);
                    startActivity(intent);
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.v("oke","GALAT");
                }
                x=0;
            }
        });
//        Log.v("oke","st");
    }
}
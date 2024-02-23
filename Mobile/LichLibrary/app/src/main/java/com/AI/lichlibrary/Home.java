package com.AI.lichlibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class Home extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    DB DB;
    MyRecyclerViewAdapter adapter;
    MyRecyclerViewAdapter adapter2;
    RecyleBook adapterBook;
    App app;
    protected Credentials credentials;
    public MongoClient mongoClient;
    public MongoCollection<Document> mongoCollection;
    public MongoDatabase mongoDatabase;
    EditText search;
    public User user;
    AtomicReference<User> users = new AtomicReference<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setTitle("Home");

        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.lich_logo_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DB = new DB(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0087FF")));

        search = findViewById(R.id.sc);

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

        KatValue();
        BookValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater MenuI = getMenuInflater();
        MenuI.inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId==R.id.card){
//            Toast.makeText(this, "card", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,UserCard.class);
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
            Intent intent = new Intent(Home.this, Pinjam.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.kembali) {
//            Toast.makeText(this, "kembali", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Home.this, Kembali.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.logot) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            String cek = DB.cek();
            DB.delLog(cek);
            Intent intent = new Intent(Home.this, Login.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.koleksi) {
            ValueKoleksi();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onItemClick(View view, int position) {
        MongoCollection book = mongoDatabase.getCollection("buku");
        ArrayList<String> data1 = new ArrayList<>();
        ArrayList<String> data2 = new ArrayList<>();
        ArrayList<String> data3 = new ArrayList<>();
        ArrayList<String> data4 = new ArrayList<>();
        String kategori = adapter.getItem(position).toString();
        book.aggregate(Arrays.asList(
                new Document("$match",new Document("kategori",kategori)),
                new Document("$sort",new Document("_id",-1)),
                new Document("$project",new Document("judul",1)
                        .append("kategori",1).append("penulis",1))
        )).iterator().getAsync(val -> {
            if (val.isSuccess()) {
                MongoCursor<Document> value = (MongoCursor) val.get();
                int x = 0;
                while (value.hasNext()) {
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
                if (x == 0) {
                    Toast.makeText(this, "Category Haven't Data Book!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(this, ListViewBook.class);
                intent.putStringArrayListExtra("valKode", data1);
                intent.putStringArrayListExtra("valJudul", data2);
                intent.putStringArrayListExtra("valKategori", data3);
                intent.putStringArrayListExtra("valPenulis", data4);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Connection Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onItemClick2(View view, int position) {
        MongoCollection book = mongoDatabase.getCollection("buku");
        ArrayList<String> data1 = new ArrayList<>();
        ArrayList<String> data2 = new ArrayList<>();
        ArrayList<String> data3 = new ArrayList<>();
        ArrayList<String> data4 = new ArrayList<>();
        String kategori = adapter2.getItem(position).toString();
        book.aggregate(Arrays.asList(
                new Document("$match",new Document("kategori",kategori)),
                new Document("$sort", new Document("_id",-1)),
                new Document("$project", new Document("judul",1)
                        .append("kategori",1).append("penulis",1))
        )).iterator().getAsync(val -> {
            if (val.isSuccess()) {
                MongoCursor<Document> value = (MongoCursor) val.get();
                int x = 0;
                while (value.hasNext()) {
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
                if (x == 0) {
                    Toast.makeText(this, "Category Haven't Data Book!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(this, ListViewBook.class);
                intent.putStringArrayListExtra("valKode", data1);
                intent.putStringArrayListExtra("valJudul", data2);
                intent.putStringArrayListExtra("valKategori", data3);
                intent.putStringArrayListExtra("valPenulis", data4);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Connection Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onItemClick3(View view, int position) {
        Intent intent = new Intent(this, ViewBook.class);
        intent.putExtra("_id", this.adapterBook.getItem(position).toString());
        startActivity(intent);
    }
    public void KatValue(){
        MongoCollection kategori = mongoDatabase.getCollection("kategoribuku");
        ArrayList<String> KatVal1 = new ArrayList<>();
        ArrayList<String> KatVal2 = new ArrayList<>();
        kategori.find().sort(new Document("_id",-1)).limit(6).iterator().getAsync(v2 -> {
            MongoCursor<Document> value = (MongoCursor) v2.get();
            int sz = 1;
            while (value.hasNext()) {
                String vv = value.next().getString("NamaKategori");
                if (sz <= 6) {
                    if (sz % 2 != 0) {
                        KatVal1.add(vv);
                    } else if (sz % 2 == 0) {
                        KatVal2.add(vv);
                    }
                    sz++;
                }
            }
            RecyclerView recyclerView = findViewById(R.id.listView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new MyRecyclerViewAdapter(this, KatVal1);
            adapter.setClickListener(this::onItemClick);
            recyclerView.setAdapter(adapter);

            RecyclerView recyclerView2 = findViewById(R.id.listView2);
            recyclerView2.setLayoutManager(new LinearLayoutManager(this));
            adapter2 = new MyRecyclerViewAdapter(this, KatVal2);
            adapter2.setClickListener(this::onItemClick2);
            recyclerView2.setAdapter(adapter2);

        });


    }
    public void BookValue(){
        MongoCollection book = mongoDatabase.getCollection("buku");
        ArrayList<String> BVal1 = new ArrayList<>();
        ArrayList<String> BVal2 = new ArrayList<>();
        ArrayList<String> BVal3 = new ArrayList<>();
        ArrayList<String> BVal4 = new ArrayList<>();
        ArrayList<String> BVal5 = new ArrayList<>();
        book.aggregate(Arrays.asList(
                new Document("$sort",new Document("_id",-1)),
                new Document("$limit",5),
                new Document("$project", new Document("judul",1)
                        .append("kategori",1).append("penulis",1))
        )).iterator().getAsync(v2 ->{
            MongoCursor<Document> value = (MongoCursor) v2.get();
            while (value.hasNext()) {
                    value.forEachRemaining(val2 ->{
                        String KD = val2.getObjectId("_id").toString();
                        String TL = val2.getString("judul");
                        String GN = val2.getString("kategori");
                        String PN = val2.getString("penulis");

                        BVal1.add(KD);
                        BVal2.add(TL);
                        BVal3.add(GN);
                        BVal4.add(PN);
                    });
            }
            RecyclerView recyclerBook = findViewById(R.id.listBook);
            recyclerBook.setLayoutManager(new LinearLayoutManager(this));
            adapterBook = new RecyleBook(this, BVal1, BVal2, BVal3, BVal4);
            adapterBook.setClickListener(this::onItemClick3);
            recyclerBook.setAdapter(adapterBook);
        });

    }
    public void fresh(View view) {
        KatValue();
        BookValue();
    }
    public void showKategori(View view) {
        Intent intent = new Intent(Home.this,ListViewGenre.class);
        startActivity(intent);
    }
    public void srcBook(View view){
        if (search.getText().toString().equals("")) {
            Toast.makeText(this, "Fill Search Column!", Toast.LENGTH_SHORT).show();
        }else {
            MongoCollection book = mongoDatabase.getCollection("buku");
            ArrayList<String> data1 = new ArrayList<>();
            ArrayList<String> data2 = new ArrayList<>();
            ArrayList<String> data3 = new ArrayList<>();
            ArrayList<String> data4 = new ArrayList<>();
            String src = search.getText().toString();

            List<Document> ag = Arrays.asList(
                    new Document("$match",new Document("$or",Arrays.asList(
                            new Document("judul",
                                    new Document("$regex","."+src+".*")),
                            new Document("judul",
                                    new Document("$regex",src+".*")),
                            new Document("judul",
                                    new Document("$regex","."+src))
                    ))),
                    new Document("$sort",new Document("_id",-1)),
                    new Document("$project", new Document("judul",1)
                            .append("kategori",1).append("penulis",1))
            );
            book.aggregate(ag).iterator().getAsync(val2 ->{
                if (val2.isSuccess()) {
                    int x = 0;
                    MongoCursor<Document> value = (MongoCursor) val2.get();
                    while (value.hasNext()) {
                        value.forEachRemaining(v2 ->{
                            String KD = v2.getObjectId("_id").toString();
                            String TL = v2.getString("judul");
                            String GN = v2.getString("kategori");
                            String PN = v2.getString("penulis");
                            data1.add(KD);
                            data2.add(TL);
                            data3.add(GN);
                            data4.add(PN);
                        });
                        x++;
                    }
                    if (x == 0) {
                        Toast.makeText(this, "Book doesn't exits!", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(this, ListViewBook.class);
                        intent.putStringArrayListExtra("valKode", data1);
                        intent.putStringArrayListExtra("valJudul", data2);
                        intent.putStringArrayListExtra("valKategori", data3);
                        intent.putStringArrayListExtra("valPenulis", data4);
                        startActivity(intent);
                    }
                }else {
                    Toast.makeText(this, "Failed Search Book!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void ValueKoleksi() {
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
                x=0;
            }
            else {
//                Log.v("oke","Ada");
//                Log.v("oke",data1+"\n"+data2+"\n"+data3+"\n"+data4+"\n"+x);

                try {
                    Intent intent = new Intent(Home.this, ListViewKol.class);
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
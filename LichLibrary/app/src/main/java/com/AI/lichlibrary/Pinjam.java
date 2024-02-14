package com.AI.lichlibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.SimpleDateFormat;
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

public class Pinjam extends AppCompatActivity {
    DB DB;
    App app;
    protected Credentials credentials;
    public MongoClient mongoClient;
    public MongoCollection<Document> mongoCollection;
    public MongoDatabase mongoDatabase;
    public User user;
    AtomicReference<User> users = new AtomicReference<>();
    RecylePinjam adapterPinjam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinjam);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setTitle("Home");

        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.lich_logo_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DB = new DB(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0087FF")));

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

        GetPinjam();
    }
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
            Intent intent = new Intent(Pinjam.this,UserCard.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.boking) {
//            Toast.makeText(this, "booking", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Pinjam.this, Booking.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.pinjam) {
//            Toast.makeText(this, "pinjam", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId==R.id.kembali) {
//            Toast.makeText(this, "kembali", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Pinjam.this, Kembali.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.logot) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            String cek = DB.cek();
            DB.delLog(cek);
            Intent intent = new Intent(Pinjam.this, Login.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.koleksi) {
            ValueKoleksi();
            return true;
        } else if (itemId==R.id.home) {
            Intent intent = new Intent(Pinjam.this, Home.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "Don't forget to return it on time!", Toast.LENGTH_SHORT).show();
    }
    private static String convertMongoDate(String val){
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat outputFormat= new SimpleDateFormat("E, MMM dd, yyyy");
        try {
            String finalStr = outputFormat.format(inputFormat.parse(val));
//        System.out.println(finalStr);
            return finalStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public void GetPinjam(){
        String usr = DB.cek();
        MongoCollection pinjam = mongoDatabase.getCollection("peminjaman");
        Document cek = new Document();
        cek.put("username",usr);
        cek.put("status","Dipinjam");

        ArrayList<String> data1 = new ArrayList<>();
        ArrayList<String> data2 = new ArrayList<>();
        ArrayList<String> data3 = new ArrayList<>();
        ArrayList<String> data4 = new ArrayList<>();
        ArrayList<Integer> data5 = new ArrayList<Integer>();

        //query to join 2 tables
        Document fJ  = new Document();
        fJ.put("from","buku");
        fJ.put("localField","_id_buku");
        fJ.put("foreignField","_id");
        fJ.put("as","data");
        List<Document> Join = Arrays.asList(
                new Document("$match",cek),
                new Document("$lookup",fJ),
                new Document("$unwind","$data"),
                new Document("$sort", new Document("_id",-1)),
                new Document("$project",
                        new Document("dateDifference",
                                new Document("$dateDiff",
                                        new Document("startDate","$tgl_kembali").append("endDate","$$NOW").append("unit","day")))
                                .append("tgl_pinjam",1).append("tgl_kembali",1).append("username",1).append("data",1))
        );

        pinjam.aggregate(Join).iterator().getAsync(val -> {
            MongoCursor cursor = (MongoCursor) val.get();
            while (cursor.hasNext()){
                Document doc = (Document) cursor.next();
                Document data = doc.get("data", Document.class);
                String nested = data.getString("judul");
                String id = doc.getObjectId("_id").toString();
                String pjm = doc.getDate("tgl_pinjam").toString();
                String kml = doc.getDate("tgl_kembali").toString();
//                System.out.println(id+"\n"+user+"\n"+sts+"\n"+nested);
                int diff = 0;
                try {
                    diff = Integer.parseInt(doc.get("dateDifference").toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                data1.add(id);
                data2.add(nested);
                data3.add(convertMongoDate(pjm));
                data4.add(convertMongoDate(kml));
                data5.add(diff);


            }
            RecyclerView recyclerBook = findViewById(R.id.listPinjam);
            recyclerBook.setLayoutManager(new LinearLayoutManager(this));
            adapterPinjam = new RecylePinjam(this, data1,data2,data3,data4,data5);
            adapterPinjam.setClickListener(this::onItemClick);
            recyclerBook.setAdapter(adapterPinjam);
        });
    }
    public void fresh(View view) {
        GetPinjam();
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
                    Intent intent = new Intent(Pinjam.this, ListViewKol.class);
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
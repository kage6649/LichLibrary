package com.AI.lichlibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.bson.Document;
import org.bson.types.ObjectId;

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

public class Kembali extends AppCompatActivity {
    public MongoClient mongoClient;
    public MongoDatabase mongoDatabase;
    public MongoCollection<Document> mongoCollection;
    AtomicReference<User> users = new AtomicReference<User>();
    public User user;
    App app;
    protected Credentials credentials;
    DB DB;
    RecyleKembali adapterKembali;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kembali);
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

        GetKembali();
    }
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
            Intent intent = new Intent(Kembali.this,UserCard.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.boking) {
//            Toast.makeText(this, "booking", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Kembali.this, Booking.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.pinjam) {
//            Toast.makeText(this, "pinjam", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Kembali.this, Pinjam.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.logot) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            String cek = DB.cek();
            DB.delLog(cek);
            Intent intent = new Intent(Kembali.this, Login.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.koleksi) {
            ValueKoleksi();
            return true;
        } else if (itemId==R.id.home) {
            Intent intent = new Intent(Kembali.this, Home.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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
    public void GetKembali(){
        String usr = DB.cek();
        MongoCollection kem = mongoDatabase.getCollection("peminjaman");
        Document cek = new Document("$or", Arrays.asList(
                new Document("status","Dikembalikan").append("username",usr),
                new Document("status","Hilang").append("username",usr)
        ));

        ArrayList<String> data1 = new ArrayList<>();
        ArrayList<String> data2 = new ArrayList<>();
        ArrayList<String> data3 = new ArrayList<>();
        ArrayList<String> data4 = new ArrayList<>();

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
                new Document("$project",new Document("data.judul",1)
                        .append("tgl_dikembalikan",1).append("denda",1))
        );

        kem.aggregate(Join).iterator().getAsync(val -> {
            MongoCursor cursor = (MongoCursor) val.get();
            while (cursor.hasNext()){
                Document doc = (Document) cursor.next();
                Document data = doc.get("data", Document.class);
                String nested = data.getString("judul");
                String id = doc.getObjectId("_id").toString();
                String kml = doc.getDate("tgl_dikembalikan").toString();
                String den = "";
                int d = doc.getInteger("denda");

                if (d==0){
                    den = "Tidak ada denda";
                }else {
                    den = "Rp "+d;
                }

                data1.add(id);
                data2.add(nested);
                data3.add(convertMongoDate(kml));
                data4.add(den);


            }
            RecyclerView recyclerBook = findViewById(R.id.listKembali);
            recyclerBook.setLayoutManager(new LinearLayoutManager(this));
            adapterKembali = new RecyleKembali(this, data1,data2,data3,data4);
            adapterKembali.setClickListener(this::onItemClick);
            recyclerBook.setAdapter(adapterKembali);
        });
    }

    private void onItemClick(View view, int i) {
//        Toast.makeText(this, adapterKembali.getItem(i).toString(), Toast.LENGTH_SHORT).show();
        String id = adapterKembali.getItem(i).toString();
        String usr = DB.cek();
        MongoCollection kem = mongoDatabase.getCollection("peminjaman");
        MongoCollection ul = mongoDatabase.getCollection("ulasanbuku");
        kem.aggregate(Arrays.asList(
                new Document("$match", new Document("$or",Arrays.asList(
                        new Document("status","Dikembalikan").append("username",usr),
                        new Document("status","Hilang").append("username",usr)
                ))),
                new Document("$sort",new Document("_id",-1)),
                new Document("$project",new Document("_id_buku",1))
        )).iterator().getAsync(val -> {
            MongoCursor<Document> value = (MongoCursor) val.get();
            ObjectId id_book = value.next().getObjectId("_id_buku");
            Document cek2 = new Document("_id_buku",id_book).append("username",usr);
            ul.find(cek2).iterator().getAsync(va -> {
                MongoCursor<Document> value2 = (MongoCursor) va.get();
                if (value2.hasNext()){
                    Toast.makeText(this, "Termakasih Telah Meminjam Buku!", Toast.LENGTH_SHORT).show();
                }else {
                    showDialog(Kembali.this,id_book);
                }
            });

        });
    }
    public void fresh(View view) {
        GetKembali();
    }
    public void showDialog(Activity activity,ObjectId id){

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_review);

        final EditText et = dialog.findViewById(R.id.et);
        final EditText et2 = dialog.findViewById(R.id.et2);

        Button btnok = (Button) dialog.findViewById(R.id.btnok);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et.length()==0||et2.length()==0){
                    Toast.makeText(activity, "Fill all column!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else {
                    int rat; String ul;
                    rat = Integer.parseInt(et.getText().toString());
                    ul = et2.getText().toString();

                    if (rat<=5){
                        Intent intent = getIntent();
                        String kode = intent.getStringExtra("_id");
                        String usr = DB.cek();
                        MongoCollection ulas = mongoDatabase.getCollection("ulasanbuku");

                        Document in = new Document();
                        in.put("username",usr);
                        in.put("_id_buku",id);
                        in.put("rating",rat);
                        in.put("ulasan",ul);

                        ulas.insertOne(in).getAsync(cek -> {
                            if (cek.isSuccess()){
                                Toast.makeText(Kembali.this, "Thanks for review!", Toast.LENGTH_SHORT).show();
//                                finish();
                            }else {
                                Toast.makeText(Kembali.this, "Error Review Book!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(Kembali.this, "Rating 1 until 5!", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
                dialog.dismiss();
            }
        });

        Button btncn = (Button) dialog.findViewById(R.id.btncn);
        btncn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
                x=0;
            }
            else {
//                Log.v("oke","Ada");
//                Log.v("oke",data1+"\n"+data2+"\n"+data3+"\n"+data4+"\n"+x);

                try {
                    Intent intent = new Intent(Kembali.this, ListViewKol.class);
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
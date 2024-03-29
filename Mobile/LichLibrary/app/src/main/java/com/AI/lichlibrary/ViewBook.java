package com.AI.lichlibrary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class ViewBook extends AppCompatActivity {
    public MongoClient mongoClient;
    public MongoDatabase mongoDatabase;
    public MongoCollection<Document> mongoCollection;
    AtomicReference<User> users = new AtomicReference<User>();
    public User user;
    App app;
    protected Credentials credentials;
    DB DB;
    TextView judul,penulis,penerbit,genre,tahun,stok;
    ImageView imgv;
    private static final String CHILD_DIR = "images";
    private static final String FILE_EXTENSION = ".png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

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

        judul = findViewById(R.id.judul);
        penulis = findViewById(R.id.penulis);
        penerbit = findViewById(R.id.penerbit);
        genre = findViewById(R.id.genre);
        tahun = findViewById(R.id.tahun);
        stok = findViewById(R.id.stok);
        imgv = findViewById(R.id.imgv);

        DB = new DB(this);

        getVal();
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
            Intent intent = new Intent(ViewBook.this,UserCard.class);
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
            Intent intent = new Intent(ViewBook.this, Pinjam.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.kembali) {
//            Toast.makeText(this, "kembali", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ViewBook.this, Kembali.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.logot) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            String cek = DB.cek();
            DB.delLog(cek);
            Intent intent = new Intent(ViewBook.this, Login.class);
            startActivity(intent);
            finish();
            return true;
        } else if (itemId==R.id.koleksi) {
            ValueKoleksi();
            return true;
        } else if (itemId==R.id.home) {
            Intent intent = new Intent(ViewBook.this, Home.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    public void getVal(){
        Intent intent = getIntent();
        String kode = intent.getStringExtra("_id");
//        Log.v("st#",kode);

        MongoCollection book = mongoDatabase.getCollection("buku");
        book.aggregate(Arrays.asList(
                new Document("$match",new Document("_id", new ObjectId(kode))),
                new Document("$lookup",new Document("from","kategoribuku_relasi")
                        .append("localField","_id")
                        .append("foreignField","_id_buku")
                        .append("as","data")),
                new Document("$unwind","$data"),
                new Document("$project",new Document("judul",1)
                        .append("penulis",1).append("penerbit",1)
                        .append("kategori",1).append("tahun",1)
                        .append("data.stok",1))
        )).iterator().getAsync(val -> {
//            Log.v("st#","0");
            if (val.isSuccess()){
                MongoCursor<Document> value = (MongoCursor) val.get();
                value.forEachRemaining(v -> {
                    judul.setText(v.getString("judul"));
                    penulis.setText(v.getString("penulis"));
                    penerbit.setText(v.getString("penerbit"));
                    genre.setText(v.getString("kategori"));
                    tahun.setText(v.getInteger("tahun").toString());
                    Document data_stok = (Document) v.get("data");
                    stok.setText(data_stok.getInteger("stok").toString());


                });
            }else {
                Toast.makeText(this, "Conection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        book.aggregate(Arrays.asList(
                new Document("$match",new Document("_id",new ObjectId(kode))),
                new Document("$project",new Document("img",1))
        )).iterator().getAsync(val -> {
            if(val.isSuccess()){
                MongoCursor<Document> value = (MongoCursor) val.get();
                value.forEachRemaining(v -> {
                    String img = kode;
//                    Log.v("st#","1");
                    try {
                        //to decode img string
                        byte[] data = android.util.Base64.decode(v.getString("img"), android.util.Base64.DEFAULT);
                        File cachePath = new File(this.getDataDir(), CHILD_DIR);
                        cachePath.mkdirs();
                        FileOutputStream stream = new FileOutputStream(cachePath + "/" + img + FILE_EXTENSION);
                        stream.write(data);
                        stream.close();
                        //to show img
                        File path = new File("/data/data/com.AI.lichlibrary/"+CHILD_DIR+"/"+img+FILE_EXTENSION);
                        imgv.setImageURI(Uri.fromFile(path));
//                        Log.v("st#","2");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }else{
                Toast.makeText(this, "Image Load Failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void boking(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Masa Peminjaman!");
            builder.setMessage("Masukkan masa peminjaman buku!\nMaksimal 7 hari");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Booking", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int masa;
                masa = Integer.parseInt(input.getText().toString());

                if (masa<=7){
                    Intent intent = getIntent();
                    String kode = intent.getStringExtra("_id");
                    String usr = DB.cek();
                    MongoCollection boking = mongoDatabase.getCollection("peminjaman");

                    Document in = new Document();
                    in.put("_id_buku",new ObjectId(kode));
                    in.put("username",usr);
                    in.put("masa",masa);
                    in.put("status","Booking");

                    boking.insertOne(in).getAsync(cek -> {
                        if (cek.isSuccess()){
                            Toast.makeText(ViewBook.this, "Please Pick Up a Book at The Staff Center!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(ViewBook.this, "Error Booking Book!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(ViewBook.this, "Maksimal 7 hari!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void review(View view) {
        Intent intent_get = getIntent();
        String kode = intent_get.getStringExtra("_id");

        MongoCollection ulas = mongoDatabase.getCollection("ulasanbuku");
        ArrayList<String> data1 = new ArrayList<>();
        ArrayList<String> data2 = new ArrayList<>();
        ArrayList<String> data3 = new ArrayList<>();
        ArrayList<String> data4 = new ArrayList<>();

        ulas.find(new Document("_id_buku",new ObjectId(kode))).sort(new Document("_id",-1)).iterator().getAsync(val ->{
            if (val.isSuccess()){
                MongoCursor<Document> value = (MongoCursor) val.get();
                int x = 0;
                try {
                    while (value.hasNext()){
                        value.forEachRemaining(v -> {
                            String KD = v.getObjectId("_id").toString();
                            String TL = v.getString("ulasan");
                            String GN = v.getString("username");
                            String PN = v.getInteger("rating").toString();
                            data1.add(KD);
                            data2.add(TL);
                            data3.add(GN);
                            data4.add(PN);
                        });
//                        Log.v("ok","ok"+x);
                        x++;
                    }
//                    Log.v("ok","ok");
                    if (x == 0) {
                        Toast.makeText(this, "Book Haven't Review!", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    Log.v("ok","ok-nd");
                    Intent intent = new Intent(ViewBook.this, ListReview.class);
                    intent.putStringArrayListExtra("valKode", data1);
                    intent.putStringArrayListExtra("valJudul", data2);
                    intent.putStringArrayListExtra("valKategori", data3);
                    intent.putStringArrayListExtra("valPenulis", data4);
                    startActivity(intent);
//                    Log.v("ok",data1.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this, "Connection Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addkoleksi(View view) {
        Intent intent_get = getIntent();
        String kode = intent_get.getStringExtra("_id");
        String usr = DB.cek();
        MongoCollection kol = mongoDatabase.getCollection("koleksipribadi");
        Document cek = new Document("_id_buku", new ObjectId(kode)).append("username",usr);
        kol.find(cek).iterator().getAsync(val -> {
            if (val.isSuccess()){
                MongoCursor<Document> value = (MongoCursor) val.get();
                int x = 0;
                if (value.hasNext()){
//                    Log.v("oke","ada");
                    Toast.makeText(this, "You Already Wishlist this Book!", Toast.LENGTH_SHORT).show();
                    x++;
                }
                if (x==0){
//                    Log.v("oke","kosong");
                    kol.insertOne(cek).getAsync(va -> {
                        if (va.isSuccess()){
                            Toast.makeText(this, "You Wishlist this Book!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    x=0;
                }
            }
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
                    Intent intent = new Intent(ViewBook.this, ListViewKol.class);
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
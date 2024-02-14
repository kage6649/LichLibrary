/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package LichLibrary.menu;

import LichLibrary.config.cont;
import LichLibrary.config.route;
import static LichLibrary.menu.Booking.addDaysToSQLDate;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author eater
 */
public class Pinjam extends javax.swing.JFrame {
    String point;
    cont c = new cont();
    route r = new route();
    int x = 0,d2 = 0;
    String PJ = "";
    private static final long DAY_IN_MILLSEC = 1000L * 60L * 60L * 24L;
    String level = "";
    /**
     * Creates new form Pinjam
     */
    public Pinjam() {
        initComponents();
        //to add level
        try {
        c.con = DriverManager.getConnection(c.url);
        c.stm = c.con.createStatement();
        String show = "select * from log";
//      String del = "delete from log where status='login'";
        c.rs = c.stm.executeQuery(show);
        if (c.rs.next()) {                
        level = c.rs.getString("level");
        }               
         c.stm.close();c.con.close();
         } catch (Exception e) {
         e.printStackTrace();
         }
        
        logout.setVisible(false);
        users.setVisible(false);
        denda.setVisible(false);
        line1.setVisible(false);
        line2.setVisible(false);
        Booking.setVisible(false);
        book.setVisible(false);
        pinjam.setVisible(false);
        laporan.setVisible(false);
        kategori.setVisible(false);
        home.setVisible(false);
        staf.setVisible(false);
        
        act.setVisible(false);
        acct.setVisible(false);
        err.setVisible(false);
        inHari.setVisible(false);
        lHari.setVisible(false);
        sv.setVisible(false);
        lost.setVisible(false);
        gfine.setVisible(false);
        
        setTable();
        table_PJ_val();
        table_KM_val();
    }
    public void setTable(){
        JTableHeader tableHeader = KMtable.getTableHeader();
        KMtable.setBackground(Color.WHITE);
        tableHeader.setFont(new Font("Segoe UI",Font.BOLD,12));
        tableHeader.setForeground(Color.BLACK);
        
        JTableHeader tableHeaderPJ = PJtable.getTableHeader();
        PJtable.setBackground(Color.WHITE);
        tableHeaderPJ.setFont(new Font("Segoe UI",Font.BOLD,12));
        tableHeaderPJ.setForeground(Color.BLACK);
    }
    public void table_PJ_val(){
        MongoCollection<Document> pijm = c.db.getCollection("peminjaman");
        MongoCollection<Document> book = c.db.getCollection("buku");
        
        DefaultTableModel dt = (DefaultTableModel) PJtable.getModel();
        dt.setRowCount(0);
        
        FindIterable<Document> show = pijm.find(Filters.eq("status","Dipinjam")).sort(new Document("_id",-1));
        show.forEach(val -> {
            Vector v = new Vector();
            v.add(val.getObjectId("_id"));
            v.add(val.getString("username"));
            String date_n;//date_x;
            FindIterable<Document> show2 = book.find(Filters.eq("_id", val.getObjectId("_id_buku")));
            show2.forEach(val2-> {
                v.add(val2.getString("judul"));
            });
            date_n = val.getDate("tgl_kembali").toString();
//            date_x = val.getDate("tgl_kembali").toString();
            v.add(convertMongoDate(date_n));
//            v.add(convertMongoDate(date_x));
            
            dt.addRow(v);
        });
    }
    public void table_KM_val(){
        MongoCollection<Document> book = c.db.getCollection("buku");
        MongoCollection<Document> kem = c.db.getCollection("peminjaman");
        
        DefaultTableModel dt = (DefaultTableModel) KMtable.getModel();
        dt.setRowCount(0);
        Document cek = new Document("$or",Arrays.asList(
                new Document("status", "Dikembalikan"),new Document("status", "Hilang")
        ));
        FindIterable<Document> show = kem.find(cek).sort(new Document("_id",-1));
        show.forEach(val -> {
            Vector v = new Vector();
            v.add(val.getString("username"));
            String date_k;
            FindIterable<Document> show2 = book.find(Filters.eq("_id", val.getObjectId("_id_buku")));
            show2.forEach(val2-> {
                v.add(val2.getString("judul"));
            });
            int d = val.getInteger("denda");
            if (d==0) {
                v.add("Tidak ada denda");
            } else {
                v.add("Rp "+d+"");
            }
            date_k = val.getDate("tgl_dikembalikan").toString();
            v.add(convertMongoDate(date_k));
            dt.addRow(v);
        });
        
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        menu = new javax.swing.JLabel();
        logout = new javax.swing.JLabel();
        users = new javax.swing.JLabel();
        line2 = new javax.swing.JPanel();
        line1 = new javax.swing.JPanel();
        denda = new javax.swing.JLabel();
        book = new javax.swing.JLabel();
        Booking = new javax.swing.JLabel();
        kategori = new javax.swing.JLabel();
        pinjam = new javax.swing.JLabel();
        home = new javax.swing.JLabel();
        laporan = new javax.swing.JLabel();
        staf = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pijam = new javax.swing.JScrollPane();
        KMtable = new javax.swing.JTable();
        boking = new javax.swing.JScrollPane();
        PJtable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        reUs = new javax.swing.JLabel();
        act = new javax.swing.JPanel();
        err = new javax.swing.JLabel();
        acct = new javax.swing.JButton();
        inHari = new javax.swing.JTextField();
        lHari = new javax.swing.JLabel();
        sv = new javax.swing.JButton();
        lost = new javax.swing.JButton();
        gfine = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(862, 531));
        setResizable(false);

        jPanel5.setBackground(new java.awt.Color(0, 153, 255));

        jLabel5.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Lich_nav.png")).getImage().getScaledInstance(64, 55, Image.SCALE_DEFAULT)));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(774, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(97, 97, 97))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        menu.setBackground(new java.awt.Color(255, 255, 255));
        menu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-menu-48.png"))); // NOI18N
        menu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuMouseClicked(evt);
            }
        });

        logout.setBackground(new java.awt.Color(255, 255, 255));
        logout.setFont(new java.awt.Font("Verdana", 1, 11)); // NOI18N
        logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/exit.png"))); // NOI18N
        logout.setText("Logout");
        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutMouseClicked(evt);
            }
        });

        users.setBackground(new java.awt.Color(255, 255, 255));
        users.setFont(new java.awt.Font("Verdana", 1, 11)); // NOI18N
        users.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/user.png"))); // NOI18N
        users.setText("Users");
        users.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                usersMouseClicked(evt);
            }
        });

        line2.setBackground(new java.awt.Color(0, 153, 255));
        line2.setPreferredSize(new java.awt.Dimension(120, 3));

        javax.swing.GroupLayout line2Layout = new javax.swing.GroupLayout(line2);
        line2.setLayout(line2Layout);
        line2Layout.setHorizontalGroup(
            line2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        line2Layout.setVerticalGroup(
            line2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        line1.setBackground(new java.awt.Color(0, 153, 255));
        line1.setPreferredSize(new java.awt.Dimension(120, 3));

        javax.swing.GroupLayout line1Layout = new javax.swing.GroupLayout(line1);
        line1.setLayout(line1Layout);
        line1Layout.setHorizontalGroup(
            line1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        line1Layout.setVerticalGroup(
            line1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        denda.setBackground(new java.awt.Color(255, 255, 255));
        denda.setFont(new java.awt.Font("Verdana", 1, 11)); // NOI18N
        denda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/denda.png"))); // NOI18N
        denda.setText("Denda");
        denda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dendaMouseClicked(evt);
            }
        });

        book.setBackground(new java.awt.Color(255, 255, 255));
        book.setFont(new java.awt.Font("Verdana", 1, 11)); // NOI18N
        book.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/open-book.png"))); // NOI18N
        book.setText("Buku");
        book.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookMouseClicked(evt);
            }
        });

        Booking.setBackground(new java.awt.Color(255, 255, 255));
        Booking.setFont(new java.awt.Font("Verdana", 1, 11)); // NOI18N
        Booking.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/booking.png"))); // NOI18N
        Booking.setText("Booking");
        Booking.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BookingMouseClicked(evt);
            }
        });

        kategori.setBackground(new java.awt.Color(255, 255, 255));
        kategori.setFont(new java.awt.Font("Verdana", 1, 11)); // NOI18N
        kategori.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/genre.png"))); // NOI18N
        kategori.setText("Kategori");
        kategori.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kategoriMouseClicked(evt);
            }
        });

        pinjam.setBackground(new java.awt.Color(255, 255, 255));
        pinjam.setFont(new java.awt.Font("Verdana", 1, 11)); // NOI18N
        pinjam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/borrow.png"))); // NOI18N
        pinjam.setText("Peminjaman");
        pinjam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pinjamMouseClicked(evt);
            }
        });

        home.setBackground(new java.awt.Color(255, 255, 255));
        home.setFont(new java.awt.Font("Verdana", 1, 11)); // NOI18N
        home.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/home.png"))); // NOI18N
        home.setText("Home");
        home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homeMouseClicked(evt);
            }
        });

        laporan.setBackground(new java.awt.Color(255, 255, 255));
        laporan.setFont(new java.awt.Font("Verdana", 1, 11)); // NOI18N
        laporan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/writing.png"))); // NOI18N
        laporan.setText("Laporan");
        laporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                laporanMouseClicked(evt);
            }
        });

        staf.setBackground(new java.awt.Color(255, 255, 255));
        staf.setFont(new java.awt.Font("Verdana", 1, 11)); // NOI18N
        staf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/staf.png"))); // NOI18N
        staf.setText("Staf");
        staf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stafMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(menu)
                    .addComponent(line1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(line2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(home)
                            .addComponent(Booking)
                            .addComponent(book)
                            .addComponent(kategori)
                            .addComponent(pinjam)
                            .addComponent(denda)
                            .addComponent(logout)
                            .addComponent(laporan)
                            .addComponent(users)
                            .addComponent(staf))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(line1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(home)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(staf)
                .addGap(12, 12, 12)
                .addComponent(users)
                .addGap(12, 12, 12)
                .addComponent(book)
                .addGap(12, 12, 12)
                .addComponent(kategori)
                .addGap(12, 12, 12)
                .addComponent(Booking)
                .addGap(12, 12, 12)
                .addComponent(pinjam)
                .addGap(12, 12, 12)
                .addComponent(denda)
                .addGap(12, 12, 12)
                .addComponent(laporan)
                .addGap(12, 12, 12)
                .addComponent(logout)
                .addGap(12, 12, 12)
                .addComponent(line2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        KMtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Username", "Judul", "Denda", "Tanggal Pengenbalian"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        KMtable.setFocusable(false);
        KMtable.setGridColor(new java.awt.Color(0, 0, 0));
        KMtable.setOpaque(false);
        KMtable.setSelectionBackground(new java.awt.Color(204, 0, 0));
        KMtable.setShowGrid(true);
        KMtable.setShowVerticalLines(false);
        KMtable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                KMtableMouseClicked(evt);
            }
        });
        pijam.setViewportView(KMtable);

        jTabbedPane1.addTab("Pengembalian", pijam);

        PJtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Username", "Judul", "Tenggat Waktu"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        PJtable.setFocusable(false);
        PJtable.setGridColor(new java.awt.Color(0, 0, 0));
        PJtable.setOpaque(false);
        PJtable.setSelectionBackground(new java.awt.Color(255, 0, 0));
        PJtable.setShowGrid(true);
        PJtable.setShowVerticalLines(false);
        PJtable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PJtableMouseClicked(evt);
            }
        });
        boking.setViewportView(PJtable);

        jTabbedPane1.addTab("Peminjaman", boking);

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        jLabel2.setText("Pengembalian");

        reUs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refreshh.png"))); // NOI18N
        reUs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reUsMouseClicked(evt);
            }
        });

        act.setBackground(new java.awt.Color(255, 255, 255));

        err.setBackground(new java.awt.Color(255, 255, 255));
        err.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        err.setForeground(new java.awt.Color(255, 51, 51));
        err.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/warning.png"))); // NOI18N

        acct.setBackground(new java.awt.Color(0, 153, 255));
        acct.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        acct.setForeground(new java.awt.Color(255, 255, 255));
        acct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/moneysearch.png"))); // NOI18N
        acct.setText("Check");
        acct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acctActionPerformed(evt);
            }
        });

        inHari.setEditable(false);
        inHari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inHariActionPerformed(evt);
            }
        });
        inHari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                inHariKeyTyped(evt);
            }
        });

        lHari.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lHari.setText("Rp");

        sv.setBackground(new java.awt.Color(0, 204, 0));
        sv.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        sv.setForeground(new java.awt.Color(255, 255, 255));
        sv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/borrow.png"))); // NOI18N
        sv.setText("Received");
        sv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                svActionPerformed(evt);
            }
        });

        lost.setBackground(new java.awt.Color(255, 0, 0));
        lost.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lost.setForeground(new java.awt.Color(255, 255, 255));
        lost.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/lost.png"))); // NOI18N
        lost.setText("Lost");
        lost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lostActionPerformed(evt);
            }
        });

        gfine.setBackground(new java.awt.Color(0, 204, 0));
        gfine.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        gfine.setForeground(new java.awt.Color(255, 255, 255));
        gfine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/denda.png"))); // NOI18N
        gfine.setText("Get Fine");
        gfine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gfineActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout actLayout = new javax.swing.GroupLayout(act);
        act.setLayout(actLayout);
        actLayout.setHorizontalGroup(
            actLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(actLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(actLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(actLayout.createSequentialGroup()
                        .addComponent(err, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(21, 21, 21))
                    .addGroup(actLayout.createSequentialGroup()
                        .addGroup(actLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(gfine, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lost, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(actLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lHari)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(inHari))
                            .addComponent(sv, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                            .addComponent(acct, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        actLayout.setVerticalGroup(
            actLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(actLayout.createSequentialGroup()
                .addComponent(err)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(acct)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(actLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lHari)
                    .addComponent(inHari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lost)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(gfine)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sv)
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(reUs))
                            .addComponent(act, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reUs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(act, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuMouseClicked
        // TODO add your handling code here:
        if (x==0) {
            logout.setVisible(true);
            users.setVisible(true);
            denda.setVisible(true);
            line1.setVisible(true);
            line2.setVisible(true);
            Booking.setVisible(true);
            book.setVisible(true);
            pinjam.setVisible(true);
            laporan.setVisible(true);
            kategori.setVisible(true);
            home.setVisible(true);
            if (level.equals("admin")) {
                staf.setVisible(true);
            }
            x=1;
        }else if(x==1){
            home.setVisible(false);
            users.setVisible(false);
            logout.setVisible(false);
            denda.setVisible(false);
            line1.setVisible(false);
            line2.setVisible(false);
            Booking.setVisible(false);
            book.setVisible(false);
            pinjam.setVisible(false);
            laporan.setVisible(false);
            kategori.setVisible(false);
            staf.setVisible(false);
            x=0;
        }
    }//GEN-LAST:event_menuMouseClicked

    private void logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseClicked
        // TODO add your handling code here:
        try {
            c.con = DriverManager.getConnection(c.url);
            c.stm = c.con.createStatement();
            String q = "delete from log where username='"+point+"'";
            c.stm.executeUpdate(q);
            r.toLogin();
            this.dispose();
            c.stm.close();c.con.close();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_logoutMouseClicked

    private void usersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usersMouseClicked
        // TODO add your handling code here:
        this.dispose();r.toUsers();
    }//GEN-LAST:event_usersMouseClicked

    private void dendaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dendaMouseClicked
        // TODO add your handling code here:
        this.dispose();r.toDenda();
    }//GEN-LAST:event_dendaMouseClicked

    private void bookMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookMouseClicked
        // TODO add your handling code here:
        this.dispose();r.toBook();
    }//GEN-LAST:event_bookMouseClicked

    private void BookingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BookingMouseClicked
        // TODO add your handling code here:
        this.dispose();r.toBoking();
    }//GEN-LAST:event_BookingMouseClicked

    private void kategoriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kategoriMouseClicked
        // TODO add your handling code here:
        this.dispose();r.toKategori();
    }//GEN-LAST:event_kategoriMouseClicked

    private void pinjamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pinjamMouseClicked
        // TODO add your handling code here:
//        this.dispose();r.toPinjam();
    }//GEN-LAST:event_pinjamMouseClicked

    private void homeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeMouseClicked
        // TODO add your handling code here:
        this.dispose();r.toHome();
    }//GEN-LAST:event_homeMouseClicked

    private void laporanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laporanMouseClicked
        // TODO add your handling code here:
        this.dispose();r.toLaporan();
    }//GEN-LAST:event_laporanMouseClicked

    private void stafMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stafMouseClicked
        // TODO add your handling code here:
                this.dispose();r.toStaf();
    }//GEN-LAST:event_stafMouseClicked

    private void KMtableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_KMtableMouseClicked
        // TODO add your handling code here:
        act.setVisible(false);
        lost.setVisible(false);
        inHari.setVisible(false);
        lHari.setVisible(false);
        sv.setVisible(false);
        gfine.setVisible(false);
    }//GEN-LAST:event_KMtableMouseClicked

    private void PJtableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PJtableMouseClicked
        // TODO add your handling code here:
        int[] td = PJtable.getSelectedRows();
        int n = 0;
        for(int dt : td){
            n = 10*n+dt;
        }
        PJ=PJtable.getValueAt(n, 0).toString();
        //        cek(bk);
        act.setVisible(true);
        lost.setVisible(true);
        acct.setVisible(true);
        inHari.setVisible(false);
        lHari.setVisible(false);
        sv.setVisible(false);
        gfine.setVisible(false);
        inHari.setText("");
    }//GEN-LAST:event_PJtableMouseClicked

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        // TODO add your handling code here:
        act.setVisible(false);
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void reUsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reUsMouseClicked
        // TODO add your handling code here:
        table_PJ_val();
        table_KM_val();
        act.setVisible(false);
        inHari.setVisible(false);
        lHari.setVisible(false);
        sv.setVisible(false);
        gfine.setVisible(false);
    }//GEN-LAST:event_reUsMouseClicked

    private void acctActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acctActionPerformed
        // TODO add your handling code here:
        MongoCollection<Document> pijm = c.db.getCollection("peminjaman");
        MongoCollection<Document> denda = c.db.getCollection("denda");
        Calendar cal = Calendar.getInstance();
        java.sql.Date date_now = new java.sql.Date(cal.getTime().getTime());
        List<Document> a =  Arrays.asList(
                new Document("$match",new Document("_id",new ObjectId(PJ))),
                new Document("$project",
                        new Document("dateDifference",
                                new Document("$dateDiff",
                                        new Document("startDate","$tgl_kembali").append("endDate","$$NOW").append("unit","day"))))
        );
        AggregateIterable<Document> cek = pijm.aggregate(a);
        cek.forEach(val -> {
            int x = Integer.parseInt(val.get("dateDifference").toString());
            if (x>0) {
                FindIterable<Document> get = denda.find(Filters.eq("num", 1));
                get.forEach(v -> {
                    int d = v.getInteger("denda");
                    int r = x*d;
                    inHari.setText(r+"");
                });
            }else{
                inHari.setText("0");
            }
        });
        inHari.setVisible(true);
        lHari.setVisible(true);
        sv.setVisible(true);
        lost.setVisible(false);
        //        inHari.setText("");
    }//GEN-LAST:event_acctActionPerformed

    private void inHariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inHariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inHariActionPerformed

    private void inHariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inHariKeyTyped
        // TODO add your handling code here:
        char tlp = evt.getKeyChar();

        if (!Character.isDigit(tlp)) {
            evt.consume();
        }
    }//GEN-LAST:event_inHariKeyTyped

    private void svActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svActionPerformed
        // TODO add your handling code here:
        MongoCollection<Document> pijm = c.db.getCollection("peminjaman");
        MongoCollection<Document> pijm_d = c.db.getCollection("kategoribuku_relasi");

        int denda = Integer.parseInt(inHari.getText().toString());
        Calendar cal = Calendar.getInstance();
        java.sql.Date date_now = new java.sql.Date(cal.getTime().getTime());
        //        java.sql.Date date_back = addDaysToSQLDate(date_now, date_kem);
        try {
            pijm.updateMany(Filters.eq("_id", new ObjectId(PJ)),Arrays.asList(
            Updates.set("status", "Dikembalikan"),
            Updates.set("tgl_dikembalikan", date_now),
            Updates.set("denda", denda)
        ));
            FindIterable<Document> show = pijm.find(Filters.eq("_id", new ObjectId(PJ)));
            show.forEach(val -> {
                pijm_d.updateOne(Filters.eq("_id_buku", val.getObjectId("_id_buku")), Updates.inc("stok", 1));
            });
            err.setText("Sucess Received Book!");
            err.setForeground(Color.GREEN);
            err.setIcon(new ImageIcon(getClass().getResource("/icon/check.png")));
            err.setVisible(true);
            //delay showoff
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(() -> {
                err.setVisible(false);
                act.setVisible(false);
                err.setForeground(Color.red);
                err.setIcon(new ImageIcon(getClass().getResource("/icon/warning.png")));
                inHari.setText("");
                inHari.setVisible(false);
                lHari.setVisible(false);
                sv.setVisible(false);
                gfine.setVisible(false);
            }, 5, TimeUnit.SECONDS);
        } catch (Exception e) {
            err.setText("Failed Received Book!");
            err.setVisible(true);
            //delay showoff
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(() -> {
                err.setVisible(false);
                act.setVisible(false);
                inHari.setText("");
                inHari.setVisible(false);
                lHari.setVisible(false);
                sv.setVisible(false);
                gfine.setVisible(false);
            }, 5, TimeUnit.SECONDS);
        }
        table_KM_val();
        table_PJ_val();

    }//GEN-LAST:event_svActionPerformed

    private void lostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lostActionPerformed
        // TODO add your handling code here:
        MongoCollection<Document> pijm = c.db.getCollection("peminjaman");
        MongoCollection<Document> denda = c.db.getCollection("denda");
        Calendar cal = Calendar.getInstance();
        java.sql.Date date_now = new java.sql.Date(cal.getTime().getTime());
        List<Document> a =  Arrays.asList(
                new Document("$match",new Document("_id",new ObjectId(PJ))),
                new Document("$project",
                        new Document("dateDifference",
                                new Document("$dateDiff",
                                        new Document("startDate","$tgl_kembali").append("endDate","$$NOW").append("unit","day"))))
        );
        AggregateIterable<Document> cek = pijm.aggregate(a);
        cek.forEach(val -> {
            int x = Integer.parseInt(val.get("dateDifference").toString());
            if (x>0) {
                FindIterable<Document> get = denda.find(Filters.eq("num", 1));
                FindIterable<Document> get2 = denda.find(Filters.eq("num", 2));
                get2.forEach(v2 -> {
                    d2 = v2.getInteger("denda");
                });
                get.forEach(v -> {
                    int d = v.getInteger("denda");
                    int r = (x*d)+d2;
                    inHari.setText(r+"");
                });
            }else{
                FindIterable<Document> get2 = denda.find(Filters.eq("num", 2));
                get2.forEach(v2 -> {
                    d2 = v2.getInteger("denda");
                    int r = d2;
                    inHari.setText(r+"");
                });
            }
        });
        inHari.setVisible(true);
        lHari.setVisible(true);
        //        sv.setVisible(true);
        gfine.setVisible(true);
        acct.setVisible(false);
    }//GEN-LAST:event_lostActionPerformed

    private void gfineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gfineActionPerformed
        // TODO add your handling code here:
        MongoCollection<Document> pijm = c.db.getCollection("peminjaman");
//        MongoCollection<Document> pijm_d = c.db.getCollection("kategoribuku_relasi");
        MongoCollection<Document> book = c.db.getCollection("buku");

        int denda = Integer.parseInt(inHari.getText().toString());
        Calendar cal = Calendar.getInstance();
        java.sql.Date date_now = new java.sql.Date(cal.getTime().getTime());
        //        java.sql.Date date_back = addDaysToSQLDate(date_now, date_kem);
        try {
            pijm.updateMany(Filters.eq("_id", new ObjectId(PJ)),Arrays.asList(
            Updates.set("status", "Hilang"),
            Updates.set("tgl_dikembalikan", date_now),
            Updates.set("denda", denda)
        ));
            FindIterable<Document> show = pijm.find(Filters.eq("_id", new ObjectId(PJ)));
            show.forEach(val -> {
                book.updateOne(Filters.eq("_id", val.getObjectId("_id_buku")), Updates.inc("stok", -1));
            });
            err.setText("Sucess Received Fine!");
            err.setForeground(Color.GREEN);
            err.setIcon(new ImageIcon(getClass().getResource("/icon/check.png")));
            err.setVisible(true);
            //delay showoff
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(() -> {
                err.setVisible(false);
                act.setVisible(false);
                err.setForeground(Color.red);
                err.setIcon(new ImageIcon(getClass().getResource("/icon/warning.png")));
                inHari.setText("");
                inHari.setVisible(false);
                lHari.setVisible(false);
                sv.setVisible(false);
                gfine.setVisible(false);
            }, 5, TimeUnit.SECONDS);
        } catch (Exception e) {
            err.setText("Failed Received Book!");
            err.setVisible(true);
            //delay showoff
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(() -> {
                err.setVisible(false);
                act.setVisible(false);
                inHari.setText("");
                inHari.setVisible(false);
                lHari.setVisible(false);
                sv.setVisible(false);
                gfine.setVisible(false);
            }, 5, TimeUnit.SECONDS);
        }
        table_KM_val();
        table_PJ_val();

    }//GEN-LAST:event_gfineActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Pinjam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pinjam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pinjam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pinjam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pinjam().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Booking;
    private javax.swing.JTable KMtable;
    private javax.swing.JTable PJtable;
    private javax.swing.JButton acct;
    private javax.swing.JPanel act;
    private javax.swing.JScrollPane boking;
    private javax.swing.JLabel book;
    private javax.swing.JLabel denda;
    private javax.swing.JLabel err;
    private javax.swing.JButton gfine;
    private javax.swing.JLabel home;
    private javax.swing.JTextField inHari;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel kategori;
    private javax.swing.JLabel lHari;
    private javax.swing.JLabel laporan;
    private javax.swing.JPanel line1;
    private javax.swing.JPanel line2;
    private javax.swing.JLabel logout;
    private javax.swing.JButton lost;
    private javax.swing.JLabel menu;
    private javax.swing.JScrollPane pijam;
    private javax.swing.JLabel pinjam;
    private javax.swing.JLabel reUs;
    private javax.swing.JLabel staf;
    private javax.swing.JButton sv;
    private javax.swing.JLabel users;
    // End of variables declaration//GEN-END:variables
}

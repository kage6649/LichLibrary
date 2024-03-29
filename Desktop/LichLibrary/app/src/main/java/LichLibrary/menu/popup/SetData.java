/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package LichLibrary.menu.popup;

import LichLibrary.config.cont;
import LichLibrary.config.route;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import java.awt.Color;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import org.bson.Document;

/**
 *
 * @author eater
 */
public class SetData extends javax.swing.JFrame {
    cont c = new cont();
    route r = new route();
    /**
     * Creates new form Setting
     */
    public SetData() {
        initComponents();
        err.setVisible(false);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        set = new javax.swing.JButton();
        check = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        err = new javax.swing.JLabel();
        cl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 255), 2, true));
        jPanel1.setForeground(new java.awt.Color(0, 153, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(325, 254));

        set.setBackground(new java.awt.Color(255, 0, 0));
        set.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        set.setForeground(new java.awt.Color(255, 255, 255));
        set.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/database.png"))); // NOI18N
        set.setText("Reset");
        set.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setActionPerformed(evt);
            }
        });

        check.setText("Yes, Reset Database");

        jLabel14.setText("Confirm if you want to reset the database");

        jLabel15.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 0, 0));
        jLabel15.setText("Reset Database");

        err.setBackground(new java.awt.Color(255, 255, 255));
        err.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        err.setForeground(new java.awt.Color(255, 51, 51));
        err.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/warning.png"))); // NOI18N

        cl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/back-arrow.png"))); // NOI18N
        cl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(jLabel15))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(check))))
                        .addGap(0, 25, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(err, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cl)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(set)
                .addGap(79, 79, 79))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(err)
                    .addComponent(cl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addGap(38, 38, 38)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(check)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(set)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void setActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setActionPerformed
        // TODO add your handling code here:
        if (check.isSelected()) {
            //reset database
            MongoCollection<Document> users = c.db.getCollection("user");
            MongoCollection<Document> book = c.db.getCollection("buku");
            MongoCollection<Document> kategori = c.db.getCollection("kategoribuku");
            MongoCollection<Document> pinjam = c.db.getCollection("peminjaman");
            MongoCollection<Document> kategori_d = c.db.getCollection("kategoribuku_relasi");
            MongoCollection<Document> denda = c.db.getCollection("denda");
            MongoCollection<Document> koleksi = c.db.getCollection("koleksipribadi");
            MongoCollection<Document> ulasan = c.db.getCollection("ulasanbuku");

            try {
                users.deleteMany(new Document());
                book.deleteMany(new Document());
                koleksi.deleteMany(new Document());
                kategori.deleteMany(new Document());
                pinjam.deleteMany(new Document());
                kategori_d.deleteMany(new Document());
                ulasan.deleteMany(new Document());

                denda.updateOne(Filters.eq("num", 1), Updates.set("denda", 0));
                denda.updateOne(Filters.eq("num", 2), Updates.set("denda", 0));
                
                Document val = new Document("username","admin").append("password", "39383C39345D574059444D")
                        .append("email", "admin@lmail.com").append("NamaLengkap", "Lich Admin")
                        .append("Alamat", "None").append("level", "admin");
                users.insertOne(val);

                err.setText("Reset Database Successs!");
                err.setForeground(Color.GREEN);
                err.setIcon(new ImageIcon(getClass().getResource("/icon/check.png")));
                err.setVisible(true);
                //delay showoff
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.schedule(() -> {
                    this.dispose();
                    err.setVisible(false);
                    err.setForeground(Color.red);
                    err.setIcon(new ImageIcon(getClass().getResource("/icon/warning.png")));
                }, 5, TimeUnit.SECONDS);
            } catch (Exception e) {
                err.setText("Error Reset Database!");
                err.setVisible(true);
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.schedule(() -> {
                    err.setVisible(false);
                }, 5, TimeUnit.SECONDS);
            }

        }else{
            err.setText("Check first to continue!");
            err.setVisible(true);
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(() -> {
                err.setVisible(false);
            }, 5, TimeUnit.SECONDS);
        }
    }//GEN-LAST:event_setActionPerformed

    private void clMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clMouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_clMouseClicked

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
            java.util.logging.Logger.getLogger(SetData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SetData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SetData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SetData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SetData().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox check;
    private javax.swing.JLabel cl;
    private javax.swing.JLabel err;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton set;
    // End of variables declaration//GEN-END:variables
}

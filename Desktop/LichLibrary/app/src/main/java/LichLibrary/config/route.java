/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LichLibrary.config;
//import com.perpusProto.Login;
//import LichLibrary.menu.Users;
import LichLibrary.*;
/**
 *
 * @author Eater
 */
public class route {
    public void toLogin(){
        Login l = new Login();
        l.setVisible(true);
    }
    public void toHome(){
        LichLibrary.menu.Home h = new LichLibrary.menu.Home();
        h.setVisible(true);
    }
//    public void toHome(){
//        LichLibrary.menu.home h = new LichLibrary.menu.home();
//        h.setVisible(true);
//    }
//    //menu route Admin
//    public void toAdmin(){
//        com.perpusProto.menu.admin.Add s = new com.perpusProto.menu.admin.Add();
//        s.setVisible(true);
//    }
//    public void toReport(){
//        com.perpusProto.menu.admin.ReportAdmin r = new com.perpusProto.menu.admin.ReportAdmin();
//        r.setVisible(true);
//    }
//    public void toSetAdmin(){
//        com.perpusProto.menu.admin.SettAdmin s = new com.perpusProto.menu.admin.SettAdmin();
//        s.setVisible(true);
//    }
//    //menu route 
    public void toUsers(){
        LichLibrary.menu.Users u = new LichLibrary.menu.Users();
        u.setVisible(true);
    }
    public void toBook(){
        LichLibrary.menu.Book u = new LichLibrary.menu.Book();
        u.setVisible(true);
    }
    public void toKategori(){
        LichLibrary.menu.Kategori u = new LichLibrary.menu.Kategori();
        u.setVisible(true);
    }
    public void toBoking(){
        LichLibrary.menu.Booking u = new LichLibrary.menu.Booking();
        u.setVisible(true);
    }
    public void toPinjam(){
        LichLibrary.menu.Pinjam u = new LichLibrary.menu.Pinjam();
        u.setVisible(true);
    }
    public void toLaporan(){
        LichLibrary.menu.Laporan u = new LichLibrary.menu.Laporan();
        u.setVisible(true);
    }
    public void toDenda(){
        LichLibrary.menu.Denda u = new LichLibrary.menu.Denda();
        u.setVisible(true);
    }
    public void toStaf(){
        LichLibrary.menu.Staf s = new LichLibrary.menu.Staf();
        s.setVisible(true);
    }
    //Popup program
    public void toKpop(){
        LichLibrary.menu.popup.kPopup k = new LichLibrary.menu.popup.kPopup();
        k.setVisible(true);
    }
    public void toAddBook(){
        LichLibrary.menu.popup.AddBook b = new LichLibrary.menu.popup.AddBook();
        b.setVisible(true);
    }
    public void toSetAdmin(){
        LichLibrary.menu.popup.SetAdmin s = new LichLibrary.menu.popup.SetAdmin();
        s.setVisible(true);
    }
    public void toSetting(){
        LichLibrary.menu.popup.SetData s = new LichLibrary.menu.popup.SetData();
        s.setVisible(true);
    }
    
}

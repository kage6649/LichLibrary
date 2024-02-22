/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LichLibrary.config;
//import com.mongodb.gridfs.*;s
import java.io.*;
import java.util.Base64;

/**
 *
 * @author eater
 */
public class getIMG {
        
        public String encodeImage(String path) throws FileNotFoundException, IOException{
            FileInputStream imgstream = new FileInputStream(path);
            byte[] data = imgstream.readAllBytes();
            String dataimg = Base64.getEncoder().encodeToString(data);
            
            imgstream.close();
            return dataimg;
        }
        public void decodeimg(String strimg, String path) throws FileNotFoundException, IOException{
            byte[] data = Base64.getDecoder().decode(strimg);
            FileOutputStream img = new FileOutputStream(path);
            img.write(data);
            img.close();
        
        }
        
//        public static void main(String[] args) throws IOException {
//        JFileChooser j = new JFileChooser();
//        j.showOpenDialog(null);
//            System.out.println(j.getSelectedFile().getPath());
//            String x = encodeImage(j.getSelectedFile().getPath());
//            System.out.println(x);
//            decodeimg(x, "C:\\Users\\eater\\Downloads\\kucing.png");
//    }
        
}

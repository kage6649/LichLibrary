/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LichLibrary.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.sql.*;

/**
 *
 * @author Eater
 */
public class cont {
     //if using online mongodb | mongodb-atlas
    private final String connectionString = "mongodb+srv://LichProgrammer:JavaLichProgram@clusterlich.zqjgaet.mongodb.net/?retryWrites=true&w=majority";
    //connection URL, in mongodb project connection   
    private final ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
       private final MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        public final MongoClient mc = MongoClients.create(settings);
        public final MongoDatabase db = mc.getDatabase("LichLibrary");
        
        public final String url = "jdbc:sqlite:Log.db";
        public Connection con;
        public Statement stm;
        public ResultSet rs;
        
        
}

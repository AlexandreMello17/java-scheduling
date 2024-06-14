/*
 * 
 * by. Alexandre Mello
 * 
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;






public class Conexao {
    
    public Connection getConnection()throws SQLException {
    Connection conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aplicacao", "postgres","X@nde17930");
    return conexao;
        
    }
    
}
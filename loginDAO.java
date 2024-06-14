/*
 * 
 * 
 * by. Alexandre Mello
 * 
 * 
 */

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;



public class loginDAO {
    
    public void cadastrarUsuario(String bookCodeField, String bookNameField, String emailField, String text2)throws SQLException {
        
        Connection conexao = new Conexao().getConnection();
        String sql = "INSERT INTO login (nome, telefone, data, hora, servico) VALUES ('"+bookCodeField+"','"+bookNameField+"','"+emailField+"')";
        PreparedStatement statment = conexao.prepareStatement(sql);
        statment.execute();
        conexao.close();

        
        
    }

    public void cadastrarUsuario(String text, String text2, String text3) {
    
    }
}
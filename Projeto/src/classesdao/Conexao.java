
package classesdao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
   static Connection conn;
    
    private final String url = "jdbc:mysql://localhost:3306/Asi";
    private final String usuario = "root";
    private final String senha = "root";

    public static Connection getConn() {
        return conn;
    }
    
   
    
    public boolean conectar() {
        try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(url,usuario,senha);
        return true;
       
        } catch(ClassNotFoundException ex) {
            System.out.println("erro ao encontrar classe");
           return false;
        }
            catch(SQLException e ) {
                System.out.println("Erro de sintaxe");
                return false;
              }
                 
    }
    
    
    
    public void desconectar() {
        try {
            conn.close();
        } catch(SQLException e) {
            
        }
    }
    
}

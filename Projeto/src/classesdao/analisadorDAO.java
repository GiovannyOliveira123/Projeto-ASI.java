
package classesdao;

import java.sql.Connection;
import utilitarios.AnalisarLink;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import usuario.usuarioAtual;

public class analisadorDAO {
Connection conn = Conexao.getConn();    


public void gerarRelatorioLink(String url, int risco, LocalDate data,String result, int idUser) {
 
    String sql = "INSERT INTO Analise_link (link_verificado,nivel_risco,data_analise,resultado,id_usuario) VALUES (?,?,?,?,?)";
    
    try {
    PreparedStatement psmt = conn.prepareStatement(sql);
    
    psmt.setString(1, url);
    psmt.setInt(2, risco);
    psmt.setObject(3, data);
    psmt.setString(4, result);
    psmt.setInt(5, idUser);
    
    psmt.executeUpdate();
    
    } catch(SQLException e) {
        JOptionPane.showMessageDialog(null, "Erro de sintaxe sql: "+e.getSQLState());
    }
}   
    
    public void gerarRelatorioArquivo(String arq, int risco, LocalDate data,String result, int idUser) {
 
        String sql = "INSERT INTO Analise_arquivo (arquivo_verificado,nivel_risco,data_analise,resultado,id_usuario) VALUES (?,?,?,?,?)";
    
    try {
    PreparedStatement psmt = conn.prepareStatement(sql);
    
    psmt.setString(1, arq   );
    psmt.setInt(2, risco);
    psmt.setObject(3, data);
    psmt.setString(4, result);
    psmt.setInt(5, idUser);
    
    psmt.executeUpdate();
    
    } catch(SQLException e) {
        JOptionPane.showMessageDialog(null, "Erro de sintaxe sql: "+e.getSQLState());
    }
}   
    
    

    
}


package classesdao;

import java.sql.Connection;
import senhas.Senhas;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SenhasDAO {
 Connection conn = Conexao.getConn();    
 
  public List<Senhas> buscar(int id_usuario) {
      
      List<Senhas> lista = new ArrayList<>();
      String sql = "SELECT id_senha,nome_servico,iv,senha_criptografada FROM Senha WHERE id_usuario = ?";
      
      try {
      PreparedStatement psmt = conn.prepareStatement(sql);
      
      psmt.setInt(1, id_usuario);
      
      ResultSet rs = psmt.executeQuery();
      
     
      int id;
      String servico;
      String iv;
      String senhaCripto;
      Senhas senha = new Senhas();
      
      while(rs.next()) {
      id = rs.getInt("id_senha");
      servico = rs.getString("nome_servico");
      iv = rs.getString("iv");
      senhaCripto = rs.getString("senha_criptografada");
          
      senha.setIdSenha(id);
      senha.setServico(servico);
      senha.setIv(iv);
      senha.setSenhaCriptografada(senhaCripto);
      senha.setIdUsuario(id_usuario);
      
       lista.add(senha);
      
      }
      return lista;
      
      } catch(SQLException e) {
          JOptionPane.showMessageDialog(null, "Erro de sintaxe sql: "+e.getSQLState());
          return null;
      }
  }
 
  public void addSenha(Senhas senha) {
      
     String sql = "INSERT INTO Senha (nome_servico, iv, senha_criptografada, id_usuario) VALUES (?, ?, ?, ?)";
      
     try {
     PreparedStatement psmt = conn.prepareStatement(sql);
     
     psmt.setString(1,senha.getServico());
     psmt.setString(2, senha.getIv());
     psmt.setString(3, senha.getSenhaCriptografada());
     psmt.setInt(4, senha.getIdUsuario());
     
     psmt.executeUpdate();
     
     } 
     
     
     catch(SQLException e) {
         JOptionPane.showMessageDialog(null, "Erro de sintaxe sql "+e.getSQLState());
     }
      
  }
  
  
  public void editarSenha(Senhas senha) {
    String sql = "UPDATE Senha SET nome_servico = ?, iv = ?, senha_criptografada = ? WHERE id_senha = ?;";
    
    try {
    PreparedStatement psmt = conn.prepareStatement(sql);
    
    psmt.setString(1, senha.getServico());
    psmt.setString(2, senha.getIv());
    psmt.setString(3, senha.getSenhaCriptografada());
    psmt.setInt(4, senha.getIdSenha());
    
    psmt.executeUpdate();
    
    } catch(SQLException e) {
        JOptionPane.showMessageDialog(null, "Erro de sintaxe sql: "+e.getSQLState());
        
        
    }
  }
  
   public void excluirSenha(int id) {
    String sql = "DELETE FROM Senha WHERE id_senha = ?";
    
    try {
    PreparedStatement psmt = conn.prepareStatement(sql);
    
    psmt.setInt(1, id);
    
    psmt.executeUpdate();
    
    } catch(SQLException e) {
        JOptionPane.showMessageDialog(null, "Erro de sintaxe sql: "+e.getSQLState());
        
        
    }
   }
 
}

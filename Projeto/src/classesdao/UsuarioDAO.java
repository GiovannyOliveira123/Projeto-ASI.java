
package classesdao;

import usuario.Usuario;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import usuario.usuarioAtual;

public class UsuarioDAO {
    Connection conn = Conexao.getConn();
    
   
    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO Usuario (nome,senha_hash,email,telefone,salt,senha_mestra_hash,data_criacao) VALUES (?,?,?,?,?,?,?)";
            
     try {
    PreparedStatement pstm = conn.prepareStatement(sql);
    
    
    pstm.setString(1,usuario.getNome());
    pstm.setString(2, usuario.getSenhaHash());
    pstm.setString(3, usuario.getEmail());
    pstm.setString(4, usuario.getTelefone());
    pstm.setString(5, usuario.getSalt());
    pstm.setString(6, usuario.getSenhaMestraHash());
    pstm.setObject(7, usuario.getDataCriacao());
   
    pstm.executeUpdate();
    
     } catch(SQLException e) {
         e.getErrorCode();
     }
}
    
    public void buscarEmail(String mail) {
        String sql = "SELECT id_usuario,email,senha_hash FROM Usuario WHERE email = ?";
        try {
            
        PreparedStatement pstm = conn.prepareStatement(sql);
        
        pstm.setString(1, mail);
        
        ResultSet rs = pstm.executeQuery();
        
        while(rs.next()) {
           
          int id = rs.getInt("id_usuario");
          String email = rs.getString("email");
          String senhaHash = rs.getString("senha_hash");
           
          if(email != null && senhaHash != null) {
            Usuario user = new Usuario();
            user.setEmail(email);
            user.setIdUsuario(id);
            user.setSenhaHash(senhaHash);
            usuarioAtual.setUser(user);
          } 
        }
        
        
        
        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro de sintaxe "+e.getSQLState());
        }
         
    }
    
  
        
    
    
     public Usuario buscarAll(int id) {
        String sql = "SELECT nome,telefone,senha_mestra_hash,salt,data_criacao FROM Usuario WHERE id_usuario = ?";
        try {
            
        PreparedStatement pstm = conn.prepareStatement(sql);
        
        pstm.setInt(1, id);
         
        Usuario user = usuarioAtual.getUser();

        ResultSet rs = pstm.executeQuery();
        
        while(rs.next()) {
           
            
          String nome = rs.getString("nome");
          String tel = rs.getString("telefone");
          String hashMestra = rs.getString("senha_mestra_hash");
          String salt = rs.getString("salt");
          LocalDate criacao = rs.getObject("data_criacao", LocalDate.class);
            
          
            user.setNome(nome);
            user.setTelefone(tel);
            user.setSenhaMestraHash(hashMestra);
            user.setSalt(salt);
            user.setDataCriacao(criacao);
            
        }
                    return user;

        
        
        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro de sintaxe "+e.getSQLState());
            return null;
        }
    
    
     }   
}
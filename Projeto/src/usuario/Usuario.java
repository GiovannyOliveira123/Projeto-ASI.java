
package usuario;

import java.time.LocalDate;

public class Usuario {
private int idUsuario; 
private String nome = "";
private String email = "";
private String telefone = "";
private String salt;
private String senhaHash = "";
private String senhaMestraHash = "";
private LocalDate dataCriacao;

    public Usuario() {
    }
    
    public Usuario(String nome, String email, String telefone, String salt, String senhaHash, LocalDate dataCadastro) {
        
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.salt = salt;
        this.senhaHash = senhaHash;
        this.dataCriacao = dataCadastro;
    }
    
    public Usuario(String nome, String email, String telefone, String salt, String senhaHash, String senhaMestraHash, LocalDate dataCadastro) {
        
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.salt = salt;
        this.senhaHash = senhaHash;
        this.senhaMestraHash = senhaMestraHash;
        this.dataCriacao = dataCadastro;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getSenhaMestraHash() {
        return senhaMestraHash;
    }

    public void setSenhaMestraHash(String senhaMestraHash) {
        this.senhaMestraHash = senhaMestraHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    
    
    
}

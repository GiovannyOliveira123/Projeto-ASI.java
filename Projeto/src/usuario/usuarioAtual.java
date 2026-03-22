
package usuario;


public class usuarioAtual {
 
 private static Usuario usuarioAtual;
 private static String chaveAES;
 
 
    
  
 public static Usuario getUser() {
      
     return usuarioAtual;      
  }
 
     public static void setUser(Usuario user) {     
         usuarioAtual = user;
      }

    public static String getChaveAES() {
        return chaveAES;
    }

    public static void setChave(String chave) {
        chaveAES = chave;
    }
     
     
    
}

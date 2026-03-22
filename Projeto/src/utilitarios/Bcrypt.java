
package utilitarios;

import java.security.SecureRandom;
import java.util.Base64;
import org.mindrot.jbcrypt.BCrypt;

public class Bcrypt {
    
    
    public static String gerarSalt() {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
         String s = Base64.getEncoder().encodeToString(salt);
      return s;
    }
    
    public static String gerarHash(String senha) {
      String hash = BCrypt.hashpw(senha, BCrypt.gensalt());
        
      return hash;
      
    }
    
    public static boolean compararHash(String senha, String hash) {
        return BCrypt.checkpw(senha, hash);
    }
    
    
}

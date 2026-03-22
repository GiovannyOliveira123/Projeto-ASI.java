
package main;

import classesdao.Conexao;
import telas.telaLogin;

public class Main {
    
    public static void main(String[] args) {
        
        Conexao conect = new Conexao();
        
        if(conect.conectar()) {

        telaLogin tl = new telaLogin();
        
        tl.setVisible(true);
        } 
    }
    
}


package senhas;

import java.util.ArrayList;
import java.util.List;


public class listaSen {
private static List<Senhas> listSenhas = new ArrayList();
   
public static void add(Senhas s) {
    listSenhas.add(s);
}
public static List<Senhas> get() {
    return listSenhas;
}

public static void set(List<Senhas> s) {
    listSenhas = s;
}

public static int tamanhoList() {
    return listSenhas.size();
}


}

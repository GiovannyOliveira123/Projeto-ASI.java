
package utilitarios;

import classesdao.analisadorDAO;
import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class AnalisarLink extends Analisador{
 private final String linkVerificado;

 public AnalisarLink(String link) {
     this.linkVerificado = link;
 }
 
 
 public String getLinkVerificado() {
 return linkVerificado;   
}

    public String getResultado() {
        return resultado;
    }

    public LocalDate getDataAnalise() {
        return dataAnalise;
    }


    
    private String normalizarUrl(String url) {
       
        url = url.replaceAll("[^a-zA-Z0-9]", "");
        
           // Mapeamento de substituições
        Map<Character, Character> mapa = new HashMap<>();
        mapa.put('0', 'o');
        mapa.put('1', 'i');
        mapa.put('4', 'a');
        mapa.put('3', 'e');
        mapa.put('@', 'a');
        
         StringBuilder sb = new StringBuilder();
        for (char c : url.toCharArray()) {   //Percorre cada caractere da url
            sb.append(mapa.getOrDefault(c, c)); //Se o valor for encontrado na url, substitui pela chave correspondente
        }
        url = sb.toString();
        
        return url.trim().toLowerCase();
    }
    
    private int padroesSuspeitos(String url) {
     String dominioAntes = extrairDominio(url);   //Dominio base
     String dominioDepois = normalizarUrl(dominioAntes); //Dominio depois de normalizar
       
     
     if(!dominioAntes.equals(dominioDepois)) { 
         nivelRisco += 20;        //Se o dominio for diferente antes e depois da normalização = alteração de alguns Caracteres
     }                                        
   
     return nivelRisco;
    }
    
    

    private String extrairDominio(String url) {
        
            //Caso a url não contenha protocolo, o sistema adiciona
            if (!url.matches("^[a-zA-Z][a-zA-Z0-9+.-]*://.*")) {
            url = "http://" + url;
           
        }
        
        try {
            String host = new URI(url).getHost();   //Armazena o host na variavel
            if (host == null) return "";
            String[] p = host.split("\\.");
            return p.length >= 2 ? p[p.length - 2] : host; //Retorna o host Sem protocolo, www, ou extensão)
        } catch (Exception e) {
            return "";
        }
    }
    
    
    
    

    private String[] extrairSubDominios(String url) {
        
          if (!url.matches("^[a-zA-Z][a-zA-Z0-9+.-]*://.*")) {    //Caso a url informada não contenha protocolo, adicione http
            url = "http://" + url;    
           
        }
  
        try {
            String host = new URI(url).getHost(); //Recebe o host da url
            if (host == null) return new String[]{""}; //Caso nada seja encontrado, retorne valor algum
            String[] partes = host.split("\\."); //Recebe as partes da url divididas por "."
            if (partes.length <= 2) return new String[]{""}; //Caso tenha 2 ou menos partes retorna subdominios vazios
            String[] sub = new String[partes.length - 2];  //Subdominios são as partes que não correspondem ao dominio legitimo e o final
            System.arraycopy(partes, 0, sub, 0, partes.length - 2);
            
            //Normaliza o subdominio
            for(int i = 0; i < sub.length; i++) {
            sub[i] = normalizarUrl(sub[i]);
            }
            
            return sub;
        } catch (Exception e) {
            return new String[]{""}; 
        }
    }

   
    
    
    
    
    private int compararDominio(String url) {
        String[] dominiosConhecidos = {
            "instagram", "facebook", "youtube", "nubank",
            "netflix", "paypal", "google", "store.steampowered",
            "steampowered", "microsoft","ebay","spotify","apple"
        };
     
        String dominio = extrairDominio(url); //Dominio base
        String dominioNormalizado = normalizarUrl(dominio); //Dominio após aplicar a normalização
        
        
        for (String conhecido : dominiosConhecidos) {
            
          
       
            //Caso o dominio normalizado for igual a um dominio conhecido mas
            //o dominio base não, marca como suspeito de phishing
            if(dominioNormalizado.contains(conhecido) && !dominio.equals(conhecido)) {
              
                nivelRisco += 40;
            }
        }

        String[] subDominios = extrairSubDominios(url);
        
        if (subDominios.length >= 5) nivelRisco += 40; //Caso a url apresente muitos subdominios, suspeita de phishing
        else if (subDominios.length >= 3) nivelRisco += 20;

        
        //Se um subdominio for igual a um dominio legitimo, marca como subdomio falso, um padrão comum de phishing
        for (String sub : subDominios) {
            for (String conhecido : dominiosConhecidos) {
                if ((sub).contains(conhecido)) { 
                 nivelRisco += 40;
                
                }
                
            }
        }
        
        return nivelRisco;
    }

    
    
    
    
    private int buscarPalavrasChave(String url) {
    // Palavras comuns em sites falsos de phishing
    String[] palavrasSuspeitas = {
        "login", "access", "auth", "secure", "security", "verify", "authentication",
        "account", "accont", "urgent", "alert", "warning", "action-required",
        "login-account", "free", "redirect", "return","promo"
    };

    // Extrai domínio e parte restante do link
    String dominio = extrairDominio(url).replaceAll("[^a-z0-9]", "");
    int inicio = url.indexOf(dominio) + dominio.length();
    String restante = (inicio > 0 && inicio < url.length()) ? url.substring(inicio) : "";
    
    // Quebra o restante do link em fragmentos (parâmetros e diretórios)
    String[] diretorios = restante.split("[\\/=?&_\\-]+");

    // Extrai subdomínios para analisar nomes suspeitos
    String[] subDominios = extrairSubDominios(url);

    
    
    // Percorre todas as palavras suspeitas
    for (String palavra : palavrasSuspeitas) {
        // Verifica se algum subdomínio contém a palavra suspeita
        for (String sub : subDominios) {
            if (sub.toLowerCase().contains(palavra)) {
                nivelRisco += 5;
            }
        }

        // Verifica se algum diretório contém a palavra suspeita
        for (String dir : diretorios) {
            if (dir.toLowerCase().contains(palavra)) {
                nivelRisco += 5;
            }
        }
    }

    // Penaliza fortemente se houver redirecionamentos explícitos
    if (url.contains("redirect") || url.contains("url=")) {
        nivelRisco += 20;
    }

    return nivelRisco;
}


    
    
    private int verificarTamanho(String url) {
        if (url.length() >= 80) nivelRisco += 20;
        else if (url.length() >= 60) nivelRisco += 10;
        return nivelRisco;
    }
    
    
    

    private int verificarHTTPS(String url) {
        try {
            String protocolo = new URI(url).getScheme();
            if (protocolo == null || protocolo.equalsIgnoreCase("http")) {
                nivelRisco += 10;
            }
        } catch (Exception e) {
            nivelRisco += 10;
        }
        return nivelRisco;
    }
    
    
    

    public int analisarLink() {
        
        String url = linkVerificado;
        
        
        padroesSuspeitos(url);
        verificarHTTPS(url);
        verificarTamanho(url);
        compararDominio(url);
        buscarPalavrasChave(url);
        
        analisadorDAO adao = new analisadorDAO();
        
        return nivelRisco;
        
    }
}


package utilitarios;

import java.io.File;
import java.io.IOException;
import static java.lang.Math.log;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;

public class AnalisadorArquivo extends Analisador {

    public String getResultado() {
        return resultado;
    }

    
    
 private int compararCabecalhos(File arq, String extensao) {
        
            
        Map<String, byte[]> numerosMagicos = new 
 HashMap<>();
        
                    
          //Mapa das extensões e seus headers correspondentes
          numerosMagicos.put("pdf", new byte[] { 0x25, 0x50, 0x44, 0x46 }); //Headers em hexa das extensões
          numerosMagicos.put("png", new byte[] { (byte) 0x89, 0x50, 0x4e, 0x47 }); //Adiciona (byte) para dizer ao compilador que aceita o valor sendo interpretado como negativo
          numerosMagicos.put("exe", new byte[] { 0x4D, 0x5A, 00, 00});
          numerosMagicos.put("zip", new byte[] { 0x50, 0x4b, 0x03, 0x04 });  //Arquivos compactados como zip e docx possuem a mesma
          numerosMagicos.put("docx", new byte[] { 0x50, 0x4b, 0x03, 0x04 }); //Assinatura
          numerosMagicos.put("elf", new byte[] { 0x7F, 0x45, 0x4C, 0x46 }); //Elf: executavel do linux
          numerosMagicos.put("jpg", new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0 });
          
          
          Set<String> chaves = numerosMagicos.keySet();
        

        try {
            
            byte[] conteudo = Files.readAllBytes(arq.toPath()); //Recebe todos os bytes do arquivo
            byte[] headers = new byte[4]; 
          
            
              if(extensao.equals("exe")) {
                 for(int i = 0; i < 2; i++) {
                headers[i] = conteudo[i]; //Pega apenas os 2 primeiros bytes caso seja um exe 
             } 
            }
                 else {
                 for(int i = 0; i < 4; i++) {
                headers[i] = conteudo[i]; //Pega apenas os 4 primeiros bytes (Os headers)  
                         }
            
           
            }
            
            if(extensao.equals("txt")) {
                
                for(int i = 0; i < numerosMagicos.size(); i++) { //Percorre todo o tamanho do mapa numeros
                
                  for(String s : chaves) { //Percorre todas as chaves guardadas no mapa
                    
             if(Arrays.equals(headers, numerosMagicos.get(s))) { //Se o header do arquivo for igual o valor da chave atual
                JOptionPane.showMessageDialog(null, "Falso txt detectado");
                 nivelRisco += 20;
             }
            
                  }
                    break;
                    
                }
            }
            
            
            byte[] cabecalhoExtensao = numerosMagicos.get(extensao); //Recebe o valor da chave do mapa com base na extensão que o arquivo diz ter
       
        
            
            
           if(!(Arrays.equals(cabecalhoExtensao, headers)) && !extensao.equals("txt") ){
                JOptionPane.showMessageDialog(null,"Falsa extensão detectada");
                nivelRisco += 20; //Se encontar uma falsa extensão adicione nivel risco com mais 20 pontos
           }
                
                
                 //Se a extensão não declarar o arquivo como executavel e ele for um executavel, adiciona mais 40 pontos
                if(!(extensao.equals("exe")) && Arrays.equals(headers, numerosMagicos.get("exe")) ) {
                    
                JOptionPane.showMessageDialog(null, "ALERTA: Arquivo se diz "+extensao+" mas é um executavel disfarçado");
                    nivelRisco += 20;
                    extensao = "exe";
                    
                } if(!(extensao.equals("elf")) && Arrays.equals(headers, numerosMagicos.get("elf")) ) {
                    
               JOptionPane.showMessageDialog(null, "ALERTA: Arquivo se diz "+extensao+" mas é um executavel disfarçado");
               nivelRisco += 20;
               extensao = "elf";
            }
          
                
          
            analisarEntropia(arq, extensao);

        } catch (IOException e) {
            e.printStackTrace();
        }
   
     return nivelRisco;
     
 }   
     
    private int analisarEntropia(File arq, String tipoArquivo) {
        
         try {
         
            byte[] conteudo = Files.readAllBytes(arq.toPath()); //Recebe todos os bytes do arquivo 
            byte[] bytesPorBloco = new byte[256];
            byte b; //Variavel que vai percorrer cada byte do conteudo
            int[] contador = new int[256]; //Array que vai armazenar a quantidade de vezes que cada byte aparece
            List<byte[]> blocos = new ArrayList<>(); //Divide o conteudo em 256 bytes por bloco
            int tamanho = 0; //Variavel que fará o controle do tamanho do array
            
                if(conteudo.length < 256) {
                blocos.add(conteudo); //Se o conteúdo for menor que o minimo de blocos, já adiciona na lista
               } else {
            
            for(int i = 0; i < conteudo.length; i++) {
            
                
               b = conteudo[i]; //b recebe o byte na posicao i
               
                bytesPorBloco[tamanho] = b; //Bloco na posição de tamanho recebe B
                tamanho += 1;
                   
                if(blocos.isEmpty() && tamanho == 64) { 
                   blocos.add(bytesPorBloco); //Adiciona os 64 primeiro bytes (headers) na posição 0
                   bytesPorBloco = new byte[bytesPorBloco.length]; //Reseta o array
                }
                
               if(conteudo.length - i < 256 && i == conteudo.length - 1) {
                 blocos.add(bytesPorBloco); //Caso chegue ao final do arquivo sem completar o tamanho de 256, adicione o resto ao
                                            //bloco
               }
                
               if(tamanho == 256) {
               blocos.add(bytesPorBloco); //Se a variavel atingir o tamanho maximo do array adicione ele no bloco
               bytesPorBloco = new byte[bytesPorBloco.length]; //Reseta o array
               tamanho = 0; //Reseta a variavel de controle
               
               }   
              }  
             }
            
            
            int totalBytes = 0; //Variavel para receber o total de bytes assim calculando a probabilidade para a entropia de shannon
            
              
           
              b = 0;
              float probabilidade;         
              final float log2 = (float) log(2);
              float entropia = 0; //Variavel que será usada para somar todas as medias de incerteza do bytes
              List<Float> listaEntropia = new ArrayList<>();
         
              for(int i = 0; i < blocos.size(); i++) {
                  
                  bytesPorBloco = blocos.get(i);
                  
                     
              for(int j = 0; j < bytesPorBloco.length; j++) {
                  
                b = bytesPorBloco[j]; //Recebe o byte do bloco i na posição j
                contador[b & 0xFF] += 1;
                totalBytes += 1; //Recebe o total de bytes do bloco
              }
             
               
                      for(int j = 0; j < contador.length; j++ ) {
                      
                      if(contador[j] > 0) {
                      //Probabilidade = quantidade de aparições de um byte no bloco dividido pelo totalBytes de todo o bloco
                      probabilidade = (float) contador[j]/totalBytes; //probabilidade = frequencia/quantidade
                       
                     float logP = (float) log(probabilidade); //Guarda o log de p
                float log2p = -(logP/log2); //Log de Pna base 2, serve para calcular (Entropia = probabilidade * -log2(probabilidade)) o menos garante que o resultado seja positivo
                         
                entropia += probabilidade*log2p;
                
                    }           
                  }
                 listaEntropia.add(entropia); //Guarda todas as entropias em uma lista
                 entropia = 0; //Reseta a entropia para o proximo bloco
                totalBytes = 0; //reseta o totalBytes
                contador = new int[contador.length]; //Reseta o contador
              }
              
              
               //Medias de entropia esperados para cada tipo de arquivo
               Map<String, Float> mediaEsperada = new HashMap<>();
             mediaEsperada.put("zip", 7.2f);
             mediaEsperada.put("exe", 5.8f);
             mediaEsperada.put("elf", 5.8f);
             mediaEsperada.put("pdf", 7.0f);        //A maior media esperada, passou disso é suspeito
             mediaEsperada.put("png", 7.0f);
             mediaEsperada.put("jpg", 7.0f);
             mediaEsperada.put("docx", 7.2f);
             mediaEsperada.put("txt", 5.0f);
             final float tolerancia = 0.2f;
            
              
              
           int sequenciaAlta = 0; //Variavel para guardar a sequencia de entropia alta atual
           int maiorSequencia = 0; //Variavel que guarda a maior sequencia encontrada no arquivo
           int pontuacao = 0;   
           
           for (int i = 1; i < listaEntropia.size(); i++) {
               float entropiaAtual = listaEntropia.get(i);
               float entropiaAnterior = listaEntropia.get(i - 1);
              if(entropiaAtual > mediaEsperada.get(tipoArquivo) && entropiaAnterior < 4.5) { //Detecta saltos abruptos demais
               pontuacao++;         
               }
               
               if(listaEntropia.get(i) >= 7.0) {
                   sequenciaAlta++;
                   maiorSequencia = Math.max(maiorSequencia, sequenciaAlta);  
               } else {
                   sequenciaAlta = 0;
               }
               
           }
           
            float entropiaMedia = 0;
            for (float e : listaEntropia) entropiaMedia += e; //Pega a media de entropia do arquivo
            entropiaMedia /= listaEntropia.size();
            
         
                   //Medias de entropia esperados para cada tipo de arquivo
               Map<String, Float> tamanhoHeader = new HashMap<>();
             tamanhoHeader.put("zip", 1.7f);
             tamanhoHeader.put("exe", 1f);
             tamanhoHeader.put("elf", 1f);
             tamanhoHeader.put("pdf", 2.3f); //A maior media esperada, passou disso é suspeito
             tamanhoHeader.put("png", 1.7f);
             tamanhoHeader.put("jpg", 1.7f);
             tamanhoHeader.put("docx", 1.7f);
             tamanhoHeader.put("txt", 6f);
             
             
            if(listaEntropia.get(0) > tamanhoHeader.get(tipoArquivo)) pontuacao++;
          
            if(mediaEsperada.get(tipoArquivo) + tolerancia > entropiaMedia) pontuacao++;
            
            if(tipoArquivo.equals("exe") && maiorSequencia >= 5) pontuacao++;
            
            else if(tipoArquivo.equals("elf") && maiorSequencia >= 5) pontuacao++;
            
            if(tipoArquivo.equals("exe") && maiorSequencia >= 10) pontuacao = pontuacao + 2;
            else if(tipoArquivo.equals("elf") && maiorSequencia >= 10) pontuacao++;
            
            if (tipoArquivo.equals("exe") && maiorSequencia >= 20) pontuacao = pontuacao + 3;
            else if(tipoArquivo.equals("elf") && maiorSequencia >= 20) pontuacao++;
            
            if(pontuacao <= 2) {
             
            }
             if(pontuacao >= 3) {
               nivelRisco += 15;
             }
             else if(pontuacao >= 5) {
                 nivelRisco += 20;
             }

        } catch (IOException e) {
          
        }
     return nivelRisco;
    }
    
    public int analisarArquivo(String extensao, File arq) {
        compararCabecalhos(arq, extensao);
       
        return nivelRisco;
    }
    
    
    
}

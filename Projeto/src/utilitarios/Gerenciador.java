/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilitarios;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import usuario.usuarioAtual;


/**
 *
 * Fluxo da classe:
 * 
 * Usuario faz login bem sucedido com a senha mestra -> Sistema pega a senha mestra limpa e cria uma chave AES para criptografar 
 * ou descriptografar --> Caso criptografar: Sistema criptografa a senha informada usando a chave e um iv aleatorio para garantir
 * unicidade de chaves, depois encoda a senha em base64 para evitar corruptção(Boas praticas antes do envio para o BD)
 * Tambem faz o mesmo com o IV, pois o iv deve ser armazenado no BD para o sistema fazer o decript
 * 
 * Em caso de Descriptografar: O sistema pega a senha a ser descriptografado e usa o mesmo IV e mesma chave para fazer o decript
 * se iv ou a chave estiverem incorretos, não será possivel o decript, por isso o iv deve ser armazenado com segurança no BD.
 * Porém a chave não é necessario manter no BD, pois o processo de gerar a chave é realizado durante o login do usuario
e apenas se o usuario tiver feito login bem sucedido

*/
public class Gerenciador {
           

        
    public String gerarChave(String senhaMestra, byte[] salt)  {
        
          String chaveCriptografada;
        
        try {
        
          
         
        SecretKeyFactory algoritmo = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"); //Define o algoritmo que será usado
        PBEKeySpec init = new PBEKeySpec(senhaMestra.toCharArray(), salt, 600000, 256);
        
            SecretKey key = algoritmo.generateSecret(init); //Cria um molde de chave para a classe SecretKeySpec
            SecretKeySpec senhaMestraCifrada = new SecretKeySpec(key.getEncoded(), "AES"); //Cria uma chave secreta generica
          
         chaveCriptografada = Base64.getEncoder().encodeToString(senhaMestraCifrada.getEncoded()); //Cifra a chave em base64
         
     
            
        } catch(Exception e) {
            return "";
        }
        return chaveCriptografada;
    }
    
    
    public String criptografar(String senha, String chaveAES, byte[] iv) {
        
        String senhaCriptografada = "";
        
        try {
        SecretKeySpec chave = new SecretKeySpec(Base64.getDecoder().decode(chaveAES), "AES"); //Recebe a chave em base64 e decodifica
        
           
            GCMParameterSpec parametros = new GCMParameterSpec(128, iv); //Parametros do algoritmo GCM para criptografar a senha
            
            
            Cipher cripto = Cipher.getInstance("AES/GCM/NoPadding"); //Define o algoritmo AES usado para a chave
            cripto.init(Cipher.ENCRYPT_MODE, chave,parametros); //Configura o Cipher com as informações de modo (encriptar ou decriptar) a chave usada para criar uma chave AES e os parametros do algoritmo
            byte[] bytes = cripto.doFinal(senha.getBytes()); //Criptografa a senha usando a chave AES
            
            senhaCriptografada = Base64.getEncoder().encodeToString(bytes); //Criptografa a senha em base64   
        
            
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Erro desconhecido");
          return "";
        }
        
                    return senhaCriptografada;

        
    }
    
    
    
    public String descriptografar(String senhaCriptografada, String iv) {
            
          try {
             
           byte[] chaveDecod = Base64.getDecoder().decode(usuarioAtual.getChaveAES()); //Decodifica a chave que será usada para o decode
           SecretKeySpec chaveDecode = new SecretKeySpec(chaveDecod, "AES"); //Converte os bytes da chave em secret key
           SecretKeySpec senhaDecode = new SecretKeySpec(Base64.getDecoder().decode(senhaCriptografada), "AES"); //Decodifica a senha da base64
            byte[] ivDecode = Base64.getDecoder().decode(iv); //Decodifica o iv
            
            GCMParameterSpec parametros = new GCMParameterSpec(128, ivDecode); //Parametros usados pelo cipher

           
            Cipher cripto = Cipher.getInstance("AES/GCM/NoPadding"); //Define o algoritmo AES usado para a chave
            cripto.init(Cipher.DECRYPT_MODE, chaveDecode, parametros); //Configura o cipher com o modo, a chave e os parametros
            byte[] bytes = cripto.doFinal(senhaDecode.getEncoded()); //Recebe os bytes sem a criptografia
            
            String senhaDescriptografada = new String(bytes);
            

            return senhaDescriptografada; //Retorna a senha original
           
             
            
            
            
            
            
          } catch(Exception e) {
              return "";
          }
        
    }
    
    public String gerarSenha(int tamanhoSenha) {
          
       //Declaração da variavel a qual a senha será armazenada, recebe o valor vazio para concatenar com os caracteres aleatorios
       String senha = "", secureSenha = "";
              
        SecureRandom gerador = new SecureRandom();
        
       //Declaração de array contendo letras minusculas
       char[] letrasMin = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};       
       
       //Declaração do array contendo letras maiusculas
       char[] letrasMaiusc = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
       
       //Declaração dos array contendo simbolos e n
       char[] simbolos = {'@','#','$','%','!','?','&','*','^'};
       int[] numeros = {0,1,2,3,4,5,6,7,8,9};
 
          //Inserindo pelo menos um valor de cada array para garantir uma senha que contenha uma letra minuscula, uma maiuscula,
          //um simbolo e um número
             int valor = gerador.nextInt(letrasMin.length);
             char caractereMin = letrasMin[valor];
             
             senha += caractereMin;
             
               int valor2 = gerador.nextInt(letrasMaiusc.length);
             char caractereMaiusc = letrasMaiusc[valor2];
                
             senha += caractereMaiusc;
             
               int valor3 = gerador.nextInt(simbolos.length);
             char caractereSimbolo = simbolos[valor3];
             
                
             senha += caractereSimbolo;
             
               int valor4 = gerador.nextInt(numeros.length);
             int numero = numeros[valor4];
             
                
             senha += numero;
             
             
      //Laço de repetição que será execultado enquanto a variavel i for menor que o tamanho da senha inserida pelo usuario
      for (int i = 0; i < tamanhoSenha - 4; i++) {
        
          //Declara uma variavel escolherArray que recebe um valor aleatorio de 0 a 4
          int escolherArray = gerador.nextInt(4);                     
                         
          //Condicionais para cada valor da variavel escolherArray
           if(escolherArray == 0) {

               //Declara uma variavel que recebe um valor aleatorio conforme a quantidade de posições do array letrasMin
               int indice = gerador.nextInt(letrasMin.length); 
              
               
            //Declara uma variavel para receber o valor da posição que indice indica
            //Ex: se indice equivale a 10, a variavel caractere1 receberá o valor que está na posição 10 do array letrasMin
         
           char caractere1 = letrasMin[indice]; 
         
           //Concatena esse valor na variavel senha
           senha += caractere1;
           
         
                                                    
           }  
           
           //A mesma coisa ocorre, porém em arrays diferentes
            if(escolherArray == 1) {
                
                int indice = gerador.nextInt(letrasMaiusc.length);
                
                char caractere2 = letrasMaiusc[indice];
                
                           senha += caractere2;
                
            }
              if(escolherArray == 2) {
                  
                int indice = gerador.nextInt(simbolos.length);
                char caractere3 = simbolos[indice];
                  senha += caractere3;
                        
            }
                if(escolherArray == 3) {
                    
                    int indice = gerador.nextInt(numeros.length);
                int caractere4 = numeros[indice];
                  senha += caractere4;
                    
                }
                
                
      }
      
        //Armazenando cada caractere da variavel senha em um array
        char[] caracteresSenha = new char[senha.length()];
        for (int i = 0; i < senha.length(); i++) {
            caracteresSenha[i] = senha.charAt(i);
        }      
      
             //Percorre todo o array trocando indices de posição aleatoriamente
            for (int i = 0; i < caracteresSenha.length; i++) {
                int j = gerador.nextInt(i + 1);
               
                char temp = caracteresSenha[i];
                caracteresSenha[i] = caracteresSenha[j];
                caracteresSenha[j] = temp;
                               
                
            }
            
            //Armazena cada caractere na variavel
             for(int i = 0; i < tamanhoSenha; i++) {
                 
           secureSenha += caracteresSenha[i];
                 
             }
        
               //retorna a senha gerada
               return secureSenha;
    }
    
    
    
    
    
    
    
    
    
}

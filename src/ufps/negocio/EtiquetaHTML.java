/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ufps.negocio;

import java.util.StringTokenizer;

/**
 *
 * @author hogar
 */
public class EtiquetaHTML {
   private String etiqueta;
   private String descripcion;
 
   /**
    * construtor vacio.
    */
    public EtiquetaHTML() {
    }
  /**
   * Construtor con parametros de los datos.
   * @param etiqueta(String)etiqueta
   * @param descripcion (String)descpcion de la etiqueta.
   */
    public EtiquetaHTML(String etiqueta, String descripcion) {
        this.etiqueta = etiqueta;
        this.descripcion = descripcion;
    }

  
    /**
     * Metodo que saca contenido  o solo el nombre de la etiqueta.
     * @param etq(String)etiqueta
     * @return (String) solo el nombre de la etiqueta.
     */
   private String sacarContenidoEtiqueta(String etq) {
        StringTokenizer st = new StringTokenizer(etq, "<>");
        return st.nextElement().toString();
    }
   
    
    /**
     * Metodo que permite reconocer las etiquetas en estadar de html  y nalaiza su semantica.
     * @param etiq(String)etiqueta
     * @return (boolean) retorna si la etiqueta esta correctamente escrita.
     */
   protected boolean reconocerEtiqueta(String etiq){
       char []parEtique=etiq.toCharArray();
       char []etiqInici="<!DOCTYPE".toCharArray();
        if(this.compararDosVectores(etiqInici, parEtique)){
            return this.EncontrarParteDoctypeHtml(9,parEtique);
        }
          if(this.verificarEtiquetaSiHayComentario(parEtique)){
            return this.etiquetaFinalComentario(parEtique);
               }
              if(parEtique[1]==' '){
                return false;
            }
              if(this.etiquetaUnariaConEspacio(parEtique)){
              return false;
              }
       if(this.ComprobartiquetaCierre(parEtique)){
                  return false;
              }
     
   return this.compararDosVectores(this.sacarEtiquetaSinAtributos(parEtique).toCharArray(),this.etiqueta.toCharArray());
                              
    } 
   
   /**
    * Metodo que analiza la etiqueta unaria con espacio.
    * @param etq(String)etiqueta
    * @return (boolean)retorna true si esta correctamente.
    */
  private boolean etiquetaUnariaConEspacio(char [] etq){
return (String.valueOf(etq[etq.length-2]).equals("\t")&&etq[etq.length-3]=='/');
  } 

  /**
   * Metodo que permite determinar si la etiqueta de comentario fian lesta bien.
   * @param etiq(char [] )vector de la etiqueta.
   * @return (boolena) retorna true si esta bien escrita.
   */
   private boolean etiquetaFinalComentario(char [] etiq){
       char []vec="-->".toCharArray();
       int j=2;
       int i=etiq.length-1;        
         for(int x=3;0<x;i--,x--){
           if(etiq[i]!=vec[j]){
               return false;
               }
                     j--;
         }
         return true;
   }
   
   /**
    * Metodo que permite determinar si la etiqueta de comentario esta bien escrita
    * @param etiq(char [])etiqueta
    * @return (boolean) si esta bien escrita envia true.
    */
  private  boolean verificarEtiquetaSiHayComentario(char [] etiq){
      char []vec="<!--".toCharArray();
            for(int i=0;i<vec.length;i++){
            if(vec[i]!=etiq[i]){
                 return false;  
            }       
            }
            return true;   
  }
   
  /**metodo amaliza que la etiqueta <!DOCTYPE HTML> este bien escrita. 
   * 
   * @param i(int) indice.
   * @param etiq(char [])etiqueta
   * @return (boolean ) si esta bien lanza un true.
   */
   private boolean EncontrarParteDoctypeHtml(int i,char []etiq){
       char [] vec="html".toCharArray();
         String cad="\t";
        for(int j=0;j<vec.length&&i<etiq.length;i++){
            if(!String.valueOf(etiq[i]).equals(cad)){
    if(!String.valueOf(etiq[i]).equalsIgnoreCase(String.valueOf(vec[j]))){
                 return false;   
                }
         j++;
            }
        }
        return true;
   }
    
   /**
    * Metodo que examina que las etiquetas de cierre no tenga espacios
    * @param parEtique(char[] )parEtique
    * @return (boolean)retorna true si la etiqueta de cierre esta bien .
    */
   private  boolean ComprobartiquetaCierre(char[] parEtique ){
           boolean entrar=false;
           String cad="\t";
           entrar=(parEtique[1]=='/')?true:false; 
              
                if(entrar){
            for(int i=1;i<parEtique.length;i++){
                if(String.valueOf( parEtique[i]).equals(cad)){
                        return true;
                    }
                      }
               }
                  return false;          
   }
   
   
   /**
    * Metodo que comprueba si dos vectores de char son iguales.
    * @param cade(char []) vectro 1
    * @param cedr(char [])vector 2
    * @return (boolean) retorna verdadero si son iguales.
    */
   private boolean compararDosVectores(char []cade,char []cedr){
             for(int i=0;i<cedr.length&&i<cade.length;i++){
         if(!String.valueOf(cedr[i]).equalsIgnoreCase(String.valueOf(cade[i])) ){
                       return false ; 
                    }    
                }
             return true;
       
   }
  /**
   * Metodo que permite sacar las etiquetas sinb atributos.
   * @param vect((char []) vector de char.
   * @return (String)saca etiquetas sin atributos.
   */
   private String sacarEtiquetaSinAtributos(char [] vect){
       String cad="";
         for(int i=1;i<vect.length-1;i++){
             String cade="\t";
          if(String.valueOf(vect[i]).equals(cade)){
                   break;
               } 
               if(vect[i]!='/'){
               cad+=vect[i];
                   }
               }
         return "<"+cad+">";
         
   }
   
   /**
    * Metodo para saber si dos etiquetas son iguales.
    * @param etique(String) etiqueta.
    * @return (boolean) si son iguales envia true.
    */
   protected boolean etiquetaIgual(String etique){
  return this.etiqueta.equalsIgnoreCase(etique);  
   }
   
   /**
    * Metodo que analiza sin dos etiquetas son iguales pero solo en su nombre.
    * @param etq(String)etiqueta
    * @return (boolean) envia true si son iguales.
    */
  protected boolean etiquetaAyudaIgual(String etq){
  return this.sacarContenidoEtiqueta(this.etiqueta).equalsIgnoreCase(etq);
  }
   

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
   
}

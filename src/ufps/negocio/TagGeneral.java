/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ufps.negocio;

import ufps.util.ArchivoLeerURL;
import ufps.util.Cola;

/**
 *
 * @author hogar
 */
public class TagGeneral {
    private String tipo;
    private Cola<EtiquetaHTML> etiquetas=new Cola<>();
    
/**
 * Construtor vacio.
 */
    public TagGeneral() {  
        
    }
/**
 * Construtor con parametros que inmediatamente llamado llama al metodo encontrarEtiquetas que leer el archivo en sanbox.
 * @param tipo (String) tipo de tag.
 */
    public TagGeneral(String tipo) {
        this.tipo = tipo;
        this.encontrarEtiquetas();
    }
    /**
     * Metodo que permite leer el archivo de sanbox y cargar las etiquetas  de ese tipo de tag.
     */
   private void encontrarEtiquetas(){
       ArchivoLeerURL file=new ArchivoLeerURL("http://sandbox1.ufps.edu.co/~madarme/estructuras/html_w3c.txt");
       Object v[]=file.leerArchivo();
        for(Object dato:v)
      {
          String []linea=dato.toString().split(";");
               if(tipo.equals(linea[0])){
               if("<h1> to <h6>".equals(linea[1])){
                this.crearEtiquetaH(linea[2]);
              }
              else{
              EtiquetaHTML etiqueta=new EtiquetaHTML();
              etiqueta.setEtiqueta(linea[1]);
              etiqueta.setDescripcion(linea[2]);
             this.etiquetas.enColar(etiqueta);
              }
            }
         
      }
   }
   
   
/**
 * Metodo que sirve para crear las 6 etiquetas de h.
 * @param descrip (String) etiqueta leida de sanbox.
 */
   private void crearEtiquetaH(String descrip){
           for(int i=1;i<=6;i++){
     EtiquetaHTML etiqueta=new EtiquetaHTML();
      etiqueta.setEtiqueta("<h"+i+">");
      etiqueta.setDescripcion(descrip);
      this.etiquetas.enColar(etiqueta);
     }     
   }
   
   /**
    * Metodo  llamado por el sistemaHTML para hallar una etiqueta
    * @param etique(String) etiqueta a encontrar
    * @return (EtiquetaHTML)devuelve un objeto .
    */
   protected EtiquetaHTML hallarEtiqueta(String etique){
       EtiquetaHTML e=null;
       Cola<EtiquetaHTML> aux=new Cola<>();
          while(!this.etiquetas.esVacio()){
              e=this.etiquetas.deColar();
           if(e.etiquetaIgual(etique)){
                aux.enColar(e);
                break;
              } 
              aux.enColar(e); 
              e=null;
          }
          this.llenarColaNuevamente(aux);
          return e;
   }
   
   /**
    * Metodo que permite buscar una etiqueta.
    * @param etique(String) etiqueta
    * @return (String) devuelve algo si encuentra uanetiqueta.
    */
   protected String buscarEtiqueta(String etique){
  String cad="";
  EtiquetaHTML e=null;
       Cola<EtiquetaHTML> aux=new Cola<>();
          while(!this.etiquetas.esVacio()){
              e=this.etiquetas.deColar();
           if(e.etiquetaAyudaIgual(etique)){
               cad=e.getEtiqueta()+"-"+e.getDescripcion();
                aux.enColar(e);
                break;
              } 
              aux.enColar(e); 
              e=null;
          }
          this.llenarColaNuevamente(aux);
          return cad;
   
   }   
   
   /**
    * Metodo que devulve verdadero si encuentar una etiqueta.
    * @param nomEtiqueta(String)nombre de etiqueta
    * @return (boolean) devuelve true si la encontro.
    */
   protected boolean encontrarEtiqueta(String nomEtiqueta){
       boolean res=false;
         Cola<EtiquetaHTML> aux=new Cola<>();
         EtiquetaHTML e=null;
         while(!this.etiquetas.esVacio()){
              e=this.etiquetas.deColar();
              if(e.reconocerEtiqueta(nomEtiqueta)){
               aux.enColar(e);
                  res=true;
                  break;
              }  
             aux.enColar(e);
         }
         this.llenarColaNuevamente(aux);
         return res;     
   }
   /**
    * Metodo toString  que sacalas etiquetas de cierto tipo.
    * @return 
    */
    @Override
    public String toString() {
        String cad="";
        Cola<EtiquetaHTML> aux=new Cola<>();
       while(!this.etiquetas.esVacio()){
           EtiquetaHTML etiqueta=this.etiquetas.deColar();
           if("<html>".equals(etiqueta.getEtiqueta())){
               cad+="< html >"+";";
           }
           else{
           cad+=etiqueta.getEtiqueta()+";";
           }
           aux.enColar(etiqueta);
       }
      this.llenarColaNuevamente(aux);
      return cad;
    }
    
    
    /**
     * Metodo que genera las etiqueta de h
     * @return (String)devuelve las 6 etiquetas.
     */
    private String generarEtiquetasH(){
        String cad="";
              for(int i=0;i<6;i++){
              cad+="<h"+(i+1)+">"+";";    
              }
              return cad;
    }
    
    /**
     * Metodo  que permite llenar la cola etiquetas.
     * @param aux (Cola<EtiquetaHTML>) cola auxiliar.
     */
    private void llenarColaNuevamente(Cola<EtiquetaHTML> aux){
        while(!aux.esVacio()){
            this.etiquetas.enColar(aux.deColar());
        }
    }
   
   
 

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
        this.encontrarEtiquetas();
    }

    public Cola<EtiquetaHTML> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(Cola<EtiquetaHTML> etiquetas) {
        this.etiquetas = etiquetas;
    }
    
    
    
}

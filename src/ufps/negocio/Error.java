/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ufps.negocio;

/**
 *
 * @author hogar
 */
public class Error {
    private String descripcion;
/**
 * Construtor vacio
 */
    public Error() {
    }

    
   /**
    * Construtor con parametro
    * @param descripcion 
    */
    public Error(String descripcion) {
        this.descripcion = descripcion;
    }

    
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ufps.negocio;

/**
 *
 * @author hogar
 */
public class ErrorHTML {
 private Error myError;
 private EtiquetaHTML myEtiqueta;

 /**
  * Construtor vacio de la calse HTML
  */
    public ErrorHTML() {
    }
    
/**
 * Construtor con parametros.
 * @param myError(Error)error asociado
 * @param myEtiqueta (EtiquetaHTML)etiqueta.
 */
    public ErrorHTML(Error myError, EtiquetaHTML myEtiqueta) {
        this.myError = myError;
        this.myEtiqueta = myEtiqueta;
    }

    public Error getMyError() {
        return myError;
    }

    public void setMyError(Error myError) {
        this.myError = myError;
    }

    public EtiquetaHTML getMyEtiqueta() {
        return myEtiqueta;
    }

    public void setMyEtiqueta(EtiquetaHTML myEtiqueta) {
        this.myEtiqueta = myEtiqueta;
    }
 
 
 
}

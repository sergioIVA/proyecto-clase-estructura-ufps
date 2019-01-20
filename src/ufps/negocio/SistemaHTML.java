/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ufps.negocio;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import ufps.util.ArchivoLeerURL;
import ufps.util.Cola;
import ufps.util.ListaCD;
import ufps.util.PdfTabla;
import ufps.util.Pila;
import ufps.util.Secuencia;

/**
 *
 * @author hogar
 */
public class SistemaHTML {

    private ListaCD<String> filasDelArchivo;
    private Error error1;
    private Error error2;
    private Error error3;
    private Error error4;
    private Pila<ErrorHTML> errores = new Pila();
    private Secuencia<TagGeneral> tags;
   
 /**
  * construtor de la clase sistema html que crea la secuencia de tags y inicializa la lista y crea los posibles errores
  */
    public SistemaHTML() {
        this.tags = new Secuencia<>(12);
        filasDelArchivo = new ListaCD<>();
        this.crearSecuenciaTags();
        this.CrearErrores();

    }
/**
 * Metodo que informa si la pila de errores esta llena para saber si existen errores en el codigo html analizado.
 * @return (boolean) devuelve verdadero si es cierto o false si lo contratio.
 */
    public boolean hayErrrores() {
        return (!this.errores.esVacio());
    }

    /**
     * Metodo que inicializa los errores que pueden genererse durante el analisis de codigo html.
     */
    private void CrearErrores() {
        error1 = new Error("NO TIENE FIN DE ETIQUETA");
        error2 = new Error("NO TIENE ETIQUETA DE INICIO");
        error3 = new Error("ETIQUETA NO RECONOCIDA");
        error4 = new Error("NO TIENE ETIQUETA DE ESTRUCTURA");
    }
    
    
/**
 * Metodo que toma una linea  de codigo de html y la pone en la lista circular doble.
 * @param linea(String) parametro de la linea agregar en la lista. 
 */
    public void tomarCodigoHtml(String linea) {
        this.filasDelArchivo.addFin(linea);
    }
    
    /**
     * Metodo que cumple la funcion de leer el archivo txt sanbox con todas las etiquetas validas para el sistema.
     */
     private void crearSecuenciaTags() {
        ArchivoLeerURL file = new ArchivoLeerURL("http://sandbox1.ufps.edu.co/~madarme/estructuras/html_w3c.txt");
        Object v[] = file.leerArchivo();
        String cad = "";
        int i = 0;
        for (Object dato : v) {
            String[] linea = dato.toString().split(";");
            if (!cad.equals(linea[0])) {
                TagGeneral tags = new TagGeneral(linea[0]);
                this.tags.set(i, tags);
                i++;
            }
            cad = linea[0];
        }
    }

    /**
     * Metodo de gran relevancia por que tiene la funcion de detectar cualquier error que ocurra en el codigo 
     * html analizado. .
     */
    public void buscarErrores() {
        Cola<String> c = new Cola<>();
        Pila<ErrorHTML> err = new Pila<>();
        String cad = "";
        String[] etiquetas = null;
        for (String li : this.filasDelArchivo) {
            cad += li + "\t";
        }
        this.llenarCola(this.token2(cad).split(";"), c);
        this.etiquetaNoreconocida(c, err);
        this.eliminarAtributosEtiquetas(c);
        this.verificarEtiquetaEstrutura(err, c);
        this.sacarEtiquetaUnarias(c);
        this.verificarEtiquetaInicioFinal(c, err);
        this.pasarPilaAuxPilaErrores(err);
        this.filasDelArchivo.removeAll();

    }

    
    /**
     * Metodo que permite pasar de  una pila auxiliar los errores HTML a la pila principal de errores HTML
     * para poder dejar en orden la pila .
     * @param aux(Pila<ErrorHTML>)pila auxiliar
     */  
    private void pasarPilaAuxPilaErrores(Pila<ErrorHTML> aux) {
        while (!aux.esVacio()) {
            this.errores.push(aux.pop());
        }
    }

    /**
     * Metodo que permite sacar las etiquetas unarias de una cola auxiliar.
     * @param etq(Cola<String> etq))cola auxiliar. 
     */
    private void sacarEtiquetaUnarias(Cola<String> etq) {
        Cola<String> aux = new Cola<>();
        String etiq = "";
        while (!etq.esVacio()) {
            etiq = etq.deColar();
            if (etiq.charAt(etiq.length() - 2) != '/' && etiq.charAt(1) != '!') {
                aux.enColar(etiq);
            }
        }
        this.vaciarColaEnOtra(aux, etq);
    }

    /**
     * Metodo que permite hacer la operacion  de verificar si falta una etiqueta de inicio o de  fin
     * para poder generar error.
     * @param etiq(Cola<String>)cola etiqueta a analizar.
     * @param p(Pila<ErrorHTML>)pila donde se van almacenar los errores. 
     */
    private void verificarEtiquetaInicioFinal(Cola<String> etiq, Pila<ErrorHTML> p) {
        Pila<String> etqInicio = new Pila<>();
        String etq = "";
        while (!etiq.esVacio()) {
            etq = etiq.deColar();
            if (etq.charAt(1) != '/') {
                etqInicio.push(etq);
            } else {
                etq = this.sacarContenidoEtiqueta(etq);
                if (!this.detectarErroresEtiqInic(etqInicio, etq)) {
                    p.push(new ErrorHTML(this.error2, new EtiquetaHTML("</" + etq + ">", "xxxx")));
                }
            }
        }
        this.detectarErroresEtqFinal(etqInicio, p);
    }
/**
 * Metodo individual que se encarga de detectar si hace falta cerrar una etiqueta o falta una etiqueta de cierre.
 * @param etqInicio(Pila<String>)pila donde estan solo las etiquetas de inicio.
 * @param p(Pila<ErrorHTML>)pila donde se van almacenar los errores.
 */
    private void detectarErroresEtqFinal(Pila<String> etqInicio, Pila<ErrorHTML> p) {
        while (!etqInicio.esVacio()) {
            p.push(new ErrorHTML(this.error1, this.encontrarEtiqueta("<" + this.sacarContenidoEtiqueta(etqInicio.pop()) + ">")));
        }
    }

    /**
     * Metodo individual que permite saber  si falta etiqueta de inicio.
     * @param etqInicio(Pila<String) pila de puras etiquetas de inicio.
     * @param etq(String) etiqueta  de cierre .
     * @return (boolean) retorna verdadero si se encuentra la etiqueta a buscar de inicio.
     */
    private boolean detectarErroresEtiqInic(Pila<String> etqInicio, String etq) {
        Cola<String> aux = new Cola<>();
        String etiqInicio = "";
        boolean seEnc = false;
        while (!etqInicio.esVacio()) {
            etiqInicio = etqInicio.pop();
            if (etq.equalsIgnoreCase(this.sacarContenidoEtiqueta(etiqInicio))) {
                seEnc = true;
                break;
            } else {
                aux.enColar(etiqInicio);
            }
        }
        this.vaciarColaEnPila(aux, etqInicio);
        return seEnc;

    }
/**
 * Metodo que permite de  una cola sacar sus elemnetos y pasarla a otra .
 * @param c(Cola<String> ) cola a sacar elementos.
 * @param p (Pila<String>) pila donde se van a pasar estos elementos.
 */
    private void vaciarColaEnPila(Cola<String> c, Pila<String> p) {
        while (!c.esVacio()) {
            p.push(c.deColar());
        }
    }
/**
 * metodo que permite sacar el contenido de una etiqueta de inicio y de cierre ,es decir  solo el nombre de la etiqueta
 * sin sus angulares y "/" .
 * @param etq(String) etiqueta.
 * @return (String) retorna un string con el solo nombre de la etiqueta. 
 */
    private String sacarContenidoEtiqueta(String etq) {
        StringTokenizer st = new StringTokenizer(etq, "</>");
        return st.nextElement().toString();
    }
    
/**
 * Metodo que permite quitar o leminar los atributos de las etiquetas escritas por el usuario.
 * @param c (Cola<String>) cola de etiquetas.
 */
    private void eliminarAtributosEtiquetas(Cola<String> c) {
        Cola<String> aux = new Cola<>();
        String etiqueta = "";
        String etiqC = "";
        while (!c.esVacio()) {
            etiqueta = c.deColar();
            for (int i = 0; i < etiqueta.length(); i++) {
                if (String.valueOf(etiqueta.charAt(i)).equals("\t")) {
                    if (etiqueta.charAt(etiqueta.length() - 2) == '/') {
                        etiqC += "/";
                    }
                    etiqueta = etiqC + ">";
                    break;
                }
                etiqC += etiqueta.charAt(i);
            }
            aux.enColar(etiqueta);
            etiqC = "";
        }

        this.vaciarColaEnOtra(aux, c);
    }

    /**
     * Metodo que verifica si hace falta alguna etiqueta de estrutura de un html y si es asi genera errores que los
     * pasa a una pila de errores html.
     * @param p(Pila<ErrorHTML> )pila de errores HTML .
     * @param etique ( Cola<String>) cola de etiquetas.
     */
    private void verificarEtiquetaEstrutura(Pila<ErrorHTML> p, Cola<String> etique) {
        boolean primeraVez = true;
        Cola<String> auxEtique = new Cola<>();
        Cola<String> colaEtiqEstru = this.llenarColaEstrutura();
        Cola<String> colaEtique = new Cola<>();
        String etiq = "";
        while (!etique.esVacio()) {
            etiq = etique.deColar();
            if (primeraVez && !"<!DOCTYPE>".equalsIgnoreCase(etiq)) {
                p.push(new ErrorHTML(this.error4, this.encontrarEtiqueta("<!DOCTYPE> ")));
            }
            if (this.existenciaEtiquetaEstrutu(colaEtiqEstru, etiq, colaEtique)) {
                colaEtique.enColar(etiq);
            }
            primeraVez = false;
            auxEtique.enColar(etiq);
        }

        this.vaciarColaEnOtra(auxEtique, etique);
        this.ExaminarColaBuscaErroresEstrutu(p, colaEtique, colaEtiqEstru);

    }

  

   /**
    * Metodo  que toma dos colas una es la cola con etiquetas de tan solo estrtura que fueron sacadas de la cola donde 
    * estan todas las etiquetas la otra es una cola creada con el orden que debe llevar la estrtutura.
    * @param p(Pila<ErrorHTML>) pila de errores donde se almacenas los errores de estrutura si llegan ser encontrados.
    * @param colaEtiq(Cola<String> ) cola con las etiquetas filtradas de estruturas.
    * @param colaEtiqEstru ( Cola<String>) cola con las etiquetas del orden de la estrutura.
    */
    private void ExaminarColaBuscaErroresEstrutu(Pila<ErrorHTML> p, Cola<String> colaEtiq, Cola<String> colaEtiqEstru) {
        String etiqEstru = "";
        String etiq = "";
        boolean moverseEtq = true;

        while (!colaEtiqEstru.esVacio()) {
            if (moverseEtq && !colaEtiq.esVacio()) {
                etiq = colaEtiq.deColar();
            }
            etiqEstru = colaEtiqEstru.deColar();
            if (!etiqEstru.equalsIgnoreCase(etiq)) {
                this.crearErrorHtml(etiqEstru, p, this.error4);
                moverseEtq = false;
            } else {
                moverseEtq = true;
            }
        }

    }

   /**
    * Metodo que permite crear ErrorHTMl con la etiqueta pasada  la pila de errores para meterlo y 
    * el error al que hace refenrecia.
    * @param etq(String)etiqueta.
    * @param p(Pila<ErrorHTML> ) pila de errores.
    * @param err (Error)error al cual se hace referencia.
    */
    private void crearErrorHtml(String etq, Pila<ErrorHTML> p, Error err) {
        EtiquetaHTML e = null;
        e = this.encontrarEtiqueta(etq);
        if (e != null) {
            p.push(new ErrorHTML(err, e));
        } else {
            p.push(new ErrorHTML(err, new EtiquetaHTML(etq, "xxxxx")));
        }
    }
     
    /**
     * Metodo que se encarga de determinar pasada una etiqueta si existe esa etiqueta de estrutura.
     * @param estrutu(Cola<String>) cola con la estrutura
     * @param etiq(String)etiqueta
     * @param p(Cola<String>) cola etiquetas
     * @return (boolean) retorna un boolean.
     */
    private boolean existenciaEtiquetaEstrutu(Cola<String> estrutu, String etiq, Cola<String> p) {
        Cola<String> aux = new Cola<>();
        String etiqE = "";
        boolean res = false;
        int i = estrutu.getSize();
        while (!estrutu.esVacio()) {
            i--;
            etiqE = estrutu.deColar();
            if (etiqE.equalsIgnoreCase(etiq)) {
                aux.enColar(etiqE);
                res = true;
                break;
            } else {
                aux.enColar(etiqE);
            }
        }
        this.vaciarColaEnOtra(aux, estrutu);
        this.ordenarColaEstrutura(estrutu, i);
        return res;

    }
    
    /**
     * Metodo  que permite ordenar una cola es este caso la cola de estrutura vulva quedar en ese orden.
     * @param c(Cola<String>) cola de estrutura.
     * @param i (int) que tanto debe ordenarse.
     */
    private void ordenarColaEstrutura(Cola<String> c, int i) {
        while (i > 0) {
            i--;
            c.enColar(c.deColar());
        }
    }

    /**
     * Metodo que crea una cola cola la estrutura html.
     * @return (Cola<String>)retorna la cola con la estrutura html.
     */
    private Cola<String> llenarColaEstrutura() {

        Cola<String> c = new Cola();
        String[] es = "<html>,<head>,<title>,</title>,</head>,<body>,</body>,</html>".split(",");
        for (String x : es) {
            c.enColar(x);
        }

        return c;

    }

   /**
    * Metodo que se utiliza en el requerimiento de la ayuda este se encarga de buscar una etiqueta con el nombre
    * pasado por el usuario.
    * @param etq(String)etiqueta solicitada por el usuario.
    * @return (String) si retorna algo es por que encontro la etiqueta.
    */
    public String AyudaHTML(String etq) {
        String cad = "";
        for (int i = 0; i < this.tags.length(); i++) {
            cad = this.tags.get(i).buscarEtiqueta(etq);
            if (!cad.isEmpty()) {
                cad = this.tags.get(i).getTipo() + "-" + cad;
                break;
            }
        }
        return cad;

    }
/**
 * Permite buscar una etiqueta dentro del sistema y si lo encuentra devuelve un objeto EtiquetaHTML y si no lo encuentra
 * un null.
 * @param etiq(String)nombre de la etiqueta
 * @return (EtiquetaHTML )retorn objeto EtiquetaHTML
 */
    private EtiquetaHTML encontrarEtiqueta(String etiq) {
        EtiquetaHTML obj = null;
        for (int i = 0; i < this.tags.length(); i++) {
            obj = this.tags.get(i).hallarEtiqueta(etiq);
            if (obj != null) {
                return obj;
            }
        }
        return null;

    }

/**
 * Metodo que   analiza la cola etiquetas si estan semanticamente bien.
 * @param c(Cola<String>)cola etiquetas.
 * @param p (Pila<ErrorHTML>)pila donde se van almacenar los errores.
 */
   private void etiquetaNoreconocida(Cola<String> c, Pila<ErrorHTML> p) {
        Cola<String> aux = new Cola<>();
        String cad = "";
        while (!c.esVacio()) {
            cad = c.deColar();
            if (!this.compararEtiqueta(cad)) {
                p.push(new ErrorHTML(this.error3, new EtiquetaHTML(cad, "XXXXX")));
            } else {
                aux.enColar(cad);
            }
        }
        this.vaciarColaEnOtra(aux, c);

    }
    
/**
 * Metodo que  a base de una etiqueta examina si esta.
 * @param nomEtiqueta(String) nombreEtiqueta
 * @return (boolean)retorna verdadero si esta.
 **/
  private boolean compararEtiqueta(String nomEtiqueta) {
        for (int i = 0; i < this.tags.length(); i++) {
            if (this.tags.get(i).encontrarEtiqueta(nomEtiqueta)) {
                return true;
            }
        }
        return false;
    }

  /**
   * Metodo que a base de un vector de String ingresa esto a una cola.
   * @param cad(String[] )vector String.
   * @param c (Cola<String>) cola donde se ingresa.
   */
    private void llenarCola(String[] cad, Cola<String> c) {
        for (int i = 0; i < cad.length; i++) {
            if (!cad[i].isEmpty()) {
                c.enColar(cad[i]);
            }
        }
    }
   
/**
 * Metodo que permite sacar las etiquetas de las lineas que guarda la lista circular doble.
 * @param linea(String) linea como tal
 * @return (String)retorna etiquetas
 */
    private String token2(String linea) {
        String cad = "";
        String aux = "";
        for (int x = 0; x < linea.length(); x++) {
            char c = linea.charAt(x);
            if (c == '<') {
                for (; (x < linea.length() - 1) && !(linea.charAt(x) == '>' && (linea.charAt(x + 1) == '<' || linea.charAt(x + 1) != '>')); x++) {
                    aux += linea.charAt(x);

                }
                cad += aux + ">" + ";";
                aux = "";
            }
        }

        return cad;

    }
    
    /**
     * Metodo que toma una cola y la vaica en otra.
     * @param aux(Cola<String>)cola a vaciar.
     * @param c (Cola<String>)cola a llenar.
     */
    private void vaciarColaEnOtra(Cola<String> aux, Cola<String> c) {
        while (!aux.esVacio()) {
            c.enColar(aux.deColar());

        }
    }

   
 
/**
 * Metodo que le sirve al combo para cargar los tipos de tags
 * @return (String) devulve una cadena donde estan los tipos separados por una raya
 */
    public String cargarTags() {
        String cad = "";
        for (int x = 0; x < this.tags.length(); x++) {
            cad += this.tags.get(x).getTipo() + "-";
        }
        return cad;

    }
    

    
    /**
     * Metodo que le sirve al combo  de mostrar la s etiqueta de cierot tipo.
     * @return (String) retorna las etiquetas.
     */
    public String cargarEtiquetas() {
        String cad = "";
        for (int x = 0; x < this.tags.length(); x++) {
            cad += this.tags.get(x).toString() + ",";
        }

        return cad;
    }
    
    /**
     * Metodo que permite leer una url su contenido.
     * @param url(String)direccion.
     * @return (String)retorna contenido leido.
     */
    public String obtenerCodigoFuente(String url) {
        URL urlpagina = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        String linea;
        StringBuffer buffer = new StringBuffer();

        try {
            urlpagina = new URL(url);
            isr = new InputStreamReader(urlpagina.openStream());
            br = new BufferedReader(isr);
            while ((linea = br.readLine()) != null) {
                buffer.append(linea);
            }
            br.close();
            isr.close();
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, "Error en la url, ejemplo http://www.google.com.co");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo");
        }

        return buffer.toString();
    }
    
    /**
     * Metodo que crea el PDF a base de uan matriz de errores que se le envia.
     * @param m(String [][])matriz de errores.
     * @param dir(String)direccion del archivo.
     * @throws IOException
     * @throws DocumentException 
     */
    public void generarPDF(String [][]m, String dir) throws IOException, DocumentException{
    
        File f = new File (dir);
        PdfTabla pdf = new PdfTabla(m,f);
        pdf.crearTablaPDF();
    
    }

    public ListaCD<String> getFilasDelArchivo() {
        return filasDelArchivo;
    }

    public void setFilasDelArchivo(ListaCD<String> filasDelArchivo) {
        this.filasDelArchivo = filasDelArchivo;
    }

    public Error getError1() {
        return error1;
    }

    public void setError1(Error error1) {
        this.error1 = error1;
    }

    public Error getError2() {
        return error2;
    }

    public void setError2(Error error2) {
        this.error2 = error2;
    }

    public Error getError3() {
        return error3;
    }

    public void setError3(Error error3) {
        this.error3 = error3;
    }

    public Error getError4() {
        return error4;
    }

    public void setError4(Error error4) {
        this.error4 = error4;
    }

    public Pila<ErrorHTML> getErrores() {
        return errores;
    }

    public void setErrores(Pila<ErrorHTML> errores) {
        this.errores = errores;
    }

    public Secuencia<TagGeneral> getTags() {
        return tags;
    }

    public void setTags(Secuencia<TagGeneral> tags) {
        this.tags = tags;
    }

    
    
     
}

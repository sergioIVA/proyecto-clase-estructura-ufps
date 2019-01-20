/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ufps.util;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class PdfTabla {

    String m [][]; 
    File archivo;
    
    public PdfTabla(String m [][],File dir) {
    this.archivo = dir;
    this.m=m;
    
    }
 


        //Ruta del archivo, esto es dentro del proyecto Netbeans
    //public static String archivo = System.getProperty("user.dir") + "/PdfTabla.pdf";
 
 
     
public void createPdf() throws BadElementException, IOException, DocumentException {
        /*Declaramos documento como un objeto Document
         Asignamos el tamaño de hoja y los margenes */
        Document documento = new Document(PageSize.LETTER, 80, 80, 75, 75);

        //writer es declarado como el método utilizado para escribir en el archivo
        PdfWriter writer = null;

        try {
            //Obtenemos la instancia del archivo a utilizar
            writer = PdfWriter.getInstance(documento, new FileOutputStream(archivo));

        } catch (FileNotFoundException | DocumentException ex) {
            ex.getMessage();
        }

        //Agregamos un titulo al archivo
        documento.addTitle("Informe de Errores");

        //Agregamos el autor del archivo
        documento.addAuthor("Sergio Villamizar - Luis Sandoval");

        //Abrimos el documento para edición
        documento.open();

//imagen
        Image imagen = Image.getInstance("Membrete.jpg");

        imagen.scalePercent(25);
        documento.add(imagen);

//Declaramos un texto como Paragraph
        //Le podemos dar formado como alineación, tamaño y color a la fuente.
        Paragraph parrafo = new Paragraph();
        parrafo.setAlignment(Paragraph.ALIGN_CENTER);
        parrafo.setFont(FontFactory.getFont("Sans", 20, Font.BOLD, BaseColor.BLUE));
        parrafo.add("Informe de Errores HTLM");

        try {
            //Agregamos el texto al documento
            documento.add(parrafo);
      //documento.add(a);

            //Agregamos un salto de linea
            documento.add(new Paragraph(" "));

            //Agregamos la tabla al documento haciendo
            //la llamada al método tabla()
            documento.add(tabla());
        } catch (DocumentException ex) {
            ex.getMessage();
        }

        documento.close(); //Cerramos el documento
        // writer.close(); //Cerramos writer
    }

    //Método para crear la tabla
    public PdfPTable tabla() {
        //Instanciamos una tabla de 3 columnas
        //PdfPTable tabla = new PdfPTable(3);
        PdfPTable tabla = new PdfPTable(2);

        //Declaramos un objeto para manejar las celdas
        PdfPCell celda;
        
        tabla.addCell("Etiqueta");
        tabla.addCell("Descripcion del Error");

        for (int i = 0; i < this.m.length; i++) {
            for (int j = 0; j < this.m[i].length; j++) {
                tabla.addCell(m[i][j]);
            }
        }
        return tabla;
    }

    public void crearTablaPDF() throws IOException, DocumentException{
    this.createPdf();
    
    } 
}

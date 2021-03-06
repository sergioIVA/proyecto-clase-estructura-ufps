/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ufps.gui;

import com.itextpdf.text.DocumentException;
import com.sun.awt.AWTUtilities;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import ufps.negocio.ErrorHTML;
import ufps.util.Pila;

/**
 *
 * @author hogar
 */
public class InformeErrores extends javax.swing.JFrame {
private int x;
private int y;
private CrearArchivo myCre;
private String matriz[][];


    /**
     * Creates new form InformeErrores
     */
    public InformeErrores(CrearArchivo myCre) {
           initComponents();
           this.setLocationRelativeTo(null);
        this.myCre=myCre;                                     
        this.crearTablaAbaseErroresPila(this.myCre.getMyP().getM().getErrores());
        Shape forma = new RoundRectangle2D.Double(0, 0, this.getBounds().width, this.getBounds().height, 30, 30);
        AWTUtilities.setWindowShape(this, forma);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaErrores = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 255));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
        });
        jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel1MouseDragged(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Snap ITC", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 0, 0));
        jLabel1.setText("INFORME DE ERRORES");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar.png"))); // NOI18N
        jLabel2.setToolTipText("cerrar");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(138, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(136, 136, 136)
                .addComponent(jLabel2))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 562, -1));

        tablaErrores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "", ""
            }
        ));
        jScrollPane1.setViewportView(tablaErrores);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 38, 562, 246));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pdf-icon.png"))); // NOI18N
        jButton1.setToolTipText("dar click aca para el pdf");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 350, 86, -1));

        jLabel3.setFont(new java.awt.Font("Showcard Gothic", 1, 18)); // NOI18N
        jLabel3.setText("GENERAR PDF");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 300, -1, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ingeneria de sistemas.png"))); // NOI18N
        jLabel4.setText("jLabel4");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 290, 200, 190));

        pack();
    }// </editor-fold>//GEN-END:initComponents

     private void crearTablaAbaseErroresPila(Pila<ErrorHTML> p){
  String cad="";
  String etq="";   
  
                         while(!p.esVacio()){
                  ErrorHTML e=p.pop();
                  etq=e.getMyEtiqueta().getEtiqueta();
                        if(etq.equalsIgnoreCase("<html>")){
                        cad+=e.getMyError().getDescripcion()+";"+"<"+etq.substring(1,etq.length()-2)+" >"+",";
                        }
                        else{
                   cad+=e.getMyError().getDescripcion()+";"+etq+","; 
                        }
                         }
                         
    this.tablaErrores.setModel(new DefaultTableModel(this.crearMatrizR(cad),this.vectorColumnas()));                    
   
   }
    
    
     private String[][]crearMatrizR(String cad){ 
        String [] vec=cad.split(",");
        this.matriz=new String [vec.length][2];
        for(int i=0;i<this.matriz.length;i++){
            for(int j=0;j<this.matriz[i].length;j++){
                this.matriz[i][j]=vec[i].split(";")[j];
            }
        }
        return this.matriz;      
    }
    
    private String [] vectorColumnas(){
        String[] columna=new String [2];
        columna[0]="ETIQUETA";
        columna[1]="DESCRIPCION DEL ERROR"; 
        return columna;
    }
    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
       x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_jPanel1MousePressed

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseDragged
       Point ubicacion = MouseInfo.getPointerInfo().getLocation();//1
        setLocation(ubicacion.x - x, ubicacion.y - y);
    }//GEN-LAST:event_jPanel1MouseDragged

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String x;
        JFileChooser j = new JFileChooser();
        if(j.showSaveDialog(null)==j.APPROVE_OPTION){
            x = j.getSelectedFile().getAbsolutePath();
            if(!x.endsWith(".pdf")){
             x+=".pdf";
            }
                 try{
                this.myCre.getMyP().getM().generarPDF(matriz, x);
                JOptionPane.showMessageDialog(null,"SE HA GENERADO CORRETAMENTE EL PDF");
                  }catch(Exception e){
                     JOptionPane.showMessageDialog(null,"error al crear pdf");
                  }
            }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
  
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaErrores;
    // End of variables declaration//GEN-END:variables
}

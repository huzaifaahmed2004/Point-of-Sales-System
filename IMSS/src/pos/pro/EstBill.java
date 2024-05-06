/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.pro;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Go
 */


public class EstBill extends javax.swing.JPanel {
JpanelLoader jpload = new JpanelLoader();
int rows=0;
    int CI=0;
    public static String barcode_c = "0" ;
    public static String cus_id = "0";
    int quantity;
    JPanel x;
    public EstBill(JPanel p) {
        x=p;
        initComponents();
       data_load();
        
    }
public void tb_load(){
      try {
           DefaultTableModel dt = (DefaultTableModel) jTable2.getModel();
        int[] columnWidths = {140, 75, 60};  // Adjust the values based on your needs
int columnIndex = 0; // Start from the first column
for (int width : columnWidths) {
    TableColumn column = jTable2.getColumnModel().getColumn(columnIndex++);
    column.setMinWidth(width);
    column.setMaxWidth(width);
    column.setPreferredWidth(width);
}
          dt.setRowCount(0);
          
          Statement s= db.mycon().createStatement();
          
          ResultSet rs = s.executeQuery("SELECT * FROM products");
         
          
          while (rs.next()) {   
               Vector v = new Vector();
              v.add(rs.getString("pName"));
               v.add(rs.getString("pCompany"));
              v.add(rs.getString("pSize"));
               
               dt.addRow(v);
          }
           
      } catch (SQLException e) {
            System.out.println(e);
      }  
      
}
 public void data_load(){
  
  // load customer
  
      
     // load Product
  tb_load();
    
     try {
          
        Statement s = db.mycon().createStatement();
          ResultSet rs = s.executeQuery("SELECT * FROM extra WHERE exid =1");
          int li =0;
          if (rs.next()) {
               int i = Integer.valueOf(rs.getString("val"));
                li=i;
               i++;
      inid.setText(String.valueOf(i));
              
          }
           ResultSet rst = s.executeQuery("SELECT * FROM cart WHERE INID = "+li);
           
          
          while(rst.next()){
              CI=rst.getInt("cartID");
          }
      } catch (Exception e) {
      }
          
      
      // pluss new invoice
     
     
      
      
      
      
  }
 
  private static void printPanels(List<JPanel> panels) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();

        // Manually set the PageFormat to 13x19cm
        PageFormat pageFormat = printerJob.defaultPage();
        Paper paper = new Paper();
        double width = cmToPPI(13.5); // Convert centimeters to points (1 inch = 72 points)
        double height = cmToPPI(23);
        paper.setSize(width, height);
        pageFormat.setPaper(paper);

        // Set a printable object
        printerJob.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
                if (pageIndex >= panels.size()) {
                    return Printable.NO_SUCH_PAGE;
                }

                JPanel currentPanel = panels.get(pageIndex);

                double pageWidth = pageFormat.getImageableWidth();
                double pageHeight = pageFormat.getImageableHeight();

                // Calculate the scaling factors for width and height
                double panelWidth = currentPanel.getWidth();
                double panelHeight = currentPanel.getHeight();
                double scaleWidth = pageWidth / panelWidth;
                double scaleHeight = pageHeight / panelHeight;
                double scale = Math.min(scaleWidth, scaleHeight);

                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2d.scale(scale, scale);

                // Print the panel
                currentPanel.printAll(graphics);

                return Printable.PAGE_EXISTS;
            }
        }, pageFormat);

        // Show the print dialog
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }

    // Utility method to convert centimeters to points (1 inch = 72 points)
    private static double cmToPPI(double cm) {
        return cm / 2.54 * 72.0;
    }



    
 public void cart_total() {
    // Total amount calculation
    int numofrow = jTable1.getRowCount();
    double total = 0;

    for (int i = 0; i < numofrow; i++) {
        // Check if the value in the last column is not null and not empty
        Object valueObject = jTable1.getValueAt(i, 5);
        if (valueObject != null && !valueObject.toString().isEmpty()) {
            double value = Double.parseDouble(valueObject.toString());
            total += value;
        }else{
            System.out.println("Error");
        }
    }
    bill_tot.setText(Double.toString(total));

    // Total quantity calculation
    int numofrows = jTable1.getRowCount();
    double totals = 0;

    for (int i = 0; i < numofrows; i++) {
        // Check if the value in the second-to-last column is not null and not empty
        Object valueObject = jTable1.getValueAt(i, 3);
        if (valueObject != null && !valueObject.toString().isEmpty()) {
            double values = Double.parseDouble(valueObject.toString());
            totals += values;
        }
    }
    tot_qty.setText(Double.toString(totals));
}

 

public void tot() {
    try {
        cart_total();
        // Initialize variables with default values
        double disValue = 0.0;
        double totValue = 0.0;

        // Check if the discount field is not empty
        if (!Dis.getText().isEmpty()) {
            disValue = Double.parseDouble(Dis.getText());
        }

        // Check if the bill total field is not empty
        if (!bill_tot.getText().isEmpty()) {
            totValue = Double.parseDouble(bill_tot.getText());
        }

        // Calculate discounted amount
        double discounted = totValue - disValue;
        Discounted.setText(Double.toString(discounted));

        // Check if the paid amount field is not empty
       
    } catch (NumberFormatException e) {
        // Handle the NumberFormatException, for example, display an error message
        JOptionPane.showMessageDialog(null, "Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


 
 
 
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        c_search_tbl = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        c_search_tbl1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        inid = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        bill_tot = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        Discounted = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        tot_qty = new javax.swing.JLabel();
        Dis = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();

        setBackground(new java.awt.Color(204, 204, 204));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTable1.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jTable1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Company", "Size", "Qty", "Unit Price", "Total Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setShowGrid(true);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTable1MouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jTable2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Company", "Size"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setShowGrid(true);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTable2MouseEntered(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setText("Cart");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("product Name :");

        c_search_tbl.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        c_search_tbl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_search_tblActionPerformed(evt);
            }
        });
        c_search_tbl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                c_search_tblKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Company Name :");

        c_search_tbl1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        c_search_tbl1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_search_tbl1ActionPerformed(evt);
            }
        });
        c_search_tbl1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                c_search_tbl1KeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("INVOICE NO :");

        inid.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        inid.setText("01");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(c_search_tbl, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(c_search_tbl1)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inid))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(183, 183, 183)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(c_search_tbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(inid))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(c_search_tbl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton2.setBackground(new java.awt.Color(255, 102, 102));
        jButton2.setText("Remove");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 102, 102));
        jButton3.setText("Remove All");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addComponent(jButton2)
                .addGap(19, 19, 19)
                .addComponent(jButton3)
                .addContainerGap(148, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel7.setBackground(new java.awt.Color(204, 204, 204));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        bill_tot.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bill_tot.setText("00.00");
        bill_tot.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("Total Amount :");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Grand Total :");

        Discounted.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Discounted.setText("00.00");
        Discounted.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap(44, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Discounted, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bill_tot, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bill_tot, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Discounted, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(9, 9, 9))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Total Qty :");

        tot_qty.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tot_qty.setText("00");

        Dis.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Dis.setText("0");
        Dis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DisMouseClicked(evt);
            }
        });
        Dis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisActionPerformed(evt);
            }
        });
        Dis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DisKeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Discount :");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Dis, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tot_qty, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(tot_qty)))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Dis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jButton4.setBackground(new java.awt.Color(153, 255, 153));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton4.setText(" Print Bill");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(236, 236, 236))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // selected remove
        try {
            
            DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
            int rw = jTable1.getSelectedRow();
           
            dt.removeRow(rw);
            
            
        } catch (Exception e) {
        }
        
        cart_total(); 
         tot(); 
        
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // remove all
        DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
        dt.setRowCount(0);
        
         cart_total();
         tot(); 
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
          // data send to databace
          LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = currentDate.format(formatter);
            String INID=inid.getText();
            // `cartid`, `INID`, `Product_Name`, `Bar_code`, `qty`, `Unit_Price`, `Total_Price`
            
          DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
          int rc = dt.getRowCount();
          if(rc!=0){
              try {
            for (int i = 0; i < rc; i++) {
                
                String name = dt.getValueAt(i, 0).toString(); // get inid
                String comp = dt.getValueAt(i, 1).toString(); // get product name
                String size = dt.getValueAt(i, 2).toString(); // get barcode
                String qty = dt.getValueAt(i, 3).toString(); // get product qty
                String un_price = dt.getValueAt(i, 4).toString(); // get product unit price
                String tot_price = dt.getValueAt(i, 5).toString(); // get product total Price
            //deduct products from table
        
                // cart DB
             Statement s = db.mycon().createStatement();
            s.executeUpdate(" INSERT INTO cart (INID, pName, pCompany,pSize, pQuantity, pUPrice, pTPrice,Date) VALUES ('"+INID+"','"+name+"','"+comp+"','"+size+"','"+qty+"','"+un_price+"','"+tot_price+"','"+date+"') ");
           
            }
            
            
        } catch (HeadlessException | SQLException e) {
            System.out.println(e);
        }
  
        try {
            
            
            // sales DB
           

        // Format the date using a DateTimeFormatter
        
             //`saleid`, `INID`, `Cid`, `Customer_Name`, `Total_Qty`, `Total_Bill`, `Status`, `Balance`
            String inv_id = inid.getText();
            String cname  = "N/A";
            String totqty = tot_qty.getText();
            String tot_bil = bill_tot.getText();           
            String dis = Discounted.getText();
            Double disx = Double.valueOf(Discounted.getText());
            double tot = -disx;

            
             
             Statement ss = db.mycon().createStatement();
             ss.executeUpdate("INSERT INTO sales(INID, customerName,Date, totalQty, totalAmt,discountedAmt, Remaining) VALUES('"+inv_id+"','"+cname+"','"+date+"','"+totqty+"','"+tot_bil+"','"+dis+"','"+tot+"')");
              
            
            
        } catch (NumberFormatException | SQLException e) {
           
        }
  
        // save las inid number
        try {
            
           String id = inid.getText();
            Statement s = db.mycon().createStatement();
            s.executeUpdate("UPDATE  extra SET val='"+id+"' WHERE exid = 1");
            
            
        } catch (SQLException e) {
        }
        
      
       
         String ii=inid.getText();
        rows=rc;
        List<JPanel> panelsToPrint = new ArrayList<>();
//1 page
if (rows <= 22) {
    BILL b = new BILL(ii, CI,1,1);
    JPanel p = b.getJPanel();
    panelsToPrint.add(p);
} else
    //2page
    if (rows > 22 && rows <= 44) {
    BILL a = new BILL(ii, CI,1,1);
    JPanel p = a.getJPanel();
    panelsToPrint.add(p);

    BILL b = new BILL(ii, CI + 22,2,1);
    JPanel q = b.getJPanel();
    panelsToPrint.add(q);
}//3page
else
    
    if (rows > 44 && rows <= 66) {
    BILL a = new BILL(ii, CI,1,1);
    JPanel p = a.getJPanel();
    panelsToPrint.add(p);

    BILL b = new BILL(ii, CI + 22,2,1);
    JPanel q = b.getJPanel();
    panelsToPrint.add(q);
    
     BILL c = new BILL(ii, CI + 44,3,1);
    JPanel r = c.getJPanel();
    panelsToPrint.add(r);
}
else
    //4
    if (rows > 66 && rows <= 88) {
    BILL a = new BILL(ii, CI,1,1);
    JPanel p = a.getJPanel();
    panelsToPrint.add(p);

    BILL b = new BILL(ii, CI + 22,2,1);
    JPanel q = b.getJPanel();
    panelsToPrint.add(q);
    
     BILL c = new BILL(ii, CI + 44,3,1);
    JPanel r = c.getJPanel();
    panelsToPrint.add(r);
    
    BILL d = new BILL(ii, CI + 66,4,1);
    JPanel s = d.getJPanel();
    panelsToPrint.add(s);
}
else
    //5
    if (rows > 88 && rows <= 110) {
    BILL a = new BILL(ii, CI,1,1);
    JPanel p = a.getJPanel();
    panelsToPrint.add(p);

    BILL b = new BILL(ii, CI + 22,2,1);
    JPanel q = b.getJPanel();
    panelsToPrint.add(q);
    
     BILL c = new BILL(ii, CI + 44,3,1);
    JPanel r = c.getJPanel();
    panelsToPrint.add(r);
    
    BILL d = new BILL(ii, CI + 66,4,1);
    JPanel s = d.getJPanel();
    panelsToPrint.add(s);
    
     BILL e = new BILL(ii, CI + 88,5,1);
    JPanel t = e.getJPanel();
    panelsToPrint.add(t);
}else
    //6
    if (rows > 110 && rows <= 132) {
    BILL a = new BILL(ii, CI,1,1);
    JPanel p = a.getJPanel();
    panelsToPrint.add(p);

    BILL b = new BILL(ii, CI + 22,2,1);
    JPanel q = b.getJPanel();
    panelsToPrint.add(q);
    
     BILL c = new BILL(ii, CI + 44,3,1);
    JPanel r = c.getJPanel();
    panelsToPrint.add(r);
    
    BILL d = new BILL(ii, CI + 66,4,1);
    JPanel s = d.getJPanel();
    panelsToPrint.add(s);
    
     BILL e = new BILL(ii, CI + 88,5,1);
    JPanel t = e.getJPanel();
    panelsToPrint.add(t);
    
     BILL f = new BILL(ii, CI + 110,6,1);
    JPanel u = f.getJPanel();
    panelsToPrint.add(u);
}

         JOptionPane.showMessageDialog(null, "Printing...", "Info", JOptionPane.INFORMATION_MESSAGE);
   
 printPanels(panelsToPrint);

       int xx=Integer.valueOf(INID);
         deleteRecords(xx);
         EstBill emp =new EstBill(x);
        jpload.jPanelLoader(x, emp);}
          else{
           JOptionPane.showMessageDialog(null, "Cart is empty", "Error", JOptionPane.ERROR_MESSAGE);
   
          }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void DisKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DisKeyReleased
         tot();   // TODO add your handling code here:
    }//GEN-LAST:event_DisKeyReleased
public void deleteRecords(int inid) {
    try (Connection connection = db.mycon()) {
        // Delete records from cart
        String deleteCartQuery = "DELETE FROM cart WHERE INID = ?";
        try (PreparedStatement deleteCartStatement = connection.prepareStatement(deleteCartQuery)) {
            deleteCartStatement.setInt(1, inid);
            deleteCartStatement.executeUpdate();
        }

        // Delete records from sales
        String deleteSalesQuery = "DELETE FROM sales WHERE INID = ?";
        try (PreparedStatement deleteSalesStatement = connection.prepareStatement(deleteSalesQuery)) {
            deleteSalesStatement.setInt(1, inid);
            deleteSalesStatement.executeUpdate();
        }
         int id=inid-1;
            Statement s = db.mycon().createStatement();
            s.executeUpdate("UPDATE  extra SET val='"+id+"' WHERE exid = 1");
            

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    private void c_search_tblActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_search_tblActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_c_search_tblActionPerformed

    private void c_search_tblKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c_search_tblKeyReleased

        String name = c_search_tbl.getText();
        try {

            DefaultTableModel dt = (DefaultTableModel) jTable2.getModel();
            dt.setRowCount(0);
            Statement s = db.mycon().createStatement();

            ResultSet rs = s.executeQuery("SELECT * FROM products WHERE pName LIKE '%"+name+"%' ");

            while (rs.next()) {
                Vector v = new Vector();

                 v.add(rs.getString("pName"));
               v.add(rs.getString("pCompany"));
              v.add(rs.getString("pSize"));
               

                dt.addRow(v);

            }

        } catch (Exception e) {
            tb_load();

        }
    }//GEN-LAST:event_c_search_tblKeyReleased


    void input() {
    // Display an input dialog to get the quantity
    String input = JOptionPane.showInputDialog(null, "Enter Quantity:", "Quantity Input", JOptionPane.QUESTION_MESSAGE);

    // Check if the user pressed cancel or closed the dialog
    if (input == null) {
        // User canceled the input, no need to show an error message
        return;
    }

    // Parse the input as an integer
    try {
        quantity = Integer.parseInt(input);
    } catch (NumberFormatException e) {
        // Handle the case where the input is not a valid integer
        JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
          int r = jTable2.getSelectedRow();

    input();

    if (quantity != 0) {
        
        
          
         DefaultTableModel dt =  (DefaultTableModel) jTable1.getModel();
        
            int[] columnWidths = {125, 70, 40,40,65,90};// Adjust the values based on your needs
int columnIndex = 0; // Start from the first column

for (int width : columnWidths) {
    TableColumn column = jTable1.getColumnModel().getColumn(columnIndex++);
    column.setMinWidth(width);
    column.setMaxWidth(width);
    column.setPreferredWidth(width);
}
        String name = jTable2.getValueAt(r, 0).toString();
        String company = jTable2.getValueAt(r, 1).toString();
        String size = jTable2.getValueAt(r, 2).toString();

        try {

            // Check if the product is already in jTable1
            boolean productExists = false;
            int existingRow = -1;

            for (int i = 0; i < dt.getRowCount(); i++) {
                String existingName = dt.getValueAt(i, 0).toString();
                String existingCompany = dt.getValueAt(i, 1).toString();
                String existingSize = dt.getValueAt(i, 2).toString();

                if (existingName.equals(name) && existingCompany.equals(company) && existingSize.equals(size)) {
                    productExists = true;
                    existingRow = i;
                    break;
                }
            }
int up=0;
            if (productExists) {
                try{
                    Statement s = db.mycon().createStatement();         
         ResultSet rs = s.executeQuery("SELECT * FROM products WHERE pName LIKE '%"+name+"%' AND pCompany LIKE '%"+company+"%' AND pSize LIKE '%"+size+"%'  ");
         
         if (rs.next()) {     
             int availableStock = rs.getInt("pQuantity");
              up=rs.getInt("pSellPrice");
          int existingQuantity = (int) dt.getValueAt(existingRow, 3);
double existingPrice = (double) dt.getValueAt(existingRow, 5);
int p = (int) existingPrice;  // Assuming the price is an integer, adjust accordingly if it's a double
int qt = existingQuantity + quantity;
int min=availableStock-existingQuantity;
double updatedPrice = qt * p;
         if (qt <= availableStock) {
                // Sufficient stock available, proceed with the update
                int qtt = qt + quantity;
                double updatedPric = qtt * up;

                dt.setValueAt(qt, existingRow, 3);
                dt.setValueAt(updatedPric, existingRow, 5);
                
            } else {
                // Insufficient stock, show an error message
                JOptionPane.showMessageDialog(null, "Not enough stock available.\nYou can add "+min+" more", "Error", JOptionPane.ERROR_MESSAGE);
                
            }
                }}catch(SQLException e){
                    
                }
//                // Product already exists, update the quantity
//              int existingQuantity = (int) dt.getValueAt(existingRow, 3);
//double existingPrice = (double) dt.getValueAt(existingRow, 5);
//int p = (int) existingPrice;  // Assuming the price is an integer, adjust accordingly if it's a double
//int qt = existingQuantity + quantity;
//double updatedPrice = qt * p;
//
//dt.setValueAt(qt, existingRow, 3);
//dt.setValueAt(updatedPrice, existingRow, 5);

            } else {
              
         Statement s = db.mycon().createStatement();         
         ResultSet rs = s.executeQuery("SELECT * FROM products WHERE pName LIKE '%"+name+"%' AND pCompany LIKE '%"+company+"%' AND pSize LIKE '%"+size+"%'  ");
         
         while (rs.next()) {             
              int stock=rs.getInt("pQuantity");
         int xyz=stock-quantity;
                    if(xyz>=0)
                    {
             Vector v =new Vector();
             v.add(rs.getString("pName"));
             v.add(rs.getString("pCompany"));
             v.add(rs.getString("pSize"));
             
             
             v.add(quantity);
             v.add(rs.getString("pSellPrice"));
             double sp=Double.parseDouble(rs.getString("pSellPrice"));
             
             v.add(sp*quantity);
             
             
             dt.addRow(v);
             }else{
                          JOptionPane.showMessageDialog(null, "Stock out \n remaining products are "+stock, "Error", JOptionPane.ERROR_MESSAGE);
   
                    }
         }
        
            }

            tot();
            quantity = 0;
            
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
        
        
        
        
        
        
        
        
        
        
        
//        
//        
//       int r = jTable2.getSelectedRow();
//       
//       input();
//       if(quantity!=0){
//       String name  = jTable2.getValueAt(r, 0).toString();
//       String company  = jTable2.getValueAt(r, 1).toString();
//       String   Size= jTable2.getValueAt(r, 2).toString();
//        try {
//        
//         DefaultTableModel dt =  (DefaultTableModel) jTable1.getModel();
//        
//           int[] columnWidths = {150, 110, 60,50,100,120}; // Adjust the values based on your needs
//int columnIndex = 0; // Start from the first column
//
//for (int width : columnWidths) {
//    TableColumn column = jTable1.getColumnModel().getColumn(columnIndex++);
//    column.setMinWidth(width);
//    column.setMaxWidth(width);
//    column.setPreferredWidth(width);
//}
//         Statement s = db.mycon().createStatement();         
//         ResultSet rs = s.executeQuery("SELECT * FROM products WHERE pName LIKE '%"+name+"%' AND pCompany LIKE '%"+company+"%' AND pSize LIKE '%"+Size+"%'  ");
//         
//         while (rs.next()) {             
//              int stock=rs.getInt("pQuantity");
//         int xyz=stock-quantity;
//                    if(xyz>=0)
//                    {
//             Vector v =new Vector();
//             v.add(rs.getString("pName"));
//             v.add(rs.getString("pCompany"));
//             v.add(rs.getString("pSize"));
//             
//             
//             v.add(quantity);
//             v.add(rs.getString("pSellPrice"));
//             double sp=Double.parseDouble(rs.getString("pSellPrice"));
//             
//             v.add(sp*quantity);
//             
//             
//             dt.addRow(v);
//             }else{
//                          JOptionPane.showMessageDialog(null, "Stock out \n remaining products are "+stock, "Error", JOptionPane.ERROR_MESSAGE);
//   
//                    }
//         }
//         quantity=0;
//         tot();
//     } catch (SQLException e) {
//         System.out.println(e);
//     }}
    }//GEN-LAST:event_jTable2MouseClicked

    private void DisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DisActionPerformed

    private void c_search_tbl1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_search_tbl1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_c_search_tbl1ActionPerformed

    private void c_search_tbl1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c_search_tbl1KeyReleased
      
        String name = c_search_tbl1.getText();
        try {

            DefaultTableModel dt = (DefaultTableModel) jTable2.getModel();
            dt.setRowCount(0);
            Statement s = db.mycon().createStatement();

            ResultSet rs = s.executeQuery("SELECT * FROM products WHERE pCompany LIKE '%"+name+"%' ");

            while (rs.next()) {
                Vector v = new Vector();

                 v.add(rs.getString("pName"));
               v.add(rs.getString("pCompany"));
              v.add(rs.getString("pSize"));
               

                dt.addRow(v);

            }

        } catch (Exception e) {
            tb_load();

        }
    }//GEN-LAST:event_c_search_tbl1KeyReleased

    private void DisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DisMouseClicked
      Dis.setText("");
    }//GEN-LAST:event_DisMouseClicked

    private void jTable2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable2MouseEntered

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int r = jTable1.getSelectedRow();
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    double up = Double.parseDouble(jTable1.getValueAt(r, 4).toString());

    String newQuantityString = JOptionPane.showInputDialog(null, "Enter new quantity:", "Update Quantity", JOptionPane.QUESTION_MESSAGE);

    // Check if the user clicked OK and entered a valid quantity
    if (newQuantityString != null && !newQuantityString.isEmpty()) {
        try {
            int newQuantity = Integer.parseInt(newQuantityString);

            // Check if there is enough stock in the products table
            String productName = model.getValueAt(r, 0).toString();
            String productCompany = model.getValueAt(r, 1).toString();
            String productSize = model.getValueAt(r, 2).toString();

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery("SELECT pQuantity FROM products WHERE pName LIKE '%" + productName + "%' AND pCompany LIKE '%" + productCompany + "%' AND pSize LIKE '%" + productSize + "%'");

            if (rs.next()) {
                int availableStock = rs.getInt("pQuantity");

                if (newQuantity <= availableStock) {
                    // Update the quantity in the table
                    model.setValueAt(String.valueOf(newQuantity), r, 3);
                    model.setValueAt(String.valueOf(up * newQuantity), r, 5);

                    // You can add further logic here to update your database or perform other actions
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough stock available.\nremaining Stock is "+availableStock, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid quantity. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            System.out.println(e);
        }
        tot();
    }     // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Dis;
    private javax.swing.JLabel Discounted;
    private javax.swing.JLabel bill_tot;
    private javax.swing.JTextField c_search_tbl;
    private javax.swing.JTextField c_search_tbl1;
    private javax.swing.JLabel inid;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel tot_qty;
    // End of variables declaration//GEN-END:variables
}

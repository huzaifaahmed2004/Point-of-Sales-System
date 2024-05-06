/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pos.pro;

import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
/**
 *
 * @author huzai
 */
public class BILL extends javax.swing.JFrame {
String INID;
int ci=0,cimax=0,page=0,sss=0;
public JPanel getJPanel() {
        return jPanel1;
       
    }

    /**
     * Creates new form BILL
     */
   
    public BILL(String x,int y,int z,int n) {
        initComponents();
         JTableHeader header = jTable1.getTableHeader();

        // Set a bold font for the header
        Font boldFont = new Font(header.getFont().getName(), Font.BOLD, header.getFont().getSize() + 2); // You can adjust the size increase
       header.setFont(boldFont);
        ci=y;
        cimax=y+22;
        INID=x;
        page=z;
        sss=n;
        Load();
    }
public void Load() {
    String pg=String.valueOf(page);
    p1.setText(pg);
    DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
      int[] columnWidths = {240,80, 50,50,45,85,104}; // Adjust the values based on your needs
int columnIndex = 0; // Start from the first column

for (int width : columnWidths) {
    TableColumn column = jTable1.getColumnModel().getColumn(columnIndex++);
    column.setMinWidth(width);
    column.setMaxWidth(width);
    column.setPreferredWidth(width);
}
    dt.setRowCount(0);

  
      if(ci>0){   
            try (Connection connection = db.mycon();
         PreparedStatement preparedStatement = connection.prepareStatement(  "SELECT * FROM cart WHERE INID = ? AND cartID > ? AND cartID <= ?");) 
         {
        preparedStatement.setString(1, INID);
        preparedStatement.setInt(2, ci);
        preparedStatement.setInt(3, cimax);

        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) 
        {
            Vector<Object> v = new Vector<>();

            String name = rs.getString("pName");
            String company = rs.getString("pCompany");
            String pSize = rs.getString("pSize");
            String unittype = null;

            v.add(name);
            v.add(company);
            v.add(pSize);

            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM products WHERE pName = ? AND pCompany = ? AND pSize = ?")) {
                ps.setString(1, name);
                ps.setString(2, company);
                ps.setString(3, pSize);

                ResultSet r = ps.executeQuery();

                if (r.next()) {
                    unittype = r.getString("pUnitType");
                }
            }

            v.add(unittype);

            int qty = rs.getInt("pQuantity");
            double up = rs.getDouble("pUPrice");

            v.add(qty);
            v.add(up);
            v.add(qty * up);

            dt.addRow(v);
        }
 
         SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
         
         Statement s = connection.createStatement();
         {
    ResultSet salesResultSet = s.executeQuery("SELECT * FROM sales WHERE INID = " + INID);

    if (salesResultSet.next()) {
        String customer = salesResultSet.getString("customerName");
        Date saleDate = salesResultSet.getDate("Date");
        String formattedDate = dateFormat.format(saleDate);
        int qtyy = salesResultSet.getInt("totalQty");
        double tot = salesResultSet.getDouble("totalAmt");
        double discounted = salesResultSet.getDouble("discountedAmt");
        double remaining = salesResultSet.getDouble("Remaining");
        double dis = tot - discounted;
        double paid = discounted + remaining;
                
                
                
        Date.setText(formattedDate);
        cName.setText(customer);
        tp.setText(String.valueOf(tot));
         qty.setText(String.valueOf(qtyy));
         discount.setText(String.valueOf(dis));
         dp.setText(String.valueOf(discounted));
         if(sss==1){
                p.setText("Est Bill");
         }else{
        p.setText(String.valueOf(paid));
         }
    }
    }}
        catch (SQLException e) {
        e.printStackTrace();
    }}else{
          ci=0;
                     try (Connection connection = db.mycon();
         PreparedStatement preparedStatement = connection.prepareStatement(  "SELECT * FROM cart WHERE INID = ?");) 
         {
        preparedStatement.setString(1, INID);

        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) 
        {
            Vector<Object> v = new Vector<>();

            String name = rs.getString("pName");
            String company = rs.getString("pCompany");
            String pSize = rs.getString("pSize");
            String unittype = null;

            v.add(name);
            v.add(company);
            v.add(pSize);

            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM products WHERE pName = ? AND pCompany = ? AND pSize = ?")) {
                ps.setString(1, name);
                ps.setString(2, company);
                ps.setString(3, pSize);

                ResultSet r = ps.executeQuery();

                if (r.next()) {
                    unittype = r.getString("pUnitType");
                }
            }

            v.add(unittype);

            int qty = rs.getInt("pQuantity");
            double up = rs.getDouble("pUPrice");

            v.add(qty);
            v.add(up);
            v.add(qty * up);

            dt.addRow(v);
        }
 
         SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
         
         Statement s = connection.createStatement();
         {
    ResultSet salesResultSet = s.executeQuery("SELECT * FROM sales WHERE INID = " + INID);

    if (salesResultSet.next()) {
        String customer = salesResultSet.getString("customerName");
        Date saleDate = salesResultSet.getDate("Date");
        String formattedDate = dateFormat.format(saleDate);
        int qtyy = salesResultSet.getInt("totalQty");
        double tot = salesResultSet.getDouble("totalAmt");
        double discounted = salesResultSet.getDouble("discountedAmt");
        double remaining = salesResultSet.getDouble("Remaining");
        double dis = tot - discounted;
        double paid = discounted + remaining;
                
                
                
        Date.setText(formattedDate);
        cName.setText(customer);
        tp.setText(String.valueOf(tot));
         qty.setText(String.valueOf(qtyy));
         discount.setText(String.valueOf(dis));
         dp.setText(String.valueOf(discounted));
        if(sss==1){
                p.setText("Est Bill");
         }else{
        p.setText(String.valueOf(paid));
         }
        
    }
    }}
        catch (SQLException e) {
        e.printStackTrace();
    }
            }
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
        jTable1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cName = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        Date = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        tp = new javax.swing.JLabel();
        discount = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        dp = new javax.swing.JLabel();
        p = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        qty = new javax.swing.JLabel();
        pge = new javax.swing.JLabel();
        pge1 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        p1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        tp1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setText("Huzaifa Sentry and Hardware Store");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel2.setText("Fateh Jang Road, Near T&T Gate F17, Nogazi, Islamabad");

        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Name", "Company", "Size", "Unit", "Qty", "Unit Price", "Total Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTable1.setRowHeight(22);
        jTable1.setShowGrid(true);
        jScrollPane1.setViewportView(jTable1);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel3.setText(" 0330 8478754");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel4.setText("Customer Name:");

        cName.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        cName.setText("XYZ");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel6.setText("Date:");

        Date.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        Date.setText("XYZ");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel7.setText("Total Price :");

        tp.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        tp.setText("XYZ");

        discount.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        discount.setText("XYZ");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel8.setText("Discount :");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel15.setText("Grand Total :");

        dp.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        dp.setText("XYZ");

        p.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        p.setText("XYZ");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel16.setText("Paid :");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel17.setText("Total Quantity :");

        qty.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        qty.setText("XYZ");

        pge.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        pge.setText("x");

        pge1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        pge1.setText("Page ");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel18.setText("Page :");

        p1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        p1.setText("XYZ");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel9.setText("Prev Balance :");

        tp1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        tp1.setText("XYZ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(p, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dp, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(qty, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(discount, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tp1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tp, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(pge1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pge, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(140, 140, 140))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(122, 122, 122))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cName, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Date, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 661, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(252, 252, 252)
                        .addComponent(jLabel3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(Date))
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cName))
                .addGap(4, 4, 4)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(tp1))
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pge)
                            .addComponent(pge1)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(tp))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(qty))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(discount))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(dp))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel18)
                                .addComponent(p1))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel16)
                                .addComponent(p)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 659, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Date;
    private javax.swing.JLabel cName;
    private javax.swing.JLabel discount;
    private javax.swing.JLabel dp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel p;
    private javax.swing.JLabel p1;
    private javax.swing.JLabel pge;
    private javax.swing.JLabel pge1;
    private javax.swing.JLabel qty;
    private javax.swing.JLabel tp;
    private javax.swing.JLabel tp1;
    // End of variables declaration//GEN-END:variables
}

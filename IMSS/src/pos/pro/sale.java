/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pos.pro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author huzai
 */
public class sale extends javax.swing.JPanel {
ArrayList<String> invoice = new ArrayList<>();

    /**
     * Creates new form sale
     */
    public sale() {
        initComponents();
    initializeDatePickers();
    addDatePickersListeners();
    loadData();
    tb_load();
    }
    private void initializeDatePickers() {
    from.setDate(getYesterday());
    To.setDate(new Date());
}

private Date getYesterday() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -1);
    return calendar.getTime();
}
public void loadData() {
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;

    try {
        // Prepare SQL statement
        String query = "SELECT * FROM sales WHERE Date BETWEEN ? AND ?";
        preparedStatement = db.mycon().prepareStatement(query);

        // Set parameters
        java.sql.Date sqlFromDate = new java.sql.Date(from.getDate().toInstant().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000);
        java.sql.Date sqlToDate = new java.sql.Date(To.getDate().toInstant().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000);

        preparedStatement.setDate(1, sqlFromDate);
        preparedStatement.setDate(2, sqlToDate);

        // Execute query
        resultSet = preparedStatement.executeQuery();

        int quantity = 0;
        double amount = 0;

        // Process the result set (modify as needed)
        while (resultSet.next()) {
            quantity += resultSet.getInt("totalQty");
            amount += resultSet.getDouble("discountedAmt");
            invoice.add(resultSet.getString("INID"));

            // Process the retrieved data as needed

            // Or update your GUI components or data structures with the retrieved data
        }

        Amt.setText(String.valueOf(amount));
        Qty.setText(String.valueOf(quantity));

    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            // Close resources
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

private void addDatePickersListeners() {
        from.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    invoice.clear();
                    loadData();
    tb_load();
                }
            }
        });

        To.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    invoice.clear();
                   loadData();
    tb_load();
                }
            }
        });
    }
public void tb_load() {
    try {
        DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
        dt.setRowCount(0);

        Connection connection = db.mycon();
      String query = "SELECT pName, pCompany, pSize, SUM(pQuantity) AS totalQuantity " +
               "FROM cart " +
               "WHERE INID IN (" + String.join(",", Collections.nCopies(invoice.size(), "?")) + ") " +
               "AND Date BETWEEN ? AND ? " +
               "GROUP BY pName, pCompany, pSize";



       
 java.sql.Date sqlFromDate = new java.sql.Date(from.getDate().toInstant().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000);
        java.sql.Date sqlToDate = new java.sql.Date(To.getDate().toInstant().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000);
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int j = 0; j < invoice.size(); j++) {
                preparedStatement.setString(j + 1, invoice.get(j));
            }
preparedStatement.setDate(invoice.size() + 1, sqlFromDate);
    preparedStatement.setDate(invoice.size() + 2, sqlToDate);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("pName"));
                    v.add(rs.getString("pCompany"));
                    v.add(rs.getString("pSize"));
                    v.add(rs.getString("totalQuantity"));
                    dt.addRow(v);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
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
        from = new com.toedter.calendar.JDateChooser();
        To = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Amt = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Qty = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBackground(new java.awt.Color(204, 204, 204));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("From :");

        from.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fromMouseClicked(evt);
            }
        });
        from.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fromPropertyChange(evt);
            }
        });

        To.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ToMouseClicked(evt);
            }
        });
        To.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                ToPropertyChange(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("To :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(from, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(To, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(from, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(To, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Earned :");

        Amt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Amt.setText("000");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Quantity Sold :");

        Qty.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Qty.setText("000");

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Product Name", "Company", "Size", "Quantity"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Amt)
                .addGap(53, 53, 53)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Qty)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 838, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(Amt)
                    .addComponent(jLabel5)
                    .addComponent(Qty))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void ToPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_ToPropertyChange
      if (To.getDate() != null && To.getDate().after(new Date())) {
                    // If it is, reset it to the current date
                    To.setDate(new Date());}
      
    }//GEN-LAST:event_ToPropertyChange

    private void fromPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fromPropertyChange
        if (from.getDate() != null && from.getDate().after(new Date())) {
                    // If it is, reset it to the current date
                    from.setDate(new Date());}
        
    }//GEN-LAST:event_fromPropertyChange

    private void fromMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fromMouseClicked
       loadData();
    }//GEN-LAST:event_fromMouseClicked

    private void ToMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ToMouseClicked
       loadData();
    }//GEN-LAST:event_ToMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Amt;
    private javax.swing.JLabel Qty;
    private com.toedter.calendar.JDateChooser To;
    private com.toedter.calendar.JDateChooser from;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}

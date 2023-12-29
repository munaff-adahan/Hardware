/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import static gui.Invoice.customer_id;
import static gui.Invoice.uniqueId2;
import java.awt.Color;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import model.MySQL;
import net.sf.jasperreports.data.empty.EmptyDataAdapter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author MUNAFF
 */
public class Counter extends javax.swing.JPanel {

    public static int proItemNo = 1;
    DecimalFormat df = new DecimalFormat("0.00");
    Color cl = new Color(204, 204, 204);

    public static int customer_id = 0;
    public static String UNIQUE_INVOICE_ID = "";
    public static String uniqueId2 = "";
    public static String PAYMENTTYPE = "";
    public static String Payment = "";
    public static String TotalPayment = "";
    public static String balance = "";

    public int trowC = 0;

    /**
     * Creates new form stock_panel
     */
    public Counter() {
        initComponents();
        loadProduct();
        loadPaymenyType();
    }

    public void resetField() {
        jTextField2.setText("Enter Quantity");
        jTextField3.setText("Product Number");
        jTextField2.setForeground(this.cl);
        jTextField3.setForeground(this.cl);
        loadProduct();

    }

    public void loadPaymenyType() {

        try {
            ResultSet rs = MySQL.search("SELECT * FROM `payment_type`");

            Vector v = new Vector();

            v.add("Select");

            while (rs.next()) {
                v.add(rs.getString("name"));
            }

            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v);
            jComboBox1.setModel(dcm);
        } catch (Exception e) {
        }

    }

    public void updateTotal() {

        double total = 0;

        if (jTextField4.getText().equals("")) {

            for (int i = 0; i < jTable2.getRowCount(); i++) {
                String t = jTable2.getValueAt(i, 6).toString();
                total = total + Double.parseDouble(t);

            }
            jLabel23.setText(df.format(total));

        } else {

            String dis = jTextField4.getText();
            for (int i = 0; i < jTable2.getRowCount(); i++) {
                String t = jTable2.getValueAt(i, 6).toString();
                total = total + Double.parseDouble(t);

            }
            total = total - Double.parseDouble(dis);
            jLabel23.setText(df.format(total));

        }

    }

    public void loadProduct() {

        try {
            ResultSet rs = MySQL.search("SELECT * FROM `stock` INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` INNER JOIN `brand` ON `product`.`brand_id`=`brand`.`id` INNER JOIN `category` ON `product`.`category_id`=`category`.`id` WHERE `stock`.`quantity`> 0");

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("product.name"));
                v.add(rs.getString("brand.name"));
                v.add(rs.getString("category.name"));
                v.add(rs.getString("stock.selling_price"));
                v.add(rs.getString("product.product_no"));
                v.add(rs.getString("stock.quantity"));

                dtm.addRow(v);

            }
            jTable1.setModel(dtm);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void searchProduct() {

        String text = jTextField1.getText().toUpperCase();

        try {
            ResultSet rs = MySQL.search("SELECT * FROM `stock` INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` INNER JOIN `brand` ON `product`.`brand_id`=`brand`.`id` INNER JOIN `category` ON `product`.`category_id`=`category`.`id` WHERE `product`.`name` LIKE '%" + text + "%' OR `category`.`name` LIKE '%" + text + "%' OR `brand`.`name` LIKE '%" + text + "%' AND  `stock`.`quantity`> 0");

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("product.name"));
                v.add(rs.getString("brand.name"));
                v.add(rs.getString("category.name"));
                v.add(rs.getString("stock.selling_price"));
                v.add(rs.getString("product.product_no"));

                dtm.addRow(v);

            }
            jTable1.setModel(dtm);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void setProduct() {
        ListSelectionListener twl = (ListSelectionEvent arg0) -> {
            int SelectedRow = jTable1.getSelectedRow();
            if (SelectedRow != -1) {

                String pNo = jTable1.getValueAt(SelectedRow, 4).toString();
                jTextField3.setText(pNo);

            }
        };

        jTable1.getSelectionModel().addListSelectionListener(twl);

    }

    public void getFocus() {

        jTextField3.setForeground(Color.BLACK);

        jTextField2.grabFocus();

    }

    public void invoicePrint() {

        if (jLabel26.getForeground().equals(Color.GREEN)) {
            balance = "+" + jLabel26.getText();

        } else if (jLabel26.getForeground().equals(Color.RED)) {
            balance = "-" + jLabel26.getText();

        } else {

            balance = jLabel26.getText();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss a");
        Date dToday1 = new Date();
        Date tToday = new Date();
        String dateNowN1 = sdf.format(dToday1);
        String timwNowN = sdf1.format(tToday);

        String CashierName = null;
        String CustomerName = null;

        String discount = "0.00";
        if (!jTextField4.getText().equals("")) {
            discount = jTextField4.getText();

        }
        String rowCount = String.valueOf(trowC);
        Double grandTotal = Double.parseDouble(discount) + Double.parseDouble(TotalPayment);

        try {
            ResultSet rs = MySQL.search("SELECT * FROM `user` WHERE `id`='" + SignIn.userId + "'");
            ResultSet rs1 = MySQL.search("SELECT `customer`.`unique_id` FROM `invoice` INNER JOIN `customer` ON `invoice`.`customer_id`=`customer`.`id` WHERE `invoice`.`unique_id`='" + UNIQUE_INVOICE_ID + "'");
            if (rs.next()) {
                CashierName = rs.getString("name");
            }
            if (rs1.next()) {
                CustomerName = rs1.getString("customer.unique_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String reportUrl = "/reports/invoice_1.jasper";
            InputStream reportFile = null;
            reportFile = getClass().getResourceAsStream(reportUrl);
            HashMap parameters = new HashMap();

            parameters.put("Parameter1", dateNowN1);
            parameters.put("Parameter2", CashierName);
            parameters.put("Parameter3", UNIQUE_INVOICE_ID);
            parameters.put("Parameter4", CustomerName);
            parameters.put("Parameter5", PAYMENTTYPE);
            parameters.put("Parameter6", String.valueOf(grandTotal) + "0");
            parameters.put("Parameter7", discount + ".00");
            parameters.put("Parameter8", TotalPayment);
            parameters.put("Parameter9", Payment + ".00");
            parameters.put("Parameter10", balance + "0");
            parameters.put("Parameter11", rowCount);
            parameters.put("Parameter12", timwNowN);

            TableModel tm = jTable2.getModel();
            //JREmptyDataSource dataSource = new JREmptyDataSource();
            JRTableModelDataSource dataSource = new JRTableModelDataSource(tm);

            JasperPrint print = JasperFillManager.fillReport(reportFile, parameters, dataSource);
            JasperViewer Jviewer = new JasperViewer(print, false);
            Jviewer.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createCustomer() {

        long mTime1 = System.currentTimeMillis();
        customer_id = customer_id + 1;
        uniqueId2 = mTime1 + "Us" + SignIn.userId + "-Cus" + customer_id;

        MySQL.iud("INSERT INTO `customer` (`unique_id`) VALUES('" + uniqueId2 + "')");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(1600, 800));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setFont(new java.awt.Font("Mongolian Baiti", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Name", "Brand", "Type", "Price", "Product Number", "Quantity"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 1458, 217));

        jLabel1.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel1.setText("Search Items");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 30, -1, -1));

        jTextField1.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 545, -1));

        jButton1.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add.png"))); // NOI18N
        jButton1.setText("Add");
        jButton1.setPreferredSize(new java.awt.Dimension(59, 30));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 300, 110, 40));

        jTextField2.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(204, 204, 204));
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("Enter Quantity");
        jTextField2.setPreferredSize(new java.awt.Dimension(59, 30));
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField2FocusLost(evt);
            }
        });
        jTextField2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField2MouseClicked(evt);
            }
        });
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField2KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });
        add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 297, 150, 40));

        jTextField3.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(204, 204, 204));
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setText("Product Number");
        jTextField3.setPreferredSize(new java.awt.Dimension(140, 30));
        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField3FocusGained(evt);
            }
        });
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 300, 220, 40));

        jLabel2.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel2.setText("QTY");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, -1, -1));

        jLabel3.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel3.setText("Product  Number");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 300, -1, 40));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Product Name", "Stock No", "Product No", "Item Price", "Qty", "Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 1520, 310));

        jButton2.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/clear.png"))); // NOI18N
        jButton2.setText("Clear");
        jButton2.setPreferredSize(new java.awt.Dimension(59, 30));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 300, 120, 40));

        jLabel25.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel25.setText("Balance");
        add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 760, -1, -1));

        jLabel23.setFont(new java.awt.Font("Mongolian Baiti", 0, 18)); // NOI18N
        jLabel23.setText("0.00");
        add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 690, -1, -1));

        jComboBox1.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 760, 160, -1));

        jLabel22.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel22.setText("Payment Method");
        add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 760, -1, -1));

        jLabel21.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel21.setText("Total Payment");
        add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 690, -1, -1));

        jTextField5.setEditable(false);
        jTextField5.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });
        add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 690, 200, -1));

        jLabel24.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel24.setText("Payment");
        add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 690, -1, -1));

        jLabel26.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel26.setText("0.00");
        add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 760, -1, -1));

        jButton3.setFont(new java.awt.Font("Mongolian Baiti", 1, 36)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/invoice.png"))); // NOI18N
        jButton3.setText("Print Invoice");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 680, 800, 130));

        jLabel4.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel4.setText("Discount");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 720, 80, 30));

        jTextField4.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jTextField4.setEnabled(false);
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });
        add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 720, 160, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        searchProduct();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String pNo = jTextField3.getText();
        String qty = jTextField2.getText();
        if (pNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Select or Enter Product Number", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (jTextField2.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please Enter valid Qunatity", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {
                ResultSet rs = MySQL.search("SELECT DISTINCT `stock`.`id`,`product`.`name`,`product`.`product_no`,`stock`.`quantity`,`stock`.`selling_price`\n"
                        + "FROM `product`\n"
                        + "INNER JOIN `stock` ON `product`.`id` = `stock`.`product_id`\n"
                        + "WHERE `product`.`product_no` = '" + pNo + "'");

                if (rs.next()) {

                    int availbleQty = Integer.valueOf(rs.getString("quantity"));
                    String pName = rs.getString("product.name");
                    String price = rs.getString("stock.selling_price");
                    String sid = rs.getString("stock.id");

                    if (Integer.valueOf(qty) > availbleQty) {

                        JOptionPane.showMessageDialog(this, "Quantity Out of Stock", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {

                        DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();

                        boolean isFound = false;
                        int x = -1;

                        for (int i = 0; i < dtm.getRowCount(); i++) {
                            String s = jTable2.getValueAt(i, 2).toString();

                            if (s.equals(sid)) {
                                isFound = true;
                                x = i;
                                break;

                            }

                        }
                        //add

                        if (isFound) {
                            int option = JOptionPane.showConfirmDialog(this, "This product is already added.Do you want update?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                            if (option == JOptionPane.YES_OPTION) {

                                int oldQty = Integer.parseInt(jTable2.getValueAt(x, 5).toString());

                                int finalQty = oldQty + Integer.valueOf(qty);
                                //Check Stock

                                if (availbleQty < finalQty) {
                                    JOptionPane.showMessageDialog(this, "Quantity out of stock", "Warning", JOptionPane.WARNING_MESSAGE);
                                } else {

                                    jTable2.setValueAt(String.valueOf(finalQty), x, 5);

                                    double UpdatedItemTotal = finalQty * Double.parseDouble(price);
                                    jTable2.setValueAt(String.valueOf(UpdatedItemTotal), x, 6);

                                    updateTotal();  //Total Value Update Method
                                }
                                //Check Stock

                            }

                            resetField();
                        } else {

                            Vector v = new Vector();
                            v.add(String.valueOf(proItemNo));
                            v.add(pName);
                            v.add(sid);
                            v.add(pNo);
                            v.add(price + "0");
                            v.add(qty);

                            double itemtotal = Integer.valueOf(qty) * Double.parseDouble(price);
                            v.add(df.format(itemtotal));

                            dtm.addRow(v);

                            updateTotal();  //Total Value Update Method

                            resetField();
                            JOptionPane.showMessageDialog(this, "Product added to Invoice", "Success", JOptionPane.INFORMATION_MESSAGE);
                            proItemNo = proItemNo + 1;
                            jTextField4.setEnabled(true);
//                
                        }

                        //add
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Ivalid Product No!", "Warning", JOptionPane.WARNING_MESSAGE);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
        // TODO add your handling code here:

        if (jTextField2.getText().equals("Enter Quantity")) {
            jTextField2.setText("");
            jTextField2.setForeground(Color.BLACK);

        }
        if (jTextField3.getText().equals("")) {
            jTextField3.setText("Product Number");
            jTextField3.setForeground(this.cl);

        }


    }//GEN-LAST:event_jTextField2FocusGained

    private void jTextField2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField2MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField2MouseClicked

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed
        // TODO add your handling code here:


    }//GEN-LAST:event_jTextField2KeyPressed

    private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField2FocusLost

    private void jTextField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusGained
        // TODO add your handling code here:

        if (jTextField2.getText().equals("")) {
            jTextField2.setText("Enter Quantity");
            jTextField2.setForeground(this.cl);

        }
        if (jTextField3.getText().equals("Product Number")) {
            jTextField3.setText("");
            jTextField3.setForeground(Color.BLACK);

        }
    }//GEN-LAST:event_jTextField3FocusGained

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            if (evt.getClickCount() == 2) {
                int r = jTable1.getSelectedRow();
                if (r == -1) {
                    JOptionPane.showMessageDialog(this, "Please Select a Product", "Warning", JOptionPane.WARNING_MESSAGE);

                } else {
                    String pNo = jTable1.getValueAt(selectedRow, 4).toString();
                    jTextField3.setForeground(Color.BLACK);
                    jTextField3.setText(pNo);
                    jTextField2.grabFocus();

                }

            }

        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        resetField();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:

        if (jComboBox1.getSelectedItem() == "Select") {
            jTextField5.setEditable(false);
            jTextField5.setText("");
            jLabel26.setText("0.00");
            jLabel26.setForeground(Color.BLACK);
        } else {
            jTextField5.setEditable(true);
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        // TODO add your handling code here:
        if (jTextField5.getText().isEmpty()) {
            jLabel26.setText("0.00");
            jLabel26.setForeground(Color.BLACK);
        } else {
            String total = jLabel23.getText();
            String payment = jTextField5.getText();

            double balance = Double.parseDouble(total) - Double.parseDouble(payment);

            if (balance > 0) {

                jLabel26.setForeground(Color.RED);

            } else {
                jLabel26.setForeground(Color.GREEN);

            }
            jLabel26.setText(String.valueOf(balance));
        }
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
        // TODO add your handling code here:
        String price = jTextField5.getText();

        String text = price + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][0-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField5KeyTyped

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        // TODO add your handling code here:
        String qty = jTextField2.getText();

        String text = qty + evt.getKeyChar();

        if (!Pattern.compile("[1-9][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:

        Payment = jTextField5.getText();
        PAYMENTTYPE = jComboBox1.getSelectedItem().toString();
        trowC = jTable2.getRowCount();
        if (trowC == 0) {
            JOptionPane.showMessageDialog(this, "Please add product to Invoice!", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (PAYMENTTYPE.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please Select the payment type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("([0-9]*)|(([1-9][0-9]*)[.]([0]*[1-9][0-9]*))|([0][.]([0]*[1-9][0-9]*))").matcher(Payment).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid Payment", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            long mTime = System.currentTimeMillis();

            UNIQUE_INVOICE_ID = mTime + "-" + SignIn.userId;

            //  String cid = jLabel5.getText();
            // System.out.println(uniqueId);
//
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dNow = sdf.format(new Date());

            //System.out.println(dNow);
            // System.out.println(sid);
//
            createCustomer();

            try {
                ResultSet rs0 = MySQL.search("SELECT * FROM `customer` WHERE `unique_id`='" + uniqueId2 + "'");

                if (rs0.next()) {
                    String cusId = rs0.getString("id");
                    MySQL.iud("INSERT INTO `invoice` (`customer_id`,`date_time`,`user_id`,`unique_id`)VALUES('" + Integer.parseInt(cusId) + "','" + dNow + "','" + SignIn.userId + "','" + UNIQUE_INVOICE_ID + "')");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                //Invoice Payment Insert
                ResultSet rs = MySQL.search("SELECT * FROM `invoice` WHERE `unique_id` = '" + UNIQUE_INVOICE_ID + "'");
                rs.next();
                String id = rs.getString("id");

                // System.out.println(id);
                ResultSet rs2 = MySQL.search("SELECT * FROM `payment_type` WHERE `name`='" + PAYMENTTYPE + "'");
                rs2.next();
                String paymentTypeId = rs2.getString("id");

                balance = jLabel26.getText();

                //  System.out.println(balance);
                //Invoice Payment Insert
                MySQL.iud("INSERT INTO `invoice_payment`(`invoice_id`,`payment_type_id`,`payment`,`balance`) VALUES('" + id + "','" + paymentTypeId + "','" + Payment + "','" + balance + "')");

                //Invoice Payment Insert
                //Invoice item insert and Stock  Update
                for (int i = 0; i < jTable2.getRowCount(); i++) {

                    String sid = jTable2.getValueAt(i, 2).toString();
                    String qty = jTable2.getValueAt(i, 5).toString();

                    ResultSet rs3 = MySQL.search("SELECT * FROM `stock` WHERE `stock`.`id` = '" + sid + "' ");
                    rs3.next();

                    String availableQty = rs3.getString("quantity");

                    int updatedQty = Integer.parseInt(availableQty) - Integer.parseInt(qty);

                    MySQL.iud("UPDATE `stock` SET `quantity`='" + updatedQty + "' WHERE `id`='" + sid + "'");

                    MySQL.iud("INSERT INTO `invoice_item`(`stock_id`,`qty`,`invoice_id`)VALUES('" + sid + "','" + qty + "','" + id + "')");

                }
                TotalPayment = jLabel23.getText();
                invoicePrint();
                resetField();

                //Supplier
                jLabel23.setText("0.00");
                jLabel26.setText("0.00");
                jComboBox1.setSelectedIndex(0);
                jTextField4.setText("");
                jTextField5.setText("");

                //Supplier
                DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
                dtm.setRowCount(0);
                JOptionPane.showMessageDialog(this, "New Invoice created", "Success", JOptionPane.INFORMATION_MESSAGE);
                //GRN item insert and Stock Insert or Update
                proItemNo = 0;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {

            setProduct();
            getFocus();

        }

    }//GEN-LAST:event_jTable1KeyPressed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {

            int r = jTable2.getSelectedRow();

            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Please Select a Invoice Item", "Warning", JOptionPane.WARNING_MESSAGE);

            } else {

                int option = JOptionPane.showConfirmDialog(this, "Do you want remove this item?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();

                if (option == JOptionPane.YES_OPTION) {
                    dtm.removeRow(r);
                    updateTotal();
                    JOptionPane.showMessageDialog(this, "Invoice item removed", "Success", JOptionPane.INFORMATION_MESSAGE);
                    jTextField2.setText("Enter Quantity");
                    jTextField2.setForeground(this.cl);

                    jTextField3.setText("Product Number");
                    jTextField3.setForeground(this.cl);
                    proItemNo = proItemNo - 1;

                }

            }
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
        // TODO add your handling code here:
        String price = jTextField4.getText();

        String text = price + evt.getKeyChar();
        Double totals = Double.parseDouble(jLabel23.getText());

        if (!Pattern.compile("(0|0[.]|0[.][0-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(text).matches()) {
            evt.consume();
        }


    }//GEN-LAST:event_jTextField4KeyTyped

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        // TODO add your handling code here:
        updateTotal();
    }//GEN-LAST:event_jTextField4KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}

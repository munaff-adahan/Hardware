/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.formdev.flatlaf.IntelliJTheme;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import model.MySQL;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author MUNAFF
 */
public class ManageStock extends javax.swing.JPanel {

    int rows = 0;

    /**
     * Creates new form ManageStock
     */
    public ManageStock() {

        initComponents();
        loadCategory();
        loadBrand();
        loadStock();
    }

    public void loadCategory() {

        try {
            ResultSet rs = MySQL.search("SELECT `name` FROM `category` ORDER BY `id` ASC");

            Vector v = new Vector();

            v.add("Select");
            while (rs.next()) {
                v.add(rs.getString("name"));

            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v);
            jComboBox1.setModel(dcm);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadBrand() {

        try {
            ResultSet rs = MySQL.search("SELECT `name` FROM `brand` ORDER BY `id` ASC");

            Vector v = new Vector();

            v.add("Select");
            while (rs.next()) {
                v.add(rs.getString("name"));

            }
            DefaultComboBoxModel dcm1 = new DefaultComboBoxModel(v);
            jComboBox2.setModel(dcm1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadStock() {
        try {
            ResultSet rs = MySQL.search("SELECT DISTINCT `stock`.`id`,`stock`.`product_id`, `stock`.`quantity`,`category`.`name`,`product`.`name`,`brand`.`name`,`stock`.`selling_price`,`grn_item`.`buying_price`,`stock`.`mfd`,`stock`.`exd`\n"
                    + "FROM `stock`\n"
                    + "INNER JOIN `grn_item` ON `grn_item`.`stock_id`=`stock`.`id`\n"
                    + "INNER JOIN `product` ON `stock`.`product_id`=`product`.`id`\n"
                    + "INNER JOIN `brand` ON `product`.`brand_id`= `brand`.`id`\n"
                    + "INNER JOIN `category` ON `product`.`category_id` = `category`.`id` WHERE `stock`.`quantity`<>'0'  ORDER BY `product`.`name` ASC");

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("stock.id"));
                v.add(rs.getString("stock.product_id"));
                v.add(rs.getString("category.name"));
                v.add(rs.getString("brand.name"));
                v.add(rs.getString("product.name"));
                v.add(rs.getString("stock.quantity"));
                v.add(rs.getString("grn_item.buying_price"));
                v.add(rs.getString("stock.selling_price"));
                v.add(rs.getString("stock.mfd"));
                v.add(rs.getString("stock.exd"));
                dtm.addRow(v);
            }
            jTable1.setModel(dtm);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void reset() {
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        jDateChooser3.setDate(null);
        jDateChooser4.setDate(null);
        jComboBox3.setSelectedIndex(0);
        jLabel2.setText("0.00");
        jTextField1.setText("");
        jTable1.clearSelection();
        loadStock();

    }

    public void searchStock() {
        String category = "";
        String brand = "";
        try {

            if (jComboBox1.getSelectedItem() != null) {
                category = jComboBox1.getSelectedItem().toString();

            } else {
                category = "";
            }

            if (jComboBox2.getSelectedItem() != null) {
                brand = jComboBox2.getSelectedItem().toString();

            } else {
                brand = "";
            }

//            String category = jComboBox1.getSelectedItem().toString();
//            
//            String brand = jComboBox2.getSelectedItem().toString();
            String name = jTextField2.getText().toUpperCase();
            String minPrice = jTextField3.getText();
            String maxPrice = jTextField4.getText();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String mfd_from = null;
            String mfd_to = null;
            String exd_from = null;
            String exd_to = null;

            if (jDateChooser1.getDate() != null) {
                mfd_from = sdf.format(jDateChooser1.getDate());
            }
            if (jDateChooser2.getDate() != null) {
                mfd_to = sdf.format(jDateChooser2.getDate());
            }
            if (jDateChooser4.getDate() != null) {
                exd_from = sdf.format(jDateChooser4.getDate());
            }
            if (jDateChooser3.getDate() != null) {
                exd_to = sdf.format(jDateChooser3.getDate());
            }
            int sort = jComboBox3.getSelectedIndex();

            Vector queryVector = new Vector();

            //CATEGORY
            if (category.equals("Select")) {

            } else {
                queryVector.add("`category`.`name` = '" + category + "'");

            }
            //BRAND
            if (brand.equals("Select")) {

            } else {
                queryVector.add("`brand`.`name` = '" + brand + "'");
            }
            //NAME
            if (name.isEmpty()) {

            } else {
                queryVector.add("`product`.`name` LIKE '%" + name + "%'");
            }
            //NAME ///

            //Selling_PRICE//
            if (!minPrice.isEmpty()) {

                if (!maxPrice.isEmpty()) {
                    queryVector.add("`stock`.`selling_price` >= '" + minPrice + "' AND `stock`.`selling_price`<='" + maxPrice + "'");
                } else {
                    queryVector.add("`stock`.`selling_price` >= '" + minPrice + "'");
                }
            } else {
                if (!maxPrice.isEmpty()) {
                    queryVector.add("`stock`.`selling_price` <= '" + maxPrice + "'");

                }

            }
            // //Selling_PRICE////

            //MFD_FROM//
            if (mfd_from != null) {

                if (mfd_to != null) {
                    queryVector.add("`stock`.`mfd` >= '" + mfd_from + "' AND `stock`.`mfd`<='" + mfd_to + "'");

                } else {
                    queryVector.add("`stock`.`mfd` >= '" + mfd_from + "'");
                }
            } else {
                if (mfd_to != null) {
                    queryVector.add("`stock`.`mfd` <= '" + mfd_to + "'");
                }

            }

            /// //MFD_FROM/////
            //EXD_search
            if (exd_from != null) {

                if (exd_to != null) {
                    queryVector.add("`stock`.`exd` >= '" + exd_from + "' AND `stock`.`exd`<='" + exd_to + "'");
                } else {
                    queryVector.add("`stock`.`exd` >= '" + exd_from + "'");
                }
            } else {
                if (exd_to != null) {
                    queryVector.add("`stock`.`exd` <= '" + exd_to + "'");
                }

            }

            //EXD_search
            String whereQuery = "";
            if (queryVector.size() >= 1) {
                whereQuery = "WHERE";
                for (int i = 0; i < queryVector.size(); i++) {
                    whereQuery += " ";
                    whereQuery += queryVector.get(i);
                    whereQuery += " ";
                    if (i != queryVector.size() - 1) {
                        whereQuery += " AND";
                    }

                }

            }

            // System.out.println(whereQuery);
            //order by query part
            String sortquery;

            if (sort == 0) {
                sortquery = "`product`.`name` ASC";
            } else if (sort == 1) {
                sortquery = "`product`.`name` DESC";
            } else if (sort == 2) {
                sortquery = "`stock`.`selling_price` ASC";
            } else if (sort == 3) {
                sortquery = "`stock`.`selling_price` DESC";
            } else if (sort == 4) {
                sortquery = "`stock`.`quantity` ASC";
            } else if (sort == 5) {
                sortquery = "`stock`.`quantity` DESC";
            } else if (sort == 6) {
                sortquery = "`stock`.`exd` ASC";
            } else {
                sortquery = "`stock`.`exd` DESC";
            }

            ResultSet rs = MySQL.search("SELECT DISTINCT `stock`.`id`,`stock`.`product_id`, `stock`.`quantity`,`category`.`name`,`product`.`name`,`brand`.`name`,`stock`.`selling_price`,`grn_item`.`buying_price`,`stock`.`mfd`,`stock`.`exd`\n"
                    + "FROM `stock`\n"
                    + "INNER JOIN `grn_item` ON `grn_item`.`stock_id`=`stock`.`id`\n"
                    + "INNER JOIN `product` ON `stock`.`product_id`=`product`.`id`\n"
                    + "INNER JOIN `brand` ON `product`.`brand_id`= `brand`.`id`\n"
                    + "INNER JOIN `category` ON `product`.`category_id` = `category`.`id` " + whereQuery + "   ORDER BY " + sortquery + " ");

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("stock.id"));
                v.add(rs.getString("stock.product_id"));
                v.add(rs.getString("category.name"));
                v.add(rs.getString("brand.name"));
                v.add(rs.getString("product.name"));
                v.add(rs.getString("stock.quantity"));
                v.add(rs.getString("grn_item.buying_price"));
                v.add(rs.getString("stock.selling_price"));
                v.add(rs.getString("stock.mfd"));
                v.add(rs.getString("stock.exd"));
                dtm.addRow(v);
            }
            // jTable1.setModel(dtm);
            
        } catch (Exception e) {
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

        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jTextField3 = new javax.swing.JTextField();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator12 = new javax.swing.JSeparator();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jButton2 = new javax.swing.JButton();
        jTextField4 = new javax.swing.JTextField();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1600, 850));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jComboBox1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jComboBox1KeyReleased(evt);
            }
        });
        add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 150, -1));

        jComboBox2.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        jComboBox2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jComboBox2KeyReleased(evt);
            }
        });
        add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 30, 138, -1));

        jTextField2.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });
        add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 30, 318, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock Id", "Product Id", "Category", "Brand", "Name", "Quantity", "Buying Price", "Selling Price", "MFD", "EXD"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setPreferredSize(new java.awt.Dimension(1590, 500));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 257, 1420, 420));

        jPanel1.setPreferredSize(new java.awt.Dimension(1600, 850));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel15.setText("Category");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        jLabel16.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel16.setText("Brand");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, -1, -1));

        jLabel17.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel17.setText("Name");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 30, -1, -1));

        jLabel18.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel18.setText("Min");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, -1, -1));

        jLabel19.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel19.setText("Selling Price");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jLabel20.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel20.setText("Max");
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, -1, -1));

        jLabel21.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("MFD");
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 80, -1, -1));

        jLabel22.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel22.setText("TO");
        jPanel1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 80, -1, -1));

        jLabel23.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel23.setText("Sort Product By ");
        jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, -1, -1));

        jLabel24.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("EXD");
        jPanel1.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 120, -1, -1));

        jLabel25.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel25.setText("TO");
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 120, -1, -1));

        jSeparator9.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator9.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 10, 20, 50));

        jSeparator11.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator11.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 310, 30));

        jSeparator6.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator6.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 160, 620, 30));

        jSeparator3.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 20, 50));

        jSeparator5.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 80, 30, 70));

        jDateChooser4.setDateFormatString("yyyy-MM-dd");
        jDateChooser4.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jDateChooser4.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser4PropertyChange(evt);
            }
        });
        jPanel1.add(jDateChooser4, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 120, 130, -1));

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jDateChooser1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDateChooser1MouseClicked(evt);
            }
        });
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });
        jPanel1.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, 130, -1));

        jDateChooser2.setDateFormatString("yyyy-MM-dd");
        jDateChooser2.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });
        jPanel1.add(jDateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 80, 140, -1));

        jLabel1.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel1.setText("Buyying Price");

        jLabel3.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel3.setText("New Price ");

        jTextField1.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jButton1.setText("Update Stock");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel2.setText("0.00");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 20, 440, 210));

        jSeparator7.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator7.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 310, 30));

        jTextField3.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });
        jPanel1.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, 110, -1));

        jSeparator8.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator8.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 70, 310, 30));

        jSeparator12.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator12.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 70, 310, 30));

        jDateChooser3.setDateFormatString("yyyy-MM-dd");
        jDateChooser3.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jDateChooser3.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser3PropertyChange(evt);
            }
        });
        jPanel1.add(jDateChooser3, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 120, 140, -1));

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 5)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/reset.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 80, -1, -1));

        jTextField4.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });
        jPanel1.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, 110, -1));

        jComboBox3.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Name ASC", "Name DESC", "Price ASC", "Price DESC", "Quantity  ASC", "Quantity DESC", "EXD ASC", "EXD DESC" }));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 180, 200, -1));

        jButton5.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/printer_icon.png"))); // NOI18N
        jButton5.setText("Print Product List");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 700, 290, -1));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        searchStock();

    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1KeyReleased

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox2KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2KeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        // TODO add your handling code here:
        String price = jTextField3.getText();

        String text = price + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][0-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField3KeyTyped

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
        // TODO add your handling code here:
        String price = jTextField4.getText();

        String text = price + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][0-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField4KeyTyped

    private void jDateChooser1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateChooser1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser1MouseClicked

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jDateChooser4PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser4PropertyChange
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jDateChooser4PropertyChange

    private void jDateChooser3PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser3PropertyChange
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jDateChooser3PropertyChange

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {

            String buyingPrice = jTable1.getValueAt(selectedRow, 6).toString();
            jLabel2.setText(buyingPrice);

        }
        //set To Invoice

//        if (evt.getClickCount() == 2) {
//            int r = jTable1.getSelectedRow();
//            if (r == -1) {
//                JOptionPane.showMessageDialog(this, "Please Select a Product", "Warning", JOptionPane.WARNING_MESSAGE);
//
//            } else {
//                if (i != null) {
//
//                    i.jLabel29.setText(jTable1.getValueAt(selectedRow, 0).toString());
//                    i.jLabel13.setText(jTable1.getValueAt(selectedRow, 1).toString());
//                    i.jLabel12.setText(jTable1.getValueAt(selectedRow, 4).toString());
//                    i.jLabel16.setText(jTable1.getValueAt(selectedRow, 3).toString());
//                    i.jLabel19.setText(jTable1.getValueAt(selectedRow, 2).toString());
//                    i.jLabel31.setText(jTable1.getValueAt(selectedRow, 8).toString());
//                    i.jLabel30.setText(jTable1.getValueAt(selectedRow, 7).toString());
//                    i.jLabel32.setText(jTable1.getValueAt(selectedRow, 9).toString());
//                    this.dispose();
//
//                }
//
//            }
//
//        }
        //set To Invoice
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String buyingPrice = jLabel2.getText();
        String newPrice = jTextField1.getText();
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please Select a stock !", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("(0|0[.]|0[.][0-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(newPrice).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid Selling Price !", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            String stock_id = jTable1.getValueAt(selectedRow, 0).toString();
            if (Double.parseDouble(newPrice) <= Double.parseDouble(buyingPrice)) {
                int option = JOptionPane.showConfirmDialog(this, "new Price <=  buyingPrice. Do you want Continue?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    MySQL.iud("UPDATE `stock` SET `selling_price` = '" + newPrice + "' WHERE `id`='" + stock_id + "'");
                }
            } else {
                MySQL.iud("UPDATE `stock` SET `selling_price` = '" + newPrice + "' WHERE `id`='" + stock_id + "'");
            }
            reset();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        // TODO add your handling code here:
        String price = jTextField1.getText();

        String text = price + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][0-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  | HH:mm:ss");
        Date dToday1 = new Date();
        String dateNowN1 = sdf.format(dToday1);
        

        try {
            TableModel tm = jTable1.getModel();
            int r = tm.getRowCount();
            String row = new String(String.valueOf(r));
            String reportUrl = "/reports/productList.jasper";
            InputStream reportFile = null;
            reportFile = getClass().getResourceAsStream(reportUrl);
            HashMap parameters = new HashMap();
            parameters.put("Parameter1", row);
            parameters.put("Parameter2", dateNowN1);

           
            JRTableModelDataSource dataSource = new JRTableModelDataSource(tm);

            JasperPrint print = JasperFillManager.fillReport(reportFile, parameters, dataSource);
            JasperViewer Jviewer = new JasperViewer(print, false);
            Jviewer.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}

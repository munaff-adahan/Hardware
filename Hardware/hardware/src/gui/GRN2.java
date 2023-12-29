/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import model.MySQL;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author MUNAFF
 */
public class GRN2 extends javax.swing.JPanel {

    public static int itemNo = 0;

    DecimalFormat df = new DecimalFormat("0.00");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public int tRowCount = 0;

    /**
     * Creates new form GRN2
     */
    public GRN2() {
        initComponents();
        loadPaymenyType();
    }

    public void resetFields() {
        jLabel12.setText("None");
        jLabel13.setText("None");
        jLabel16.setText("None");
        jLabel19.setText("None");
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField4.setText("");
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);

    }

    public void grnPrint() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  | HH:mm:ss");
        Date dToday1 = new Date();
        String dateNowN1 = sdf.format(dToday1);

        String supID = jLabel5.getText();

        String CashierName = null;
        String total = jLabel21.getText();
        String supplierName = "";
        String supMobile = "";
        String supEmail = "";
        String supCompanyName = "";
        String branchName = "";
        String branchContact = "";
        String BranchAddress = "";
        String grandTotal = jLabel23.getText();
        String paymentMethod = jComboBox1.getSelectedItem().toString();
        String rows = String.valueOf(tRowCount);

        String balance = "";
        if (jLabel26.getForeground().equals(Color.GREEN)) {
            balance = "+" + jLabel26.getText();

        } else if (jLabel26.getForeground().equals(Color.RED)) {
            balance = "-" + jLabel26.getText();

        } else {

            balance = jLabel26.getText();
        }

        String Payment = jTextField3.getText();

        try {

            ResultSet rs = MySQL.search("SELECT *\n"
                    + "FROM `supplier`\n"
                    + "INNER JOIN `company_branch` ON `supplier`.`company_branch_id`=`company_branch`.`id`\n"
                    + "INNER JOIN `company_branch_address` ON `company_branch`.`company_branch_address_id` = `company_branch_address`.`id`\n"
                    + "INNER JOIN `company` ON `company_branch`.`company_id` = `company`.`id` INNER JOIN `city` ON `city`.`id`= `company_branch_address`.`city_id` WHERE `supplier`.`id`='" + supID + "'");
            ResultSet rs1 = MySQL.search("SELECT * FROM `user` WHERE `id`='" + SignIn.userId + "'");
            if (rs.next()) {
                supplierName = rs.getString("supplier.name");
                supMobile = rs.getString("supplier.contact_number");
                supEmail = rs.getString("supplier.email");
                supCompanyName = rs.getString("company.name");
                branchName = rs.getString("company_branch.name");
                branchContact = rs.getString("company_branch.branch_contact_number");
                BranchAddress = rs.getString("company_branch_address.line1") + "," + rs.getString("company_branch_address.line2") + "," + rs.getString("city.name");

            }
            if (rs1.next()) {
                CashierName = rs1.getString("name");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String reportUrl = "/reports/grn2.jasper";
            InputStream reportFile = null;
            reportFile = getClass().getResourceAsStream(reportUrl);
            HashMap parameters = new HashMap();
            parameters.put("Parameter1", supplierName);
            parameters.put("Parameter2", supMobile);
            parameters.put("Parameter3", supEmail);
            parameters.put("Parameter4", supCompanyName);
            parameters.put("Parameter5", branchName);
            parameters.put("Parameter6", branchContact);
            parameters.put("Parameter7", BranchAddress);
            parameters.put("Parameter8", dateNowN1);
            parameters.put("Parameter9", Payment + ".00");
            parameters.put("Parameter10", paymentMethod);
            parameters.put("Parameter11", balance + "0");
            parameters.put("Parameter12", grandTotal);
            parameters.put("Parameter13", CashierName);
            parameters.put("Parameter14", rows);

            TableModel tm = jTable1.getModel();

            JRTableModelDataSource dataSource = new JRTableModelDataSource(tm);

            JasperPrint print = JasperFillManager.fillReport(reportFile, parameters, dataSource);
            JasperViewer Jviewer = new JasperViewer(print, false);
            Jviewer.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void resetField() {

        jLabel12.setText("None");
        jLabel13.setText("None");
        jLabel16.setText("None");
        jLabel19.setText("None");

        jTextField1.setText("");
        jTextField2.setText("");
        jTextField4.setText("");
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        jTextField1.grabFocus();

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

        for (int i = 0; i < jTable1.getRowCount(); i++) {
            String t = jTable1.getValueAt(i, 10).toString();
            total = total + Double.parseDouble(t);

        }
        jLabel23.setText(df.format(total));

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
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel27 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel18 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1600, 850));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Category", "Product Id", "Brand", "Name", "Quantity", "Buying Price", "Selling Price", "MFD", "EXD", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setPreferredSize(new java.awt.Dimension(1600, 400));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(51, 279, 1480, 230));

        jLabel21.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Total Payment");
        add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 610, -1, -1));

        jLabel23.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("0.00");
        add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 610, -1, -1));

        jLabel24.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Payment");
        add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 610, -1, -1));

        jTextField3.setEditable(false);
        jTextField3.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });
        add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 610, 300, -1));

        jLabel26.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("0.00");
        add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 680, 222, -1));

        jLabel25.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Balance");
        add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 680, -1, -1));

        jComboBox1.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 680, 230, -1));

        jLabel22.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Payment Method");
        add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 680, -1, -1));

        jButton4.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/printer_icon.png"))); // NOI18N
        jButton4.setText("Print GRN");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 530, 450, 200));

        jButton1.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/suppliers.png"))); // NOI18N
        jButton1.setText("Select Supplier");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 310, 42));

        jLabel1.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Id");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 30, -1));

        jLabel5.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("None");
        jLabel5.setPreferredSize(new java.awt.Dimension(50, 25));
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, 140, -1));

        jLabel2.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Name");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, -1, -1));

        jLabel3.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Contact Number");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        jLabel4.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Branch");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, -1, -1));

        jLabel6.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("None");
        jLabel6.setPreferredSize(new java.awt.Dimension(50, 25));
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 120, 140, -1));

        jLabel7.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("None");
        jLabel7.setPreferredSize(new java.awt.Dimension(50, 25));
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 160, 140, -1));

        jLabel8.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("None");
        jLabel8.setPreferredSize(new java.awt.Dimension(50, 25));
        add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 210, 140, -1));

        jButton2.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/products.png"))); // NOI18N
        jButton2.setText("Select Product");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 20, 1030, 50));

        jLabel9.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Id");
        add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 80, 34, 23));

        jLabel13.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("None");
        add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 80, 150, -1));

        jLabel15.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Brand");
        add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 130, -1, -1));

        jLabel16.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("None");
        add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 130, 230, -1));

        jLabel17.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Quantity");
        add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 180, -1, -1));

        jTextField1.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });
        add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 180, 260, -1));

        jLabel10.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Name");
        add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 80, -1, -1));

        jLabel12.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("None");
        add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 80, 250, -1));

        jLabel11.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Category");
        add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 130, 123, -1));

        jLabel19.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("None");
        add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 130, 150, -1));

        jLabel20.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("MFD");
        add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 230, 98, -1));

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jDateChooser1.setMinSelectableDate(new java.util.Date(-62135785727000L));
        add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 230, 220, 29));

        jLabel27.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("EXD");
        add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(1270, 230, -1, -1));

        jDateChooser2.setDateFormatString("yyyy-MM-dd");
        jDateChooser2.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        add(jDateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1320, 230, 220, 30));

        jLabel18.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Buyying Price");
        add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 180, -1, -1));

        jTextField2.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });
        add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 180, 198, -1));

        jLabel14.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 18)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Selling Price");
        add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 230, -1, -1));

        jTextField4.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });
        add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 230, 198, -1));

        jButton3.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add.png"))); // NOI18N
        jButton3.setText("Add To GRN");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1390, 80, 170, 130));

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/resets.png"))); // NOI18N
        jButton5.setContentAreaFilled(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1280, 80, 100, 90));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 20, 50, 240));
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {

            jButton1.setEnabled(true);
            jButton1.setText("Select Supplier");

            jLabel5.setText("None");
            jLabel6.setText("None");
            jLabel7.setText("None");
            jLabel8.setText("None");
        }
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        SupplierRegistration sr = new SupplierRegistration(this);
        sr.setVisible(true);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        ProductRegistraion pr = new ProductRegistraion(this);
        pr.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:

        String sid = jLabel5.getText();
        String pid = jLabel13.getText();
        String qty = jTextField1.getText();
        String buyingPrice = jTextField2.getText();

        //Update
        String sellingPrice = jTextField4.getText();
        Date mfd = jDateChooser1.getDate();
        Date exd = jDateChooser2.getDate();
        //Update

        if (sid.equals("None")) {
            JOptionPane.showMessageDialog(this, "Please select supplier", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (pid.equals("None")) {
            JOptionPane.showMessageDialog(this, "Please select product", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("[1-9][0-9]*").matcher(qty).matches()) {
            JOptionPane.showMessageDialog(this, "Enter Valid Quantity", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("([1-9][0-9]*)|(([1-9][0-9]*)[.]([0]*[1-9][0-9]*))|([0][.]([0]*[1-9][0-9]*))").matcher(buyingPrice).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid buying Price", "Warning", JOptionPane.WARNING_MESSAGE);
        } //Update
        else if (!Pattern.compile("([1-9][0-9]*)|(([1-9][0-9]*)[.]([0]*[1-9][0-9]*))|([0][.]([0]*[1-9][0-9]*))").matcher(sellingPrice).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid selling Price", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (Double.parseDouble(buyingPrice) >= Double.parseDouble(sellingPrice)) {
            JOptionPane.showMessageDialog(this, "Invalid buyying and selling price", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (mfd == null) {
            JOptionPane.showMessageDialog(this, "Invalid MFD", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (mfd.after(new Date())) {
            JOptionPane.showMessageDialog(this, "Invalid MFD", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (exd == null) {
            JOptionPane.showMessageDialog(this, "Invalid EXD", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (exd.before(new Date())) {
            JOptionPane.showMessageDialog(this, "Invalid EXD", "Warning", JOptionPane.WARNING_MESSAGE);

        } //Update
        else {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();

            boolean isFound = false;
            int x = -1;

            for (int i = 0; i < dtm.getRowCount(); i++) {
                String id = jTable1.getValueAt(i, 2).toString();

                if (id.equals(pid)) {
                    isFound = true;
                    x = i;
                    break;

                }

            }

            if (isFound) {
                int option = JOptionPane.showConfirmDialog(this, "This product is already added.Do you want update?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {

                    int oldQty = Integer.parseInt(jTable1.getValueAt(x, 5).toString());

                    int finalQty = oldQty + Integer.parseInt(qty);

                    jTable1.setValueAt(String.valueOf(finalQty), x, 5);
                    jTable1.setValueAt(buyingPrice, x, 6);
                    jTable1.setValueAt(sellingPrice, x, 7);

                    double UpdatedItemTotal = finalQty * Double.parseDouble(buyingPrice);
                    jTable1.setValueAt(String.valueOf(UpdatedItemTotal), x, 10);

                    updateTotal();  //Total Value Update Method
                }

                resetField();
            } else {
                itemNo = itemNo + 1;
                Vector v = new Vector();
                v.add(String.valueOf(itemNo));

                v.add(jLabel19.getText());
                v.add(pid);
                v.add(jLabel16.getText());
                v.add(jLabel12.getText());
                v.add(qty);
                v.add(buyingPrice + ".00");
                v.add(sellingPrice + ".00");
                v.add(sdf.format(mfd));
                v.add(sdf.format(exd));

                double itemtotal = Integer.parseInt(qty) * Double.parseDouble(buyingPrice);
                v.add(df.format(itemtotal));

                dtm.addRow(v);

                updateTotal();  //Total Value Update Method

                resetField();

                //JOptionPane.showMessageDialog(this, "Product added to GRN", "Success", JOptionPane.INFORMATION_MESSAGE);
                //                if (jTable1.getRowCount() == 0) {
                //                    jTextField3.setEnabled(false);
                //                } else {
                //                    jTextField3.setEnabled(true);
                //
                //                }
            }

        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        // TODO add your handling code here:
        String qty = jTextField1.getText();

        String text = qty + evt.getKeyChar();

        if (!Pattern.compile("[1-9][0-9]*").matcher(text).matches()) {
            evt.consume();
        }

    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        // TODO add your handling code here:
        String price = jTextField2.getText();

        String text = price + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][0-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
        // TODO add your handling code here:
        String price = jTextField4.getText();

        String text = price + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][0-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField4KeyTyped

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {

            int r = jTable1.getSelectedRow();

            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Please Select a GRN Item", "Warning", JOptionPane.WARNING_MESSAGE);

            } else {

                int option = JOptionPane.showConfirmDialog(this, "Do you want remove this product?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();

                if (option == JOptionPane.YES_OPTION) {
                    dtm.removeRow(r);
                    updateTotal();
                    JOptionPane.showMessageDialog(this, "GRN item removed", "Success", JOptionPane.INFORMATION_MESSAGE);
                    jTextField3.setText("");
                    jLabel23.setText("0.00");
                    jLabel26.setText("0.00");
                    jComboBox1.setSelectedIndex(0);
                    itemNo = itemNo - 1;

                }

            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // TODO add your handling code here:
        if (jTextField3.getText().isEmpty()) {
            jLabel26.setText("0.00");
            jLabel26.setForeground(Color.BLACK);
        } else {
            String total = jLabel23.getText();
            String payment = jTextField3.getText();

            double balance = Double.parseDouble(total) - Double.parseDouble(payment);

            if (balance > 0) {

                jLabel26.setForeground(Color.RED);

            } else {
                jLabel26.setForeground(Color.GREEN);

            }
            jLabel26.setText(String.valueOf(balance));
        }
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        // TODO add your handling code here:
        String price = jTextField3.getText();

        String text = price + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][0-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField3KeyTyped

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:

        if (jComboBox1.getSelectedItem() == "Select") {
            jTextField3.setEditable(false);
            jTextField3.setText("");
            jLabel26.setText("0.00");
            jLabel26.setForeground(Color.BLACK);
        } else {
            jTextField3.setEditable(true);
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:

        String payment = jTextField3.getText();
        String paymenttype = jComboBox1.getSelectedItem().toString();
        tRowCount = jTable1.getRowCount();
        if (tRowCount == 0) {
            JOptionPane.showMessageDialog(this, "Please add product to GRN table!", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (paymenttype.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please Select the payment type", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (!Pattern.compile("([0-9]*)|(([1-9][0-9]*)[.]([0]*[1-9][0-9]*))|([0][.]([0]*[1-9][0-9]*))").matcher(payment).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid Payment", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            long mTime = System.currentTimeMillis();

            String sid = jLabel5.getText();

            String uniqueId = mTime + "-" + SignIn.userId;
            // System.out.println(uniqueId);
            //
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dNow = sdf.format(new Date());

            //System.out.println(dNow);
            // System.out.println(sid);
            //
            MySQL.iud("INSERT INTO `grn` (`supplier_id`,`date_time`,`user_id`,`unique_id`)VALUES('" + sid + "','" + dNow + "','" + SignIn.userId + "','" + uniqueId + "')");
            try {

                //Grn Payment Insert
                ResultSet rs = MySQL.search("SELECT * FROM `grn` WHERE `unique_id` = '" + uniqueId + "'");
                rs.next();
                String id = rs.getString("id");

                // System.out.println(id);
                ResultSet rs2 = MySQL.search("SELECT * FROM `payment_type` WHERE `name`='" + paymenttype + "'");
                rs2.next();
                String paymentTypeId = rs2.getString("id");

                //  System.out.println(paymentTypeId);
                //
                String balance = jLabel26.getText();

                //  System.out.println(balance);
                MySQL.iud("INSERT INTO `grn_payment`(`grn_id`,`payment_type_id`,`payment`,`balance`) VALUES('" + id + "','" + paymentTypeId + "','" + payment + "','" + balance + "')");
                //                    jLabel23.setText("0.00");
                //                    jComboBox1.setSelectedItem("Select");
                //Grn Payment Insert
                //GRN item insert and Stock Insert or Update

                for (int i = 0; i < jTable1.getRowCount(); i++) {

                    String pid = jTable1.getValueAt(i, 2).toString();
                    String qty = jTable1.getValueAt(i, 5).toString();
                    String sellingPrice = jTable1.getValueAt(i, 7).toString();
                    String buyyingPrice = jTable1.getValueAt(i, 6).toString();
                    String mfd = jTable1.getValueAt(i, 8).toString();
                    String exd = jTable1.getValueAt(i, 9).toString();
                    //grn_id = id
                    //stock_id = ?

                    ResultSet rs3 = MySQL.search("SELECT * FROM `stock` WHERE `product_id`='" + pid + "' AND `selling_price`='" + sellingPrice + "' AND `mfd`='" + mfd + "' AND `exd`='" + exd + "'");

                    String stock_id;
                    if (rs3.next()) {
                        //Update
                        stock_id = rs3.getString("id");
                        String stock_qty = rs3.getString("quantity");

                        int updatedQty = Integer.parseInt(stock_qty) + Integer.parseInt(qty);

                        MySQL.iud("UPDATE `stock` SET `quantity`='" + updatedQty + "' WHERE `id`='" + stock_id + "'");

                    } else {
                        //Insert
                        MySQL.iud("INSERT INTO `stock` (`product_id`,`quantity`,`selling_price`,`mfd`,`exd`) VALUES ('" + pid + "','" + qty + "','" + sellingPrice + "','" + mfd + "','" + exd + "')");
                        ResultSet rs4 = MySQL.search("SELECT * FROM `stock` WHERE `product_id`='" + pid + "' AND `selling_price`='" + sellingPrice + "' AND `mfd`='" + mfd + "' AND `exd`='" + exd + "'");
                        rs4.next();
                        stock_id = rs4.getString("id");

                    }
                    MySQL.iud("INSERT INTO `grn_item`(`quantity`,`buying_price`,`grn_id`,`stock_id`)VALUES('" + qty + "','" + buyyingPrice + "','" + id + "','" + stock_id + "')");

                }
                grnPrint();
                resetField();
                //Supplier
                jButton1.setEnabled(true);
                jButton1.setText("Select Supplier");
                jLabel5.setText("None");
                jLabel6.setText("None");
                jLabel7.setText("None");
                jLabel8.setText("None");

                jLabel23.setText("0.00");
                jLabel26.setText("0.00");
                jComboBox1.setSelectedIndex(0);
                jTextField3.setText("");

                //Supplier
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);
                JOptionPane.showMessageDialog(this, "New GRN created", "Success", JOptionPane.INFORMATION_MESSAGE);
                itemNo = 0;
                //GRN item insert and Stock Insert or Update

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        resetFields();
    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    public javax.swing.JLabel jLabel12;
    public javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    public javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    public javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    public javax.swing.JLabel jLabel5;
    public javax.swing.JLabel jLabel6;
    public javax.swing.JLabel jLabel7;
    public javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    public javax.swing.JTextField jTextField1;
    public javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables

}

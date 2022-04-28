/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frame;

import Database.DBConnection;
import Database.DBController;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.DeleteResult;
import model.FillTable;
import model.UpdateResult;
import model.User;
import model.Wrong;

import java.sql.*;
import jdk.internal.icu.util.CodePointMap;

public class MainForm extends javax.swing.JFrame {
   
    UpdateResult updateresult;

    FillTable filltable; 
  
    User user;
       
    Wrong wrong;

    DeleteResult deleteresult;
    
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private DefaultTableModel model;
    
    
    DBConnection koneksi = new DBConnection();
    
    
    
    private int saldo = 0;
    private int tempAmount = 0;
    private int id = 0;
    private int selectedId = 0;
    private String tempComboBox = null;
    
    public MainForm() {
        initComponents();
        this.initTable();
        
        this.user = new User();
        this.wrong = new Wrong();
        this.deleteresult = new DeleteResult();
        this.updateresult = new UpdateResult();
        this.filltable = new FillTable();
        
        btnDelete.setVisible(false);
//        cbCategory.setEnabled(false);
//        jaku.setEnabled(false);
//        this.testComboBox();
        this.testComboBoxx();
        this.testcomboboxxx();
    }
    
    //function for filter the table data (income or spending)
    private void filter(String query) {
         TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);
         table.setRowSorter(tr);

         if(query != "All") {
            tr.setRowFilter(RowFilter.regexFilter(query));
         }  else {
            table.setRowSorter(tr);
         }
    }
   
    //function to set current saldo
    public void setSaldo(){
        txtSaldo.setText(String.valueOf(this.saldo));
    }
    
    //function to reset the variable and textfield
    public void resetForm(){
        this.selectedId = 0;
        
        txtDate.setDate(null);
        txtAmount.setText("");
        txtDesc.setText("");
        
        btnDelete.setVisible(false);
//        cbCategory.setEnabled(false);
    }
    
    //function ro reset temporary variable
    public void resetTemp(){
        this.tempAmount = 0;
        this.tempComboBox = null;
    }
    
    public void store(){
       
        if(txtDate.getDate() == null || txtAmount.getText().equals("") || 
            txtDesc.getText().equals("")){
            this.wrong.cantNull();
        }else if(txtAmount.getText().equals("0")){
            this.wrong.amountEqualZero();
            txtAmount.setText("");
        }else{
            String dateInput = sdf.format(txtDate.getDate());
            String date = String.valueOf(Date.valueOf(dateInput));
            
            int amount = Integer.valueOf(txtAmount.getText());
            String desc = txtDesc.getText();
            String type = cbType.getSelectedItem().toString();
            
            //store data if combobox type equals to income
            if("Income".equals(cbType.getSelectedItem().toString())){
                this.id += 1;
                String idTable = String.valueOf(this.id);

                Object[] obj = new Object[]{
                    idTable,
                    date,
                    amount,
                    desc,
                    type
                };
                
                //update the current saldo
                this.saldo += amount;
                setSaldo();
                this.model.addRow(obj);
                //show success msg
                this.filltable.Good();    
                
            //store data if combobox type equals to spending
            }else{
                
                if(amount > this.saldo){
                    this.wrong.amountMoreThanSaldo();
                }else{
                    this.id += 1;
                    
                    String idTable = String.valueOf(this.id);

                    Object[] obj = new Object[]{
                        idTable,
                        date,
                        amount,
                        desc,
                        type
                    };
                    
                    //update the current saldo
                    this.saldo -= amount;
                    setSaldo();
                    this.model.addRow(obj);
                    //show success msg
                    this.filltable.Good();    
                }
            }
        }
    }
    
    private String getSelectedRow() {
        int selectedRowIndex = table.getSelectedRow();
        
        Object selectedObject = (Object) table.getModel().getValueAt(
            selectedRowIndex, 0
        );
         
        return String.valueOf(selectedObject);    
    }    
    
    private void update(){
        String row = this.getSelectedRow();
        
        if(txtDate.getDate() == null || txtAmount.getText().equals("") || 
           txtDesc.getText().equals("")){
            this.wrong.cantNull();
        }else if(txtAmount.getText().equals("0")){
            this.wrong.amountEqualZero();
            txtAmount.setText("");
        }
        
        else{
            String dateInput = sdf.format(txtDate.getDate());
            String date = String.valueOf(Date.valueOf(dateInput));
            
            int amount = Integer.valueOf(txtAmount.getText());
            String desc = txtDesc.getText();
            String combo = cbType.getSelectedItem().toString();
           
            if("Spending".equals(combo)){
                if(amount > this.saldo){
                    this.wrong.amountMoreThanSaldo();
                }
            }
            
            for (int i = 0; model.getRowCount() > i; i++) {
            String id = (String) model.getValueAt(i, 0); 

                if (id.equals(row)) {
                    model.setValueAt(date, i, 1); 
                    model.setValueAt(amount, i, 2); 
                    model.setValueAt(desc, i, 3); 
                    model.setValueAt(combo, i, 4);
                    break;
                }
            }
                
            //function for updating the current saldo
            if(combo.equals("Income")){
                this.saldo -= this.tempAmount;
                this.saldo += amount;
                this.setSaldo();
            }else{
                this.saldo += this.tempAmount;
                this.saldo -= amount;
                this.setSaldo();
            }

            this.updateresult.Good();
            resetTemp();
        }
    }
    
    private void initTable(){
        model = new DefaultTableModel();
        table.setModel(model);
        
        model.addColumn("Id");
        model.addColumn("Date");
        model.addColumn("Amount (Rp)");
        model.addColumn("Description");
        model.addColumn("Type");
        
        btnDelete.setVisible(false);
    }
    
/*    public void connect() {
        String sql;
        Connection con = koneksi.open();
        PreparedStatement ps = con.prepareStatement(sql);
    }
*/    

    public void testComboBoxx() {
        try {
            Connection con = koneksi.open();
            String query = "SELECT DISTINCT type FROM categories";
//            String query = "SELECT type FROM categories WHERE type = Income";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String type = rs.getString("type");
                System.out.println(type);
                cbCategory.addItem(type);
            }
        } catch (Exception e) {
        }
    }
    
    public void testcomboboxxx() {
        String category = cbCategory.getSelectedItem().toString();
        
        if(category == "Income") {
            try {
            Connection con = koneksi.open();
            String query = "SELECT name FROM categories WHERE type = 'Income'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String type = rs.getString("name");
                cbTipe.addItem(type);
            }
        } catch (Exception e) {}
        }else{
            try {
            Connection con = koneksi.open();
            String query = "SELECT name FROM categories WHERE type = 'Spending'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String type = rs.getString("name");
                cbTipe.addItem(type);
            }
        } catch (Exception e) {
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

        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        btnLogout = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbType = new javax.swing.JComboBox<>();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        cbTable = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtDesc = new javax.swing.JTextField();
        txtSaldo = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        JLabel = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();
        txtDate = new com.toedter.calendar.JDateChooser();
        jaku = new javax.swing.JLabel();
        cbCategory = new javax.swing.JComboBox<>();
        cbTipe = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("TOTAL SALDO (Rp) :");

        jLabel2.setText("PERSONAL FINANCE RECORDS APPLICATION");

        btnLogout.setText("LOGOUT");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        jLabel3.setText("DATE :");

        jLabel4.setText("AMOUNT (Rp) :");

        txtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAmountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAmountKeyReleased(evt);
            }
        });

        jLabel6.setText("TYPE :");

        cbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Income", "Spending" }));

        btnSave.setText("SAVE");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnDelete.setText("DELETE");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tableMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        cbTable.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Income", "Spending" }));
        cbTable.setToolTipText("");
        cbTable.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbTableItemStateChanged(evt);
            }
        });
        cbTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTableActionPerformed(evt);
            }
        });

        jLabel7.setText("DESCRIPTION :");

        txtDesc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDescKeyReleased(evt);
            }
        });

        txtSaldo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtSaldo.setText("0");

        btnReset.setText("RESET");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        jaku.setText("CATEGORIES : ");

        cbCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "tst" }));

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbTable, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addGap(87, 87, 87)
                                .addComponent(btnLogout))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel1)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(txtSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabel7)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                                                    .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jLabel3))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                                                        .addComponent(txtDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                            .addGap(18, 18, 18)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabel6)
                                                    .addGap(18, 18, 18)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(cbType, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout.createSequentialGroup()
                                                            .addComponent(btnSave)
                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(btnReset)
                                                            .addGap(75, 75, 75))))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jaku)
                                                    .addGap(18, 18, 18)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(cbTipe, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(cbCategory, 0, 152, Short.MAX_VALUE))))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(59, 59, 59)
                                        .addComponent(JLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnDelete))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(79, 79, 79)
                                                .addComponent(jButton1)
                                                .addGap(0, 0, Short.MAX_VALUE)))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(btnLogout))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSaldo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(24, 24, 24)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(jLabel6)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(cbType, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jaku)
                            .addComponent(cbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5)
                            .addComponent(cbTipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnSave)
                                    .addComponent(btnDelete)
                                    .addComponent(btnReset)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(jButton1)))
                        .addGap(11, 11, 11))
                    .addComponent(JLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(cbTable, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        
        //check if selected id is available
        if(this.selectedId > 0){
            update();
        }else{
            store();
        }
        resetForm();
        cbType.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        LoginForm loginForm = new LoginForm();
        loginForm.setLocationRelativeTo(null);
        loginForm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void tableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMousePressed
        
        cbType.setEnabled(false);
        
        String row = this.getSelectedRow();

        String date = null;
        int amount = 0;
        String desc = null;
        String combo = null;
        
        for (int i = 0; model.getRowCount() > i; i++) {
            String id = (String) model.getValueAt(i, 0);
            
            if (id.equals(row)) {
                this.selectedId = Integer.parseInt(id);    
                date = (String) model.getValueAt(i, 1);
                
                int tblAmount = (int) model.getValueAt(i, 2);
                amount = tblAmount;
                
                //set the current temporary textfield amount
                this.tempAmount = tblAmount;
                
                desc = (String) model.getValueAt(i, 3);
                String tblCombo = (String) model.getValueAt(i, 4);
                combo = tblCombo;
                
                //set the current temporary combobox
                this.tempComboBox = tblCombo;
                
                btnDelete.setVisible(true);
                cbCategory.setEnabled(true);
            }
        }
        
        txtDate.setDate(Date.valueOf(date));
        txtAmount.setText(String.valueOf(amount));
        txtDesc.setText(desc);
        cbType.setSelectedItem(combo);

    }//GEN-LAST:event_tableMousePressed

    private void cbTableItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbTableItemStateChanged
        String query = cbTable.getSelectedItem().toString();
        filter(query);
    }//GEN-LAST:event_cbTableItemStateChanged

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int selectedRowIndex = table.getSelectedRow();
        model.removeRow(selectedRowIndex);
        
        //update the current saldo
        if(this.tempComboBox.equals("Income")){
            this.saldo -= this.tempAmount;
            this.setSaldo();
        }else{
            this.saldo += this.tempAmount;
            this.setSaldo();
        } 
        resetTemp();
        
        this.resetForm();
        this.deleteresult.Good();
        cbType.setEnabled(true);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void txtAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyReleased
        int x;
        
        try {
        x = Integer.parseInt(txtAmount.getText());
        } catch (NumberFormatException nfe) {
            txtAmount.setText("");
            this.wrong.amountCantInput();
        }
    }//GEN-LAST:event_txtAmountKeyReleased

    private void txtDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescKeyReleased
        String input = txtDesc.getText();
        
        if(input.length()>20){
            txtDesc.setText("");
            this.wrong.descTooMuch();
        }
    }//GEN-LAST:event_txtDescKeyReleased

    private void cbTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTableActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTableActionPerformed

    private void txtAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyPressed

    }//GEN-LAST:event_txtAmountKeyPressed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        resetForm();
        cbType.setEnabled(true);
    }//GEN-LAST:event_btnResetActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
       // testComboBox();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JLabel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cbCategory;
    private javax.swing.JComboBox<String> cbTable;
    private javax.swing.JComboBox<String> cbTipe;
    private javax.swing.JComboBox<String> cbType;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel jaku;
    private javax.swing.JTable table;
    private javax.swing.JTextField txtAmount;
    private com.toedter.calendar.JDateChooser txtDate;
    private javax.swing.JTextField txtDesc;
    private javax.swing.JLabel txtSaldo;
    // End of variables declaration//GEN-END:variables
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.table.DefaultTableModel;

/**11
 *
 * @author Sahithi
 */
public class hw3 extends javax.swing.JFrame {
    List<javax.swing.JCheckBox> catboxlist = new ArrayList<>(); 
    List<javax.swing.JCheckBox> subcatboxlist = new ArrayList<>();
    List<javax.swing.JCheckBox> subcatboxlist_temp = new ArrayList<>();
    List<javax.swing.JCheckBox> attributeboxlist = new ArrayList<>();
    List<javax.swing.JCheckBox> attributeboxlist_temp = new ArrayList<>();
    List<String> cat_selected = new ArrayList<>();
    List<String> subcat_selected = new ArrayList<>();
    List<String> attribute_selected = new ArrayList<>();
    HashMap<String,HashSet> main_subcat = new HashMap<>();
    HashSet<String> selected_subcat = new HashSet<>();
    javax.swing.JCheckBox box;
    javax.swing.JCheckBox subcatbox;
    javax.swing.JCheckBox attributebox;
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel model1 = new DefaultTableModel();
    DefaultTableModel model2 = new DefaultTableModel();
    DefaultTableModel model3 = new DefaultTableModel();
    String main_category_parameter = null, sub_category_parameter = null,sql="";
    String sql_query1;
    /**
     * Creates new form hw3
     */
    public hw3() throws ClassNotFoundException, SQLException 
    {      
        initComponents();   
        model.setRowCount(800000);
        model.addColumn("Business ID");
        model.addColumn("Business");
        model.addColumn("City");
        model.addColumn("State");
        model.addColumn("Avg Stars");
        model1.setRowCount(800000);
        model1.addColumn("Review ID");
        model1.addColumn("Review Text");
        model1.addColumn("Review Date");
        model1.addColumn("Username");
        model1.addColumn("Votes");
        model1.addColumn("Stars");
        model3.setRowCount(800000);
        model3.addColumn("User ID");
        model3.addColumn("User Name");
        model3.addColumn("Yelping Since");
        model3.addColumn("Avg Stars");
        model2.setRowCount(800000);
        model2.addColumn("Review ID");
        model2.addColumn("Review Text");
        model2.addColumn("Review Date");
        model2.addColumn("User ID");
        model2.addColumn("Votes");
        model2.addColumn("Stars");
        cat_checkboxes();   
    }
    void cat_checkboxes() throws ClassNotFoundException, SQLException
    {
        Class.forName("oracle.jdbc.OracleDriver");
        try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "Oracle")) 
        {
            String sql = "select distinct category from bcategory order by category";
            Statement statement = con.createStatement();
            ResultSet rs= statement.executeQuery(sql);
            while(rs.next())
            {
                box = new javax.swing.JCheckBox(rs.getString(1),false);
                jPanel1.add(box);
                catboxlist.add(box);
                box.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        JCheckBox checkbox = (JCheckBox) evt.getSource();
                        if(checkbox.isSelected())
                        {
                            System.out.println( "Selected ");
                            cat_selected.add(checkbox.getText());
                            //System.out.println(cat_selected);
                            try {
                                subcat_checkboxes();
                                get_main_subcat(checkbox.getText());
                            } catch (SQLException ex) {
                                Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                        }
                        else
                        {
                            System.out.println( "unSelected ");
                            cat_selected.remove(checkbox.getText());
                            main_subcat.remove(checkbox.getText());
                            try {
                                
                                subcat_checkboxes();
                            } catch (SQLException ex) {
                                Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });
                
            }
        }
        jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.PAGE_AXIS));    
        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.PAGE_AXIS)); 
        jPanel3.setLayout(new BoxLayout(jPanel3, BoxLayout.PAGE_AXIS));       
    }
    void get_main_subcat(String category) throws ClassNotFoundException, SQLException
    {     
        String sql = "select distinct sub_category from business_category where category like '%" + category + "%'";
        Class.forName("oracle.jdbc.OracleDriver");       
        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "Oracle");       
        Statement statement = con.createStatement();
        ResultSet rs= statement.executeQuery(sql);       
        String res = null,res1=null;
        HashSet<String> subcat = new HashSet<>();
        while(rs.next())
        {
            res=rs.getString(1);
            res1 = res.substring(1,res.length()-1);
            String[] splits = res1.split(",");
            for(String split1:splits)
            {
               if(!split1.equals(""))
               {
                    subcat.add(split1.trim());
               }
            }
        }
        main_subcat.put(category,subcat);
        //System.out.println("In main_subcat:" + main_subcat);
    }
    void subcat_checkboxes() throws SQLException, ClassNotFoundException
    {
        String sql = subcat_getdata();
        Class.forName("oracle.jdbc.OracleDriver");       
        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "Oracle");       
        Statement statement = con.createStatement();
        ResultSet rs= statement.executeQuery(sql);
        String res = null,res1=null;
        ArrayList<String> subcat = new ArrayList<>(); 
        int i=0;
        while(rs.next())
        {
            res=rs.getString(1);
            res1 = res.substring(1,res.length()-1);
            String[] splits = res1.split(",");
            for(String split1:splits)
            {
               if(!split1.equals(""))
               {
                    subcat.add(split1.trim());
               }
            }
        }
        con.close();
        Set<String> d_subcat = new HashSet<String>(subcat);
        jPanel2.removeAll();
        revalidate();
        subcatboxlist = new ArrayList<>();
        for(String d_subcats:d_subcat)
        {
           subcatbox = new javax.swing.JCheckBox(d_subcats);          
           subcatboxlist.add(subcatbox);   
           subcatbox.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {                
                JCheckBox checkbox = (JCheckBox) evt.getSource();
                if(checkbox.isSelected())
                {
                        System.out.println("Selected" + checkbox.getText());
                        subcat_selected.add(checkbox.getText());
                        selected_subcat.add(checkbox.getText());
                    try {
                        get_main_subcat_combinations();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
                    }   
                }
                else
                {
                    System.out.println("unSelected");
                    selected_subcat.remove(checkbox.getText());
                    subcat_selected.remove(checkbox.getText());                  
                    //System.out.println(subcat_selected);
                    //System.out.println("selected_subcat" +subcat_selected);
                    try {
                        get_main_subcat_combinations();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }          
            });      
        }       
        for(JCheckBox subcatboxlist1: subcatboxlist)
        {
            for (JCheckBox subcatboxlist_temp1 : subcatboxlist_temp) 
            {
                if(subcatboxlist1.getText().equals(subcatboxlist_temp1.getText()))
                {
                   if(subcatboxlist_temp1.isSelected())
                   {
                       subcatboxlist1.setSelected(true);
                   }
                }
            }
        }
        subcatboxlist_temp=subcatboxlist;         
        for(JCheckBox subcatboxlist1: subcatboxlist)
        {
        jPanel2.add(subcatboxlist1);
        revalidate();
        repaint();
        }     
    }
    void get_main_subcat_combinations() throws ClassNotFoundException, SQLException
    {
        sql="";        
        HashSet<String> main_from_subcat = new HashSet<>();
        for(String key:main_subcat.keySet())
        {
            main_from_subcat.addAll(main_subcat.get(key)); 
            main_from_subcat.retainAll(selected_subcat);
            for(String sub_category: main_from_subcat)
            {
                main_category_parameter = key;
                sub_category_parameter = sub_category;
                sql = attribute_getdata(sql,main_category_parameter,sub_category_parameter); 
            }            
        }
        if(jComboBox1.getSelectedItem()== "AND")
        {
            sql= sql.replaceAll(" intersect $","");  
        }
        else
        {
            sql= sql.replaceAll(" union $","");    
        }
        attributes_checkboxes(sql);
    }
    String subcat_getdata()
    {
        String sql = "select distinct sub_category from business_category where category like ";
        if(jComboBox1.getSelectedItem()== "AND")
        {
        for (JCheckBox catboxlist1 : catboxlist) 
        {
            if(catboxlist1.isSelected())
            {
                sql += " '%"+ catboxlist1.getText() + "%' and category like";
            }
        }
        sql= sql.replaceAll("and category like$","");
        }
        else
        {
        for (JCheckBox catboxlist1 : catboxlist) 
        {
            if(catboxlist1.isSelected())
            {
                sql += " '%"+ catboxlist1.getText() + "%' or category like";
            }
        }
        sql= sql.replaceAll("or category like$","");
        }
        return sql;
    }
    void attributes_checkboxes(String sql) throws ClassNotFoundException, SQLException
    { 
        Class.forName("oracle.jdbc.OracleDriver");       
        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "Oracle");
        Statement statement = con.createStatement();
        ResultSet rs= statement.executeQuery(sql);
        String res = null;
        ArrayList<String> attributes = new ArrayList<>(); 
        while(rs.next())
        {
            res=rs.getString(1);          
           if(!res.equals(""))
           {
                    attributes.add(res.trim());
           }           
        }
        con.close();
        jPanel3.removeAll();
        revalidate();
        attributeboxlist = new ArrayList<>();
        for(String attribute:attributes)
        {
           attributebox = new javax.swing.JCheckBox(attribute);
           attributeboxlist.add(attributebox);
            attributebox.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {                
                JCheckBox checkbox = (JCheckBox) evt.getSource();    
                if(checkbox.isSelected())
                {
                        attribute_selected.add(checkbox.getText());
                        System.out.println("Selected");                    
                }
                else
                {
                    System.out.println("unSelected");
                }
            }          
            });       
        }
        for(JCheckBox attributeboxlist1: attributeboxlist)
        {
            for (JCheckBox attributeboxlist_temp1 : attributeboxlist_temp) 
            {
                if(attributeboxlist1.getText().equals(attributeboxlist_temp1.getText()))
                {
                   if(attributeboxlist_temp1.isSelected())
                   {
                       attributeboxlist1.setSelected(true);
                   }
                }
            }
        }
        attributeboxlist_temp=attributeboxlist;         
        for(JCheckBox attributeboxlist1: attributeboxlist)
        {
        jPanel3.add(attributeboxlist1);
        revalidate();
        repaint();
        }    
    }
    String attribute_getdata(String sql, String main_category_parameter,String sub_category_parameter)
    {       
        String sql1="",sql2=""; 
        sql += "(select distinct ba.attributes from business_category bc,business_attributes ba where bc.BUSINESS_ID= ba.BUSINESS_ID and "+
"bc.CATEGORY like '%" +  main_category_parameter + "%' and bc.SUB_CATEGORY like '%" + sub_category_parameter + "%')";
        if(jComboBox1.getSelectedItem()== "AND")
        {
            sql1 = " intersect ";
            sql += sql1;
        }
        else
        {
            sql2 = " union ";
            sql += sql2;     
        }
        return sql;     
    } 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jComboBox6 = new javax.swing.JComboBox();
        jComboBox7 = new javax.swing.JComboBox();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jComboBox8 = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(900, 600));

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jPanel6.setLayout(null);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 645, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel1);

        jPanel6.add(jScrollPane1);
        jScrollPane1.setBounds(0, 10, 130, 210);

        jTextPane1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextPane1InputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        jScrollPane6.setViewportView(jTextPane1);

        jPanel6.add(jScrollPane6);
        jScrollPane6.setBounds(466, 10, 550, 90);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(jPanel3);

        jPanel6.add(jScrollPane3);
        jScrollPane3.setBounds(300, 10, 160, 210);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select AND or OR between attributes","AND", "OR"}));
        jPanel6.add(jComboBox1);
        jComboBox1.setBounds(0, 230, 422, 21);

        jTable1.setModel(model);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable1);

        jPanel6.add(jScrollPane4);
        jScrollPane4.setBounds(466, 110, 550, 190);

        jLabel1.setText("Stars");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"=,<,>", "=", "<", ">" }));

        jLabel2.setText("Votes");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"=,<,>", "=", "<", ">" }));

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel4);
        jPanel4.setBounds(10, 360, 440, 100);

        jButton1.setText("Submit Query");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton1);
        jButton1.setBounds(90, 480, 263, 51);

        jTable2.setModel(model1);
        jScrollPane5.setViewportView(jTable2);

        jPanel6.add(jScrollPane5);
        jScrollPane5.setBounds(469, 317, 550, 210);

        jLabel3.setText("Review");

        jLabel4.setText("From");

        jLabel5.setText("To");

        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(35, 35, 35)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(62, 62, 62)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(176, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel5);
        jPanel5.setBounds(10, 260, 440, 100);

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 500));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 777, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jPanel2);

        jPanel6.add(jScrollPane2);
        jScrollPane2.setBounds(140, 10, 150, 210);

        jTabbedPane1.addTab("Business Query", jPanel6);

        jLabel6.setText("Member Since");

        jLabel7.setText("Review Count");

        jLabel8.setText("Number of Friends");

        jLabel9.setText("Average Stars");

        jLabel10.setText("Number of Votes");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "=, >, <", "=", ">", "<" }));

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "=, >, <", "=", ">", "<" }));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "=, >, <", "=", ">", "<" }));

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "=, >, <", "=", ">", "<" }));

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select AND OR between attributes", "AND", "OR" }));

        jButton2.setText("Execute User Query");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane7.setViewportView(jTextArea1);

        jTable3.setModel(model3);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(jTable3);

        jTable4.setModel(model2);
        jScrollPane9.setViewportView(jTable4);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jComboBox8, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jComboBox5, 0, 123, Short.MAX_VALUE)
                                            .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jComboBox6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jComboBox7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                                            .addComponent(jTextField5)
                                            .addComponent(jTextField4)
                                            .addComponent(jTextField3)))
                                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGap(64, 64, 64)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                    .addComponent(jScrollPane8))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("User Query", jPanel7);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:    
        String rel_op="",cat_sel="",subcat_sel="",attr_sel="";
        String star_selection = jComboBox2.getSelectedItem().toString();
        String vote_selection = jComboBox3.getSelectedItem().toString();
        String star_text = jTextField1.getText().toString();
        String vote_text = jTextField2.getText().toString();
        if(vote_selection == ">")
        {       
            vote_selection = ">";
        }
        else if(vote_selection == "<")
        {
            vote_selection = "<";
        }
        else
        {
            vote_selection = "=";
        }
        if(star_selection == ">")
        {
            star_selection =">";
        }
        else if(star_selection == "<")
        {
            star_selection ="<";
        }
        else
        {
            star_selection ="=";
        }
        if(jComboBox1.getSelectedItem()=="AND")
        {
            rel_op= " AND ";
        }
        else
        {
            rel_op=" OR ";
        }
        for(String selectedvalue:cat_selected)
        {
            cat_sel +=  " '%"+ selectedvalue.toString() + "%' " + rel_op + "bc.category like"; 
        }
        cat_sel=cat_sel.replaceAll(rel_op + "bc.category like$","");
        for(String selectedvalue:subcat_selected)
        {
            subcat_sel +=  " '%"+ selectedvalue.toString() + "%' " + rel_op + "bc.sub_category like"; 
        }
        subcat_sel=subcat_sel.replaceAll(rel_op + "bc.sub_category like$","");      
        for(String selectedvalue:attribute_selected)
        {
            attr_sel +=  " '%"+ selectedvalue.toString() + "%' " + rel_op + "ba.attributes like"; 
        }
        attr_sel=attr_sel.replaceAll(rel_op + "ba.attributes like$","");  
      
         sql_query1 ="select distinct b.business_id,b.name,b.city,b.state,b.stars from business b where b.business_id in"
                + " (select distinct bc.business_id from business_Category bc where (bc.category like " + cat_sel + "))";
        if(!subcat_selected.isEmpty())
        {        
            sql_query1 = sql_query1.substring(0, sql_query1.length()-1);
            sql_query1 += " and (bc.sub_category like" + subcat_sel + "))" ; 
            System.out.println("sql_query:" + sql_query1);
        }
        if((!subcat_selected.isEmpty()) && (!attribute_selected.isEmpty()))
        {
            sql_query1= "select distinct b.business_id,b.name,b.city,b.state,b.stars from business b where b.business_id in\n"
                    + "(select distinct bc.business_id from business_Category bc, business_attributes ba where \n" +
"ba.business_id=bc.business_id and (bc.category like " + cat_sel + ") and (bc.sub_category like" + subcat_sel + ") \n" +
"and (ba.attributes like " + attr_sel + " ))";
        }
        if(jDateChooser1.getDate() != null && !jTextField1.getText().isEmpty() && !jTextField2.getText().isEmpty())
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String from_date = df.format(jDateChooser1.getDate());
            String to_date = df.format(jDateChooser2.getDate());
            sql_query1 = sql_query1.substring(0, sql_query1.length()-1);
            sql_query1 += "\n and bc.business_id in (select distinct business_id from review where" +
    "\n to_date(review_date,'yyyy-MM-dd') between to_date('" + from_date + "','yyyy-MM-dd') and to_date('" + to_date  + "','yyyy-MM-dd')"
                    + " and stars" + star_selection + star_text  +  " and votes" + vote_selection + vote_text  + " ))" ;  
            System.out.println("sql_query:" + sql_query1);  
        }
        else if(jDateChooser1.getDate() != null && jTextField1.getText().isEmpty() && jTextField2.getText().isEmpty() )
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String from_date = df.format(jDateChooser1.getDate());
            String to_date = df.format(jDateChooser2.getDate());
            sql_query1 = sql_query1.substring(0, sql_query1.length()-1);
            sql_query1 += "\n and bc.business_id in (select distinct business_id from review where" +
    "\n to_date(review_date,'yyyy-MM-dd') between to_date('" + from_date + "','yyyy-MM-dd') and to_date('" + to_date  + "','yyyy-MM-dd')"
              + " ))" ; 
            System.out.println("sql_query:" + sql_query1);
        }
        else if(jDateChooser1.getDate() == null && !jTextField1.getText().isEmpty() && !jTextField2.getText().isEmpty())
        {
            sql_query1 = sql_query1.substring(0, sql_query1.length()-1);            
            sql_query1 +="\n and bc.business_id in (select distinct business_id from review where" +
                    " stars" + star_selection + star_text  + " and votes" + vote_selection + vote_text  + " ))"  ;
            System.out.println("sql_query:" + sql_query1);
        }
        else if(jDateChooser1.getDate() == null && !jTextField1.getText().isEmpty() && jTextField2.getText().isEmpty())
        {
            sql_query1 = sql_query1.substring(0, sql_query1.length()-1);            
            sql_query1 +="\n and bc.business_id in (select distinct business_id from review where" +
                    " stars" + star_selection + star_text   + " ))"  ;
            System.out.println("sql_query:" + sql_query1);
        }
        else if(jDateChooser1.getDate() == null && jTextField1.getText().isEmpty() && !jTextField2.getText().isEmpty())
        {
            sql_query1 = sql_query1.substring(0, sql_query1.length()-1);            
            sql_query1 +="\n and bc.business_id in (select distinct business_id from review where" +
                     " votes" + vote_selection + vote_text  + " ))"  ;
            System.out.println("sql_query:" + sql_query1);
        }
        else if(jDateChooser1.getDate() != null && !jTextField1.getText().isEmpty() && jTextField2.getText().isEmpty())
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String from_date = df.format(jDateChooser1.getDate());
            String to_date = df.format(jDateChooser2.getDate());
            sql_query1 = sql_query1.substring(0, sql_query1.length()-1);
            sql_query1 += "\n and bc.business_id in (select distinct business_id from review where" +
    "\n to_date(review_date,'yyyy-MM-dd') between to_date('" + from_date + "','yyyy-MM-dd') and to_date('" + to_date  + "','yyyy-MM-dd')"
                    + " and stars" + star_selection + star_text + " ))" ;  
            System.out.println("sql_query:" + sql_query1);  
        }
        else if(jDateChooser1.getDate() != null && jTextField1.getText().isEmpty() && !jTextField2.getText().isEmpty())
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String from_date = df.format(jDateChooser1.getDate());
            String to_date = df.format(jDateChooser2.getDate());
            sql_query1 = sql_query1.substring(0, sql_query1.length()-1);
            sql_query1 += "\n and bc.business_id in (select distinct business_id from review where" +
    "\n to_date(review_date,'yyyy-MM-dd') between to_date('" + from_date + "','yyyy-MM-dd') and to_date('" + to_date  + "','yyyy-MM-dd')"
                    +  " and votes" + vote_selection + vote_text  + " ))" ;  
            System.out.println("sql_query:" + sql_query1);  
        }

        jTextPane1.setText(sql_query1);
        if(cat_selected.isEmpty())
        {
            jTextPane1.setText("Select Category!!");
        } 
        else
        {
        try {       
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        Connection con=null;       
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "Oracle");
        } catch (SQLException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        Statement statement=null;
        try {
            statement = con.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResultSet rs=null;
        try {
            rs= statement.executeQuery(sql_query1);
        } catch (SQLException ex) {
            jTextPane1.setText("Incorrect query");
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            int row=0;
            if((rs.next()) == false)
            {
                System.out.println("No business retrieved");
            } 
            while(rs.next())
            {
                int col=0;
                jTable1.setValueAt(rs.getString(1), row, col);
                jTable1.setValueAt(rs.getString(2), row, col+1);
                jTable1.setValueAt(rs.getString(3), row, col+2);
                jTable1.setValueAt(rs.getString(4), row, col+3);
                jTable1.setValueAt(rs.getFloat(5), row, col+4);              
               row++;
               revalidate();
            }
            System.out.println("Business Query computed");
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        }     
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
    System.out.println(jTable1.getValueAt(jTable1.getSelectedRow(),0));
    String review_sql = "select r.review_id,r.text,r.review_date, yc.NAME,r.votes,r.stars from review r,yelp_user yc where r.USER_ID=yc.USER_ID and r.BUSINESS_ID = ?";
    try {       
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        Connection con=null;       
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "Oracle");
        } catch (SQLException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        PreparedStatement statement=null;
        try {
            statement = con.prepareStatement(review_sql);
            statement.setString(1,jTable1.getValueAt(jTable1.getSelectedRow(),0).toString());
        } catch (SQLException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResultSet rs=null;
        try {
            rs= statement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            int row=0;
            if((rs.next()) == false)
            {
                System.out.println("No reviews retrieved");
            } 
            while(rs.next())
            {
                int col=0;
                jTable2.setValueAt(rs.getString(1), row, col);
                jTable2.setValueAt(rs.getString(2), row, col+1);
                jTable2.setValueAt(rs.getString(3), row, col+2);
                jTable2.setValueAt(rs.getString(4), row, col+3);
                jTable2.setValueAt(rs.getInt(5), row, col+4);  
                jTable2.setValueAt(rs.getFloat(6), row, col+5);
               row++;
               revalidate();
            }
            System.out.println("Review in Business Query computed");
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        // TODO add your handling code here:
       
       
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
         String from_date="";
        if(jDateChooser1.getDate() != null)
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            from_date = df.format(jDateChooser1.getDate());    
        }
        String review_selection = jComboBox4.getSelectedItem().toString();
        String review_count_text = jTextField3.getText().toString();
        String rel_op="";
        String friend_selection = jComboBox5.getSelectedItem().toString();
        String friend_text = jTextField4.getText().toString();
        String star_selection = jComboBox6.getSelectedItem().toString();
        String vote_selection = jComboBox7.getSelectedItem().toString();
        String star_text = jTextField5.getText().toString();
        String vote_text = jTextField6.getText().toString();
        if(vote_selection == ">")
        {       
            vote_selection = ">";
        }
        else if(vote_selection == "<")
        {
            vote_selection = "<";
        }
        else
        {
            vote_selection = "=";
        }
        if(star_selection == ">")
        {
            star_selection =">";
        }
        else if(star_selection == "<")
        {
            star_selection ="<";
        }
        else
        {
            star_selection ="=";
        }
        if(review_selection == ">")
        {
            review_selection =">";
        }
        else if(review_selection == "<")
        {
            review_selection ="<";
        }
        else
        {
            review_selection ="=";
        }
        if(friend_selection == ">")
        {
            friend_selection =">";
        }
        else if(friend_selection == "<")
        {
            friend_selection ="<";
        }
        else
        {
            friend_selection ="=";
        }
        if(jComboBox8.getSelectedItem()=="AND")
        {
            rel_op= " AND ";
        }
        else
        {
            rel_op=" OR ";
        }
        String user_query = "select user_id,name,yelping_since,average_stars from \n" + "yelp_user " +
"where to_date(yelping_since,'yyyy-MM') >= \n" + "to_date('"+ from_date + "','yyyy-MM-dd') ";
        if(!jTextField3.getText().isEmpty())
        {
            user_query += rel_op + "review_count \n" + review_selection  + review_count_text ; 
        }
        if(!jTextField4.getText().isEmpty())
        {
            user_query += rel_op  +  "friends" + friend_selection  +  friend_text;
        }
        if(!jTextField5.getText().isEmpty())
        {
            user_query += rel_op + "average_stars" + star_selection + star_text ;
        }
        if(!jTextField6.getText().isEmpty())
        {
            user_query += rel_op + "\nvotes" + vote_selection + vote_text ;
        }
        jTextArea1.setText(user_query);
        
        try {       
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        Connection con=null;       
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "Oracle");
        } catch (SQLException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        Statement statement=null;
        try {
            statement = con.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResultSet rs=null;
        try {
            rs= statement.executeQuery(user_query);
        } catch (SQLException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            int row=0;
            if((rs.next()) == false)
            {
                System.out.println("No users retrieved");
            }         
            while(rs.next())
            {
                int col=0;
                jTable3.setValueAt(rs.getString(1), row,col);
                jTable3.setValueAt(rs.getString(2), row, col+1);
                jTable3.setValueAt(rs.getString(3), row, col+2);
                jTable3.setValueAt(rs.getFloat(4), row, col+3);              
               row++;
               revalidate();
            }
            System.out.println("User Query computed");
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:
        System.out.println(jTable3.getValueAt(jTable3.getSelectedRow(),0));
    String user_sql = "select r.review_id,r.text,r.review_date, r.user_id,r.votes,r.stars from review r where r.USER_ID = ?";
    try {       
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        Connection con=null;       
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "Oracle");
        } catch (SQLException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        PreparedStatement statement=null;
        try {
            statement = con.prepareStatement(user_sql);
            statement.setString(1,jTable3.getValueAt(jTable3.getSelectedRow(),0).toString());
        } catch (SQLException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResultSet rs=null;
        try {
            rs= statement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            int row=0;
            if((rs.next()) == false)
            {
                System.out.println("No reviews retrieved");
            } 
            while(rs.next())
            {
                int col=0;
                jTable4.setValueAt(rs.getString(1), row, col);
                jTable4.setValueAt(rs.getString(2), row, col+1);
                jTable4.setValueAt(rs.getString(3), row, col+2);
                jTable4.setValueAt(rs.getString(4), row, col+3);
                jTable4.setValueAt(rs.getInt(5), row, col+4);  
                jTable4.setValueAt(rs.getFloat(6), row, col+5);
               row++;
               revalidate();
            }
            System.out.println("Reviews in user query computed");
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTable3MouseClicked

    private void jTextPane1InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextPane1InputMethodTextChanged
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jTextPane1InputMethodTextChanged
 
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
            java.util.logging.Logger.getLogger(hw3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(hw3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(hw3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(hw3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new hw3().setVisible(true);
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}

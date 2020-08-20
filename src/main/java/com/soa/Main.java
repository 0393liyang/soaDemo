package com.soa;


import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

public class Main {
    private static AtomikosDataSourceBean createAtomikosDataSourceBean(String dbName){
        Properties p=new Properties();
        p.setProperty("url","jdbc:mysql://localhost:3306/"+dbName+"?serverTimezone=UTC&characterEncoding=utf-8");
        p.setProperty("user","root");
        p.setProperty("password","123456");

        AtomikosDataSourceBean ds=new AtomikosDataSourceBean();
        ds.setUniqueResourceName(dbName);
        ds.setXaDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
        ds.setXaProperties(p);
        return ds;

    }

    public static void main(String[] args) {
        AtomikosDataSourceBean ds1=createAtomikosDataSourceBean("ymorder");
        AtomikosDataSourceBean ds2=createAtomikosDataSourceBean("stock");

        Connection conn1=null;
        Connection conn2=null;

        PreparedStatement ps1=null;
        PreparedStatement ps2=null;

        UserTransaction userTransaction=new UserTransactionImp();

        try{
            userTransaction.begin();

            conn1= ds1.getConnection();
            ps1= conn1.prepareStatement("insert into ymorder.orderinfo (user_id,state,sku_no) value (?,?,?)");
            ps1.setString(1,"456");
            ps1.setString(2,"456");
            ps1.setString(3,"456");
            ps1.executeUpdate();

            conn2=ds2.getConnection();
            ps2=conn2.prepareStatement("insert into stock.stock(sku_no,real_stock,pre_stock) value (?,?,?)");
            ps2.setString(1,"456");
            ps2.setString(2,"456");
            ps2.setString(3,"456");
            ps2.executeUpdate();

            userTransaction.commit();
        }catch (Exception e){
            e.printStackTrace();
            try {
                userTransaction.rollback();
            } catch (SystemException e1) {
                e1.printStackTrace();
            }
        }
    }
}

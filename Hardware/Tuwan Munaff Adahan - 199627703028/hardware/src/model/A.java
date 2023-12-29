
package model;

import java.sql.ResultSet;

/**
 *
 * @author MUNAFF
 */
class A {
    public static void main(String[] args) {
        
        try {
            String x;
        
        if(10>5){
            x = "WHERE `username` = 'munaff'";
        }else{
        
        x = "";
        }
        
        ResultSet rs = MySQL.search("SELECT * FROM `user` "+x);
        while(rs.next()){
            System.out.println(rs.getString("name")); 
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}

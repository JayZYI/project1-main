/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import javax.swing.JOptionPane;

public class Warning {
    public void cantString(){
        JOptionPane.showMessageDialog(null,"Cannot string!");
    }
    
    public void cantNull(){
        JOptionPane.showMessageDialog(null,"Field cannot null!");
    }

    public void amountMoreThanSaldo(){
        JOptionPane.showMessageDialog(null,"Amount more than saldo!");
    } 

    public void descTooMuch(){
        JOptionPane.showMessageDialog(null,"Description cannot more than 20");
    } 

    public void amountCantInput(){
        JOptionPane.showMessageDialog(null,"Amount Cannot input character and cannot more than 9");
    }
    
    public void amountEqualZero(){
        JOptionPane.showMessageDialog(null,"Amount cannot 0");
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphe_java;

import graphe_java.Fenetre.TypeGraphe;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Romain
 */
public class DialArbre extends JDialog implements ActionListener{
    private JLabel tailleL, degreL;
    private JTextField taille, degre;
    private JButton but1, but2;
    private int[] param = new int[2];
    
    public DialArbre(Fenetre owner){
        super(owner, true);
        init();
    }
    
    public void init(){
        tailleL = new JLabel("Hauteur");
        degreL = new JLabel("Nombre de liens");
        taille = new JTextField(10);
        degre = new JTextField(10);
        but1 = new JButton("ANNULER");
        but2 = new JButton("VALIDER");
        but1.addActionListener(this);
        but2.addActionListener(this);
        
        getRootPane().setDefaultButton(but2);
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints pos = new GridBagConstraints();
        pos.fill = GridBagConstraints.BOTH;
            
        pos.gridx=0; pos.gridy=0;
        this.add(tailleL, pos);
        pos.gridx=1; pos.gridy=0;
        this.add(taille, pos);
        pos.gridx=0; pos.gridy=1;
        this.add(degreL, pos);
        pos.gridx=1; pos.gridy=1;
        this.add(degre, pos);
        pos.gridx=0; pos.gridy=2;
        this.add(but1, pos);
        pos.gridx=1; pos.gridy=2;
        this.add(but2, pos);
        
        this.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == but1){
            param[0]=0;param[1]=0;
            this.setVisible(false); 
        }
        if(e.getSource() == but2){
            if(!"".equals(taille.getText()) && !"".equals(degre.getText())){
                param[0]=Integer.parseInt(taille.getText()); param[1]=Integer.parseInt(degre.getText());
                this.setVisible(false);
            }
        }
    }
   
    
    public int[] showDialog(){
       setVisible(true); // when this is called the dialog will show up
       return param;
    }
}

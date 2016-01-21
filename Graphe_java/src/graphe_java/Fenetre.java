/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphe_java;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.PngImage;
import static graphe_java.Fenetre.TypeGraphe.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.coloring.WelshPowell;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.stream.file.FileSinkImages;

/**
 *
 * @author Romain
 */
public final class Fenetre extends JFrame{
    
    private Choix choix;
    private Vue_Graphe graphe;
    private Actions ac;
    private Algo algo;
    private boolean couvrant;
    
    
    public enum TypeGraphe {
        Cycle("Cycle"), Chaine("Chaine"), Tore("Tore"), Grille("Grille"), Arbre("Arbre"), Aleatoire("Aleatoire");	
        private final String s;
        private TypeGraphe(String s) {
            this.s = s;
        }
        @Override
        public String toString() {
            return s;
        }
    }
    
    public final class Choix extends JPanel implements ActionListener{
        private JComboBox listeChoix;
        private JButton boutonChoix;
        Fenetre owner;
        
        public Choix(Fenetre o){
            this.owner = o;
            init();
        }
        
        public void init(){
            listeChoix = new JComboBox(new DefaultComboBoxModel(TypeGraphe.values()));
            boutonChoix = new JButton("Générer");
            boutonChoix.addActionListener(this);
            
            this.setLayout(new GridBagLayout());
            GridBagConstraints pos = new GridBagConstraints();
            pos.fill = GridBagConstraints.BOTH;
            
            pos.gridx=0;
            pos.gridy=0;
            this.add(listeChoix,pos);
            
            pos.gridx=1;
            this.add(boutonChoix,pos);
            
            this.setBorder(BorderFactory.createTitledBorder("Choix du Graphe"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==boutonChoix){
                TypeGraphe selectedType = (TypeGraphe)listeChoix.getSelectedItem();
                int[] param = new int[2];
                switch(selectedType){
                    case Aleatoire:
                        DialAlea da = new DialAlea(owner);
                        param = da.showDialog();
                        break;
                    case Cycle:
                        DialNbr dc = new DialNbr(owner);
                        param = dc.showDialog();
                        break;
                    case Chaine:
                        DialNbr dch = new DialNbr(owner);
                        param = dch.showDialog();
                        break;
                    case Tore:
                        DialNbr dt = new DialNbr(owner);
                        param = dt.showDialog();
                        break;
                    case Grille:
                        DialNbr dg = new DialNbr(owner);
                        param = dg.showDialog();
                        break;
                    case Arbre:
                        DialArbre dar = new DialArbre(owner);
                        param = dar.showDialog();
                        break;
                }
                
                Graph g = FabricGraphe.GenererGraphe(selectedType, param);
                if(g!=null && (param[0]!=0 || param[1]!=0)){
                    graphe.setGraphe(g);
                    graphe.update();
                    graphe.type = selectedType;
                    couvrant = false;
                }
            }
        }
    }
    
    public final class Actions extends JPanel implements ActionListener{
        private JButton pdf, pond;
        
        public Actions(){
            init();
        }
        
        public void init(){
            pdf = new JButton("Export PDF");
            pdf.addActionListener(this);
            pond = new JButton("Pondérer");
            pond.addActionListener(this);
            
            this.setLayout(new GridBagLayout());
            GridBagConstraints pos = new GridBagConstraints();
            pos.fill = GridBagConstraints.BOTH;
            
            pos.gridx=0;
            pos.gridy=0;
            this.add(pdf,pos);
            
            pos.gridx=1;
            this.add(pond,pos);
            
            this.setBorder(BorderFactory.createTitledBorder("Actions"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == pdf){
                String pngFile = "graphe.pdf";
                
                FileSinkImages pic = new FileSinkImages(FileSinkImages.OutputType.PNG,
                FileSinkImages.Resolutions.VGA);
                pic.setLayoutPolicy(FileSinkImages.LayoutPolicy.COMPUTED_FULLY_AT_NEW_IMAGE);
                try {
                    pic.writeAll(graphe.graph, pngFile); //pngFile est une variable de type String, grapheCourant est un Graph
                } catch (IOException ex) {
                    Logger.getLogger(Fenetre.class.getName()).log(Level.SEVERE, null, ex);
                }
                //pour créer une image qui sera ajoutable au pdf:
                com.itextpdf.text.Image convertBmp = null;
                try {
                    convertBmp = PngImage.getImage(pngFile);
                } catch (IOException ex) {
                    Logger.getLogger(Fenetre.class.getName()).log(Level.SEVERE, null, ex);
                }
                 //Redimensionner l’image
                int PAGE_LEFT_MARGIN = 0;
                int PAGE_RIGHT_MARGIN = 75;
                int PAGE_TOP_MARGIN = 0;
                int PAGE_BOTTOM_MARGIN = 0;
                convertBmp.scaleToFit(400, 300);
                
                Document document = new Document(PageSize.A4);
                try{
                    PdfWriter.getInstance(document,
                    new FileOutputStream(pngFile));
                    document.open();
                    document.add(new Paragraph("Infos sur le graphe"));
                    document.add(new Paragraph("Nom: " + graphe.type + " Graph"));
                    document.add(new Paragraph("Degré moyen: " + Toolkit.averageDegree(graphe.graph)));
                    document.add(new Paragraph("Degré mini: " + Toolkit.degreeDistribution(graphe.graph)[0]));
                    document.add(new Paragraph("Degré maxi: " + Toolkit.degreeDistribution(graphe.graph)[Toolkit.degreeDistribution(graphe.graph).length-1]));
                    document.add(new Paragraph("Diamètre: " + Toolkit.diameter(graphe.graph)));
                    document.add(convertBmp);
                } catch (DocumentException | IOException de) {
                }
                document.close();
            }
            
            if(e.getSource() == pond){
                for(Edge edge : graphe.graph.getEachEdge()){
                    int rand = 1 + (int)(Math.random() * ((100 - 1) + 1));
                    edge.setAttribute("ui.label", rand);
                    graphe.update2();
                }
            }
        }
    }
    
    public final class Algo extends JPanel implements ActionListener{
        private JButton arbre, welsh, dsat;
        private JLabel nbrCol1, nbrCol2;
        
        public Algo(){
            init();
        }
        
        public void init(){
            arbre = new JButton("Arbre Couvrant");
            arbre.addActionListener(this);
            welsh = new JButton("Welsh-Powell");
            welsh.addActionListener(this);
            dsat = new JButton("DSat");
            dsat.addActionListener(this);
            nbrCol1 = new JLabel();
            nbrCol2 = new JLabel();
            
            this.setLayout(new GridBagLayout());
            GridBagConstraints pos = new GridBagConstraints();
            pos.fill = GridBagConstraints.BOTH;
            
            pos.gridx=0;
            pos.gridy=0;
            this.add(arbre,pos);
            pos.gridy=1;
            this.add(welsh,pos);
            pos.gridx=1;
            this.add(nbrCol1,pos);
            pos.gridx=0;
            pos.gridy=2;
            this.add(dsat,pos);
            pos.gridx=1;
            this.add(nbrCol2,pos);
            
            this.setBorder(BorderFactory.createTitledBorder("Algorithmes"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == arbre){
                if(!couvrant){
                    Graph g = FabricGraphe.GenererGrapheKruskal(graphe.graph);
                    graphe.setGraphe(g);
                    graphe.update2();
                    couvrant = true;
                }
                else{
                    Graph g = FabricGraphe.ResetGraph(graphe.graph);
                    graphe.setGraphe(g);
                    graphe.update();
                    couvrant = false;
                }
            }
            if(e.getSource() == welsh){
                WelshPowell wp = new WelshPowell("color");
                Graph g = FabricGraphe.GenererGrapheWelsh(graphe.graph, wp);
                graphe.setGraphe(g);
                nbrCol1.setText(Integer.toString(wp.getChromaticNumber()));
                graphe.update2();
            }
            if(e.getSource() == dsat){
                
            }
        }
    }
    
    public Fenetre(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
    }
    
    public void init(){
        graphe = new Vue_Graphe();
        choix = new Choix(this);
        ac = new Actions();
        algo = new Algo();
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints pos = new GridBagConstraints();
        pos.fill = GridBagConstraints.BOTH;
            
        pos.gridx=0; pos.gridy=0;
        pos.gridheight=9;
        this.add(graphe, pos);
        pos.gridheight=1;
        pos.gridx=1;
        this.add(choix, pos);
        pos.gridy=1;
        this.add(ac, pos);
        pos.gridy=2;
        this.add(algo, pos);
        this.pack();
        this.setVisible(true);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphe_java;

import graphe_java.Fenetre.TypeGraphe;
import java.awt.Color;
import org.graphstream.algorithm.Kruskal;
import org.graphstream.algorithm.coloring.WelshPowell;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 *
 * @author Romain
 */
public abstract class FabricGraphe {
    
    public static Graph GenererGraphe(TypeGraphe t, int param[]){
        Graph g = new SingleGraph("Graphe");
        Generator gen;
        switch(t){
        case Grille:
            gen = new GridGenerator();
            gen.addSink(g);
            gen.begin();
            for(int i=0; i < param[0]; i++) 
                gen.nextEvents();
            gen.end();
            return g;
        case Aleatoire:
            gen = new RandomGenerator(param[1]);
            gen.addSink(g);
            gen.begin();
            for(int i=0; i < param[0]; i++) 
                gen.nextEvents();
            gen.end();
            return g;
        case Chaine:
            for(int i=0; i < param[0]; i++){
                g.addNode(Integer.toString(i));
                if(i>0)
                    g.addEdge(Integer.toString(i-1)+Integer.toString(i), Integer.toString(i-1), Integer.toString(i));
            }
            return g;
        case Cycle:
            for(int i=0; i < param[0]; i++){
                g.addNode(Integer.toString(i));
                if(i>0)
                    g.addEdge(Integer.toString(i-1)+Integer.toString(i), Integer.toString(i-1), Integer.toString(i));
                if(i == param[0]-1)
                    g.addEdge(Integer.toString(i+1), Integer.toString(0), Integer.toString(i));
            }
            return g;
        case Tore:
            gen = new GridGenerator(true, true);
            gen.addSink(g);
            gen.begin();
            for(int i=0; i < param[0]; i++) 
                gen.nextEvents();
            gen.end();
            return g;
        case Arbre:
            if(param[0] < 0){
                return g;
            }
            g.addNode("0");
            constructArbre(g,param[1],param[0],0);
            return g;
        default:
            break;
        }
        return null;
        
    }
    
    public static void constructArbre(Graph g, int nbrFils, int hauteur, int parent){
        if(hauteur == 0)
            return;
        else{
            for(int i = 0; i < nbrFils; i++){
                g.addNode(Integer.toString(g.getNodeCount()));
                g.addEdge(Integer.toString(parent)+"."+Integer.toString(g.getNodeCount()-1), Integer.toString(parent), Integer.toString(g.getNodeCount()-1));
                constructArbre(g,nbrFils,hauteur-1,g.getNodeCount()-1);
            }
        }
    }
    
    public static Graph GenererGrapheKruskal(Graph g){
        String css = "edge .notintree {size:1px;fill-color:gray;} " +
				 "edge .intree {size:3px;fill-color:black;}";
	g.addAttribute("ui.stylesheet", css);
        Kruskal kruskal = new Kruskal("ui.class", "intree", "notintree");
        kruskal.init(g);
        kruskal.compute();
        return g;
    }
    
    public static Graph ResetGraph(Graph g){
        g.setAttribute("ui.stylesheet", "edge { fill-color: black; }");
        return g;
    }
    
    public static Graph GenererGrapheWelsh(Graph g, WelshPowell wp){
        wp.init(g);
        wp.compute();
        
        Color[] cols = new Color[wp.getChromaticNumber()];
        for(int i=0;i< wp.getChromaticNumber();i++){
            cols[i]=Color.getHSBColor((float) (Math.random()*360), 0.8f, 0.9f);
        }
        for(Node n : g){ 
            int col = (int) n.getNumber("color");
            n.addAttribute("ui.style", "fill-color:rgba("+cols[col].getRed()+","+cols[col].getGreen()+","+cols[col].getBlue()+",255);" );
        }
        return g;
    }

    
}

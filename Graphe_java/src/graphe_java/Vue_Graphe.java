/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphe_java;

import graphe_java.Fenetre.TypeGraphe;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;

/**
 *
 * @author Romain
 */
public final class Vue_Graphe extends JPanel{
    
    Graph graph;
    Viewer viewer;
    View view;
    TypeGraphe type;
    
    public Vue_Graphe(){
        setVisible(true);
        setPreferredSize(new Dimension(600, 600));
        this.setLayout(new BorderLayout());
        graph = new SingleGraph("Graphe");
        graph.addAttribute("ui.stylesheet", "edge { fill-color: black; }");
        update();
        //graph.display();
    }
    
    public void update() {
        if (graph != null) {
            try {
                this.remove(viewer.getView(Viewer.DEFAULT_VIEW_ID));
            } catch (Exception ex) {
            }
            viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
            viewer.enableAutoLayout();
            view = viewer.addDefaultView(false);   // false indicates "no JFrame".
            this.add(view);
            view.revalidate();
        }
    }
    
    public void update2(){
        view.revalidate();
    }
    
    public void setGraphe(Graph g){
        this.graph = g;
    }
}
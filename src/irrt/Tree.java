/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package irrt;

import java.util.ArrayList;

/**
 *
 * @author javedhossain
 */
public class Tree {
    
    
    private ArrayList<Node> vertices;
    private ArrayList<ArrayList<Node>> children;
    private ArrayList<Node> parents;
    
    //static double dist = 0;
    //static Node closestNode = null;        
    
    public Tree(){
        vertices = new ArrayList<Node>();
        children = new ArrayList<ArrayList<Node>>();
        parents  = new ArrayList<Node>();
    }        
    
    public void addNode(Node n, Node parent){
        if(!vertices.contains(n)){
            vertices.add(n);
            int index = vertices.indexOf(n);
            parents.add(index, parent);
            children.add(index, new ArrayList<Node>());
        }
    }
    
    public void addChild(Node parent, Node child){
        int index = vertices.indexOf(parent);        
        if(!children.get(index).contains(child))            
            children.get(index).add(child);        
    }
    
    public ArrayList<Node> getChildren(Node n){
        int index = vertices.indexOf(n);
        if(index == -1) return null;
        else if(children.get(index).isEmpty()) return null;
        else return children.get(index);
    }
    
    public Node getParent(Node n){
        int index = vertices.indexOf(n);
        return parents.get(index);
    }
    
    public void changeParent(Node n, Node newParent){
        int index = vertices.indexOf(n);
        Node temp = parents.get(index);
        Node oldParent = new Node(temp.getX(), temp.getY());        
        parents.set(index, newParent);
        index = vertices.indexOf(oldParent);
        //children.get(index).remove(n);
    }
    
    public Node getNode(int index){
        
        if(index == -1) return null;
        
        if(index < vertices.size())
            return vertices.get(index);
        else
            return null;
    }
    
    public int getNodeCount(){
        return vertices.size();
    }
}

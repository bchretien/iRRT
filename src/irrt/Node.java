/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package irrt;

/**
 *
 * @author javedhossain
 */
public class Node {
    
    private int x;
    private int y;
    private double pathLengthFromRoot;
    private double distanceFromGoal;
    
    public Node(){
        x = -1;
        y = -1;
        pathLengthFromRoot = -1;
        distanceFromGoal = Double.POSITIVE_INFINITY;
    }
    
    public Node(int eks, int wye){
        x = eks;
        y = wye;
        pathLengthFromRoot = -1;
        distanceFromGoal = Double.POSITIVE_INFINITY;
    }
    
    public Node(int eks, int wye, double pathLength, double distFromGoal){
        x = eks;
        y = wye;
        pathLengthFromRoot = pathLength;
        distanceFromGoal = distFromGoal;
    }
    
    public void setCoordinate(int eks, int wye){
        x = eks;
        y = wye;
    }
    
    public void setPathLengthFromRoot(double pathLength){
        pathLengthFromRoot = pathLength;
    }
    
    public void setDistFromGoal(double dist){
        distanceFromGoal = dist;
    }
    
    public double getDistFromGoal(){
        return distanceFromGoal;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public double getPathLengthFromRoot(){
        return pathLengthFromRoot;
    }
    
    public String toString(){
        return "N = ("+x+","+y+")";
    }
    
}

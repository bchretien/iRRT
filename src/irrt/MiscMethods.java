/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package irrt;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;


import java.text.DecimalFormat;

import java.util.*;


/**
 *
 * @author 
 */
public class MiscMethods {
    
    double roundDecimal(double d) {
        	DecimalFormat df = new DecimalFormat("#.##");
		return Double.valueOf(df.format(d));
    }

    public int getTryGoalMode(javax.swing.JComboBox jComboBox_tryGoalMode) {
        String mode = jComboBox_tryGoalMode.getSelectedItem().toString();

        if (mode.equals("Never")) {
            return -1;
        } else if (mode.equals("1%")) {
            return 100;
        } else if (mode.equals("10%")) {
            return 10;
        } else if (mode.equals("20%")) {
            return 5;
        } else if (mode.equals("25%")) {
            return 4;
        } else if (mode.equals("33%")) {
            return 3;
        } else if (mode.equals("50%")) {
            return 2;
        } else if (mode.equals("Always")) {
            return 1;
        } else if (mode.equals("Adaptive")) {
            return 0;
        }

        return -1;
    }

    public Node getRandomState(BufferedImage image) {

        int x = (int) (Math.random() * image.getWidth());
        int y = (int) (Math.random() * image.getHeight());

        int black = new Color(0, 0, 0).getRGB();
        int rgb = image.getRGB(x, y);

        while (rgb == black) {
            x = (int) (Math.random() * image.getWidth());
            y = (int) (Math.random() * image.getHeight());
            rgb = image.getRGB(x, y);
        }

        return new Node(x, y);
    }

    public boolean goalReached(Node newNode, int goal_x, int goal_y, int goal_r) {
        int x = newNode.getX();
        int y = newNode.getY();

        double d = Math.sqrt((goal_x - x) * (goal_x - x) + (goal_y - y) * (goal_y - y));
        if (d <= goal_r) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Node> getPath(Node start, Node end, Tree tree) {
        ArrayList<Node> path = new ArrayList<Node>();
        path.add(end);
        Node n = end;
        while (true) {
            n = tree.getParent(n);
            path.add(n);
            if (n == start) {
                return path;
            }
        }
    }
    
    public double distanceBetween(Node n1, Node n2){
        int x1 = n1.getX();
        int y1 = n1.getY();
        int x2 = n2.getX();
        int y2 = n2.getY();
        return Math.sqrt(Math.pow((x1 - x2),2) + Math.pow((y1 - y2),2));
    }

    public double getPathLength(Node n, Node goal) {
        return n.getPathLengthFromRoot()+distanceBetween(n,goal);
    }

    public void highLightPath(Graphics2D g2D, BufferedImage image, ArrayList<Node> path, double edgeLength) {
        int x1, y1, x2, y2;
        int dotRadius = 4;
        Color color;
        
        double nodesInPath = path.size();
        double r_v = 200/(nodesInPath-1);
        double g_v = 150/(nodesInPath-1);
        
        switch ((int)edgeLength) {
            case 4:
                dotRadius = 4;
                break;
            case 5:
                dotRadius = 4;
                break;
            case 8:
                dotRadius = 6;
                break;
            case 10:
                dotRadius = 8;
                break;           
            case 15:
                dotRadius = 10;
                break;
            case 20:
                dotRadius = 10;
                break;
            case 25:
                dotRadius = 12;
                break;           
            case 99999:
                dotRadius = 12;
                break;            
        }
                
        double r = 200;
        double g = 0;
        double b = 0;

        for (int i = 0; i < path.size()-1; i++) {
            
            r = 200-((double)i*r_v);
            g = 0+((double)i*g_v);
            color = new Color((int)Math.ceil(r), (int)Math.ceil(g), (int)b);
            //color = new Color(200,0,0);
            
            x1 = path.get(i).getX();
            y1 = path.get(i).getY();

            x2 = path.get(i + 1).getX();
            y2 = path.get(i + 1).getY();
            
            g2D.setColor(color);            
            g2D.setStroke(new BasicStroke(3));
            g2D.drawLine(x1, y1, x2, y2);            
            g2D.setColor(color);           
        }        
    }
    
    public ArrayList<Node> getNearestNodes(Tree tree, Node node, double radius) {

        int x = node.getX();
        int y = node.getY();
        double dist;
        double pathLength;
        
        HashMap<Node, Double> nearestNodes = new HashMap<Node, Double>();
        
        for(int i = 0; i < tree.getNodeCount(); i++){
            dist = distanceBetween(tree.getNode(i),node);
            pathLength = tree.getNode(i).getPathLengthFromRoot();
            if(dist < radius)
                nearestNodes.put(tree.getNode(i), pathLength + dist);
        }
        
        HashMap<Node, Double> sortedNodes = new HashMap<Node, Double>();
        sortedNodes = sortHashMap(nearestNodes);
        
        ArrayList<Node> candidateNodes = new ArrayList<Node>();
        
        for (Node n : sortedNodes.keySet()){
            candidateNodes.add(n);
        }
        
        return candidateNodes;
    }
    
    private HashMap<Node, Double> sortHashMap(HashMap<Node, Double> input){
        
        Map<Node, Double> tempMap = new HashMap<Node, Double>();
        
        for (Node wsState : input.keySet()){
            tempMap.put(wsState,input.get(wsState));
        }

        List<Node> mapKeys = new ArrayList<Node>(tempMap.keySet());
        List<Double> mapValues = new ArrayList<Double>(tempMap.values());
        HashMap<Node, Double> sortedMap = new LinkedHashMap<Node, Double>();
        TreeSet<Double> sortedSet = new TreeSet<Double>(mapValues);
        Object[] sortedArray = sortedSet.toArray();
        
        int size = sortedArray.length;
        for (int i=0; i<size; i++){
            sortedMap.put(mapKeys.get(mapValues.indexOf(sortedArray[i])), 
                          (Double)sortedArray[i]);
        }
        return sortedMap;
}

    public Node getNearestNode(Tree tree, Node randNode) {

        int x = randNode.getX();
        int y = randNode.getY();

        double d = 0;
        double min = Double.MAX_VALUE;
        Node nearestNode = null;
        int vx = -1;
        int vy = -1;
        Node n;

        for (int i = 0; i < tree.getNodeCount(); i++) {
            n = tree.getNode(i);
            vx = n.getX();
            vy = n.getY();
            d = Math.sqrt(((vx - x) * (vx - x)) + ((vy - y) * (vy - y)));
            if (d < min) {
                min = d;
                nearestNode = n;
            }
        }
        return nearestNode;
    }
    

    
    public void drawLineSegment(Graphics2D g2D, BufferedImage image, Node nearestNode, Node newNode){
        
        int vx = nearestNode.getX();
        int vy = nearestNode.getY();
        int rx = newNode.getX();
        int ry = newNode.getY();               
                
        g2D.setColor(new Color(1, 1, 1));
        g2D.setStroke(new BasicStroke(0));
        g2D.drawLine(vx, vy, rx, ry);
        g2D.setColor(new Color(1, 1, 1));
    }
    
    public void removeLineSegment(Graphics2D g2D, BufferedImage image, Node nearestNode, Node newNode){
        
        int vx = nearestNode.getX();
        int vy = nearestNode.getY();
        int rx = newNode.getX();
        int ry = newNode.getY();               
        
        g2D.setColor(new Color(255, 255, 255));
        g2D.drawLine(vx, vy, rx, ry);
        g2D.setColor(new Color(255, 255, 255));
    }
    
    public void updateEdges(Graphics2D g2D, BufferedImage map, javax.swing.JPanel jp, Tree tree, Node start, Node goal){
        
        g2D.drawImage(map,0,0, jp);
        highLightPath(g2D, map, getPath(start,goal,tree), 0);
    }
    
    public boolean isValidExpansion(BufferedImage image, Node nearestNode, Node randomNode, double maxDist) {

        if (nearestNode == null) {
            return false;
        }

        int vx = nearestNode.getX();
        int vy = nearestNode.getY();
        int rx = randomNode.getX();
        int ry = randomNode.getY();

        if (vx == rx && vy == ry) {
            return false;
        }

        int black = new Color(0, 0, 0).getRGB();        
        int red = new Color(0, 0, 0).getRGB();
        
        double d;
        int rgb;

        if (vx > rx && vy > ry) {
            for (int x = rx; x <= vx; x++) {
                for (int y = ry; y <= vy; y++) {

                    d = Math.sqrt((vx - x) * (vx - x) + (vy - y) * (vy - y));

                    if (d < maxDist) {
                        if (((x - vx) / (vx - rx)) == ((y - vy) / (vy - ry))) {
                            rgb = image.getRGB(x, y);
                            if (rgb == black || rgb == red) {
                                return false;
                            }
                        }
                    }
                }
            }
        } else if (vx > rx && vy < ry) {
            for (int x = rx; x <= vx; x++) {
                for (int y = ry; y >= vy; y--) {

                    d = Math.sqrt((vx - x) * (vx - x) + (vy - y) * (vy - y));
                    if (d < maxDist) {
                        if (((x - vx) / (vx - rx)) == ((y - vy) / (vy - ry))) {
                            rgb = image.getRGB(x, y);
                            if (rgb == black || rgb == red) {
                                return false;
                            }
                        }
                    }
                }
            }
        } else if (vx < rx && vy > ry) {
            for (int x = rx; x >= vx; x--) {
                for (int y = ry; y <= vy; y++) {

                    d = Math.sqrt((vx - x) * (vx - x) + (vy - y) * (vy - y));

                    if (d < maxDist) {
                        if (((x - vx) / (vx - rx)) == ((y - vy) / (vy - ry))) {
                            rgb = image.getRGB(x, y);
                            if (rgb == black || rgb == red) {
                                return false;
                            }
                        }
                    }
                }
            }
        } else if (vx < rx && vy < ry) {
            for (int x = rx; x >= vx; x--) {
                for (int y = ry; y >= vy; y--) {

                    d = Math.sqrt((vx - x) * (vx - x) + (vy - y) * (vy - y));
                    if (d < maxDist) {
                        if (((x - vx) / (vx - rx)) == ((y - vy) / (vy - ry))) {
                            rgb = image.getRGB(x, y);
                            if (rgb == black || rgb == red) {
                                return false;
                            }
                        }
                    }
                }
            }
        } else if (vx == rx && vy > ry) {
            for (int y = ry; y <= vy; y++) {

                d = Math.sqrt((vy - y) * (vy - y));

                if (d < maxDist) {
                    rgb = image.getRGB(vx, y);
                    if (rgb == black || rgb == red) {
                        return false;
                    }
                }
            }
        } else if (vx == rx && vy < ry) {
            for (int y = ry; y >= vy; y--) {

                d = Math.sqrt((vy - y) * (vy - y));

                if (d < maxDist) {
                    rgb = image.getRGB(vx, y);
                    if (rgb == black || rgb == red) {
                        return false;
                    }
                }
            }
        } else if (vx > rx && vy == ry) {
            for (int x = rx; x <= vx; x++) {

                d = Math.sqrt((vx - x) * (vx - x));
                if (d < maxDist) {
                    rgb = image.getRGB(x, vy);
                    if (rgb == black || rgb == red) {
                        return false;
                    }
                }
            }
        } else if (vx < rx && vy == ry) {
            for (int x = rx; x >= vx; x--) {
                d = Math.sqrt((vx - x) * (vx - x));

                if (d < maxDist) {
                    rgb = image.getRGB(x, vy);
                    if (rgb == black || rgb == red) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public Node goTowardsNode(Node nearestNode, Node randomNode, double maxDist) {

        int vx = nearestNode.getX();
        int vy = nearestNode.getY();
        int rx = randomNode.getX();
        int ry = randomNode.getY();

        Node newNode = new Node(rx, ry);

        double d = distanceBetween(nearestNode, randomNode);

        if (d <= maxDist) {
            
            newNode.setPathLengthFromRoot(nearestNode.getPathLengthFromRoot() + d);
            return newNode;
        }

        newNode.setPathLengthFromRoot(nearestNode.getPathLengthFromRoot() + maxDist);

        double distFromLine = 0;
        double distFromNearestNode = 0;

        int num = 0;
        double den = 0;

        if (vx > rx && vy > ry) {
            for (int x = rx; x <= vx; x++) {
                for (int y = ry; y <= vy; y++) {

                    num = Math.abs((x - vx) * (vy - ry) - (y - vy) * (vx - rx));
                    den = Math.sqrt((vx - rx) * (vx - rx) + (vy - ry) * (vy - ry));

                    distFromLine = num / den;
                    distFromNearestNode = Math.sqrt((x - vx) * (x - vx) + (y - vy) * (y - vy));

                    if (distFromLine <= 2 && distFromNearestNode <= maxDist && distFromNearestNode > maxDist - 2) {                        
                        newNode.setCoordinate(x, y);
                        return newNode;
                    }
                }
            }
        } else if (vx > rx && vy < ry) {
            for (int x = rx; x <= vx; x++) {
                for (int y = ry; y >= vy; y--) {

                    num = Math.abs((x - vx) * (vy - ry) - (y - vy) * (vx - rx));
                    den = Math.sqrt((vx - rx) * (vx - rx) + (vy - ry) * (vy - ry));

                    distFromLine = num / den;
                    distFromNearestNode = Math.sqrt((x - vx) * (x - vx) + (y - vy) * (y - vy));

                    if (distFromLine <= 2 && distFromNearestNode <= maxDist && distFromNearestNode > maxDist - 2) {                        
                        newNode.setCoordinate(x, y);
                        return newNode;
                    }
                }
            }
        } else if (vx < rx && vy > ry) {
            for (int x = rx; x >= vx; x--) {
                for (int y = ry; y <= vy; y++) {

                    num = Math.abs((x - vx) * (vy - ry) - (y - vy) * (vx - rx));
                    den = Math.sqrt((vx - rx) * (vx - rx) + (vy - ry) * (vy - ry));

                    distFromLine = num / den;
                    distFromNearestNode = Math.sqrt((x - vx) * (x - vx) + (y - vy) * (y - vy));

                    if (distFromLine <= 2 && distFromNearestNode <= maxDist && distFromNearestNode > maxDist - 2) {                        
                        newNode.setCoordinate(x, y);
                        return newNode;
                    }
                }
            }
        } else if (vx < rx && vy < ry) {
            for (int x = rx; x >= vx; x--) {
                for (int y = ry; y >= vy; y--) {

                    num = Math.abs((x - vx) * (vy - ry) - (y - vy) * (vx - rx));
                    den = Math.sqrt((vx - rx) * (vx - rx) + (vy - ry) * (vy - ry));

                    distFromLine = num / den;
                    distFromNearestNode = Math.sqrt((x - vx) * (x - vx) + (y - vy) * (y - vy));

                    if (distFromLine <= 2 && distFromNearestNode <= maxDist && distFromNearestNode > maxDist - 2) {                        
                        newNode.setCoordinate(x, y);
                        return newNode;
                    }
                }
            }
        } else if (vx == rx && vy > ry) {
            for (int y = ry; y <= vy; y++) {

                num = 0;
                den = Math.sqrt((vy - ry) * (vy - ry));

                distFromNearestNode = Math.sqrt((y - vy) * (y - vy));

                if (distFromNearestNode <= maxDist && distFromNearestNode > maxDist - 2) {                    
                    newNode.setCoordinate(vx, y);
                    return newNode;
                }
            }
        } else if (vx == rx && vy < ry) {
            for (int y = ry; y >= vy; y--) {

                num = 0;
                den = Math.sqrt((vy - ry) * (vy - ry));

                distFromNearestNode = Math.sqrt((y - vy) * (y - vy));

                if (distFromNearestNode <= maxDist && distFromNearestNode > maxDist - 2) {                    
                    newNode.setCoordinate(vx, y);
                    return newNode;
                }
            }
        } else if (vx > rx && vy == ry) {
            for (int x = rx; x <= vx; x++) {

                den = Math.sqrt((vx - rx) * (vx - rx));

                distFromNearestNode = Math.sqrt((x - vx) * (x - vx));

                if (distFromNearestNode <= maxDist && distFromNearestNode > maxDist - 2) {                    
                    newNode.setCoordinate(x, vy);
                    return newNode;
                }
            }
        } else if (vx < rx && vy == ry) {
            for (int x = rx; x >= vx; x--) {
                den = Math.sqrt((vx - rx) * (vx - rx));

                distFromNearestNode = Math.sqrt((x - vx) * (x - vx));

                if (distFromNearestNode <= maxDist && distFromNearestNode > maxDist - 2) {                    
                    newNode.setCoordinate(x, vy);
                    return newNode;
                }
            }
        }
        return null;
    }
}

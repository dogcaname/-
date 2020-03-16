package edu.neu.coe.info6205.life.GA;

import edu.neu.coe.info6205.life.base.Point;

import java.util.HashSet;

public class Pattern implements Comparable<Pattern>{
    private int[] x = new int[GA.points];
    private int[] y = new int[GA.points];
    private long fitness;
    private HashSet<Point> setOfpoint;
    public Pattern(){
        super();
        setOfpoint = new HashSet<>();
    }
    public Pattern(Pattern copy){
        super();
        setOfpoint = new HashSet<>();
        for(int i = 0; i < GA.points; i++){
            x[i] = copy.getX(i);
            y[i] = copy.getY(i);
        }
    }
    public int getX(int i){
        return x[i];
    }
    public void setX(int i, int value){
        this.x[i] = value;
    }
    public int getY(int i){
        return y[i];
    }
    public void setY(int i, int value){
        this.y[i] = value;
    }
    public void setFitness(long fitness){
        this.fitness = fitness;
    }
    public long getFitness(){
        return fitness;
    }
    public HashSet<Point> getSetOfpoint(){
        return setOfpoint;
    }
    public void resetSetOfpoint(){
        setOfpoint.clear();
        for(int i = 0; i < GA.points; i++){
            Point point = new Point(x[i], y[i]);
            setOfpoint.add(point);
        }
    }
    @Override
    public int compareTo(Pattern pattern) {
        return (int) (this.getFitness() - pattern.getFitness());
    }
}

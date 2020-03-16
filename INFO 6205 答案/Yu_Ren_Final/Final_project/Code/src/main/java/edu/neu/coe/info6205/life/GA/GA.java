package edu.neu.coe.info6205.life.GA;

import edu.neu.coe.info6205.life.UI.GameOfLife;
import edu.neu.coe.info6205.life.base.Game;
import edu.neu.coe.info6205.life.base.Point;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class GA {
    public static int points = 10; // the number of points individually
    public static int range = 5; // the range of x and y
    public static int initialpopulation = 1000; // the number of population
    public static int maxgeneration = 20; // the maximum number of generation
    public static double rateofbreed = 0.5; // Proportion of organisms that survive and breed
    public static double rateofmutation = 0.5; // Proportion of mutation

    // Initialise the population randomly
    public static void InitPopulationRandomly(List<Pattern> initpop){
        Random random = new Random();
        for(int i = 0; i < initialpopulation; i++){
            Pattern pattern = new Pattern();
            for(int j = 0; j < points; j++){
                int x;
                int y;
                // generate a pattern without duplicate point
                while(true){
                    Boolean x_plusminus = random.nextBoolean(); // Judge plus or minus for the random coordinate
                    int x_value = random.nextInt(range);  // the value
                    x = x_plusminus? x_value : 0 - x_value;
                    Boolean y_plusminus = random.nextBoolean(); // Judge plus or minus for the random coordinate
                    int y_value = random.nextInt(range);  // the value
                    y = y_plusminus? y_value : 0 - y_value;
                    Point point = new Point(x, y);
                    //Judge if it is duplicate
                    if(!pattern.getSetOfpoint().contains(point)){
                        pattern.getSetOfpoint().add(point);
                        break;
                    }
                }
                pattern.setX(j, x);
                pattern.setY(j, y);
            }
            initpop.add(pattern);
        }
    }

    // Initialise the population with previous data
    public static void InitPopulationWithdata(List<Pattern> initpop, String filename, int points){
        GA.points = points;
        String thisline = null;
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            while((thisline = br.readLine()) != null){
                Pattern pattern = new Pattern();
                String[] data = thisline.split(":");
                String[] fields = data[1].split(",");
                for(int i = 0; i < fields.length; i++){
                    String[] cmp = fields[i].split(" ");
                    pattern.setX(i, Integer.parseInt(cmp[1]));
                    pattern.setY(i, Integer.parseInt(cmp[2]));
                }
                pattern.resetSetOfpoint();
                initpop.add(pattern);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // Mutate the pop
    public static List<Pattern> Mutation(List<Pattern> popForbreed){
        List<Pattern> popAfterbreed = new ArrayList<>();
        Random random = new Random();
        for(Pattern pattern : popForbreed){
            int randomnum = random.nextInt(1000);

            // Mutate randomly with rate of mutation
            // Change x of point
            if(randomnum < 1000 * rateofmutation / 4){
                int Changedpoint = random.nextInt(points);
                int x;
                while(true){
                    Boolean x_plusminus = random.nextBoolean(); // Judge plus or minus for the random coordinate
                    int x_value = random.nextInt(range);  // the value
                    x = x_plusminus? x_value : 0 - x_value;
                    Point point = new Point(x, pattern.getY(Changedpoint));
                    if(!pattern.getSetOfpoint().contains(point)) break;
                }
                Pattern newpattern = new Pattern(pattern);
                newpattern.setX(Changedpoint, x);
                newpattern.resetSetOfpoint();
                popAfterbreed.add(newpattern);
            }

            // Change y of point
            if(randomnum >= 1000 * rateofmutation / 4 && randomnum < 1000 * rateofmutation / 2){
                int Changedpoint = random.nextInt(points);
                int y;
                while(true){
                    Boolean y_plusminus = random.nextBoolean(); // Judge plus or minus for the random coordinate
                    int y_value = random.nextInt(range);  // the value
                    y = y_plusminus? y_value : 0 - y_value;
                    Point point = new Point(pattern.getX(Changedpoint), y);
                    if(!pattern.getSetOfpoint().contains(point)) break;
                }
                Pattern newpattern = new Pattern(pattern);
                newpattern.setY(Changedpoint, y);
                newpattern.resetSetOfpoint();
                popAfterbreed.add(newpattern);
            }

            // Change x and y of point
            if(randomnum >= 1000 * rateofmutation / 2 && randomnum < 1000 * rateofmutation){
                int Changedpoint = random.nextInt(points);
                int x;
                int y;
                while(true){
                    Boolean x_plusminus = random.nextBoolean(); // Judge plus or minus for the random coordinate
                    int x_value = random.nextInt(range);  // the value
                    x = x_plusminus? x_value : 0 - x_value;
                    Boolean y_plusminus = random.nextBoolean(); // Judge plus or minus for the random coordinate
                    int y_value = random.nextInt(range);  // the value
                    y = y_plusminus? y_value : 0 - y_value;
                    Point point = new Point(x, y);
                    if(!pattern.getSetOfpoint().contains(point)) break;
                }
                Pattern newpattern = new Pattern(pattern);
                newpattern.setX(Changedpoint, x);
                newpattern.setY(Changedpoint, y);
                newpattern.resetSetOfpoint();
                popAfterbreed.add(newpattern);
            }
        }

        return popAfterbreed;
    }

    // Select the best part with rate of breed
    public static void Selection(List<Pattern> pop){
        Collections.sort(pop);
//        System.out.println("\n\n\n\n\n" + popforselection.size() + "\n" + popforselection.size() * rateofbreed + "\n\n\n\n\n");
        int len = (int) (pop.size() * rateofbreed);
        for(int i = 0; i < len; i++){
            pop.remove(0);
        }
    }

    // Find the fitness of every pattern
    public static void Fitness(List<Pattern> pop)
    {
        // Get the generation though Game
        for(Pattern pattern : pop){
            Game.Behavior generation = Game.run(0L, Expression(pattern));
            pattern.setFitness(generation.generation);
        }
    }

    // Express phenotype from genotype
    public static String Expression(Pattern pattern){
        String result = "";
        for(int i = 0; i < GA.points; i++){
            if(i == GA.points - 1){
                result = result + pattern.getX(i) + " " + pattern.getY(i);
            }
            else{
                result = result + pattern.getX(i) + " " + pattern.getY(i) + ", ";
            }
        }
        return result;
    }

    // Evolution from generations
    public static List<Pattern> Evolution(){
        List<String> record = new ArrayList<>();

        List<Pattern> initpop = new ArrayList<>(); //A list for initialising population
        InitPopulationRandomly(initpop);
//        InitPopulationWithdata(initpop, "Goodpop_100points.txt", 100);
        RecordInitialPop(initpop);
        Fitness(initpop);
        List<Pattern> Parent = initpop;
        List<Pattern> Child = null;
        for(int i = 0; i < GA.maxgeneration; i++){
            if(i == 0){

                long aveGeneration = 0;
                for(Pattern pattern : Parent){
                    aveGeneration += pattern.getFitness();
                }
                aveGeneration = aveGeneration / Parent.size();
                record.add((i+1) + " // " + aveGeneration);

                Child = Mutation(Parent);
                Fitness(Child);
                Selection(Parent);
            }
            else{

                long aveGeneration = 0;
                for(Pattern pattern : Parent){
                    aveGeneration += pattern.getFitness();
                }
                aveGeneration = aveGeneration / Parent.size();
                record.add((i+1) + " // " + aveGeneration);

                Parent.addAll(Child);
                Child = Mutation(Parent);
                Fitness(Child);
                Selection(Parent);
            }
            if(Parent.size() + Child.size() < GA.initialpopulation/10){
//                Parent.addAll(Child);
                break;
            }
        }
        Parent.addAll(Child);
        Collections.sort(Parent);
        RecordFianlPop(Parent);

        try(BufferedWriter out = new BufferedWriter(new FileWriter("process.txt"))){
            for(String re : record){
                out.write(re);
                out.newLine();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return Parent;
    }

    // Record initial population
    public static void RecordInitialPop(List<Pattern> pop){
        try(BufferedWriter out = new BufferedWriter(new FileWriter("Initial.txt"))){
            out.write(GA.initialpopulation + " Initial Population:");
            out.newLine();
            int i = 1;
            for(Pattern pattern : pop){
                out.write("\t\t" + "No." + i + ": " + Expression(pattern));
                out.newLine();
                i++;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // Record final result
    public static void RecordFianlPop(List<Pattern> pop){
        try(BufferedWriter out = new BufferedWriter(new FileWriter("Result.txt"))){
            out.write("Points = " + GA.initialpopulation);
            out.newLine();
            out.write("Range = " + GA.range);
            out.newLine();
            out.write("Initialpopulation = " + GA.initialpopulation);
            out.newLine();
            out.write("Maxgeneration = " + GA.maxgeneration);
            out.newLine();
            out.write("Rate of breed = " + GA.rateofbreed);
            out.newLine();
            out.write("Rate of mutation = " + GA.rateofmutation);
            out.newLine();
            int i = 1;
            for(Pattern pattern : pop){
                out.write("\t\t" + "No." + i + "   Generations: " + pattern.getFitness() + "\n\t\tPattern: " + Expression(pattern));
                out.newLine();
                i++;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){

        List<Pattern> result = Evolution();
        List<String> datas = new ArrayList<>();
        for(int i = result.size() - 1; i > result.size() - 7; i --){
            datas.add(" " + Expression(result.get(i)));
        }

        String title = "Game of life";
        int i = 0;
        for(String data : datas){
            JFrame frame = new JFrame();
            frame.setTitle(title);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setLocationRelativeTo(null);
            frame.setLocation(500 * (i % 3), 500 * (i / 3));
            frame.setAlwaysOnTop(true);
            GameOfLife gol = new GameOfLife();
            frame.add(gol);
            frame.pack();
            frame.setVisible(true);

            gol.start(data);
            i++;
        }
    }

    //


}

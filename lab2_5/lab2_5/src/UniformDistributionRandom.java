
import java.util.Random;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class UniformDistributionRandom implements RandomGenerable{
    private double a, b;
    private Random rnd;
    
    UniformDistributionRandom(double a, double b)
    {
        this.a = a;
        this.b = b;
        this.rnd = new Random();
    }
    
    public double GenerateRandomNumber()
    {
        return a+(b-a)*rnd.nextDouble();
    }
} 

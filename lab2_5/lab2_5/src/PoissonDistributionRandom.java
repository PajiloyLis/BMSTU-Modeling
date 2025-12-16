
import java.util.Random;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class PoissonDistributionRandom implements RandomGenerable {
    private double lambda;
    private Random rnd;
    
    PoissonDistributionRandom(double lambda)
    {
        if(lambda <= 0)
            throw new InvalidPoissonDistributionLambdaParameterException("Параметр lambda должен быть больше нуля");
        this.lambda=lambda;
        rnd = new Random();
    }
    
    public double GenerateRandomNumber()
    {
        double L = Math.exp(-lambda);
        int k = 0;
        double p = 1.0;

        do {
            k++;
            p *= rnd.nextDouble();
        } while (p > L);

        return k - 1;
    }
}

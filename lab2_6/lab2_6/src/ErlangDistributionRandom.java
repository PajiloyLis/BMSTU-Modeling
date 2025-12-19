
import java.util.Random;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class ErlangDistributionRandom implements RandomGenerable {
    private int k;
    private double lambda;
    private Random rnd;
    
    ErlangDistributionRandom(int k, double lambda)
    {
        if(k <= 0)
            throw new InvalidErlangDistributionKParameterException("Параметр K должен быть целым числом больше нуля");
        if(lambda <= 0)
            throw new InvalidErlangDistributionLambdaParameterException("Параметр lambda должен быть больше нуля");
        this.lambda = lambda;
        this.k = k;
        rnd = new Random();
    }
    
    public double GenerateRandomNumber()
    {
        double sum = 0;
        for(int i = 0;i < k; ++i)
            sum+=(1-rnd.nextDouble());
        
        return -sum/(k*lambda);
    }
}

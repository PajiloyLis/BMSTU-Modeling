
import java.util.Random;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class ExponentialDistributionRandom implements RandomGenerable {
    private double lambda;
    private Random rnd;
    
    ExponentialDistributionRandom(double lambda)
    {
        if(lambda <= 0)
            throw new InvalidExponentialDistributionLambdaParameterException("Параметр lambda должен быть больше нуля");
                    
        this.lambda=lambda;
        rnd = new Random();
    }
    
    public double GenerateRandomNumber()
    {
        return -Math.log(1-rnd.nextDouble())/lambda;
    }
    
}

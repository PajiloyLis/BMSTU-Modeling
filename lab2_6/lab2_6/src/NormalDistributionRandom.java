
import java.util.Random;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class NormalDistributionRandom implements RandomGenerable {
    private double mu, variance;
    private Random rnd;
    
    NormalDistributionRandom(double mu, double var)
    {
        if(var <= 0)
            throw new InvalidNormalDistributionVarianceParameterException("Диспресия должа быть больше нуля");
        
        this.mu = mu;
        this.variance = var;
        rnd = new Random();
    }
    
    public double GenerateRandomNumber()
    {
        int n = 50;
        double sum = 0;
        for(int i = 0; i < n; ++i)
            sum += rnd.nextDouble();
        return Math.sqrt(variance*12/n)*(sum-n/2)+mu;
    }
}

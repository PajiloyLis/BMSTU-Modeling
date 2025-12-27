
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class Statistic {
    private ArrayList<Integer> operatorRejected;
    private ArrayList<Integer> operatorProcessed;
    private ArrayList<Double> operatorWorkingTime;
    
    private ArrayList<Integer> kitchenProcessed;
    private ArrayList<Double> kitchenWorkingTime;
    
    private ArrayList<Integer> releaseProcessed;
    private ArrayList<Double> releaseWorkingTime;
    
    Statistic(int operatorsCount, int kitchenCount, int releaseCount)
    {
        operatorRejected = new ArrayList<>();
        operatorProcessed = new ArrayList<>();
        operatorWorkingTime = new ArrayList<>();
        for(int i = 0; i < operatorsCount; ++i)
        {
            operatorRejected.add(0);
            operatorProcessed.add(0);
            operatorWorkingTime.add(0.0);
        }
        
        kitchenProcessed = new ArrayList<>();
        kitchenWorkingTime = new ArrayList<>();
        for(int i = 0; i < kitchenCount; ++i)
        {
            kitchenProcessed.add(0);
            kitchenWorkingTime.add(0.0);
        }
        
        releaseProcessed = new ArrayList<>();
        releaseWorkingTime = new ArrayList<>();
        for(int i = 0; i < releaseCount; ++i)
        {
            releaseProcessed.add(0);
            releaseWorkingTime.add(0.0);
        }
    }
    
    public void AddOperatorRejectedCount(int index, int add){
        operatorRejected.set(index, operatorRejected.get(index)+add);
    }
    
    public void AddOperatorProcessedCount(int index, int add){
        operatorProcessed.set(index, operatorProcessed.get(index)+add);
    }
    
    public void AddOperatorWorkingTime(int index, double add){
        operatorWorkingTime.set(index, operatorWorkingTime.get(index)+add);
    }
    
    public void AddKitchenProcessedCount(int index, int add){
        kitchenProcessed.set(index, kitchenProcessed.get(index)+add);
    }
    
    public void AddKitchenWorkingTime(int index, double add){
        kitchenWorkingTime.set(index, kitchenWorkingTime.get(index)+add);
    }
    
    public void AddReleaseProcessedCount(int index, int add){
        releaseProcessed.set(index, releaseProcessed.get(index)+add);
    }
    
    public void AddReleaseWorkingTime(int index, double add){
        releaseWorkingTime.set(index, releaseWorkingTime.get(index)+add);
    }
    
    public double GetOperatorRejectProbability(int index)
    {
        return (double)operatorRejected.get(index)/(operatorRejected.get(index)+operatorProcessed.get(index));
    }
    
    public double GetOperatorsRejectProbbility()
    {
        int processedSum = 0, rejectedSum = 0;
        for(int i = 0; i < operatorProcessed.size(); ++i)
        {
            processedSum += operatorProcessed.get(i);
            rejectedSum += operatorRejected.get(i);
        }
        return (double)rejectedSum/(rejectedSum+processedSum);
    }
    
    public int GetOperatorRejectedCount(int index){
        return operatorRejected.get(index);
    }
    
    public int GetOperatorProcessedCount(int index){
        return operatorProcessed.get(index);
    }
    
    public double GetOperatorWorkingTime(int index){
        if(operatorProcessed.get(index) == 0)
            return 0.0;
        return operatorWorkingTime.get(index);
    }
    
    public double GetOperatorAverageWorkingTime(int index){
        if(operatorProcessed.get(index) == 0)
            return 0.0;
        return operatorWorkingTime.get(index)/operatorProcessed.get(index);
    }
    
    public int GetOperatorsProcessed()
    {
        int sum = 0;
        for(int i = 0; i < operatorProcessed.size(); ++i)
            sum += operatorProcessed.get(i);
        return sum;
    }
    
    public double GetOperatorsAverageWorkingTime()
    {
        int processedSum = 0;
        for(int i = 0; i < operatorProcessed.size(); ++i)
            processedSum += operatorProcessed.get(i);
        double workingTimeSum = 0;
        for(int i = 0; i < operatorWorkingTime.size(); ++i)
            workingTimeSum += operatorWorkingTime.get(i);
        return workingTimeSum/processedSum;
    }
    
    public int GetKitchenProcessedCount(int index){
        return kitchenProcessed.get(index);
    }
    
    public double GetKitchenWorkingTime(int index){
        if(kitchenProcessed.get(index) == 0)
            return 0.0;
        return kitchenWorkingTime.get(index);
    }
    
    public double GetKitchenAverageWorkingTime(int index){
        if(kitchenProcessed.get(index) == 0)
            return 0.0;
        return kitchenWorkingTime.get(index)/operatorProcessed.get(index);
    }
        
    public double GetKitchensAverageWorkingTime()
    {
        int processedSum = 0;
        for(int i = 0; i < kitchenProcessed.size(); ++i)
            processedSum += kitchenProcessed.get(i);
        double workingTimeSum = 0;
        for(int i = 0; i < kitchenWorkingTime.size(); ++i)
            workingTimeSum += kitchenWorkingTime.get(i);
        return workingTimeSum/processedSum;
    }
    
    public int GetReleaseProcessedCount(int index){
        return releaseProcessed.get(index);
    }
    
    public double GetReleaseWorkingTime(int index){
        if(releaseProcessed.get(index) == 0)
            return 0.0;
        return releaseWorkingTime.get(index);
    }
    
    public double GetReleaseAverageWorkingTime(int index){
        return releaseWorkingTime.get(index)/operatorProcessed.get(index);
    }
    
    
}

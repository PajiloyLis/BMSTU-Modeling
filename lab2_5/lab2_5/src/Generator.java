/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class Generator {
    private RandomGenerable rnd;
    private int requestsCount;
    private double lastGeneratedTime;
    private double nextGenerateTime;
    private int generatedRequestsCount;
    
    Generator(RandomGenerable rnd, int requestsCount)
    {
        this.rnd= rnd;
        this.requestsCount = requestsCount;
        lastGeneratedTime = 0;
        nextGenerateTime = 0;
        generatedRequestsCount = 0;
    }
    
    public double GetLastGeneratedTime()
    {
        return lastGeneratedTime;
    }
    
    public void SetLastGeneratedTime(double value)
    {
        if(value < 0)
            throw new IllegalArgumentException("lastGeneratedTime value must be greater or equal than 0");
        lastGeneratedTime = value;
    }
    
    public double GetNextGenerateTime()
    {
        return nextGenerateTime;
    }
    
    public void SetNextGenerateTime(double value)
    {
        if(value < 0)
            throw new IllegalArgumentException("nextGenerateTime value must be greater or equal than 0");
        nextGenerateTime = value;
    }
    
    public int GetGeneratedRequestsCount()
    {
        return generatedRequestsCount;
    }
    
    public void SetGeneratedRequestsCount(int value)
    {
        if(value < 0)
            throw new IllegalArgumentException("generatedRequestsCount value must be greater or equal than 0");
        generatedRequestsCount = value;
    }
    
    public double GetTime()
    {
        return rnd.GenerateRandomNumber();
    }
    
    public int GetRequestsCount()
    {
        return requestsCount;
    }
}

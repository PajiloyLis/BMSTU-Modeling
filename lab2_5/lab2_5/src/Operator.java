/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class Operator {
    private RandomGenerable rnd;
    private boolean isBusy;
    private int lastOperationsTimeEnd;
    private int nextOperationsTimeEnd;
    private int rejectedCount;
    
    Operator(RandomGenerable rnd)
    {
        this.rnd = rnd;
        isBusy = false;
        lastOperationsTimeEnd = 0;
        nextOperationsTimeEnd = 0;
        rejectedCount = 0;
    }
     
    public int GetLastOperationsTimeEnd()
    {
        return lastOperationsTimeEnd;
    }
    
    public void SetLastOperationsTimeEnd(int value)
    {
        if(value < 0)
            throw new IllegalArgumentException("lastGeneratedTime value must be greater or equal than 0");
        lastOperationsTimeEnd = value;
    }
    
    public int GetNextOperationsTimeEnd()
    {
        return nextOperationsTimeEnd;
    }
    
    public void SetNextOperationsTimeEnd(int value)
    {
        if(value < 0)
            throw new IllegalArgumentException("lastGeneratedTime value must be greater or equal than 0");
        nextOperationsTimeEnd = value;
    }
    
    public boolean GetIsBusy()
    {
        return isBusy;
    }
    
    public void SetIsBusy(boolean value)
    {
        isBusy = value;
    }
    
    public int GetRejectedCount()
    {
        return rejectedCount;
    }
    
    public void SetRejectedCount(int value)
    {
        if(value < 0)
            throw new IllegalArgumentException("lastGeneratedTime value must be greater or equal than 0");
        rejectedCount = value;
    }
    
    public double GetTime()
    {
        return rnd.GenerateRandomNumber();
    }
}

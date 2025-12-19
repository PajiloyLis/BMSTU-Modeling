/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class Service {
    private RandomGenerable rnd;
    private boolean isBusy;
    private double lastServiceTimeEnd;
    private double lastServiceTimeStart;
    
    Service(RandomGenerable rnd)
    {
        this.rnd = rnd;
        lastServiceTimeEnd = 0;
        lastServiceTimeStart = 0;
        isBusy = false;
    }
    
    public double GetLastServiceTimeEnd()
    {
        return lastServiceTimeEnd;
    }
    
    public void SetLastServiceTimeEnd(double value)
    {
        if(value < 0)
            throw new IllegalArgumentException("lastServiceTimeEnd value must be greater or equal than 0");
        lastServiceTimeEnd = value;
    }
    
    public double GetLastServiceTimeStart()
    {
        return lastServiceTimeStart;
    }
    
    public void SetLastServiceTimeStart(double value)
    {
        if(value < 0)
            throw new IllegalArgumentException("lastServiceTimeStart value must be greater or equal than 0");
        lastServiceTimeStart = value;
    }
    
    public boolean GetIsBusy()
    {
        return isBusy;
    }
    
    public void SetIsBusy(boolean value)
    {
        isBusy = value;
    }
    
    public double GetTime()
    {
        return rnd.GenerateRandomNumber();
    }
}

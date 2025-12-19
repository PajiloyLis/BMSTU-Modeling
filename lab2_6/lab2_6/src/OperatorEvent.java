/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class OperatorEvent extends SimulationEvent{
    private int operatorNumber;
    OperatorEvent(int operatorNumber, EventType type)
    {
        super(type);
        
        this.operatorNumber = operatorNumber;
    }
    
    public int GetOperatorNumber()
    {
        return operatorNumber;
    }
    
    public void SetOperatorNumber(int value)
    {
        if(operatorNumber < 0)
            throw new IllegalArgumentException("Operator number must be more than zero");
        this.operatorNumber=value;
    }
}

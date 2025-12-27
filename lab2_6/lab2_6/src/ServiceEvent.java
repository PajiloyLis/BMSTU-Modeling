/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class ServiceEvent extends SimulationEvent{
    private int serviceNumber;
    ServiceEvent(int serviceNumber, EventType type)
    {
        super(type);
        
        this.serviceNumber = serviceNumber;
    }
    
    public int GetServiceNumber()
    {
        return serviceNumber;
    }
}

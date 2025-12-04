/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class SimulationEvent {
    private int eventId;
    private EventType type;
    
    SimulationEvent(int id, EventType type)
    {
        this.eventId = id;
        this.type = type;
    }
    
    public int id()
    {
        return eventId;
    }
    
    public EventType type(){
        return type;
    }
}

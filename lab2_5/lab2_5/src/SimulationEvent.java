/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
abstract public class SimulationEvent {
    private EventType type;
    
    SimulationEvent(EventType type)
    {
        this.type = type;
    }
    
    public EventType type(){
        return type;
    }
}

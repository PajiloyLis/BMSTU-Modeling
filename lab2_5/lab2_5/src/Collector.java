
import java.util.LinkedList;
import java.util.Queue;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class Collector {
    private Queue<Double> queue;
    
    Collector()
    {
        queue = new LinkedList<>();
    }
    
    public int GetQueueLength()
    {
        return queue.size();
    }
    
    public void offer(double value)
    {
        queue.offer(value);
    }
    
    public double poll()
    {
        return queue.poll();
    }
}

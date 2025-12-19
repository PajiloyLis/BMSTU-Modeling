
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
    private int maxQueueLength;
    Collector(int maxQueueLength)
    {
        queue = new LinkedList<>();
        this.maxQueueLength=maxQueueLength;
    }
    
    public int GetQueueLength()
    {
        return queue.size();
    }
    
    public void Offer(double value)
    {
        queue.offer(value);
    }
    
    public double Poll()
    {
        return queue.poll();
    }
    
    public int GetMaxQueueLength()
    {
        return maxQueueLength;
    }
}

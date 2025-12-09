
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Queue;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class Simulator {
    private RandomGenerable generator, service;
    private int requestsCount;
    private double rejectProbability, timeStep;
    private boolean rejectedReturn;
    
    Simulator(RandomGenerable generator, RandomGenerable service, int requestsCount, double rejectProbability, double timeStep, boolean rejectedReturn)
    {
        this.generator = generator;
        this.service = service;
        this.rejectProbability = rejectProbability;
        this.rejectedReturn = rejectedReturn;
        this.requestsCount = requestsCount;
        this.timeStep = timeStep;
    }
    
    public int eventSimulation()
    {
        int maxQueueLen=0, processedRequests=0, curQueueLength = 0;
        NavigableMap<Double, SimulationEvent> queueEvents = new TreeMap<>();
        NavigableMap<Double, SimulationEvent> serviceEvents = new TreeMap<>();
        for(int i =0; i < this.requestsCount; ++i)
        {
            queueEvents.put(generator.GenerateRandomNumber(), new SimulationEvent(i, EventType.TOQUEUE));
        }
        
//        NavigableMap<Double, ArrayList<SimulationEvent>> newEvents = new TreeMap<>();
//        double maxGoToServiceTime = 0;
//        for(Double time: events.keySet())
//        {
//            for(int i = 0; i < events.get(time).size(); ++i){
//                double firstServiceTime = Math.max(time, maxGoToServiceTime) + service.GenerateRandomNumber();
//                maxGoToServiceTime = firstServiceTime;
//                if(!events.containsKey(firstServiceTime)){
//                    newEvents.put(firstServiceTime, new ArrayList<>());
//                    newEvents.get(firstServiceTime).addLast(new SimulationEvent(events.get(time).get(i).id(), EventType.SERVICEEND));
//                }
//                else
//                {
//                    events.get(firstServiceTime).addLast(new SimulationEvent(events.get(time).get(i).id(), EventType.SERVICEEND));
//                }
//            }
//        }
        if(requestsCount == 1)
            return 0;
        
        serviceEvents.put(queueEvents.firstKey()+service.GenerateRandomNumber(), new SimulationEvent(queueEvents.firstEntry().getValue().id(), EventType.SERVICEEND));
        double firstQueueTime = queueEvents.higherKey(queueEvents.firstKey()), lastQueueTime = firstQueueTime, curQueueTime = firstQueueTime;
        double curServiceTime = serviceEvents.firstKey();
        Random rnd = new Random();
        while(processedRequests < requestsCount)
        {
            while(queueEvents.higherKey(lastQueueTime)!= null && queueEvents.higherKey(lastQueueTime)<=curServiceTime)
            {
                lastQueueTime = queueEvents.higherKey(lastQueueTime);
                ++curQueueLength;
                maxQueueLen = Math.max(maxQueueLen, curQueueLength);
            }
            if(rejectedReturn){
                if(rnd.nextDouble()<rejectProbability){
                    
                    queueEvents.put(curServiceTime, new SimulationEvent(serviceEvents.get(curServiceTime).id(), EventType.TOQUEUE));
                }
                else
                {
                    --curQueueLength;
                    ++processedRequests;
                }
            }
            else
            {
                --curQueueLength;
                ++processedRequests;
            }
            curServiceTime = firstQueueTime + service.GenerateRandomNumber();
            serviceEvents.put(curServiceTime, new SimulationEvent(queueEvents.get(firstQueueTime).id(), EventType.SERVICEEND));
            if(queueEvents.higherKey(firstQueueTime) != null)
                firstQueueTime=queueEvents.higherKey(firstQueueTime);
        }
        return maxQueueLen;
    }
    
    public int stepSimulation()
    {
        ArrayList<Double> timesToQueue = new ArrayList<>();
        for(int i = 0; i < requestsCount; ++i)
        {
            timesToQueue.addLast(generator.GenerateRandomNumber());
        }
        Collections.sort(timesToQueue);
        Queue<Double> queue = new LinkedList<>();
        int generatorInd = 0;
        double curTime = 0, lastServiceTime = 0;
        boolean serviceBusy=false;
        Random rnd = new Random();
        int maxQueueSize = 0;
        while(true)
        {
            
            curTime += timeStep;
            while(generatorInd < timesToQueue.size() && timesToQueue.get(generatorInd) <= curTime)
            {
                queue.offer(timesToQueue.get(generatorInd++));
            }
            if(rejectedReturn){
                 if(serviceBusy)
                {
                    if(lastServiceTime <=curTime)
                    {
                        if(rnd.nextDouble() <= rejectProbability)
                        {
                            queue.offer(curTime);
                        }
                        else{
                            serviceBusy = false;
                            if(queue.size() > 0)
                            {
                                queue.poll();
                                lastServiceTime += service.GenerateRandomNumber();
                                serviceBusy = true;
                            }
                        }
                    }
                }
                else{
                    if (queue.size() > 0)
                    {
                        double start = queue.poll();
                        lastServiceTime = start + service.GenerateRandomNumber();
                        serviceBusy = true;
                    }
                }
            }
            else{
                if(serviceBusy)
                {
                    if(lastServiceTime <=curTime)
                    {
                        serviceBusy = false;
                        if(queue.size() > 0)
                        {
                            double start = queue.poll();
                            lastServiceTime = start + service.GenerateRandomNumber();
                            serviceBusy = true;
                        }
                    }
                }
                else{
                    if (queue.size() > 0)
                    {
                        double start = queue.poll();
                        lastServiceTime += service.GenerateRandomNumber();
                        serviceBusy = true;
                    }
                }
            }
            if(queue.size() ==0 && timesToQueue.size() == generatorInd && serviceBusy == false)
                break;
            maxQueueSize = Math.max(maxQueueSize, queue.size());
        }
        return maxQueueSize;
    }
}

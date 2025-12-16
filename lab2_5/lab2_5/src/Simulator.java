
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class Simulator {
    private Generator generator;
//    private Operator operator1, operator2, operator3;
    private ArrayList<Operator> operators;
//    private Collector collector1, collector2;
    private ArrayList<Collector> collectors;
//    private Service service1, service2;
    private ArrayList<Service> services; 
    private double timeStep;
    
    Simulator(RandomGenerable generator, 
//              RandomGenerable operator1, 
//              RandomGenerable operator2,
//              RandomGenerable operator3,
              List<RandomGenerable> operators,
//              RandomGenerable service1,
//              RandomGenerable service2,
              List<RandomGenerable> services,
              int requestsCount, int timeStep)
    {
        this.generator = new Generator(generator, requestsCount);
//        this.operator1 = new Operator(operator1);
//        this.operator2 = new Operator(operator2);
//        this.operator3 = new Operator(operator3);
//        this.operators = operators;
        this.operators = new ArrayList<>();
        for(int i = 0; i < operators.size(); ++i)
            this.operators.add(new Operator(operators.get(i)));
//        this.collector1 = new Collector();
//        this.collector2 = new Collector();
        this.collectors = new ArrayList<>();
        for(int i = 0; i < 2; ++i)
            this.collectors.add(new Collector());
//        this.service1 = new Service(service1);
//        this.service2 = new Service(service2);
//        this.services = services;
        this.services = new ArrayList<>();
        for(int i = 0; i < services.size(); ++i)
            this.services.add(new Service(services.get(i)));
        this.timeStep = timeStep;
    }
    
    public double eventSimulation()
    {
//        int maxQueueLen=0, processedRequests=0, curQueueLength = 0;
//        NavigableMap<Double, SimulationEvent> queueEvents = new TreeMap<>();
//        NavigableMap<Double, SimulationEvent> serviceEvents = new TreeMap<>();
//        for(int i =0; i < this.requestsCount; ++i)
//        {
//            queueEvents.put(generator.GenerateRandomNumber(), new SimulationEvent(i, EventType.TOQUEUE));
//        }
        
////        NavigableMap<Double, ArrayList<SimulationEvent>> newEvents = new TreeMap<>();
////        double maxGoToServiceTime = 0;
////        for(Double time: events.keySet())
////        {
////            for(int i = 0; i < events.get(time).size(); ++i){
////                double firstServiceTime = Math.max(time, maxGoToServiceTime) + service.GenerateRandomNumber();
////                maxGoToServiceTime = firstServiceTime;
////                if(!events.containsKey(firstServiceTime)){
////                    newEvents.put(firstServiceTime, new ArrayList<>());
////                    newEvents.get(firstServiceTime).addLast(new SimulationEvent(events.get(time).get(i).id(), EventType.SERVICEEND));
////                }
////                else
////                {
////                    events.get(firstServiceTime).addLast(new SimulationEvent(events.get(time).get(i).id(), EventType.SERVICEEND));
////                }
////            }
////        }
//        if(requestsCount == 1)
//            return 0;
       
//        serviceEvents.put(queueEvents.firstKey()+service.GenerateRandomNumber(), new SimulationEvent(queueEvents.firstEntry().getValue().id(), EventType.SERVICEEND));
//        double firstQueueTime = queueEvents.higherKey(queueEvents.firstKey()), lastQueueTime = firstQueueTime, curQueueTime = firstQueueTime;
//        double curServiceTime = serviceEvents.firstKey();
//        Random rnd = new Random();
//        while(processedRequests < requestsCount)
//        {
//            while(queueEvents.higherKey(lastQueueTime)!= null && queueEvents.higherKey(lastQueueTime)<=curServiceTime)
//            {
//                lastQueueTime = queueEvents.higherKey(lastQueueTime);
//                ++curQueueLength;
//                maxQueueLen = Math.max(maxQueueLen, curQueueLength);
//            }
//            if(rejectedReturn){
//                if(rnd.nextDouble()<rejectProbability){
//                    
//                    queueEvents.put(curServiceTime, new SimulationEvent(serviceEvents.get(curServiceTime).id(), EventType.TOQUEUE));
//                }
//                else
//                {
//                    --curQueueLength;
//                    ++processedRequests;
//                }
//            }
//            else
//            {
//                --curQueueLength;
//                ++processedRequests;
//            }
//            curServiceTime = firstQueueTime + service.GenerateRandomNumber();
//            serviceEvents.put(curServiceTime, new SimulationEvent(queueEvents.get(firstQueueTime).id(), EventType.SERVICEEND));
//            if(queueEvents.higherKey(firstQueueTime) != null)
//                firstQueueTime=queueEvents.higherKey(firstQueueTime);
//        }
//        return maxQueueLen;
        double curTime = 0;
        Comparator<SimulationEvent> eventComparator = new Comparator<>() {
            private final Map<EventType, Integer> priority = Map.of(
                EventType.SERVICEEND, 1,
                EventType.OPERATOREND, 2,
                EventType.GENERATION, 3
            );
            
            @Override
            public int compare(SimulationEvent e1, SimulationEvent e2) {
                return Integer.compare(priority.get(e1.type()), priority.get(e2.type()));
            }
        };
        NavigableMap<Double, NavigableSet<SimulationEvent>> events = new TreeMap<>();
        double firstGenerationTime = generator.GetTime();
        generator.SetNextGenerateTime(firstGenerationTime);
        generator.SetLastGeneratedTime(0);
        generator.SetGeneratedRequestsCount(1);
        if(!events.containsKey(firstGenerationTime)){
            events.put(firstGenerationTime, new TreeSet<>(eventComparator));
            events.get(firstGenerationTime).add(new GeneratorEvent(EventType.GENERATION));
        }
        else
        {
            events.get(firstGenerationTime).add(new GeneratorEvent(EventType.GENERATION));
        }
        double nextTime=0;
        int rejectedCount = 0;
        boolean operatorFound = false;
        while(events.higherKey(nextTime)!=null)
        {
            nextTime = events.higherKey(nextTime);
            for(SimulationEvent e: events.get(nextTime))
            {
                switch (e.type())
                {
                    case EventType.GENERATION:
                        // to operators
                        operatorFound = false;
                        for(int i = 0; i < operators.size() && !operatorFound; ++i)
                        {
                            if(!operators.get(i).GetIsBusy())
                            {
                                operators.get(i).SetIsBusy(true);
                                double operationTime = operators.get(i).GetTime();
                                if(!events.containsKey(nextTime+operationTime))
                                {
                                    events.put(nextTime+operationTime, new TreeSet<>(eventComparator));
                                }
                                events.get(nextTime+operationTime).add(new OperatorEvent(i, EventType.OPERATOREND));
                                operatorFound = true;
                            }
                        }
                        if(!operatorFound)
                        {
                            ++rejectedCount;
                        }
                        
                        // new generation
                        if (generator.GetGeneratedRequestsCount()<generator.GetRequestsCount())
                        {
                            double generationTime = generator.GetTime();
                            if(!events.containsKey(nextTime+generationTime))
                            {
                                events.put(nextTime+generationTime, new TreeSet<>(eventComparator));
                            }
                            events.get(nextTime+generationTime).add(new GeneratorEvent(EventType.GENERATION));
                            generator.SetGeneratedRequestsCount(generator.GetGeneratedRequestsCount()+1);
                        }
                        break;
                    case EventType.OPERATOREND:
                        // to collector;
                        if(e instanceof OperatorEvent operatorEvent)
                        {
                            operators.get(operatorEvent.GetOperatorNumber()).SetIsBusy(false);
                            collectors.get(operatorEvent.GetOperatorNumber()/2).offer(nextTime);
                        }
                        break;
                    case EventType.SERVICEEND:
                        if(e instanceof ServiceEvent serviceEvent)
                        {
                            services.get(serviceEvent.GetServiceNumber()).SetIsBusy(false);
                            services.get(serviceEvent.GetServiceNumber()).SetLastServiceTimeEnd(nextTime);
                            
                        }
                        break;
                }
                for(int i = 0; i < services.size() && !operatorFound; ++i)
                {
                    if(!services.get(i).GetIsBusy() && collectors.get(i).GetQueueLength()!=0)
                    {
                        double serviceTime = services.get(i).GetTime();
                        double toQueueTime = collectors.get(i).poll();
                        double startTime = Math.max(toQueueTime, Math.max(nextTime, services.get(i).GetLastServiceTimeEnd()));
                        if(!events.containsKey(startTime+serviceTime))
                        {
                            events.put(startTime+serviceTime, new TreeSet<>(eventComparator));
                        }
                        events.get(startTime+serviceTime).add(new ServiceEvent(i, EventType.SERVICEEND));
                        operatorFound = true;
                    }
                }
            }
        }
        return rejectedCount/(double)generator.GetGeneratedRequestsCount();
    }
    
//    public int stepSimulation()
//    {
//        ArrayList<Double> timesToQueue = new ArrayList<>();
//        for(int i = 0; i < requestsCount; ++i)
//        {
//            timesToQueue.addLast(generator.GenerateRandomNumber());
//        }
//        Collections.sort(timesToQueue);
//        Queue<Double> queue = new LinkedList<>();
//        int generatorInd = 0;
//        double curTime = 0, lastServiceTime = 0;
//        boolean serviceBusy=false;
//        Random rnd = new Random();
//        int maxQueueSize = 0;
//        while(true)
//        {
//            
//            curTime += timeStep;
//            while(generatorInd < timesToQueue.size() && timesToQueue.get(generatorInd) <= curTime)
//            {
//                queue.offer(timesToQueue.get(generatorInd++));
//            }
//            if(rejectedReturn){
//                 if(serviceBusy)
//                {
//                    if(lastServiceTime <=curTime)
//                    {
//                        if(rnd.nextDouble() <= rejectProbability)
//                        {
//                            queue.offer(curTime);
//                        }
//                        else{
//                            serviceBusy = false;
//                            if(queue.size() > 0)
//                            {
//                                queue.poll();
//                                lastServiceTime += service.GenerateRandomNumber();
//                                serviceBusy = true;
//                            }
//                        }
//                    }
//                }
//                else{
//                    if (queue.size() > 0)
//                    {
//                        double start = queue.poll();
//                        lastServiceTime = start + service.GenerateRandomNumber();
//                        serviceBusy = true;
//                    }
//                }
//            }
//            else{
//                if(serviceBusy)
//                {
//                    if(lastServiceTime <=curTime)
//                    {
//                        serviceBusy = false;
//                        if(queue.size() > 0)
//                        {
//                            double start = queue.poll();
//                            lastServiceTime = start + service.GenerateRandomNumber();
//                            serviceBusy = true;
//                        }
//                    }
//                }
//                else{
//                    if (queue.size() > 0)
//                    {
//                        double start = queue.poll();
//                        lastServiceTime += service.GenerateRandomNumber();
//                        serviceBusy = true;
//                    }
//                }
//            }
//            if(queue.size() ==0 && timesToQueue.size() == generatorInd && serviceBusy == false)
//                break;
//            maxQueueSize = Math.max(maxQueueSize, queue.size());
//        }
//        return maxQueueSize;
//    }
}

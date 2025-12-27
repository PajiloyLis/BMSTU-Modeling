
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
    private ArrayList<Operator> operators;
    private ArrayList<Collector> preBoxOfficeCollectors;
    private Collector preKitchenCollector;
    private ArrayList<Service> kitchen;
    private Collector preReleaseCollector;
    private Service release;
    private double timeStep;
    private Statistic statistic;
    
    Simulator(RandomGenerable generator, 
              List<Integer> maxPreBoxOfficeQueueLength,
              List<RandomGenerable> operators,
              List<Double> operatorsRejectProbabilities,
              List<RandomGenerable> services,
              RandomGenerable release,
              int requestsCount, int timeStep)
    {
        this.generator = new Generator(generator, requestsCount);
        this.preBoxOfficeCollectors = new ArrayList<>();
        for(int i = 0; i < operators.size(); ++i)
            this.preBoxOfficeCollectors.add(new Collector(maxPreBoxOfficeQueueLength.get(i)));
        this.operators = new ArrayList<>();
        for(int i = 0; i < operators.size(); ++i)
            this.operators.add(new Operator(operators.get(i), operatorsRejectProbabilities.get(i)));
        this.preKitchenCollector = new Collector(-1);
        this.kitchen = new ArrayList<>();
        for(int i = 0; i < services.size(); ++i)
            this.kitchen.add(new Service(services.get(i)));
        this.preReleaseCollector = new Collector(-1);
        this.release = new Service(release);
        this.timeStep = timeStep;
        this.statistic = new Statistic(operators.size(), services.size(), 1);
    }
    
    public Map<StatisticType, Double> eventSimulation()
    {
        double curTime = 0;
        Comparator<SimulationEvent> eventComparator = new Comparator<>() {
            private final Map<EventType, Integer> priority = Map.of(
                EventType.RELEASEEND, 0,
                EventType.KITECHENEND, 1,
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
                        // to operators queue
                        operatorFound = false;
                        int minOperatorQueueLengthIndex = 0;
                        if (preBoxOfficeCollectors.get(minOperatorQueueLengthIndex).GetQueueLength() > preBoxOfficeCollectors.get(minOperatorQueueLengthIndex).GetMaxQueueLength())
                        {
                            operators.get(minOperatorQueueLengthIndex).SetRejectedCount(operators.get(minOperatorQueueLengthIndex).GetRejectedCount()+1);
                            statistic.AddOperatorRejectedCount(minOperatorQueueLengthIndex, 1);
                        }
                        else
                        {
                            operatorFound = true;
                        }
                                
                        for(int i = 1; i < operators.size(); ++i)
                        {
                            if(preBoxOfficeCollectors.get(i).GetQueueLength() > preBoxOfficeCollectors.get(i).GetMaxQueueLength())
                            {
                                operators.get(i).SetRejectedCount(operators.get(i).GetRejectedCount()+1);
                                statistic.AddOperatorRejectedCount(i, 1);
                            }
                            else if(preBoxOfficeCollectors.get(minOperatorQueueLengthIndex).GetQueueLength() > preBoxOfficeCollectors.get(i).GetQueueLength())
                            {
                                minOperatorQueueLengthIndex=i;
                                operatorFound = true;
                            }
                        }
                        if(operatorFound)
                        {
                            preBoxOfficeCollectors.get(minOperatorQueueLengthIndex).Offer(nextTime);
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
                        if(e instanceof OperatorEvent operatorEvent)
                        {
                            // free operator
                            operators.get(operatorEvent.GetOperatorNumber()).SetIsBusy(false);
                            operators.get(operatorEvent.GetOperatorNumber()).SetLastOperationsTimeEnd(nextTime);
                            operators.get(operatorEvent.GetOperatorNumber()).SetProcessedCount(operators.get(operatorEvent.GetOperatorNumber()).GetProcessedCount()+1);
                            statistic.AddOperatorProcessedCount(operatorEvent.GetOperatorNumber(), 1);
                            // to kitchen collector;
                            preKitchenCollector.Offer(nextTime);
                        }
                        break;
                    case EventType.KITECHENEND:
                        if(e instanceof ServiceEvent serviceEvent)
                        {
                            // free kitchen
                            kitchen.get(serviceEvent.GetServiceNumber()).SetIsBusy(false);
                            kitchen.get(serviceEvent.GetServiceNumber()).SetLastServiceTimeEnd(nextTime);
                            statistic.AddKitchenProcessedCount(serviceEvent.GetServiceNumber(), 1);
                            // to release collector
                            preReleaseCollector.Offer(nextTime);
                        }
                        break;
                    case EventType.RELEASEEND:
                        if(e instanceof ServiceEvent serviceEvent)
                        {
                            // free release
                            release.SetIsBusy(false);
                            release.SetLastServiceTimeEnd(nextTime); 
                            statistic.AddReleaseProcessedCount(0, 1);
                        }
                        break;
                }
                for(int i = 0; i < operators.size(); ++i)
                {
                    if(!operators.get(i).GetIsBusy() && preBoxOfficeCollectors.get(i).GetQueueLength()!=0)
                    {
                        while(operators.get(i).GetRejectionValue()<operators.get(i).GetRejectProbability() && preBoxOfficeCollectors.get(i).GetQueueLength()!=0 )
                        {
                            operators.get(i).SetRejectedCount(operators.get(i).GetRejectedCount()+1);
                            statistic.AddOperatorRejectedCount(i, 1);
                            preBoxOfficeCollectors.get(i).Poll();
                        }
                        if(preBoxOfficeCollectors.get(i).GetQueueLength()!=0){
                            double serviceTime = operators.get(i).GetTime();
                            double toQueueTime = preBoxOfficeCollectors.get(i).Poll();
                            double startTime = Math.max(toQueueTime, Math.max(nextTime, operators.get(i).GetLastOperationsTimeEnd()));
                            if(!events.containsKey(startTime+serviceTime))
                            {
                                events.put(startTime+serviceTime, new TreeSet<>(eventComparator));
                            }
                            events.get(startTime+serviceTime).add(new OperatorEvent(i, EventType.OPERATOREND));
                            operators.get(i).SetIsBusy(true);
                            statistic.AddOperatorWorkingTime(i, serviceTime);
                            operatorFound = true;
                        }
                    }
                }
                operatorFound = false;
                for(int i = 0; i < kitchen.size(); ++i)
                {
                    if((!kitchen.get(i).GetIsBusy()) && preKitchenCollector.GetQueueLength()!=0)
                    {
                        double serviceTime = kitchen.get(i).GetTime();
                        double toQueueTime = preKitchenCollector.Poll();
                        double startTime = Math.max(toQueueTime, Math.max(nextTime, kitchen.get(i).GetLastServiceTimeEnd()));
                        if(!events.containsKey(startTime+serviceTime))
                        {
                            events.put(startTime+serviceTime, new TreeSet<>(eventComparator));
                        }
                        events.get(startTime+serviceTime).add(new ServiceEvent(i, EventType.KITECHENEND));
                        kitchen.get(i).SetIsBusy(true);
                        statistic.AddKitchenWorkingTime(i, serviceTime);
                        operatorFound = true;
                    }
                }
                if(!release.GetIsBusy() && preReleaseCollector.GetQueueLength()!=0)
                {
                    double serviceTime = release.GetTime();
                    double toQueueTime = preReleaseCollector.Poll();
                    double startTime = Math.max(toQueueTime, Math.max(nextTime, release.GetLastServiceTimeEnd()));
                    if(!events.containsKey(startTime+serviceTime))
                    {
                        events.put(startTime+serviceTime, new TreeSet<>(eventComparator));
                    }
                    events.get(startTime+serviceTime).add(new ServiceEvent(-1, EventType.RELEASEEND));
                    statistic.AddReleaseWorkingTime(0, serviceTime);
                    release.SetIsBusy(true);
                }
            }
        }
//        return rejectedCount/(double)generator.GetGeneratedRequestsCount();
        Map<StatisticType, Double> res = new TreeMap<>();
        res.put(StatisticType.OPERATOR1_REJECTION_PROBABILITY, statistic.GetOperatorRejectProbability(0));
        res.put(StatisticType.OPERATOR2_REJECTION_PROBABILITY, statistic.GetOperatorRejectProbability(1));
        res.put(StatisticType.OPERATOR3_REJECTION_PROBABILITY, statistic.GetOperatorRejectProbability(2));
        res.put(StatisticType.OPERATOR1_REJECTED_COUNT, (double)statistic.GetOperatorRejectedCount(0));
        res.put(StatisticType.OPERATOR2_REJECTED_COUNT, (double)statistic.GetOperatorRejectedCount(1));
        res.put(StatisticType.OPERATOR1_PROCESSED_COUNT, (double)statistic.GetOperatorProcessedCount(0));
        res.put(StatisticType.OPERATOR2_PROCESSED_COUNT, (double)statistic.GetOperatorProcessedCount(1));
        res.put(StatisticType.OPERATOR3_REJECTED_COUNT, (double)statistic.GetOperatorRejectedCount(2));
        res.put(StatisticType.OPERATOR3_PROCESSED_COUNT, (double)statistic.GetOperatorProcessedCount(2));
        res.put(StatisticType.OPERATOR1_AVERAGE_TIME, statistic.GetOperatorAverageWorkingTime(0));
        res.put(StatisticType.OPERATOR2_AVERAGE_TIME, statistic.GetOperatorAverageWorkingTime(1));
        res.put(StatisticType.OPERATOR3_AVERAGE_TIME, statistic.GetOperatorAverageWorkingTime(2));
        res.put(StatisticType.OPERATOR1_TOTAL_TIME, statistic.GetOperatorWorkingTime(0));
        res.put(StatisticType.OPERATOR2_TOTAL_TIME, statistic.GetOperatorWorkingTime(1));
        res.put(StatisticType.OPERATOR3_TOTAL_TIME, statistic.GetOperatorWorkingTime(2));
        res.put(StatisticType.OPERATORS_AVERAGE_TIME, statistic.GetOperatorsAverageWorkingTime());
        res.put(StatisticType.OPERATORS_PROCESSED_COUNT, (double)statistic.GetOperatorsProcessed());
        res.put(StatisticType.OPERATORS_REJECTION_PROBABILITY, statistic.GetOperatorsRejectProbbility());

        res.put(StatisticType.KITCHEN1_PROCESSED_COUNT, (double)statistic.GetKitchenProcessedCount(0));
        res.put(StatisticType.KITCHEN2_PROCESSED_COUNT, (double)statistic.GetKitchenProcessedCount(1));
        res.put(StatisticType.KITCHEN3_PROCESSED_COUNT, (double)statistic.GetKitchenProcessedCount(2));
        res.put(StatisticType.KITCHEN1_AVERAGE_TIME, statistic.GetKitchenAverageWorkingTime(0));
        res.put(StatisticType.KITCHEN2_AVERAGE_TIME, statistic.GetKitchenAverageWorkingTime(1));
        res.put(StatisticType.KITCHEN3_AVERAGE_TIME, statistic.GetKitchenAverageWorkingTime(2));
        res.put(StatisticType.KITCHEN1_TOTAL_TIME, statistic.GetKitchenWorkingTime(0));
        res.put(StatisticType.KITCHEN2_TOTAL_TIME, statistic.GetKitchenWorkingTime(1));
        res.put(StatisticType.KITCHEN3_TOTAL_TIME, statistic.GetKitchenWorkingTime(2));
        res.put(StatisticType.KITCHENS_AVERAGE_TIME,  statistic.GetKitchensAverageWorkingTime());

        res.put(StatisticType.RELEASE_PROCESSED_COUNT, (double)statistic.GetReleaseProcessedCount(0));
        res.put(StatisticType.RELEASE_AVERAGE_TIME,statistic.GetReleaseAverageWorkingTime(0));
        res.put(StatisticType.RELEASE_TOTAL_TIME, statistic.GetReleaseWorkingTime(0));
        
        return res;
    }
}

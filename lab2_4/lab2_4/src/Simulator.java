/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class Simulator {
    private RandomGenerable generator;
    private int requestsCount;
    private double rejectProbability, timeStep;
    private boolean rejectedReturn;
    Simulator(RandomGenerable generator, int requestsCount, double rejectProbability, double timeStep, boolean rejectedReturn)
    {
        this.generator = generator;
        this.rejectProbability = rejectProbability;
        this.rejectedReturn = rejectedReturn;
        this.requestsCount = requestsCount;
        this.timeStep = timeStep;
    }
}

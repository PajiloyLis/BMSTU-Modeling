//
// Created by ivan on 13.10.25.
//

#ifndef LAB2_1_RANDOMGENERATOR_H
#define LAB2_1_RANDOMGENERATOR_H

#include<string>
#include<vector>
#include<fstream>
#include<cmath>
#include<map>

using namespace std;

#define DEFAULT_SEED 112445
#define A 6
#define B 7
#define C 3
#define INT_MOD ((1<<(sizeof(int)*8-2))-1)
#define ONE_DIGIT_FILE "../data/one_digit_nums.txt"
#define TWO_DIGIT_FILE "../data/two_digit_nums.txt"
#define THREE_DIGIT_FILE "../data/three_digit_nums.txt"

#define BETA_0_95_QUANTILE 1.65
#define BETA_0_95 0.9

#define EDGE 0.5

class RandomGenerator {
public:
    RandomGenerator();

    static vector<int> readOneDigitNumbers(int count);

    static vector<int> readTwoDigitNumbers(int count);

    static vector<int> readThreeDigitNumbers(int count);

    vector<int> generateOneDigitNumbers(int count);

    vector<int> generateTwoDigitNumbers(int count);

    vector<int> generateThreeDigitNumbers(int count);

    double next();

    void setSeed(int seed);

    int Seed() const;

    double CheckRandomness(const vector<int> &nums, int l, int r);

    static bool checkInterval(double r_h, double r_b, double r);

    static double getAlpha() { return 1 - beta; }

private:
    int lastGenerated;
    const long long a = A, b = B, c = C, mod = INT_MOD;
    constexpr static const double edge = EDGE, quantile = BETA_0_95_QUANTILE, beta = BETA_0_95;
    vector<double> betas;
    vector<double> quantiles;
};


#endif //LAB2_1_RANDOMGENERATOR_H

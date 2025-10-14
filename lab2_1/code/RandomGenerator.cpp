//
// Created by ivan on 13.10.25.
//

#include "RandomGenerator.h"

vector<int> RandomGenerator::readOneDigitNumbers(int count) {
    vector<int> nums;
    ifstream in(ONE_DIGIT_FILE);
    int tmp;
    while (in >> tmp && nums.size() < count)
        if (tmp > -1 && tmp < 10)
            nums.push_back(tmp);
    in.close();
    return nums;
}

vector<int> RandomGenerator::readTwoDigitNumbers(int count) {
    vector<int> nums;
    ifstream in(TWO_DIGIT_FILE);
    int tmp;
    while (in >> tmp && nums.size() < count)
        if (tmp > 9 && tmp < 100)
            nums.push_back(tmp);
    in.close();
    return nums;
}

vector<int> RandomGenerator::readThreeDigitNumbers(int count) {
    vector<int> nums;
    ifstream in(THREE_DIGIT_FILE);
    int tmp;
    while (in >> tmp && nums.size() < count)
        if (tmp > 99 && tmp < 1000)
            nums.push_back(tmp);
    in.close();
    return nums;
}

double RandomGenerator::next() {
    lastGenerated = (a * lastGenerated * lastGenerated + b * lastGenerated + c) % mod;
    return lastGenerated / static_cast<double>(mod);
}

vector<int> RandomGenerator::generateOneDigitNumbers(int count) {
    vector<int> nums(count);
    for (int i = 0; i < count; ++i) {
        nums[i] = static_cast<int>(round(next() * 9));
    }
    return nums;
}

vector<int> RandomGenerator::generateTwoDigitNumbers(int count) {
    vector<int> nums(count);
    for (int i = 0; i < count; ++i) {
        nums[i] = static_cast<int>(round(next() * 89) + 10);
    }
    return nums;
}

vector<int> RandomGenerator::generateThreeDigitNumbers(int count) {
    vector<int> nums(count);
    for (int i = 0; i < count; ++i) {
        nums[i] = static_cast<int>(round(next() * 899) + 100);
    }
    return nums;
}

void RandomGenerator::setSeed(int seed) {
    lastGenerated = seed;
}

int RandomGenerator::Seed() const {
    return lastGenerated;
}

double RandomGenerator::CheckRandomness(const vector<int> &nums, int l, int r) {
    vector<double> nums_0_1(nums.size());
    for (int i = 0; i < nums.size(); ++i)
        nums_0_1[i] = (nums[i] - l) / static_cast<double>(r - l);

    vector<int> y(nums.size());
    y[0] = static_cast<int>(nums_0_1[0] >= edge);
    for (int i = 1; i < nums.size(); ++i)
        y[i] = static_cast<int>(nums_0_1[i] >= edge);

    int batch_count = 0;
    for (int i = 1; i < y.size(); ++i)
        if (y[i - 1] != y[i])
            ++batch_count;

    double mean = 2 * y.size() * edge * (1 - edge) + edge * edge + (1 - edge) * (1 - edge),
            standard_deviation = sqrt(4 * y.size() * edge * (1 - edge) * (1 - 3 * edge * (1 - edge)) -
                                      2 * edge * (1 - edge) * (3 - 10 * edge * (1 - edge)));

    int left = -1, right = quantiles.size();
    while (right - left > 1) {
        int m = (right + left) / 2;
        double r_h = mean - quantiles[m] * standard_deviation, r_b = mean + quantiles[m] * standard_deviation;
        if (checkInterval(r_h, r_b, batch_count))
            right = m;
        else
            left = m;
    }
    return right == quantiles.size() ? 0 : 1-betas[right];
}


RandomGenerator::RandomGenerator() : lastGenerated(DEFAULT_SEED) {
    ifstream in("../data/standard_normal_distribution_quantiles.txt");
    double quant, value;
    while (in >> quant) {
        in >> value;
        betas.push_back(quant);
        quantiles.push_back(value);
    }
    in.close();
}

bool RandomGenerator::checkInterval(double r_h, double r_b, double r) {
    return r_h <= r && r <= r_b;
}

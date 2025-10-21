//
// Created by ivan on 21.10.25.
//

#ifndef LAB2_2_MARKOV_CHAIN_CALCULATOR_H
#define LAB2_2_MARKOV_CHAIN_CALCULATOR_H

#include<vector>
#include"Gauss_Zeidel_solver.h"
#include <Eigen/Dense>
using namespace std;

vector<double> calc_limit_probabilities(const vector<vector<double>> &lambdas);

vector<double> calc_average_time(const vector<double> &p, const vector<vector<double>> &lambdas);

vector<double> gauss(vector<vector<double>> &a, vector<double> &y);

#endif //LAB2_2_MARKOV_CHAIN_CALCULATOR_H

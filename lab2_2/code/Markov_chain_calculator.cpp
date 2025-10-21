//
// Created by ivan on 21.10.25.
//

#include "Markov_chain_calculator.h"


vector<double> calc_limit_probabilities(const vector<vector<double>> &lambdas) {
    vector<vector<double>> _a(lambdas.size(), vector<double>(lambdas.size()));
    double row_sum = 0;
    for (int i = 0; i < lambdas.size(); ++i) {
        row_sum = 0;
        for (int j = 0; j < lambdas.size(); ++j)
            row_sum += lambdas[i][j];
        _a[i][i] = lambdas[i][i] - row_sum;
        for (int j = 0; j < lambdas.size(); ++j)
            if (i != j)
                _a[i][j] = lambdas[j][i];
    }
    Eigen::MatrixXd a(lambdas.size(), lambdas.size());
    for (int i = 0; i < lambdas.size(); ++i)
        for (int j = 0; j < lambdas.size(); ++j)
            a(i, j) = _a[i][j];
    Eigen::JacobiSVD<Eigen::MatrixXd> svd(a, Eigen::ComputeThinU | Eigen::ComputeThinV);
    Eigen::VectorXd solution = svd.matrixV().col(a.cols() - 1);
    double sum = solution.sum();
    if (std::abs(sum) > EPS) {  
        solution /= sum;
    }
    vector<double> result(lambdas.size());
    for (int i = 0; i < lambdas.size(); ++i)
        result[i] = solution(i);
    return result;
}

vector<double> calc_average_time(const vector<double> &p, const vector<vector<double>> &lambdas) {
    vector<double> t(p.size());
    double denominator;
    for (int i = 0; i < t.size(); ++i) {
        denominator = 0;
        for (int j = 0; j < lambdas[i].size(); ++j) {
            if (i != j)
                denominator += (lambdas[i][j] + lambdas[j][i]);
            else
                denominator += lambdas[i][i];
        }
        t[i] = p[i] / denominator;
    }
    return t;
}
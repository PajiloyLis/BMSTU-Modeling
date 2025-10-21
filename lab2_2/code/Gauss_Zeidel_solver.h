//
// Created by ivan on 21.10.25.
//

#ifndef LAB2_2_GAUSS_ZEIDEL_SOLVER_H
#define LAB2_2_GAUSS_ZEIDEL_SOLVER_H

#include<vector>
#include<cmath>

#define EPS 1e-6
#define ITERATIONS_COUNT 1000

using namespace std;

vector<double> solve_equation_system(const vector<vector<double>> &a, const vector<double> &b);


#endif //LAB2_2_GAUSS_ZEIDEL_SOLVER_H

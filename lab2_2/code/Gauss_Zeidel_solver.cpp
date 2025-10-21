//
// Created by ivan on 21.10.25.
//

#include "Gauss_Zeidel_solver.h"

vector<vector<double>> calc_c(const vector<vector<double>> &a);

vector<double> calc_d(const vector<vector<double>> &a, const vector<double> &b);

double calc_vector_norm(const vector<double> &x);

vector<double> substract_vectors(const vector<double> &a, const vector<double> &b);

bool check_correct(const vector<double> &x_prev, const vector<double> &x);

vector<double> gauss(vector<vector<double>> &a, vector<double> &y)
{
    int n = a.size();
    double max;
    int k, index;
    const double eps = 0.00001;  // точность
    vector<double> x(n);
    k = 0;
    while (k < n)
    {
        // Поиск строки с максимальным a[i][k]
        max = abs(a[k][k]);
        index = k;
        for (int i = k + 1; i < n; i++)
        {
            if (abs(a[i][k]) > max)
            {
                max = abs(a[i][k]);
                index = i;
            }
        }
        // Перестановка строк
        if (max < eps)
        {
            return x;
        }
        for (int j = 0; j < n; j++)
        {
            double temp = a[k][j];
            a[k][j] = a[index][j];
            a[index][j] = temp;
        }
        double temp = y[k];
        y[k] = y[index];
        y[index] = temp;
        // Нормализация уравнений
        for (int i = k; i < n; i++)
        {
            double temp = a[i][k];
            if (abs(temp) < EPS) continue; // для нулевого коэффициента пропустить
            for (int j = k; j < n; j++)
                a[i][j] = a[i][j] / temp;
            y[i] = y[i] / temp;
            if (i == k)  continue; // уравнение не вычитать само из себя
            for (int j = 0; j < n; j++)
                a[i][j] = a[i][j] - a[k][j];
            y[i] = y[i] - y[k];
        }
        k++;
    }
    // обратная подстановка
    for (k = n - 1; k >= 0; k--)
    {
        x[k] = y[k];
        for (int i = 0; i < k; i++)
            y[i] = y[i] - a[i][k] * x[k];
    }
    return x;
}


vector<double> solve_equation_system(const vector<vector<double>> &a, const vector<double> &b) {
    vector<vector<double>> c = calc_c(a);
    vector<double> d = calc_d(a, b);

    vector<double> x_prev(a.size(), 1./a.size());
    vector<double> x = x_prev;
    do{
        for(int i = 0; i < x.size(); ++i)
        {
            double cur_x_component = 0, prev_x_component = 0;
            for(int j = 0; j < i;++j)
                cur_x_component+=c[i][j]*x[j];
            for(int j = i+1; j < x.size(); ++j)
                prev_x_component+=c[i][j]*x[j];
            x[i]=cur_x_component+prev_x_component+d[i];
        }
    } while(!check_correct(x_prev, x));

    return x;
}

vector<vector<double>> calc_c(const vector<vector<double>> &a) {
    vector<vector<double>> res(a.size(), vector<double>(a.size()));
    for (int i = 0; i < a.size(); ++i) {
        for (int j = 0; j < a.size(); ++j) {
            if (i == j)
                res[i][j] = 0;
            else {
                res[i][j] = -a[i][j] / a[i][i];
            }
        }
    }
    return res;
}

vector<double> calc_d(const vector<vector<double>> &a, const vector<double> &b)
{
    vector<double> d(a.size());
    for(int i = 0; i < a.size(); ++i)
    {
        d[i]=b[i]/a[i][i];
    }
    return d;
}

bool check_correct(const vector<double> &x_prev, const vector<double> &x)
{
    if(calc_vector_norm(substract_vectors(x, x_prev))<=EPS)
        return true;
    return false;
}

vector<double> substract_vectors(const vector<double> &a, const vector<double> &b)
{
    vector<double> res(a.size());
    for(int i = 0; i < a.size(); ++i)
    {
        res[i]=a[i]-b[i];
    }
    return res;
}

double calc_vector_norm(const vector<double> &x)
{
    double square_sum = 0.0;
    for(int i = 0; i < x.size(); ++i)
        square_sum+=x[i]*x[i];
    return sqrt(square_sum);
}
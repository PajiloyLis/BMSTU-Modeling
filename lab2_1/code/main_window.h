#ifndef LAB2_1_MAIN_WINDOW_H
#define LAB2_1_MAIN_WINDOW_H

#include <QColor>
#include <QColorDialog>
#include <QCoreApplication>
#include <QLabel>
#include <QMessageBox>
#include <QObject>
#include <QPushButton>
#include <QTableWidget>
#include <iostream>
#include <string>
#include <map>
#include <QProgressBar>
#include <QFileDialog>
#include <QMainWindow>
#include <QHeaderView>
#include <QApplication>
#include <QLineEdit>
#include <string>
#include <vector>

#include "RandomGenerator.h"
#include "ui_mainwindow.h"

using namespace std;

class MainWindow : public QMainWindow {

Q_OBJECT

public:
    MainWindow();

    ~MainWindow() override { delete randomGenerator; };

private:
    void setBindings();

    bool checkPositiveNumericField(QLineEdit *field);

    void setTableColumn(QTableWidget *table, int column_number, const vector<int> &nums);

    void GenerateNumbersButtonClicked();

    void setLastTableRow(QTableWidget *table, int row_num, const vector<double> &randomness);

    RandomGenerator *randomGenerator;

    int lastSeed;
};

#endif //LAB2_1_MAIN_WINDOW_H

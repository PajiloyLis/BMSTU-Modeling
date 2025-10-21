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

#include "ui_main_window.h"

using namespace std;

class MainWindow : public QMainWindow {

Q_OBJECT

public:
    MainWindow();

    ~MainWindow() = default;

private:
    void setBindings();

    bool checkPositiveNumericField(QLineEdit *field);

    void setTableColumn(QTableWidget *table, int column_number, const vector<int> &nums);

    void StatesCountLineEditChanged();

    void SetSquareTable(QTableWidget *table, int count);

    bool CheckIntensivityCorrectness(QTableWidgetItem *item);

    void CalculationButtonClicked();
};

#endif //LAB2_1_MAIN_WINDOW_H
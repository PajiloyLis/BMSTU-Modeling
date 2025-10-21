#include "main_window.h"
#include "Markov_chain_calculator.h"

MainWindow::MainWindow() : QMainWindow() {
    std::setlocale(LC_ALL, "");
    Ui::MainWindow ui;
    ui.setupUi(this);

    setBindings();
}

void MainWindow::setBindings() {
//    QObject::connect(this->findChild<QPushButton *>("GenerateNumbersPushButton"),
//                     &QPushButton::clicked, this, &MainWindow::GenerateNumbersButtonClicked);
    QObject::connect(this->findChild<QLineEdit *>("StatesCountLineEdit"), &QLineEdit::editingFinished, this,
                     &MainWindow::StatesCountLineEditChanged);
    QObject::connect(this->findChild<QPushButton *>("CalcPushButton"), &QPushButton::clicked, this,
                     &MainWindow::CalculationButtonClicked);
}

void MainWindow::StatesCountLineEditChanged() {
    auto states_count_line_edit = this->findChild<QLineEdit *>("StatesCountLineEdit");
    if (!checkPositiveNumericField(states_count_line_edit))
        return;
    SetSquareTable(this->findChild<QTableWidget *>("InputTableWidget"), states_count_line_edit->text().toInt());
}

void MainWindow::SetSquareTable(QTableWidget *table, int count) {
    for (int i = 0; i < table->rowCount(); ++i) {
        for (int j = 0; j < table->columnCount(); ++j) {
            delete table->item(i, j);
        }
    }
    table->setColumnCount(count);
    table->setRowCount(count);
    table->horizontalHeader()->setSectionResizeMode(QHeaderView::Stretch);
    table->verticalHeader()->setSectionResizeMode(QHeaderView::Stretch);
    table->setHorizontalScrollBarPolicy(Qt::ScrollBarAlwaysOff);
    for (int i = 0; i < count; ++i) {
        for (int j = 0; j < count; ++j) {
            table->setItem(i, j, new QTableWidgetItem("0"));
        }
    }
}


bool MainWindow::checkPositiveNumericField(QLineEdit *field) {
    QPalette palette = QPalette();
    if (!(field->text().toInt()) || (field->text().toInt() < 1)) {
        palette.setColor(QPalette::Base, Qt::red);
        field->setPalette(palette);
        return false;
    } else {
        palette.setColor(QPalette::Base, Qt::white);
        field->setPalette(palette);
        return true;
    }
}


void MainWindow::setTableColumn(QTableWidget *table, int column_number, const vector<int> &nums) {
    for (int i = 0; i < nums.size(); ++i) {
        table->setItem(i, column_number, new QTableWidgetItem(QString(to_string(nums[i]).c_str())));
    }
}

bool MainWindow::CheckIntensivityCorrectness(QTableWidgetItem *item) {
    QBrush palette = QBrush();
    bool ok;
    item->text().toDouble(&ok);
    if (!ok) {
        palette.setColor(Qt::red);
        ok = false;
    } else
        palette.setColor(Qt::white);
    item->setBackground(palette);
    return ok;
}

void MainWindow::CalculationButtonClicked() {
    auto input_table = this->findChild<QTableWidget *>("InputTableWidget");
    for (int i = 0; i < input_table->rowCount(); ++i)
        for (int j = 0; j < input_table->columnCount(); ++j)
            if (!CheckIntensivityCorrectness(input_table->item(i, j)))
                return;

    vector<vector<double>> lambdas(input_table->rowCount(), vector<double>(input_table->columnCount()));
    for (int i = 0; i < input_table->rowCount(); ++i)
        for (int j = 0; j < input_table->columnCount(); ++j)
            lambdas[i][j] = input_table->item(i, j)->text().toDouble();

    vector<double> limit_probabilities = calc_limit_probabilities(lambdas);
    vector<double> times = calc_average_time(limit_probabilities, lambdas);

    auto probabilities_table = this->findChild<QTableWidget *>("PossibilityTableWidget");
    auto times_table = this->findChild<QTableWidget *>("AverageTimeTableWidget");

    for (int i = 0; i < probabilities_table->rowCount(); ++i) {
        for (int j = 0; j < probabilities_table->columnCount(); ++j) {
            delete probabilities_table->item(i, j);
        }
    }

    for (int i = 0; i < times_table->rowCount(); ++i) {
        for (int j = 0; j < times_table->columnCount(); ++j) {
            delete times_table->item(i, j);
        }
    }

    probabilities_table->setRowCount(0);
    probabilities_table->setColumnCount(1);
    probabilities_table->setRowCount(limit_probabilities.size());
    probabilities_table->horizontalHeader()->setSectionResizeMode(QHeaderView::Stretch);
    probabilities_table->verticalHeader()->setSectionResizeMode(QHeaderView::Stretch);

    times_table->setRowCount(0);
    times_table->setColumnCount(1);
    times_table->setRowCount(times.size());
    times_table->horizontalHeader()->setSectionResizeMode(QHeaderView::Stretch);
    times_table->verticalHeader()->setSectionResizeMode(QHeaderView::Stretch);

    for (int i = 0; i < limit_probabilities.size(); ++i) {
        probabilities_table->setItem(i, 0, new QTableWidgetItem(QString(to_string(limit_probabilities[i]).c_str())));
        times_table->setItem(i, 0, new QTableWidgetItem(QString(to_string(times[i]).c_str())));
    }
}

//void MainWindow::GenerateNumbersButtonClicked() {
//    auto count_line_edit = this->findChild<QLineEdit *>("CountInputLineEdit"),
//            seed_line_edit = this->findChild<QLineEdit *>("SeedInputLineEdit");
//
//    if (!checkPositiveNumericField(count_line_edit) ||
//        (!seed_line_edit->text().isEmpty() && !checkPositiveNumericField(seed_line_edit)))
//        return;
//    int count = count_line_edit->text().toInt();
//
//    int seed;
//    if (!seed_line_edit->text().isEmpty()) {
//        seed = seed_line_edit->text().toInt();
//        if (lastSeed == -1 || lastSeed != seed)
//            randomGenerator->setSeed(seed);
//    } else {
//        seed = randomGenerator->Seed();
//    }
//
//    lastSeed = seed;
//
//
//    vector<int> one_digit_precalced = RandomGenerator::readOneDigitNumbers(count),
//            two_digit_precalced = RandomGenerator::readTwoDigitNumbers(count),
//            three_digit_precalced = RandomGenerator::readThreeDigitNumbers(count),
//            one_digit_generated = randomGenerator->generateOneDigitNumbers(count),
//            two_digit_generated = randomGenerator->generateTwoDigitNumbers(count),
//            three_digit_generated = randomGenerator->generateThreeDigitNumbers(count);
//
//    auto nums_table = this->findChild<QTableWidget *>("NumbersTableWidget");
//    nums_table->setRowCount(0);
//    nums_table->setRowCount(count + 1);
//    setTableColumn(nums_table, 0, one_digit_precalced);
//    setTableColumn(nums_table, 1, two_digit_precalced);
//    setTableColumn(nums_table, 2, three_digit_precalced);
//    setTableColumn(nums_table, 3, one_digit_generated);
//    setTableColumn(nums_table, 4, two_digit_generated);
//    setTableColumn(nums_table, 5, three_digit_generated);
//
//    vector<double> randomness(6);
//    randomness[0] = randomGenerator->CheckRandomness(one_digit_precalced, 0, 9);
//    randomness[1] = randomGenerator->CheckRandomness(two_digit_precalced, 10, 99);
//    randomness[2] = randomGenerator->CheckRandomness(three_digit_precalced, 100, 999);
//    randomness[3] = randomGenerator->CheckRandomness(one_digit_generated, 0, 9);
//    randomness[4] = randomGenerator->CheckRandomness(two_digit_generated, 10, 99);
//    randomness[5] = randomGenerator->CheckRandomness(three_digit_generated, 100, 999);
//
//    setLastTableRow(nums_table, count, randomness);
//}
//
//void MainWindow::setLastTableRow(QTableWidget *table, int row_num, const vector<double> &randomness) {
//    table->setVerticalHeaderItem(row_num, new QTableWidgetItem(
//            QString("Случайно при уровне alpha = ")));
//    for (int i = 0; i < randomness.size(); ++i)
//        table->setItem(row_num, i, new QTableWidgetItem(
//                QString(randomness[i] == 0 ? "< 0.001" : to_string(randomness[i]).c_str())));
//}


#include "main_window.h"

MainWindow::MainWindow() : QMainWindow(), lastSeed(-1) {
    std::setlocale(LC_ALL, "");
    Ui::MainWindow ui;
    ui.setupUi(this);

    auto *table_nums = this->findChild<QTableWidget *>("NumbersTableWidget");
    table_nums->setColumnCount(6);
    table_nums->setHorizontalHeaderLabels(
            {QString("1 разряд"), QString("2 разряда"), QString("3 разряда"), QString("1 разряд"), QString("2 разряда"),
             QString("3 разряда")});
    table_nums->horizontalHeader()->setSectionResizeMode(QHeaderView::Stretch);
    table_nums->setShowGrid(true);

    auto *table_headers = this->findChild<QTableWidget *>("MethodsHeaderTableWidget");
    table_headers->horizontalHeader()->setSectionResizeMode(QHeaderView::Stretch);
    table_headers->setShowGrid(true);

    randomGenerator = new RandomGenerator();

    setBindings();
}

void MainWindow::setBindings() {
    QObject::connect(this->findChild<QPushButton *>("GenerateNumbersPushButton"),
                     &QPushButton::clicked, this, &MainWindow::GenerateNumbersButtonClicked);
}

bool MainWindow::checkPositiveNumericField(QLineEdit *field) {
    bool valid = true;
    auto *palette = new QPalette();
    const std::string field_text = field->text().toStdString();
    std::size_t pos = 0;
    if (!field_text.empty()) {
        long val;
        try {
            val = std::stol(field_text, &pos);
            if (pos != field_text.size()) {
                palette->setColor(QPalette::Base, Qt::red);
                valid = false;
            } else
                palette->setColor(QPalette::Base, Qt::white);
        } catch (const std::invalid_argument &e) {
            palette->setColor(QPalette::Base, Qt::red);
            valid = false;
        }
        (void) val;
    } else {
        palette->setColor(QPalette::Base, Qt::red);
        valid = false;
    }
    field->setPalette(*palette);
    return valid;
}

void MainWindow::setTableColumn(QTableWidget *table, int column_number, const vector<int> &nums) {
    for (int i = 0; i < nums.size(); ++i) {
        table->setItem(i, column_number, new QTableWidgetItem(QString(to_string(nums[i]).c_str())));
    }
}

void MainWindow::GenerateNumbersButtonClicked() {
    auto count_line_edit = this->findChild<QLineEdit *>("CountInputLineEdit"),
            seed_line_edit = this->findChild<QLineEdit *>("SeedInputLineEdit");

    if (!checkPositiveNumericField(count_line_edit) ||
        (!seed_line_edit->text().isEmpty() && !checkPositiveNumericField(seed_line_edit)))
        return;
    int count = count_line_edit->text().toInt();

    int seed;
    if (!seed_line_edit->text().isEmpty()) {
        seed = seed_line_edit->text().toInt();
        if(lastSeed == -1 || lastSeed != seed)
            randomGenerator->setSeed(seed);
    }
    else
    {
        seed = randomGenerator->Seed();
    }

    lastSeed = seed;


    vector<int> one_digit_precalced = RandomGenerator::readOneDigitNumbers(count),
            two_digit_precalced = RandomGenerator::readTwoDigitNumbers(count),
            three_digit_precalced = RandomGenerator::readThreeDigitNumbers(count),
            one_digit_generated = randomGenerator->generateOneDigitNumbers(count),
            two_digit_generated = randomGenerator->generateTwoDigitNumbers(count),
            three_digit_generated = randomGenerator->generateThreeDigitNumbers(count);

    auto nums_table = this->findChild<QTableWidget *>("NumbersTableWidget");
    nums_table->setRowCount(0);
    nums_table->setRowCount(count + 1);
    setTableColumn(nums_table, 0, one_digit_precalced);
    setTableColumn(nums_table, 1, two_digit_precalced);
    setTableColumn(nums_table, 2, three_digit_precalced);
    setTableColumn(nums_table, 3, one_digit_generated);
    setTableColumn(nums_table, 4, two_digit_generated);
    setTableColumn(nums_table, 5, three_digit_generated);

    vector<double> randomness(6);
    randomness[0] = randomGenerator->CheckRandomness(one_digit_precalced, 0, 9);
    randomness[1] = randomGenerator->CheckRandomness(two_digit_precalced, 10, 99);
    randomness[2] = randomGenerator->CheckRandomness(three_digit_precalced, 100, 999);
    randomness[3] = randomGenerator->CheckRandomness(one_digit_generated, 0, 9);
    randomness[4] = randomGenerator->CheckRandomness(two_digit_generated, 10, 99);
    randomness[5] = randomGenerator->CheckRandomness(three_digit_generated, 100, 999);

    setLastTableRow(nums_table, count, randomness);
}

void MainWindow::setLastTableRow(QTableWidget *table, int row_num, const vector<double> &randomness) {
    table->setVerticalHeaderItem(row_num, new QTableWidgetItem(
            QString("Случайно при уровне alpha = ")));
    for (int i = 0; i < randomness.size(); ++i)
        table->setItem(row_num, i, new QTableWidgetItem(
                QString(randomness[i] == 0 ? "< 0.001" : to_string(randomness[i]).c_str())));
}



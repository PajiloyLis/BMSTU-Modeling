/********************************************************************************
** Form generated from reading UI file 'lab.ui'
**
** Created by: Qt User Interface Compiler version 6.2.4
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_MAIN_WINDOW_H
#define UI_MAIN_WINDOW_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QHeaderView>
#include <QtWidgets/QLabel>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QTableWidget>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_MainWindow
{
public:
    QWidget *centralwidget;
    QTableWidget *InputTableWidget;
    QLabel *label;
    QLineEdit *StatesCountLineEdit;
    QLabel *label_2;
    QTableWidget *PossibilityTableWidget;
    QLabel *label_3;
    QPushButton *CalcPushButton;
    QTableWidget *AverageTimeTableWidget;
    QLabel *label_4;
    QStatusBar *statusbar;

    void setupUi(QMainWindow *MainWindow)
    {
        if (MainWindow->objectName().isEmpty())
            MainWindow->setObjectName(QString::fromUtf8("MainWindow"));
        MainWindow->resize(1036, 590);
        centralwidget = new QWidget(MainWindow);
        centralwidget->setObjectName(QString::fromUtf8("centralwidget"));
        InputTableWidget = new QTableWidget(centralwidget);
        InputTableWidget->setObjectName(QString::fromUtf8("InputTableWidget"));
        InputTableWidget->setGeometry(QRect(10, 50, 431, 431));
        InputTableWidget->setVerticalScrollBarPolicy(Qt::ScrollBarAlwaysOff);
        InputTableWidget->setHorizontalScrollBarPolicy(Qt::ScrollBarAlwaysOff);
        InputTableWidget->setSizeAdjustPolicy(QAbstractScrollArea::AdjustToContents);
        InputTableWidget->horizontalHeader()->setMinimumSectionSize(10);
        InputTableWidget->horizontalHeader()->setDefaultSectionSize(10);
        label = new QLabel(centralwidget);
        label->setObjectName(QString::fromUtf8("label"));
        label->setGeometry(QRect(30, 520, 241, 31));
        QFont font;
        font.setPointSize(14);
        label->setFont(font);
        StatesCountLineEdit = new QLineEdit(centralwidget);
        StatesCountLineEdit->setObjectName(QString::fromUtf8("StatesCountLineEdit"));
        StatesCountLineEdit->setGeometry(QRect(280, 520, 121, 31));
        label_2 = new QLabel(centralwidget);
        label_2->setObjectName(QString::fromUtf8("label_2"));
        label_2->setGeometry(QRect(10, 10, 431, 31));
        label_2->setFont(font);
        label_2->setAlignment(Qt::AlignCenter);
        PossibilityTableWidget = new QTableWidget(centralwidget);
        PossibilityTableWidget->setObjectName(QString::fromUtf8("PossibilityTableWidget"));
        PossibilityTableWidget->setGeometry(QRect(480, 50, 241, 431));
        PossibilityTableWidget->horizontalHeader()->setMinimumSectionSize(10);
        PossibilityTableWidget->horizontalHeader()->setDefaultSectionSize(10);
        label_3 = new QLabel(centralwidget);
        label_3->setObjectName(QString::fromUtf8("label_3"));
        label_3->setGeometry(QRect(480, 10, 241, 31));
        label_3->setFont(font);
        label_3->setAlignment(Qt::AlignCenter);
        CalcPushButton = new QPushButton(centralwidget);
        CalcPushButton->setObjectName(QString::fromUtf8("CalcPushButton"));
        CalcPushButton->setGeometry(QRect(620, 500, 241, 51));
        QFont font1;
        font1.setPointSize(20);
        font1.setBold(true);
        CalcPushButton->setFont(font1);
        AverageTimeTableWidget = new QTableWidget(centralwidget);
        AverageTimeTableWidget->setObjectName(QString::fromUtf8("AverageTimeTableWidget"));
        AverageTimeTableWidget->setGeometry(QRect(760, 50, 241, 431));
        AverageTimeTableWidget->horizontalHeader()->setMinimumSectionSize(10);
        AverageTimeTableWidget->horizontalHeader()->setDefaultSectionSize(10);
        label_4 = new QLabel(centralwidget);
        label_4->setObjectName(QString::fromUtf8("label_4"));
        label_4->setGeometry(QRect(750, 10, 271, 31));
        label_4->setFont(font);
        label_4->setAlignment(Qt::AlignCenter);
        MainWindow->setCentralWidget(centralwidget);
        statusbar = new QStatusBar(MainWindow);
        statusbar->setObjectName(QString::fromUtf8("statusbar"));
        MainWindow->setStatusBar(statusbar);

        retranslateUi(MainWindow);

        QMetaObject::connectSlotsByName(MainWindow);
    } // setupUi

    void retranslateUi(QMainWindow *MainWindow)
    {
        MainWindow->setWindowTitle(QCoreApplication::translate("MainWindow", "MainWindow", nullptr));
        label->setText(QCoreApplication::translate("MainWindow", "\320\222\320\262\320\265\320\264\320\270\321\202\320\265 \321\207\320\270\321\201\320\273\320\276 \321\201\320\276\321\201\321\202\320\276\321\217\320\275\320\270\320\271:", nullptr));
        label_2->setText(QCoreApplication::translate("MainWindow", "\320\234\320\260\321\202\321\200\320\270\321\206\320\260 \320\270\320\275\321\202\320\265\320\275\321\201\320\270\320\262\320\275\320\276\321\201\321\202\320\265\320\271", nullptr));
        label_3->setText(QCoreApplication::translate("MainWindow", "\320\237\321\200\320\265\320\264\320\265\320\273\321\214\320\275\321\213\320\265 \320\262\320\265\321\200\320\276\321\217\321\202\320\275\320\276\321\201\321\202\320\270", nullptr));
        CalcPushButton->setText(QCoreApplication::translate("MainWindow", "\320\222\321\213\321\207\320\270\321\201\320\273\320\270\321\202\321\214", nullptr));
        label_4->setText(QCoreApplication::translate("MainWindow", "\320\241\321\200\320\265\320\264\320\275\320\265\320\265 \320\262\321\200\320\265\320\274\321\217 \320\262 \321\201\320\276\321\201\321\202\320\276\321\217\320\275\320\270\320\270 i", nullptr));
    } // retranslateUi

};

namespace Ui {
    class MainWindow: public Ui_MainWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_MAIN_WINDOW_H

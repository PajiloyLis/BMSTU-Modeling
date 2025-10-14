/********************************************************************************
** Form generated from reading UI file 'pseudorandom_numbers_generator.ui'
**
** Created by: Qt User Interface Compiler version 6.2.4
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_MAINWINDOW_H
#define UI_MAINWINDOW_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QHeaderView>
#include <QtWidgets/QLabel>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QTableWidget>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_MainWindow
{
public:
    QWidget *centralwidget;
    QLabel *label;
    QLineEdit *SeedInputLineEdit;
    QPushButton *GenerateNumbersPushButton;
    QTableWidget *NumbersTableWidget;
    QLineEdit *CountInputLineEdit;
    QLabel *label_2;
    QTableWidget *MethodsHeaderTableWidget;

    void setupUi(QMainWindow *MainWindow)
    {
        if (MainWindow->objectName().isEmpty())
            MainWindow->setObjectName(QString::fromUtf8("MainWindow"));
        MainWindow->resize(979, 600);
        QSizePolicy sizePolicy(QSizePolicy::Ignored, QSizePolicy::Preferred);
        sizePolicy.setHorizontalStretch(0);
        sizePolicy.setVerticalStretch(0);
        sizePolicy.setHeightForWidth(MainWindow->sizePolicy().hasHeightForWidth());
        MainWindow->setSizePolicy(sizePolicy);
        QFont font;
        font.setPointSize(14);
        MainWindow->setFont(font);
        centralwidget = new QWidget(MainWindow);
        centralwidget->setObjectName(QString::fromUtf8("centralwidget"));
        label = new QLabel(centralwidget);
        label->setObjectName(QString::fromUtf8("label"));
        label->setGeometry(QRect(30, 510, 121, 31));
        QFont font1;
        font1.setFamilies({QString::fromUtf8("Roboto Condensed")});
        font1.setPointSize(14);
        font1.setBold(true);
        label->setFont(font1);
        SeedInputLineEdit = new QLineEdit(centralwidget);
        SeedInputLineEdit->setObjectName(QString::fromUtf8("SeedInputLineEdit"));
        SeedInputLineEdit->setGeometry(QRect(260, 510, 181, 31));
        GenerateNumbersPushButton = new QPushButton(centralwidget);
        GenerateNumbersPushButton->setObjectName(QString::fromUtf8("GenerateNumbersPushButton"));
        GenerateNumbersPushButton->setGeometry(QRect(690, 510, 261, 71));
        QFont font2;
        font2.setFamilies({QString::fromUtf8("Roboto Condensed")});
        font2.setPointSize(20);
        font2.setBold(true);
        GenerateNumbersPushButton->setFont(font2);
        NumbersTableWidget = new QTableWidget(centralwidget);
        if (NumbersTableWidget->columnCount() < 6)
            NumbersTableWidget->setColumnCount(6);
        QTableWidgetItem *__qtablewidgetitem = new QTableWidgetItem();
        NumbersTableWidget->setHorizontalHeaderItem(0, __qtablewidgetitem);
        QTableWidgetItem *__qtablewidgetitem1 = new QTableWidgetItem();
        NumbersTableWidget->setHorizontalHeaderItem(1, __qtablewidgetitem1);
        QTableWidgetItem *__qtablewidgetitem2 = new QTableWidgetItem();
        NumbersTableWidget->setHorizontalHeaderItem(2, __qtablewidgetitem2);
        QTableWidgetItem *__qtablewidgetitem3 = new QTableWidgetItem();
        NumbersTableWidget->setHorizontalHeaderItem(3, __qtablewidgetitem3);
        QTableWidgetItem *__qtablewidgetitem4 = new QTableWidgetItem();
        NumbersTableWidget->setHorizontalHeaderItem(4, __qtablewidgetitem4);
        QFont font3;
        font3.setFamilies({QString::fromUtf8("Roboto")});
        QTableWidgetItem *__qtablewidgetitem5 = new QTableWidgetItem();
        __qtablewidgetitem5->setFont(font3);
        NumbersTableWidget->setHorizontalHeaderItem(5, __qtablewidgetitem5);
        if (NumbersTableWidget->rowCount() < 2000)
            NumbersTableWidget->setRowCount(2000);
        NumbersTableWidget->setObjectName(QString::fromUtf8("NumbersTableWidget"));
        NumbersTableWidget->setGeometry(QRect(20, 50, 931, 411));
        NumbersTableWidget->setContextMenuPolicy(Qt::NoContextMenu);
        NumbersTableWidget->setSizeAdjustPolicy(QAbstractScrollArea::AdjustToContentsOnFirstShow);
        NumbersTableWidget->setRowCount(2000);
        NumbersTableWidget->horizontalHeader()->setCascadingSectionResizes(true);
        NumbersTableWidget->horizontalHeader()->setProperty("showSortIndicator", QVariant(false));
        NumbersTableWidget->horizontalHeader()->setStretchLastSection(true);
        NumbersTableWidget->verticalHeader()->setCascadingSectionResizes(false);
        NumbersTableWidget->verticalHeader()->setStretchLastSection(true);
        CountInputLineEdit = new QLineEdit(centralwidget);
        CountInputLineEdit->setObjectName(QString::fromUtf8("CountInputLineEdit"));
        CountInputLineEdit->setGeometry(QRect(260, 550, 181, 31));
        label_2 = new QLabel(centralwidget);
        label_2->setObjectName(QString::fromUtf8("label_2"));
        label_2->setGeometry(QRect(30, 550, 221, 31));
        label_2->setFont(font1);
        MethodsHeaderTableWidget = new QTableWidget(centralwidget);
        if (MethodsHeaderTableWidget->columnCount() < 2)
            MethodsHeaderTableWidget->setColumnCount(2);
        QTableWidgetItem *__qtablewidgetitem6 = new QTableWidgetItem();
        MethodsHeaderTableWidget->setHorizontalHeaderItem(0, __qtablewidgetitem6);
        QTableWidgetItem *__qtablewidgetitem7 = new QTableWidgetItem();
        MethodsHeaderTableWidget->setHorizontalHeaderItem(1, __qtablewidgetitem7);
        MethodsHeaderTableWidget->setObjectName(QString::fromUtf8("MethodsHeaderTableWidget"));
        MethodsHeaderTableWidget->setGeometry(QRect(20, 20, 931, 31));
        MethodsHeaderTableWidget->horizontalHeader()->setStretchLastSection(true);
        MainWindow->setCentralWidget(centralwidget);
        label->raise();
        SeedInputLineEdit->raise();
        GenerateNumbersPushButton->raise();
        CountInputLineEdit->raise();
        label_2->raise();
        MethodsHeaderTableWidget->raise();
        NumbersTableWidget->raise();

        retranslateUi(MainWindow);

        QMetaObject::connectSlotsByName(MainWindow);
    } // setupUi

    void retranslateUi(QMainWindow *MainWindow)
    {
        MainWindow->setWindowTitle(QCoreApplication::translate("MainWindow", "Pseudorandom Numbers Generator", nullptr));
        label->setText(QCoreApplication::translate("MainWindow", "\320\222\320\262\320\265\320\264\320\270\321\202\320\265 seed:", nullptr));
        SeedInputLineEdit->setText(QString());
        GenerateNumbersPushButton->setText(QCoreApplication::translate("MainWindow", "\320\241\320\263\320\265\320\275\320\265\321\200\320\270\321\200\320\276\320\262\320\260\321\202\321\214 \321\207\320\270\321\201\320\273\320\260", nullptr));
        QTableWidgetItem *___qtablewidgetitem = NumbersTableWidget->horizontalHeaderItem(0);
        ___qtablewidgetitem->setText(QCoreApplication::translate("MainWindow", "1 \321\200\320\260\320\267\321\200\321\217\320\264", nullptr));
        QTableWidgetItem *___qtablewidgetitem1 = NumbersTableWidget->horizontalHeaderItem(1);
        ___qtablewidgetitem1->setText(QCoreApplication::translate("MainWindow", "2 \321\200\320\260\320\267\321\200\321\217\320\264\320\260", nullptr));
        QTableWidgetItem *___qtablewidgetitem2 = NumbersTableWidget->horizontalHeaderItem(2);
        ___qtablewidgetitem2->setText(QCoreApplication::translate("MainWindow", "3 \321\200\320\260\321\200\321\217\320\264\320\260", nullptr));
        QTableWidgetItem *___qtablewidgetitem3 = NumbersTableWidget->horizontalHeaderItem(3);
        ___qtablewidgetitem3->setText(QCoreApplication::translate("MainWindow", "1 \321\200\320\260\320\267\321\200\321\217\320\264", nullptr));
        QTableWidgetItem *___qtablewidgetitem4 = NumbersTableWidget->horizontalHeaderItem(4);
        ___qtablewidgetitem4->setText(QCoreApplication::translate("MainWindow", "2 \321\200\320\260\320\267\321\200\321\217\320\264\320\260", nullptr));
        QTableWidgetItem *___qtablewidgetitem5 = NumbersTableWidget->horizontalHeaderItem(5);
        ___qtablewidgetitem5->setText(QCoreApplication::translate("MainWindow", "3 \321\200\320\260\320\267\321\200\321\217\320\264\320\260", nullptr));
        CountInputLineEdit->setText(QString());
        label_2->setText(QCoreApplication::translate("MainWindow", "\320\222\320\262\320\265\320\264\320\270\321\202\320\265 \320\272\320\276\320\273\320\270\321\207\320\265\321\201\321\202\320\262\320\276 \321\207\320\270\321\201\320\265\320\273:", nullptr));
        QTableWidgetItem *___qtablewidgetitem6 = MethodsHeaderTableWidget->horizontalHeaderItem(0);
        ___qtablewidgetitem6->setText(QCoreApplication::translate("MainWindow", "\320\242\320\260\320\261\320\273\320\270\321\207\320\275\321\213\320\265 \320\267\320\275\320\260\321\207\320\265\320\275\320\270\321\217", nullptr));
        QTableWidgetItem *___qtablewidgetitem7 = MethodsHeaderTableWidget->horizontalHeaderItem(1);
        ___qtablewidgetitem7->setText(QCoreApplication::translate("MainWindow", "\320\232\320\262\320\260\320\264\321\200\320\260\321\202\320\270\321\207\320\275\321\213\320\271 \320\272\320\276\320\275\320\263\321\200\321\203\321\215\320\275\321\202\320\275\321\213\320\271 \320\274\320\265\321\202\320\276\320\264", nullptr));
    } // retranslateUi

};

namespace Ui {
    class MainWindow: public Ui_MainWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_MAINWINDOW_H

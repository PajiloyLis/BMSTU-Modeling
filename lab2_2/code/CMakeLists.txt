cmake_minimum_required(VERSION 3.31)
project(lab2_2)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)
#set(CMAKE_AUTOUIC ON)

set(CMAKE_PREFIX_PATH "/usr/lib/qt6/bin/qmake")

include_directories(/usr/include/qt6 ./UI)

find_package(Qt6 COMPONENTS
        Core
        Gui
        Widgets
        REQUIRED)

find_package(Eigen3 3.3 REQUIRED NO_MODULE)

add_executable(lab2_2 main.cpp
        main_window.cpp
        ui_main_window.h
#        RandomGenerator.cpp
#        RandomGenerator.h
        ui_main_window.h
        Gauss_Zeidel_solver.cpp
        Gauss_Zeidel_solver.h
        Markov_chain_calculator.cpp
        Markov_chain_calculator.h
)

target_link_libraries(lab2_2
        Qt::Core
        Qt::Gui
        Qt::Widgets
        Eigen3::Eigen
)
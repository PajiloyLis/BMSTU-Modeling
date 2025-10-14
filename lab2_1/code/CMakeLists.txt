cmake_minimum_required(VERSION 3.31)
project(lab2_1)

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

add_executable(lab2_1 main.cpp
        main_window.cpp
        main_window.h
        RandomGenerator.cpp
        RandomGenerator.h
        ui_mainwindow.h
)

target_link_libraries(lab2_1
        Qt::Core
        Qt::Gui
        Qt::Widgets
)

#include "main_window.h"


signed main(int argc, char *argv[])
{
  QApplication app(argc, argv);
  MainWindow window;
  window.show();
  return app.exec();
}
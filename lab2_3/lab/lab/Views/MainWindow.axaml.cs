using Avalonia.Controls;
using Avalonia.Markup.Xaml;
using lab.ViewModels;

namespace lab.Views;

public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
        
        if (DataContext == null)
        {
            DataContext = new MainWindowViewModel();
        }
        
    }
    
    private void InitializeComponent()
    {
        AvaloniaXamlLoader.Load(this);
    }
}
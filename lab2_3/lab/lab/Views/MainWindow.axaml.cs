using System;
using System.ComponentModel;
using System.Linq;
using Avalonia.Controls;
using Avalonia.Markup.Xaml;
using Avalonia.Threading;
using lab.Models;
using lab.ViewModels;
using ScottPlot;
using ScottPlot.Avalonia;

namespace lab.Views;

public partial class MainWindow : Window
{
    private MainWindowViewModel? _subscribedViewModel;

    public MainWindow()
    {
        InitializeComponent();

        // Подписываемся на изменения данных графика при загрузке окна
        Loaded += (sender, args) => { SubscribeToViewModel(); };
    }

    private void SubscribeToViewModel()
    {
        // Отписываемся от старого ViewModel если был
        if (_subscribedViewModel != null)
        {
            _subscribedViewModel.PropertyChanged -= OnViewModelPropertyChanged;
            _subscribedViewModel = null;
        }

        // Подписываемся на новый ViewModel
        if (DataContext is MainWindowViewModel viewModel)
        {
            _subscribedViewModel = viewModel;
            viewModel.PropertyChanged += OnViewModelPropertyChanged;

            // Также подписываемся на выполнение команды для прямого обновления
            viewModel.GenerateCommand.Subscribe(_ =>
            {
                // Небольшая задержка для гарантии, что данные обновлены
                Dispatcher.UIThread.Post(() =>
                {
                    if (viewModel.DensityPlot != null && viewModel.DensityPlot.Count > 0 &&
                        viewModel.DistributionPlot != null &&
                        viewModel.DistributionPlot.Count > 0)
                    {
                        UpdateDensityPlot(viewModel);
                        UpdateDistributionPlot(viewModel);
                    }
                }, DispatcherPriority.Normal);
            });
        }
    }

    private void OnViewModelPropertyChanged(object? sender, PropertyChangedEventArgs e)
    {
        if ((e.PropertyName == nameof(MainWindowViewModel.DensityPlot) ||
             e.PropertyName == nameof(MainWindowViewModel.DistributionPlot)) && sender is MainWindowViewModel viewModel)
        {
            UpdateDensityPlot(viewModel);
            UpdateDistributionPlot(viewModel);
        }
    }

    private void InitializeComponent()
    {
        AvaloniaXamlLoader.Load(this);
        DensityPlot = this.FindControl<AvaPlot>("DensityPlot");
        DistributionPlot = this.FindControl<AvaPlot>("DistributionPlot");
    }

    public void UpdateDensityPlot(MainWindowViewModel viewModel)
    {
        if (DensityPlot is null || viewModel is null)
            return;

        // Проверяем, находимся ли мы в UI потоке
        if (Dispatcher.UIThread.CheckAccess())
            UpdateDensityPlotInternal(viewModel);
        else
            // Если не в UI потоке, используем Post для асинхронного выполнения
            Dispatcher.UIThread.Post(() => UpdateDensityPlotInternal(viewModel));
    }


    private void UpdateDensityPlotInternal(MainWindowViewModel viewModel)
    {
        try
        {
            // Пытаемся найти элемент, если он еще не инициализирован
            if (DensityPlot == null) DensityPlot = this.FindControl<AvaPlot>("DensityPlot");

            if (DensityPlot == null || viewModel == null)
            {
                Console.WriteLine("DensityPlot is null or viewModel is null");
                return;
            }

            var plt = DensityPlot.Plot;
            plt.Clear();

            if (viewModel.DensityPlot != null && viewModel.DensityPlot.Count > 0)
            {
                Console.WriteLine($"Updating density plot with {viewModel.DensityPlot.Count} points");

                var x = viewModel.DensityPlot.Select(p => p.X).ToArray();
                var y = viewModel.DensityPlot.Select(p => p.Y).ToArray();

                if (viewModel.Distribution == DistributionType.Poisson)
                {
                    var markers = plt.Add.Markers(x, y);
                    markers.Color = Colors.Black;
                }
                else
                {
                    var scatter = plt.Add.Scatter(x, y);
                    scatter.Color = Colors.Black;
                    scatter.LineColor = Colors.Black;
                }
                plt.Title(viewModel.DensityPlotTitle);
                plt.XLabel("x");
                plt.YLabel("f(x)");
                

                DensityPlot.Refresh();
                Console.WriteLine("Density plot updated successfully");
            }
            else
            {
                Console.WriteLine("No data to density plot");
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error updating density plot: {ex.Message}");
        }
    }

    public void UpdateDistributionPlot(MainWindowViewModel viewModel)
    {
        if (DistributionPlot is null || viewModel is null)
            return;

        // Проверяем, находимся ли мы в UI потоке
        if (Dispatcher.UIThread.CheckAccess())
            UpdateDistributionPlotInternal(viewModel);
        else
            // Если не в UI потоке, используем Post для асинхронного выполнения
            Dispatcher.UIThread.Post(() => UpdateDistributionPlotInternal(viewModel));
    }

    private void UpdateDistributionPlotInternal(MainWindowViewModel viewModel)
    {
        try
        {
            // Пытаемся найти элемент, если он еще не инициализирован
            if (DistributionPlot == null) DistributionPlot = this.FindControl<AvaPlot>("DistributionPlot");

            if (DistributionPlot == null || viewModel == null)
            {
                Console.WriteLine("DistributionPlot is null or viewModel is null");
                return;
            }

            var plt = DistributionPlot.Plot;
            plt.Clear();

            if (viewModel.DistributionPlot != null && viewModel.DistributionPlot.Count > 0)
            {
                Console.WriteLine($"Updating distribution plot with {viewModel.DistributionPlot.Count} points");

                var x = viewModel.DistributionPlot.Select(p => p.X).ToArray();
                var y = viewModel.DistributionPlot.Select(p => p.Y).ToArray();
                var scatter = plt.Add.Scatter(x, y);
                plt.Title(viewModel.DistributionPlotTitle);
                plt.XLabel("x");
                plt.YLabel("F(x)");
                scatter.Color = Colors.Black;
                scatter.LineColor = Colors.Black;

                DistributionPlot.Refresh();
                Console.WriteLine("Distribution plot updated successfully");
            }
            else
            {
                Console.WriteLine("No data to distribution plot");
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error updating distribution plot: {ex.Message}");
        }
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reactive;
using Avalonia.Media;
using lab.Models;
using OxyPlot;
using OxyPlot.Axes;
using OxyPlot.Series;
using ReactiveUI;

namespace lab.ViewModels;

public class MainWindowViewModel : ReactiveObject
{
    private PlotModel _densityPlot;
    private DistributionType _distribution;
    private PlotModel _distributionPlot;
    private bool _hasValidationError;
    private double _mean;
    private double _variance;

    public MainWindowViewModel()
    {
        Distribution = DistributionType.Uniform;
        Mean = 0;
        Variance = 1;

        GenerateCommand = ReactiveCommand.Create(GenerateGraphs);
    }

    public bool HasValidationError
    {
        get => _hasValidationError;
        private set => this.RaiseAndSetIfChanged(ref _hasValidationError, value);
    }

    public bool HasMeanError { get; private set; }
    public bool HasVarianceError { get; private set; }

    public PlotModel DensityPlot
    {
        get => _densityPlot;
        set => this.RaiseAndSetIfChanged(ref _densityPlot, value);
    }

    public PlotModel DistributionPlot
    {
        get => _distributionPlot;
        set => this.RaiseAndSetIfChanged(ref _distributionPlot, value);
    }

    public DistributionType Distribution
    {
        get => _distribution;
        set
        {
            this.RaiseAndSetIfChanged(ref _distribution, value);
            Console.WriteLine($"Выбрано распределение: {value}");
        }
    }

    public double Mean
    {
        get => _mean;
        set
        {
            this.RaiseAndSetIfChanged(ref _mean, value);
            Console.WriteLine($"Mean: {value}");
        }
    }

    public double Variance
    {
        get => _variance;
        set
        {
            this.RaiseAndSetIfChanged(ref _variance, value);
            Console.WriteLine($"Variance {value}");
        }
    }

    public ReactiveCommand<Unit, Unit> GenerateCommand { get; }

    private void GenerateGraphs()
    {
        ResetValidation();

        ValidateMathematicalConditions();

        if (HasValidationError)
            return;

        DensityPlot = CreateDensityPlot(DistributionName());
    }

    private void ResetValidation()
    {
        HasValidationError = false;
        HasMeanError = false;
        HasVarianceError = false;

        this.RaisePropertyChanged(nameof(HasMeanError));
        this.RaisePropertyChanged(nameof(HasVarianceError));
    }


    private void ValidateMathematicalConditions()
    {
        if (_variance < 0)
        {
            HasValidationError = true;
            HasVarianceError = true;
        }

        if ((Distribution == DistributionType.Poisson || Distribution == DistributionType.Erlang ||
             Distribution == DistributionType.Exponential) && _mean < 0)
        {
            HasValidationError = true;
            HasMeanError = true;
        }
    }


    private PlotModel CreateDensityPlot(string distributionName)
    {
        var plot = new PlotModel
        {
            Title =
                $"{distributionName} график функции {(_distribution != DistributionType.Poisson ? "плотности" : "вероятности")}",
            TitleFontSize = 14,
            TitleFontWeight = FontWeights.Bold
        };

        var series = new LineSeries
        {
            Title = "f(x)",
            Color = OxyColors.Black,
            StrokeThickness = 2
        };

        var points = GenerateDensityPoints();
        
        foreach (var point in points)
        {
            series.Points.Add(point);
        }
            
            
        plot.Axes.Add(new LinearAxis
        {
            Position = AxisPosition.Bottom,
            Title = "x",
            MajorGridlineStyle = LineStyle.Solid
        });
            
        plot.Axes.Add(new LinearAxis
        {
            Position = AxisPosition.Left,
            Title = "f(x)",
            MajorGridlineStyle = LineStyle.Solid,
            Minimum = 0,
            Maximum = 1
        });
            
        return plot;
    }

    private DataPoint[] GenerateUniformDensity()
    {
        // Для U(a,b): mean = (a+b)/2, variance = (b-a)²/12
        var a = _mean - Math.Sqrt(3 * _variance);
        var b = _mean + Math.Sqrt(3 * _variance);
        var height = 1.0 / (b - a);

        var points = new List<DataPoint>();

        for (var x = 2 * a - b; x < a; x += 0.01) points.Add(new DataPoint(x, 0));

        points.Add(new DataPoint(a, 0));
        points.Add(new DataPoint(a, height));
        points.Add(new DataPoint(b, height));
        points.Add(new DataPoint(b, 0));

        for (var x = b + 0.01; x < 2 * b - a; x += 0.01) points.Add(new DataPoint(x, 0));

        return points.ToArray();
    }

    private DataPoint[] GenerateGaussianDensity()
    {
        var l = _mean - 4 * Math.Sqrt(_variance);
        var r = _mean + 4 * Math.Sqrt(_variance);

        var points = new List<DataPoint>();

        for (var x = l; x <= r; x += 0.01)
            points.Add(new DataPoint(x,
                1 / (Math.Sqrt(_variance * 2 * Math.PI) * Math.Exp(-Math.Pow(x - _mean, 2) / (2 * _variance)))));

        return points.ToArray();
    }

    private DataPoint[] GenerateExponentialDensity()
    {
        var lamba = 1 / _mean;
        // 99-th percentile
        var r = 4.6 / lamba;

        var points = new List<DataPoint>();
        for (var x = -1.0; x < 0; x += 0.01)
            points.Add(new DataPoint(x, 0));
        for (var x = 0.0; x <= r; x += 0.01)
            points.Add(new DataPoint(x, lamba * Math.Exp(-lamba * x)));

        return points.ToArray();
    }

    private DataPoint[] GeneratePoissonDensity()
    {
        var lamba = _mean;
        // 99-th percentile
        var kMax = (int)(lamba + 3 * Math.Sqrt(lamba));

        var points = new List<DataPoint>();
        for (var k = -5; k < 0; ++k)
            points.Add(new DataPoint(k, 0));
        points.Add(new DataPoint(0, Math.Exp(-lamba)));
        for (var k = 1; k <= kMax; ++k)
            points.Add(new DataPoint(k, points.Last().Y * lamba / k));

        return points.ToArray();
    }

    private DataPoint[] GenerateErlangDensity()
    {
        var lambda = _mean / _variance;
        var k = (int)(Math.Round(_mean * _mean / _variance));

        var factK = 1;
        for (int i = 2; i < k; ++i)
            factK *= i;
        var points = new List<DataPoint>();
        for(var x = -1.0; x < 0; x+=0.01)
            points.Add(new DataPoint(x, 0));

        double xMax;
        if (k == 1)
            xMax = 4.6 / lambda;
        else if (k <= 10)
            xMax = (k + 4 * Math.Sqrt(k)) * lambda;
        else if (k <= 50)
            xMax = (k + 3.5 * Math.Sqrt(k)) * lambda;
        else
            xMax = (k + 3 * Math.Sqrt(k)) * lambda;
        for (var x = 0.0; x <= xMax; x += 0.01)
        {
            points.Add(new DataPoint(x, (Math.Pow(lambda, k) * Math.Pow(x, k-1) * Math.Exp(-lambda*x))/factK));
        }

        return points.ToArray();
    }

    private DataPoint[] GenerateDensityPoints()
    {
        return _distribution switch
        {
            DistributionType.Uniform => GenerateUniformDensity(),
            DistributionType.Gaussian => GenerateGaussianDensity(),
            DistributionType.Exponential => GenerateExponentialDensity(),
            DistributionType.Poisson => GeneratePoissonDensity(),
            DistributionType.Erlang => GenerateErlangDensity(),
            _ => GenerateUniformDensity()
        };
    }

    private string DistributionName()
    {
        return _distribution switch
        {
            DistributionType.Uniform => "Равномерное распределение",
            DistributionType.Poisson => "Распределение Пуассона",
            DistributionType.Gaussian => "Нормальное распределение",
            DistributionType.Exponential => "Экспоненциальное распределение",
            DistributionType.Erlang => "Распределение Эрланга",
            _ => "Равномерное распределение"
        };
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reactive;
using System.Reactive.Linq;
using lab.Models;
using ReactiveUI;

namespace lab.ViewModels;

public class MainWindowViewModel : ReactiveObject
{
    private List<Point> _densityPlot;
    private string _densityPlotTitle;
    private DistributionType _distribution;
    private List<Point> _distributionPlot;
    private string _distributionPlotTitle;
    private bool _hasValidationError;
    private double _mean;
    private double _variance;

    public MainWindowViewModel()
    {
        Distribution = DistributionType.Uniform;
        Mean = 0;
        Variance = 1;

        // Инициализируем графики
        DensityPlot = new List<Point>();
        DistributionPlot = new List<Point>();
        DensityPlotTitle = "";

        // Создаем команду с указанием UI scheduler
        // Используем Observable.Return для создания observable, который выполняется в UI потоке
        var canExecute = Observable.Return(true)
            .ObserveOn(RxApp.MainThreadScheduler);

        GenerateCommand = ReactiveCommand.Create(
            GenerateGraphs,
            canExecute,
            RxApp.MainThreadScheduler);
    }

    public bool HasValidationError
    {
        get => _hasValidationError;
        private set => this.RaiseAndSetIfChanged(ref _hasValidationError, value);
    }

    public bool HasMeanError { get; private set; }
    public bool HasVarianceError { get; private set; }

    public List<Point> DensityPlot
    {
        get => _densityPlot;
        set => this.RaiseAndSetIfChanged(ref _densityPlot, value);
    }

    public List<Point> DistributionPlot
    {
        get => _distributionPlot;
        set => this.RaiseAndSetIfChanged(ref _distributionPlot, value);
    }

    public string DensityPlotTitle
    {
        get => _densityPlotTitle;
        private set => this.RaiseAndSetIfChanged(ref _densityPlotTitle, value);
    }

    public string DistributionPlotTitle
    {
        get => _distributionPlotTitle;
        private set => this.RaiseAndSetIfChanged(ref _distributionPlotTitle, value);
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

        CreateDensityPlot(DistributionName());
        CreateDistributionPlot(DistributionName());
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

        if (Distribution == DistributionType.Poisson && Math.Abs(Mean - Variance) > 1e-6)
        {
            HasValidationError = true;
            HasMeanError = true;
            HasVarianceError = true;
        }
    }

    private void CreateDensityPlot(string distributionName)
    {
        var points = GenerateDensityPoints();

        // Сохраняем данные для графика
        DensityPlot = points;

        // Сохраняем заголовок графика
        DensityPlotTitle =
            $"{distributionName} график функции {(_distribution != DistributionType.Poisson ? "плотности" : "вероятности")}";
    }

    private List<Point> GenerateUniformDensity()
    {
        var a = _mean - Math.Sqrt(3 * _variance);
        var b = _mean + Math.Sqrt(3 * _variance);
        var height = 1.0 / (b - a);

        var points = new List<Point>();

        points.Add(new Point(2 * a - b, 0));

        points.Add(new Point(a, 0));
        points.Add(new Point(a, height));
        points.Add(new Point(b, height));
        points.Add(new Point(b, 0));

        points.Add(new Point(2 * b - a, 0));

        return points;
    }

    private List<Point> GenerateUniformDistribution()
    {
        var a = _mean - Math.Sqrt(3 * _variance);
        var b = _mean + Math.Sqrt(3 * _variance);

        var points = new List<Point>();

        points.Add(new Point(2 * a - b, 0));

        points.Add(new Point(a, 0));
        points.Add(new Point(b, 1));

        points.Add(new Point(2 * b - a, 1));

        return points;
    }

    private List<Point> GenerateGaussianDensity()
    {
        var l = _mean - 4 * Math.Sqrt(_variance);
        var r = _mean + 4 * Math.Sqrt(_variance);

        var points = new List<Point>();

        for (var x = l; x <= r; x += 0.01)
            points.Add(new Point(x,
                Math.Exp(-Math.Pow(x - _mean, 2) / (2 * _variance)) / Math.Sqrt(_variance * 2 * Math.PI)));

        return points;
    }

    private List<Point> GenerateGaussianDistribution()
    {
        var l = _mean - 4 * Math.Sqrt(_variance) - 0.01;
        var r = _mean + 4 * Math.Sqrt(_variance) + 0.01;

        var densityPoints = new List<double>();

        for (var x = l; x <= r; x += 0.01)
            densityPoints.Add(Math.Exp(-Math.Pow(x - _mean, 2) / (2 * _variance)) / Math.Sqrt(_variance * 2 * Math.PI));

        var points = new List<Point>();
        for (var i = 1; i < densityPoints.Count - 1; ++i)
            points.Add(new Point(l + 0.01 * i,
                (i > 1 ? points[i - 2].Y : 0.0) + 0.01 * (densityPoints[i] + densityPoints[i - 1]) / 2.0));
        return points;
    }

    private List<Point> GenerateExponentialDensity()
    {
        var lamba = 1 / _mean;
        var r = 4.6 / lamba;

        var points = new List<Point>();
        points.Add(new Point(-1, 0));
        points.Add(new Point(0, -(1e-12)));
        for (var x = 0.0; x <= r; x += 0.01)
            points.Add(new Point(x, lamba * Math.Exp(-lamba * x)));

        return points;
    }

    private List<Point> GenerateExponentialDistribution()
    {
        var lamba = 1 / _mean;
        var r = 4.6 / lamba;

        var points = new List<Point>();
        points.Add(new Point(-1, 0));
        points.Add(new Point(0, 0));
        for (var x = 0.0; x <= r; x += 0.01)
            points.Add(new Point(x, 1 - Math.Exp(-lamba * x)));
        return points;
    }

    private List<Point> GeneratePoissonDensity()
    {
        var lamba = _mean;
        var kMax = (int)(lamba + 3 * Math.Sqrt(lamba));

        var points = new List<Point>();
        for (var k = -5; k < 0; ++k)
            points.Add(new Point(k, 0));

        points.Add(new Point(0, Math.Exp(-lamba)));
        for (var k = 1; k <= kMax; ++k)
            points.Add(new Point(k, points.Last().Y * lamba / k));

        return points;
    }

    private List<Point> GeneratePoissonDistribution()
    {
        var lamba = _mean;
        var kMax = (int)(lamba + 3 * Math.Sqrt(lamba));

        var points = new List<Point>();
        var pdfValues = new List<double>();
        for (var k = -5; k <= 0; ++k)
            points.Add(new Point(k, 0));

        points.Add(new Point(0, Math.Exp(-lamba)));
        pdfValues.Add(Math.Exp(-lamba));
        for (var k = 1; k <= kMax; ++k)
        {
            pdfValues.Add(pdfValues.Last() * lamba / k);
            points.Add(new Point(k-1e-6, points.Last().Y));
            points.Add(new Point(k, points.Last().Y + pdfValues.Last()));
        }
        points.Add(new Point(points.Last().X+1-1e-6, points.Last().Y));

        return points;
    }

    private List<Point> GenerateErlangDensity()
    {
        var lambda = _mean / _variance;
        var k = (int)Math.Round(_mean * _mean / _variance);

        var factK = 1;
        for (var i = 2; i < k; ++i)
            factK *= i;

        var points = new List<Point>();
        points.Add(new Point(-1, 0));
        points.Add(new Point(0, 0));
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
            points.Add(new Point(x, Math.Pow(lambda, k) * Math.Pow(x, k - 1) * Math.Exp(-lambda * x) / factK));

        return points;
    }

    private List<Point> GenerateErlangDistribution()
    {
        var lambda = _mean / _variance;
        var k = (int)Math.Round(_mean * _mean / _variance);

        var factorials = new List<int>();
        factorials.Add(1);
        for (var i = 1; i < k; ++i)
        {
            factorials.Add(factorials.Last() * i);
        }

        var points = new List<Point>();
        points.Add(new Point(-1, 0));
        points.Add(new Point(0, 0));

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
            var sumLambaXInNXFact = 1.0;
            for (var i = 1; i < k; ++i)
                sumLambaXInNXFact += Math.Pow(lambda * x, i)/factorials[i];
            points.Add(new Point(x, 1-Math.Exp(-lambda * x) * sumLambaXInNXFact));
        }

        return points;
    }

    private List<Point> GenerateDensityPoints()
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

    private List<Point> GenerateDistributionPoints()
    {
        return _distribution switch
        {
            DistributionType.Uniform => GenerateUniformDistribution(),
            DistributionType.Gaussian => GenerateGaussianDistribution(),
            DistributionType.Exponential => GenerateExponentialDistribution(),
            DistributionType.Poisson => GeneratePoissonDistribution(),
            DistributionType.Erlang => GenerateErlangDistribution(),
            _ => GenerateUniformDistribution()
        };
    }

    private void CreateDistributionPlot(string distributionName)
    {
        var points = GenerateDistributionPoints();

        // Сохраняем данные для графика
        DistributionPlot = points;

        // Сохраняем заголовок графика
        DistributionPlotTitle = $"{distributionName} график функции распределения";
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

// Вспомогательный класс Point
public class Point
{
    public Point(double x, double y)
    {
        X = x;
        Y = y;
    }

    public double X { get; set; }
    public double Y { get; set; }
}
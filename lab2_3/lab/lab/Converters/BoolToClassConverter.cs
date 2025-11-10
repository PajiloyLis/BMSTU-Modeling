using Avalonia.Data.Converters;
using System;
using System.Globalization;

namespace lab.Converters;

public class BoolToClassConverter : IValueConverter
{
    public string TrueValue { get; set; } = "error";
    public string FalseValue { get; set; } = "";

    public Object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        return value is bool boolValue && boolValue ? TrueValue : FalseValue;
    }

    public Object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        throw new NotSupportedException();
    }
}
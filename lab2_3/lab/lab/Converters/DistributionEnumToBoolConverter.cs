using System;
using System.Globalization;
using Avalonia.Data;
using Avalonia.Data.Converters;

namespace lab.Converters;

public class DistributionEnumToBoolConverter :IValueConverter
{
        public object? Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
        {
            return value?.Equals(parameter) ?? false;
        }

        public object? ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
        {
            return value is bool boolValue && boolValue ? parameter : BindingOperations.DoNothing;
        }
}
$u'' - 2xu' +u = x$

$u(0) = 0, u'(1) = 1$

$0 \le x \le 1$

Уравнение имеет вид:

$A[u(x)] = f(x)$

$u(a) = \alpha, u(b) = \beta$

$a \le x \le b$

Решение ищется в виде

$u(x) \approx y(x) = \phi_0(x) + \displaystyle\sum_{i=1}^{n} c_i \phi_i(x)$,

где $\phi_i(x), i = \overline {1, n}$ линейно независимы.

Выберем: $\phi_0(x) = x, \phi_0(0) = 0, \phi_0'(1) = 1$

Выберем $\phi_i(x) = x^i(x-1), \phi_i(0)=0, \phi_i'(1) = ix^{i-1}(x-1)+x^i = 1, i = \overline{1, n}$

Для определения $c_i$ составляется и решается система уравнений вида:

$\displaystyle\int_{a}^{b} (A[y(x)]-f(x))\phi_{i}(x)dx = 0, i=\overline{1, n}$


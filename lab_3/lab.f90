program lab
   implicit none

   interface
      subroutine dgesv(n, nrhs, a, lda, ipiv, b, ldb, info)
         integer, intent(in) :: n, nrhs, lda, ldb
         integer, intent(out) :: ipiv(*), info
         real(8), intent(inout) :: a(lda, *), b(ldb, *)
      end subroutine dgesv
   end interface

   real(8) :: a, b, alpha, beta, right_derivative_value
   integer :: n, i, k
   real(8), allocatable, dimension(:, :) :: coefs, Au, equations, A_inverted, Cs
   real(8), allocatable, dimension(:) :: phi
   real(8), allocatable :: residual(:)
   integer :: info
   integer, allocatable :: ipiv(:)
   real(8), allocatable :: A_matrix(:,:), b_matrix(:)

   a = 0.0; b = 0.0; alpha = 0.0; beta = 0.0

   call read_params(a, b, alpha, beta, n, "params.txt")

   ! print *, a
   ! print *, b
   ! print *, alpha
   ! print *, beta
   ! print *, n

   allocate(coefs(n+1, n+2))
   allocate(Au(n+1, n+2))
   allocate(equations(n, n+1))

   equations = 0.0_16
   coefs = 0.0_16
   Au = 0.0_16

   call y_view(a, b, alpha, beta, n, coefs)

   ! print *, "Coefs"

   ! do i = 1, n+1
   !    print *, coefs(i, :)
   ! end do

   call A_u(n, coefs, Au)

   allocate(phi(n+2))

   do k = 1, n
      phi = 0.0_16
      call phi_i(a, b, k, phi)
      ! print *, k
      ! print *, phi
      call form_ith_equation(Au, n+1, n+2, [0._8, 1._8], 2, phi, k+2, a, b, equations(k, :))
   end do

   ! print *, "Equations"

   ! do i = 1, n
      ! print *, equations(i, :)
   ! end do

   allocate(A_matrix(n, n))
   allocate(b_matrix(n))
   allocate(ipiv(n))

   do i = 1, n
      b_matrix(i) = real(equations(i, 1), kind=8)
      A_matrix(i, :) = real(equations(i, 2:n+1), kind=8)
   end do

   call dgesv(n, 1, A_matrix, n, ipiv, b_matrix, n, info)
   if (info == 0) then
       print *, "LU-решение Cs:"
       print *, b_matrix
   else
       print *, "Ошибка LU-разложения, info = ", info
   end if

   allocate(Cs(n, 1))
   do i = 1, n
      Cs(i, 1) = b_matrix(i)
   end do

   ! print *, "Cs"

   ! print *, Cs

   allocate(residual(n))
   call multiply_matr(equations(:, 2:n+1), Cs, n, n, 1, residual)
   residual = residual - equations(:, 1)
   print *, "Residual (A*Cs - b):"
   print *, residual

   call draw_plots(Cs, n, a, b, alpha, beta, 1000)

   contains
   function phi_0(a, b,  alpha, beta, x) result(res)
      implicit none
      real(8), intent(in) :: a, b
      real(8), intent(in) :: alpha
      real(8), intent(in) :: beta
      real(8), intent(in) :: x
      real(8) :: res
      res = alpha + beta*(x-a)
   end function phi_0
   
   subroutine phi_i(a, b, k, res)
      implicit none
      real(8), intent(in) :: a
      real(8), intent(in) :: b
      integer, intent(in) :: k
      real(8), intent(inout), dimension(k+2)  :: res
      integer, allocatable, dimension(:, :) :: triangle
      real(8), dimension(k+1) :: poli_1
      integer :: i
      allocate(triangle(k, k+1))
      triangle = 0.0
      call pascal_triangle(k, triangle)
      do i=1, k+1
         poli_1(i)=triangle(k, i)*(((-a)**(i-1)))
      end do
      call multiply_polynomails(poli_1, k+1, [-a+(k+1._8)*b, -1._8*k], 2, res)
   end subroutine phi_i
   
   subroutine multiply_polynomails(polinomial_1, n_1, polinomial_2, n_2, result)
      integer, intent(in) :: n_1, n_2
      real(8), intent(in), dimension(n_1) :: polinomial_1
      real(8), intent(in), dimension(n_2) :: polinomial_2
      real(8), intent(inout), dimension((n_1-1)+n_2) :: result
      integer i, j
      do i = 1, n_1
         do j = 1, n_2
            result((i-1)+(j-1)+1) = result((i-1)+(j-1)+1) + polinomial_1(i)*polinomial_2(j)
         end do
      end do
   end subroutine
   
   subroutine substract_polinomials(poli_1, n_1, poli_2, n_2, res)
      integer, intent(in) :: n_1, n_2
      real(8), intent(in), dimension(n_1) :: poli_1
      real(8), intent(in), dimension(n_2) :: poli_2
      real(8), intent(inout), dimension(max(n_1, n_2)) ::res
      do i = 1, max(n_1, n_2)
         if ( i <= min(n_1, n_2) ) then
            res(i) = poli_1(i)-poli_2(i)
         else
            if ( n_1 > n_2 ) then
               res(i) = poli_1(i)
            else
               res(i) = -poli_2(i)
            end if
         end if
      end do
   end subroutine substract_polinomials
   
   subroutine multiply_matr(matr1,  matr2, n, k, m, res)
      integer, intent(in) :: n, m, k
      real(8), intent(in), dimension(n, k) :: matr1
      real(8), intent(in), dimension(k, m) :: matr2
      real(8), intent(inout), dimension(n, m) :: res
      integer :: i, j, l
      do i = 1, n
         do j=1, m
            res(i, j) = 0._8
            do l = 1, k
               res(i, j) = res(i, j) + matr1(i, l)*matr2(l, j)
            end do
         end do
      end do
   end subroutine multiply_matr
   
   subroutine sum_polinomials(poli_1, n_1, poli_2, n_2, res)
      integer, intent(in) :: n_1, n_2
      real(8), intent(in), dimension(n_1) :: poli_1
      real(8), intent(in), dimension(n_2) :: poli_2
      real(8), intent(inout), dimension(max(n_1, n_2)) :: res
      do i = 1, max(n_1, n_2)
         if ( i<=min(n_1, n_2) ) then
            res(i) = poli_1(i)+poli_2(i)
         else
            if ( n_1>n_2 ) then
               res(i)=poli_1(i)
            else
               res(i)=poli_2(i)
            end if
         end if
      end do
   end subroutine sum_polinomials
   
   subroutine invert_matrix(matr, n, res)
      implicit none
      integer, intent(in) :: n
      real(8), intent(in), dimension(n, n) :: matr
      real(8), intent(inout), dimension(n, n) :: res
      real(8), dimension(n, 2*n) :: temp
      integer :: i, k
      res = 0.0
      do i = 1, n
         res(i, i) = 1.0
      end do
   
      temp(:,1:n) = matr
      temp(:,n+1:2*n) = res
   
      ! Прямой ход метода Гаусса
      do k = 1, n
         ! Нормировка строки
         temp(k,:) = temp(k,:) / temp(k,k)
   
         ! Исключение элементов
         do i = 1, n
            if (i /= k) then
               temp(i,:) = temp(i,:) - temp(i,k) * temp(k,:)
            end if
         end do
      end do
   
      ! Извлечение обратной матрицы
      res = temp(:,n+1:2*n)
   end subroutine invert_matrix
   
   ! form of array of tuples of view (coef_x_i, ...) for free and each C_i polinomial
   subroutine y_view(a, b, alpha, beta, n, coefs)
      implicit none
      real(8), intent(in)::a, b, alpha, beta
      integer, intent(in) :: n
      real(8), intent(inout), dimension(n+1, n+2) :: coefs
      real(8), allocatable, dimension(:) :: poli_1, res
      real(8), dimension(2) :: poli_2
      integer, allocatable, dimension(:, :) :: triangle
      integer :: i, j
      allocate(triangle(n, n+1))
      triangle = 0.0
      call pascal_triangle(n, triangle)
      coefs(1, :) = [alpha-a*beta, beta]
      allocate(poli_1(n+1))
      poli_1 = 0.0
      allocate(res(n+2))
      res = 0.0
      do i = 2, n+1
         poli_2=[-a+(1._8+(i-1))*b, -1._8*(i-1)]
         poli_1=0.0
         res=0.0
         do j = 1, i
            poli_1(j) = triangle(i-1, j) * ((-a)**(j-1))
         end do
         call multiply_polynomails(poli_1, i, poli_2, 2, res)
         coefs(i, 1:i+1) = res(1:i+1)
      end do
   end subroutine
   
   subroutine differentiate_poli(poli, n, res)
      integer, intent(in) :: n
      real(8), intent(in), dimension(n+1) :: poli
      real(8), intent(inout), dimension(n) :: res
      integer :: i
      do i = 1, n
         res(i) = poli(i+1)*(i)
      end do
   end subroutine differentiate_poli
   
   subroutine A_u(n, coefs, res)
      integer, intent(in) :: n
      real(8), intent(in), dimension(n+1, n+2) ::  coefs
      real(8), intent(inout), dimension(n+1, n+2) :: res
      real(8), allocatable, dimension(:, :) :: first_derivative
      real(8), allocatable, dimension(:, :) :: second_derivative
      real(8), allocatable, dimension(:, :) :: multiplied_first_derivative
      real(8), allocatable, dimension(:, :) :: substracted
      integer :: i
      allocate(multiplied_first_derivative(n+1, n+2))
      allocate(substracted(n+1, n+2))
      allocate(first_derivative(n+1, n+1))
      allocate(second_derivative(n+1, n))
      first_derivative=0.0
      second_derivative=0.0
      multiplied_first_derivative = 0.0
      substracted = 0.0
      do i = 1, n+1
         call differentiate_poli(coefs(i, :), n+1, first_derivative(i, :))
         call differentiate_poli(first_derivative(i, :), n, second_derivative(i, :))
      end do
   
      ! print *, "First derivative"
   
      ! do i = 1, n+1
      !    print *, first_derivative(i, :)
      ! end do
   
      ! print *, "Second derivative"
   
      ! do i = 1, n+1
      !    print *, second_derivative(i, :)
      ! end do
   
      do i = 1, n+1
         call multiply_polynomails(first_derivative(i, :), n+1, [0._8, 2._8], 2, multiplied_first_derivative(i, :))
      end do
   
      ! print *, "Multiplied first derivative"
   
      ! do i = 1, n+1
      !    print *, multiplied_first_derivative(i, :)
      ! end do
   
      do i = 1, n+1
         call substract_polinomials(second_derivative(i, :), n, multiplied_first_derivative(i, :), n+2, substracted(i, :))
      end do
   
      ! print *, "Substracted"
   
      ! do i = 1, n+1
      !    print *, substracted(i, :)
      ! end do
   
      do i = 1, n+1
         call sum_polinomials(substracted(i, :), n+2, coefs(i, :), n+2, res(i, :))
      end do
   
      ! print *, "A[u(x)]"
   
      ! do i = 1, n+1
      !    print *, res(i, :)
      ! end do
   
   end subroutine A_u
   
   subroutine form_ith_equation(Au_coefs, Au_rows, Au_cols,  fx, fx_n, phi_i, phi_i_n, x_min, x_max, res)
      implicit none
      integer, intent(in) :: Au_rows, Au_cols, fx_n, phi_i_n
      real(8), intent(in), dimension(Au_rows, Au_cols) :: Au_coefs
      real(8), intent(in), dimension(fx_n) :: fx
      real(8), intent(in), dimension(phi_i_n) :: phi_i
      real(8), intent(in) :: x_min, x_max
      real(8), intent(inout), dimension(Au_rows) :: res
      real(8), allocatable, dimension(:, :) :: substracted, multiplied
      integer :: i
      allocate(substracted(Au_rows, max(Au_cols, fx_n)))
      allocate(multiplied(Au_rows, Au_cols+phi_i_n-1))
      substracted=0.0
      multiplied=0.0
      call substract_polinomials(Au_coefs(1, :), Au_cols, fx, fx_n, substracted(1, :))
      do i = 2, Au_rows
         substracted(i, :) = Au_coefs(i, :)
      end do
      do i = 1, Au_rows
         call multiply_polynomails(substracted(i, :), max(Au_cols, fx_n), phi_i, phi_i_n, multiplied(i, :))
      end do
   
      ! print *, "A[u(x)]-f(x)"
   
      ! do i = 1, Au_rows
      !    print *, substracted(i, :)
      ! end do
   
      ! print *, "(A[u(x)] - f(x))*phi_i(x)"
   
      ! do i = 1, Au_rows
      !    print *, multiplied(i, :)
      ! end do
   
      do i = 1, Au_rows
         call definite_integrate_poli(multiplied(i, :), Au_cols+phi_i_n-1, x_min, x_max, res(i))
      end do
   
      ! print *, "integrate(a)(b)[(A[u(x)] - f(x))*phi_i(x)]"
   
      ! print *, res
   end subroutine
   
   subroutine definite_integrate_poli(poli, poli_n, x_min, x_max, res)
      integer, intent(in) :: poli_n
      real(8), intent(in), dimension(poli_n) :: poli
      real(8), intent(in) :: x_min, x_max
      real(8), intent(inout) :: res
      integer :: i
      res = 0.0
      do i = 1, poli_n
         res = res + (poli(i) * (x_max ** (i) - x_min ** (i)) / (i))
      end do
   end subroutine definite_integrate_poli
   
   subroutine pascal_triangle(n, mat)
      implicit none
      integer, intent(in) :: n
      integer, intent(inout) :: mat(n,n+1)
      integer :: i, j
   
      mat(1, 1) = 1
      mat(1, 2) = 1
      do i = 2, n
         mat(i, 1) = 1
         do j = 2, i+1
            mat(i, j) = mat(i-1, j-1)+mat(i-1, j)
         end do
      end do
   end subroutine
   
   subroutine read_params(a, b, alpha, beta, n, filename)
      implicit none
      real(8), intent(inout) :: a, b, alpha, beta
      integer, intent(inout) :: n
      character(len = 10), intent(in) :: filename
      character(len = 10) dummy
      integer:: fileunit, stat
   
      ! print *, filename
   
      open(newunit=fileunit, file=filename, status='old', iostat=stat)
      if ( stat /= 0 ) then
         print *, "error open file"
         return
      end if
      read(fileunit, *) dummy, dummy, a
      read(fileunit, *) dummy, dummy, b
      read(fileunit, *) dummy, dummy, alpha
      read(fileunit, *) dummy, dummy, beta
      read(fileunit, *) dummy, dummy, n
      close(fileunit)
   end subroutine read_params
   
   subroutine draw_plots(Cs,  n, a, b, alpha, beta, points_cnt)
      use :: plplot
      implicit none
      integer, intent(in) :: n, points_cnt
      real(8), intent(in) :: a, b, alpha, beta
      real(8), intent(in), dimension(n) :: Cs
      real(plflt), dimension(points_cnt) :: x, y
      real(plflt) :: xmin, xmax, ymin, ymax
      integer :: i, j
      xmin = 0._plflt
      xmax = 1._plflt
      ymin = 0._plflt
      ymax = 0._plflt
      x(1) = a
      y(1) = alpha
      do i = 1, points_cnt-1
         x(i+1) = a+i*(b-a)/(points_cnt-1)
         y(i+1) = alpha - a*beta + beta*x(i+1)
         do j = 1, n
            y(i+1) = y(i+1) + Cs(j) * ((x(i+1)-a)**j)*(x(i+1) - a + (j+1._8)*(b-x(i+1)))
         end do
         ymin = min(y(i+1), ymin)
         ymax = max(y(i+1), ymax)
      end do
      print *, "y(points_cnt)"
      print *, y(points_cnt)
      print *, "y(points_cnt-1)"
      print *, y(points_cnt-1)
      print *, "x(points_cnt)"
      print *, x(points_cnt)
      print *, "x(points_cnt-1)"
      print *, x(points_cnt-1)
      print *, "y(points_cnt) - y(points_cnt-1)"
      print *, y(points_cnt) - y(points_cnt-1)
      print *, "x(points_cnt) - x(points_cnt-1)"
      print *, x(points_cnt) - x(points_cnt-1)
      print *, (y(points_cnt) - y(points_cnt-1))/(x(points_cnt) - x(points_cnt-1))
      call plscolbg(255, 255, 255)
      call plscol0(1, 0, 0, 0)
      call plsdev("svg")
      call plsfnam("./plot.svg")
      call plinit()
      call plenv(xmin, xmax, ymin, ymax, 0, 0)
      call pllab('X', 'Y', 'График полученного полинома')
      call plline(x, y)
      call plend()
   end subroutine draw_plots
      
end program lab

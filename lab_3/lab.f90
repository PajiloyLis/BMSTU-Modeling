function phi_0(a, alpha, beta, x) result(res)
   implicit none
   real, intent(in) :: a
   real, intent(in) :: alpha
   real, intent(in) :: beta
   real, intent(in) :: x
   real :: res
   res = beta*x+alpha-beta*a
end function phi_0

function phi_i(a, b, i, x) result(res)
   implicit none
   real, intent(in) :: a
   real, intent(in) :: b
   integer, intent(in) :: i
   real, intent(in) :: x
   real :: res
   res = (b - x)*(x-a)**i
end function phi_i

subroutine multiply_polynomails(polinomial_1, n_1, polinomial_2, n_2, result)
   integer, intent(in) :: n_1, n_2
   real, intent(in), dimension(n_1) :: polinomial_1
   real, intent(in), dimension(n_2) :: polinomial_2
   real, intent(inout), dimension((n_1-1)+(n_2-1)) :: result
   integer i, j
   do i = 1, n_1
      do j = 1, n_2
         result((i-1)+(j-1)+1) = polinomial_1(i)*polinomial_2(j)
      end do
   end do
end subroutine

! form of array of tuples of view (coef_x_i, ...) for free and each C_i polinomial
subroutine y_view(a, b, alpha, beta, n, coefs)
   implicit none
   real, intent(in)::a, b, alpha, beta
   integer, intent(in) :: n
   real, intent(inout), dimension(n+1, n+2) :: coefs
   real, allocatable, dimension(:) :: poli_1, res
   real, dimension(2) :: poli_2
   integer, allocatable, dimension(:, :) :: triangle
   integer :: i, j
   poli_2 = [b, -1.]
   allocate(triangle(n, n+1))
   triangle = 0.0
   call pascal_triangle(n, triangle)
   coefs(1, :) = [a - a * (beta-alpha)/(b - a), (beta - alpha)/(b - a)]
   allocate(poli_1(n+1))
   poli_1 = 0.0
   allocate(res(n+2))
   res = 0.0
   do i = 2, n+1
      do j = 1, i
         poli_1(j) = triangle(i-1, j) * ((-a)**(i-j))
      end do
      call multiply_polynomails(poli_1, i, poli_2, 2, res)
      coefs(i, 1:i+1) = res(1:i+1)
   end do
end subroutine

subroutine differentiate_poli(poli, n, res)
   integer, intent(in) :: n
   real, intent(in), dimension(n+1) :: poli
   real, intent(inout), dimension(n) :: res
   integer :: i
   do i = 1, n
      res(i) = poli(i+1)*(i)
   end do
end subroutine differentiate_poli

subroutine A_u(n, coefs, res)
   integer, intent(in) :: n
   real, intent(in), dimension(n+1, n+2) ::  coefs
   ! TODO dimension
   real, intent(inout), dimension(n+1:n+2) :: res
   real, allocatable, dimension(:, :) :: first_derivative
   real, allocatable, dimension(:, :) :: second_derivative
   integer :: i
   allocate(first_derivative(n+1, n+1))
   allocate(second_derivative(n+1, n))
   first_derivative=0.0
   second_derivative=0.0

   do i = 1, n+1
      call differentiate_poli(coefs(i, :), n, first_derivative(i, :))
      call differentiate_poli(first_derivative(i, :), n, second_derivative(i, :))
   end do

end subroutine A_u


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
   real, intent(inout) :: a, b, alpha, beta
   integer, intent(inout) :: n
   character(len = 10), intent(in) :: filename
   character(len = 10) dummy
   integer:: fileunit, stat

   print *, filename

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

program lab
   implicit none
   real :: a, b, alpha, beta
   integer :: n, i
   real, allocatable, dimension(:, :) :: coefs;


   a = 0.0; b = 0.0; alpha = 0.0; beta = 0.0

   call read_params(a, b, alpha, beta, n, "params.nml")

   print *, a
   print *, b
   print *, alpha
   print *, beta
   print *, n

   allocate (coefs(n+1, n+2))

   coefs = 0.0

   call y_view(a, b, alpha, beta, n, coefs)

   do i = 1, n+1
      print *, coefs(i, :)
   end do

end program lab

using PrettyTables

# function euler(x0, y0, y_derivative_0, step, func, eps)
#     no_disperancy = true
#     y = y0
#     xs = [x0]
#     ys = [y0]
#     p = y_derivative_0
#     half_p = y_derivative_0
#     half_y = y0
#     # println(x0, " ", y)
#     while true
#         x0 += step
#         half_p = func(x0, y, half_p, step/2.)
#         half_y += half_p*step/2.
#         half_p = func(x0+step/2., half_y, half_p, step/2.)
#         half_y += half_p*step/2.
#         p = func(x0, y, p, step)
#         y += step * p
#         # println(x0," ", y)
#         if x0 == Inf || x0 > 10.0
#             break
#         end
#         if abs(y - half_y)/half_y > eps
#             # println(y, " ", half_y, " ", abs(y - half_y)/half_y)
#             no_disperancy = false
#         end

#         push!(xs, x0)
#         push!(ys, y)
#     end
#     xs, ys, x0, no_disperancy
# end

function p_derivative(prev_x, prev_y, prev_p)
    if(prev_x ==0.0)
        prev_x = 1e-7
    end
    derivative_p = (-2prev_p-prev_y)/(4prev_x)
end

# function y_derivative(prev_x, prev_y, prev_p, x_step)
#     new_p = prev_p + x_step *p_derivative(prev_x, prev_y, prev_p)
# end

function analityc(xs)
    ys = @. cos(sqrt(xs))
end;

# best_step = 0.1
# euler_xs = []
# euler_yss = []
# for i in [0.1, 0.05, 0.01, 0.005, 0.001, 0.0005, 0.0001, 0.00005, 0.00001]
#     global best_step = i
#     global euler_xs, euler_ys, x0, no_disperancy =  euler(0.0, 1., -0.5, best_step, y_derivative, 0.5)
#     push!(euler_yss, euler_ys)
#     if no_disperancy
#         break
#     end
# end

# data = []
# push!(data, euler_xs)
# push!(data, [])
# for i in 1:length(euler_xs)
#     if i > 1 && i < length(euler_xs) && (i - 1) % 2 == 0
#         push!(data[2], euler_yss[1][div(i, 2)+1])
#     elseif i==1
#         push!(data[2], euler_yss[1][i])
#     else
#         push!(data[2], nothing)    
#     end
# end
# push!(data, euler_yss[2])
# push!(data, analityc(euler_xs))
# table = hcat(data[1], data[2], data[3], data[4])
# header = ["x", "h = 0.1", "h = 0.05", "y(x)"]
# pretty_table(table; backend = Val(:markdown), header=header)

# function runge_kutta(x0, y0, abs_x_max, y_derivative_0, step, func, alpha)
#     # no_disperancy = true
#     y = y0
#     xs = [x0]
#     ys = [y0]
#     p = y_derivative_0
#     # half_p = y_derivative_0
#     # half_y = y0
#     # println(x0, " ", y)
#     while true
#         # x0 += step
#         # half_p = func(x0, y, half_p, step/2.)
#         # half_y += half_p*step/2.
#         # half_p = func(x0+step/2., half_y, half_p, step/2.)
#         # half_y += half_p*step/2.
#         # p = func(x0, y, p, step, alpha)
#         p_temp = func(x0, y, p, step, alpha)
#         y += step*((1.0 - alpha)*func(x0, y, p, step, alpha) + 
#         alpha*func(x0 + step/(2alpha), y + step*p/(2alpha), p+step*func(x0, y, p, step/(2alpha), alpha)/(2alpha), step/(2alpha), alpha))
#         x0 += step
#         p=p_temp
#         # println(x0," ", y)
#         if y == Inf || abs(x0) > abs_x_max
#             break
#         end
#         # println(y, " ", half_y)
#         # if abs(y - half_y)/half_y >= eps
#         #     no_disperancy = false
#         # end

#         push!(xs, x0)
#         push!(ys, y)
#     end
#     xs, ys, x0
# end;

function runge_kutta(x0, y0, abs_x_max, y_derivative_0, step, alpha)
    # no_disperancy = true
    xs = [x0]
    ys = [y0]
    p = y_derivative_0
    while true
        x0 = (xs[end] == 0.0 ? 1e-7 : xs[end])
        new_y = y0 + step*((1 - alpha)p + alpha*(p + step * p_derivative(x0, y0, p) / (2alpha)))
        new_p = p + step*((1 - alpha)p_derivative(x0, y0, p) +
                         alpha * p_derivative(x0 + step / (2alpha),
            y0 + step * p / (2alpha),
            p + step * p_derivative(x0, y0, p) / (2alpha)))
        p = new_p
        y0 = new_y
        x0 += step
        if y0 == Inf || abs(x0) > abs_x_max
            break
        end
        push!(xs, x0)
        push!(ys, y0)
    end
    xs, ys, x0
end;

function y_derivative_runge_kutta(prev_x, prev_y, prev_p, x_step, alpha)
    # new_p = prev_p + x_step *p_derivative(prev_x, prev_y, prev_p)
    new_p = prev_p + 
        x_step * ((1.0 - alpha)*p_derivative(prev_x, prev_y, prev_p) + 
            alpha*p_derivative(prev_x+x_step/(2*alpha), 
                                prev_y+x_step*prev_p/(2*alpha), 
                                prev_p+x_step * p_derivative(prev_x, prev_y, prev_p)/(2*alpha)))
end;

best_alpha = 1.0
runge_kutta_xs = []
runge_kutta_yss = []
for i in [1.0, 0.5]
    global runge_kutta_xs, runge_kutta_ys, x0 =  runge_kutta(0.0, 1.0, 10.0, -0.5, 0.01, i)
    push!(runge_kutta_yss, runge_kutta_ys)
end

data = []
push!(data, runge_kutta_xs[1:5:end])
push!(data, runge_kutta_yss[1][1:5:end])
push!(data, runge_kutta_yss[2][1:5:end])
push!(data, analityc(runge_kutta_xs)[1:5:end])
table = hcat(data[1], data[2], data[3], data[4])
header = ["x", "alpha = 1.0", "alpha = 0.5", "y(x)"]
pretty_table(table; backend = Val(:markdown), header=header)

alpha1_mean_err =sum(abs.(analityc(runge_kutta_xs) - runge_kutta_yss[1]))/length(runge_kutta_xs)
alpha1_max_err = maximum(abs.(analityc(runge_kutta_xs) - runge_kutta_yss[1]))
alpha05_mean_err = sum(abs.(analityc(runge_kutta_xs) - runge_kutta_yss[2]))/length(runge_kutta_xs)
alpha05_max_err = maximum(abs.(analityc(runge_kutta_xs) - runge_kutta_yss[2]))
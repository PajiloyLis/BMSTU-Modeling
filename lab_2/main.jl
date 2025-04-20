using Plots;
using Pkg;

c = 	BigFloat("3e10")
t_w = 	BigFloat("2e3")
t_0 = 	BigFloat("1e4")
p = 	BigFloat("4")
R = 	BigFloat("0.35")

t_s = [	BigFloat("2e3"), 
		BigFloat("3e3"),
		BigFloat("4e3"),
		BigFloat("5e3"),
		BigFloat("6e3"),
		BigFloat("7e3"),
		BigFloat("8e3"),
		BigFloat("9e3"),
		BigFloat("1e4")]

k_ts_1 = [	BigFloat("8.200E-03"), 
        BigFloat("2.768E-02"),
        BigFloat("6.560E-02"),
        BigFloat("1.281E-01"),
        BigFloat("2.214E-01"),
        BigFloat("3.516E-01"),
        BigFloat("5.248E-01"),
        BigFloat("7.472E-01"),
        BigFloat("1.025E+00")]

k_ts_2 = [	BigFloat("1.600E+00"),
        BigFloat("5.400E+00"),
        BigFloat("1.280E+01"),
        BigFloat("2.500E+01"),
        BigFloat("4.320E+01"),
        BigFloat("6.860E+01"),
        BigFloat("1.024E+02"),
        BigFloat("1.458E+02"),
        BigFloat("2.000E+02")]


# plot(t_s, k_ts_1)
# plot!(t_s, k_ts_2)

# log_k_ts_1 = @. log(k_ts_1)
# log_k_ts_2 = @. log(k_ts_2)
# log_ts = @. log(t_s)

# plot(log_ts, log_k_ts_1)
# plot!(log_ts, log_k_ts_2)

function t(r)
    return (t_w-t_0)*((r/R)^p)+t_0
end

function k_linear(T, ks)
    if T<=t_s[1]
        return t_s[1]
    elseif T >= t_s[end]
        return t_s[end]
    end
    left = searchsortedlast(t_s, T)
	right = left + 1
    return exp(linear_interpolation(log(T), log(t_s[left]), log(t_s[right]), log(ks[left]), log(ks[right])))
end

function u_p(r)
    return 3.084e-4/(exp(4.799e4/t(r))-1)
end

function linear_interpolation(x, x0, x1, y0, y1)
    return y0+(y1-y0)*(x-x0)/(x1-x0)
end

function du_equation(ks, r, F)
    return -(3k_linear(t(r), ks)/c)*F
end

function df_equation(r, F, u, ks)
    if(r==BigFloat("0.0"))
        return -c * k_linear(t(r), ks)*(u-u_p(r))/2
    end
    return -c * k_linear(t(r), ks)*(u-u_p(r)) - F/r
end

function runge_kutta_2(r_min, r_max, u_min, F_min, steps, ks)
    h = (r_max-r_min)/steps
    r = r_min
    u = u_min
    F=F_min
	us = [u]
    rs = [r]
    Fs = [F]

    for i in 1:steps
        k1_u = du_equation(ks, r, F)
        k1_F = df_equation(r, F, u, ks)

        u_mid = u + h*k1_u/2.0
        F_mid = F + h*k1_F/2.0
        r_mid = r+h/2.0

        k2_u = du_equation(ks, r_mid, F_mid)
        k2_F = df_equation(r_mid, F_mid, u_mid, ks)

        u+=h*k2_u
        F+=h*k2_F
        r+=h
        push!(us, u)
        push!(rs, r)
        push!(Fs, F)
    end
    return rs, us, Fs
end

function runge_kutta_4(r_min, r_max, u_min, F_min, steps, ks)
    h = (r_max - r_min) / steps
    r = r_min
	F = F_min
	u = u_min
    us = [u]
    rs = [r]
    Fs = [F]

    for i in 1:steps
        k1_u = du_equation(ks, r, F)
        k1_F = df_equation(r, F, u, ks)

        k2_u = du_equation(ks, r + h / 2, F + k1_F * h / 2)
        k2_F = df_equation(r + h / 2, F + k1_F * h / 2, u + k1_u * h / 2, ks)

        k3_u = du_equation(ks, r + h / 2, F + h * k2_F / 2)
        k3_F = df_equation(r + h / 2, F + h * k2_F / 2, u + h * k2_u / 2, ks)

        k4_u = du_equation(ks, r + h, F + h * k3_F)
        k4_F = df_equation(r + h, F + h * k3_F, u + h * k3_u, ks)

        u += h / 6 * (k1_u + 2k2_u + 2k3_u + k4_u)
        F += h / 6 * (k1_F + 2k2_F + 2k3_F + k4_F)
        r += h

        push!(us, u)
        push!(rs, r)
        push!(Fs, F)
    end
    return rs, us, Fs
end

function shooting(r_min, r_max, F0, steps, runge_kutta, ks, filename1, filename2, filename3, hi_min=BigFloat("0.1"), hi_max=BigFloat("0.9"), eps=BigFloat("1e-10"), bin_iter_cnt=1e3)
    hi_mid = BigFloat("0.0")
    rs=[]
    us=[]
    Fs=[]
    open("funcs.txt", "w") do f
        open("vals.txt", "w") do f1
            u_p0 = u_p(r_min)
            function calc_err(hi)
                u0 = hi * u_p0
                rs, us, Fs = runge_kutta(r_min, r_max, u0, F0, steps, ks)
                # plot(rs, Fs, label="F")
                # savefig("shit.png")
                # plot(rs, us, label="u");
                # savefig("shit1.png")
                r_final = rs[end]
                u_final = us[end]
                F_final = Fs[end]
                print(f, r_final, " ", u_final, " ", F_final, "\n")
                return (0.39 * u_final * c - F_final), rs, us, Fs
            end



            err_min, rs, us, Fs = calc_err(hi_min)
            err_max, rs, us, Fs = calc_err(hi_max)

            if(err_min*err_max> 0)
                error("Все плохо")
            end

            print(err_min, " ", err_max, "\n")
            
            hi_mid = (hi_min + hi_max) / 2
            for i in 1:bin_iter_cnt
                hi_mid = (hi_min + hi_max) / 2
                err_mid, rs, us, Fs = calc_err(hi_mid)

                print(f1, hi_mid, " ", err_mid, "\n")

                if (abs(err_mid) < eps)
                    print(i, "\n")
                    return hi_mid
                end

                if (err_mid * err_min <= BigFloat("0.0"))
                    hi_max = hi_mid
                    err_max = err_mid
                else
                    hi_min = hi_mid
                    err_min = err_mid
                end
            end
        end
    end
    plot(rs, Fs, label="F")
    savefig(filename2)
    plot(rs, [us, [u_p(x) for x in rs]], label=["u" "u_p"]);
    savefig(filename3)
    plot(rs, us, label = "u")
    savefig(filename1)
    return hi_mid
end

shooting(BigFloat("0.0"), R, BigFloat("0.0"), BigFloat("5000"), runge_kutta_4, k_ts_1, "u1.png", "F1.png", "with_u_p1.png")
# 0.150884891735387327571515925228595733642578125
shooting(BigFloat("0.0"), R, BigFloat("0.0"), BigFloat("1000"), runge_kutta_4, k_ts_2, "u2.png", "F2.png", "with_u_p2.png", BigFloat("0.0"), BigFloat("1.0"))
# 0.9999989168897405592785636042506987283186238270555928795268917077369431748733737
shooting(BigFloat("0.0"), R, BigFloat("0.0"), BigFloat("5000"), runge_kutta_4, [x*200 for x in k_ts_1], "u_multiplied.png", "F1_multiplied.png", "with_u_p_multiplied.png", BigFloat("0.0"), BigFloat("1.0"))

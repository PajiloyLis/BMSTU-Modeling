with open("../data/the_only_table_i_could_find.txt") as f:
    all_nums_strings = f.readlines()

all_nums = [int(num[:-1]) for num in all_nums_strings]

delta = max(all_nums)-min(all_nums)

one_digit_nums = [int(num/delta*9) for num in all_nums]
two_digit_nums = [int(num/delta*89+10) for num in all_nums]
three_digit_nums = [int(num/delta*899+100) for num in all_nums]

with open("../data/one_digit_nums.txt", 'w') as f:
    for num in one_digit_nums:
        f.write(f"{num}\n")

with open("../data/two_digit_nums.txt", 'w') as f:
    for num in two_digit_nums:
        f.write(f"{num}\n")

with open("../data/three_digit_nums.txt", 'w') as f:
    for num in three_digit_nums:
        f.write(f"{num}\n")
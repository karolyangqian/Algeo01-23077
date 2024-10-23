import math

def f(x):
    return (math.pow(x, 2) + math.sqrt(x)) / (math.exp(x) + x)

n = 5

for i in range(n + 1):
    print(i*2/n, f(i*2/n))
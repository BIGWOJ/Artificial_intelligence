import numpy as np

def generate_dkp(n, scale):
    items = np.ceil(scale * np.random.rand(n, 2)).astype("int32")
    C = int(np.ceil(0.5 * 0.5 * n * scale))
    v = items[:, 0]
    c = items[:, 1]
    return v, c, C


def dkp(values, weights, capacity):
    n = len(values)
    v = np.zeros((n+1, capacity+1))
    for i in range(1, n+1):
        for j in range(1, capacity+1):
            if weights[i-1] > j:
                v[i, j] = v[i-1, j]
            else:
                v[i, j] = max(v[i-1, j], v[i-1, j-weights[i-1]] + values[i-1])
    return v[n, capacity]


v, c, C = generate_dkp(10, 10)
# print(v, c, C)
# print(dkp(v, c, C))


def adaptation(backpack, values, weights, capacity):
    f = np.dot(backpack, values)
    weight = np.dot(backpack, weights)

    if weight > capacity:
        return 0

    return f

class AG_class():

    def __init__(self, population_size, loop_count, cross_prob, mutation_prob, adaptation_func, *args):
        self.population_size = population_size
        self.loop_times = loop_count
        self.cross_prob = cross_prob
        self.mutation_prob = mutation_prob
        self.adaptation_func = adaptation_func
        self.adaptation_args = args

    def fit(self, population_size, loop_count, column_count):
        starting_population = np.random.rand(population_size, len(column_count))
        starting_population = np.where(starting_population > 0.9, 1, 0)
        for t in range(loop_count):
            for backpack in starting_population:
                ocena = self.adaptation_func()





AG = AG_class(1000, 100, 0.9, 0.001, adaptation())
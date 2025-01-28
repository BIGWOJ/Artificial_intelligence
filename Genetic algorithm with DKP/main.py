import numpy as np
import matplotlib.pyplot as plt

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


v, c, C = generate_dkp(100, 2000)
# print(v, c, C)
# print(dkp(v, c, C))


def adaptation(backpack, values, weights, capacity):
    f = np.dot(backpack, values)
    weight = np.dot(backpack, weights)

    if weight > capacity:
        return 0

    return f

class AG_class():

    def __init__(self, population_size, loop_count, cross_prob, mutation_prob, adaptation_func, args=None):
        self.population_size = population_size
        self.loop_times = loop_count
        self.cross_prob = cross_prob
        self.mutation_prob = mutation_prob
        self.adaptation_func = adaptation_func
        self.adaptation_args = args
        self.column_count = len(args[0])

    def fit(self):
        starting_population = np.random.rand(self.population_size, self.column_count)
        starting_population = np.where(starting_population > 0.9, 1, 0)
        oceny = np.zeros(self.population_size)
        najlepsza_ocena = 0
        najlepszy_osobnik = None
        oceny_macierz = np.zeros((self.loop_times, 3))

        for t in range(self.loop_times):
            najlepszy_aktualny = 0
            for i, backpack in enumerate(starting_population):
                ocena = self.adaptation_func(backpack, *self.adaptation_args)
                oceny[i] = ocena

                if ocena > najlepszy_aktualny:
                    najlepszy_aktualny = ocena

                    if najlepszy_aktualny > najlepsza_ocena:
                        najlepsza_ocena = najlepszy_aktualny
                        najlepszy_osobnik = backpack.copy()

            oceny_macierz[t, 0] = najlepsza_ocena
            oceny_macierz[t, 1] = najlepszy_aktualny
            oceny_macierz[t, 2] = np.mean(oceny)

            oceny /= np.sum(oceny)
            wybrany = np.random.choice(np.arange(self.population_size), size=self.population_size, p=oceny)
            new_population = np.zeros_like(starting_population)
            for i in range(0, self.population_size, 2):
                liczba = np.random.rand()
                if liczba < self.cross_prob:
                    punkt = np.random.randint(1, starting_population.shape[1]-1)
                    new_population[i][:punkt] = starting_population[wybrany[i]][:punkt]
                    new_population[i][punkt:] = starting_population[wybrany[i+1]][punkt:]

                    new_population[i+1][punkt:] = starting_population[wybrany[i]][punkt:]
                    new_population[i+1][:punkt] = starting_population[wybrany[i+1]][:punkt]
                else:
                    new_population[i] = starting_population[wybrany[i]]
                    new_population[i+1] = starting_population[wybrany[i+1]]

            for mutacja in new_population:
                for i in range(len(mutacja)):
                    if np.random.rand() < self.mutation_prob:
                        mutacja[i] = 1 - mutacja[i]
            starting_population = new_population
        return oceny_macierz, najlepszy_osobnik

population_size = 1000
loop_count = 100
cross_prob = 0.9
mutation_prob = 0.001
adaptation_func = adaptation

AG = AG_class(1000, 100, 0.9, 0.001, adaptation, args = (v, c, C))
wyniki = AG.fit()
print(wyniki)

plt.plot(wyniki[0])
plt.show()


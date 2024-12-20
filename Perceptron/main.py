from Perceptron_class import Perceptron
import numpy as np
from matplotlib import pyplot as plt

n = 6
data = np.random.rand(1000, 2) * 2 - 1


idx = data[:,0] < 0

gamma = 0.1
data[idx, 0] -= gamma/2
data[~idx, 0] += gamma/2
alpha = np.random.uniform(0, 2*np.pi)
data = data@[[np.cos(alpha), -np.sin(alpha)], [np.sin(alpha), np.cos(alpha)]]

plt.scatter(data[:,0], data[:,1], c=idx)
plt.show()

y = -2. * idx + 1
X = np.c_[np.ones(data.shape[0], 1), data]
w = np.zeros(X.shape[1])
#E = np.nonzero(np.sign(y) != np.sign(X @ w))[0]

print(E)
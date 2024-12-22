import numpy as np
from sklearn.model_selection import train_test_split
from sklearn import datasets
import matplotlib.pyplot as plt
from Perceptron_class import Perceptron

def accuracy(y_true, y_pred):
    return np.sum(y_true == y_pred) / len(y_true)

def test_1():
    X, y = datasets.make_blobs(n_samples=150, n_features=2, centers=2, cluster_std=1.05, random_state=2)
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=123)

    Perceptron_model = Perceptron(learning_rate=0.01, max_iterations=1000)
    Perceptron_model.fit(X_train, y_train)
    predictions = Perceptron_model.predict(X_test)
    print(f'Accuracy: {accuracy(y_test, predictions)}')

    fig = plt.figure()
    ax = fig.add_subplot(1,1,1)
    plt.scatter(X_train[:,0], X_train[:,1], marker='o', c=y_train)
    # x0_1 = np.amin(X_train[:,0])
    # x0_2 = np.amax(X_train[:,0])
    #
    # x1_1 = (-Perceptron_model.weights[0] *x0_1 - Perceptron_model.bias) / Perceptron_model.weights[1]
    # x1_2 = (-Perceptron_model.weights[0] *x0_2 - Perceptron_model.bias) / Perceptron_model.weights[1]
    #
    # ax.plot([x0_1, x0_2], [x1_1, x1_2], 'k')
    # ymin = np.amin(X_train[:,1])
    # ymax = np.amax(X_train[:,1])
    # ax.set_ylim([ymin-3, ymax+3])
    plt.show()

def test_2():
    n = 200
    data = np.random.rand(n, 2) * 2 - 1

    idx = data[:, 0] < 0

    gamma = 0.01
    data[idx, 0] -= gamma / 2
    data[~idx, 0] += gamma / 2
    alpha = np.random.uniform(0, 2 * np.pi)
    data = data @ [[np.cos(alpha), -np.sin(alpha)], [np.sin(alpha), np.cos(alpha)]]

    # plt.scatter(data[:, 0], data[:, 1], c=idx)
    # plt.scatter(data[:, 0], data[:, 1], c=idx)

    # Fit the Perceptron model
    fig, axs = plt.subplots(2, 2, figsize=(15, 10))

    iterations = [1, 5, 10, 100]
    learning_rates = [0.01, 0.1, 0.5, 1]
    i = 0
    for ax, iters in zip(axs.ravel(), iterations):
        Perceptron_model = Perceptron(learning_rate=learning_rates[i], max_iterations=iters)
        i+=1
        Perceptron_model.fit(data, idx)

        # Plot the decision boundary
        x0_1 = np.min(data[:, 0])
        x0_2 = np.max(data[:, 0])
        x1_1 = (-Perceptron_model.weights[0] * x0_1 - Perceptron_model.bias) / Perceptron_model.weights[1]
        x1_2 = (-Perceptron_model.weights[0] * x0_2 - Perceptron_model.bias) / Perceptron_model.weights[1]
        ax.plot([x0_1, x0_2], [x1_1, x1_2], 'k')
        ax.scatter(data[:, 0], data[:, 1], c=idx)
        predictions = Perceptron_model.predict(data)

        ax.set_title(f'Iterations: {Perceptron_model.max_iterations}\nAccuracy: {accuracy(idx, predictions):.2f}\nLearning rate: {Perceptron_model.learning_rate}')
        ax.set_xlim([np.min(data[:, 0] * 11/10), np.max(data[:, 0] * 11/10)])
        ax.set_ylim([np.min(data[:, 1] * 11/10), np.max(data[:, 1] * 11/10)])

    plt.tight_layout()
    plt.show()
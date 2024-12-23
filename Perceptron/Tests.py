import numpy as np
from sklearn.model_selection import train_test_split
import matplotlib.pyplot as plt
from Perceptron_class import Perceptron

#Function to calculate prediction accuracy
def accuracy(y_true, y_pred):
    return np.round(np.sum(y_true == y_pred) / len(y_true), 2)

def test_1():
    n = 1000
    data = np.random.rand(n, 2) * 2 - 1

    idx = data[:, 0] < 0

    classes_gap = 0.001
    data[idx, 0] -= classes_gap / 2
    data[~idx, 0] += classes_gap / 2
    alpha = np.random.uniform(0, 2 * np.pi)
    data = data @ [[np.cos(alpha), -np.sin(alpha)], [np.sin(alpha), np.cos(alpha)]]

    learning_rates = [0, 0.1, 0.5, 1]
    test_sizes = [0.1, 0.3, 0.5, 0.7]

    for test_size in test_sizes:
        X_train, X_test, y_train, y_test = train_test_split(data, idx, test_size=test_size)
        fig, axs = plt.subplots(2, 2, figsize=(15, 10))

        for ax, learning_rate in zip(axs.ravel(), learning_rates):
            Perceptron_model = Perceptron(learning_rate=learning_rate)
            Perceptron_model.fit(X_train, y_train)

            #Decision boundary
            x0_1 = np.min(X_test[:, 0])
            x0_2 = np.max(X_test[:, 0])
            x1_1 = (-Perceptron_model.weights[0] * x0_1 - Perceptron_model.bias) / Perceptron_model.weights[1]
            x1_2 = (-Perceptron_model.weights[0] * x0_2 - Perceptron_model.bias) / Perceptron_model.weights[1]
            ax.plot([x0_1, x0_2], [x1_1, x1_2], 'k')

            predictions = Perceptron_model.predict(X_test)
            colors = np.where(predictions == y_test, 'g', 'r')
            ax.scatter(X_test[:, 0], X_test[:, 1], c=colors)

            ax.set_title(f'Iterations: {Perceptron_model.iterations_done}\nTrain size: {test_size}'
                         f'\nAccuracy: {accuracy(y_test, predictions):.2f}\nLearning rate: {Perceptron_model.learning_rate}')

            #Adjusting the plot limits with extra 10% margin
            ax.set_xlim([np.min(X_test[:, 0] * 11/10), np.max(X_test[:, 0] * 11/10)])
            ax.set_ylim([np.min(X_test[:, 1] * 11/10), np.max(X_test[:, 1] * 11/10)])
            # print(f'Iterations: {Perceptron_model.iterations_done}\nAccuracy: {accuracy(predictions, y_test):.2f}\nLearning rate: {Perceptron_model.learning_rate}\n'
            #       f'Weights: {np.round(Perceptron_model.weights, 2)}\nBias: {np.round(Perceptron_model.bias, 2)}\n')
        plt.tight_layout()
        plt.show()
import numpy as np

class Perceptron:
    def __init__(self, learning_rate=0.01, max_iterations=1000):
        self.learning_rate = learning_rate
        self.max_iterations = max_iterations
        self.weights = None
        self.bias = None

    def fit(self, X, y):
        self.weights = np.random.rand(X.shape[1])
        self.bias = 0

        #y_ = np.array([1 if i > 0 else 0 for i in y])

        for _ in range(self.max_iterations):
            for idx, x_i in enumerate(X):
                linear_output = np.dot(x_i, self.weights) + self.bias
                y_predicted = self.decision_function(linear_output)

                #Update
                update = self.learning_rate * (y[idx] - y_predicted)

                self.weights += update * x_i
                self.bias += update

    def predict(self, X):
        linear_output = np.dot(X, self.weights) + self.bias
        #y_predicted = self.activation_func(linear_output)
        y_predicted = self.decision_function(linear_output)
        return y_predicted

    def decision_function(self, x):
        return np.where(x >= 0, 1, 0)


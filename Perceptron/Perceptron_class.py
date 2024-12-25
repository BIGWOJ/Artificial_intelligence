import numpy as np

class Perceptron:
    def __init__(self, learning_rate=0.01):
        self.learning_rate = learning_rate
        self.max_iterations = 1000
        self.iterations_done = 0
        self.weights = None
        self.bias = 0

    def fit(self, X, y):
        self.weights = np.ones(X.shape[1])
        self.iterations_done = 0
        previous_weights = np.ones_like(self.weights)
        previous_bias = 0
        epsilon = 0.0001

        while True:
            for index, sample in enumerate(X):
                weighted_sum = np.dot(sample, self.weights) + self.bias
                y_predicted = self.decision_function(weighted_sum)
                #Update weight -> weight += delta weight
                #Update bias -> bias += delta bias
                #Update (delta) -> learning rate * (y_actual - y_predicted)

                #If delta is positive, the weight needs to be increased
                #If delta is negative, the weight needs to be decreased
                #Bias is used to move the decision boundary
                update = self.learning_rate * (y[index] - y_predicted)
                self.weights += update * sample
                self.bias += update

            weight_change = np.linalg.norm(self.weights - previous_weights)
            bias_change = abs(self.bias - previous_bias)

            self.iterations_done += 1
            if self.iterations_done > self.max_iterations or max(weight_change, bias_change) < epsilon:
                break

            previous_weights = self.weights.copy()
            previous_bias = self.bias

    def predict(self, X):
        #Weighted sum from perceptron model
        weighted_sum = np.dot(X, self.weights) + self.bias
        return self.decision_function(weighted_sum)

    def decision_function(self, x):
        return np.where(x >= 0, 1, 0)
import numpy as np
from sklearn.base import BaseEstimator, ClassifierMixin

class NBC(BaseEstimator, ClassifierMixin):
    def __init__(self, buckets_amount=None, laplace=False, logarithms=False):
        self.laplace = laplace
        self.logarithms = logarithms
        self.classes = None
        self.attributes_probabilities = None
        self.classes_probabilities = None
        self.buckets_amount = buckets_amount

    def discretize(self, X):
        discretized_X = []
        for attribute in range(X.shape[1]):
            data = X[:, attribute]
            bin_size = (np.max(data) - np.min(data)) / self.buckets_amount
            for i in range(len(data)):
                bin_num = int((data[i] - np.min(data)) / bin_size)
                if bin_num == self.buckets_amount:
                    bin_num -= 1
                discretized_X.append(bin_num)

        return np.array(discretized_X).reshape(X.shape)

    def fit(self, X, y):
        self.classes, class_counts = np.unique(y, return_counts=True)
        self.classes_probabilities = class_counts / len(self.classes)

        self.attributes_probabilities = []
        for j in range(X.shape[1]):
            attribute_probabilities = []
            for value in np.unique(X[:, j]):
                value_probabilities = []
                for class_index, class_label in enumerate(self.classes):
                    count = np.sum((X[:, j] == value) & (y == class_label))
                    #P(A) ~= (k+1) / m + q -> PDF
                    if self.laplace:
                        count += 1
                        denominator = class_counts[class_index] + len(np.unique(X[:, j]))
                    else:
                        denominator = class_counts[class_index]

                    value_probabilities.append(count / denominator)
                attribute_probabilities.append(value_probabilities)
            self.attributes_probabilities.append(np.array(attribute_probabilities))

        return self

    def predict_proba(self, X):
        samples_amount, attributes_amount = X.shape
        epsilon = 1e-100

        if self.logarithms:
            probabilities = np.zeros((samples_amount, len(self.classes)))
            for i in range(samples_amount):
                for y_index in range(len(self.classes)):
                    probabilities[i, y_index] += np.log(max(self.classes_probabilities[y_index], epsilon))
                    for j in range(attributes_amount):
                        value_index = np.where(np.unique(X[:, j]) == X[i, j])[0][0]
                        probabilities[i, y_index] += np.log(max(self.attributes_probabilities[j][value_index, y_index], epsilon))

        else:
            probabilities = np.ones((samples_amount, len(self.classes)))
            for i in range(samples_amount):
                for y_index in range(len(self.classes)):
                    for j in range(attributes_amount):
                        value_index = np.where(np.unique(X[:, j]) == X[i, j])[0][0]
                        probabilities[i, y_index] *= max(self.attributes_probabilities[j][value_index, y_index], epsilon)
                    probabilities[i, y_index] *= max(self.classes_probabilities[y_index], epsilon)

        return probabilities

    def predict(self, X):
        return self.classes[np.argmax(self.predict_proba(X), axis=1)]

import numpy as np
from sklearn.base import BaseEstimator, ClassifierMixin

class NBC(BaseEstimator, ClassifierMixin):
    def __init__(self, buckets_amount=None, laplace=False, logarithms=False):
        self.laplace = laplace
        self.logarithms = logarithms
        self.classes_ = None
        self.attributes_probabilities = None
        self.classes_probabilities = None
        self.buckets_amount = buckets_amount

    def discretize(self, X):
        discretized_X = []
        for attribute in range(X.shape[1]):
            data = X[:, attribute]
            #print(self.buckets_amount)
            bin_size = (np.max(data) - np.min(data)) / self.buckets_amount
            for i in range(len(data)):
                #print(int(min))
               # exit(0)
                bin_num = int((data[i] - np.min(data)) / bin_size)
                if bin_num == self.buckets_amount:
                    bin_num -= 1
                discretized_X.append(bin_num)
            #discretized_X.append()
        #
        # for i in range(len(data)):
        #     bin_num = int((data[i] - min) / bin_size)
        #     if bin_num == num_bins:
        #         bin_num -= 1
        #     discretized_data.append(bin_num)

        return np.array(discretized_X).reshape(X.shape)

    def fit(self, X, y):
        m, n = X.shape
        if not np.issubdtype(X.dtype, np.integer):
            X = self.discretize(X)
            print(X)

        self.classes_, class_counts = np.unique(y, return_counts=True)
        K = len(self.classes_)
        self.classes_probabilities = class_counts / m

        # Tworzenie tablicy prawdopodobieństw warunkowych
        self.attributes_probabilities = []
        for j in range(n):
            attribute_probabilities = []
            for value in np.unique(X[:, j]):
                value_probabilities = []
                for class_index, class_label in enumerate(self.classes_):
                    count = np.sum((X[:, j] == value) & (y == class_label))
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
        m, n = X.shape
        K = len(self.classes_)
        eps = 1e-300  # Zapobieganie zerowym prawdopodobieństwom

        # Obliczanie prawdopodobieństw dla każdej klasy
        if not self.logarithms:
            probabilities = np.ones((m, K))
            for i in range(m):
                for y_index in range(K):
                    for j in range(n):
                        value_index = np.where(np.unique(X[:, j]) == X[i, j])[0][0]
                        probabilities[i, y_index] *= max(self.attributes_probabilities[j][value_index, y_index], eps)
                    probabilities[i, y_index] *= max(self.classes_probabilities[y_index], eps)
        else:
            probabilities = np.zeros((m, K))
            for i in range(m):
                for y_index in range(K):
                    probabilities[i, y_index] += np.log(max(self.classes_probabilities[y_index], eps))
                    for j in range(n):
                        value_index = np.where(np.unique(X[:, j]) == X[i, j])[0][0]
                        probabilities[i, y_index] += np.log(max(self.attributes_probabilities[j][value_index, y_index], eps))

            # Konwersja log-prawdopodobieństw na rzeczywiste prawdopodobieństwa
            probabilities -= probabilities.max(axis=1, keepdims=True)
            probabilities = np.exp(probabilities)

        # Normalizacja wyników
        probabilities /= probabilities.sum(axis=1, keepdims=True)
        return probabilities

    def predict(self, X):
        return self.classes_[np.argmax(self.predict_proba(X), axis=1)]

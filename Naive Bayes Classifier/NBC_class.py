import numpy as np
from sklearn.base import BaseEstimator, ClassifierMixin

class NBC(BaseEstimator, ClassifierMixin):
    def __init__(self, laplace=False):
        self.laplace = laplace
        self.contingency_table = None

    def print_contingency_table(self, short=False):
        if short:
            for table in self.contingency_table:
                print(table)

        for variable_index, values in enumerate(self.contingency_table):
            print(f"Variable: {variable_index}")
            for value_index, lens_counts in enumerate(values):
                print(f"  Value: {value_index}")
                for lens_index, count in enumerate(lens_counts):
                    print(f"\t{lens_index}: {count}")

    def fit(self, X, y):
        unique, counts = np.unique(y, return_counts=True)
        self.classes = unique
        self.classes_counts = counts
        self.classes_probabilities = counts / sum(counts)

        contingency_table = []
        self.attributes_possibilities = []

        for column in range(X.shape[1]):
            self.attributes_possibilities.append(np.unique(X[:, column]))
            contingency_table.append(np.zeros((len(self.attributes_possibilities[-1]), len(self.classes))))

        self.attributes_probabilities = []
        for variable_index in range(len(self.attributes_possibilities)):
            attribute_probabilities = []
            for value_code in self.attributes_possibilities[variable_index]:
                value_probabilities = []
                for lens_index in self.classes:
                    lens_index = int(lens_index)
                    count = np.sum((X[:, variable_index] == value_code) & (y == lens_index))
                    contingency_table[variable_index][value_code-1][lens_index-1] = int(count)
                    value_probabilities.append(count / self.classes_counts[lens_index-1])

                    if self.laplace:
                        count += 1
                        denominator = self.classes_counts[lens_index - 1] + len(
                            self.attributes_possibilities[variable_index])
                    else:
                        denominator = self.classes_counts[lens_index - 1]
                    probability = count / denominator

                attribute_probabilities.append(value_probabilities)
            self.attributes_probabilities.append(attribute_probabilities)

        #print(self.attributes_possibilities)
        #print(self.attributes_probabilities)
        #print(self.classes_probabilities)
        #exit(0)

        for variable_index in range(len(self.attributes_possibilities)):
            for value_code in self.attributes_possibilities[variable_index]:
                for lens_index in self.classes:
                    count = np.sum((X[:, variable_index] == value_code) & (y == lens_index))
                    contingency_table[variable_index][value_code-1][lens_index-1] = int(count)

        self.contingency_table = contingency_table
        #print(self.attributes_possibilities)



        #self.print_contingency_table(short=True)
        #print(self.classes_counts)
        #print(self.classes_probabilities)
        #print(self.attributes_possibilities)
        #exit(0)
        return contingency_table

    def predict_proba(self, X):
        probabilities = np.ones((X.shape[0], len(self.classes)))

        for i in range(X.shape[0]):
            for y_index in range(len(self.classes)):
                probabilities[i, y_index] *= self.classes_probabilities[y_index]
                for j in range(X.shape[1]):
                    probabilities[i, y_index] *= self.attributes_probabilities[j][X[i, j] - 1][y_index]

        #print(probabilities)
        return probabilities

    def predict(self, X):
        return np.argmax(self.predict_proba(X), axis=1)

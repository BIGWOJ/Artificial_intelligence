import numpy as np
from sklearn.base import BaseEstimator, ClassifierMixin

class NBC(BaseEstimator, ClassifierMixin):
    def __init__(self, laplace=False):
        self.laplace = laplace
        self.contingency_tables = None

    def print_contingency_table(self, short=False):
        if short:
            for table in self.contingency_tables:
                print(table)

        for variable_index, values in enumerate(self.contingency_table):
            print(f"Variable: {variable_index}")
            for value_index, lens_counts in enumerate(values):
                print(f"  Value: {value_index}")
                for lens_index, count in enumerate(lens_counts):
                    print(f"\t{lens_index}: {count}")

    def fit(self, X, y):
        unique, counts = np.unique(y, return_counts=True)
        self.unique_labels = unique
        self.unique_counts = counts
        self.unique_prob = counts / sum(counts)

        contingency_table = []
        self.unique_columns = []

        for column in range(X.shape[1]):
            self.unique_columns.append(np.unique(X[:, column]))
            contingency_table.append(np.zeros((len(self.unique_columns[-1]), len(self.unique_labels))))

        for variable_index in range(len(self.unique_columns)):
            for value_code in self.unique_columns[variable_index]:
                for lens_index in self.unique_labels:
                    count = np.sum((X[:, variable_index] == value_code) & (y == lens_index))
                    contingency_table[variable_index][value_code-1][lens_index-1] = int(count)

        self.contingency_table = contingency_table
        #self.print_contingency_table(short=True)
        return contingency_table

    def predict_proba(self, X):
        prob_matrix = np.zeros((X.shape[0], len(self.classes)))

        for sample_index, sample in enumerate(X):
            for class_index in range(len(self.classes)):
                prob = self.class_probs[class_index]
                for attr_idx, attr_value in enumerate(sample):
                    attr_value_idx = np.where(self.attributes[attr_idx] == attr_value)[0][0]
                    prob *= self.contingency_tables[attr_idx][attr_value_idx, class_index]
                prob_matrix[sample_index, class_index] = prob

        prob_matrix /= prob_matrix.sum(axis=1, keepdims=True)
        return prob_matrix

    def predict(self, X):
        prob_matrix = self.predict_proba(X)
        return np.argmax(prob_matrix, axis=1)

import numpy as np
from sklearn.base import BaseEstimator, ClassifierMixin
class NBC_class(BaseEstimator, ClassifierMixin):
    def __init__(self, num_bins):
        self.num_bins = num_bins
        self.contingency_table = None

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
        #self.print_contingency_table()
        # print(self.unique_labels)
        # print(self.unique_columns)
        # print(self.unique_prob)
        # print(self.unique_counts)
        # exit(0)
        return contingency_table

    def print_contingency_table(self):
        for variable_index, values in enumerate(self.contingency_table):
            print(f"Variable: {variable_index}")
            for value_index, lens_counts in enumerate(values):
                print(f"  Value: {value_index}")
                for lens_index, count in enumerate(lens_counts):
                    print(f"\t{lens_index}: {count}")

    def predict(self, X):
        #return argmax(predict_proba)
        pass

    def predict_proba(self, X):
        #ile próbek tyle wierszy
                #tyl kolumn ile klas
        #macierz prawdopodobieństw przynależności  do klasy
        probabilities = np.zeros((X.shape[0], len(self.unique_labels)))
        print(probabilities)
        for x in X:
            for i in range(len(self.unique_labels)):
                pass





    # def __init__(self, X, y):
    #     self.X = X
    #     self.y = y
    #     self.classes = np.unique(y)
    #     self.priors = self.get_priors()
    #     self.likelihoods = self.get_likelihoods()
    #
    # def get_priors(self):
    #     priors = {}
    #     for c in self.classes:
    #         priors[c] = np.sum(self.y == c) / len(self.y)
    #     return priors
    #
    # def get_likelihoods(self):
    #     likelihoods = {}
    #     for c in self.classes:
    #         likelihoods[c] = {}
    #         for i in range(self.X.shape[1]):
    #             likelihoods[c][i] = {}
    #             for v in np.unique(self.X[:, i]):
    #                 likelihoods[c][i][v] = np.sum((self.X[:, i] == v) & (self.y == c)) / np.sum(self.y == c)
    #     return likelihoods
    #
    # def predict(self, X):
    #     preds = []
    #     for x in X:
    #         probs = {}
    #         for c in self.classes:
    #             probs[c] = self.priors[c]
    #             for i in range(len(x)):
    #                 probs[c] *= self.likelihoods[c][i][x[i]]
    #         preds.append(max(probs, key=probs.get))
    #     return preds
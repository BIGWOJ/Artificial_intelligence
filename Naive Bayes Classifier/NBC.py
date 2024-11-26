import numpy as np
from sklearn.base import BaseEstimator, ClassifierMixin
class NBC(BaseEstimator, ClassifierMixin):
    def __init__(self, num_bins):
        self.num_bins = num_bins

    def fit(self, X, y):
        pass

    def predict(self, X):
        pass

    def predict_proba(self, X):
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
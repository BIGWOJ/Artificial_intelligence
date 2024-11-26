import NBC
import numpy as np
from sklearn.model_selection import train_test_split

def discretize(data, num_bins, min, max):
    bin_size = (max - min) / num_bins
    discretized_data = []

    for i in range(len(data)):
        bin_num = int((data[i] - min) / bin_size)
        if bin_num == num_bins:
            bin_num -= 1
        discretized_data.append(bin_num)

    return np.array(discretized_data)

def wine():
    wine_data = np.genfromtxt('wine.data', delimiter=',')
    class_labels = wine_data[:, 0]
    X = wine_data[:, 1:]
    y = wine_data[:, 0]
    #X_train, X_test, y_train, y_test = train_test_split(features, class_labels)
    # print(wine_data[:, 2])
    #first_case = wine_data[:, 1]
    #first_feature = wine_data[:, feature]

    discretized_wine_data = []
    for feature in range(X.shape[1]):
        data = X[:, feature]
        discretized_wine_data.append(discretize(data, 3, np.min(data), np.max(data)))

    for i in discretized_wine_data:
        print(i, end='\n\n')


wine()


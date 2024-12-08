import NBC_class
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.model_selection import cross_val_score
from sklearn.metrics import accuracy_score

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
    X = wine_data[:, 1:-1]
    y = wine_data[:, -1]

    mean_accuracy = 0
    mean_accuracy_laplace = 0
    tests_number = 100
    for _ in range(tests_number):
        X_train, X_test, y_train, y_test = train_test_split(X, y)

        # discretized_wine_data = []
        # for attribute in range(X_train.shape[1]):
        #     data = X_train[:, attribute]
        #     discretized_wine_data.append(discretize(data, 2, np.min(data), np.max(data)))
        #
        # X_train = np.array(discretized_wine_data).T
        y_train = discretize(y_train, 2, np.min(y), np.max(y))
        y_test = discretize(y_test, 2, np.min(y), np.max(y))

        # X_test_discretized = []
        # for attribute in range(X_test.shape[1]):
        #     data = X_test[:, attribute]
        #     X_test_discretized.append(discretize(data, 2, np.min(data), np.max(data)))
        # X_test = np.array(X_test_discretized).T

        NBC = NBC_class.NBC(buckets_amount=2)
        NBC.fit(X_train, y_train)
        y_pred = NBC.predict(X_test)
        mean_accuracy += accuracy_score(y_test, y_pred)

        NBC_laplace = NBC_class.NBC(laplace=True, buckets_amount=2)
        NBC_laplace.fit(X_train, y_train)
        y_pred = NBC_laplace.predict(X_test)
        mean_accuracy_laplace += accuracy_score(y_test, y_pred)

    print(f'{"="*10} Wine {"="*10}\nPredictions accuracy: {mean_accuracy / tests_number:.3f}')
    print(f'Predictions accuracy using Laplace: {mean_accuracy_laplace / tests_number:.3f}\n')

def lenses():
    lenses_data = np.genfromtxt('lenses.data', dtype=int)
    #contingency_table = create_contingency_table(lenses_data)
    #print_contingency_table(contingency_table)

    X = lenses_data[:, 1:-1]
    y = lenses_data[:, -1]

    mean_accuracy = 0
    mean_accuracy_laplace = 0
    tests_number = 100
    for _ in range(tests_number):
        X_train, X_test, y_train, y_test = train_test_split(X, y)

        # X_train = X
        # y_train = y
        # X_test = X
        # y_test = y

        NBC = NBC_class.NBC()
        NBC.fit(X_train, y_train)
        y_pred = NBC.predict(X_test)
        mean_accuracy += accuracy_score(y_test, y_pred)

        NBC_laplace = NBC_class.NBC(laplace=True)
        NBC_laplace.fit(X_train, y_train)
        y_pred = NBC_laplace.predict(X_test)
        mean_accuracy_laplace += accuracy_score(y_test, y_pred)

    print(f'{"="*10} Contact lenses {"="*10}\nPredictions accuracy: {mean_accuracy/tests_number:.3f}')
    print(f'Predictions accuracy using Laplace: {mean_accuracy_laplace / tests_number:.3f}')

wine()
lenses()


import numpy as np
from sklearn.metrics import accuracy_score
from sklearn.model_selection import train_test_split
import NBC_class

#Discretization function
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
    #Getting data and labels
    X = wine_data[:, 1:]
    y = wine_data[:, -1]

    mean_accuracy = 0
    mean_accuracy_laplace = 0
    mean_accuracy_logs = 0
    mean_accuracy_laplace_logs = 0
    tests_number = 10
    bucket_amount = 2

    for _ in range(tests_number):
        X_train, X_test, y_train, y_test = train_test_split(X, y)

        discretized_wine_data = []
        for attribute in range(X_train.shape[1]):
            data = X_train[:, attribute]
            discretized_wine_data.append(discretize(data, bucket_amount, np.min(data), np.max(data)))

        X_train = np.array(discretized_wine_data).T
        y_train = discretize(y_train, bucket_amount, np.min(y), np.max(y))
        y_test = discretize(y_test, bucket_amount, np.min(y), np.max(y))

        X_test_discretized = []
        for attribute in range(X_test.shape[1]):
            data = X_test[:, attribute]
            X_test_discretized.append(discretize(data, bucket_amount, np.min(data), np.max(data)))
        X_test = np.array(X_test_discretized).T

        #Using different NBC configurations
        NBC = NBC_class.NBC(buckets_amount=bucket_amount)
        NBC.fit(X_train, y_train)
        y_pred = NBC.predict(X_test)
        mean_accuracy += accuracy_score(y_test, y_pred)

        NBC_laplace = NBC_class.NBC(laplace=True,  buckets_amount=bucket_amount)
        NBC_laplace.fit(X_train, y_train)
        y_pred = NBC_laplace.predict(X_test)
        mean_accuracy_laplace += accuracy_score(y_test, y_pred)

        NBC_logs = NBC_class.NBC(logarithms=True, buckets_amount=bucket_amount)
        NBC_logs.fit(X_train, y_train)
        y_pred = NBC_logs.predict(X_test)
        mean_accuracy_logs += accuracy_score(y_test, y_pred)

        NBC_laplace_logs = NBC_class.NBC(laplace=True, logarithms=True, buckets_amount=bucket_amount)
        NBC_laplace_logs.fit(X_train, y_train)
        y_pred = NBC_laplace_logs.predict(X_test)
        mean_accuracy_laplace_logs += accuracy_score(y_test, y_pred)

    print(f'{"=" * 10} Wine {"=" * 10}\nPredictions accuracy: {mean_accuracy / tests_number:.3f}')
    print(f'Predictions accuracy using Laplace: {mean_accuracy_laplace / tests_number:.3f}')
    print(f'Predictions accuracy using logarithms: {mean_accuracy_logs / tests_number:.3f}')
    print(f'Predictions accuracy using Laplace and logarithms: {mean_accuracy_laplace_logs / tests_number:.3f}\n')

def lenses():
    lenses_data = np.genfromtxt('lenses.data', dtype=int)

    X = lenses_data[:, 1:]
    y = lenses_data[:, -1]

    mean_accuracy = 0
    mean_accuracy_laplace = 0
    mean_accuracy_laplace_logs = 0
    mean_accuracy_logs = 0
    tests_number = 50
    bucket_amount = 3

    for _ in range(tests_number):
        X_train, X_test, y_train, y_test = train_test_split(X, y)

        NBC = NBC_class.NBC(buckets_amount=bucket_amount)
        NBC.fit(X_train, y_train)
        y_pred = NBC.predict(X_test)
        mean_accuracy += accuracy_score(y_test, y_pred)

        NBC_laplace = NBC_class.NBC(laplace=True, buckets_amount=bucket_amount)
        NBC_laplace.fit(X_train, y_train)
        y_pred = NBC_laplace.predict(X_test)
        mean_accuracy_laplace += accuracy_score(y_test, y_pred)

        NBC_logs = NBC_class.NBC(logarithms=True, buckets_amount=bucket_amount)
        NBC_logs.fit(X_train, y_train)
        y_pred = NBC_logs.predict(X_test)
        mean_accuracy_logs += accuracy_score(y_test, y_pred)

        NBC_laplace_logs = NBC_class.NBC(laplace=True, logarithms=True, buckets_amount=bucket_amount)
        NBC_laplace_logs.fit(X_train, y_train)
        y_pred = NBC_laplace_logs.predict(X_test)
        mean_accuracy_laplace_logs += accuracy_score(y_test, y_pred)

    print(f'{"="*10} Contact lenses {"="*10}\nPredictions accuracy: {mean_accuracy / tests_number:.3f}')
    print(f'Predictions accuracy using Laplace: {mean_accuracy_laplace / tests_number:.3f}')
    print(f'Predictions accuracy using logarithms: {mean_accuracy_logs / tests_number:.3f}')
    print(f'Predictions accuracy using Laplace and logarithms: {mean_accuracy_laplace_logs / tests_number:.3f}\n')
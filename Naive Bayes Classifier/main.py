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

def create_contingency_table(data):
    variables = {
        'age': {'young': 1, 'pre-presbyopic': 2, 'presbyopic': 3},
        'spectacle_prescription': {'myope': 1, 'hypermetrope': 2},
        'astigmatic': {'no': 1, 'yes': 2},
        'tear_production_rate': {'reduced': 1, 'normal': 2}
    }
    lenses = ['hard', 'soft', 'none']

    data = data[:, 1:]
    contingency_table = {}

    for variable in variables:
        contingency_table[variable] = {}
        for value in variables[variable]:
            contingency_table[variable][value] = {}
            for lens in lenses:
                contingency_table[variable][value][lens] = 0

    for variable_index, variable in enumerate(variables):
        # print("=" * 10, variable, "=" * 10)
        for value, value_code in variables[variable].items():
            for lens in lenses:
                lens_index = lenses.index(lens) + 1
                count = np.sum((data[:, variable_index] == value_code) & (data[:, -1] == lens_index))
                # print(f"{value} {lens} {count}")
                contingency_table[variable][value][lens] = int(count)

    return contingency_table

def print_contingency_table(contingency_table):
    for variable, values in contingency_table.items():
        print(f"Variable: {variable}")
        for value, lens_counts in values.items():
            print(f"  Value: {value}")
            for lens, count in lens_counts.items():
                print(f"\t{lens}: {count}")

    for variable, values in contingency_table.items():
        print(variable, values)

    # print(contingency_table)

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

def lenses():
    lenses_data = np.genfromtxt('lenses.data')
    contingency_table = create_contingency_table(lenses_data)
    print_contingency_table(contingency_table)

#wine()
lenses()


import sys
import csv
import numpy as np


def empty_table(input_file):
    max_feature = 0
    count = 0
    with open(input_file, 'r', newline='') as f:
        reader = csv.reader(f, delimiter=" ")
        for line in reader:
            count += 1
            for i in line:
                num = int(i.split(":")[0])
                if num > max_feature:
                    max_feature = num
    
    return np.zeros((count, max_feature + 1))


def write(input_file, output_file, table):
    with open(input_file, 'r', newline='') as f:
        reader = csv.reader(f, delimiter=" ")
        for c, line in enumerate(reader):
            label = line.pop(0)
            table[c, 0] = label
            if line[-1].strip() == '':
                line.pop(-1)

            line = map(lambda x : tuple(x.split(":")), line)
            for i, v in line:
                i = int(i)
                table[c, i] = v

    delete_col = []
    for col in range(table.shape[1]):
        if not any(table[:, col]):
            delete_col.append(col)
    
    table = np.delete(table, delete_col, axis=1)
    with open(output_file, 'w') as f:
        writer = csv.writer(f)
        for line in table:
            writer.writerow(line)


if __name__ == "__main__":
    input_file = sys.argv[1]
    output_file = sys.argv[2]
    table = empty_table(input_file)
    write(input_file, output_file, table)
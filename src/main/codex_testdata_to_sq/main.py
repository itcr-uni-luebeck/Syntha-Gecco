# IMPORTANT: Some parts of this file were changed in order to facilitate compatibility with this project

import json, os, sys
from testDataToUnitTest import generate_unit_test

if __name__ == '__main__':
    path_to_json = sys.argv[1]
    output_dir = sys.argv[2]

with open(path_to_json,
            encoding="utf-8") as json_file:
    file_path = os.path.normpath(path_to_json)
    output_path = os.path.normpath(output_dir)
    test_data = json.load(json_file)
    generate_unit_test(test_data, output_path)

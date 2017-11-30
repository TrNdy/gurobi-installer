[![](https://travis-ci.org/TrNdy/gurobi-installer.svg?branch=master)](https://travis-ci.org/TrNdy/gurobi-installer)

# gurobi-installer

This package helps to install Gurobi from within ImageJ.

## Usage

There are only two important methods: `GurobiInstaller.install()` and `GurobiInstaller.testGurobi()`.

`testGurobi()` returns `true` if gurobi is installed correctly.

`install()` first tests if gurobi is correctly installed. If not, it will try to install guroby:
1. It will copy the requiered libraries to the appropriate ImageJ subdirectories.
2. It shows a dialog which helps the user to obtain a valid licent.

# EPFL BA1 - Fingerprint Matcher

For better clarity, please read this file with a proper markdown editor or on [GitHub](https://github.com/notsagg/epfl-ba1-ic-Fingerprint/blob/master/src/cs107/README.md).

## Introduction

This project is a fingerprint matcher. It takes as input two images, and returns a boolean true or false giving a hint on the alikeness of two prints.

This README file is not an extensive explanation of the project, but more a rough sketch of the internal mechanisms of this fingerprint matcher.
A thorough description of the project can however be found [here](https://github.com/notsagg/epfl-ba1-ic-Fingerprint/blob/master/resources/fingerprint.pdf), and the exported HTML documentation [here](https://htmlpreview.github.io/?https://github.com/notsagg/epfl-ba1-ic-Fingerprint/blob/master/src/cs107/docs/html/classcs107_1_1Fingerprint.html). See the [Documentation](README.md#Documentation) section for a more detailed understanding.

## Overall Process

The gist of matching two fingerprints can be summarized by a three-step process:

At first, the fingerprint goes through thinning. This makes finding unique patterns easier. We get this done by identifying redundant pixels and removing them until we get a 1px line of all the features of the whole print.

The second step is identifying these unique patterns constituting a fingerprint, called the minutiae. This fingerprint matcher is only capable of recognizing [terminations](https://en.wikipedia.org/wiki/Fingerprint#/media/File:Ridge_ending.svg) and [bifurcations](https://en.wikipedia.org/wiki/Fingerprint#/media/File:Bifurcation.svg).
There are of course [many other](https://en.wikipedia.org/wiki/Fingerprint#/media/File:Fingerprints_Minutiae_Patterns_Representation.jpg) minutiae patterns, but for the sake of simplicity we won't go any further.

The third step is comparing the extracted minutiae. It is from here that we get a feedback on the similarity of two fingerprints. This is done by superposing in all possible ways the two fingerprints until we get a positive match. Since the two input prints won't exactly be the same, there is a certain allowed tolerance in determining a positive match.

The majority of this algorithm utilizes a simple brute-force approach which is obviously not the most efficient, but by far the easiest.

## Documentation

An extensive documentation of this project can be found [here](https://htmlpreview.github.io/?https://github.com/notsagg/epfl-ba1-ic-Fingerprint/blob/master/src/cs107/docs/html/classcs107_1_1Fingerprint.html).
This documentation was generated using [doxygen](https://www.doxygen.nl/index.html) a documentation framework.
> Doxygen is the de facto standard tool for generating documentation

## Unit-Tests

This Fingerprint matcher has been developed with a test-driven approach in mind. For this, unit-tests have been written with [JUnit](https://junit.org/junit4/) as the testing framework.

All unit-tests can be found under `./tests/{part1, part2, part3}`, where `part1, part2, and part3` are the three respective steps in matching two fingerprints described earlier. They're all executables named with the following synthax, `Test` `function to test` `.java`.

However, because of the strict 20KB zip size, all unit-tests have been stripped away, but can of course be recovered by running `getUnitTests.sh` script.
Some unit-tests also require the given resources which can also be recovered with a script, `getResources.sh`.

If you wish to test this project, run the following commands,

`./getUnitTests.sh && ./getResources.sh`

`cd tests && ./testAll.sh`

This will build the whole project and then run all associated unit-tests. You might also want to download [JUnit](https://junit.org/junit4/) if you don't already have it installed.
We use version `4.13.2` which includes assertions against thrown exceptions. Run as such to install (be aware that this updates your `.bashrc` configuration file),

`cd build && ./getJunit.sh`

## Further Notes

Unfortunately not all test successfully run to the end. This is especially true for methods of part 3 where different fingerprints not expected to match, end up matching for some reason.

Two weeks were given to us to complete this project. With more time it would have been possible to elaborate a more efficient algorithm than the simple brute-force process currently employed.

I would also have liked to include code-coverage and other kind of statistics to get a better understanding of the resources used by the algorithm, and how it could be improved.

# EPFL BA1 - Fingerprint Matcher

For better clarity, please read this file with a proper markdown editor or on [GitHub](https://github.com/notsagg/epfl-ba1-ic-Fingerprint/blob/master/src/cs107/README.md).

## Introduction

This project is a fingerprint matcher. It takes as input two images, and returns a boolean true or false giving a hint on the alikeness of two prints.

This README file is not an extensive explanation of the project, but more a rough sketch of the internal mechanism of this fingerprint matcher.
A thorough description of the project can however be found [here](https://proginsc.epfl.ch/wwwhiver/mini-projet1/fingerprint.pdf), and the exported HTML documentation at [`docs/index.html`](https://htmlpreview.github.io/?https://github.com/notsagg/epfl-ba1-ic-Fingerprint/blob/master/src/cs107/docs/html/classcs107_1_1Fingerprint.html)
See the [Docuementation](README.md#Documentation) section for a more detailed understanding.

## Overall Process

The gist of matching two fingerprints can be summarized by a three-step process:

At first, the fingerprint goes through thinning. This makes finding unique patterns easier. We get this done by identifying redundant pixels and removing them until we get a 1px line of all the features of the whole print.

The second step is identifying these unique patterns constituting a fingerprint, called the minutiae. This fingerprint matcher is only capable of recognizing [terminations](https://en.wikipedia.org/wiki/Fingerprint#/media/File:Ridge_ending.svg) and [bifurcations](https://en.wikipedia.org/wiki/Fingerprint#/media/File:Bifurcation.svg).
There are of course [many other](https://en.wikipedia.org/wiki/Fingerprint#/media/File:Fingerprints_Minutiae_Patterns_Representation.jpg) minutiae patterns, but for the sake of simplicity we won't go any further.

The third step is comparing the extracted minutiae. It is from here that we get a feedback on the similarity of two fingerprints. This is done by superposing in all possible ways the two fingerprints until we get a positive match. Since the two input prints won't exactly be the same, there is a certain allowed tolerance in determining a positive match.

The majority of this algorithm utilizes a simple brute-force approach which is obviously not the most efficient, but by far the easiest.

## Documentation

An extensive documentation of this project can be found at [`docs/index.html`](https://htmlpreview.github.io/?https://github.com/notsagg/epfl-ba1-ic-Fingerprint/blob/master/src/cs107/docs/html/classcs107_1_1Fingerprint.html).
This documentation was generated using [doxygen](https://www.doxygen.nl/index.html) a documentation framework.
> Doxygen is the de facto standard tool for generating documentation

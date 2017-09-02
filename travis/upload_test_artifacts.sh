#!/bin/sh

git clone https://github.com/eobermuhlner/experiments.git

mkdir experiments/csv2chart

cp ./ch.obermuhlner.csv2chart/src/test/resources/out_images/*.png experiments/csv2chart/

cd experiments
git add csv2chart/*
git commit -m "Travis `date`"
git push --repo "https://$UPLOAD_ARTIFACT_USERNAME:$UPLOAD_ARTIFACT_PASSWORD@github.com/eobermuhlner/experiments.git"
cd ..
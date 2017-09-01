#!/bin/sh

git clone https://github.com/eobermuhlner/experiments.git

mkdir experiments/csv2chart

cp ./ch.obermuhlner.csv2chart/src/test/resources/out_images/*.png experiments/csv2chart/

cd experiments
git add csv2char/*
git commit -m "Travis"
echo "$UPLOAD_ARTIFACT_PASSWORD" | git push --repo "https://$UPLOAD_ARTIFACT_USERNAME:github.com/eobermuhlner/experiments.git"
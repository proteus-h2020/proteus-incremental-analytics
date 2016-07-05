#!/bin/bash

JAVADOC_DIR=$(pwd)

#Create javadocs
mvn javadoc:javadoc

echo -e "Publishing javadoc...\n"

cd $HOME
git init
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
echo -e "Cloning the gh-pages branch...\n"

git remote add upstream "https://$GH_TOKEN@github.com/proteus-h2020/proteus-backend.git"
git fetch upstream
git reset upstream/gh-pages
cd gh-pages
rm -rf ./*
cp -R $JAVADOC_DIR/target/site/apidocs/* .
git add -A .
git commit -m "[DOCS-$TRAVIS_BUILD_NUMBER] Add javadocs"
git push -q upstream HEAD:gh-pages

echo -e "Published Javadoc to gh-pages.\n"
  

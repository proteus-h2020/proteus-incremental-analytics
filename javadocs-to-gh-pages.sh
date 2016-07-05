#!/bin/bash

JAVADOC_DIR=$(pwd)

#Create javadocs
mvn javadoc:javadoc

echo -e "Publishing javadoc...\n"

cd $HOME
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
echo -e "Cloning the gh-pages branch...\n"


git clone --quiet --branch=gh-pages https://github.com/proteus-h2020/proteus-backend gh-pages > /dev/null
cd gh-pages
rm -rf ./*
cp -R $JAVADOC_DIR/target/site/apidocs/* .
git add -f .
git commit -m "[DOCS-$TRAVIS_BUILD_NUMBER] Add javadocs"
git push -fq origin gh-pages > /dev/null

echo -e "Published Javadoc to gh-pages.\n"
  

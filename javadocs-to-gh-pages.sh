#!/bin/bash

JAVADOC_DIR=$(pwd)
echo "Current path: ${JAVADOC_DIR}"
#Create javadocs
mvn javadoc:javadoc

echo -e "Publishing javadoc...\n"

cd $HOME
mkdir docs && cd "$_"
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
echo -e "Cloning the gh-pages branch...\n"

git clone "https://$GH_TOKEN@github.com/proteus-h2020/proteus-backend.git" --branch=gh-pages gh-pages
cd gh-pages
rm -rf ./*
cp -R $JAVADOC_DIR/target/site/apidocs/* .
git add -A . > /dev/null
git commit -m "[DOCS-$TRAVIS_BUILD_NUMBER] Generate javadoc site (#$TRAVIS_COMMIT_MESSAGE)."
git push -f origin gh-pages > /dev/null


echo -e "Published Javadoc to gh-pages.\n"
  

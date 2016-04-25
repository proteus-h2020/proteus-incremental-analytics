#!/bin/bash
cd "$2"
npm install
node index.js "$1" "$3"
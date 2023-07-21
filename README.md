# mgf-p2m2-tools
[![CircleCI](https://circleci.com/gh/p2m2/mgf-p2m2-tools.svg?style=shield)](https://circleci.com/gh/p2m2/mgf-p2m2-tools)

Scala parser for generic MGF mascot files containing MS/MS data

https://p2m2.github.io/mgf-p2m2-tools/

## specifications

http://www.matrixscience.com/help/data_file_help.html

### Html

#### Development version

npm should be installed.

```shell 
export NODE_OPTIONS=--openssl-legacy-provider
# fastOptJS or fullOptJS check html/index.html
sbt fastOptJS 
sbt fullOptJS 
# open html/index.html
```

#### Release

```shell 
sbt fullOptJS::webpack
cp target/scala-2.13/scalajs-bundler/main/mgf-parser-opt-bundle.js docs/
# open docs/index.html
```

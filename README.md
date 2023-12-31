# mgf-p2m2-tools
[![CircleCI](https://circleci.com/gh/p2m2/mgf-p2m2-tools.svg?style=shield)](https://circleci.com/gh/p2m2/mgf-p2m2-tools)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/516456a87a7447ce9c02290c4fe13bea)](https://app.codacy.com/gh/p2m2/mgf-p2m2-tools/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

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
cp target/scala-2.13/scalajs-bundler/main/mgf-p2m2-tools-opt-bundle.js docs/
# open docs/index.html
```

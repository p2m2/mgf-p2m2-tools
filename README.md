# mgf-p2m2-tools
[![CircleCI](https://circleci.com/gh/p2m2/mgf-p2m2-tools.svg?style=shield)](https://circleci.com/gh/p2m2/mgf-p2m2-tools)
[![codecov](https://codecov.io/gh/p2m2/mgf-p2m2-tools/branch/develop/graph/badge.svg)](https://codecov.io/gh/p2m2/p2m2)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/9db61bd9732740c79a39de678c6e5246)](https://www.codacy.com/gh/p2m2/mgf-p2m2-tools/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=p2m2/mgf-p2m2-tools&amp;utm_campaign=Badge_Grade)

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

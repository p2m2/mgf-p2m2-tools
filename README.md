# mgf-parser
Scala parser for generic MGF mascot files containing MS/MS data


## specifications

http://www.matrixscience.com/help/data_file_help.html

### Html

#### Development version

npm should be installed.

```shell 
export NODE_OPTIONS=--openssl-legacy-provider
sbt fastOptJS::webpack
# open html/index.html
```

#### Release

```shell 
sbt fullOptJS::webpack
cp target/scala-2.13/scalajs-bundler/main/xxxx-opt-bundle.js docs/
# open docs/index.html
```

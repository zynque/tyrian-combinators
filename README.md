# tyrian-combinators
Tyrian-Combinators is an extension to Tyrian, the Elm-inspired Scala UI library, that enables the creation of reusable UI elements and data transformations that can be combined in various ways to produce arbitrarily complex UIs.

## Setup Instructions

I recommend keeping two terminals open: one for npm and one for sbt.

* Install Prerequisites: Java, SBT, Node

* Install application's node dependencies:
```sh
npm install
```

* Run the tests:
```sh
sbt test
```

* Build the Scala code:
```sh
sbt fastLinkJS
```

* OR - Let sbt watch files
During active development, this will automatically compile changes incrementally
```sh
sbt ~fastLinkJS
```

* Package the javascript and start a build server:
You can leave this running along with sbt watching files for changes
```sh
npm run start
```

Now navigate to [http://localhost:1234/](http://localhost:1234/) to see your site running.

* OR - Package for production:

Update applauncher.js: 'threetyrz-fastopt' => 'threetyrz-opt'
```sh
sbt fullLinkJS
npm run build
```

TODO:
* Propagate initialization values somehow
* Figure out best way to handle animation loop(s) between three.js and tyrian Elements
* Move tyrian element library outside of threetyrz package
* Split element library into separate repository and publish
* Split threejs interop code into separate repository and publish:
Example Published Lib: https://github.com/dcascaval/scala-threejs-facades/tree/master
Publish Steps:
https://www.awwsmm.com/blog/publish-your-scala-sbt-project-to-maven-in-5-minutes-with-sonatype
Once interop code is split, we can re-enable scalafix and tpolecat settings

Global / onChangedBuildSource := ReloadOnSourceChanges
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / scalaVersion := "3.4.1"
ThisBuild / organization := "zynque.org"
ThisBuild / version := "0.0.1"

lazy val commonSettings: Seq[sbt.Def.Setting[?]] = Seq(
  libraryDependencies ++= Seq(
    "io.indigoengine" %%% "tyrian" % "0.11.0",
  ),
  scalacOptions ++= Seq("-language:strictEquality"),
  scalafixOnCompile := true,
  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision,
  autoAPIMappings   := true,
  scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
)

lazy val tyriancombinators =
  (project in file("tyrian-combinators"))
    .enablePlugins(ScalaJSPlugin)
    .settings(
      name         := "tyrian-combinators",
      libraryDependencies ++= Seq(
        "org.scalameta"   %%% "munit"     % "0.7.29" % Test
      ),
      testFrameworks += new TestFramework("munit.Framework"),
      commonSettings,
    )

lazy val examples =
  (project in file("examples"))
    .enablePlugins(ScalaJSPlugin)
    .settings(
      name         := "examples",
      libraryDependencies ++= Seq(
        "io.indigoengine" %%% "tyrian-zio" % "0.11.0",
        "dev.zio"         %%% "zio-interop-cats" % "23.1.0.3",
      ),
      commonSettings,
    )
    .dependsOn(tyriancombinators)

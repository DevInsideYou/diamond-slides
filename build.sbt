import Dependencies._
import Util._

ThisBuild / organization := "dev.insideyou"
ThisBuild / scalaVersion := "3.1.2"

ThisBuild / scalacOptions ++=
  Seq(
    "-deprecation",
    "-explain",
    "-feature",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Yexplicit-nulls", // experimental (I've seen it cause issues with circe)
    "-Ykind-projector",
    "-Ysafe-init", // experimental (I've seen it cause issues with circe)
  ) ++ Seq("-rewrite", "-indent") ++ Seq("-source", "future-migration")

lazy val `diamond-slides` =
  project
    .in(file("."))
    .aggregate(
      // layer 1
      // team red
      `core-headers`,
      // team yellow
      `lib5-util`,
      `lib7-util`,
      // layer 2
      // team blue
      `delivery-http-lib5`,
      `kafka-consumer-lib7`,
      `scheduler-lib8`,
      `job-processor-lib9`,
      // team green
      core,
      `persistence-db-postgres-lib1`,
      `persistence-db-postgres-lib2`,
      `config-file-lib3`,
      `config-db-mongo-lib4`,
      `external-google-maps-http-lib5`,
      `external-stripe-http-lib5`,
      `internal-accounting-grpc-lib6`,
      `internal-kafka-producer-lib7`,
      // layer 3
      // team red
      main,
    )

// layer 1

// team red

lazy val `core-headers` =
  project
    .in(file("01-core-headers"))
    .settings(commonSettings)
    .settings(commonDependencies)
    .settings(
      libraryDependencies ++= Seq(
        // the less the better
      )
    )

// layer 2

// team yellow (c=common)

lazy val `lib5-util` =
  project
    .in(file("02-c-lib5-util"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        // lib5
      )
    )

lazy val `lib7-util` =
  project
    .in(file("02-c-lib7-util"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        // lib7
      )
    )

// team blue (i=input) from here

lazy val `delivery-http-lib5` =
  project
    .in(file("02-i-delivery-http-lib5"))
    .dependsOn(`lib5-util` % Cctt) // the dependencies on `core-headers` and lib5 are added transitively
    .settings(commonSettings)

lazy val `kafka-consumer-lib7` =
  project
    .in(file("02-i-kafka-consumer-lib7"))
    .dependsOn(`lib7-util` % Cctt) // the dependencies on `core-headers` and lib7 are added transitively
    .settings(commonSettings)

lazy val `scheduler-lib8` =
  project
    .in(file("02-i-scheduler-lib8"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        // lib8
      )
    )

lazy val `job-processor-lib9` =
  project
    .in(file("02-i-job-processor-lib9"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        // lib9
      )
    )

// team green (o=output) from here

lazy val core =
  project
    .in(file("02-o-core"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        // the less the better
      )
    )

lazy val `persistence-db-postgres-lib1` =
  project
    .in(file("02-o-persistence-db-postgres-lib1"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        // lib1
      )
    )

lazy val `persistence-db-postgres-lib2` =
  project
    .in(file("02-o-persistence-db-postgres-lib2"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        // lib2
      )
    )

lazy val `config-file-lib3` =
  project
    .in(file("02-o-config-file-lib3"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        // lib3
      )
    )

lazy val `config-db-mongo-lib4` =
  project
    .in(file("02-o-config-db-mongo-lib4"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        // lib4
      )
    )

lazy val `external-google-maps-http-lib5` =
  project
    .in(file("02-o-external-google-maps-http-lib5"))
    .dependsOn(`lib5-util` % Cctt) // the dependencies on `core-headers` and lib5 are added transitively
    .settings(commonSettings)

lazy val `external-stripe-http-lib5` =
  project
    .in(file("02-o-external-stripe-http-lib5"))
    .dependsOn(`lib5-util` % Cctt) // the dependencies on `core-headers` and lib5 are added transitively
    .settings(commonSettings)

lazy val `internal-accounting-grpc-lib6` =
  project
    .in(file("02-o-internal-accounting-grpc-lib6"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        // lib6
      )
    )

lazy val `internal-kafka-producer-lib7` =
  project
    .in(file("02-o-internal-kafka-producer-lib7"))
    .dependsOn(`lib7-util` % Cctt) // the dependencies on `core-headers` and lib7 are added transitively
    .settings(commonSettings)

// layer 3

// team red

lazy val main =
  project
    .in(file("03-main"))
    // the dependency on `core-headers` is added transitively
    // the dependencies on team yellow are added transitively
    // team blue
    .dependsOn(`delivery-http-lib5` % Cctt)
    .dependsOn(`kafka-consumer-lib7` % Cctt)
    .dependsOn(`scheduler-lib8` % Cctt)
    .dependsOn(`job-processor-lib9` % Cctt)
    // team green
    .dependsOn(core % Cctt)
    .dependsOn(`persistence-db-postgres-lib1` % Cctt)
    .dependsOn(`persistence-db-postgres-lib2` % Cctt)
    .dependsOn(`config-file-lib3` % Cctt)
    .dependsOn(`config-db-mongo-lib4` % Cctt)
    .dependsOn(`external-google-maps-http-lib5` % Cctt)
    .dependsOn(`external-stripe-http-lib5` % Cctt)
    .dependsOn(`internal-accounting-grpc-lib6` % Cctt)
    .dependsOn(`internal-kafka-producer-lib7` % Cctt)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        // the less the better (usually zero)
      )
    )

lazy val commonSettings = commonScalacOptions ++ Seq(
  update / evictionWarningOptions := EvictionWarningOptions.empty
)

lazy val commonScalacOptions = Seq(
  Compile / console / scalacOptions --= Seq(
    "-Wunused:_",
    "-Xfatal-warnings",
  ),
  Test / console / scalacOptions :=
    (Compile / console / scalacOptions).value,
)

lazy val commonDependencies = Seq(
  libraryDependencies ++= Seq(
    // main dependencies
  ),
  libraryDependencies ++= Seq(
    org.scalatest.scalatest,
    org.scalatestplus.`scalacheck-1-15`,
  ).map(_ % Test),
)

name := "stateNevents"

scalaVersion := "2.10.4"

version := "0.1"

EclipseKeys.withSource := true

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.6",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.6"
)

libraryDependencies ++= Seq(
  "io.spray" %% "spray-can" % "1.3.2",
  "io.spray" %% "spray-routing" % "1.3.2",
  "io.spray" %% "spray-json" % "1.2.5"
)

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "0.6.0"


libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.7"

//libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.3"

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/Releases"

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/"

resolvers += "spray repo" at "http://repo.spray.io"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-language:_",
  "-target:jvm-1.6",
  "-encoding", "UTF-8"
)

// webapp task for
resourceGenerators in Compile <+= (resourceManaged, baseDirectory) map { (managedBase, base) =>
  val webappBase = base / "src" / "main" / "webapp"
  for {
    (from, to) <- webappBase ** "*" x rebase(webappBase, managedBase / "main" / "webapp")
  } yield {
    Sync.copy(from, to)
    to
  }
}

// watch webapp files
watchSources <++= baseDirectory map { path => ((path / "src" / "main" / "webapp") ** "*").get }

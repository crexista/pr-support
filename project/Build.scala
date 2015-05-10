import sbt._
import sbt.Keys._

object PRSupportBuild extends Build {

	lazy val root = Project("PR_Support", file(".")).settings(
		organization := "st.crexi",
		scalaVersion := "2.11.0",

		libraryDependencies ++= {
			val akkaV = "2.3.9"
			val sprayV = "1.3.1"
			Seq(
				"io.spray"          %% "spray-can"     % sprayV,
				"io.spray"          %% "spray-http"    % sprayV,
				"io.spray"          %% "spray-servlet" % sprayV,
				"io.spray"          %% "spray-routing" % sprayV,
				"io.spray"          %% "spray-httpx"   % sprayV,
				"io.spray"          %% "spray-util"    % sprayV,
				"io.spray"          %% "spray-json"    % sprayV cross CrossVersion.binary,
				"io.spray"          %% "spray-client"  % sprayV cross CrossVersion.binary,
				"com.typesafe.akka" %% "akka-actor"    % akkaV,
				"io.spray"          %% "spray-testkit" % sprayV   % "test",
				"com.typesafe.akka" %% "akka-testkit"  % akkaV    % "test",
				"org.specs2"        %% "specs2-core"   % "2.3.11" % "test"
			)
		}
	)
}

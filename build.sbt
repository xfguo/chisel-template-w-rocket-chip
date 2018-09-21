organization := "com.example"
name := "project-template"
version := "0.0.1"

lazy val commonSettings = Seq(
  scalaVersion := "2.12.4",  // This needs to match rocket-chip's scalaVersion
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xsource:2.11",
    "-language:reflectiveCalls"
  )
)

// A RootProject (not well-documented) tells sbt to treat the target directory
// as its own root project, reading its build settings. If we instead used the
// normal `project in file()` declaration, sbt would ignore all of rocket-chip's
// build settings, and therefore not understand that it has its own dependencies
// on chisel, etc.
lazy val rocketChip = RootProject(file("lib/rocket-chip"))

lazy val templatePlatforms = (project in file(".")).
  dependsOn(rocketChip).
  settings(commonSettings: _*)

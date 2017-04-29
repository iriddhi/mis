import Dependencies._
import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._
import com.typesafe.sbt.SbtScalariform.{ ScalariformKeys, scalariformSettings }
import net.virtualvoid.sbt.graph.DependencyGraphSettings.graphSettings
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import playscalajs.ScalaJSPlay
import sbt.Keys._
import sbt._

object Shared {
  val projectId = "amumis"
  val projectName = "AMUMIS"

  object Versions {
    val app = "1.0.0"
    val scala = "2.11.11"
  }

  lazy val commonSettings = Seq(
    version := Shared.Versions.app,
    scalaVersion := Shared.Versions.scala,

    scalacOptions ++= Seq(
      "-encoding", "UTF-8", "-feature", "-deprecation", "-unchecked", "–Xcheck-null", "-Xfatal-warnings", "-Xlint",
      "-Ywarn-adapted-args", "-Ywarn-dead-code", "-Ywarn-inaccessible", "-Ywarn-nullary-override", "-Ywarn-numeric-widen"
    ),
    scalacOptions in Test ++= Seq("-Yrangepos"),

    publishMavenStyle := false,

    // Prevent Scaladoc
    publishArtifact in (Compile, packageDoc) := false,
    publishArtifact in packageDoc := false,
    sources in (Compile,doc) := Seq.empty,

    // Code Quality
    scapegoatVersion := Utils.scapegoatVersion,
    scapegoatDisabledInspections := Seq("MethodNames", "MethodReturningAny", "DuplicateImport"),
    scapegoatIgnoredFiles := Seq(".*/JsonSerializers.scala"),
    ScalariformKeys.preferences := ScalariformKeys.preferences.value
  ) ++ graphSettings ++ scalariformSettings

  def withProjects(p: Project, includes: Seq[Project]) = includes.foldLeft(p) { (proj, inc) =>
    proj.aggregate(inc).dependsOn(inc)
  }

  lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).settings(commonSettings: _*).jsSettings(
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "upickle" % Serialization.version,
      "com.beachape" %%% "enumeratum-upickle" % Utils.enumeratumVersion
    )
  ).jvmSettings(
    libraryDependencies ++= Seq(Dependencies.Serialization.uPickle, Dependencies.Utils.enumeratum)
  )

  lazy val sharedJs = shared.js.enablePlugins(ScalaJSPlay).settings(scalaJSStage in Global := FastOptStage)

  lazy val sharedJvm = shared.jvm
}

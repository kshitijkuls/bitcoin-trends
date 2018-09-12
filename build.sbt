name := "bitcoin-trends"

version := "0.1"

scalaVersion := "2.11.11"

libraryDependencies ++= (Dependencies.apiDependencies ++ Dependencies.coreDependencies)

test in assembly := {}

updateOptions := updateOptions.value.withCachedResolution(true)

assemblyJarName in assembly := "bitcoin-trends.jar"

assemblyMergeStrategy in assembly := {
  case PathList("reference.conf") => MergeStrategy.concat
  case entry =>
    val strategy = (assemblyMergeStrategy in assembly).value(entry)
    if (strategy == MergeStrategy.deduplicate) MergeStrategy.first
    else strategy
}
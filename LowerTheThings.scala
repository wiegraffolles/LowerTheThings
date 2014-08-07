#!/usr/bin/env scalas
 
/***
scalaVersion := "2.11.2"

libraryDependencies += "org.scala-sbt" % "io" % "0.13.5"
*/

import sbt._, Path._, sbt.IO 
import java.io.File
import java.net.{URI, URL}

// Alias for defining files
def file(s: String): File = new File(s)

/* In case that the file is a directory, rename it and then
   run the function recursively on each of its children.
   java.io.File.listFiles sometimes throws null pointers, so we need
   to filter for that. */
   
def renameRecursive(x: File): Unit =
  if(x.isDirectory) {
    renameLower(x)
    if (x.listFiles != null)
      x.listFiles.foreach { x => renameRecursive(x) }
  }
  else
    renameLower(x)


def renameLower(x: File): Unit = {
  
  // Get the absolute path and split it into individual strings
  val absPath: String = x.absolutePath
  var splitPath: Array[String] = absPath.split("/")
  
  /* In case of hidden files remove the extra '.' at the start of file name,
     in either case lower case the file name */
  if (splitPath.last.head == '.')
    splitPath(splitPath.length - 1) = splitPath.last.tail.toLowerCase
  else
    splitPath(splitPath.length - 1) = splitPath.last.toLowerCase
  
  // Create a new File out of the reconstructed path
  val newFile: File = file(splitPath.mkString("/"))
  
  // Overwrite the file with the new path
  x.renameTo(newFile)
}

//Get all local files
val srcDir = file(".")
val fs: Seq[File] = (srcDir * "*").get

// Call recursive function for first time on all files in local directory
fs.foreach { x => renameRecursive(x) }

println("All files successfully renamed to lower case.")










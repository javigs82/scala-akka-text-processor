package me.javigs82.textprocessor.util

import org.apache.spark.ml.feature.{CountVectorizerModel, Tokenizer}
import org.apache.spark.ml.linalg.SparseVector
import org.apache.spark.sql.{Row, SparkSession}

import org.apache.log4j.{Level, Logger}

object TfIdfUtil {

  private val spark = SparkSession
    .builder
    .config("spark.master", "local")
    .appName("TfIdfUtil")
    .getOrCreate()


  def getTF(path: String, tt: String): Double = {


    val documents = spark.read.text(path).toDF("document")
    val tokenizer = new Tokenizer().setInputCol("document").setOutputCol("words")

    val wordsData = tokenizer.transform(documents)


    val ttArray = tt.toLowerCase.split(" ")

    val cvm = new CountVectorizerModel(ttArray)
      .setInputCol("words")
      .setOutputCol("features")

    val cvmTransform = cvm.transform(wordsData)

    //cvmTransform.show(false)

    val features = cvmTransform .select("features").collectAsList()

    val featureSparseVector = {
      features.toArray.map(row => row.asInstanceOf[Row].get(0).asInstanceOf[SparseVector].values.sum)
    }

    //necessary to free memory
    spark.catalog.clearCache()

    documents.unpersist()

    featureSparseVector.sum

  }


}

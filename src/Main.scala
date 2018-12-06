import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions.explode
import org.apache.spark.sql.{DataFrame, SparkSession}

object App
{
  val CurrentPath = System.getProperty("user.dir")
  val spark: SparkSession = SparkSession.builder.master("local").getOrCreate

  def main(args: Array[String]): Unit =
  {
    val dataFrame : DataFrame = readJson()
    val rdd : RDD[String] = dataFrameToRDD(dataFrame)
    val rddInverted : RDD[(String, String)] = invertedIndex(rdd)
    rddToFile(rddInverted)
  }

  /**
    * convert Json file to Dataframe
    */
  def readJson(): DataFrame = {
    val jsonFile : String = CurrentPath + "/src/crawler/monsters.json"
    val df : DataFrame = spark.read.format("json").json(jsonFile)

    return df
  }

  /**
    * convert dataFrame to RDD
    */
  def dataFrameToRDD(dataframe: DataFrame): RDD[String] = {
    import spark.implicits._
    // convert Dataframe to RDD
    val rdd: RDD[String] = dataframe.withColumn("spells", explode($"spells")).rdd.map(_.mkString(","))

    return rdd
  }

  /**
    * map & reduce
    */
  def invertedIndex(rdd: RDD[String]): RDD[(String, String)] = {
    // convert RDD[String] to RDD[(String, String)] and swap key-value
    val inverted : RDD[(String, String)] = rdd
      .map(line => (line.split(",")(0), line.split(",")(1)) )
      .map(pair => pair.swap)

    // reduce by key
    val rddReduced : RDD[(String, String)] = inverted.reduceByKey((accum, n) => accum + ", " + n )

    return rddReduced
  }

  /**
    * convert inverted Rdd to file
    */
  def rddToFile(rdd : RDD[(String, String)]): Unit = {
    // check if folder exist
    if (new java.io.File("src/rdd_to_file").exists) {
      return
    }

    rdd.saveAsTextFile("src/rdd_to_file")
  }
}

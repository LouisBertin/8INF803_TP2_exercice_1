import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import org.apache.spark.sql.{DataFrame, SparkSession}

object App
{
  val CurrentPath = System.getProperty("user.dir")
  val spark: SparkSession = SparkSession.builder.master("local").getOrCreate

  def main(args: Array[String]): Unit =
  {
    val dataframe : DataFrame = readJson()
    val RDD : RDD[Row] = dataFrameToRDD(dataframe)

    invertedIndex(RDD)
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
  def dataFrameToRDD(dataframe: DataFrame): RDD[Row] = {
    val rdd: RDD[Row] = dataframe.rdd
    // debug
    rdd.collect().foreach(println)

    return rdd
  }

  def invertedIndex(Rdd: RDD[Row]): Unit = {
  }
}

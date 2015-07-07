import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by kallurivenkatesh on 7/6/15.
 */
object SparkTweets {
  def main(args: Array[String]) {



  val conf =new SparkConf().setAppName("TwitterTweets").setMaster("local")

  val sc = new SparkContext(conf)

  val sqlContext = new SQLContext(sc)

    val textFile = sc.textFile("src/main/resources/TweetFile.json").filter(l => l.trim != "")

    val textFile1 = sc.textFile("src/main/resources/tweets.json").filter(l => l.trim != "")


  val rdds = Seq(textFile,textFile1)

  val TestRDD = sc.union(textFile,textFile1)

    val tweetData = sqlContext.jsonRDD(TestRDD)

  //  val TweetTable = sqlContext.jsonRDD(text)

    tweetData.registerTempTable("TweetTable")

    val texts = sqlContext.sql("SELECT text FROM TweetTable where text <> '' ").map(r=> r.getString(0))

    print("-------------------------------------------------------------------------------------")

    print("Hii")



    //texts.flatMap(l => readLine().split(" "))

    val counts = texts.flatMap(l=> l.split(" ")).map(word => (word, 1)).reduceByKey(_ + _)

    val sortedData=  counts.sortBy(c=> c._2, false)

    val topValues = sortedData.take(10)


    topValues.foreach(println)

  /*val counts = TestRDD.flatMap(line => line.split(" ")).map(word => (word,1)).reduceByKey(_ + _)

  counts.foreach(println)
*/


  }




}

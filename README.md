# Scala akka Text Processor

Text Processor is a concurrent text processor system  written in Scala and built on top of akka framework.

It aims to show how to use actor model paradigm to implement concurrency operations avoiding locks and blocks 
(and also their tedious and complex use) while focusing in the business requirements.
 
## Main Requirements

Tf/idf (term frequency / inverse document frequency) is an statistic that reflects the importance of a term T in a 
document D (or the relevance of a document for a searched term) relative to a document set S.

See wikipedia article in [this link](https://en.wikipedia.org/wiki/Tf%E2%80%93idf)

Assume that we have a directory D containing a document set S, with one file per document.
Documents will be added to that directory by external agents,but they will never be removed or
overwritten.
We are given a set of terms TT, and asked to compute the tf/idf of TT for each document in D, and
report the N top documents sorted by relevance.

The program must run as a daemon/service that is watching for new documents, and dynamically
updates the computed tf/idf for each document and the inferred ranking.

The program will run with the parameters:

- The directory D where the documents will be written.

- The terms TT to be analyzed.

- The count N of top results to show.
  
- The period P to report the top N.

For example

```bash
[javi@localhost javigs82]$ tdIdf -d dir -n 5 -p 300 -t "password try again"

...

doc1.txt 0.78
doc73.txt 0.76
```


### Assumptions

 - Persistence is not the objective of the subject.
 - Spark for ml features.
 - This version only contains printing of topN TF documents. TF-idf will implement in future versions.


### Caveats in this version

 - as scala object are lazy, first analysis has to start spark context up.
 - in this version, tf-idf has not been full archived, so printer should show something like:
 
 ```scala
 
 val cvm = new CountVectorizerModel(ttArray)
       .setInputCol("words")
       .setOutputCol("features").setMinTF(0)
       
```
 
## Actor Architecture 

In order to develop the solution, following actor hierarchy has been defined:

- TextProcessorApp: Main application. Receives paramters and send it to proper actors. 

- TextProcessorSupervisor: It calls to ProcessManager to start listen directory and PrinterManager.

  - TextProcessorManager: In charge of coordinate task like watching directory, analyze tf for documents and recovery
  on startup
  
    - Analyzer: It provides analysis functions
    
  - PrinterManager: In charge of printing periodically via `akka.timers`

        
### Messages

Messages are defined in the companion object of actor classes.

## Prerequisites

What things you need to install the software and how to install them

* JDK 1.8
* Scala 2
* SBT 1


## Installing

A step by step series of examples that tell you have to get a development env running

```
sbt clean
```
```
sbt compile
```
```
sbt dist
```

## Running the tests

Explain how to run the automated tests for this system

```
sbt test
```

## Deployment

Add additional notes about how to deploy this on a live system choosing your preferred options

```
sbt run
```

## References

 - [Watching a Directory for Changes](https://docs.oracle.com/javase/tutorial/essential/io/notification.html)
 - [Spark ml features - tfidf](https://spark.apache.org/docs/2.2.0/ml-features.html#tf-idf) )

## Author
 
 * javigs82 [github](https://github.com/javigs82/)
 
## License
 
 This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
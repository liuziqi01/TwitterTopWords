//Reference: Develop Java-based topologies for a basic word-count application with Apache Storm and Maven on HDInsight WordCount.java https://azure.microsoft.com/en-us/documentation/articles/dinsight-storm-develop-java-topology  
package com.microsoft.example;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;
import java.text.BreakIterator;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import twitter4j.HashtagEntity;
import twitter4j.Status;

public class SplitSentence extends BaseFunction {

    //Execute is called to process tuples
  @Override
  public void execute(TridentTuple tuple, TridentCollector collector) {
      //Get the sentence content from the tuple
      String sentence = ((Status)tuple.get(0)).getText();
      //An iterator to get each word
      BreakIterator boundary=BreakIterator.getWordInstance();
      //Give the iterator the sentence
      boundary.setText(sentence);
      //Find the beginning first word
      int start=boundary.first();
      //Iterate over each word and emit it to the output stream
      for (int end=boundary.next(); end != BreakIterator.DONE; start=end, end=boundary.next()) {
	  //get the word
	  String word=sentence.substring(start,end);
	  //If a word is whitespace characters, replace it with empty
	  word=word.replaceAll("\\s+","");
	  //if it's an actual word, emit it
	  if (!word.equals("")) {
	      collector.emit(new Values(word));
	  }
      }
  }
}

package com.microsoft.example;

import java.util.HashMap;
import java.util.Map;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

//There are a variety of bolt types. In this case, we use BaseBasicBolt
public class WordCount extends BaseFunction{
    //For holding words and counts
    Map<String, Integer> counts = new HashMap<String, Integer>();

    //execute is called to process tuples
    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
	//Get the word contents from the tuple
	String word = tuple.getString(0);
	//Have we counted any already?
	Integer count = counts.get(word);
	if (count == null)
	    count = 0;
	//Increment the count and store it
	count++;
	counts.put(word, count);
	//Emit the word and the current count
	collector.emit(new Values(count));
    }


}

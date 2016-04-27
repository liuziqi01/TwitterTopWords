package com.microsoft.example;

import java.util.HashMap;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import java.util.Date;
import java.sql.Timestamp;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

//There are a variety of bolt types. In this case, we use BaseBasicBolt
public class OutputTime extends BaseFunction{
    //For holding words and counts


    //execute is called to process tuples
    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
	//Get the word contents from the tuple
	String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        int currSec = new Date().getSeconds();
      	if((currSec + 10)  %30 ==0)
	    System.out.println(timeStamp +" ");
	collector.emit(new Values(timeStamp));
    }


}
package com.microsoft.example;

import java.util.HashMap;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.ReducerAggregator;
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
import java.util.*;

public class ConvertToList implements ReducerAggregator<List<String>> {
    public List<String> init() {
        return new ArrayList<String>();
    }

    public List<String> reduce(List<String> curr, TridentTuple tuple) {
        //System.out.println("converting " + tuple);
	if(tuple.size()!=0)
	    curr.add(tuple.getString(0));
	String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        int currSec = new Date().getSeconds();
	if(curr.size() ==10 && (currSec + 13) %30 < 4)
	    printOutput(curr);
	return curr;
    }
    
    public void printOutput(List<String> list){
	String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	System.out.print(timeStamp + " ");
	for( String s : list){
	    System.out.print(s + " ");
	}
	System.out.println();
    }
}

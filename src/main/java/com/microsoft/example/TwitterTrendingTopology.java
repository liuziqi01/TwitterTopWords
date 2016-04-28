package com.microsoft.example;


import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Count;
import storm.trident.operation.builtin.Debug;
import storm.trident.operation.builtin.FirstN;
import storm.trident.spout.IBatchSpout;
import storm.trident.testing.MemoryMapState;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import backtype.storm.StormSubmitter;

import java.io.IOException;
import com.microsoft.example.SplitSentence;
import com.microsoft.example.TwitterSpout;
import com.microsoft.example.WordCount;
import com.microsoft.example.Output;
import com.microsoft.example.OutputTime;
import com.microsoft.example.ConvertToList;
public class TwitterTrendingTopology {
  //Build the topology
  public static StormTopology buildTopology(IBatchSpout spout) throws IOException {
    final TridentTopology topology = new TridentTopology();
    //Define the topology:
    //1. spout reads tweets
    //2. HashtagExtractor emits hashtags pulled from tweets
    //3. hashtags are grouped
    //4. a count of each hashtag is created
    //5. each hashtag, and how many times it has occured
    //   is emitted.
    topology.newStream("spout", spout)
    .each(new Fields("tweet"), new SplitSentence(), new Fields("word"))
    .groupBy(new Fields("word"))
	//    .persistentAggregate(new MemoryMapState.Factory(), new Count(), new Fields("count"))
	.each(new Fields("word"), new WordCount(), new Fields("count"))
	//	.newValuesStream()
	.toStream()
	.applyAssembly(new FirstN(10,"count"))
	.toStream()
	//.each(new Fields("word","count"), new Debug())
	.aggregate(new Fields("word"),new ConvertToList(), new Fields("list"));
	//.newValuesStream()
	//.each(new Fields("list"), new Debug())
	//.each(new Fields("list"), new Output(), new Fields("Output"));
	//	.each(new Fields("word"), new Debug());
	//.project(new Fields("word", "count"));
	//     .groupBy(new Fields("word"))
	//   .newValuesStream()
	//.applyAssembly(new FirstN(10, "count"))
	// .each(new Fields("word", "count"), new Debug());
    //Build and return the topology

    return topology.build();
  }

  public static void main(String[] args) throws Exception {
    final Config conf = new Config();
    final IBatchSpout spout = new TwitterSpout();
    //If no args, assume we are testing locally
    
    if(args.length==0) {
      //create LocalCluster and submit
      final LocalCluster local = new LocalCluster();
      try {
        local.submitTopology("hashtag-count-topology", conf, buildTopology(spout));
	//System.out.println(local);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else {
      //If on a real cluster, set the workers and submit
      conf.setNumWorkers(3);
      StormSubmitter.submitTopology(args[0], conf, buildTopology(spout));
    }
  }
}

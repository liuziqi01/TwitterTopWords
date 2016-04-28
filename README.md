<!--
#Twitter Trending topics with Apache Storm on HDInsight

A storm topology using Trident. It calculates trending topics (hashtags) on Twitter. This is heavily based on the [trident-storm](https://github.com/jalonsoramos/trident-storm) example by Juan Alonso.

Trident is a high-level abstraction that provides tools such as joins, aggregations, grouping, functions and filters. Additionally, Trident adds primitives for doing stateful, incremental processing. This project demonstrates how you can build a topology using a custom spout, function, and several built-in functions provided by trident.

##Download the project

git clone this repository

##What it does

The Trident code that implements the topology is as follows:

	topology.newStream("spout", spout)
		.each(new Fields("tweet"), new HashtagExtractor(), new Fields("hashtag"))
		.groupBy(new Fields("hashtag"))
		.persistentAggregate(new MemoryMapState.Factory(), new Count(), new Fields("count"))
		.newValuesStream()
		.applyAssembly(new FirstN(10, "count"))
		.each(new Fields("hashtag", "count"), new Debug());

This does the following:

1. Create a new stream from the spout. The spout retrieves tweets from Twitter, filtered for specific keywords - love, music, and coffee in this example.

2. HashtagExtractor, a custom function, is used to extract hashtags from each tweet. These are emitted to the stream.

3. The stream is then grouped by hashtag, and passed to a aggregator. This aggregator creates a count of how many times each hashtag has occurred, which is persisted in memory. Finally, a new stream is emitted that contains the hashtag and the count.

4. Since we are only interested in the most popular hashtags for a given batch of tweets, the FirstN assembly is applied to return only the top 10 values based on the count field.

Other than the spout and HashtagExtractor, we are using built-in Trident functionality.

For information on built-in operations, see <a href="https://storm.apache.org/apidocs/storm/trident/operation/builtin/package-summary.html" target="_blank">storm.trident.operation.builtin</a>.

For Trident-state implementations other than MemoryMapState, see the following:

* <a href="https://github.com/fhussonnois/storm-trident-elasticsearch" target="_blank">https://github.com/fhussonnois/storm-trident-elasticsearch</a>

* <a href="https://github.com/kstyrc/trident-redis" target="_blank">https://github.com/kstyrc/trident-redis</a>

###The spout

The spout, **TwitterSpout** uses <a href="http://twitter4j.org/en/" target="_blank">Twitter4j</a> to retrieve tweets from Twitter. A filter is created (love, music, and coffee,) and incoming tweets (status) that match the filter are stored into a <a href="http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/LinkedBlockingQueue.html" target="_blank">LinkedBlockingQueue</a>. Finally, items are pulled off the queue and emitted into the topology.

###The HashtagExtractor

To extract hashtags, <a href="http://twitter4j.org/javadoc/twitter4j/EntitySupport.html#getHashtagEntities--" target="_blank">getHashtagEntities</a> is used to retrieve all hashtags contained in the tweet. These are then emitted to the stream.


##Get a twitter account

Use the following steps to register a new Twitter Application and obtain the consumer and access token information needed to read from Twitter.

1. Go to <a href="" target="_blank">https://apps.twitter.com/</a> and use the **Create new app** button. When filling in the form, leave **Callback URL** empty.

2. Once the app has been created, select the **Keys and Access Tokens** tab.

3. Copy the **Consumer Key** and **Consumer Secret** information.

4. At the bottom of the page, select **Create my access token** if no tokens exist. Once the tokens have been created, copy the **Access Token** and **Access Token Secret** information.

5. In the **TwitterSpoutTopology** project you previously cloned, open the **resources/twitter4j.properties** file, and add the information gathered in the previous steps and then save the file.

##Build the topology

Use the following build the project.

		cd TwitterTrending
		mvn compile

##Test the topology

Use the following command to test the topology locally.

	mvn compile exec:java -Dstorm.topology=com.microsoft.example.TwitterTrendingTopology

After the topology starts, you should see debug information containing the hashtags and counts emitted by the topology. The output should appear similar to the following.

	DEBUG: [Quicktellervalentine, 7]
	DEBUG: [GRAMMYs, 7]
	DEBUG: [AskSam, 7]
	DEBUG: [poppunk, 1]
	DEBUG: [rock, 1]
	DEBUG: [punkrock, 1]
	DEBUG: [band, 1]
	DEBUG: [punk, 1]
	DEBUG: [indonesiapunkrock, 1]
-->
# TwitterTopWords
Using Apache Storm to get the real-time sentiment of a certain hash tag.
report the top 10 words that are not stop words (http://www.ranks.nl/stopwords) in all the live
tweets In English that contain the #gameofthrones hashtag.
##What it does

The Trident code that implements the topology is as follows:

	topology.newStream("spout", spout)
    .each(new Fields("tweet"), new SplitSentence(), new Fields("word"))
	.each(new Fields("word"),new FilterStopWords())
	.groupBy(new Fields("word"))
	.each(new Fields("word"), new WordCount(), new Fields("count"))
	.toStream()
	.applyAssembly(new FirstN(10,"count"))
	.toStream()
	.aggregate(new Fields("word"),new ConvertToList(), new Fields("list"));


###The spout

The spout, **TwitterSpout** uses <a href="http://twitter4j.org/en/" target="_blank">Twitter4j</a> to retrieve tweets from Twitter. A filter is created (love, music, and coffee,) and incoming tweets (status) that match the filter are stored into a <a href="http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/LinkedBlockingQueue.html" target="_blank">LinkedBlockingQueue</a>. Finally, items are pulled off the queue and emitted into the topology.

##Test the topology

Use the following command to test the topology locally.

	mvn compile exec:java -Dstorm.topology=com.microsoft.example.TwitterTrendingTopology

After the topology starts, you should see debug information containing the hashtags and counts emitted by the topology. The output should appear similar to the following.


	2016.04.28.05.46.18 Watch 3ujAe0LsqT Recap Rap Killer Video awm56w1kEb TheRoots JimmyFallon https 
	2016.04.28.05.46.48 just entered $100 e-gift giveaway open GoTSeason6 WW Amazon GameOfThrones 
	2016.04.28.05.47.20 ILoveNorthCoast Co.Antrim thanks 65% UK's interest 9PHPm3q9lU tourism sees http 
	2016.04.28.05.47.47 JellyZombie ti cGP9xvoPLi Señor confiamos 🙏 🏻 en RT t.co 
	2016.04.28.05.47.47 Ai pessoa pessoalmente com acho desculpa enganei encontra você d0Lxg2x2kc 
	2016.04.28.05.47.50 EW teases got He's Westeros Isaac_H_Wright return nYdxVFc8di Stark's star 
	2016.04.28.05.47.50 MyTeenGuide Today Re w5ZV2VA4Lm 7X075GvAqu Is Day Episode Season The 
	2016.04.28.05.48.19 GoT_Tyrion fate Fuck Jaime us Lannister isn zlsR7JmAyp everyone prophecies 
	2016.04.28.05.49.50 Fidarsi soprattutto Sq3OBVV4nz non meglio TronoDiSpade SkyOnline TronoDiMeme fidarsi bene 
	2016.04.28.05.50.20 Certified badass psrGSvS7sj Whether guaranteed kick ass StarWars GameofThrones t.co 
	2016.04.28.05.51.18 box Blu-Ray x6UHKSg3eT set shape HBO Mall d8LqzsvF23 great Used 
	2016.04.28.05.52.48 De neige La promo avril fin va trop loin WinterIsComing 
	2016.04.28.05.52.48 Obi1Shaneobi G1MAdZZzgd actually 200 old yo years girl find When 


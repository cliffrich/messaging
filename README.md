messaging
Scenarios
1. Start 2 brokers with 1 partition and 1 topic. Post 5 messages.
    * Consume from partition0 and from offset 0
    * consume from partition0 and from offset 3
    Stop 1 broker and try consuming from offset 0 - Should produce same result
    Stop both brokers and try consuming from offset 0 - Should not produce any result
    Start 1 broker and try consuming from offset 0 - Should produce result
    
2. Start 2 brokers with 5 partitions each and 1 topic. Post 5 messages. Messages should be spread across partitions
    * Consume from partition0 and from offset 0. You should get just 1 message.
    * Consume from partition1 to partition4 and you should get just 1 different message from each partition.
    Stop 1 broker - EXPECT REBALANCING OF PARTITIONS
    * Consume from partition0-4 and expect more than one from at least 1 partition
    Start the other broker - EXPECT REBALANCING OF PARTITIONS
    * Consume from partition0-4 and expect more than one from at least 1 partition

3. Two topics ...
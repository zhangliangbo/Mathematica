package xxl.kafka;

import java.util.List;

public class ConsumerDemo {
  public static void main(String[] args) {
    KfkConsumer consumer = new KfkConsumer(new String[]{"localhost:9094", "localhost:9095", "localhost:9096"}, "xxl");
    consumer.subscribe("test");
    while (true) {
      List<Record> records = consumer.poll(1000);
      if (records != null) {
        for (Record record : records) {
          System.err.println(record);
          consumer.commitSync();
        }
      }
    }
  }
}

package xxl.flink;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.wikiedits.WikipediaEditEvent;
import org.apache.flink.streaming.connectors.wikiedits.WikipediaEditsSource;

/**
 * 测试
 * https://ci.apache.org/projects/flink/flink-docs-release-1.9/getting-started/tutorials/datastream_api.html
 */
public class WikipediaAnalysis {
  public static void main(String[] args) throws Exception {

    StreamExecutionEnvironment see = StreamExecutionEnvironment.getExecutionEnvironment();

    DataStream<WikipediaEditEvent> edits = see.addSource(new WikipediaEditsSource());

    KeyedStream<WikipediaEditEvent, String> keyedEdits = edits
        .keyBy(new KeySelector<WikipediaEditEvent, String>() {
          @Override
          public String getKey(WikipediaEditEvent event) {
            return event.getUser();
          }
        });

    DataStream<Tuple2<String, Long>> result = keyedEdits
        .timeWindow(Time.seconds(5))
        .aggregate(new AggregateFunction<WikipediaEditEvent, Tuple2<String, Long>, Tuple2<String, Long>>() {
          @Override
          public Tuple2<String, Long> createAccumulator() {
            return new Tuple2<>("", 0L);
          }

          @Override
          public Tuple2<String, Long> add(WikipediaEditEvent value, Tuple2<String, Long> accumulator) {
            accumulator.f0 = value.getUser();
            accumulator.f1 += value.getByteDiff();
            return accumulator;
          }

          @Override
          public Tuple2<String, Long> getResult(Tuple2<String, Long> accumulator) {
            return accumulator;
          }

          @Override
          public Tuple2<String, Long> merge(Tuple2<String, Long> a, Tuple2<String, Long> b) {
            return new Tuple2<>(a.f0, a.f1 + b.f1);
          }
        });

    result.print();

    see.execute();
  }
}

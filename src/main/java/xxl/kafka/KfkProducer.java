package xxl.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import xxl.mathematica.string.StringRiffle;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 生产者
 */
public class KfkProducer {
    private final Producer<byte[], byte[]> producer;

    public KfkProducer(String[] servers, int ack) {
        producer = new KafkaProducer<>(props(servers, ack));
    }

    /**
     * 通用属性
     *
     * @param servers
     * @param ack
     * @return
     */
    public static Properties props(String[] servers, int ack) {
        Properties props = new Properties();
        props.put("bootstrap.servers", StringRiffle.stringRiffle(Arrays.asList(servers), ","));
        props.put("acks", String.valueOf(ack));
        props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        return props;
    }

    /**
     * 发送数据
     *
     * @param topic
     * @param partition
     * @param timestamp
     * @param key
     * @param value
     * @return 分区offset
     */
    public SendResult send(String topic, Integer partition, Long timestamp, byte[] key, byte[] value) {
        ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(topic, partition, timestamp, key, value);
        Future<RecordMetadata> future = producer.send(record);
        try {
            RecordMetadata metadata = future.get();
            return new SendResult(metadata.timestamp(), metadata.topic(), metadata.partition(), metadata.offset());
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    /**
     * 时间默认使用System.currentTimeMillis()
     *
     * @param topic
     * @param partition
     * @param key
     * @param value
     * @return
     */
    public SendResult send(String topic, Integer partition, byte[] key, byte[] value) {
        return send(topic, partition, null, key, value);
    }

    /**
     * 使用默认分区策略
     * If a valid partition number is specified that partition will be used when sending the record. If no partition is
     * specified but a key is present a partition will be chosen using a hash of the key. If neither key nor partition is
     * present a partition will be assigned in a round-robin fashion.
     *
     * @param topic
     * @param key
     * @param value
     * @return
     */
    public SendResult send(String topic, byte[] key, byte[] value) {
        return send(topic, null, key, value);
    }

    /**
     * 默认键为空
     *
     * @param topic
     * @param value
     * @return
     */
    public SendResult send(String topic, byte[] value) {
        return send(topic, null, value);
    }

    /**
     * 关闭生产者
     */
    public void close() {
        producer.close();
    }
}

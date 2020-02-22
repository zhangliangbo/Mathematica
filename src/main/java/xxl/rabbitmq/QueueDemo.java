package xxl.rabbitmq;

public class QueueDemo {
  public static void main(String[] args) {
    RabbitMQ rabbitMQ = new RabbitMQ(
        new String[]{"localhost:5672"},
        "mqtt",
        "mqtt",
        "/",
        true
    );
    if (rabbitMQ.newChannel()) {
      boolean deleteQ = rabbitMQ.exchangeDelete("xxl", false);
      System.err.println("queue delete " + deleteQ);
      if (rabbitMQ.exchangeDeclare("xxl") && rabbitMQ.queueDeclare("zlb") && rabbitMQ.queueBind("zlb", "xxl", "xxl-zlb")) {
        if (rabbitMQ.qos(1, false)) {
          rabbitMQ.consume("zlb", "random", new RabbitConsumer() {
            @Override
            public void onDelivery(Record record) {
              if (record != null) {
                System.err.println(record);
                rabbitMQ.ack(record.deliveryTag(), false);
              }
            }
          });
        }
      }
    }
  }
}
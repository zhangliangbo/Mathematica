package xxl.rabbitmq;

import com.rabbitmq.client.*;
import xxl.mathematica.Map;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/**
 * rabbitmq连接
 */
public class RabbitMQ {
  private Connection connection;
  private Channel channel;

  public RabbitMQ(String[] servers, String username, String password, String vHost) throws IOException, TimeoutException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUsername(username);
    factory.setPassword(password);
    factory.setVirtualHost(vHost);
    connection = factory.newConnection(Map.map(t -> {
      String[] server = t.split(":");
      if (server.length < 2) {
        return new Address(server[0]);
      } else {
        return new Address(server[0], Integer.parseInt(server[1]));
      }
    }, Arrays.asList(servers)));
    channel = connection.createChannel();
  }

  /**
   * 声明交换器
   *
   * @param exchange
   * @return
   */
  public boolean exchangeDeclare(String exchange, String type, boolean durable, boolean autoDelete) {
    try {
      channel.exchangeDeclare(exchange, type, durable, autoDelete, null);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * 默认没有绑定时（先绑过，后来没绑了），自动删除
   *
   * @param exchange
   * @param type
   * @param durable
   * @return
   */
  public boolean exchangeDeclare(String exchange, String type, boolean durable) {
    return exchangeDeclare(exchange, type, durable, true);
  }

  /**
   * 默认不持久化
   *
   * @param exchange
   * @param type
   * @return
   */
  public boolean exchangeDeclare(String exchange, String type) {
    return exchangeDeclare(exchange, type, false);
  }

  /**
   * 默认direct类型
   *
   * @param exchange
   * @return
   */
  public boolean exchangeDeclare(String exchange) {
    return exchangeDeclare(exchange, "direct");
  }

  /**
   * 交换器是否存在
   *
   * @param exchange
   * @return
   */
  public boolean exchangeExist(String exchange) {
    try {
      channel.exchangeDeclarePassive(exchange);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * 声明一个队列
   *
   * @param queue
   * @param durable
   * @param exclusive
   * @param autoDelete
   * @return
   */
  public boolean queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete) {
    try {
      channel.queueDeclare(queue, durable, exclusive, autoDelete, null);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * 默认自动删除
   *
   * @param queue
   * @param durable
   * @param exclusive
   * @return
   */
  public boolean queueDeclare(String queue, boolean durable, boolean exclusive) {
    return queueDeclare(queue, durable, exclusive, true);
  }

  /**
   * 默认不绑定连接
   *
   * @param queue
   * @param durable
   * @return
   */
  public boolean queueDeclare(String queue, boolean durable) {
    return queueDeclare(queue, durable, false);
  }

  /**
   * 不持久化
   *
   * @param queue
   * @return
   */
  public boolean queueDeclare(String queue) {
    return queueDeclare(queue, false);
  }

  /**
   * 队列是否存在
   *
   * @param queue
   * @return
   */
  public boolean queueExist(String queue) {
    try {
      AMQP.Queue.DeclareOk res = channel.queueDeclarePassive(queue);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * 交换器绑定交换器
   *
   * @param dstExchange
   * @param srcExchange
   * @param routingKey
   * @return
   */
  public boolean exchangeBind(String dstExchange, String srcExchange, String routingKey) {
    try {
      channel.exchangeBind(dstExchange, srcExchange, routingKey);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * 绑定器解绑绑定器
   *
   * @param dstExchange
   * @param srcExchange
   * @param routingKey
   * @return
   */
  public boolean exchangeUnbind(String dstExchange, String srcExchange, String routingKey) {
    try {
      channel.exchangeUnbind(dstExchange, srcExchange, routingKey);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * 队列绑定交换器
   *
   * @param queue
   * @return
   */
  public boolean queueBind(String queue, String exchange, String routingKey) {
    try {
      channel.queueBind(queue, exchange, routingKey);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * 队列解绑交换器
   *
   * @param queue
   * @param exchange
   * @param routingKey
   * @return
   */
  public boolean queueUnbind(String queue, String exchange, String routingKey) {
    try {
      channel.queueUnbind(queue, exchange, routingKey);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * 发布消息
   *
   * @return
   */
  public boolean publish(String exchange, String routingKey, byte[] body) {
    try {
      channel.basicPublish(exchange, routingKey, null, body);
      return false;
    } catch (IOException e) {
      return true;
    }
  }

  /**
   * 消费消息
   *
   * @param queue
   * @param autoAck   是否自动回复
   * @param tag       消费者标签
   * @param noLocal   不消费同一个连接发的消息
   * @param exclusive 是否接受其他连接的消费者
   * @return
   */
  public boolean consume(String queue, boolean autoAck, String tag, boolean noLocal, boolean exclusive, RabbitConsumer consumer) {
    try {
      Consumer inner = consumer == null ? null : new DefaultConsumer(channel) {
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
          consumer.onDelivery(body, envelope.getExchange(), envelope.getRoutingKey(), consumerTag, envelope.getDeliveryTag(), envelope.isRedeliver());
        }
      };
      channel.basicConsume(queue, autoAck, tag, noLocal, exclusive, null, inner);
      return false;
    } catch (IOException e) {
      return true;
    }
  }

  /**
   * 取消消费
   *
   * @param consumerTag
   * @return
   */
  public boolean cancel(String consumerTag) {
    try {
      channel.basicCancel(consumerTag);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * 应答消息
   *
   * @param deliveryTag
   * @param multiple
   * @return
   */
  public boolean ack(long deliveryTag, boolean multiple) {
    try {
      channel.basicAck(deliveryTag, multiple);
      return true;
    } catch (IOException e) {
      return false;
    }
  }


  public void close() throws IOException, TimeoutException {
    channel.close();
    connection.close();
  }
}

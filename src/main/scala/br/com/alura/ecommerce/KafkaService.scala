package br.com.alura.ecommerce

import java.util.regex.Pattern
import java.util.{Collections, Properties, UUID}

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, KafkaConsumer}

import scala.collection.JavaConverters.{iterableAsScalaIterableConverter, mapAsJavaMapConverter}

class KafkaService[A] {
  var consumerGroup: String = null
  var parse: ConsumerRecord[String, A] => Unit = null
  var topic: String  = null
  var kafkaConsumer: KafkaConsumer[String, A] = null

  def this(consumerGroup: String, parse: (ConsumerRecord[String, A]) => Unit) {
    this()
    this.consumerGroup = consumerGroup
    this.parse = parse
  }

  def this(consumerGroup: String, topic: String, parse: (ConsumerRecord[String, A]) => Unit, clazz: Class[A], props: Map[String, String]) {
    this(consumerGroup, parse)
    this.kafkaConsumer = new KafkaConsumer[String, A](properties(clazz, props))
    this.kafkaConsumer.subscribe(Collections.singletonList(topic))
  }

  def this(consumerGroup: String, topic: Pattern, parse: (ConsumerRecord[String, A]) => Unit, clazz: Class[A], props: Map[String, String]) {
    this(consumerGroup, parse)
    this.kafkaConsumer = new KafkaConsumer[String, A](properties(clazz, props))
    this.kafkaConsumer.subscribe(topic)
  }

  def properties(clazz: Class[A], props: Map[String, String]) : Properties = {
    val properties = new Properties()
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[GsonDeserializer[String]].getName)
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.consumerGroup)
    properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID.toString)
    properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
    properties.setProperty(GsonDeserializer.CLAZZ_CONFIG, clazz.getName)
    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    properties.putAll(props.asJava)
    properties
  }

  def run(): Unit = {
    Stream
      .continually(kafkaConsumer.poll(100).asScala)
      .flatten
      .foreach(this.parse)
  }


}

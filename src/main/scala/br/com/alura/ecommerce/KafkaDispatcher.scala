package br.com.alura.ecommerce

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

class KafkaDispatcher[A] {
  var producer: KafkaProducer[String, A] = new KafkaProducer[String, A](properties())

  def send(topic: String, key: String, value: A): Unit = {
    val record = new ProducerRecord[String, A](topic, key, value)
    producer.send(record)
  }
  def properties() : Properties = {
    val properties = new Properties()
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaSerializer[A]].getName)
    properties
  }
}

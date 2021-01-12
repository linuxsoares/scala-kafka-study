package br.com.alura.ecommerce

import java.util.regex.Pattern

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import java.util.UUID

class LogService[A] {
  def parse(record: ConsumerRecord[String, A]): Unit = {
    println(s"Log Service Data -> ${record}")
  }
}

object LogService {
  def main(args: Array[String]): Unit = {
    val logService = new LogService[String]()
    val consumerGroup: String = s"${LogService.getClass.getSimpleName}-${UUID.randomUUID}"
    val kafkaService = new KafkaService[String](consumerGroup, Pattern.compile("ECOMMERCE.*"), logService.parse(_), classOf[String], Map(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer].getName))
    kafkaService.run()
  }
}
package br.com.alura.ecommerce

import org.apache.kafka.clients.consumer.ConsumerRecord
import java.util.UUID

class FraudDetectorService[A] {
  def parse(record: ConsumerRecord[String, A]): Unit = {
    println(s"Fraud Detector ${record.key()} - ${record.value()} - ${record}")
  }
}

object FraudDetectorService {
  def main(args: Array[String]): Unit = {
    val fraudDetectorService = new FraudDetectorService[Order]()
    val consumerGroup = s"${this.getClass.getName}-${UUID.randomUUID}"
    val kafkaService = new KafkaService[Order](consumerGroup,"ECOMMERCE_NEW_ORDER", fraudDetectorService.parse(_), classOf[Order], Map.empty[String, String])
    kafkaService.run
  }
}

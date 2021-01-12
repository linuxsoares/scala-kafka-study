package br.com.alura.ecommerce

import org.apache.kafka.clients.consumer.ConsumerRecord
import java.util.UUID

class EmailService {
  def parse(record: ConsumerRecord[String, Email]): Unit = {
    println(s"Email Service ${record.key()} - ${record.value()} - ${record}")
  }
}

object EmailService {
  def main(args: Array[String]): Unit = {
    val emailService = new EmailService()
    val consumerGroup: String = s"${this.getClass.getSimpleName}-${UUID.randomUUID}"
    val kafkaService = new KafkaService[Email](consumerGroup, "ECOMMERCE_SEND_MAIL", emailService.parse(_), classOf[Email], Map.empty[String, String])
    kafkaService.run()
  }
}

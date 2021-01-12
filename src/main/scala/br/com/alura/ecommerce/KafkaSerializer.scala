package br.com.alura.ecommerce

import com.google.gson.GsonBuilder
import org.apache.kafka.common.serialization.Serializer

class KafkaSerializer[A] extends Serializer[A] {
  val gson = new GsonBuilder().create()

  override def serialize(topic: String, data: A): Array[Byte] = {
    gson.toJson(data).getBytes
  }
}

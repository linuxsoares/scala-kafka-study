package br.com.alura.ecommerce

import java.util

import com.google.gson.GsonBuilder
import org.apache.kafka.common.serialization.Deserializer

class GsonDeserializer[A] extends Deserializer[A] {
  val gson = new GsonBuilder().create()
  var clazz: Class[A] = null

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {
    val typeName = String.valueOf(configs.get(GsonDeserializer.CLAZZ_CONFIG))
    try {
      this.clazz = Class.forName(typeName).asInstanceOf[Class[A]]
    }  catch {
      case e: Exception => {
        println(e)
        throw e
      }
    }

  }

  override def deserialize(topic: String, data: Array[Byte]): A = {
    gson
      .fromJson((data.map(_.toChar)).mkString, clazz)
  }
}

object GsonDeserializer {
  val CLAZZ_CONFIG: String = "br.com.alura.ecommerce.type_config"
}

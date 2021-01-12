package br.com.alura.ecommerce

import java.util.UUID

class NewOrderMain{
  def hello: String = "Hello World"
}

object NewOrderMain {

  def main(args: Array[String]): Unit = {
    val orderDispatcher = new KafkaDispatcher[Order]()
    val emailDispatcher = new KafkaDispatcher[Email]()
    for (i <- 1 to 100000) {
      val key = s"${UUID.randomUUID}"
      val client = Client("Gilmar Soares", UUID.randomUUID.toString, "linux.soares@gmail.com")
      val order = Order(client, UUID.randomUUID, BigDecimal(Math.random * 5000 + 1))
      val email = Email(order.client.email, s"\nBom dia!\nSeu Pedido ${UUID.randomUUID} esta em processamento.\n\nAguarde!")
      orderDispatcher.send("ECOMMERCE_NEW_ORDER", key, order)
      emailDispatcher.send("ECOMMERCE_SEND_MAIL", key, email)
    }
  }
}

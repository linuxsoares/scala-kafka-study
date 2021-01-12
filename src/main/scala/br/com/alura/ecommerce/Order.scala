package br.com.alura.ecommerce

import java.util.UUID

case class Client(name: String, document: String, email: String)

case class Order(client: Client, id: UUID, price: BigDecimal)

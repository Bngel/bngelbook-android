package cn.bngel.bngelbook.utils

import cn.bngel.bngelbook.activity.ActivityManager
import cn.bngel.bngelbook.activity.BaseActivity
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import java.io.IOException
import java.util.concurrent.TimeoutException
import kotlin.concurrent.thread

object MqUtils {

    private val context by lazy {
        ActivityManager.getCurActivity() as BaseActivity
    }
    private val rabbitMQProp = PropertiesUtils.getProperties(context, "rabbitMQConfig.properties")

    private fun getConnectionFactory(): ConnectionFactory {
        val factory = ConnectionFactory()
        factory.username = rabbitMQProp.getProperty("rabbitMQ.username")
        factory.password = rabbitMQProp.getProperty("rabbitMQ.password")
        factory.host = rabbitMQProp.getProperty("rabbitMQ.host")
        factory.port = rabbitMQProp.getProperty("rabbitMQ.port").toInt()
        return factory
    }

    fun startConsumer() {
        val defaultQueueName = "defaultQueue"
        val defaultExchangeName = "defaultExchange"
        thread {
            try {
                val connection = getConnectionFactory().newConnection()
                val channel = connection.createChannel()
                channel.exchangeDeclare(defaultExchangeName, "topic", true)
                channel.queueBind(defaultQueueName, defaultExchangeName, "default.user")
                channel.basicConsume(defaultQueueName, true, "user", object: DefaultConsumer(channel){
                    override fun handleDelivery(
                        consumerTag: String?,
                        envelope: Envelope?,
                        properties: AMQP.BasicProperties?,
                        body: ByteArray?
                    ) {
                        super.handleDelivery(consumerTag, envelope, properties, body)
                        val message = String(body!!, Charsets.UTF_8)
                        UiUtils.createSimpleNotification(ActivityManager.getCurActivity() as BaseActivity,
                                                        "tip:", message)
                    }
                })
                channel.basicQos(1)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: TimeoutException) {
                e.printStackTrace()
            }
        }
    }
}
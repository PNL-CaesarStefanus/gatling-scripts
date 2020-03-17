import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.mqtt.Predef._

class MqttSample {

  private val mqttConf = mqtt
    .broker("172.31.20.201", 3700)
    .clientId("test")
    .qosAtLeastOnce
    // .correlateBy(jsonPath("$.correlationId"))

  private val scn = scenario("MQTT Test")
    .feed(csv("topics-and-payloads.csv"))
    .exec(mqtt("Connecting").connect)
    // .exec(mqtt("Subscribing").subscribe("${myTopic}"))
    .exec(mqtt("Publishing").publish("proxyPoC").message(StringBody("MUDA"))
      .expect(100 milliseconds).check(jsonPath("$.error").notExists))

  setUp(scn.inject(atOnceUsers(10))
    .protocols(mqttConf)
}
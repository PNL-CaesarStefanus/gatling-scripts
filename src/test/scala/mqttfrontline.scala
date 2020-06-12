package frontline.mqttsample

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.mqtt.Predef._
import io.gatling.http.Predef._

class MqttSample extends Simulation {

  val mqttConf = mqtt
    .mqttVersion_3_1
    .broker("tcp://MQTT-balancer-91305777aa64bc6f.elb.ap-northeast-1.amazonaws.com", 3700)
    //.broker("172.31.32.99", 3700)
    //.broker("localhost", 9999)
    .clientId("test")
    //.qosAtLeastOnce
    // .correlateBy(jsonPath("$.correlationId"))

  private val scn = scenario("MQTT Test")
    // .feed(csv("topics-and-payloads.csv"))
    .exec(mqtt("Connecting").connect)
    // .exec(mqtt("Subscribing").subscribe("${myTopic}"))
    .during(10 seconds){
        pace(0.1 second)
        .exec(mqtt("Publishing").publish("proxyPoC").message(StringBody("MUDA"))
      .expect(100 milliseconds).check(jsonPath("$.error").notExists))
    }
    

  //setUp(scn.inject(atOnceUsers(10))
  //).protocols(mqttConf)
  setUp(scn.inject(rampUsersPerSec(10) to 1000 during (2 minutes)))
    .protocols(mqttConf)
}
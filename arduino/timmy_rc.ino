#include <ESP8266WiFi.h>
#include <WiFiUdp.h>

const char* ssid = "Timmy";  // Enter SSID here
const char* password = "timmyrulz";  //Enter Password here

IPAddress local_ip(192,168,1,1);
IPAddress gateway(192,168,1,1);
IPAddress subnet(255,255,255,0);

WiFiUDP Udp;
unsigned int localUdpPort = 1234;
byte incomingPacket[4];

void setup(){
  WiFi.softAP(ssid, password);
  WiFi.softAPConfig(local_ip, gateway, subnet);

  Udp.begin(localUdpPort);
  pinMode(D1, OUTPUT);
  pinMode(D2, OUTPUT);
  pinMode(D3, OUTPUT);
  pinMode(D4, OUTPUT);

  // PWM
  pinMode(D5, OUTPUT);
  pinMode(D6, OUTPUT);
}

void loop()
{
  int packetSize = Udp.parsePacket();
  Serial.println(packetSize);
  if (packetSize)
  {
    int len = Udp.read(incomingPacket, 100);
    if (len > 0)
    { 
      if (incomingPacket[0] > 0) {
        analogWrite(D5, incomingPacket[0] * 4);
        digitalWrite(D1, HIGH);
      } else if (incomingPacket[0] == 0) {
        digitalWrite(D1, LOW);
      }
      
      if (incomingPacket[1] > 0) {
        analogWrite(D5, incomingPacket[1] * 4);
        digitalWrite(D2, HIGH);
      } else if (incomingPacket[1] == 0) {
        digitalWrite(D2, LOW);
      }

      if (incomingPacket[2] > 0) {
        digitalWrite(D3, HIGH);
        analogWrite(D6, incomingPacket[2] * 4);
      } else if (incomingPacket[2] == 0) {
        digitalWrite(D3, LOW);
      }
      if (incomingPacket[3] > 0) {
        digitalWrite(D4, HIGH);
        analogWrite(D6, incomingPacket[3] * 4);
      } else if (incomingPacket[3] == 0) {
        digitalWrite(D4, LOW);
      }
      Udp.flush();
    }
    
  }
}

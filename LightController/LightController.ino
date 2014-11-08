#include <SPI.h>         // needed for Arduino versions later than 0018
#include <Ethernet.h>
#include <EthernetUdp.h>
#define UDP_TX_PACKET_MAX_SIZE 80 // Current JSON is 58 - 60...
#include <G35String.h>
#include <JsonParser.h>
using namespace ArduinoJson::Parser;


// Total # of lights on string (usually 50, or 25). Maximum is 63, because
// the protocol uses 6-bit addressing and bulb #63 is reserved for broadcast
// messages.
#define LIGHT_COUNT (25)
#define LIGHT_COUNT_LARGE (50)

// Arduino pin number. Pins used by ethernet sheild 10, 11, 12, 13, 50, 51, 52, 53
#define G35_PIN_1 (48)
#define G35_PIN_2 (46)
#define G35_PIN_3 (49)
#define G35_PIN_4 (47)
#define G35_PIN_5 (45)
#define G35_PIN_6 (43)
#define G35_PIN_7 (41)
#define G35_PIN_8 (39)
#define G35_PIN_9 (37)
#define G35_PIN_10 (35)
#define G35_PIN_11 (33)
#define G35_PIN_12 (31)
#define G35_PIN_13 (29)

// Strings of lights
G35String* lights = NULL;
G35String lights_1(G35_PIN_1, LIGHT_COUNT);
G35String lights_2(G35_PIN_2, LIGHT_COUNT);
G35String lights_3(G35_PIN_3, LIGHT_COUNT);
G35String lights_4(G35_PIN_4, LIGHT_COUNT);
G35String lights_5(G35_PIN_5, LIGHT_COUNT);
G35String lights_6(G35_PIN_6, LIGHT_COUNT);
G35String lights_7(G35_PIN_7, LIGHT_COUNT);
G35String lights_8(G35_PIN_8, LIGHT_COUNT);
G35String lights_9(G35_PIN_9, LIGHT_COUNT);
G35String lights_10(G35_PIN_10, LIGHT_COUNT);
G35String lights_11(G35_PIN_11, LIGHT_COUNT);
G35String lights_12(G35_PIN_12, LIGHT_COUNT);
G35String lights_13(G35_PIN_13, LIGHT_COUNT_LARGE);

// Define JSON parser can handle 16 parts
JsonParser<16> parser;


// Enter a MAC address and IP address for your controller below.
byte mac[] = {
  0x90, 0xA2, 0xDA, 0x0E, 0xAF, 0x5D
};

unsigned int localPort = 8888;      // local port to listen on

// buffers for receiving and sending data
char packetBuffer[UDP_TX_PACKET_MAX_SIZE]; //buffer to hold incoming packet,
char  ReplyBuffer[] = "acknowledged";       // a string to send back

// An EthernetUDP instance to let us send and receive packets over UDP
EthernetUDP Udp;

void setup()
{

  // Start the Ethernet and UDP:
  Ethernet.begin(mac);
  Udp.begin(localPort);

  // Initialize lights
  delay(1000);
  lights_1.enumerate();
  delay(10);
  lights_2.enumerate();
  delay(10);
  lights_3.enumerate();
  delay(10);
  lights_4.enumerate();
  delay(10);
  lights_5.enumerate();
  delay(10);
  lights_6.enumerate();
  delay(10);
  lights_7.enumerate();
  delay(10);
  lights_8.enumerate();
  delay(10);
  lights_9.enumerate();
  delay(10);
  lights_10.enumerate();
  delay(10);
  lights_11.enumerate();
  delay(10);
  lights_12.enumerate();
  delay(10);
  lights_13.enumerate();

  //Create Serial Object
  Serial.begin(115200);
}




void loop()
{

  // What the JSON looks like {"string":1,"bulb":2,"r":3,"g":4,"b":5,"brightness":6}

  // if there's data available, read a packet
  int packetSize = Udp.parsePacket();
  if (packetSize)
  {
    Serial.print("Received packet of size: ");
    Serial.println(packetSize);

    // read the packet into packetBufffer
    Udp.read(packetBuffer, UDP_TX_PACKET_MAX_SIZE);

    JsonObject root = parser.parse(packetBuffer);
    if (!root.success())
    {
      error();
      return;
    }

    long lightStringIndex  = root["string"];
    long bulbIndex         = root["bulb"];
    long colorSelect       = root["color"];
    long r                 = root["r"];
    long g                 = root["g"];
    long b                 = root["b"];
    long brightness        = root["brightness"];

    // Get the String
    setString(lightStringIndex);

    if (brightness > G35::MAX_INTENSITY) {
      brightness == G35::MAX_INTENSITY;
    }
    if (brightness < 0) {
      brightness = 0;
    }
    // Apply Light settings
    lights->set_color_if_in_range(bulbIndex, brightness, getColor(colorSelect, r, g, b));

    //send a reply, to the IP address and port that sent us the packet we received
    Udp.beginPacket(Udp.remoteIP(), Udp.remotePort());
    Udp.write(ReplyBuffer);
    Udp.endPacket();
  }
}

color_t getColor(long colorSelect, long r, long g, long b) {
  color_t color = COLOR(r, g, b);
  if (colorSelect == 1) {
    color = COLOR_WHITE;
  } else if (colorSelect == 2) {
    color = COLOR_RED;
  } else if (colorSelect == 3) {
    color = COLOR_GREEN;
  } else if (colorSelect == 4) {
    color = COLOR_BLUE;
  } else if (colorSelect == 5) {
    color = COLOR_CYAN;
  } else if (colorSelect == 6) {
    color = COLOR_MAGENTA;
  } else if (colorSelect == 7) {
    color = COLOR_YELLOW;
  } else if (colorSelect == 8) {
    color = COLOR_PURPLE;
  } else if (colorSelect == 9) {
    color = COLOR_ORANGE;
  } else if (colorSelect == 10) {
    color = COLOR_PALE_ORANGE;
  } else if (colorSelect == 11) {
    color = COLOR_WARMWHITE;
  } else if (colorSelect == 12) {
    color = COLOR_INDIGO;
  } else if (colorSelect == 13) {
    color = COLOR_VIOLET;
  } else if (colorSelect == 14) {
    color = COLOR_BLACK;
  }
  return color;
}

void setString(long lightStringIndex) {
  if (lightStringIndex == 1) {
    lights = &lights_1;
  } else if (lightStringIndex == 2) {
    lights = &lights_2;
  } else if (lightStringIndex == 3) {
    lights = &lights_3;
  } else if (lightStringIndex == 4) {
    lights = &lights_4;
  } else if (lightStringIndex == 5) {
    lights = &lights_5;
  } else if (lightStringIndex == 6) {
    lights = &lights_6;
  } else if (lightStringIndex == 7) {
    lights = &lights_7;
  } else if (lightStringIndex == 8) {
    lights = &lights_8;
  } else if (lightStringIndex == 9) {
    lights = &lights_9;
  } else if (lightStringIndex == 10) {
    lights = &lights_10;
  } else if (lightStringIndex == 11) {
    lights = &lights_11;
  } else if (lightStringIndex == 12) {
    lights = &lights_12;
  } else if (lightStringIndex == 13) {
    lights = &lights_13;
  }
}

void error() {
  Serial.println("JsonParser.parse() failed");
  lights = &lights_13;
  lights->fill_color(0, lights->get_light_count(), G35::MAX_INTENSITY, COLOR_WARMWHITE);
  delay(100);
  lights->fill_color(0, lights->get_light_count(), G35::MAX_INTENSITY, COLOR_RED);
  delay(100);
  lights->fill_color(0, lights->get_light_count(), G35::MAX_INTENSITY, COLOR_WARMWHITE);
  delay(100);
  lights->fill_color(0, lights->get_light_count(), G35::MAX_INTENSITY, COLOR_RED);
  delay(100);
}

#include <SPI.h>         // needed for Arduino versions later than 0018
#include <Ethernet.h>
#include <EthernetUdp.h>
#define UDP_TX_PACKET_MAX_SIZE 1500 // Current light command is 8
#include <G35String.h>


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

#define NUMBER_OF_STRANDS (13)

// Commands
#define SINGLE_LIGHT_COMMAND  (0)
#define MULTI_LIGHT_COMMAND   (1)
#define FADE_ALL_COMMAND      (2)
#define FILL_COMMAND          (3)


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

// Enter a MAC address and IP address for your controller below.
byte mac[] = {
  //0x90, 0xA2, 0xDA, 0x0E, 0xAF, 0x5D // Live system
  0x90, 0xA2, 0xDA, 0x0E, 0xAF, 0x5E // Test System
};

unsigned int localPort = 8888;      // local port to listen on

// buffers for receiving and sending data
char packetBuffer[UDP_TX_PACKET_MAX_SIZE]; //buffer to hold incoming packet,
char  ReplyBuffer[] = "OK";       // a string to send back
char  ReplyError[] = "ERR";       // a string to send back

// An EthernetUDP instance to let us send and receive packets over UDP
EthernetUDP Udp;

void setup()
{
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
  Serial.begin(9600);
  Serial.println("Serial has begun");

  // Start the Ethernet and UDP:
  Ethernet.begin(mac);
  Udp.begin(localPort);

  
  // print your local IP address:
  Serial.print("My IP address: ");
  Serial.println(Ethernet.localIP());
  
  resetPacketBuffer();
  //Serial.println(F("We are good to go!"));
}

void resetPacketBuffer() {
  memset(packetBuffer, 0, UDP_TX_PACKET_MAX_SIZE);
}

// 0 = commnad, 1 = size, 2 = string, 3 = bulb...
void processSingleLightCommand(int offset) {
  ////Serial.print(F("Offset: "));
  ////Serial.println(offset);
  long lightStringIndex  = (int)packetBuffer[2+offset] & 0xFF;
  long bulbIndex         = (int)packetBuffer[3+offset] & 0xFF;
  long colorSelect       = (int)packetBuffer[4+offset] & 0xFF;
  long r                 = (int)packetBuffer[5+offset] & 0xFF;
  long g                 = (int)packetBuffer[6+offset] & 0xFF;
  long b                 = (int)packetBuffer[7+offset] & 0xFF;
  long brightness        = (int)packetBuffer[8+offset] & 0xFF;

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
  //Serial.print(F("String: "));
  //Serial.print(lightStringIndex);
  //Serial.print(F(", Bulb: "));
  //Serial.print(bulbIndex);
  //Serial.print(F(", Color: "));
  //Serial.print(colorSelect);
  //Serial.print(F(", Intensity: "));
  //Serial.println(brightness);

}

void processMultiLightCommand() {
  long numberOfCommands  = (int)packetBuffer[1] & 0xFF;
  
  
  for (int i = 0; i < numberOfCommands; ++i)
  {
    processSingleLightCommand(i*7);
  }
}

void processFillCommand() {
  
 Serial.println(F("processFillCommand "));
  long lightStringIndex  = (int)packetBuffer[1] & 0xFF;
  long colorSelect       = (int)packetBuffer[2] & 0xFF;
  long r                 = (int)packetBuffer[3] & 0xFF;
  long g                 = (int)packetBuffer[4] & 0xFF;
  long b                 = (int)packetBuffer[5] & 0xFF;
  long brightness        = (int)packetBuffer[6] & 0xFF;

  if (brightness > G35::MAX_INTENSITY) {
    brightness == G35::MAX_INTENSITY;
  }
  if (brightness < 0) {
    brightness = 0;
  }
  
      Serial.print(F("lightStringIndex: "));
      Serial.println(lightStringIndex);      
      Serial.print(F("colorSelect: "));
      Serial.println(colorSelect);      
      Serial.print(F("r: "));
      Serial.println(r);      
      Serial.print(F("g: "));
      Serial.println(g);      
      Serial.print(F("b: "));
      Serial.println(b);
      Serial.print(F("brightness: "));
      Serial.println(brightness);
  
  if(lightStringIndex == 0 ){
    for(int strandIndex = 1; strandIndex <= NUMBER_OF_STRANDS; strandIndex++){
      // Get the String
      setString(strandIndex);
      // Apply Light settings
      lights->fill_color(0, lights->get_light_count(), brightness, getColor(colorSelect, r, g, b));
    }
  } else {
    // Get the String
    setString(lightStringIndex);
    // Apply Light settings
    lights->fill_color(0, lights->get_light_count(), brightness, getColor(colorSelect, r, g, b));
  }
}

void error() {
  //Serial.println(F("Unknown COMMAND failed"));
  lights = &lights_13;
  lights->fill_color(0, lights->get_light_count(), G35::MAX_INTENSITY, COLOR_WARMWHITE);
  delay(100);
  lights->fill_color(0, lights->get_light_count(), G35::MAX_INTENSITY, COLOR_RED);
  delay(100);
  lights->fill_color(0, lights->get_light_count(), G35::MAX_INTENSITY, COLOR_WARMWHITE);
  delay(100);
  lights->fill_color(0, lights->get_light_count(), G35::MAX_INTENSITY, COLOR_RED);
  delay(100);

  Udp.beginPacket(Udp.remoteIP(), Udp.remotePort());
  Udp.write(ReplyError);
  Udp.endPacket();
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

void allOffFade(long fadeDelay) {
  ////Serial.print("Fade Delay: ");
  ////Serial.println(fadeDelay);
  if (fadeDelay <= 0) {
    lights_1.broadcast_intensity(0);
    lights_2.broadcast_intensity(0);
    lights_3.broadcast_intensity(0);
    lights_4.broadcast_intensity(0);
    lights_5.broadcast_intensity(0);
    lights_6.broadcast_intensity(0);
    lights_7.broadcast_intensity(0);
    lights_8.broadcast_intensity(0);
    lights_9.broadcast_intensity(0);
    lights_10.broadcast_intensity(0);
    lights_11.broadcast_intensity(0);
    lights_12.broadcast_intensity(0);
    lights_13.broadcast_intensity(0);
  } else {
    for (int i = G35::MAX_INTENSITY; i >= 0; i--) {
      lights_1.broadcast_intensity(i);
      lights_2.broadcast_intensity(i);
      lights_3.broadcast_intensity(i);
      lights_4.broadcast_intensity(i);
      lights_5.broadcast_intensity(i);
      lights_6.broadcast_intensity(i);
      lights_7.broadcast_intensity(i);
      lights_8.broadcast_intensity(i);
      lights_9.broadcast_intensity(i);
      lights_10.broadcast_intensity(i);
      lights_11.broadcast_intensity(i);
      lights_12.broadcast_intensity(i);
      lights_13.broadcast_intensity(i);
      delay(fadeDelay);
    }
  }
}


void loop()
{

  // if there's data available, read a packet
  int packetSize = Udp.parsePacket();
  if (packetSize)
  {
    //Serial.print(F("Received packet of size: "));
    //Serial.println(packetSize);

    // read the packet into packetBufffer
    Udp.read(packetBuffer, UDP_TX_PACKET_MAX_SIZE);
//    //Serial.println(F("Data: "));
//    //Serial.println((int)packetBuffer[0] & 0xFF);
//    //Serial.println((int)packetBuffer[1] & 0xFF);
//    //Serial.println((int)packetBuffer[2] & 0xFF);
//    //Serial.println((int)packetBuffer[3] & 0xFF);
//    //Serial.println((int)packetBuffer[4] & 0xFF);
//    //Serial.println((int)packetBuffer[5] & 0xFF);
//    //Serial.println((int)packetBuffer[6] & 0xFF);

    int command  = (int)packetBuffer[0] & 0xFF;
    //Serial.print(F("Got command: "));
    //Serial.println(command);
    if (command==SINGLE_LIGHT_COMMAND) {
      processSingleLightCommand(0);
    } else if (command==MULTI_LIGHT_COMMAND) {
      processMultiLightCommand();
    } else if (command==FADE_ALL_COMMAND) {
      allOffFade(packetBuffer[1] & 0xFF);
    }  else if (command==FILL_COMMAND) {
      processFillCommand();
    } else {
      Serial.print(F("Unknown Command: "));
      Serial.println(command);
      error();
    }

    //send a reply, to the IP address and port that sent us the packet we received
    Udp.beginPacket(Udp.remoteIP(), Udp.remotePort());
    Udp.write(ReplyBuffer);
    Udp.endPacket();
    resetPacketBuffer();
  }

}

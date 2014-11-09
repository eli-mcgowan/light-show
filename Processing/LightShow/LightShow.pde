import java.util.StringTokenizer;
import hypermedia.net.*;

// UDP receive values
int PORT_RX = 7654;
String HOST_IP = "localhost";//IP Address of the PC in which this App is running
UDP udp;//Create UDP object for recieving
String lastValue;
String UDP_ACK = "acknowledged";

int[] numLightsPerStrand;
LightStrand[] lightStrands;
int rowHeight;
int rowWidth;
int lightsXOffset;
int lightsYOffset;
int lightBaseWidth;
int numStrands;
int maxBulbs;
String typing;
String lastCommand;
String instructions;
PFont f;

void setup(){
 size(1550, 750);
 
 udp= new UDP(this, PORT_RX, HOST_IP);
 udp.log(true);
 udp.listen(true);
 lastValue = "";
 
 numLightsPerStrand = new int[]{25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 50};
 lightStrands = new LightStrand[numLightsPerStrand.length];
 for(int i=0; i<numLightsPerStrand.length; i++){
   lightStrands[i] = new LightStrand(numLightsPerStrand[i]);
 }
 
 rowHeight = 50;
 rowWidth = 30;
 lightsYOffset = 20;
 lightsXOffset = 15;
 lightBaseWidth = 10;
 numStrands = 2;
 typing = "";
 lastCommand = "";
 instructions = "Setup: To set the number of strands and their size type 'setup' followed by the length of each strand (invalid entries will be set to 1). Example: setup 12 18 24";
 f = createFont("Arial",16,true); // Arial, 16 point, anti-aliasing on
 
 setMaxBulbs();
 
 //frameRate(2);
}

void draw(){
  background(0);
  
//  for(int i=0; i<lightStrands.length; i++){
//    shiftLights(lightStrands[i].lights);
//  }
  
  drawGrid();
  drawGridNumbers();
  
  for(int i=0; i<lightStrands.length; i++){
    drawLights(lightStrands[i].lights, lightsYOffset + (rowHeight * i));
  }
  
  
  fill(255);
  text(instructions,10,690);
  text("Current: " + typing,10,715);
  text("Last: " + lastCommand,10,735);
}

void receive(byte[] data, String HOST_IP, int PORT_RX){
  lastValue=new String(data);
  println(lastValue);
  JSONObject json = JSONObject.parse(lastValue);
 // json = loadJSONObject(lastValue);
  int stringNum = json.getInt("string");
  int bulbNum = json.getInt("bulb");
  int bulbColor = json.getInt("color");
  Light lightColor = getLight(bulbColor);
  //int red = json.getInt("r");
  //int green = json.getInt("g");
  //int blue = json.getInt("b");
  int brightness = json.getInt("brightness"); // TODO: currently ignored
  println(bulbColor);
  udp.send(UDP_ACK, HOST_IP, PORT_RX);
  updateLight(stringNum, bulbNum, lightColor);
}

void keyPressed() {
  // If the return key is pressed, save the String and clear it
  if (key == '\n' ) {
    lastCommand = typing;
    // A String can be cleared by setting it equal to ""
    typing = ""; 
    executeCommand(lastCommand);
  } else {
    // Otherwise, concatenate the String
    // Each character typed by the user is added to the end of the String variable.
    typing = typing + key; 
  }
}

/**
 * Parses (1, 2, 50, 75, 100) into:
 * 1 - light strand
 * 2 - light number
 * 50 - red value
 * 75 - green value
 * 100 - blue value
 */
void executeCommand(String command){
  StringTokenizer strings = new StringTokenizer(command, ", ");
  if(command.indexOf("setup") == 0){
    strings.nextToken(); // remove "setup"
    // count the number of tokens - this is our number of strands
    int numStrands = strings.countTokens();
    lightStrands = new LightStrand[numStrands];
    for(int i=0; i<numStrands; i++){
      try{
        lightStrands[i] = new LightStrand(Integer.parseInt(strings.nextToken()));
      } catch(NumberFormatException e){
        lightStrands[i] = new LightStrand(1);
      }
    }
    setMaxBulbs();
  }
}

void updateLight(int lightStrand, int lightNumber, Light lightColor){
   updateLight(lightStrand, lightNumber, lightColor.red, lightColor.green, lightColor.blue);
}

void updateLight(int lightStrand, int lightNumber, int red, int green, int blue){
  int lightStrandIndex = lightStrand-1;
   if(lightStrand <= 0 || lightStrand > lightStrands.length){
    setInvalidCommand("bad lightStrandIndex" + lightStrand);
    return;
  }
  if(lightNumber < 0 || lightNumber >= lightStrands[lightStrandIndex].lights.length){
    setInvalidCommand("bad lightIndex" + lightNumber);
    return;
  }
  lightStrands[lightStrandIndex].lights[lightNumber].red = red;
  lightStrands[lightStrandIndex].lights[lightNumber].green = green;
  lightStrands[lightStrandIndex].lights[lightNumber].blue = blue;
}

void setMaxBulbs(){
  maxBulbs = 0;
 for(int i=0; i<lightStrands.length; i++){
   if(lightStrands[i].lights.length > maxBulbs){
    maxBulbs =  lightStrands[i].lights.length;
   }
 }
}

void setInvalidCommand(String details){
        lastCommand = "Invalid command (" + details + ") - " + lastCommand;
}


void drawGrid(){
   fill(255, 255, 255, 50);
   // draw horizontal grid
  for(int i = 0; i<lightStrands.length; i++){
    if(i%2 == 1){
       rect(0, lightsYOffset + (i * rowHeight), (maxBulbs * rowWidth), rowHeight); 
    }
  }
   // draw vertical grid
  for(int i = 0; i<maxBulbs; i++){
    if(i%2 == 0){
       rect(5 + (i * rowWidth), 0, rowWidth, lightsYOffset + (lightStrands.length * rowHeight)); 
    }
  }
  
}

void drawGridNumbers(){
  fill(255);
  // draw vertical grid
  for(int i = 0; i<maxBulbs; i++){
    text("" + (i+1),15 + (i * rowWidth),10);
  }
}

void drawLights(Light[] lightArray, int yOffset){
  
  // light bases
  stroke(0);
  fill(0, 255, 0);
  for(int i=0; i<lightArray.length; i++){
    rect(lightsXOffset + (i*rowWidth), 30 + yOffset, lightBaseWidth, 10);
  }
  
  // string between lights
  ellipseMode(CORNER);
  noFill();
  stroke(0, 255, 0);
  for(int i=0; i<(lightArray.length - 1); i++){
    arc(lightsXOffset + (lightBaseWidth/2) + (i*rowWidth), 30 + yOffset, 30, 20, 0, PI, OPEN);
  }
  
  // lights
  stroke(0);
  ellipseMode(CENTER);
  for(int i=0; i<lightArray.length; i++){
    Light light = lightArray[i];
    fill(light.red, light.green, light.blue);
    ellipse(20 + (i*rowWidth), 20 + yOffset, 20, 30);
  }
}

void shiftLights(Light[] lightArray){
 Light first = lightArray[0];
  for(int i=0; i<(lightArray.length - 1); i++){
    lightArray[i] = lightArray[i+1];
  }
  lightArray[lightArray.length - 1] = first;
}

class LightStrand{
  Light[] lights;
  
  public LightStrand(int numLights){
    lights = new Light[numLights];
     for(int i=0; i<lights.length; i++){
       Light light = new Light();
     lights[i] = light;
     }
     init();
  }
  
  public void init(){
    for(int i=0; i<lights.length; i++){
      Light light = lights[i];
      // init will be all red, like g35 enumerate
      light.red = 255;
//     if(i%3 == 0){
//      light.red = 255;
//     }
//     if(i%3 == 1){
//      light.green = 255;
//     }
//     if(i%3 == 2){
//      light.blue = 255;
//     }
   }
  }
}

class Light{
 int red;
 int green;
 int blue;
 
 public Light(){}
 public Light(int red, int green, int blue){
   this.red = red;
   this.green = green;
   this.blue = blue;
 }
 
}

Light getLight(int arduinoColor){
   switch(arduinoColor){
    case 1: // COLOR_WHITE
      return new Light(255,255,255); 
    case 2: // COLOR_RED
      return new Light(255,0,0);
    case 3: // COLOR_GREEN
      return new Light(0,255,0);
    case 4: // COLOR_BLUE
      return new Light(0,0,255);
    case 5: // COLOR_CYAN
      return new Light(0,255,255);
    case 6: // COLOR_MAGENTA
      return new Light(255,0,255);
    case 7: // COLOR_YELLOW
      return new Light(255,255,0);
    case 8: // COLOR_PURPLE
      return new Light(128,0,128);
    case 9: // COLOR_ORANGE
      return new Light(255,140,0);
    case 10: // COLOR_PALE_ORANGE
      return new Light(255,180,0);
    case 11: // COLOR_WARMWHITE
      return new Light(255,244,229);
    case 12: // COLOR_INDIGO
      return new Light(75,0,130);
    case 13: // COLOR_VIOLET
      return new Light(238,130,238);
    case 14: // COLOR_BLACK
      return new Light(0,0,0);
   }
   // COLOR_BLACK
   return new Light(0,0,0);
 }


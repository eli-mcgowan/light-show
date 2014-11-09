import hypermedia.net.*;

int PORT_RX = 7654;
String HOST_IP = "localhost";//IP Address of the PC in which this App is running
UDP udp;//Create UDP object for recieving
String lastValue;

void setup(){
  size(500, 200);
  udp= new UDP(this, PORT_RX, HOST_IP);
  udp.log(true);
  udp.listen(true);
  lastValue = "";
}

void draw(){
  background(255);
  fill(0);
  text(HOST_IP + ":" + PORT_RX, 10, 10);
  text("X: " + lastValue, 10, 40);
}

void receive(byte[] data, String HOST_IP, int PORT_RX){
  lastValue=new String(data);
  println(lastValue);
}

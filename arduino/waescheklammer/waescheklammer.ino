#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <dht11.h>


dht11 DHT11;

//ACHTUNG Pinbelegung von Arduino und NodeMCU unterschiedlich   DHT11PIN 2
#define DHT11PIN 2

void setup()
{
  Serial.begin(115200);
  Serial.println();

  WiFi.begin("Jugend hackt", "");

  Serial.print("Connecting");
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }
  Serial.println();

  Serial.print("Connected, IP address: ");
  Serial.println(WiFi.localIP());
}

void loop() {
 
 if(WiFi.status()== WL_CONNECTED){   //Check WiFi connection status

   int chk = DHT11.read(DHT11PIN);

  Serial.print("Read sensor: ");
  switch (chk)
  {
    case DHTLIB_OK: 
    Serial.println("OK"); 
    break;
    case DHTLIB_ERROR_CHECKSUM: 
    Serial.println("Checksum error"); 
    break;
    case DHTLIB_ERROR_TIMEOUT: 
    Serial.println("Time out error"); 
    break;
    default: 
    Serial.println("Unknown error"); 
    break;
  }

  int humidity = (float)DHT11.humidity;
  Serial.print("Humidity (%): ");
  Serial.println(humidity);


  int temperature = (float)DHT11.temperature;
  Serial.print("Temperature (°C): ");
  Serial.println(temperature);

  delay(2000);

  
 
   HTTPClient http;    

   //Specify request destination -> in this case a ifttt adress / REST-API wich accepts POST-request
   http.begin("http://maker.ifttt.com/trigger/default/with/key/knv6299hNGG7fF53lqdCuXFt2VjjwlFr7_MJ1s2Xm9L");      
   http.addHeader("Content-Type", "application/x-www-form-urlencoded");  //Specify content-type header
 
    int httpCode = http.POST("value1=" + String(humidity) + "&value2=" + String(temperature) + "&value3=1111");   //Send the request
   String payload = http.getString();                  //Get the response payload
 
   Serial.println(httpCode);   //Print HTTP return code
   Serial.println(payload);    //Print request response payload

   http.end();  //Close connection

   //delay(30000);  //Send a request every 30 seconds -> normal mode
   delay(10000);  //Send a request every 10 seconds -> presentation mode
   
 }else{
 
    Serial.println("Error in WiFi connection");   
 
 }
 
  

 
}

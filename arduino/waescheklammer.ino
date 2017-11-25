#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>

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
 
   HTTPClient http;    //Declare object of class HTTPClient

   //Specify request destination -> in this case a ifttt adress / REST-API wich accepts POST-request
   http.begin("http://maker.ifttt.com/trigger/test/with/key/dc4KqLQpR4jM3lk99hc-if");      
   http.addHeader("Content-Type", "application/x-www-form-urlencoded");  //Specify content-type header
 
   int httpCode = http.POST("title=medl&body=-10&temp=20");   //Send the request
   String payload = http.getString();                  //Get the response payload
 
   Serial.println(httpCode);   //Print HTTP return code
   Serial.println(payload);    //Print request response payload
 
   http.end();  //Close connection
 
 }else{
 
    Serial.println("Error in WiFi connection");   
 
 }
 
  delay(30000);  //Send a request every 30 seconds
 
}

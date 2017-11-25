
#include <dht11.h>

dht11 DHT11;

#define DHT11PIN 2

void setup()
{
  Serial.begin(115200);
}

void loop()
{
  Serial.println("\n");

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
  
 

  Serial.print("Temperature (°C): ");
  Serial.println((float)DHT11.temperature);

  delay(2000);
}

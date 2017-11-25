# {Wäsche} - die intelligente Wäscheklammer


Was ist {Wäsche}?
-----
-----
Jedes kennt das Problem man hängt nasse Wäsche zum trocknen auf und kann nicht abschätzen wann diese getrocknet ist. Leider kommt es oft vor, dass man diese aus den Augen verliert. {Wäsche} hilft dabei. Durch die intelligente Vernetzung der Wäscheklammer bestückt mit Sensoren und Microcontroller wird der Trockenvorgang der Wäsche überwacht. Außerdem wird man benachrichtigt, wenn die Wäsche getrocknet ist.

Wie funktioniert {Wäsche}?
---
---
Die Hardware von {Wäsche} basiert auf einem ESP 8266 Microcontroller incl. WLAN (In diesem Fall wurde ein kombinierter [Wemos D1 Mini](https://wiki.wemos.cc/products:d1:d1_mini "Wemos D1 Mini") verwendet).
Zusätzlich wurde ein Luftfeuchtigkeitssensor incl. Thermometer  [DHT11](https://funduino.de/anleitung-dht11-dht22 "DHT 11") verbaut, der regelmäßig die Temperatur und den Luftdruck an den ESP weitergibt.
Der Arduino Sketch für ESP und Sensoren ist in /arduino/waescheklammer zu finden.
Um die Software nutzen zu können, müssen die passenden [Treiber](https://wiki.wemos.cc/products:d1:d1_mini "Treiber") für den ESP installiert werden.
Standartmäßig sendet der ESP per HTTP post request die Temperatur und die Luftfeuchtigkeit alle 30 Sekunden an einen festgelegten Server. Im vorliegendem Fall werden alle Daten an den IOT-Cloudprovider [IFTTT](https://ifttt.com/ "ifttt") gesendet. Dieser kann auch gegen jeglichen anderen passenden Server getauscht werden. Die zu übertragenden Variablen können ebenfalls nachträglich angepasst werden.

Die Prototype 3D-Druckdateien können ebenfalls heruntergeladen werden.

Die Website dieses [Jugend Hackt](https://github.com/Jugendhackt "jh") Projektes ist [hier](https://github.com/Jugendhackt/waescheklammer-web "hier") zu finden.
Außerdem kann der aktuelle Projektstatus auf [Hackdash](https://hackdash.org/projects/5a19365b87d0970a0e0a4066 "hackdash") nachverfolgt werden.



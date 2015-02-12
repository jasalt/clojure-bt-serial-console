/* Quick serial data mocking sketch.
   From Serial Call and Response Example
   http://www.arduino.cc/en/Tutorial/SerialCallResponse
 
   Sends something like:
   <S:240:134:498>
 */

void setup()
{
  Serial.begin(9600);
  establishContact();  // send a byte to establish contact until receiver responds
}

void loop()
{
  /*  */
}

void establishContact() {
  while (Serial.available() <= 0) {
    Serial.write("<S:");
    Serial.write(random(100, 999));
    Serial.write(":");
    Serial.write(random(100, 999));
    Serial.write(":");
    Serial.write(random(100, 999));
    Serial.println(">");
    delay(random(20,100));
  }
}

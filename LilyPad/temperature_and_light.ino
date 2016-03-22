#define aref_voltage 4.08
// This is a reference voltage for your power supply: as 4.08
//but measure it with a multimeter when running and add in the correct voltage.

// 'int' is short for 'integer' = whole number


//input pin for the light sensor to get readings
int sensorpinlight= A3;

//input pin for temperature sensor to get the readings

int tempPin = 18;

//the resolution is 10 mV / degree centigrade with a
//500 mV offset to allow for negative temperatures
int tempReading;        // the analog reading from the sensor


int lightvalue;


int positivePin = 19;
//setting 19 to be a positive pin.

int negativelight=10;
//setitng 10 to be negative pin for the sensor

void setup(void) {
// To send debugging information via the Serial monitor
Serial.begin(9600);
pinMode(sensorpinlight, INPUT);

  // Set pin A5 to use as a power pin for the light sensor
  // If using the LilyPad Development Board, comment out these lines of code
  pinMode(A2, OUTPUT);
  pinMode(A2, HIGH);



pinMode(negativelight,OUTPUT);


pinMode(positivePin, OUTPUT);
    //digitalwrite is 'set the value of this pin to be .... LOW = minus , HIGH = positive'
    //digitalwrite is 'set the value of this pin to be ....'
digitalWrite(positivePin, HIGH);  //digitalwrite is 'set the value of 19 to be positive
digitalWrite(negativelight,LOW);
}

void loop(void) {

tempReading = analogRead(tempPin);  //Get a temperaure reading from the temp sensor

Serial.print("Temp reading = "); // allow the temp reading to appear in debugging > Serial Monitor
Serial.print(tempReading);     // the raw analog reading

// converting that reading to voltage, which is based off the reference voltage
float voltage = tempReading * aref_voltage;
voltage /= 1024.0;

// print out the voltage
Serial.print(" - ");
Serial.print(voltage);
Serial.println(" volts");

// now print out the temperature
float temperatureC = (voltage - 0.5) * 100 ;  //converting from 10 mv per degree with 500 mV offset
//to degrees ((voltage - 500mV) times 100)
Serial.print(temperatureC);
Serial.println(" degrees C");

// now convert to Fahrenheight
float temperatureF = (temperatureC * 9.0 / 5.0) + 32.0;
Serial.print(temperatureF);
Serial.println(" degrees F");

lightvalue = analogRead(sensorpinlight);

   // Print some descriptive text and then the value from the sensor
     Serial.print("Light value is:");
    Serial.println(lightvalue);

//for setting delay to print the next reading 
delay(200);



}

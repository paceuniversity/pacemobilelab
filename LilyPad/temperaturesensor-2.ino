// This is a reference voltage determined with a multimeter
#define aref_voltage 3.78

// Pin connections
// S - 18
// + - 19
// - - -

// Setting up the pins

//pin to get sensor reading
int tempPin = 18;

//pin for positive output to the sensor
int positivePin = 19;

// Voltage of sensor

int voltage;
// Raw temperature read from pin S / 18
int tempReading;
// Temperature in degrees Celcius
float temperatureC;
// Temperature in degrees Fahrenheit
float temperatureF;

void setup(void) {
  // To send debugging information via the serial monitor
  Serial.begin(9600);
  // Initialization of the pins
  pinMode(tempPin,INPUT);
  pinMode(positivePin, OUTPUT);
  // The value that is read is positive - LOW = minus , HIGH = positive
  digitalWrite(positivePin, HIGH);
}

void loop(void) {
  // Get the temperature reading from the temperature sensor
  tempReading = analogRead(tempPin);

  // Debugging raw analog information available in the Serial monitor
  Serial.print("Temp reading = ");
  Serial.print(tempReading);
  Serial.println(“”);

  // Converting that reading to voltage, which is based on the reference voltage
  float voltage = tempReading * aref_voltage;
  voltage /= 1024.0;

  Serial.print(" - ");
  Serial.print(voltage);
  Serial.println(" volts");

  // Converting from 10 mv per degree with 500 mV offset to degrees Celsius ((voltage - 500mV) times 100)
  temperatureC = (voltage - 0.5) * 100 ;
  Serial.print(temperatureC);
  Serial.println(" degrees Celsius");

  // To degrees Fahrenheit
  temperatureF = (temperatureC * 9.0 / 5.0) + 32.0;
  Serial.print(temperatureF);
  Serial.println(" degrees Fahrenheit");
  delay(1000);
}

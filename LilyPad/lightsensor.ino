// Pin connections
// S - 18
// + - 19
// - - -

// Setting up the pins

//pin to read sensor value
int lightPin = 18;
//positive pin for the sensor
int positivePin = 19;

// Value of the luminosity from the sensor
int lightValue;

void setup(void) {
  // To send debugging information via the serial monitor
  Serial.begin(9600);
  // Initialization of the pins
  pinMode(lightPin,INPUT);
  pinMode(positivePin, OUTPUT);
  // The value that is read is positive - LOW = minus , HIGH = positive
  digitalWrite(positivePin, HIGH);
}

void loop(void) {
  // Reading of the raw light value
  lightValue = analogRead(lightPin);
  // Print some descriptive text and then the value from the sensor
  Serial.print("Light value is:");
  Serial.println(lightValue);
}

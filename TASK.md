# Task: Long Arithmetic

> You are a hacker trying to break the RSA encryption algorithm. There is a problem though. Modern RSA keys are usually 2048 or 4096 bits long. There is no way these huge numbers would fit into friendly-neighborhood long long variables. So your task is to implement a set of class(es) that expand integer capabilities by allowing one to work with arbitrary-long numbers.

### Task
* Implement a set of BigInt class(es) that will expand integer capabilities and work with arbitrary-long numbers.
* Support basic comparison operations: “<, <=, >, >=, ==”.
* Support basic arithmetic operations: “+, -, *, /, %, **”.
* Support conversion “int -> BigInt”, “BigInt -> int”, “BigInt -> string”, “string -> BigInt”.
* As a bonus: implement the Karatsuba multiplication algorithm for numbers under 100_000 digits long and the Fast Fourier Transform (FFT) for the longer ones.

### What to hand in
* The link to the Github repo where the code is published.
* The set of “unit tests” that demonstrate the implemented functionality.
* The README.md file should contain the information on how to compile and run the program.

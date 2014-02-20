set terminal png
set xlabel "Time (Distance)"
set ylabel "Sound Value (?)"
set title "Sound"

# Exercise 1
set output "w4.ex1.testsample.png"
plot "../samples/w4.ex1.test.sample.sound.s" using 1:2 w l title "Just fun test sample, does it work"

set output "w4.ex1.knips.png"
plot "../samples/w4.ex1.knips.s" using 1:2 w l title "Knips"

set output "w4.ex1.mobil.png"
plot "../samples/w4.ex1.mobil.s" using 1:2 w l title "With mobile"

set output "w4.ex1.klap.png"
plot "../samples/w4.ex1.klap.s" using 1:2 w l title "Klap"

set output "w4.klaptest.png"
set xrange [0:500] 
plot "../samples/w4.klaptest.s" using ($1-6000):2 w lp title "Sample of one clap"

set output "w4.ex1.hej.png"
set xrange [0:17000] 
plot "../samples/w4.ex1.hej.s" using 1:2 w l title "Saying 'Hej'"



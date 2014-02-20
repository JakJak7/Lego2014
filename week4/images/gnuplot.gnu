set terminal png
set xlabel "Time (Distance)"
set ylabel "Sound Value (?)"
set title "Sound"

# Exercise 1
set output "Exercise1_just_random_test.png"
plot "../samples/ex1_test_sample_sound.s" using 1:2 w l

set output "w4.ex1.fixp.s1.png"
plot "../samples/w4.ex1.knips.s" using 1:2 w l

set output "w4.ex1.fixp.s2.png"
plot "../samples/w4.ex1.mobil.s" using 1:2 w l

set output "w4.ex1.klap.png"
plot "../samples/w4.ex1.klap.s" using 1:2 w l

set output "w4.ex1.hej.png"
set xrange [0:17000]
plot "../samples/w4.ex1.hej.s" using 1:2 w l


set terminal png
set xlabel "Time (Distance)"
set ylabel "Sound Value (?)"
set title "Sound"

# Exercise 1
set output "Exercise1_just_random_test.png"
plot "../samples/ex1_test_sample_sound.s" using 1:2 w l

set output "w4.ex1.fixp.s1.png"
plot "../samples/w4.ex1.fixp1.s" using 1:2 w l

set output "w4.ex1.fixp.s2.png"
plot "../samples/w4.ex1.fixp2.s" using 1:2 w l


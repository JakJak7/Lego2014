set terminal png
set xlabel "Time in sec"
set ylabel "Distanceincm"
set title "Oscillering"

set output "Exercise4_original.png"
plot "../sample/Exercise4_original.txt" using 1:2  with lines

set output "Exercise3_100ms.png"
plot "../sample/Sample_100ms.txt" using 1:2 with lines

set output "Exercise5.png"
plot "../sample/Exercise5.txt" using 1:2 with lines

set output "Exercise5_v2.png"
plot "../sample/Exercise5_v2.txt" using 1:2 with lines

set output "Exercise4_tweaked.png"
plot "../sample/Exercise4_tweaked.txt" using 1:2 with lines

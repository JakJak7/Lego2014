set terminal png
set xlabel "Time (sec)"
set ylabel "Distance (cm)"
set title "Oscillering"

#set yrange [0:0]
#set xrange [0:10]
set xtics 1

set output "Exercise4_original.png"
plot "../sample/Exercise4_original.txt" using ($1/1000):2  w lp

set output "Exercise3_100ms.png"
plot "../sample/Sample_100ms.txt" using ($1/1000):2 w lp

set output "Exercise5.png"
plot "../sample/Exercise5.txt" using ($1/1000):2 w lp

set output "Exercise5_v2.png"
plot "../sample/Exercise5_v2.txt" using ($1/1000):2 w lp

set output "Exercise4_tweaked.png"
plot "../sample/Exercise4_tweaked.txt" using ($1/1000):2 w lp


set output "Exercise6_nqc.png"
f(x) = mean
fit f(x) '../sample/Exercise6_nqc.txt' u 1:2 via mean

stddev_y = sqrt(FIT_WSSR / (FIT_NDF + 1 ))
set xrange [0:10]
set label 1 gprintf("Mean = %g", mean) at 4, mean+5
set label 2 gprintf("Standard deviation = %g", stddev_y) at 1,15
plot "../sample/Exercise6_nqc.txt" using ($1/1000):2 w lp title "Exercise6_nqc", \
mean lt 1 lc rgb "#bbbbdd" title "Mean"

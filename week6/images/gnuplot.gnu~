set terminal png
set xlabel "Time (sec)"
set ylabel "Distance (cm)"
set title "Oscillering"
set xtics 1


set output "Exercise4_original.png"
set xrange [0:10]
set yrange [20:60]
plot "../sample/Exercise4_original.txt" using ($1/1000):2 w lp title "Exercise4 (orginal)"

set autoscale y
set autoscale x

set output "Exercise4_tweaked.png"
plot "../sample/Exercise4_tweaked.txt" using ($1/1000):2 w lp title "Exercise4 (tweaked)"


set output "Exercise5_v2.png"
#set yrange [34:36]
plot "../sample/Exercise5_v2.txt" using ($1/1000):2 w lp title "Exercise5 (v2)

set output "Exercise5.png"
plot "../sample/Exercise5.txt" using ($1/1000):2 w lp title "Exercise5"

set output "Exercise3_100ms.png"
f(x) = mean
fit f(x) '../sample/Sample_100ms.txt' u 1:2 via mean
stddev_y = sqrt(FIT_WSSR / (FIT_NDF + 1 ))
set xrange [0:10]
set label 1 gprintf("Mean = %g", mean) at 4, mean+5
set label 2 gprintf("Standard deviation = %g", stddev_y) at 1,15
plot "../sample/Sample_100ms.txt" using ($1/1000):2 w lp title "Exercise3 (100ms)", \
mean lt 1 lc rgb "#bbbbdd" title "Mean"

reset
set terminal png
set xlabel "Time (sec)"
set ylabel "Distance (cm)"
set title "Oscillering"
set xtics 1

set output "Exercise6_nqc.png"
f(x) = mean
fit f(x) '../sample/Exercise6_nqc.txt' u 1:2 via mean

stddev_y = sqrt(FIT_WSSR / (FIT_NDF + 1 ))
set xrange [0:10]
set label 1 gprintf("Mean = %g", mean) at 4, mean+5
set label 2 gprintf("Standard deviation = %g", stddev_y) at 1,15
plot "../sample/Exercise6_nqc.txt" using ($1/1000):2 w lp title "Exercise6_nqc", \
mean lt 1 lc rgb "#bbbbdd" title "Mean"

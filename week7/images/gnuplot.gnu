set terminal png
set xlabel "Time"
set ylabel "rawValue()"
set title "Jakob Elsker Justin Bieber"

set xrange [0:200000]
set output "sample.png"
f(x) = mean
fit f(x) '../sample/BabyBaby.dat' u 1:2 via mean
stddev_y = sqrt(FIT_WSSR / (FIT_NDF + 1 ))
set label 1 gprintf("Mean = %g", mean) at 0, 100
set label 2 gprintf("Standard deviation = %g", stddev_y) at 1,20
plot "../sample/BabyBaby.dat" using 1:2 w l title "BabyBaby"

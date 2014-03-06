set terminal png
set xlabel "Time (angle)"
set ylabel "Light intensitivity"
set title "Exercise 3 Light int / Angle"

set xrange [0:8000]
set yrange [120:620]
set output "sample.png"
plot "../sample/SampleL.txt" using 1:2 w l title "getNormalizedLightValue()" lc rgb "black", \
"../sample/SampleG.txt" using 1:2 w l title "getGreen()" lc rgb "green", \
"../sample/SampleB.txt" using 1:2 w l title "getBlue()" lc rgb "blue", \
"../sample/SampleR.txt" using 1:2 w l title "getRed()" lc rgb "red"


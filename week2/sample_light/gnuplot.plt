set terminal png
set xlabel "Time in sek"
set ylabel "Light %"
set title "Oscillering"
set yrange [30:70]
set xrange [0:10]
set xtics 1
set style func linespoints
set style line 1 lt 1 lw 1 pt 6 lc rgb "red"
set style line 2 lt 1 lw 1 pt 6 lc rgb "green"
set style line 3 lt 1 lw 1 pt 6 lc rgb "blue"
set style line 4 lt 1 lw 1 pt 6 lc rgb "purple"

set output "sampleall.png"
plot "Sample_10ms.txt" using ($1/1000):2 title "10 ms" w lp ls 1,\
 "Sample_100ms.txt" using ($1/1000):2 title "100 ms" w lp ls 2,\
 "Sample_500ms.txt" using ($1/1000):2 title "500 ms" w lp ls 3,\
 "Sample_1000ms.txt" using ($1/1000):2 title "1000 ms" w lp ls 4

set output "sample10.png"
plot "Sample_10ms.txt" using ($1/1000):2 title "10 ms" w lp ls 1

set output "sample100.png"
plot "Sample_100ms.txt" using ($1/1000):2 title "100 ms" w lp ls 2

set output "sample500.png"
plot "Sample_500ms.txt" using ($1/1000):2 title "500 ms" w lp ls 3

set output "sample1000.png"
plot "Sample_1000ms.txt" using ($1/1000):2 title "1000 ms" w lp ls 4

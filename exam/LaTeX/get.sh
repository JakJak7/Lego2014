#!usr/bin/bash

printf "Tjeking programs installed:"
hash git &> /dev/null
if [ $? -eq 1 ]; then
    echo >&2 "git not installed, do you want to install it?"
    sudo apt-get install git
fi
hash pandoc &> /dev/null
if [ $? -eq 1 ]; then
    echo >&2 "pandoc not installed, do you want to install it?"
    sudo apt-get install pandoc
fi
printf "\t\t Done\n"

printf "Henter markdown from github wiki:"
git clone https://github.com/JakJak7/Lego2014.wiki.git Markdown
printf "\t Done\n"

cd Markdown
printf "Converting from markdown to LaTeX"
FILES="WRO*"
i=1
for f in $FILES
do
pandoc -f markdown -t latex $f > $f.tex
((i++))
done
printf "\t\t Done\n"

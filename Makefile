all: run

run: run.c sample.o merge_sort.o
	gcc -m64 -std=c99 run.c -O2 sample.o merge_sort.o -o run

sample.o: sample.asm
	yasm -f elf64 -g dwarf2 sample.asm -o sample.o

merge_sort.o: merge_sort.c
	gcc -m64 -std=c99 merge_sort.c -O2 -c -o merge_sort.o

clean:
	rm -f sample.o run merge_sort.o

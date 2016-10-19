all: run

run: run.c sample.o
	gcc -m64 -std=c99 run.c -O2 sample.o -o run

sample.o: sample.asm
	yasm -f elf64 -g dwarf2 sample.asm -o sample.o

clean:
	rm -f sample.o run
